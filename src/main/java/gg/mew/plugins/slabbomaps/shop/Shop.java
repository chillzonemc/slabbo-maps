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
    private Location location;
    private ItemStack item;
    private UUID ownerId;
    private int stock;
    private int quantity;

    @SneakyThrows
    public static Shop fromObject(final Object obj) {
        //TODO: Bukkit's API does not allow me to deserialize into my Shop class, I couldn't do it manually, and I don't want to pull in an extra dependency.
        //TODO: It's still reflection though, so I should probably change it in the future.

        final var shop = new Shop();
        final var shopClass = obj.getClass();

        shop.buyPrice = shopClass.getDeclaredField("buyPrice").getDouble(obj);
        shop.sellPrice = shopClass.getDeclaredField("sellPrice").getDouble(obj);
        shop.location = (Location) shopClass.getDeclaredField("location").get(obj);
        shop.item = (ItemStack) shopClass.getDeclaredField("item").get(obj);
        shop.ownerId = (UUID) shopClass.getDeclaredField("ownerId").get(obj);
        shop.stock = shopClass.getDeclaredField("stock").getInt(obj);
        shop.quantity = shopClass.getDeclaredField("quantity").getInt(obj);

        return shop;
    }

}
