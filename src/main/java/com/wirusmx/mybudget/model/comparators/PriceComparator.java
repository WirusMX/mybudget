package com.wirusmx.mybudget.model.comparators;

import com.wirusmx.mybudget.model.Note;

/**
 * Compare Notes by prices.
 *
 * @author Piunov M (aka WirusMX)
 */
public class PriceComparator extends MyComparator<Note> {
    public PriceComparator(int order) {
        super(order);
    }

    @Override
    int compareNotes(Note o1, Note o2) {
        return (int)((o1.getPrice() - o2.getPrice()) * 100) * sortOrder;
    }
}
