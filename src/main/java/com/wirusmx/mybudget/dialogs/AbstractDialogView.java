package com.wirusmx.mybudget.dialogs;

import com.wirusmx.mybudget.managers.ResourcesManager;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * @author Piunov M (aka WirusMX)
 */
public abstract class AbstractDialogView extends JDialog {
    private final int DIALOG_WIDTH;
    private final int DIALOG_HEIGHT;
    private final String DIALOG_TITLE;
    private final String ICON_IMAGE;

    private AbstractDialogController controller;
    private ResourcesManager resourcesManager;

    public AbstractDialogView(int dialogWidth, int dialogHeight, String dialogTitle,
                              String iconImage, AbstractDialogController controller, ResourcesManager resourcesManager) {
        this.DIALOG_WIDTH = dialogWidth;
        this.DIALOG_HEIGHT = dialogHeight;
        this.DIALOG_TITLE = dialogTitle;
        this.ICON_IMAGE = iconImage;
        this.controller = controller;
        this.resourcesManager = resourcesManager;
    }

    public void init() {
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setModal(true);
        setLocationRelativeTo(null);
        setTitle(DIALOG_TITLE);
        setIconImage(resourcesManager.getImage(ICON_IMAGE).getImage());
        getRootPane().registerKeyboardAction(
                controller.getCloseDialogButtonActionListener(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        setResizable(false);

        initDialogContent();

        setVisible(true);
    }

    protected abstract void initDialogContent();
}
