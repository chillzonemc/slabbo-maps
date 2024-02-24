package gg.mew.plugins.slabbomaps.command;

import gg.mew.plugins.slabbomaps.shop.Shop;
import lombok.Getter;

import java.util.Comparator;
import java.util.function.Predicate;

@Getter
public enum OrderBy implements Comparator<Shop>, Predicate<Shop> {

    BuyAscending(Comparator.comparingDouble(Shop::getBuyPrice), it -> it.getBuyPrice() != -1),
    BuyDescending(Comparator.comparingDouble(Shop::getBuyPrice).reversed(), it -> it.getBuyPrice() != -1),

    SellAscending(Comparator.comparingDouble(Shop::getSellPrice), it -> it.getSellPrice() != -1),
    SellDescending(Comparator.comparingDouble(Shop::getSellPrice).reversed(), it -> it.getSellPrice() != -1);

    private final Comparator<Shop> comparator;
    private final Predicate<Shop> filter;

    OrderBy(final Comparator<Shop> comparator, final Predicate<Shop> filter) {
        this.comparator = comparator;
        this.filter = filter;
    }

    @Override
    public int compare(final Shop o1, final Shop o2) {
        return this.comparator.compare(o1, o2);
    }

    @Override
    public boolean test(final Shop shop) {
        return this.filter.test(shop);
    }
}
