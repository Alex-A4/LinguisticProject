package com.alexa4.linguistic_project.view.dialogs;

import com.alexa4.linguistic_project.data_stores.TaskConfig;
import com.alexa4.linguistic_project.models.TextModel;
import com.alexa4.linguistic_project.presenters.teacher.TeacherPresenter;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Dialog window to open/save file of task
 * //TODO: add labels to understand what to do
 */
public class TaskDialog {
    /**
     * Callback interface to send name of task which need open
     */
    public interface TaskPickerCallback {
        void sendTaskName(String taskName);
    }

    /**
     * Method to get name of task which need open
     * @param callback the callback where need send result
     */
    public static void getTaskName(TaskPickerCallback callback) {
        //Getting list of tasks names
        TextModel model = new TextModel();
        List<String> tasksNamesList = model.getTasksListOfFiles();

        //Init stage
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setTitle("Выбор задания");

        //Init parent layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label fileName = new Label("Имя файла");
        fileName.setFont(new Font(15));

        //Init area where will set name of current task
        TextField nameOfTask = new TextField();
        nameOfTask.setFocusTraversable(false);
        nameOfTask.setAlignment(Pos.CENTER);
        nameOfTask.setPrefHeight(30);
        nameOfTask.setFont(new Font(20));
        nameOfTask.setEditable(false);

        //Box with tasks
        VBox tasksListBox = new VBox(10);
        tasksListBox.setPadding(new Insets(0,0,0,20));
        tasksListBox.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"),
                BorderStrokeStyle.NONE,CornerRadii.EMPTY, BorderWidths.EMPTY)));

        HashMap<Text, SelectedFlag> textMap = new HashMap<>();

        for (int i = 0; i < tasksNamesList.size(); i++) {
            Text text = new Text(tasksNamesList.get(i));

            textMap.put(text, new SelectedFlag());

            tasksListBox.getChildren().add(text);
            text.setFont(new Font(20));

            /**
             * If current task already selected, then unselect it
             * If the current task was not selected, then unselect all other tasks and select current
             */
            text.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (textMap.get(text).isSelected()) {
                        textMap.get(text).setSelected(false);
                        text.setFill(Paint.valueOf("#000000"));
                        nameOfTask.setText("");
                    } else {
                        textMap.forEach((t, c) -> {
                            c.setSelected(false);
                            t.setFill(Paint.valueOf("#000000"));
                        });
                        text.setFill(Paint.valueOf("#1faee9"));
                        textMap.get(text).setSelected(true);
                        nameOfTask.setText(text.getText());
                    }
                }
            });
        }

        Label listOfTasks = new Label("Список заданий:");
        listOfTasks.setFont(new Font(15));


        //Init scrollPane with tasks list
        ScrollPane pane = new ScrollPane(tasksListBox);
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pane.setFocusTraversable(false);
        pane.setStyle("-fx-background-color:transparent;");

        //Init button to send task name
        Button openBtn = new Button("Открыть");
        openBtn.setFont(new Font(18));
        openBtn.setOnAction(e -> {
            if (nameOfTask.getText().equals("")) {
                nameOfTask.setText("Выберите файл");
                return;
            }

            callback.sendTaskName(nameOfTask.getText());
            stage.close();
        });
        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(openBtn);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);



        layout.setVgrow(buttonBox, Priority.ALWAYS);

        layout.getChildren().addAll(fileName, nameOfTask, listOfTasks, pane, buttonBox);

        Scene scene = new Scene(layout, 350, 330);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * Class contains information about was the specified object selected or not
     */
    private static class SelectedFlag {
        private boolean isSelected;

        public SelectedFlag() {
            isSelected = false;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }


    /**
     * Callback interface which needs to send result of saving file
     */
    public interface TaskSaverCallback {
        void sendResultOfSaving(boolean result);
    }

    /**
     * Saving changes of text to specified file which name user will enter
     * @param textOfTask the text which need to save
     * @param callback the callback where need send result of saving
     */
    public static void saveTask(String textOfTask, String previousFileName, TaskConfig config, TaskSaverCallback callback) {
        //Getting list of tasks names
        List<String> tasksNamesList = new TextModel().getTasksListOfFiles();

        //List of Text which contains name of specified task
        List<Text> listTasksText = new ArrayList<>();


        //Init stage
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setTitle("Сохранить задание");

        //Init parent layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label fileNameL = new Label("Имя файла");
        fileNameL.setFont(new Font(15));

        //Init area where will set name of current task
        TextField nameOfTask = new TextField(previousFileName);
        nameOfTask.setTooltip(new Tooltip("Введите имя файла"));
        nameOfTask.setFocusTraversable(false);
        nameOfTask.setAlignment(Pos.CENTER);
        nameOfTask.setPrefHeight(30);
        nameOfTask.setFont(new Font(20));
        nameOfTask.setEditable(false);

        //Box with tasks
        VBox tasksListBox = new VBox(10);
        tasksListBox.setPadding(new Insets(0,0,0,20));
        tasksListBox.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"),
                BorderStrokeStyle.NONE,CornerRadii.EMPTY, BorderWidths.EMPTY)));


        for (int i = 0; i < tasksNamesList.size(); i++) {
            Text text = new Text(tasksNamesList.get(i));

            listTasksText.add(text);

            tasksListBox.getChildren().add(text);
            text.setFont(new Font(20));
        }

        Label listOfTasks = new Label("Список заданий:");
        listOfTasks.setFont(new Font(15));

        //Init scrollPane with tasks list
        ScrollPane pane = new ScrollPane(tasksListBox) {
            @Override
            public void requestFocus() {
            }
        };
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pane.setFocusTraversable(false);
        pane.setStyle("-fx-background-color:transparent;");

        //Init button to send task name
        Button openBtn = new Button("Сохранить");
        openBtn.setFont(new Font(18));
        //Trying to save file
        openBtn.setOnAction(e -> {
            String fileName = nameOfTask.getText();
            for (int i = 0; i < tasksNamesList.size(); i++)
                //If fileName equals to some existence tasks names
                //then ask user does he want to rewrite file
                if (fileName.equals(tasksNamesList.get(i))) {
                    if (AlertDialog.callConfirmationAlert("Перезаписать фалй?",
                            "Вы действительно хотите перезаписать файл: ", fileName) == AlertDialog.CONFIRM_CANCEL)
                        return;
                }
            if (TeacherPresenter.getPresenter() != null)
                callback.sendResultOfSaving(TeacherPresenter.getPresenter().saveFileChanges(textOfTask, fileName, config));
            else AlertDialog.callErrorAlert("Ошибка сохранения", null, "Файл не может быть сохранен");

            stage.close();
        });
        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(openBtn);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);


        layout.setVgrow(buttonBox, Priority.ALWAYS);

        layout.getChildren().addAll(fileNameL, nameOfTask, listOfTasks, pane, buttonBox);

        Scene scene = new Scene(layout, 350, 270);
        stage.setScene(scene);
        stage.showAndWait();
    }

}
