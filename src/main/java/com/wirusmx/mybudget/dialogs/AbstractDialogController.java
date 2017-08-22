package com.wirusmx.mybudget.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Piunov M (aka WirusMX)
 */
public abstract class AbstractDialogController {
    private AbstractDialogView dialogView;

    protected void setDialogView(AbstractDialogView dialogView) {
        this.dialogView = dialogView;
    }

    public CloseDialogButtonActionListener getCloseDialogButtonActionListener() {
        return new CloseDialogButtonActionListener();
    }

    private class CloseDialogButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dialogView.dispose();
        }
    }
}
