package com.wirusmx.mybudget.dialogs.optionsdialog;

import com.wirusmx.mybudget.dialogs.AbstractDialogView;
import com.wirusmx.mybudget.managers.ResourcesManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author Piunov M (aka WirusMX)
 */
class OptionsDialogView extends AbstractDialogView {
    private static final int DIALOG_WIDTH = 600;
    private static final int DIALOG_HEIGHT = 400;
    private static final String DIALOG_TITLE = "Настройки приложения";
    private static final String ICON_IMAGE = "settings";

    private JButton applyButton;

    private OptionsDialogController controller;

    OptionsDialogView(OptionsDialogController controller, ResourcesManager resourcesManager) {
        super(DIALOG_WIDTH, DIALOG_HEIGHT, DIALOG_TITLE, ICON_IMAGE, controller, resourcesManager);
        this.controller = controller;
    }

    protected void initDialogContent() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        tabbedPane.addTab("Вид", getLookOptionsPanel());
        tabbedPane.addTab("Поиск", getSearchOptionsPanel());

        add(tabbedPane, BorderLayout.CENTER);

        add(getButtonsPanel(), BorderLayout.SOUTH);

    }

    void setApplyButtonEnabled(boolean value){
        applyButton.setEnabled(value);
    }

    private JPanel getLookOptionsPanel() {
        JPanel panel = new JPanel(null);

        JCheckBox useSystemLook = new JCheckBox("Использовать системный внешний вид");
        useSystemLook.setToolTipText("Потребуется перезагрузка программы");
        useSystemLook.setBounds(10, 10, DIALOG_WIDTH - 10, 25);
        useSystemLook.setSelected(controller.getOption("main.window.use.system.look", false));
        useSystemLook.setName("main.window.use.system.look");
        useSystemLook.addActionListener(controller.getComponentActionListener());
        panel.add(useSystemLook);

        JCheckBox useColors = new JCheckBox("Подсвечивать расходы разных категорий");
        useColors.setToolTipText("Потребуется перезагрузка программы");
        useColors.setBounds(10, 35, DIALOG_WIDTH - 10, 25);
        useColors.setSelected(controller.getOption("main.window.use.colors", false));
        useColors.setName("main.window.use.colors");
        useColors.addActionListener(controller.getComponentActionListener());
        panel.add(useColors);

        return panel;
    }

    private JPanel getSearchOptionsPanel() {
        JPanel panel = new JPanel(null);

        JCheckBox useLiveSearch = new JCheckBox("Использовать \"живой\" поиск в главном окне");
        useLiveSearch.setBounds(10, 10, DIALOG_WIDTH - 10, 25);
        useLiveSearch.setSelected(controller.getOption("main.window.use.live.search", false));
        useLiveSearch.setName("main.window.use.live.search");
        useLiveSearch.addActionListener(controller.getComponentActionListener());
        panel.add(useLiveSearch);

        JCheckBox searchByShop = new JCheckBox("Поиск в том числе по магазинам");
        searchByShop.setBounds(10, 35, DIALOG_WIDTH - 10, 25);
        searchByShop.setSelected(controller.getOption("main.window.search.by.shop", false));
        searchByShop.setName("main.window.search.by.shop");
        searchByShop.addActionListener(controller.getComponentActionListener());
        panel.add(searchByShop);

        return panel;
    }



    private JPanel getButtonsPanel(){
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 10));
        panel.add(new JLabel());
        panel.add(new JLabel());

        JButton okButton = new JButton("ОК");
        okButton.addActionListener(controller.getOkButtonActionListener());
        panel.add(okButton);

        applyButton = new JButton("Применить");
        applyButton.addActionListener(controller.getApplyButtonActionListener());
        applyButton.setEnabled(false);
        panel.add(applyButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(controller.getCloseDialogButtonActionListener());
        panel.add(cancelButton);

        return panel;
    }
}
