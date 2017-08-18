package com.wirusmx.mybudget.model;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.managers.DatabaseManager;

import java.util.*;

/**
 * @author Piunov M (aka WirusMX)
 */
public abstract class CommonModel {
    protected DatabaseManager databaseManager;

    public CommonModel(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Extracts pares of (id, title) from <code>table</code>.
     *
     * @param tableName name of table which contains values for ComboBox.
     * @return ComboBox values set with the default order.
     * @see ComboBoxValuesComparator
     */
    public Set<SimpleData> getComboBoxValues(String tableName) {
        Set<SimpleData> values = new TreeSet<>(new ComboBoxValuesComparator());

        try {
            List<SimpleData> temp = databaseManager.getComboBoxValues(tableName);
            values.addAll(temp);
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }

        return values;
    }

    /**
     * Extracts periods of specified type from data base.
     *
     * @param selectedPeriodType type of period.
     * @return set of periods.
     */
    public Set<String> getPeriods(int selectedPeriodType) {
        switch (selectedPeriodType) {
            case Model.PeriodType.YEAR:
                return getYears();
            case Model.PeriodType.MONTH:
                return getMonths();
            case Model.PeriodType.DAY:
                return getDays();
        }
        return new TreeSet<>();
    }

    private Set<String> getYears() {
        List<String> temp = new ArrayList<>();

        try {
            temp = databaseManager.getYears();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }


        return new TreeSet<>(temp).descendingSet();
    }

    private Set<String> getMonths() {
        List<String> temp = new ArrayList<>();

        try {
            temp = databaseManager.getMonths();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return new TreeSet<>(temp).descendingSet();
    }

    private Set<String> getDays() {
        List<String> temp = new ArrayList<>();

        try {
            temp = databaseManager.getDays();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(ex);
        }
        return new TreeSet<>(temp).descendingSet();
    }


    /**
     * Compare two {@link SimpleData} values. Value with <code>id = 0</code>
     * is less then others. If values <code>id != 0</code>, they compare by title.
     */
    private static class ComboBoxValuesComparator implements Comparator<SimpleData> {
        @Override
        public int compare(SimpleData o1, SimpleData o2) {
            if (o1.getId() == 0) {
                return -1;
            }

            if (o2.getId() == 0) {
                return 1;
            }

            return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
        }
    }

    public static class PeriodType {
        public static final int ALL = 0;
        public static final int YEAR = 1;
        public static final int MONTH = 2;
        public static final int DAY = 3;
    }

    @SuppressWarnings("unused")
    public static class SortType {
        public static final int UNDEFINED = -1;
        public static final int DATE = 0;
        public static final int ITEM = 1;
        public static final int TYPE = 2;
        public static final int PRICE = 3;
        public static final int SHOP = 4;
        public static final int BY_SALE = 5;
    }
}
