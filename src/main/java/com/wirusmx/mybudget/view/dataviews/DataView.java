package com.wirusmx.mybudget.view.dataviews;

import com.wirusmx.mybudget.model.Note;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Piunov M (aka WirusMX)
 */
public abstract class DataView extends JPanel {
    List<Note> notes;

    public DataView() {
        setLayout(new BorderLayout());
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        clearView();
        setViewValues();
    }


    public abstract void setComponentPopupMenu(JPopupMenu popupMenu);

    public abstract Note getSelectedValue();

    abstract void clearView();

    abstract void setViewValues();
}
