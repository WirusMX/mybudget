package com.wirusmx.mybudget.view.dataviews;

import com.wirusmx.mybudget.model.Note;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Class represents <code>Note</code>s as JTable.
 *
 * @author Piunov M (aka WirusMX)
 */
public class TableView extends DataView {
    private final String[] tableHeader = new String[]{
            "Товар",
            "Магазин",
            "Количество",
            "Стоимость",
            "Дата",
            "Качество"
    };

    private DefaultTableModel tableModel;
    private JTable table;

    public TableView() {
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setFont(new Font("Monospaced", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableModel.setColumnIdentifiers(tableHeader);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Note note = notes.get(row);

                Color color = Color.WHITE;

                if (note.getNecessity().getId() == Note.Necessity.LOW && note.isBySale()) {
                    color = Color.ORANGE;
                } else {
                    if (note.isBySale()) {
                        color = Color.GREEN;
                    } else {
                        if (note.getNecessity().getId() == Note.Necessity.LOW) {
                            color = Color.PINK;
                        }
                    }
                }

                if (isSelected){
                    color = Color.LIGHT_GRAY;
                }


                cell.setForeground(Color.black);
                cell.setBackground(color);
                return cell;
            }

        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    @Override
    public void addMouseListener(MouseListener mouseListener) {
        table.addMouseListener(mouseListener);
    }

    @Override
    public void setComponentPopupMenu(JPopupMenu popupMenu) {
        table.setComponentPopupMenu(popupMenu);
    }

    @Override
    public Note getSelectedValue() {
        int pos = table.getSelectedRow();
        if (pos >= 0) {
            return notes.get(pos);
        }

        return null;
    }

    @Override
    public int locationToIndex(Point point) {
        return table.rowAtPoint(point);
    }

    @Override
    public void setSelectedIndex(int index) {
        if (index >= 0){
            table.setRowSelectionInterval(index, index);
        }
    }

    @Override
    void clearView() {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

    @Override
    void setViewValues() {
        for (Note n : notes) {
            String total = n.getTotalAsString() + " руб.";
            if (n.getCount() != 1f || n.isBySale()) {
                total += "(";

                if (n.getCount() != 1f) {
                    total += n.getPriceAsString() + " руб./" + n.getCountType() + " ";
                }

                if (n.isBySale()) {
                    total += "со скидкой";
                }

                total += ")";
            }
            tableModel.addRow(
                    new String[]{
                            n.getItemTitle(),
                            n.getShop().toString(),
                            n.getCountAsString() + " " + n.getCountType(),
                            total,
                            n.getDate(),
                            n.getQualityInStars()
                    }
            );
        }
    }
}
