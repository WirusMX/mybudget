package com.wirusmx.mybudget.view.dataviews;

import com.wirusmx.mybudget.model.Note;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * @author Piunov M (aka WirusMX)
 */
abstract class DataView extends JPanel {
    private List<Note> notes;

    DataView(List<Note> notes) {
        this.notes = notes;
        setLayout(new BorderLayout());
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        clearView();
        updateView();
    }

    public void clear() {
        this.notes.clear();
        clearView();
    }

    void setMouseListener(MouseListener mouseListener) {
        setMouseListener(mouseListener);
    }

    void setPopupMenu(JPopupMenu popupMenu) {
        setPopupMenu(popupMenu);
    }

    public abstract Note getSelectedNote();

    abstract void clearView();

    abstract void updateView();
}
