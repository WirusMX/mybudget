package com.wirusmx.mybudget.model.comparators;

import com.wirusmx.mybudget.model.Note;

/**
 * Compare Notes by shops.
 *
 * @author Piunov M (aka WirusMX)
 */
public class ShopComparator extends MyComparator<Note> {
    public ShopComparator(int order) {
        super(order);
    }

    @Override
    int compareNotes(Note o1, Note o2) {
        return o1.getShop().toString().toLowerCase().compareTo(o2.getShop().toString().toLowerCase()) * order;
    }
}
