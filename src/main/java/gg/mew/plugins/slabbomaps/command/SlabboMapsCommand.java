package gg.mew.plugins.slabbomaps.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import gg.mew.plugins.slabbomaps.SlabboMaps;
import gg.mew.plugins.slabbomaps.shop.Shop;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import static net.kyori.adventure.text.Component.*;

@CommandAlias("slabbo-maps")
public final class SlabboMapsCommand extends BaseCommand {

    private final SlabboMaps plugin;

    public SlabboMapsCommand(final SlabboMaps plugin) {
        this.plugin = plugin;
    }

    private boolean giveCompass(Player player, Shop shop) {
        if (player.getInventory().firstEmpty() == -1)
            return false;

        final var compass = new ItemStack(Material.COMPASS, 1);
        final var meta = (CompassMeta) compass.getItemMeta();

        meta.displayName(translatable(shop.getItem().translationKey(), NamedTextColor.YELLOW).appendSpace());

        meta.getPersistentDataContainer().set(this.plugin.getDeleteKey(), PersistentDataType.BOOLEAN, true);

        meta.setLodestone(shop.getLocation());
        meta.setLodestoneTracked(false);

        compass.setItemMeta(meta);

        if (player.getInventory().contains(compass))
            return false;

        player.getInventory().addItem(compass);

        return true;
    }

    //TODO: list ...

    //TODO: locate owner

    // (add helper commands for complex, but common nbt items)

    //TODO: locate book <enchantment> [level]
    //TODO: locate rocket <level>

    //TODO: Should support NBT autocomplete
    @Subcommand("locate item")
    @Syntax("<item>")
    @CommandPermission("slabbomaps.locate.shop")
    @CommandCompletion("@items")
    public void onLocateItem(final Player player, final ItemStack itemStack, @Default("BuyAscending") final OrderBy orderBy) {
        final var shop = this.plugin.getShopRepository()
                .getShops()
                .stream()
                .filter(it -> it.getItem().isSimilar(itemStack))
                .filter(orderBy)
                .min(orderBy);

        if (shop.isPresent()) {
            final var success = giveCompass(player, shop.get());

            if (success) {
                player.sendMessage(text("[SlabboMaps] You have received a compass that will lead the way to", NamedTextColor.GRAY)
                        .appendSpace()
                        .append(empty().color(NamedTextColor.YELLOW).append(text("[").append(translatable(itemStack.translationKey()).append(text("]")))).hoverEvent(itemStack))
                        .append(text(". You can drop the compass to remove it from your inventory.")));

                //TODO: Set player title to distance when holding compass
            } else {
                //NOTE: Note explicitly what the problem is
                player.sendMessage(text("[SlabboMaps] You already have this compass or you have no inventory space.", NamedTextColor.GRAY));
            }
        } else {
            //TODO: Note explicitly when shops are out of stock

            player.sendMessage(text("[SlabboMaps]", NamedTextColor.GRAY)
                    .appendSpace()
                    .append(text("[", NamedTextColor.YELLOW).append(translatable(itemStack.translationKey()).append(text("]"))).hoverEvent(itemStack))
                    .append(text(" is not available in any shop.")));
        }
    }

    @Subcommand("reload")
    @Syntax("")
    @CommandPermission("slabbomaps.reload")
    public void onReload(final CommandSender sender) {
        this.plugin.getShopRepository().load();

        sender.sendMessage(text("[SlabboMaps] Reload complete."));
    }

}
