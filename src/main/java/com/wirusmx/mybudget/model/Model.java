package com.wirusmx.mybudget.model;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.controller.MainController;
import com.wirusmx.mybudget.managers.DatabaseManager;
import com.wirusmx.mybudget.managers.UserSettingsManager;
import com.wirusmx.mybudget.model.comparators.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Piunov M (aka WirusMX)
 */
public class Model extends CommonModel {
    private MainController controller;
    private UserSettingsManager userSettingsManager;

    private MyComparator[] comparators;

    private int selectedPeriodType = PeriodType.ALL;
    private String selectedPeriod = "";
    private int selectedSortType = SortType.DATE;
    private int selectedSortOrder = MyComparator.REVERSE_ORDER;
    private String searchQuery = "";
    private int dataViewID = 0;

    public Model(MainController controller, DatabaseManager databaseManager, UserSettingsManager userSettingsManager) {
        super(databaseManager);
        this.controller = controller;
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

    public void init() {
        loadUserSettings();

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
    private List<Note> getNotes(int selectedPeriodType, String selectedPeriod, int selectedSortType, String searchQuery) {
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
}

