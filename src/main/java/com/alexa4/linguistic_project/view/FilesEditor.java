package com.alexa4.linguistic_project.view;

import com.alexa4.linguistic_project.models.Model;
import com.alexa4.linguistic_project.presenter.Presenter;
import com.sun.istack.internal.NotNull;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilesEditor implements ViewInterface {
    private Presenter presenter;
    private StyleClassedTextArea area;
    private VBox layout;
    private VBox choiceField;
    private TextField fileNameTF;

    private static final int CHOICE_FIELD_WIDTH = 350;
    private static final int WINDOW_LEFT_PADDING = 30;
    private static final int WINDOW_RIGHT_PADDING = 30;

    public FilesEditor(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void detachPresenter() {
        presenter = null;
    }

    @Override
    public VBox getLayout() {
        layout = new VBox(10);
        initWindow();

        return layout;
    }


    /**
     * Initializing text area
     */
    private StyleClassedTextArea initTextField(){
        area = new StyleClassedTextArea();
        area.setWrapText(true);
        area.setEditable(true);
        area.setPadding(new Insets(10, 10, 10, 10));
        area.setPrefSize(600, 400);
        area.setBorder(new Border(new BorderStroke(
                Paint.valueOf("#000000"), BorderStrokeStyle.SOLID,  CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        )));

        //Setting pop-up menu, which will offer user select one of Means of expressiveness
        ContextMenu menu = initContextMenu();

        area.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                menu.show(area, event.getScreenX(),
                        event.getScreenY());
            }
        });

        return area;
    }

    /**
     * Initializing pop-up menu with means of expressiveness
     * @return pop-up menu
     */
    private ContextMenu initContextMenu(){
        ContextMenu menu = new ContextMenu();

        for (Model.MeansOfExpressiveness means : Model.MeansOfExpressiveness.values()) {
            //Initializing menuItem with title of Means. First character is in up case
            MenuItem item = new MenuItem(means.getText().substring(0, 1)
                    .toUpperCase().concat(means.getText().substring(1)));
            //If user choose some selected text, then add he's choice to choiceField and to collection
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    //If selected text is not empty, then display user choice
                    if (!area.getSelectedText().trim().equals("")) {
                        addUserChoice(item.getText(), area.getSelectedText());
                    }
                }
            });
            menu.getItems().add(item);
        }

        return menu;
    }

    /**
     * Display user choice in choiceField like a record
     *
     * Make new record box which contains:
     * ------------------------------
     * textMeans        |  Button X |
     * userSelectedText |           |
     * ------------------------------
     *
     */
    private void addUserChoice(String means, String text) {
        HBox newRecordBox = new HBox(10);
        newRecordBox.setAlignment(Pos.CENTER_LEFT);

        Text textMeans = new Text();
        Text userSelectedText = new Text();

        //Button X will delete current box from list
        Button deleteRecordButton = new Button("X");
        deleteRecordButton.setStyle("-fx-background-color: #00000000;");
        deleteRecordButton.setCursor(Cursor.CLOSED_HAND);
        deleteRecordButton.setTextFill(Paint.valueOf("#ff0000"));
        deleteRecordButton.setFont(new Font(20));

        //If user pressed X button, then delete this box and delete user's choice
        //which connect with that record
        deleteRecordButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                presenter.deleteUserChoice(means.toLowerCase(),
                        userSelectedText.getText());
                choiceField.getChildren().remove(newRecordBox);
            }
        });


        //Box with means name and with text which user selected
        VBox textBox = new VBox();

        //textMeans will display which means of expressiveness user select
        textMeans.setText("\n" + means.toUpperCase() +
                " is");
        textMeans.setWrappingWidth(CHOICE_FIELD_WIDTH - 70);
        textMeans.setFill(presenter.getTextColors());

        //userSelectedText will display which text user select
        userSelectedText.setWrappingWidth(CHOICE_FIELD_WIDTH - 70);
        userSelectedText.setText(text.trim());

        textBox.getChildren().addAll(textMeans, userSelectedText);
        textBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int start = area.getText().indexOf(userSelectedText.getText());
                int end = userSelectedText.getText().length();
                area.selectRange(start, start + end);
            }
        });
        newRecordBox.getChildren().addAll(textBox, deleteRecordButton);

        //Add current text to collection
        presenter.setUserChoice(means.toLowerCase(), text);

        //Add current box to view
        choiceField.getChildren().addAll(newRecordBox);
    }


    /**
     * Initializing window by the content, now:
     * TextArea, button to show text
     */
    private void initWindow() {
        HBox textBox = new HBox(15);
        textBox.setPadding(new Insets(0, WINDOW_RIGHT_PADDING, 30, WINDOW_LEFT_PADDING));

        area = initTextField();

        HBox menuBar = initMenuBar();

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
        
        HBox fileActions = initFileActions();
        
        layout.getChildren().addAll(menuBar, textBoxLabels, textBox, fileActions);
    }


    /**
     * Initializing box which contains labels for text box
     * @return the labelsBox
     */
    private HBox initTextBoxLabels() {
        HBox mLabelBox = new HBox(5);
        mLabelBox.setPadding(new Insets(15, WINDOW_RIGHT_PADDING, 5, WINDOW_LEFT_PADDING));

        Label textLabel = new Label("Text");
        textLabel.setFont(new Font(18));
        HBox textBox = new HBox();
        textBox.getChildren().add(textLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Label choiceLabel = new Label("Selected means of expression");
        choiceLabel.setFont(new Font(18));
        HBox choiceBox = new HBox();
        choiceBox.getChildren().add(choiceLabel);
        choiceBox.setAlignment(Pos.CENTER_RIGHT);

        mLabelBox.setHgrow(textBox, Priority.ALWAYS);
        mLabelBox.setHgrow(choiceBox, Priority.ALWAYS);
        mLabelBox.getChildren().addAll(textBox, choiceBox);

        return mLabelBox;
    }

    /**
     * Box contains TF with file name and save button
     * @return the box
     */
    private HBox initFileActions() {
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

            if (!presenter.saveFileChanges(area.getText(), fileNameTF.getText()))
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


    /**
     * Initializing menu bar
     * @return the menu bar
     */
    private HBox initMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(createTasksMenu(), createWindowsMenu());

        HBox barBox = new HBox(5);
        barBox.setAlignment(Pos.CENTER_LEFT);
        barBox.getChildren().addAll(menuBar);

        return barBox;
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
        List<String> fileNames = presenter.getFilesNameList();

        //Menu item response for creating new file
        MenuItem newFile = new MenuItem("Add new file");
        newFile.setOnAction(event -> {
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
                presenter.getText(fileNames.get(Integer.valueOf(item.getId())));
                fillUserChoice();
            });

            taskMenu.getItems().add(item);
        }

        return taskMenu;
    }

    /**
     * Filling choiceFiled by means of expressiveness which contained into file before editing
     */
    private void fillUserChoice() {
        //Clear choiceField
        int count = choiceField.getChildren().size();
        for (int i = 0; i < count; i++)
            choiceField.getChildren().remove(0);

        HashMap<String, ArrayList<String>> foundMeans = presenter.getFoundMeans();
        foundMeans.forEach((means, collection) -> collection
                .forEach(text -> addUserChoice(means, text)));
    }


    /**
     * Initializing text file where will display user's choice
     * @return
     */
    private VBox initChoiceVBox() {
        choiceField = new VBox();

        choiceField.setPrefWidth(CHOICE_FIELD_WIDTH);
        choiceField.setMaxWidth(CHOICE_FIELD_WIDTH);

        choiceField.setPadding(new Insets(10, 10, 10, 10));

        return choiceField;
    }


    /**
     * Call Dialog Alert if something happens
     * @param title the title of Alert
     * @param header the header of Alert
     * @param content the content text of Alert
     */
    private void callAlert(@NotNull String title, String header, @NotNull String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        if (header != null)
            alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Call Dialog Alert if action completed successfully
     * @param title the title of Alert
     * @param header the header of Alert
     * @param content the context of Alert
     */
    private void callSuccess(@NotNull String title, String header, @NotNull String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        if (header != null)
            alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Setting text to StyleClassedTextArea
     * @param text
     */
    @Override
    public void setText(String text) {
        area.clear();
        area.appendText(text);
        area.scrollToPixel(0, 0);
    }
}
