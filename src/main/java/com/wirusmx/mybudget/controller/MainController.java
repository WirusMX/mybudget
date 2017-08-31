package com.wirusmx.mybudget.controller;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.dialogs.noteeditdialog.NoteEditDialog;
import com.wirusmx.mybudget.dialogs.optionsdialog.OptionsDialog;
import com.wirusmx.mybudget.dialogs.plannerdialog.PlannerDialog;
import com.wirusmx.mybudget.dialogs.statisticsdialog.StatisticsDialog;
import com.wirusmx.mybudget.managers.UserSettingsManager;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;
import com.wirusmx.mybudget.view.MainView;
import com.wirusmx.mybudget.view.dataviews.DataView;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Set;

/**
 * @author Piunov M (aka WirusMX)
 */
public class MainController {
    private MainView mainView;
    private Model model;
    private UserSettingsManager userSettingsManager;

    public MainController(UserSettingsManager userSettingsManager) {
        this.userSettingsManager = userSettingsManager;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void startApplication() {
        try {
            model.init();
            mainView.init();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(this, ex);
        }
    }

    public boolean isUseSystemLook(){
        return userSettingsManager.getBooleanValue("main.window.use.system.look", false);
    }

    public boolean isUseColors() {
        return userSettingsManager.getBooleanValue("main.window.use.colors", false);
    }

    public List<Note> getNotes() {
        return model.getNotes();
    }

    private EditMenuListener editMenuListener = null;
    private ViewMenuListener viewMenuListener = null;
    private AddNewNoteButtonActionListener addNewNoteButtonActionListener = null;
    private ExitButtonButtonActionListener exitButtonButtonActionListener = null;
    private StatisticsButtonActionListener statisticsButtonActionListener = null;
    private PlannerButtonActionListener plannerButtonActionListener = null;
    private SettingsButtonActionListener settingsButtonActionListener = null;
    private AboutButtonActionListener aboutButtonActionListener = null;
    private UsingRulesButtonActionListener usingRulesButtonActionListener = null;
    private PeriodTypeComboBoxItemListener periodTypeComboBoxItemListener = null;
    private PeriodComboBoxItemListener periodComboBoxItemListener = null;
    private SortTypeComboBoxItemListener sortTypeComboBoxItemListener = null;
    private SortOrderComboBoxItemListener sortOrderComboBoxItemListener = null;
    private KeyListener searchTextFieldKeyListener = null;
    private SearchButtonActionListener searchButtonActionListener = null;
    private ResetButtonActionListener resetButtonActionListener = null;

    public EditMenuListener getEditMenuListener() {
        if (editMenuListener == null) {
            editMenuListener = new EditMenuListener();
        }

        return editMenuListener;
    }

    public ViewMenuListener getViewMenuListener() {
        if (viewMenuListener == null) {
            viewMenuListener = new ViewMenuListener();
        }

        return viewMenuListener;
    }

    public AddNewNoteButtonActionListener getAddNewNoteButtonActionListener() {
        if (addNewNoteButtonActionListener == null) {
            addNewNoteButtonActionListener = new AddNewNoteButtonActionListener();
        }

        return addNewNoteButtonActionListener;
    }

    public EditNoteActionListener getEditNoteActionListener(DataView dataView) {
        return new EditNoteActionListener(dataView);
    }

    public RemoveNoteButtonActionListener getRemoveNoteButtonActionListener(DataView dataView) {
        return new RemoveNoteButtonActionListener(dataView);
    }

    public ExitButtonButtonActionListener getExitButtonButtonActionListener() {
        if (exitButtonButtonActionListener == null) {
            exitButtonButtonActionListener = new ExitButtonButtonActionListener();
        }

        return exitButtonButtonActionListener;
    }

    public DataViewButtonActionListener getDataViewButtonActionListener(Class dataViewClass, int id) {
        return new DataViewButtonActionListener(dataViewClass, id);
    }

    public StatisticsButtonActionListener getStatisticsButtonActionListener() {
        if (statisticsButtonActionListener == null) {
            statisticsButtonActionListener = new StatisticsButtonActionListener();
        }
        return statisticsButtonActionListener;
    }

    @SuppressWarnings("unused")
    public PlannerButtonActionListener getPlannerButtonActionListener(){
        if (plannerButtonActionListener == null){
            plannerButtonActionListener = new PlannerButtonActionListener();
        }

        return plannerButtonActionListener;
    }

    public SettingsButtonActionListener getSettingsButtonActionListener() {
        if (settingsButtonActionListener == null) {
            settingsButtonActionListener = new SettingsButtonActionListener();
        }
        return settingsButtonActionListener;
    }

    public AboutButtonActionListener getAboutButtonActionListener() {
        if (aboutButtonActionListener == null) {
            aboutButtonActionListener = new AboutButtonActionListener();
        }
        return aboutButtonActionListener;
    }

    public UsingRulesButtonActionListener getUsingRulesButtonActionListener() {
        if (usingRulesButtonActionListener == null) {
            usingRulesButtonActionListener = new UsingRulesButtonActionListener();
        }
        return usingRulesButtonActionListener;
    }

    public PeriodTypeComboBoxItemListener getPeriodTypeComboBoxItemListener(JComboBox<String> periodComboBox) {
        if (periodTypeComboBoxItemListener == null) {
            periodTypeComboBoxItemListener = new PeriodTypeComboBoxItemListener(periodComboBox);
        }
        return periodTypeComboBoxItemListener;
    }

    public PeriodComboBoxItemListener getPeriodComboBoxItemListener() {
        if (periodComboBoxItemListener == null) {
            periodComboBoxItemListener = new PeriodComboBoxItemListener();
        }
        return periodComboBoxItemListener;
    }

    public SortTypeComboBoxItemListener getSortTypeComboBoxItemListener() {
        if (sortTypeComboBoxItemListener == null) {
            sortTypeComboBoxItemListener = new SortTypeComboBoxItemListener();
        }
        return sortTypeComboBoxItemListener;
    }

    public SortOrderComboBoxItemListener getSortOrderComboBoxItemListener() {
        if (sortOrderComboBoxItemListener == null) {
            sortOrderComboBoxItemListener = new SortOrderComboBoxItemListener();
        }
        return sortOrderComboBoxItemListener;
    }

    public KeyListener getSearchTextFieldKeyListener() {
        if (searchTextFieldKeyListener == null) {
            searchTextFieldKeyListener = new SearchTextFieldKeyListener();
        }
        return searchTextFieldKeyListener;
    }

    public SearchButtonActionListener getSearchButtonActionListener() {
        if (searchButtonActionListener == null) {
            searchButtonActionListener = new SearchButtonActionListener();
        }
        return searchButtonActionListener;
    }

    public ResetButtonActionListener getResetButtonActionListener() {
        if (resetButtonActionListener == null) {
            resetButtonActionListener = new ResetButtonActionListener();
        }
        return resetButtonActionListener;
    }

    public int getSelectedPeriodType() {
        return model.getSelectedPeriodType();
    }

    public String getSelectedPeriod() {
        return model.getSelectedPeriod();
    }

    public int getSelectedSortType() {
        return model.getSelectedSortType();
    }

    public int getSelectedSortOrder() {
        return model.getSelectedSortOrder();
    }

    public Set<String> getPeriods() {
        return model.getPeriods();
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(mainView, "ОШИБКА: " + message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public int getDataViewID() {
        return model.getDataViewID();
    }

    public DuplicateNoteActionListener getDuplicateNoteActionListener(DataView currentDataView) {
        return new DuplicateNoteActionListener(currentDataView);
    }

    private class EditMenuListener implements MenuListener {
        @Override
        public void menuSelected(MenuEvent e) {
            if (e.getSource() instanceof JMenu) {
                JMenu menu = (JMenu) e.getSource();
                for (int i = 0; i < menu.getMenuComponentCount(); i++) {
                    Component menuComponent = menu.getMenuComponent(i);
                    if (MainView.EDIT_TAG.equals(menuComponent.getName())) {
                        menuComponent.setEnabled(mainView.isNoteSelected());
                    }
                }
            }
        }

        @Override
        public void menuDeselected(MenuEvent e) {

        }

        @Override
        public void menuCanceled(MenuEvent e) {

        }
    }

    private class ViewMenuListener implements MenuListener {
        @Override
        public void menuSelected(MenuEvent e) {
            if (e.getSource() instanceof JMenu) {
                JMenu menu = (JMenu) e.getSource();
                for (int i = 0; i < menu.getMenuComponentCount(); i++) {
                    Component menuComponent = menu.getMenuComponent(i);
                    menuComponent.setEnabled(!mainView.getCurrentDataViewClass().equals(menuComponent.getName()));
                }
            }
        }

        @Override
        public void menuDeselected(MenuEvent e) {
        }

        @Override
        public void menuCanceled(MenuEvent e) {
        }
    }

    private class AddNewNoteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            NoteEditDialog noteEditDialog = new NoteEditDialog();

            Note note = noteEditDialog.getNote();
            if (noteEditDialog.getDialogResult() == JOptionPane.YES_OPTION && note != null) {
                model.insertNote(note);
                mainView.update();
            }

        }
    }

    private class DuplicateNoteActionListener implements ActionListener {
        private DataView dataView;

        DuplicateNoteActionListener(DataView dataView) {
            this.dataView = dataView;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Note note = dataView.getSelectedValue();
            if (note == null) {
                return;
            }

            NoteEditDialog noteEditDialog = new NoteEditDialog(note);

            note = noteEditDialog.getNote();
            if (noteEditDialog.getDialogResult() == JOptionPane.YES_OPTION && note != null) {
                model.insertNote(note);
                mainView.update();
            }
        }
    }

    private class EditNoteActionListener implements ActionListener, MouseListener {
        private DataView dataView;

        EditNoteActionListener(DataView dataView) {
            this.dataView = dataView;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doEdit();
        }

        private void doEdit() {

            Note note = dataView.getSelectedValue();
            if (note == null) {
                return;
            }

            NoteEditDialog noteEditDialog = new NoteEditDialog(note);

            note = noteEditDialog.getNote();
            if (noteEditDialog.getDialogResult() == JOptionPane.YES_OPTION && note != null) {
                model.updateNote(note);
                mainView.update();
            }

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                doEdit();
            }

            if (SwingUtilities.isRightMouseButton(e)) {
                int row = dataView.locationToIndex(e.getPoint());
                dataView.setSelectedIndex(row);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class RemoveNoteButtonActionListener implements ActionListener {
        private DataView dataView;

        RemoveNoteButtonActionListener(DataView dataView) {
            this.dataView = dataView;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Note note = dataView.getSelectedValue();
            if (note == null) {
                return;
            }

            int result = JOptionPane.showOptionDialog(mainView,
                    "Вы действительно хотите удалить запись?",
                    "Подтвердите удаление",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Да", "Нет"},
                    "Нет");

            if (result == JOptionPane.YES_OPTION) {
                model.removeNote(note);
                mainView.update();
            }
        }
    }

    private class ExitButtonButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.dispose();
        }
    }

    private class DataViewButtonActionListener implements ActionListener {
        private Class dataViewClass;
        private int id;

        DataViewButtonActionListener(Class dataViewClass, int id) {
            this.dataViewClass = dataViewClass;
            this.id = id;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void actionPerformed(ActionEvent e) {
            model.setDataViewID(id);
            mainView.setDataView(dataViewClass);
            mainView.update();
        }
    }

    private class PlannerButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new PlannerDialog();
        }
    }

