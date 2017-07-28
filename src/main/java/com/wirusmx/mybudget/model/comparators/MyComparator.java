package com.wirusmx.mybudget.model.comparators;

import java.util.Comparator;

/**
 * Notes comparator base class.
 *
 * @param <Note>
 * @author Piunov M (aka WirusMX)
 */
public abstract class MyComparator<Note> implements Comparator<Note> {

    public static final int DIRECT_ORDER = 1;
    public static final int REVERSE_ORDER = -1;

    int order;

    public MyComparator(int order) {
        this.order = order;
    }

    /**
     * Is used for checking compare order.
     *
     * @param order - compare order. Positive value corresponds to direct order,
     *              negative value - reverse order. There are default constants
     *              for this parameter: <code>MyComparator.DIRECT_ORDER</code>
     *              and <code>MyComparator.REVERSE_ORDER</code>.
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public abstract int compare(Note o1, Note o2);
}
