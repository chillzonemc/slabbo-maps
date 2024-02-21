package gg.mew.plugins.slabbomaps;

import co.aikar.commands.PaperCommandManager;
import gg.mew.plugins.slabbomaps.command.SlabboMapsCommand;
import gg.mew.plugins.slabbomaps.event.SlabboMapsEventListener;
import gg.mew.plugins.slabbomaps.shop.ShopRepository;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
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

        commandManager.registerCommand(new SlabboMapsCommand(this));
    }

    @Override
    public void onDisable() {}

}
