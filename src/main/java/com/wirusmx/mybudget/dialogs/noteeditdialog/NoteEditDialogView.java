package com.wirusmx.mybudget.dialogs.noteeditdialog;

import com.wirusmx.mybudget.dialogs.AbstractDialogView;
import com.wirusmx.mybudget.managers.ResourcesManager;
import com.wirusmx.mybudget.model.Note;
import com.wirusmx.mybudget.model.SimpleData;

import javax.swing.*;
import java.util.Set;

/**
 * Dialog, where user enter new item data, or edit existing item data.
 * Figuratively dialog divides into two columns. First columns contains
 * labels, second - fields for entering data.
 *
 * @author Piunov M (aka WirusMX)
 */
class NoteEditDialogView extends AbstractDialogView {
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
    private static final int DIALOG_WIDTH = 500;
    private static final int DIALOG_HEIGHT = 320;
    private static final String ICON_IMAGE = "edit";
    private static final String DIALOG_TITLE = "";

    private final NoteEditDialogController controller;

    private JTextField itemTitleTextField;
    private JComboBox<SimpleData> itemTypeComboBox;
    private JTextField itemPriceTextField;
    private JTextField itemsCountTextField;
    private JComboBox<SimpleData> countTypeComboBox;
    private JComboBox<SimpleData> shopComboBox;
    private JComboBox<SimpleData> necessityLevelComboBox;
    private JComboBox<SimpleData> qualityLevelComboBox;
    private JCheckBox saleCheckBox;
    private JTextField dayTextField;
    private JTextField monthTextField;
    private JTextField yearTextField;

    /**
     * @param controller - application controller.
     */
    NoteEditDialogView(NoteEditDialogController controller, ResourcesManager resourcesManager) {
        super(DIALOG_WIDTH, DIALOG_HEIGHT, DIALOG_TITLE, ICON_IMAGE, controller, resourcesManager);
        this.controller = controller;
    }

    String getItemTitle() {
        return itemTitleTextField.getText();
    }

    SimpleData getItemType() {
        return (SimpleData) itemTypeComboBox.getSelectedItem();
    }

    Float getItemPrice() {
        return Float.parseFloat(itemPriceTextField.getText());
    }

    Float getItemsCount() {
        return Float.parseFloat(itemsCountTextField.getText());
    }

    SimpleData getCountType() {
        return (SimpleData) countTypeComboBox.getSelectedItem();
    }

    SimpleData getShop() {
        return (SimpleData) shopComboBox.getSelectedItem();
    }

    SimpleData getNecessity() {
        return (SimpleData) necessityLevelComboBox.getSelectedItem();
    }

    SimpleData getQuality() {
        return (SimpleData) qualityLevelComboBox.getSelectedItem();
    }

    boolean isBySale() {
        return saleCheckBox.isSelected();
    }

    String getDay() {
        return dayTextField.getText();
    }

    String getMonth() {
        return monthTextField.getText();
    }

    String getYear() {
        return yearTextField.getText();
    }

    protected void initDialogContent() {
        setLayout(null);

        int secondColElementWidth = getWidth() - 20 - SECOND_COL_X_POS;
        int elementsLine = 0;

        JLabel label1 = new JLabel("Товар: ");
        label1.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label1);

        Note note = controller.getCurrentNote();

