package com.wirusmx.mybudget.model;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.controller.MainController;
import com.wirusmx.mybudget.managers.DatabaseManager;
import com.wirusmx.mybudget.managers.UserSettingsManager;
import com.wirusmx.mybudget.model.comparators.*;

import java.util.*;

/**
 * @author Piunov M (aka WirusMX)
 */
public class Model {
    private MainController controller;
    private DatabaseManager databaseManager;
    private UserSettingsManager userSettingsManager;

    private MyComparator[] comparators;

    private int selectedPeriodType = PeriodType.ALL;
    private String selectedPeriod = "";
    private int selectedSortType = SortType.DATE;
    private int selectedSortOrder = MyComparator.REVERSE_ORDER;
    private String searchQuery = "";
    private int dataViewID = 0;

    public Model(MainController controller, DatabaseManager databaseManager, UserSettingsManager userSettingsManager) {
        this.controller = controller;
        this.databaseManager = databaseManager;
        this.userSettingsManager = userSettingsManager;
    }

    public int getSelectedPeriodType() {
        return selectedPeriodType;
    }

    public void setSelectedPeriodType(int selectedPeriodType) {
        this.selectedPeriodType = selectedPeriodType;
    }

    public String getSelectedPeriod() {
        return selectedPeriod;
    }

    public void setSelectedPeriod(String selectedPeriod) {
        this.selectedPeriod = selectedPeriod;
        userSettingsManager.setValue("main.window.period.type", "" + selectedPeriodType);
        userSettingsManager.setValue("main.window.period", selectedPeriod);
    }

    public int getSelectedSortType() {
        return selectedSortType;
    }

    public void setSelectedSortType(int selectedSortType) {
        this.selectedSortType = selectedSortType;
        userSettingsManager.setValue("main.window.sort.type", "" + selectedSortType);
    }

    public int getSelectedSortOrder() {
        return selectedSortOrder;
    }

    public void setSelectedSortOrder(int selectedSortOrder) {
        this.selectedSortOrder = selectedSortOrder;
        userSettingsManager.setValue("main.window.sort.order", "" + selectedSortOrder);
        for (MyComparator c : comparators) {
            c.setSortOrder(selectedSortOrder);
        }
    }

    public int getDataViewID() {
        return dataViewID;
    }

    public void setDataViewID(int dataViewID) {
        this.dataViewID = dataViewID;
        userSettingsManager.setValue("main.window.data.view.id", "" + dataViewID);
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    /**
     * Method initializes and inspects database, loads user settings from *.properties file,
     * initializes comparators, which are used for sorting Notes. <br>
     * <br>
     * It is recommended to use <code>init(false) in test scope.
     *
     * @see MyComparator
     */
    public void init() {
        init(true);
    }

    /**
     * @param loadUserSettings if parameter is <code>true</code>,then user settings
     *                         will be loaded from *.properties file,
     *                         otherwise user settings will have default values.
     * @see MyComparator
     */
    void init(boolean loadUserSettings) {
        if (loadUserSettings) {
            loadUserSettings();
        }

        comparators = new MyComparator[]{
                new DateComparator(selectedSortOrder),
                new TitlesComparator(selectedSortOrder),
                new ItemTypeComparator(selectedSortOrder),
                new PriceComparator(selectedSortOrder),
                new ShopComparator(selectedSortOrder),
                new SaleComparator(selectedSortOrder)
        };
    }

    /**
     * Extracts Notes from database which matches the previously
     * specified period and search query.
     *
     * @return List of Notes which matches the specified period and
     * search query, sorted by previously specified way.
     */
    public List<Note> getNotes() {
        return getNotes(selectedPeriodType, selectedPeriod, selectedSortType, searchQuery);
    }

    /**
     * Extracts periods of previously specified type from data base.
     *
     * @return set of periods.
     */
    public Set<String> getPeriods() {
        return getPeriods(selectedPeriodType);
    }

    /**
     * Extracts periods of specified type from data base.
     *
     * @param selectedPeriodType type of period.
     * @return set of periods.
     */
    Set<String> getPeriods(int selectedPeriodType) {
        switch (selectedPeriodType) {
            case PeriodType.YEAR:
                return getYears();
            case PeriodType.MONTH:
                return getMonths();
            case PeriodType.DAY:
                return getDays();
        }
        return new TreeSet<>();
    }

    /**
     * Loads user settings from *.properties file.
     */
    private void loadUserSettings() {
        selectedPeriodType = userSettingsManager.getIntegerValue("main.window.period.type", selectedPeriodType);
        selectedPeriod = userSettingsManager.getValue("main.window.period", selectedPeriod);
        selectedSortType = userSettingsManager.getIntegerValue("main.window.sort.type", selectedSortType);
        selectedSortOrder = userSettingsManager.getIntegerValue("main.window.sort.order", selectedSortOrder);
        dataViewID = userSettingsManager.getIntegerValue("main.window.data.view.id", dataViewID);
    }

    @SuppressWarnings("unchecked")
    List<Note> getNotes(int selectedPeriodType, String selectedPeriod, int selectedSortType, String searchQuery) {
        List<Note> result = new ArrayList<>();

        try {
            result = databaseManager.getNotes(selectedPeriodType, selectedPeriod);
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(controller, ex, "Ошибка базы данных!");
        }

        if (selectedSortType >= 0 && selectedSortType < comparators.length) {
            Collections.sort(result, comparators[selectedSortType]);
        }

        if (!"".equals(searchQuery)) {
            List<Note> temp = new ArrayList<>();
            String sq = searchQuery.toLowerCase();

            for (Note n : result) {
                if (n.getItemTitle().toLowerCase().contains(sq)) {
                    temp.add(n);
                }
            }

            result = temp;
        }

        return result;
    }

    /**
     * Extracts pares of (id, title) from <code>table</code>.
     *
     * @param tableName name of table which contains values for ComboBox.
     * @return ComboBox values set with the default order.
     * @see Model.ComboBoxValuesComparator
     */
    Set<SimpleData> getComboBoxValues(String tableName) {
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
     * Insert new Note to database.
     *
     * @param note Note for insertion.
     * @throws RuntimeException if Note.itemTitle is empty.
     */
    public void insertNote(Note note) {
        try {
            if (note.getItemTitle().isEmpty()) {
                throw new RuntimeException("Empty item title");
            }

            databaseManager.insertNote(note);
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(controller, ex, "Ошибка базы данных!");
        }
    }

    /**
     * Updates Note in database.
     *
     * @param note Note for updating.
     */
    public void updateNote(Note note) {
        try {
            databaseManager.updateNote(note);
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(controller, ex, "Ошибка базы данных!");
        }
    }

    /**
     * Removes Note from database.
     *
     * @param note Note for removing.
     */
    public void removeNote(Note note) {
        try {
            databaseManager.removeNote(note);
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(controller, ex, "Ошибка базы данных!");
        }
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

