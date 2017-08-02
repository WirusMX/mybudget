package com.wirusmx.mybudget.model.comparators;

import com.wirusmx.mybudget.model.Note;

/**
 * Compare Notes by items type.
 *
 * @author Piunov M (aka WirusMX)
 */
public class ItemTypeComparator extends MyComparator<Note> {
    public ItemTypeComparator(int order) {
        super(order);
    }

    @Override
    int compareNotes(Note o1, Note o2) {
        return o1.getType().toString().toLowerCase().compareTo(o2.getType().toString().toLowerCase()) * sortOrder;
    }
}