        itemTitleTextField = new JTextField(note.getItemTitle());
        itemTitleTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine, secondColElementWidth, ELEMENT_HEIGHT);
        itemTitleTextField.setToolTipText("Укажите наименование товара. Например: Мыло.");
        add(itemTitleTextField);

        JComboBox<String> itemsComboBox = new JComboBox<>();
        itemsComboBox.addItemListener(controller.getItemsComboBoxItemListener(itemTitleTextField));
        itemsComboBox.setBounds(SECOND_COL_X_POS, Y0 + ELEMENT_HEIGHT + DELTA_Y * elementsLine++, secondColElementWidth, 0);
        add(itemsComboBox);

        itemTitleTextField.addKeyListener(controller.getItemsTitleTextFieldKeyListener(itemsComboBox));

        JLabel label2 = new JLabel("Тип: ");
        label2.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label2);

        itemTypeComboBox = new JComboBox<>();
        addValuesToComboBox(itemTypeComboBox, "item_types", true);
        selectValue(itemTypeComboBox, note.getType());
        itemTypeComboBox.addItemListener(controller.getNoteEditDialogComboBoxItemListener("item_types"));
        itemTypeComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        itemTypeComboBox.setToolTipText("Выберите тип товара из списка или добавьте новый тип.");
        add(itemTypeComboBox);

        JLabel label3 = new JLabel("Цена (руб./шт.):");
        label3.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label3);

        itemPriceTextField = new JTextField("" + note.getPriceAsString());
        itemPriceTextField.addFocusListener(controller.getSelectAllTextFocusListener());
        itemPriceTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Float.class, String.format(Note.PRICE_FORMAT, 0f), 2));
        itemPriceTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        itemPriceTextField.setToolTipText("Укажите цену за одну единицу (шт., кг., и т.п.) товара.");
        add(itemPriceTextField);

        JLabel countLabel = new JLabel("Количество:");
        countLabel.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(countLabel);

        itemsCountTextField = new JTextField("" + note.getCountAsString());
        itemsCountTextField.addFocusListener(controller.getSelectAllTextFocusListener());
        itemsCountTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Float.class, String.format(Note.COUNT_FORMAT, 0f), 3));
        itemsCountTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine, secondColElementWidth - 70, ELEMENT_HEIGHT);
        itemsCountTextField.setToolTipText("Укажите количество купленного товара.");
        add(itemsCountTextField);

        countTypeComboBox = new JComboBox<>();
        addValuesToComboBox(countTypeComboBox, "count_types", false);
        selectValue(countTypeComboBox, note.getCountType());
        countTypeComboBox.addItemListener(controller.getCountTypeComboBoxItemListener(label3));
        countTypeComboBox.setBounds(SECOND_COL_X_POS + secondColElementWidth - 60, Y0 + DELTA_Y * elementsLine++, 60, ELEMENT_HEIGHT);
        countTypeComboBox.setToolTipText("Укажите единицы измерения количества товара.");
        add(countTypeComboBox);

        JLabel totalLabel = new JLabel("Итого:");
        totalLabel.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(totalLabel);

        JTextField totalTextField = new JTextField("" + note.getTotalAsString());
        totalTextField.addFocusListener(controller.getSelectAllTextFocusListener());
        totalTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Float.class, String.format(Note.PRICE_FORMAT, 0f), 2));
        totalTextField.addFocusListener(controller.getTotalCalcTextFieldFocusListener(itemPriceTextField, itemsCountTextField));
        totalTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        totalTextField.setToolTipText("Значение будет вычислено автоматически, если заполены предыдущие поля (цена и количество).");
        add(totalTextField);

        JLabel label4 = new JLabel("Магазин: ");
        label4.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label4);

        shopComboBox = new JComboBox<>();
        addValuesToComboBox(shopComboBox, "shops", true);
        selectValue(shopComboBox, note.getShop());
        shopComboBox.addItemListener(controller.getNoteEditDialogComboBoxItemListener("shops"));
        shopComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        shopComboBox.setToolTipText("Выберите магазин, где был преобретен товар, из списка или добавьте новый.");
        add(shopComboBox);

        JLabel label5 = new JLabel("Необходимость: ");
        label5.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label5);

        necessityLevelComboBox = new JComboBox<>();
        necessityLevelComboBox.addItem(new SimpleData(Note.Necessity.HIGH, "Высокая"));
        necessityLevelComboBox.addItem(new SimpleData(Note.Necessity.LOW, "Низкая"));
        necessityLevelComboBox.setSelectedIndex(note.getNecessity().getId() % 2);
        necessityLevelComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        necessityLevelComboBox.setToolTipText("Выберите уровень необходимости в преобретении данного товара.");
        add(necessityLevelComboBox);

        JLabel label6 = new JLabel("Качество: ");
        label6.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        add(label6);

        qualityLevelComboBox = new JComboBox<>();
        qualityLevelComboBox.addItem(new SimpleData(Note.Quality.UNDEFINED, "-"));
        qualityLevelComboBox.addItem(new SimpleData(Note.Quality.HIGH, "Высокое"));
        qualityLevelComboBox.addItem(new SimpleData(Note.Quality.MEDIUM, "Среднее"));
        qualityLevelComboBox.addItem(new SimpleData(Note.Quality.LOW, "Низкое"));
        qualityLevelComboBox.setSelectedIndex(note.getQuality().getId() % 4);
        qualityLevelComboBox.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine++, secondColElementWidth, ELEMENT_HEIGHT);
        qualityLevelComboBox.setToolTipText("Оцените товар.");
        add(qualityLevelComboBox);

        saleCheckBox = new JCheckBox("Со скидкой ");
        saleCheckBox.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine++, LABEL_WIDTH, ELEMENT_HEIGHT);
        saleCheckBox.setSelected(note.isBySale());
        saleCheckBox.setToolTipText("Поставьте галочку, если товар преобретен со скидкой.");
        add(saleCheckBox);

        JLabel label7 = new JLabel("Дата: ");
        label7.setBounds(FIRST_COL_X_POS, Y0 + DELTA_Y * elementsLine, LABEL_WIDTH, ELEMENT_HEIGHT);
        label7.setToolTipText("Укажите дату перобретения товара.");
        add(label7);

        dayTextField = new JTextField(note.getDay());
        dayTextField.addFocusListener(controller.getSelectAllTextFocusListener());
        dayTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Integer.class, note.getDay(), 0));
        dayTextField.setBounds(SECOND_COL_X_POS, Y0 + DELTA_Y * elementsLine, 30, ELEMENT_HEIGHT);
        dayTextField.setToolTipText("День");
        add(dayTextField);

        monthTextField = new JTextField(note.getMonth());
        monthTextField.addFocusListener(controller.getSelectAllTextFocusListener());
        monthTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Integer.class, note.getMonth(), 0));
        monthTextField.setBounds(SECOND_COL_X_POS + 35, Y0 + DELTA_Y * elementsLine, 30, ELEMENT_HEIGHT);
        monthTextField.setToolTipText("Месяц");
        add(monthTextField);

        yearTextField = new JTextField(note.getYear());
        yearTextField.addFocusListener(controller.getSelectAllTextFocusListener());
        yearTextField.addFocusListener(controller.getNumericTextFieldFocusListener(Integer.class, note.getYear(), 0));
        yearTextField.setBounds(SECOND_COL_X_POS + 70, Y0 + DELTA_Y * elementsLine, 60, ELEMENT_HEIGHT);
        yearTextField.setToolTipText("Год");
        add(yearTextField);

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(controller.getSaveNoteButtonActionListener());
        saveButton.setBounds(30, Y0 + DELTA_Y * elementsLine + 50, 200, 30);
        add(saveButton);

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(controller.getCloseDialogButtonActionListener());
        closeButton.setBounds(260, Y0 + DELTA_Y * elementsLine++ + 50, 200, 30);
        add(closeButton);

        setSize(500, Y0 + DELTA_Y * elementsLine + 100);
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

    private void addValuesToComboBox(JComboBox<SimpleData> comboBox, String tableName, boolean addDefaulValue) {
        comboBox.removeAllItems();
        Set<SimpleData> values = controller.getComboBoxValues(tableName);
        for (SimpleData d : values) {
            comboBox.addItem(d);
        }

        if (addDefaulValue) {
            comboBox.addItem(new SimpleData(-1, "Добавить новый"));
        }
    }

}
