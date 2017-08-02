package com.wirusmx.mybudget.model.comparators;

import java.util.Comparator;

/**
 * Notes comparator base class.
 * Notes are compared by one of their fields. If by these
 * field they are equals, they are compared by titles.
 *
 * @param <Note> - class, for which these comparator is used.
 * @author Piunov M (aka WirusMX)
 */
public abstract class MyComparator<Note> implements Comparator<Note> {
    /**
     * Direct sortOrder means sort by increase.
     */
    public static final int DIRECT_ORDER = 1;

    /**
     * Revers sortOrder means sort by decrease.
     */
    public static final int REVERSE_ORDER = -1;

    /**
     * Current sort order.
     */
    int sortOrder;

    MyComparator(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Is used for checking compare sortOrder.
     *
     * @param sortOrder - compare sortOrder. Positive value corresponds to direct sortOrder,
     *              negative value - reverse sortOrder. There are default constants
     *              for this parameter: <code>MyComparator.DIRECT_ORDER</code>
     *              and <code>MyComparator.REVERSE_ORDER</code>.
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Note o1, Note o2) {
        int result = compareNotes(o1, o2);
        if (result == 0) {
            result = compareByItemTitle(o1, o2);
        }

        return result;
    }

    abstract int compareNotes(Note o1, Note o2);

    private int compareByItemTitle(Note o1, Note o2) {
        com.wirusmx.mybudget.model.Note n1 = (com.wirusmx.mybudget.model.Note) o1;
        com.wirusmx.mybudget.model.Note n2 = (com.wirusmx.mybudget.model.Note) o2;
        return n1.getItemTitle().toLowerCase().compareToIgnoreCase(n2.getItemTitle().toLowerCase()) * sortOrder;
    }
}
