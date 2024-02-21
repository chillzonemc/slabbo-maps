package gg.mew.plugins.slabbomaps.command;

import gg.mew.plugins.slabbomaps.shop.Shop;
import lombok.Getter;

import java.util.Comparator;

@Getter
public enum OrderBy implements Comparator<Shop> {

    BuyPriceAscending(Comparator.comparingDouble(Shop::getBuyPrice)),
    BuyPriceDescending(Comparator.comparingDouble(Shop::getBuyPrice).reversed()),

    SellPriceAscending(Comparator.comparingDouble(Shop::getSellPrice)),
    SellPriceDescending(Comparator.comparingDouble(Shop::getSellPrice).reversed()),

    QuantityAscending(Comparator.comparingDouble(Shop::getQuantity)),
    QuantityDescending(Comparator.comparingDouble(Shop::getQuantity).reversed());

    private final Comparator<Shop> comparator;

    OrderBy(final Comparator<Shop> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(final Shop o1, final Shop o2) {
        return this.comparator.compare(o1, o2);
    }

}
