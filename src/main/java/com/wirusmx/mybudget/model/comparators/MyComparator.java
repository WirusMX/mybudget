package com.wirusmx.mybudget.model.comparators;

import java.util.Comparator;

public abstract class MyComparator<Note> implements Comparator<Note> {

    public static final int DIRECT_ORDER = 1;
    public static final int REVERSE_ORDER = -1;

    int order;

    public MyComparator(int order) {
        this.order = order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public abstract int compare(Note o1, Note o2);
}
