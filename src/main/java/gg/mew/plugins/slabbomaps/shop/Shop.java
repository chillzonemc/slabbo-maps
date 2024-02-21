package gg.mew.plugins.slabbomaps.shop;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public final class Shop {

    private double buyPrice;
    private double sellPrice;
    private int quantity;
    private Location location;
    private ItemStack item;
    private UUID ownerId;

    @SneakyThrows
    public static Shop fromObject(final Object obj) {
        final var shop = new Shop();
        final var shopClass = obj.getClass();

        shop.buyPrice = shopClass.getDeclaredField("buyPrice").getDouble(obj);
        shop.sellPrice = shopClass.getDeclaredField("sellPrice").getDouble(obj);
        shop.quantity = shopClass.getDeclaredField("quantity").getInt(obj);
        shop.location = (Location) shopClass.getDeclaredField("location").get(obj);
        shop.item = (ItemStack) shopClass.getDeclaredField("item").get(obj);
        shop.ownerId = (UUID) shopClass.getDeclaredField("ownerId").get(obj);

        return shop;
    }

}
