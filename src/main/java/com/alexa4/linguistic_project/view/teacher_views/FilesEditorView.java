package com.alexa4.linguistic_project.view.teacher_views;

import com.alexa4.linguistic_project.data_stores.TaskConfig;
import com.alexa4.linguistic_project.presenters.teacher.TeacherPresenter;
import com.alexa4.linguistic_project.view.ViewTextInterface;
import com.alexa4.linguistic_project.view.dialogs.TaskDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Class is responsible for displaying window
 * Window contains UI for teacher to Add new|Modified existing tasks
 */
public class FilesEditorView extends ViewTextInterface {
    private TeacherPresenter mPresenter;
    private TextField fileNameTF;
    private CheckBox mSelfTesting;

    public FilesEditorView(TeacherPresenter presenter) {
        super();
        this.mPresenter = presenter;
    }

    @Override
    public void detachPresenter() {
        mPresenter = null;
    }

    @Override
    protected void addUserChoice(String means, String selectedText) {
        mPresenter.addUserChoice(means, selectedText);
    }

    @Override
    protected Paint getTextColors() {
        return mPresenter.getTextColors();
    }

    @Override
    protected void deleteUserChoice(String means, String selectedText) {
        mPresenter.deleteUserChoice(means, selectedText);
    }


    /**
     * Box contains TF with file name and save button
     * @return the box
     */
    @Override
    protected HBox initUserActions() {
        HBox fileActions = new HBox(20);
        fileActions.setPadding(new Insets(0, WINDOW_RIGHT_PADDING, 30, WINDOW_LEFT_PADDING));

        //Initializing checkBox for self-testing
        mSelfTesting = new CheckBox("Самопроверка");

        //Initializing box with file name
        HBox fileNameBox = new HBox(10);
        Label fileNameLabel = new Label("Название файла: ");
        fileNameBox.setAlignment(Pos.CENTER_LEFT);
        fileNameTF = new TextField("");
        fileNameTF.setTooltip(new Tooltip("Введите название файла"));
        fileNameBox.getChildren().addAll(fileNameLabel, fileNameTF);

        //Initializing box with save button
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button saveBtn = new Button("Сохранить");
        saveBtn.setFont(new Font(18));
        saveBtn.setTextFill(Paint.valueOf("#F00000"));
        saveBtn.setOnAction(event -> {

            //If fileName is empty
            if (fileNameTF.getText().equals("")) {
                callAlert("Имя файла", null, "Имя файла не должно быть пустым");
                return;
            }

            //TODO: add sending config
            TaskDialog.saveTask(area.getText(), fileNameTF.getText(), initConfig(),
                    new TaskDialog.TaskSaverCallback() {
                @Override
                public void sendResultOfSaving(boolean result) {
                    if (!result)
                        callAlert("Ошибка сохранения", null,
                                "Файл " + fileNameTF.getText() + " не может быть сохранен");
                    else callSuccess("Успешно!", null,
                            "Файл " + fileNameTF.getText() + ".txt сохранен");
                }
            });
        });
        buttonBox.getChildren().add(saveBtn);


        VBox actions = new VBox(10);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.getChildren().addAll(mSelfTesting, fileNameBox);

        fileActions.setHgrow(actions, Priority.ALWAYS);
        fileActions.setHgrow(buttonBox, Priority.ALWAYS);
        fileActions.getChildren().addAll(actions, buttonBox);

        return fileActions;
    }


    /**
     * Creating config based on self-testing checkbox
     */
    private TaskConfig initConfig() {
        TaskConfig config = new TaskConfig();

        if (mSelfTesting.isSelected())
            config.addFlagToConfig(TaskConfig.Flag.SELF_TESTING);

        return  config;
    }

    @Override
    protected void getText(String fileName) {
        mPresenter.getText(fileName);
    }

    @Override
    protected void clearUserChoice() {
        mPresenter.clearUserChoice();
    }


    /**
     * Initializing menu bar
     * @return the menu bar
     */
    @Override
    protected HBox initMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(createTasksMenu(), createWindowsMenu());

        HBox barBox = new HBox(5);
        barBox.setAlignment(Pos.CENTER_LEFT);
        barBox.getChildren().addAll(menuBar);

        return barBox;
    }

    @Override
    protected String getUserName() {
        return mPresenter.getUserName();
    }

    @Override
    protected int getUserMode() {
        return mPresenter.getUserMode();
    }

    private Menu createWindowsMenu() {
        Menu windowMenu = new Menu("Окна");

        MenuItem filesEditor = new MenuItem("Редактор");
        filesEditor.setOnAction(e -> {mPresenter.runFilesEditor();});

        MenuItem checkTasks = new MenuItem("Проверка заданий");
        checkTasks.setOnAction(e -> {mPresenter.runAnswersChecker();});

        windowMenu.getItems().addAll(filesEditor, checkTasks);
        return windowMenu;
    }

    /**
     * Creating menu with tasks
     * @return the Menu of tasks
     */
    private Menu createTasksMenu() {
        Menu taskMenu = new Menu("Задания");

        //Menu item response for creating new file
        MenuItem newFile = new MenuItem("Добавить новое");
        newFile.setOnAction(event -> {
            freeUserChoices();
            area.clear();
            textLabel.setText("");
            fileNameTF.clear();
        });
        taskMenu.getItems().add(newFile);

        //Adding separator which split "Add new file" button and others
        taskMenu.getItems().add(new SeparatorMenuItem());

        MenuItem getTask = new MenuItem("Открыть");
        getTask.setOnAction(event -> {
            TaskDialog.getTaskName(new TaskDialog.TaskPickerCallback() {
                @Override
                public void sendTaskName(String taskName) {
                    updateTaskName(taskName);
                    mPresenter.getText(taskName);
                    setConfigurations();
                    fillUserChoice();
                }
            });
        });

        taskMenu.getItems().add(getTask);

        return taskMenu;
    }


    /**
     * Setting configurations of task for UI
     */
    private void setConfigurations() {
        TaskConfig config = mPresenter.getConfig();

        if (config.getFlags().contains(TaskConfig.Flag.SELF_TESTING))
            mSelfTesting.setSelected(true);
    }


    /**
     * Updating label of task name
     */
    public void updateTaskName(String taskName) {
        fileNameTF.setText(taskName);
        textLabel.setText(TASK_CONST + taskName);
    }

    /**
     * Filling choiceFiled by means of expressiveness which contained into file before editing
     * Firstly userChoice is clearing and then filling by existence means
     */
    public void fillUserChoice() {
        freeUserChoices();

        HashMap<String, ArrayList<String>> foundMeans = mPresenter.getFoundMeans();
        foundMeans.forEach((means, collection) -> collection
                .forEach(text -> addUserChoiceToBox(means, text)));
    }
}
