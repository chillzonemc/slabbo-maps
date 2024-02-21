package gg.mew.plugins.slabbomaps.event;

import gg.mew.plugins.slabbomaps.SlabboMaps;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public final class SlabboMapsEventListener implements Listener {

    private final SlabboMaps plugin;

    public SlabboMapsEventListener(final SlabboMaps plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerDropItem(final PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() != Material.COMPASS)
            return;

        //TODO: Doesn't work
        if (!event.getItemDrop().getPersistentDataContainer().has(this.plugin.getDeleteKey()))
            return;

        event.getItemDrop().remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInventoryMoveItem(final InventoryMoveItemEvent event) {
        if (event.getItem().getType() != Material.COMPASS)
            return;

        //TODO: Doesn't work
        if (event.getSource() == event.getDestination())
            return;

        event.setCancelled(true);
    }

}