package com.alexa4.linguistic_project.view.teacher_views;

import com.alexa4.linguistic_project.data_stores.TaskResults;
import com.alexa4.linguistic_project.presenters.teacher.TeacherPresenter;
import com.alexa4.linguistic_project.view.ViewTextInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class is responsible for displaying window
 * Window contains UI for teacher to check students tasks
 */
public class AnswersCheckerView extends ViewTextInterface {
    private TeacherPresenter mPresenter;
    private String mTaskName = null;
    //Label with text statistic
    private Label mStatistic;

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
     *
     * @return the box with actions
     */
    @Override
    protected HBox initUserActions() {
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(0,30,30,30));

        Label openFullTask = new Label("Открыть оригинальную разметку");
        openFullTask.setTextFill(Paint.valueOf("#0000AF"));
        openFullTask.setFont(new Font(15));
        openFullTask.setCursor(Cursor.CLOSED_HAND);
        openFullTask.setPadding(new Insets(0, 0, 20, 0));
        openFullTask.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (mTaskName == null)
                    return;

                mPresenter.runModalityEditorWithTaskName(mTaskName);
            }
        });
        HBox openBox = new HBox();
        openBox.getChildren().add(openFullTask);
        openBox.setAlignment(Pos.CENTER_RIGHT);

        mStatistic = new Label();

        HBox statBox = new HBox();
        statBox.getChildren().add(mStatistic);
        statBox.setAlignment(Pos.BOTTOM_RIGHT);
        statBox.setMinHeight(70);

        actions.setHgrow(statBox, Priority.ALWAYS);
        actions.setHgrow(openBox, Priority.ALWAYS);
        actions.getChildren().addAll(openBox, statBox);

        return actions;
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
     *
     * @return the menu bar
     */
    @Override
    protected HBox initMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(createWindowsMenu());

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
     *
     * @return the menu of windows
     */
    private Menu createWindowsMenu() {
        Menu windowMenu = new Menu("Окна");

        MenuItem filesEditor = new MenuItem("Редактор");
        filesEditor.setOnAction(e -> {
            mPresenter.runFilesEditor();
        });

        MenuItem checkTasks = new MenuItem("Проверка заданий");
        checkTasks.setOnAction(e -> {
            mPresenter.runAnswersChecker();
        });

        windowMenu.getItems().addAll(filesEditor, checkTasks);
        return windowMenu;
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
        area.setEditable(false);

        ScrollPane choicePane = initChoiceScrollPane();

        //Init box which contains head labels for textBox
        HBox textBoxLabels = initTextBoxLabels();
        textLabel.setText("");

        //Init box which contains hierarchy of answers on current task
        VBox answersHierarchy = initAnswersHierarchy();

        textBox.getChildren().addAll(answersHierarchy, area, choicePane);


        //Box contains UI with navigation
        HBox userActions = initUserActions();

        layout.getChildren().addAll(mHatBox, textBoxLabels, textBox, userActions);
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
     * Filling choiceFiled by means of expressiveness which contained into file before editing
     * Firstly userChoice is clearing and then filling by existence means
     */
    private void fillUserChoice() {
        freeUserChoices();

        HashMap<String, ArrayList<String>> userMarking = mPresenter.getFoundMeans();
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
        int sum = results.getCountOfCorrectAnswers() + results.getCountOfNotFounded();
        String statistic = "Статистика:\n"
                + "Правильно: " + results.getCountOfCorrectAnswers() + " - " + (results.getCountOfCorrectAnswers() / sum)+ "%\n"
                + "Не найдено: " + results.getCountOfNotFounded() + " - " + (results.getCountOfNotFounded() / sum)+ "%\n"
                + "Лишние: " + results.getCountOfExtraAnswers();
        mStatistic.setText(statistic);
    }

    /**
     * Initializing hierarchy of users answers
     *
     * @return the box with hierarchy of students tasks
     */
    private VBox initAnswersHierarchy() {
        VBox mHierarchy = new VBox(10);
        mHierarchy.setPrefWidth(150);
        HashMap<String, ArrayList<String>> answersMap = mPresenter.getAnswersMap();

        TreeItem<String> root = new TreeItem<>("Студенты:");
        TreeView<String> mAnswersTree = new TreeView<>(root);

        answersMap.forEach((user, list) -> {
            TreeItem<String> tasksRoot = new TreeItem<>(user);
            root.getChildren().add(tasksRoot);

            list.forEach(task -> {
                TreeItem<String> item = new TreeItem<>(task);
                tasksRoot.getChildren().add(item);
            });
        });

        MultipleSelectionModel<TreeItem<String>> selectionModel = mAnswersTree.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable,
                                TreeItem<String> oldValue, TreeItem<String> newValue) {

                //If selected student answer then get its text
                if (newValue.getParent() != null && answersMap.containsKey(newValue.getParent().getValue())) {
                    mPresenter.getAnswerText(newValue.getParent().getValue(), newValue.getValue());
                    textLabel.setText(newValue.getParent().getValue() + ": " + newValue.getValue());
                    mTaskName = newValue.getValue();
                    fillUserChoice();
                }
            }
        });

        mHierarchy.getChildren().add(mAnswersTree);
        return mHierarchy;
    }
}
