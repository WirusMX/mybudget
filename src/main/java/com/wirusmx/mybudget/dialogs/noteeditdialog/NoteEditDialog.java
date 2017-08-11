package com.wirusmx.mybudget.dialogs.noteeditdialog;

import com.wirusmx.mybudget.managers.DatabaseManager;
import com.wirusmx.mybudget.managers.ResourcesManager;
import com.wirusmx.mybudget.model.Note;

/**
 * @author Piunov M (aka WirusMX)
 */
public class NoteEditDialog {

    private NoteEditDialogModel model;

    public NoteEditDialog() {
        this(new Note());
    }

    public NoteEditDialog(Note note) {
        NoteEditDialogController controller = new NoteEditDialogController();
        NoteEditDialogView view = new NoteEditDialogView(controller, ResourcesManager.getInstance());
         model = new NoteEditDialogModel(note, DatabaseManager.getInstance());

        controller.setDialogView(view);
        controller.setDialogModel(model);

        controller.showDialog();
    }

    public int getDialogResult() {
        return model.getDialogResult();
    }

    public Note getNote(){
        return model.getNote();
    }
}
