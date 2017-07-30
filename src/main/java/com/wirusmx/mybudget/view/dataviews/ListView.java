package com.wirusmx.mybudget.view.dataviews;

import com.wirusmx.mybudget.model.Note;

import javax.swing.*;
import java.util.List;

/**
 * Class represents <code>Note</code>s as JList.
 *
 * @author Piunov M (aka WirusMX)
 */
public class ListView extends DataView {

    private JList<Note> notesList;
    private DefaultListModel<Note> noteDefaultListModel;

    public ListView(List<Note> notes) {
        super(notes);
    }

    @Override
    public Note getSelectedNote() {
        return null;
    }

    @Override
    void clearView() {

    }

    @Override
    void updateView() {

    }
}
