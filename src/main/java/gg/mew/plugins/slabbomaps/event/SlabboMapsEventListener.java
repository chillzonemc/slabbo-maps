package gg.mew.plugins.slabbomaps.event;

import gg.mew.plugins.slabbomaps.SlabboMaps;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public final class SlabboMapsEventListener implements Listener {

    private final SlabboMaps plugin;

    public SlabboMapsEventListener(final SlabboMaps plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        if (!this.plugin.isRestrictedItem(event.getItemDrop().getItemStack()))
            return;

        event.getItemDrop().remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.DROP_ONE_SLOT || event.getAction() == InventoryAction.DROP_ALL_CURSOR || event.getAction() == InventoryAction.DROP_ALL_SLOT || event.getAction() == InventoryAction.DROP_ONE_CURSOR)
            return;

        if (this.plugin.isRestrictedItem(event.getCursor()) && event.getClickedInventory() != event.getWhoClicked().getInventory()
                || event.isShiftClick() && this.plugin.isRestrictedItem(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        if (event.getInventory() == event.getWhoClicked().getInventory())
            return;

        if (this.plugin.isRestrictedItem(event.getOldCursor()))
            event.setCancelled(true);
    }

}