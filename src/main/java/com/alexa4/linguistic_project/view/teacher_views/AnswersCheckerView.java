package com.alexa4.linguistic_project.view.teacher_views;

import com.alexa4.linguistic_project.presenters.teacher.TeacherPresenter;
import com.alexa4.linguistic_project.view.ViewTextInterface;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class is responsible for displaying window
 * Window contains UI for teacher to check students tasks
 */
public class AnswersCheckerView extends ViewTextInterface {
    private TeacherPresenter mPresenter;

    public AnswersCheckerView(TeacherPresenter presenter) {
        super();
        mPresenter = presenter;
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
     * @return the box with actions
     */
    @Override
    protected HBox initUserActions() {
        return new HBox(10);
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

    /**
     * Creating menu which contains list of window we can open
     * @return the menu of windows
     */
    private Menu createWindowsMenu() {
        Menu windowMenu = new Menu("Windows");

        MenuItem filesEditor = new MenuItem("Files editor");
        filesEditor.setOnAction(e -> {mPresenter.runFilesEditor();});

        MenuItem checkTasks = new MenuItem("Check tasks");
        checkTasks.setOnAction(e -> {mPresenter.runAnswersChecker();});

        windowMenu.getItems().addAll(checkTasks);
        return windowMenu;
    }

    /**
     * Creating menu with tasks
     * @return the menu which contains list of tasks
     */
    private Menu createTasksMenu() {
        Menu taskMenu = new Menu("Lessons");
        List<String> fileNames = mPresenter.getFilesNameList();

        //Menu items for opening files
        for (int i = 0; i < fileNames.size(); i++) {
            MenuItem item = new MenuItem(fileNames.get(i));
            item.setId(String.valueOf(i));
            item.setOnAction(event -> {
                mPresenter.getText(fileNames.get(Integer.valueOf(item.getId())));
                fillUserChoice();
            });

            taskMenu.getItems().add(item);
        }

        return taskMenu;
    }

    /**
     * Overriding parent method to make own UI
     */
    @Override
    protected void initWindow() {
        HBox mHatBox = initHatBox();

        HBox textBox = new HBox(15);
        textBox.setPadding(new Insets(0, WINDOW_RIGHT_PADDING, 30, WINDOW_LEFT_PADDING));

        area = initTextField();

        choiceField = initChoiceVBox();
        ScrollPane choicePane = new ScrollPane();
        choicePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        choicePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        choicePane.setContent(choiceField);
        choicePane.setBorder(new Border(new BorderStroke(
                Paint.valueOf("#000000"), BorderStrokeStyle.SOLID,  CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        )));

        HBox textBoxLabels = initTextBoxLabels();

        textBox.getChildren().addAll(area, choicePane);

        //Box contains textBoxLabels and textBox
        VBox textWorkerBox = new VBox();
        textWorkerBox.getChildren().addAll(textBoxLabels, textBox);

        //Box contains hierarchy of answers, text of task and choice field
        HBox workPlace = new HBox(10);
        workPlace.getChildren().addAll(textWorkerBox);

        //Box contains UI with navigation
        HBox userActions = initUserActions();

        layout.getChildren().addAll(mHatBox, workPlace, userActions);
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
