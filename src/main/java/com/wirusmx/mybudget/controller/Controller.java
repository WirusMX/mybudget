package com.wirusmx.mybudget.controller;

import com.wirusmx.mybudget.DefaultExceptionHandler;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;
import com.wirusmx.mybudget.view.View;
import com.wirusmx.mybudget.view.dataviews.DataView;
import com.wirusmx.mybudget.view.dialogs.NoteEditDialog;
import com.wirusmx.mybudget.view.dialogs.StatisticsDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Set;

/**
 * @author Piunov M (aka WirusMX)
 */
public class Controller {
    private Model model;
    private View view;

    public void setModel(Model model) {
        this.model = model;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void startApplication() {
        try {
            model.init();
            view.init();
        } catch (Exception ex) {
            DefaultExceptionHandler.handleException(this, ex);
        }
    }

    public Set<SimpleData> getComboBoxValues(String tableName) {
        return model.getComboBoxValues(tableName);
    }


    public List<Note> getNotes() {
        return model.getNotes();
    }

    private AddNewNoteButtonActionListener addNewNoteButtonActionListener = null;
    private ExitButtonButtonActionListener exitButtonButtonActionListener = null;
    private StatisticsButtonActionListener statisticsButtonActionListener = null;
    private SettingsButtonActionListener settingsButtonActionListener = null;
    private AboutButtonActionListener aboutButtonActionListener = null;
    private UsingRulesButtonActionListener usingRulesButtonActionListener = null;
    private PeriodTypeComboBoxItemListener periodTypeComboBoxItemListener = null;
    private PeriodComboBoxItemListener periodComboBoxItemListener = null;
    private SortTypeComboBoxItemListener sortTypeComboBoxItemListener = null;
    private SortOrderComboBoxItemListener sortOrderComboBoxItemListener = null;
    private SearchButtonActionListener searchButtonActionListener = null;
    private ResetButtonActionListener resetButtonActionListener = null;

    public AddNewNoteButtonActionListener getAddNewNoteButtonActionListener() {
        if (addNewNoteButtonActionListener == null) {
            addNewNoteButtonActionListener = new AddNewNoteButtonActionListener(this);
        }

        return addNewNoteButtonActionListener;
    }

    public EditNoteActionListener getEditNoteActionListener(DataView dataView) {
        return new EditNoteActionListener(this, dataView);
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
            statisticsButtonActionListener = new StatisticsButtonActionListener(this);
        }
        return statisticsButtonActionListener;
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

    public SearchButtonActionListener getSearchButtonActionListener(JTextField textField) {
        if (searchButtonActionListener == null) {
            searchButtonActionListener = new SearchButtonActionListener(textField);
        }
        return searchButtonActionListener;
    }

    public ResetButtonActionListener getResetButtonActionListener(JTextField textField) {
        if (resetButtonActionListener == null) {
            resetButtonActionListener = new ResetButtonActionListener(textField);
        }
        return resetButtonActionListener;
    }

    public ItemsTitleTextFieldKeyListener getItemsTitleTextFieldKeyListener(JComboBox<String> itemsComboBox) {
        return new ItemsTitleTextFieldKeyListener(itemsComboBox);
    }

    public ItemsComboBoxItemListener getItemsComboBoxItemListener(JTextField textField) {
        return new ItemsComboBoxItemListener(textField);
    }

    public NumericTextFieldFocusListener getNumericTextFieldFocusListener(Class type, String defaultValue, int cutNum) {
        return new NumericTextFieldFocusListener(type, defaultValue, cutNum);
    }

    public TotalCalcTextFieldFocusListener getTotalCalcTextFieldFocusListener(JTextField priceTextField, JTextField countTextField) {
        return new TotalCalcTextFieldFocusListener(priceTextField, countTextField);
    }

    public SelectAllTextFocusListener getSelectAllTextFocusListener() {
        return new SelectAllTextFocusListener();
    }

    public CountTypeComboBoxItemListener getCountTypeComboBoxItemListener(JLabel label) {
        return new CountTypeComboBoxItemListener(label);
    }

    public NoteEditDialogComboBoxItemListener getNoteEditDialogComboBoxItemListener(JDialog dialog, String table) {
        return new NoteEditDialogComboBoxItemListener(dialog, table);
    }


    public SaveNoteButtonActionListener getSaveNoteButtonActionListener(
            NoteEditDialog dialog,
            Note note,
            JTextField itemNameTextField,
            JComboBox itemTypeComboBox,
            JTextField itemPriceTextField,
            JTextField itemCountTextField,
            JComboBox countTypeComboBox,
            JComboBox shopComboBox,
            JComboBox necessityLevelComboBox,
            JComboBox qualityLevelComboBox,
            JCheckBox saleCheckBox,
            JTextField dayTextField,
            JTextField monthTextField,
            JTextField yearTextField
    ) {
        return new SaveNoteButtonActionListener(
                dialog,
                note,
                itemNameTextField,
                itemTypeComboBox,
                itemPriceTextField,
                itemCountTextField,
                countTypeComboBox,
                shopComboBox,
                necessityLevelComboBox,
                qualityLevelComboBox,
                saleCheckBox,
                dayTextField,
                monthTextField,
                yearTextField);

    }

    public CloseNoteEditDialogButtonActionListener getCloseDialogButtonActionListener(NoteEditDialog dialog, Note note) {
        return new CloseNoteEditDialogButtonActionListener(dialog, note);
    }

    public StatisticsDialogPeriodComboBoxItemListener getStatisticsDialogPeriodComboBoxItemListener(StatisticsDialog dialog) {
        return new StatisticsDialogPeriodComboBoxItemListener(dialog);
    }

    public StatisticsDialogItemTypeComboBoxItemListener getStatisticsDialogItemTypeComboBoxItemListener(StatisticsDialog dialog) {
        return new StatisticsDialogItemTypeComboBoxItemListener(dialog);
    }

    public CloseStatisticsDialogButtonActionListener getCloseStatisticsDialogButtonActionListener(StatisticsDialog dialog) {
        return new CloseStatisticsDialogButtonActionListener(dialog);
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

    public Set<String> getPeriods(int periodType) {
        return model.getPeriods(periodType);
    }

    public ImageIcon getImage(String fileName) {
        return model.getImage(fileName);
    }

    public float[][] getStatistics(int periodType, String period, int itemType) {
        float[][] result;
        if (periodType == Model.PeriodType.YEAR) {
            result = new float[3][12];

            List<Note> notes = model.getNotes(periodType, period);

            for (Note n : notes) {
                if (itemType == -1 || n.getType().getId() == itemType) {
                    result[2][Integer.parseInt(n.getMonth()) - 1] += n.getTotal();
                    result[n.getNecessity().getId()][Integer.parseInt(n.getMonth()) - 1] += n.getTotal();
                }
            }
        } else {
            result = new float[3][0];
        }

        return result;
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(view, "ОШИБКА: " + message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public int getDataViewID() {
        return model.getDataViewID();
    }

    private int insertNewValue(String value, String table) {
        return model.insertNewComboBoxValue(value, table);
    }

    private class AddNewNoteButtonActionListener implements ActionListener {
        private Controller controller;

        AddNewNoteButtonActionListener(Controller controller) {
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NoteEditDialog noteEditDialog = new NoteEditDialog(controller);
            Note note = noteEditDialog.getNote();
            if (noteEditDialog.getDialogResult() == JOptionPane.YES_OPTION && note != null) {
                model.insertNote(note);
                view.update();
            }
        }
    }

    private class EditNoteActionListener implements ActionListener, MouseListener {
        private Controller controller;
        private DataView dataView;

        EditNoteActionListener(Controller controller, DataView dataView) {
            this.controller = controller;
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
            NoteEditDialog noteEditDialog = new NoteEditDialog(controller, note);
            note = noteEditDialog.getNote();
            if (noteEditDialog.getDialogResult() == JOptionPane.YES_OPTION && note != null) {
                model.updateNote(note);
                view.update();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                doEdit();
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

            model.removeNote(note);
            view.update();
        }
    }


    private class ExitButtonButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
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
            view.setDataView(dataViewClass);
            view.update();
        }
    }

    private class SettingsButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class StatisticsButtonActionListener implements ActionListener {
        private Controller controller;

        StatisticsButtonActionListener(Controller controller) {
            this.controller = controller;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new StatisticsDialog(controller);
        }
    }

    private class AboutButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JLabel messageLabel = new JLabel(model.readTextFromFile("about.txt"));
            messageLabel.setFont(new Font("Monospased", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(view,
                    messageLabel,
                    "О программе", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private class UsingRulesButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JLabel messageLabel = new JLabel(model.readTextFromFile("using_rules.txt"));
            messageLabel.setFont(new Font("Monospased", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(messageLabel);
            scrollPane.setPreferredSize(new Dimension(600, 500));

            JOptionPane.showMessageDialog(view,
                    scrollPane,
                    "Правила пользования ПО",
                    JOptionPane.PLAIN_MESSAGE);
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

            view.setPeriodComboBoxValues(periodComboBox, periodType);
            if (periodType == Model.PeriodType.ALL) {
                view.update();
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

            view.update();
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
            view.update();
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
            view.update();
        }
    }

    private class SearchButtonActionListener implements ActionListener {

        private JTextField searchTextField;

        SearchButtonActionListener(JTextField searchTextField) {
            this.searchTextField = searchTextField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            model.setSearchQuery(searchTextField.getText());
            view.update();
        }
    }

    private class ResetButtonActionListener implements ActionListener {
        JTextField textField;

        ResetButtonActionListener(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            textField.setText("");
            model.setSearchQuery("");
            view.update();
        }
    }

    private class ItemsTitleTextFieldKeyListener implements KeyListener {

        private JComboBox<String> itemsComboBox;

        ItemsTitleTextFieldKeyListener(JComboBox<String> itemsComboBox) {
            this.itemsComboBox = itemsComboBox;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            JTextField textField;

            if (e.getSource() instanceof JTextField) {
                textField = (JTextField) e.getSource();
            } else {
                return;
            }

            if (itemsComboBox.isPopupVisible()) {
                itemsComboBox.hidePopup();
            }

            itemsComboBox.removeAllItems();

            if (textField.getText().length() < 2) {
                return;
            }

            Set<String> items = model.getItemsSet();


            for (String s : items) {
                if (s.toLowerCase().contains(textField.getText().toLowerCase())) {
                    itemsComboBox.addItem(s);
                }
            }

            if (itemsComboBox.getItemCount() > 0) {
                itemsComboBox.setSelectedIndex(-1);
                itemsComboBox.showPopup();
            }

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getExtendedKeyCode() == KeyEvent.VK_DOWN) {
                itemsComboBox.requestFocusInWindow();
            }
        }
    }

    private class ItemsComboBoxItemListener implements ItemListener {

        JTextField itemTextField;

        ItemsComboBoxItemListener(JTextField itemTextField) {
            this.itemTextField = itemTextField;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox) || e.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }

            JComboBox comboBox = (JComboBox) e.getSource();

            if (comboBox.isPopupVisible()) {
                itemTextField.setText((String) comboBox.getSelectedItem());
                itemTextField.requestFocusInWindow();
            }

        }
    }

    private class NumericTextFieldFocusListener implements FocusListener {
        private String defaultValue;
        private Class type;
        int cutNum;

        NumericTextFieldFocusListener(Class type, String defaultValue, int cutNum) {
            this.defaultValue = defaultValue;
            this.type = type;
            this.cutNum = cutNum;
        }

        @Override
        public void focusGained(FocusEvent e) {

        }

        @Override
        public void focusLost(FocusEvent e) {
            if (!(e.getSource() instanceof JTextField)) {
                return;
            }

            JTextField textField = (JTextField) e.getSource();

            String text = textField.getText();

            text = model.stringToNumericFormat(type, text, cutNum);

            if (text.length() == 0) {
                text = defaultValue;
            }

            textField.setText(text);
        }
    }

    private class TotalCalcTextFieldFocusListener implements FocusListener {
        private JTextField priceTextField;
        private JTextField countTextField;

        TotalCalcTextFieldFocusListener(JTextField priceTextField, JTextField countTextField) {
            this.priceTextField = priceTextField;
            this.countTextField = countTextField;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (!(e.getSource() instanceof JTextField)) {
                return;
            }

            float price;
            float count;

            try {
                price = Float.parseFloat(priceTextField.getText());
                count = Float.parseFloat(countTextField.getText());
            } catch (Exception ex) {
                DefaultExceptionHandler.handleException(ex);
                return;
            }

            JTextField totalTextField = (JTextField) e.getSource();

            if (price > 0f && count > 0f) {
                totalTextField.setText(String.format(Note.PRICE_FORMAT, price * count));
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            if (!(e.getSource() instanceof JTextField)) {
                return;
            }

            JTextField totalTextField = (JTextField) e.getSource();


            float price;
            float count;
            float total;

            try {
                price = Float.parseFloat(priceTextField.getText());
                count = Float.parseFloat(countTextField.getText());
                total = Float.parseFloat(totalTextField.getText());
            } catch (Exception ex) {
                DefaultExceptionHandler.handleException(ex);
                return;
            }

            if (price == 0f && count > 0f) {
                priceTextField.setText(String.format(Note.PRICE_FORMAT, total / count));
            }

            if (price > 0f && count == 0f){
                countTextField.setText(String.format(Note.COUNT_FORMAT, total / price));
            }

        }
    }

    private class SelectAllTextFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent e) {
            if (e.getSource() instanceof JTextField) {
                ((JTextField) e.getSource()).selectAll();
            }
        }

        @Override
        public void focusLost(FocusEvent e) {

        }
    }

    private class CountTypeComboBoxItemListener implements ItemListener {
        private JLabel label;
        private String prefix = "Цена (руб./";

        CountTypeComboBoxItemListener(JLabel label) {
            this.label = label;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            label.setText(prefix + e.getItem().toString() + "):");
        }
    }

    private class NoteEditDialogComboBoxItemListener implements ItemListener {
        JDialog dialog;
        private String table;

        NoteEditDialogComboBoxItemListener(JDialog dialog, String table) {
            this.dialog = dialog;
            this.table = table;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (!(e.getSource() instanceof JComboBox)) {
                return;
            }

            JComboBox<SimpleData> cb = (JComboBox<SimpleData>) e.getSource();

            if (!(e.getItem() instanceof SimpleData)) {
                return;
            }

            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }

            SimpleData sd = (SimpleData) e.getItem();
            if (sd.getId() != -1) {
                return;
            }

            String newValue = JOptionPane.showInputDialog(dialog, "Введите новое значение:", "", JOptionPane.PLAIN_MESSAGE);
            if (newValue != null && !newValue.isEmpty()) {
                int id = insertNewValue(newValue, table);
                if (id >= 0){
                    cb.insertItemAt(new SimpleData(id, newValue), 0);
                }
            }
            cb.setSelectedIndex(0);
        }
    }

    private class SaveNoteButtonActionListener implements ActionListener {
        private NoteEditDialog dialog;
        private Note note;
        private JTextField itemNameTextField;
        private JComboBox itemTypeComboBox;
        private JTextField itemPriceTextField;
        private JTextField itemCountTextField;
        private JComboBox countTypeComboBox;
        private JComboBox shopComboBox;
        private JComboBox necessityLevelComboBox;
        private JComboBox qualityLevelComboBox;
        private JCheckBox saleCheckBox;
        private JTextField dayTextField;
        private JTextField monthTextField;
        private JTextField yearTextField;

        SaveNoteButtonActionListener(NoteEditDialog dialog,
                                     Note note,
                                     JTextField itemNameTextField,
                                     JComboBox itemTypeComboBox,
                                     JTextField itemPriceTextField,
                                     JTextField itemCountTextField,
                                     JComboBox countTypeComboBox,
                                     JComboBox shopComboBox,
                                     JComboBox necessityLevelComboBox,
                                     JComboBox qualityLevelComboBox,
                                     JCheckBox saleCheckBox,
                                     JTextField dayTextField,
                                     JTextField monthTextField,
                                     JTextField yearTextField) {
            this.dialog = dialog;
            this.note = note;
            this.itemNameTextField = itemNameTextField;
            this.itemTypeComboBox = itemTypeComboBox;
            this.itemPriceTextField = itemPriceTextField;
            this.itemCountTextField = itemCountTextField;
            this.countTypeComboBox = countTypeComboBox;
            this.shopComboBox = shopComboBox;
            this.necessityLevelComboBox = necessityLevelComboBox;
            this.qualityLevelComboBox = qualityLevelComboBox;
            this.saleCheckBox = saleCheckBox;
            this.dayTextField = dayTextField;
            this.monthTextField = monthTextField;
            this.yearTextField = yearTextField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (itemNameTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(dialog, "Укажите товар!", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (Float.parseFloat(itemCountTextField.getText()) == 0f) {
                JOptionPane.showMessageDialog(dialog, "Укажите количество товара!", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            note.update(itemNameTextField.getText(),
                    (SimpleData) itemTypeComboBox.getSelectedItem(),
                    Float.parseFloat(itemPriceTextField.getText()),
                    Float.parseFloat(itemCountTextField.getText()),
                    (SimpleData) countTypeComboBox.getSelectedItem(),
                    (SimpleData) shopComboBox.getSelectedItem(),
                    (SimpleData) necessityLevelComboBox.getSelectedItem(),
                    (SimpleData) qualityLevelComboBox.getSelectedItem(),
                    saleCheckBox.isSelected(),
                    dayTextField.getText(),
                    monthTextField.getText(),
                    yearTextField.getText());

            dialog.setDialogResult(JOptionPane.YES_OPTION);

            dialog.dispose();
        }
    }

    private class CloseNoteEditDialogButtonActionListener implements ActionListener {
        NoteEditDialog dialog;
        Note note;

        CloseNoteEditDialogButtonActionListener(NoteEditDialog dialog, Note note) {
            this.dialog = dialog;
            this.note = note;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            note = null;

            dialog.setDialogResult(JOptionPane.NO_OPTION);

            dialog.dispose();
        }
    }

    private class StatisticsDialogPeriodComboBoxItemListener implements ItemListener {
        private StatisticsDialog dialog;

        StatisticsDialogPeriodComboBoxItemListener(StatisticsDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox periodComboBox;
            if (e.getSource() instanceof JComboBox) {
                periodComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }


            String period = (String) periodComboBox.getSelectedItem();

            dialog.setSelectedPeriod(period);
            dialog.update();
        }
    }

    private class StatisticsDialogItemTypeComboBoxItemListener implements ItemListener {
        private StatisticsDialog dialog;

        StatisticsDialogItemTypeComboBoxItemListener(StatisticsDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JComboBox periodComboBox;
            if (e.getSource() instanceof JComboBox) {
                periodComboBox = (JComboBox) e.getSource();
            } else {
                return;
            }


            int typeID = ((SimpleData) periodComboBox.getSelectedItem()).getId();

            dialog.setSelectedItemType(typeID);
            dialog.update();
        }
    }

    private class CloseStatisticsDialogButtonActionListener implements ActionListener {
        StatisticsDialog dialog;

        CloseStatisticsDialogButtonActionListener(StatisticsDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
        }
    }
}
