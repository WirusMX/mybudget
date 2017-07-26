package com.wirusmx.mybudget.view;

import com.wirusmx.mybudget.controller.Controller;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;

import javax.swing.*;
import java.util.Set;

public class NoteEditDialog extends JDialog {

    private static final int FIRST_COL_X_POS = 10;
    private static final int SECOND_COL_X_POS = 140;
    private static final int ELEMENT_HEIGHT = 25;
    private static final int LABEL_WIDTH = 130;
    private static final int Y0 = 20;
    private static final int DELTA_Y = 30;

    private final Controller controller;
    private Note note;

    private int dialogResult;

    public NoteEditDialog(Controller controller) {
        this(controller, new Note());
    }

    public NoteEditDialog(Controller controller, Note note) {
        this.note = note;
        this.controller = controller;
        init();
    }

    public Note getNote() {
        return note;
    }

    /**
     * Returns dialog result.
     *
     * @return <code>JOptionPane.YES_OPTION</code> value if button SAVE was clicked,
     * otherwise <code>JOptionPane.NO_OPTION</code>.
     */
    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    private void init() {
        setSize(500, 320);
        setLayout(null);
        setModal(true);
        setLocationRelativeTo(null);
        setUndecorated(true);

        int secondColElementWidth = getWidth() - 20 - SECOND_COL_X_POS;

        JLabel label1 = new JLabel("Товар: ");
        label1.setBounds(FIRST_COL_X_POS, Y0, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label1);

        JTextField itemNameTextField = new JTextField(note.getItem());
        itemNameTextField.setBounds(SECOND_COL_X_POS, Y0, secondColElementWidth, ELEMENT_HEIGHT);
        add(itemNameTextField);

        JLabel label2 = new JLabel("Тип: ");
        label2.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label2);

        JComboBox<SimpleData> itemTypeComboBox = new JComboBox<>();
        addValuesToComboBox(itemTypeComboBox, "item_types");
        selectValue(itemTypeComboBox, note.getType());
        itemTypeComboBox.addItemListener(controller.getNoteEditDialogComboBoxItemListener(this, "item_types"));
        itemTypeComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y, secondColElementWidth, ELEMENT_HEIGHT);
        add(itemTypeComboBox);

        JLabel label3 = new JLabel("Стоимость (руб.): ");
        label3.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 2, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label3);

        JTextField itemPriceTextField = new JTextField("" + note.getPrice());
        itemPriceTextField.addFocusListener(controller.getNumericTextFieldFocusListener("0"));

        itemPriceTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * 2, secondColElementWidth, ELEMENT_HEIGHT);
        add(itemPriceTextField);

        JLabel label4 = new JLabel("Магазин: ");
        label4.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 3, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label4);

        JComboBox<SimpleData> shopComboBox = new JComboBox<>();
        addValuesToComboBox(shopComboBox, "shops");
        selectValue(shopComboBox, note.getShop());
        shopComboBox.addItemListener(controller.getNoteEditDialogComboBoxItemListener(this, "shops"));
        shopComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * 3, secondColElementWidth, ELEMENT_HEIGHT);
        add(shopComboBox);

        JLabel label5 = new JLabel("Необходимость: ");
        label5.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 4, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label5);

        JComboBox<SimpleData> necessityLevelComboBox = new JComboBox<>();
        necessityLevelComboBox.addItem(new SimpleData(Model.Necessity.HIGH, "Высокая"));
        necessityLevelComboBox.addItem(new SimpleData(Model.Necessity.LOW, "Низкая"));
        necessityLevelComboBox.setSelectedIndex(note.getNecessity().getId() % 2);
        necessityLevelComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * 4, secondColElementWidth, ELEMENT_HEIGHT);
        add(necessityLevelComboBox);

        JLabel label6 = new JLabel("Качество: ");
        label6.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 5, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label6);

        JComboBox<SimpleData> qualityLevelComboBox = new JComboBox<>();
        qualityLevelComboBox.addItem(new SimpleData(Model.Quality.UNDEFINED, "-"));
        qualityLevelComboBox.addItem(new SimpleData(Model.Quality.HIGH, "Высокое"));
        qualityLevelComboBox.addItem(new SimpleData(Model.Quality.MEDIUM, "Среднее"));
        qualityLevelComboBox.addItem(new SimpleData(Model.Quality.LOW, "Низкое"));
        qualityLevelComboBox.setSelectedIndex(note.getQuality().getId() % 4);
        qualityLevelComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * 5, secondColElementWidth, ELEMENT_HEIGHT);
        add(qualityLevelComboBox);

        JCheckBox saleCheckBox = new JCheckBox("Со скидкой ");
        saleCheckBox.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 6, LABEL_WIDTH, ELEMENT_HEIGHT);
        saleCheckBox.setSelected(note.isBySale());
        add(saleCheckBox);

        JLabel label7 = new JLabel("Дата: ");
        label7.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * 7, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label7);

        JTextField dayTextField = new JTextField(note.getDay());
        dayTextField.addFocusListener(controller.getNumericTextFieldFocusListener(note.getDay()));
        dayTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * 7, 30, ELEMENT_HEIGHT);
        add(dayTextField);

        JTextField monthTextField = new JTextField(note.getMonth());
        monthTextField.addFocusListener(controller.getNumericTextFieldFocusListener(note.getMonth()));
        monthTextField.setBounds(SECOND_COL_X_POS + 35, Y0 + DELTA_Y * 7, 30, ELEMENT_HEIGHT);
        add(monthTextField);

        JTextField yearTextField = new JTextField(note.getYear());
        yearTextField.addFocusListener(controller.getNumericTextFieldFocusListener(note.getYear()));
        yearTextField.setBounds(SECOND_COL_X_POS + 70, Y0 + DELTA_Y * 7, 60, ELEMENT_HEIGHT);
        add(yearTextField);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(controller.getSaveNoteButtonActionListener(this, note, itemNameTextField, itemTypeComboBox, itemPriceTextField,
                shopComboBox, necessityLevelComboBox, qualityLevelComboBox, saleCheckBox, dayTextField, monthTextField,
                yearTextField));
        saveButton.setBounds(50, Y0 + DELTA_Y * 7 + 50, 200, 30);
        add(saveButton);

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(controller.getCloseDialogButtonActionListener(this, note));
        closeButton.setBounds(280, Y0 + DELTA_Y * 7 + 50, 200, 30);
        add(closeButton);

        setVisible(true);
    }

    private void selectValue(JComboBox<SimpleData> comboBox, SimpleData value) {
        if (value == null) {
            return;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (value.equals(comboBox.getItemAt(i))) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private void addValuesToComboBox(JComboBox<SimpleData> comboBox, String tableName) {
        comboBox.removeAllItems();
        Set<SimpleData> values = controller.getComboBoxValues(tableName);
        for (SimpleData d : values) {
            comboBox.addItem(d);
        }

        comboBox.addItem(new SimpleData(-1, "Добавить новый"));
    }

}
