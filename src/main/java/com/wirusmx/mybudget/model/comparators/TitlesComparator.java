package com.wirusmx.mybudget.model.comparators;

import com.wirusmx.mybudget.model.Note;

/**
 * Compare Notes by titles.
 *
 * @author Piunov M (aka WirusMX)
 */
public class TitlesComparator extends MyComparator<Note> {
    public TitlesComparator(int order) {
        super(order);
    }

    @Override
    int compareNotes(Note o1, Note o2) {
        return 0;
    }
}
