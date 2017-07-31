package com.wirusmx.mybudget.view.dialogs;

import com.wirusmx.mybudget.controller.Controller;
import com.wirusmx.mybudget.model.Model;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Set;

/**
 * Dialog, where user enter new item data, or edit existing item data.
 * Figuratively dialog divides into two columns. First columns contains
 * labels, second - fields for entering data.
 *
 * @author Piunov M (aka WirusMX)
 */
public class NoteEditDialog extends JDialog {
    /**
     * X coordinate for first column.
     */
    private static final int FIRST_COL_X_POS = 10;

    /**
     * X coordinate for second column.
     */
    private static final int SECOND_COL_X_POS = 140;

    /**
     * Elements height (labels and fields).
     */
    private static final int ELEMENT_HEIGHT = 25;

    /**
     * Labels width.
     */
    private static final int LABEL_WIDTH = 130;

    /**
     * Y coordinate for the first elements line.
     */
    private static final int Y0 = 20;

    /**
     * Value for calculating Y position of the next elements line:
     * <code>Y = Y0 +  DELTA_Y * elementsLine</code>.
     */
    private static final int DELTA_Y = 30;

    private final Controller controller;
    private Note note;

    private int dialogResult;

    /**
     * Constructs dialog for entering new item data.
     * All fields will have default values.
     *
     * @param controller - application controller.
     */
    public NoteEditDialog(Controller controller) {
        this(controller, new Note());
    }

    /**
     * Constructs dialog for editing item data.
     * Fields will have specified values.
     *
     * @param controller - application controller.
     * @param note       - item data for editing.
     */
    public NoteEditDialog(Controller controller, Note note) {
        this.note = note;
        this.controller = controller;
        init();
    }

    /**
     * @return resulted <code>Note</code> or <code>null</code> if
     * user choose the Close action.
     */
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

    /**
     * Sets dialog result. Must be used only by application controller.
     *
     * @param dialogResult - result value for these dialog. Must have one
     *                     of next values: <code>JOptionPane.YES_OPTION</code>
     *                     value if button SAVE was clicked, otherwise
     *                     <code>JOptionPane.NO_OPTION</code>.
     */
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    private void init() {
        setSize(500, 320);
        setLayout(null);
        setModal(true);
        setLocationRelativeTo(null);
        //setUndecorated(true);

        getRootPane().registerKeyboardAction(
                controller.getCloseDialogButtonActionListener(this, note),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        int secondColElementWidth = getWidth() - 20 - SECOND_COL_X_POS;
        int elementsLine = 0;

        JLabel label1 = new JLabel("Товар: ");
        label1.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label1);

        JTextField itemNameTextField = new JTextField(note.getItem());
        itemNameTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        add(itemNameTextField);

        JLabel label2 = new JLabel("Тип: ");
        label2.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label2);

        JComboBox<SimpleData> itemTypeComboBox = new JComboBox<>();
        addValuesToComboBox(itemTypeComboBox, "item_types");
        selectValue(itemTypeComboBox, note.getType());
        itemTypeComboBox.addItemListener(controller.getNoteEditDialogComboBoxItemListener(this, "item_types"));
        itemTypeComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        add(itemTypeComboBox);

