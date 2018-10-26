package com.alexa4.linguistic_project.view.dialogs;

import com.sun.istack.internal.NotNull;
import javafx.scene.control.Alert;


/**
 * Class needs to call Alerts to inform user about actions
 */
public class AlertDialog {
    /**
     * Show error alert
     * @param title the title of alert
     * @param header the header of alert
     * @param content the content of alert
     */
    public static void callErrorAlert(@NotNull String title, String header, @NotNull String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        if (header != null)
            alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Show confirmation alert
     * @param title the title of alert
     * @param header the header of alert
     * @param content the content of alert
     */
    public static void callSuccessAlert(@NotNull String title, String header, @NotNull String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        if (header != null)
            alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
