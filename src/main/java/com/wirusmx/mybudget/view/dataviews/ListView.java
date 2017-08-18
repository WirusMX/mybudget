package com.wirusmx.mybudget.view.dataviews;

import com.wirusmx.mybudget.model.Note;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Class represents <code>Note</code>s as JList.
 *
 * @author Piunov M (aka WirusMX)
 */
public class ListView extends DataView {
    private JList<Note> notesList;
    private DefaultListModel<Note> noteDefaultListModel;

    public ListView() {
        noteDefaultListModel = new DefaultListModel<>();
        notesList = new JList<>(noteDefaultListModel);
        notesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                Color color = Color.WHITE;

                if (value instanceof Note) {
                    if (((Note) value).getNecessity().getId() == Note.Necessity.LOW && ((Note) value).isBySale()) {
                        color = Color.ORANGE;
                    } else {
                        if (((Note) value).isBySale()) {
                            color = Color.GREEN;
                        } else {
                            if (((Note) value).getNecessity().getId() == Note.Necessity.LOW) {
                                color = Color.PINK;
                            }
                        }
                    }
                }


                if (isSelected) {
                    color = Color.LIGHT_GRAY;
                }

                setBackground(color);
                setForeground(Color.BLACK);

                c.setFont(new Font("Monospaced", Font.PLAIN, 13));
                return c;
            }
        });

        add(new JScrollPane(notesList));
    }

    @Override
    public void addMouseListener(MouseListener mouseListener) {
        notesList.addMouseListener(mouseListener);
    }

    @Override
    public void setComponentPopupMenu(JPopupMenu popupMenu) {
        notesList.setComponentPopupMenu(popupMenu);
    }

    @Override
    public Note getSelectedValue() {
        return notesList.getSelectedValue();
    }

    @Override
    public int locationToIndex(Point point) {
        return notesList.locationToIndex(point);
    }

    @Override
    public void setSelectedIndex(int index) {
        notesList.setSelectedIndex(index);
    }

    @Override
    void clearView() {
        noteDefaultListModel.removeAllElements();
    }

    @Override
    void setViewValues() {
        for (Note n : notes) {
            noteDefaultListModel.addElement(n);
        }
    }
}
