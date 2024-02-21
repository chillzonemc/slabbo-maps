package gg.mew.plugins.slabbomaps.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import gg.mew.plugins.slabbomaps.SlabboMaps;
import gg.mew.plugins.slabbomaps.shop.Shop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

@CommandAlias("slabbo-maps")
public final class SlabboMapsCommand extends BaseCommand {

    private final SlabboMaps plugin;

    public SlabboMapsCommand(final SlabboMaps plugin) {
        this.plugin = plugin;
    }

    private void giveCompass(Player player, Shop shop) {
        final var compass = new ItemStack(Material.COMPASS, 1);
        final var meta = (CompassMeta) compass.getItemMeta();

        meta.displayName(Component.text(String.format("Tracking %s for $%.2f at %d, %d, %d",
                        shop.getItem().getType(),
                        shop.getBuyPrice(),
                        shop.getLocation().getBlockX(),
                        shop.getLocation().getBlockY(),
                        shop.getLocation().getBlockZ())));

        meta.getPersistentDataContainer().set(this.plugin.getDeleteKey(), PersistentDataType.BOOLEAN, true);

        meta.setLodestone(shop.getLocation());
        meta.setLodestoneTracked(false);

        compass.setItemMeta(meta);

        player.getInventory().addItem(compass);
    }

    @Subcommand("locate item")
    @Syntax("<item>")
    @CommandPermission("slabbomaps.locate.shop")
    public void onLocateItem(final Player player, final Material material, @Default("BuyPriceAscending") final OrderBy orderBy) {
        final var shop = this.plugin.getShopRepository()
                .getShops()
                .stream()
                .filter(it -> it.getItem().getType() == material)
                .min(orderBy);

        if (shop.isPresent()) {
            giveCompass(player, shop.get());

            player.sendMessage(Component.text(String.format("[SlabboMaps] Tracking %s for $%.2f at %d, %d, %d using %s",
                    shop.get().getItem().getType(),
                    shop.get().getBuyPrice(),
                    shop.get().getLocation().getBlockX(),
                    shop.get().getLocation().getBlockY(),
                    shop.get().getLocation().getBlockZ(),
                    orderBy), NamedTextColor.DARK_GREEN));
        } else {
            player.sendMessage(Component.text(String.format("Unable to find shop selling the following item: %s", material), NamedTextColor.DARK_RED));
        }
    }

    @Subcommand("reload")
    @Syntax("")
    @CommandPermission("slabbomaps.reload")
    public void onReload(final CommandSender sender) {
        this.plugin.getShopRepository().load();

        sender.sendMessage(Component.text("[SlabboMaps] Reload complete.", NamedTextColor.DARK_GREEN));
    }

}
