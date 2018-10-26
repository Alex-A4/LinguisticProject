package com.alexa4.linguistic_project.view.dialogs;

import com.sun.istack.internal.NotNull;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;


/**
 * Class needs to call Alerts to inform user about actions
 */
public class AlertDialog {

    public static final int CONFIRM_OK = 1;
    public static final int CONFIRM_CANCEL = 0;

    /**
     * Show error alert
     * @param title the title of alert
     * @param header the header of alert
     * @param content the content of alert
     */
    public static void callErrorAlert(@NotNull String title, String header, @NotNull String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Show information alert
     * @param title the title of alert
     * @param header the header of alert
     * @param content the content of alert
     */
    public static void callSuccessAlert(@NotNull String title, String header, @NotNull String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Show the confirmation alert and return the user choice
     * @param title the title  alert
     * @param header the healer of alert
     * @param content the content of alert
     * @return the choice of user
     */
    public static int callConfirmationAlert(@NotNull String title, @NotNull String header,
                                             @NotNull String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null)
            return CONFIRM_CANCEL;
        else if (option.get() == ButtonType.OK)
            return CONFIRM_OK;
        else return CONFIRM_CANCEL;
    }
}
