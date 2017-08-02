package com.wirusmx.mybudget.model.comparators;

import com.wirusmx.mybudget.model.Note;

/**
 * Compare Notes by sales availability.
 *
 * @author Piunov M (aka WirusMX)
 */
public class SaleComparator extends MyComparator<Note> {
    public SaleComparator(int order) {
        super(order);
    }

    @Override
    int compareNotes(Note o1, Note o2) {
        int result = 0;

        if (o1.isBySale() && !o2.isBySale()){
            result = -1;
        }

        if (!o1.isBySale() && o2.isBySale()){
            result = 1;
        }

        return result * sortOrder;
    }
}
