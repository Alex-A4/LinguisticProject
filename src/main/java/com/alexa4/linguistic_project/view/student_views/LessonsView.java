package com.alexa4.linguistic_project.view.student_views;

import com.alexa4.linguistic_project.presenters.student.StudentPresenter;
import com.alexa4.linguistic_project.view.ViewTextInterface;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.List;


/**
 * Class is responsible for displaying window
 */
public class LessonsView extends ViewTextInterface {
    private StudentPresenter mPresenter = null;


    /**
     * Initializing main layout and text area
     * @param presenter
     */
    public LessonsView(StudentPresenter presenter){
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
    public void deleteUserChoice(String means, String selectedText) {
        mPresenter.deleteUserChoice(means, selectedText);
    }

    /**
     * Creating menu bar
     * @return the menu bar
     */
    @Override
    protected HBox initMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(createTasksMenu());

        HBox barBox = new HBox(5);
        barBox.setAlignment(Pos.CENTER_LEFT);
        barBox.getChildren().addAll(menuBar);

        return barBox;
    }
    /**
     * Creating menu with tasks
     * @return
     */
    private Menu createTasksMenu() {
        Menu taskMenu = new Menu("Lessons");
        List<String> fileNames = mPresenter.getFilesNameList();
        for (int i = 0; i < fileNames.size(); i++) {
            MenuItem item = new MenuItem(fileNames.get(i));
            item.setId(String.valueOf(i));
            item.setOnAction(event -> {
                String fileName = fileNames.get(Integer.valueOf(item.getId()));
                textLabel.setText(TASK_CONST + fileName);
                getText(fileName);
                freeUserChoices();
            });

            taskMenu.getItems().add(item);
        }

        return taskMenu;
    }


    @Override
    protected String getUserName() {
        return mPresenter.getUserName();
    }

    @Override
    protected int getUserMode() {
        return mPresenter.getUserMode();
    }

    @Override
    protected HBox initUserActions() {
        HBox fileActions = new HBox(20);
        fileActions.setPadding(new Insets(0, WINDOW_RIGHT_PADDING, 30,
                WINDOW_LEFT_PADDING));

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button saveBtn = new Button("Save result");
        saveBtn.setPrefSize(120, 40);
        saveBtn.setFont(new Font(15));
        saveBtn.setTextFill(Paint.valueOf("#F00000"));
        saveBtn.setOnAction(event -> {

            if (!mPresenter.saveUserAnswer())
                callAlert("Saving error", null,
                        "The answer could not be saved");
            else callSuccess("Saving success", null,
                    "The answer saved successfully");

        });
        buttonBox.getChildren().add(saveBtn);

        fileActions.setHgrow(buttonBox, Priority.ALWAYS);
        fileActions.getChildren().addAll(buttonBox);

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
}
