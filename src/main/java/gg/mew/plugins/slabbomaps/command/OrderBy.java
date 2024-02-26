package gg.mew.plugins.slabbomaps.command;

import gg.mew.plugins.slabbomaps.shop.Shop;
import lombok.Getter;

import java.util.Comparator;
import java.util.function.Predicate;

@Getter
public enum OrderBy implements Comparator<Shop>, Predicate<Shop> {

    BuyAscending(Comparator.comparingDouble(it -> it.getBuyPrice() / it.getQuantity()), it -> it.getBuyPrice() != -1 && it.getQuantity() > 0),
    BuyDescending(Comparator.<Shop>comparingDouble(it -> it.getBuyPrice() / it.getQuantity()).reversed(), it -> it.getBuyPrice() != -1 && it.getQuantity() > 0),

    SellAscending(Comparator.comparingDouble(it -> it.getSellPrice() / it.getQuantity()), it -> it.getSellPrice() != -1 && it.getQuantity() > 0),
    SellDescending(Comparator.<Shop>comparingDouble(it -> it.getSellPrice() / it.getQuantity()).reversed(), it -> it.getSellPrice() != -1 && it.getQuantity() > 0);

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
