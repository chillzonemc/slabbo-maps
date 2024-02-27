package gg.mew.plugins.slabbomaps;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import gg.mew.plugins.slabbomaps.command.SlabboMapsCommand;
import gg.mew.plugins.slabbomaps.event.SlabboMapsEventListener;
import gg.mew.plugins.slabbomaps.shop.Shop;
import gg.mew.plugins.slabbomaps.shop.ShopRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import me.angeschossen.lands.api.LandsIntegration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@Getter
public final class SlabboMaps extends JavaPlugin {

    private final ShopRepository shopRepository = new ShopRepository(this);

    @Getter(AccessLevel.NONE)
    private LandsIntegration landsIntegration;

    private final NamespacedKey deleteKey = new NamespacedKey(this, "delete");

    private File slabboDataDir;

    @Override
    @SneakyThrows
    public void onEnable() {
        if (this.getServer().getPluginManager().isPluginEnabled("Lands"))
            this.landsIntegration = LandsIntegration.of(this);

        final var slabbo = this.getServer().getPluginManager().getPlugin("Slabbo");

        this.slabboDataDir = Objects.requireNonNull(slabbo).getDataFolder();

        this.shopRepository.init();

        this.getServer().getPluginManager().registerEvents(new SlabboMapsEventListener(this), this);

        final var commandManager = new PaperCommandManager(this);

        commandManager.getCommandContexts().registerContext(ItemStack.class, c -> {
            final var item = c.popFirstArg();
            try {
                return Bukkit.getItemFactory().createItemStack(item);
            } catch (IllegalArgumentException e) {
                throw new InvalidCommandArgument(String.format("Unknown item '%s'", item), false);
            }
        });

        commandManager.getCommandCompletions().registerStaticCompletion("items", Arrays.stream(Material.values()).map(it -> it.getKey().asString()).toArray(String[]::new));

        commandManager.registerCommand(new SlabboMapsCommand(this));
    }

    @Override
    public void onDisable() {}

    public boolean isRestrictedItem(final ItemStack itemStack) {
        return itemStack != null && itemStack.getItemMeta() != null && itemStack.getItemMeta().getPersistentDataContainer().has(this.getDeleteKey());
    }

    public boolean isInShoppingDistrict(final Shop shop) {
        if (this.landsIntegration == null)
            return true;

        final var location = shop.getLocation();
        final var chunk = location.getChunk();

        //NOTE: getLandByChunk is preferred according to the api, but there is no guarantee the chunk is loaded, thus this.
        final var land = this.landsIntegration.getLandByUnloadedChunk(location.getWorld(), chunk.getX(), chunk.getZ());

        //TODO: Support shops outside of shopping district, different dimensions, etc...
        return land != null && land.getName().equalsIgnoreCase("spawn");
    }

}
