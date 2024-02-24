package gg.mew.plugins.slabbomaps;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import gg.mew.plugins.slabbomaps.command.SlabboMapsCommand;
import gg.mew.plugins.slabbomaps.event.SlabboMapsEventListener;
import gg.mew.plugins.slabbomaps.shop.ShopRepository;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
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

    private final NamespacedKey deleteKey = new NamespacedKey(this, "delete");

    private File slabboDataDir;

    @Override
    @SneakyThrows
    public void onEnable() {
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

}