        JLabel label3 = new JLabel("Цена (руб./ед.):");
        label3.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label3);

        JTextField itemPriceTextField = new JTextField("" + note.getPriceAsString());
        itemPriceTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Float.class, String.format(Note.PRICE_FORMAT, 0f)));
        itemPriceTextField.addMouseListener(controller.getSelectAllTextMouseListener(itemPriceTextField));
        itemPriceTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        add(itemPriceTextField);

        JLabel countLabel = new JLabel("Количество:");
        countLabel.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(countLabel);

        JTextField itemsCountTextField = new JTextField("" + note.getPriceAsString());
        itemsCountTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Float.class, String.format(Note.PRICE_FORMAT, 0f)));
        itemsCountTextField.addMouseListener(controller.getSelectAllTextMouseListener(itemsCountTextField));
        itemsCountTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        add(itemsCountTextField);

        JLabel summLabel = new JLabel("Итого:");
        summLabel.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(summLabel);

        JTextField summTextField = new JTextField("" + note.getPriceAsString());
        summTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Float.class, String.format(Note.PRICE_FORMAT, 0f)));
        summTextField.addMouseListener(controller.getSelectAllTextMouseListener(summTextField));
        summTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        add(summTextField);
        
        JLabel label4 = new JLabel("Магазин: ");
        label4.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label4);

        JComboBox<SimpleData> shopComboBox = new JComboBox<>();
        addValuesToComboBox(shopComboBox, "shops");
        selectValue(shopComboBox, note.getShop());
        shopComboBox.addItemListener(controller.getNoteEditDialogComboBoxItemListener(this, "shops"));
        shopComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        add(shopComboBox);

        JLabel label5 = new JLabel("Необходимость: ");
        label5.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label5);

        JComboBox<SimpleData> necessityLevelComboBox = new JComboBox<>();
        necessityLevelComboBox.addItem(new SimpleData(Model.Necessity.HIGH, "Высокая"));
        necessityLevelComboBox.addItem(new SimpleData(Model.Necessity.LOW, "Низкая"));
        necessityLevelComboBox.setSelectedIndex(note.getNecessity().getId() % 2);
        necessityLevelComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        add(necessityLevelComboBox);

        JLabel label6 = new JLabel("Качество: ");
        label6.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label6);

        JComboBox<SimpleData> qualityLevelComboBox = new JComboBox<>();
        qualityLevelComboBox.addItem(new SimpleData(Model.Quality.UNDEFINED, "-"));
        qualityLevelComboBox.addItem(new SimpleData(Model.Quality.HIGH, "Высокое"));
        qualityLevelComboBox.addItem(new SimpleData(Model.Quality.MEDIUM, "Среднее"));
        qualityLevelComboBox.addItem(new SimpleData(Model.Quality.LOW, "Низкое"));
        qualityLevelComboBox.setSelectedIndex(note.getQuality().getId() % 4);
        qualityLevelComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        add(qualityLevelComboBox);

        JCheckBox saleCheckBox = new JCheckBox("Со скидкой ");
        saleCheckBox.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine++, LABEL_WIDTH, ELEMENT_HEIGHT);
        saleCheckBox.setSelected(note.isBySale());
        add(saleCheckBox);

        JLabel label7 = new JLabel("Дата: ");
        label7.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label7);

        JTextField dayTextField = new JTextField(note.getDay());
        dayTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Integer.class, note.getDay()));
        dayTextField.addMouseListener(controller.getSelectAllTextMouseListener(dayTextField));
        dayTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine, 30, ELEMENT_HEIGHT);
        add(dayTextField);

        JTextField monthTextField = new JTextField(note.getMonth());
        monthTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Integer.class, note.getMonth()));
        monthTextField.addMouseListener(controller.getSelectAllTextMouseListener(monthTextField));
        monthTextField.setBounds(SECOND_COL_X_POS + 35, Y0 + DELTA_Y * elementsLine, 30, ELEMENT_HEIGHT);
        add(monthTextField);

        JTextField yearTextField = new JTextField(note.getYear());
        yearTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Integer.class, note.getYear()));
        yearTextField.addMouseListener(controller.getSelectAllTextMouseListener(yearTextField));
        yearTextField.setBounds(SECOND_COL_X_POS + 70, Y0 + DELTA_Y * elementsLine, 60, ELEMENT_HEIGHT);
        add(yearTextField);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(controller.getSaveNoteButtonActionListener(this, note, itemNameTextField, itemTypeComboBox, summTextField,
                shopComboBox, necessityLevelComboBox, qualityLevelComboBox, saleCheckBox, dayTextField, monthTextField,
                yearTextField));
        saveButton.setBounds(30, Y0 + DELTA_Y * elementsLine + 50, 200, 30);
        add(saveButton);

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(controller.getCloseDialogButtonActionListener(this, note));
        closeButton.setBounds(260, Y0 + DELTA_Y * elementsLine++ + 50, 200, 30);
        add(closeButton);

        setSize(500, Y0 + DELTA_Y * elementsLine + 100);
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
