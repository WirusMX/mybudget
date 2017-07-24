package com.wirusmx.mybudget.model.comparators;

import com.wirusmx.mybudget.model.Note;

/**
 * Compare Notes by dates.
 */
public class DateComparator extends MyComparator<Note> {
    public DateComparator(int order) {
        super(order);
    }

    @Override
    public int compare(Note o1, Note o2) {
        String date1 = o1.getYear() + ".";
        if (o1.getMonth().length() == 1) {
            date1 += "0" + o1.getMonth() + ".";
        } else {
            date1 += o1.getMonth() + ".";
        }

        if (o1.getDay().length() == 1) {
            date1 += "0" + o1.getDay();
        } else {
            date1 += o1.getDay();
        }

        String date2 = o2.getYear() + ".";
        if (o2.getMonth().length() == 1) {
            date2 += "0" + o2.getMonth() + ".";
        } else {
            date2 += o2.getMonth() + ".";
        }

        if (o2.getDay().length() == 1) {
            date2 += "0" + o2.getDay();
        } else {
            date2 += o2.getDay();
        }

        return date1.compareTo(date2) * order;
    }
}
