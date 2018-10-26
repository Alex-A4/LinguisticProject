package com.alexa4.linguistic_project.view.teacher_views;

import com.alexa4.linguistic_project.presenters.teacher.TeacherPresenter;
import com.alexa4.linguistic_project.view.ViewTextInterface;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Class is responsible for displaying window
 * Window contains UI for teacher to Add new|Modified existing tasks
 */
public class FilesEditorView extends ViewTextInterface {
    private TeacherPresenter mPresenter;
    private TextField fileNameTF;

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

        HBox fileNameBox = new HBox(10);
        Label fileNameLabel = new Label("File name: ");
        fileNameBox.setAlignment(Pos.CENTER_LEFT);
        fileNameTF = new TextField("");
        fileNameTF.setTooltip(new Tooltip("Enter file name"));
        fileNameBox.getChildren().addAll(fileNameLabel, fileNameTF);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button saveBtn = new Button("Save");
        saveBtn.setPrefSize(80, 40);
        saveBtn.setFont(new Font(20));
        saveBtn.setTextFill(Paint.valueOf("#F00000"));
        saveBtn.setOnAction(event -> {

            //If fileName is empty
            if (fileNameTF.getText().equals("")) {
                callAlert("File name", null, "File name must not be empty");
                return;
            }

            if (!mPresenter.saveFileChanges(area.getText(), fileNameTF.getText()))
                callAlert("Saving error", null,
                        "File" + fileNameTF.getText() + "could not be saved");
            else callSuccess("Saving success", null,
                    "File " + fileNameTF.getText() + ".txt saved");
        });
        buttonBox.getChildren().add(saveBtn);

        fileActions.setHgrow(fileNameBox, Priority.ALWAYS);
        fileActions.setHgrow(buttonBox, Priority.ALWAYS);
        fileActions.getChildren().addAll(fileNameBox, buttonBox);

        return fileActions;
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
        Menu windowMenu = new Menu("Windows");

        MenuItem checkTasks = new MenuItem("Check tasks");
        windowMenu.getItems().addAll(checkTasks);
        return windowMenu;
    }

    /**
     * Creating menu with tasks
     * @return
     */
    private Menu createTasksMenu() {
        Menu taskMenu = new Menu("Lessons");
        List<String> fileNames = mPresenter.getFilesNameList();

        //Menu item response for creating new file
        MenuItem newFile = new MenuItem("Add new file");
        newFile.setOnAction(event -> {
            freeUserChoices();
            area.clear();
            fileNameTF.clear();
        });
        taskMenu.getItems().add(newFile);

        //Adding separator which split "Add new file" button and others
        taskMenu.getItems().add(new SeparatorMenuItem());

        //Menu items for opening files
        for (int i = 0; i < fileNames.size(); i++) {
            MenuItem item = new MenuItem(fileNames.get(i));
            item.setId(String.valueOf(i));
            item.setOnAction(event -> {
                fileNameTF.setText(item.getText());
                mPresenter.getText(fileNames.get(Integer.valueOf(item.getId())));
                fillUserChoice();
            });

            taskMenu.getItems().add(item);
        }

        return taskMenu;
    }

    /**
     * Filling choiceFiled by means of expressiveness which contained into file before editing
     * Firstly userChoice is clearing and then filling by existence means
     */
    private void fillUserChoice() {
        freeUserChoices();

        HashMap<String, ArrayList<String>> foundMeans = mPresenter.getFoundMeans();
        foundMeans.forEach((means, collection) -> collection
                .forEach(text -> addUserChoiceToBox(means, text)));
    }
}