    private class SettingsButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new OptionsDialog();
        }
    }

    private class StatisticsButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new StatisticsDialog();
        }
    }

    private class AboutButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.showAboutDialog();
        }
    }

    private class UsingRulesButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.showUsingRulesDialog();
        }
    }

    private class PeriodTypeComboBoxItemListener implements ItemListener {

        private JComboBox<String> periodComboBox;

        PeriodTypeComboBoxItemListener(JComboBox<String> periodComboBox) {
            this.periodComboBox = periodComboBox;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox periodTypeComboBox;
            if (e.getSource() instanceof JComboBox) {
                periodTypeComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }

            int periodType = ((SimpleData) periodTypeComboBox.getSelectedItem()).getId();
            model.setSelectedPeriodType(periodType);

            mainView.setPeriodComboBoxValues(periodComboBox, periodType);
            if (periodType == Model.PeriodType.ALL) {
                mainView.update();
            }
        }
    }

    private class PeriodComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox periodComboBox;
            if (e.getSource() instanceof JComboBox) {
                periodComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }

            String selectedPeriod = ((String) periodComboBox.getSelectedItem());
            if (selectedPeriod == null) {
                model.setSelectedPeriod("");
                return;
            }

            model.setSelectedPeriod(selectedPeriod);

            mainView.update();
        }
    }

    private class SortTypeComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox sortTypeComboBox;

            if (e.getSource() instanceof JComboBox) {
                sortTypeComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }


            int selectedSortType = ((SimpleData) sortTypeComboBox.getSelectedItem()).getId();

            model.setSelectedSortType(selectedSortType);
            mainView.update();
        }
    }

    private class SortOrderComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox sortTypeComboBox;

            if (e.getSource() instanceof JComboBox) {
                sortTypeComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }

            int sortOrder = ((SimpleData) sortTypeComboBox.getSelectedItem()).getId();

            model.setSelectedSortOrder(sortOrder);
            mainView.update();
        }
    }

    private class SearchTextFieldKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                mainView.doResetButtonClick();
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER ||
                    userSettingsManager.getBooleanValue("main.window.use.live.search", false)){
                mainView.doSearchButtonClick();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private class SearchButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.setSearchQuery(mainView.getSearchQuery());
            mainView.update();
        }
    }

    private class ResetButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.resetSearchQuery();
            model.setSearchQuery("");
            mainView.update();
        }
    }


}
