package com.wirusmx.mybudget.model.comparators;

import com.wirusmx.mybudget.model.Note;

/**
 * Compare Notes by dates.
 *
 * @author Piunov M (aka WirusMX)
 */
public class DateComparator extends MyComparator<Note> {
    public DateComparator(int order) {
        super(order);
    }

    @Override
    int compareNotes(Note o1, Note o2) {
        String date1 = o1.getYear() + "." + o1.getMonth() + "." + o1.getDay();

        String date2 = o2.getYear() + "." + o2.getMonth() + "." + o2.getDay();

        return date1.compareTo(date2) * order;
    }
}
