package com.alexa4.linguistic_project.view.student_views;

import com.alexa4.linguistic_project.data_stores.TaskConfig;
import com.alexa4.linguistic_project.data_stores.TaskResults;
import com.alexa4.linguistic_project.presenters.student.StudentPresenter;
import com.alexa4.linguistic_project.view.ViewTextInterface;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Class is responsible for displaying window
 * Window contains UI for student to make linguistic task and save it
 */
public class LessonsView extends ViewTextInterface {
    private StudentPresenter mPresenter = null;
    private TaskConfig mConfig = null;
    private Button saveBtn;
    private Label mStatistic;
    private String mTaskName;

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
        Menu taskMenu = new Menu("Задания");
        List<String> fileNames = mPresenter.getFilesNameList();
        for (int i = 0; i < fileNames.size(); i++) {
            MenuItem item = new MenuItem(fileNames.get(i));
            item.setId(String.valueOf(i));
            item.setOnAction(event -> {
                String fileName = fileNames.get(Integer.valueOf(item.getId()));
                textLabel.setText(TASK_CONST + fileName);
                mTaskName = fileName;
                getText(fileName);
                mConfig = mPresenter.getConfig();
                if (mConfig.getFlags().contains(TaskConfig.Flag.SELF_TESTING))
                    saveBtn.setText("Проверить");
                else saveBtn.setText("Сохранить");
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
        saveBtn = new Button("Сохранить");
        saveBtn.setPrefSize(120, 40);
        saveBtn.setFont(new Font(15));
        saveBtn.setTextFill(Paint.valueOf("#F00000"));
        saveBtn.setOnAction(event -> {
            //If this is self-testing mode
            if (mConfig.getFlags().contains(TaskConfig.Flag.SELF_TESTING)) {
                checkAnswers();
            } else { //If this is test-mode
                if (!mPresenter.saveUserAnswer())
                    callAlert("Ошибка сохранения", null,
                            "Ответ не может быть сохранен");
                else callSuccess("Успешно!", null,
                        "Ответ был успешно сохранен");
            }
        });
        buttonBox.getChildren().add(saveBtn);

        mStatistic = new Label();
        HBox statBox = new HBox();
        statBox.setAlignment(Pos.CENTER_LEFT);
        statBox.setMinHeight(70);
        statBox.getChildren().add(mStatistic);

        fileActions.setHgrow(statBox, Priority.ALWAYS);
        fileActions.setHgrow(buttonBox, Priority.ALWAYS);
        fileActions.getChildren().addAll(buttonBox, statBox);

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
     * Display user choice in choiceField like a record
     * Make new record box which contains:
     * <p>
     * ------------------------------
     * textMeans        | correctness
     * userSelectedText | of choice
     * -----------------------------
     *
     * @param means       the name of means of expressiveness
     * @param text        the text of choice
     * @param correctness is the text correct
     */
    protected void addUserChoiceToBox(String means, String text, boolean correctness) {
        HBox newRecordBox = new HBox(10);
        newRecordBox.setAlignment(Pos.CENTER_LEFT);

        Text textMeans = new Text();
        Text userSelectedText = new Text();

        Button correctnessButton = new Button();
        correctnessButton.setStyle("-fx-background-color: #00000000;");
        correctnessButton.setFont(new Font(20));
        if (correctness) {
            correctnessButton.setTextFill(Paint.valueOf("#00ff00"));
            correctnessButton.setText("V");
        } else {
            correctnessButton.setTextFill(Paint.valueOf("#ff0000"));
            correctnessButton.setText("X");
        }


        //Box with means name and with text which user selected
        VBox textBox = new VBox();

        //textMeans will display which means of expressiveness user select
        textMeans.setText("\n" + means.toUpperCase() +
                " is");
        textMeans.setWrappingWidth(CHOICE_FIELD_WIDTH - 70);
        textMeans.setFill(getTextColors());

        //userSelectedText will display which text user select
        userSelectedText.setWrappingWidth(CHOICE_FIELD_WIDTH - 70);
        userSelectedText.setText(text.trim());

        //If user clicked on textBox, then the text will be selected in area
        textBox.getChildren().addAll(textMeans, userSelectedText);
        textBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int start = area.getText().indexOf(userSelectedText.getText());
                int end = userSelectedText.getText().length();
                area.selectRange(start, start + end);
            }
        });
        newRecordBox.getChildren().addAll(textBox, correctnessButton);

        //Add current text to collection
        addUserChoice(means.toLowerCase(), text);

        //Add current box to view
        choiceField.getChildren().addAll(newRecordBox);
    }


    /**
     * Checking user answers if SELF-TESTING mode is activate
     */
    private void checkAnswers() {
        freeUserChoices();

        //TODO: Fix this
        HashMap<String, ArrayList<String>> userMarking = mPresenter.getUserMarking();
        HashMap<String, ArrayList<String>> originalMarking = mPresenter.getOriginalMeans(mTaskName);

        TaskResults results = mPresenter.verifyAndGetCorrectnessOfAnswers(originalMarking, userMarking);

        //Add correct user answers
        userMarking.forEach((means, collection) -> collection
                .forEach(text -> addUserChoiceToBox(means, text, results.getCorrectAnswers().get(text))));

        //Add wrong user answers
        results.getNotFoundedMeans().forEach((means, collection) ->
                collection.forEach(text -> {
                    addUserChoiceToBox(means, text, false);
                })
        );
        double sum = results.getCountOfCorrectAnswers() + results.getCountOfNotFounded();
        String statistic = "Статистика:\n"
                + "Правильно: " + results.getCountOfCorrectAnswers() + " - " + (results.getCountOfCorrectAnswers() / sum * 100)+ "%\n"
                + "Не найдено: " + results.getCountOfNotFounded() + " - " + (results.getCountOfNotFounded() / sum * 100)+ "%\n"
                + "Лишние: " + results.getCountOfExtraAnswers();
        mStatistic.setText(statistic);
    }
}
