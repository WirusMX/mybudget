package com.wirusmx.mybudget.model.comparators;

import com.wirusmx.mybudget.model.Note;

/**
 * Compare Notes by titles.
 */
public class TitlesComparator extends MyComparator<Note> {
    public TitlesComparator(int order) {
        super(order);
    }

    @Override
    public int compare(Note o1, Note o2) {
        return o1.getItem().compareToIgnoreCase(o2.getItem()) * order;
    }
}
