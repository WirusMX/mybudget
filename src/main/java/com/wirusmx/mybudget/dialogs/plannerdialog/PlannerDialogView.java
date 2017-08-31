package com.wirusmx.mybudget.dialogs.plannerdialog;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringStack;
import com.wirusmx.mybudget.dialogs.AbstractDialogView;
import com.wirusmx.mybudget.managers.ResourcesManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * @author Piunov M (aka WirusMX)
 */
class PlannerDialogView extends AbstractDialogView {
    private static final int DIALOG_WIDTH = 1000;
    private static final int DIALOG_HEIGHT = 600;
    private static final String DIALOG_TITLE = "Планировщик";
    private static final String ICON_IMAGE = "planner";

    private PlannerDialogController controller;

    PlannerDialogView(PlannerDialogController controller, ResourcesManager resourcesManager) {
        super(DIALOG_WIDTH, DIALOG_HEIGHT, DIALOG_TITLE, ICON_IMAGE, controller, resourcesManager);
        this.controller = controller;
    }

    @Override
    protected void initDialogContent() {

        setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setFont(new Font("Monospaced", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String[] tableHeader = new String[]{
                "Товар",
                "Количество",
                "Комментарий"
        };
        tableModel.setColumnIdentifiers(tableHeader);

        tableModel.addRow(new String[]{"Итого:", "", "0.00"});

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
