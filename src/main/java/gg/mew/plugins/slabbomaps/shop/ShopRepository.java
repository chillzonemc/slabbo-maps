package gg.mew.plugins.slabbomaps.shop;

import gg.mew.plugins.slabbomaps.SlabboMaps;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public final class ShopRepository {

    private final SlabboMaps plugin;

    @Getter
    private final List<Shop> shops = new ArrayList<>();

    public ShopRepository(final SlabboMaps plugin) {
        this.plugin = plugin;
    }


    public void init() {
        this.load();
    }

    public void load() {
        this.shops.clear();

        final var config = YamlConfiguration.loadConfiguration(new File(this.plugin.getSlabboDataDir(), "shops.yml"));
        final var section = Objects.requireNonNull(config.getConfigurationSection("shops"));

        final var shops = section.getValues(false);

        for (final var shop : shops.values()) {
            this.shops.add(Shop.fromObject(shop));
        }
    }

}
