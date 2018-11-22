package com.alexa4.linguistic_project.view;

import com.alexa4.linguistic_project.data_stores.MeansOfExpressiveness;
import com.alexa4.linguistic_project.data_stores.User;
import com.alexa4.linguistic_project.presenters.AuthentificationPresenter;
import com.alexa4.linguistic_project.view.auth_views.LogInView;
import com.alexa4.linguistic_project.view.dialogs.AlertDialog;
import com.sun.istack.internal.NotNull;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.File;

public abstract class ViewTextInterface implements ViewInterface {
    //Text area which contains text of task
    protected StyleClassedTextArea area;
    //Layout of window which will set to Stage
    protected VBox layout;
    //Box which contains means of expressions which user select
    protected VBox choiceField;
    //The label which show current open task
    protected Label textLabel;

    protected static final int CHOICE_FIELD_WIDTH = 350;
    protected static final int WINDOW_LEFT_PADDING = 30;
    protected static final int WINDOW_RIGHT_PADDING = 30;

    protected static final String TASK_CONST = "Task: ";

    public ViewTextInterface() {
    }


    /**
     * Detach presenter from view
     */
    @Override
    public abstract void detachPresenter();

    /**
     * Getting the layout to put it into Stage
     * @return the layout of window
     */
    @Override
    public VBox getLayout(){
        layout = new VBox(15);
        initWindow();

        return layout;
    }

    /**
     * Initializing text area
     */
     protected StyleClassedTextArea initTextField() {
        area = new StyleClassedTextArea();
        area.setWrapText(true);
        int userMode = getUserMode();
        area.setEditable(userMode == User.TEACHER_MODE);
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
     protected ContextMenu initContextMenu(){
        ContextMenu menu = new ContextMenu();

        for (MeansOfExpressiveness means : MeansOfExpressiveness.values()) {
            //Initializing menuItem with title of Means. First character is in up case
            MenuItem item = new MenuItem(means.getText().substring(0, 1)
                    .toUpperCase().concat(means.getText().substring(1)));
            //If user choose some selected text, then add he's choice to choiceField and to collection
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    //If selected text is not empty, then display user choice
                    if (!area.getSelectedText().trim().equals("")) {
                        addUserChoiceToBox(item.getText(), area.getSelectedText());
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
    protected void addUserChoiceToBox(String means, String text) {
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
                deleteUserChoice(means.toLowerCase(),
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
        newRecordBox.getChildren().addAll(textBox, deleteRecordButton);

        //Add current text to collection
        addUserChoice(means.toLowerCase(), text);

        //Add current box to view
        choiceField.getChildren().addAll(newRecordBox);
    }

    /**
     * Set the choice of user to model
     * @param means the selected means
     * @param selectedText the text which means wrap
     */
    protected abstract void addUserChoice(String means, String selectedText);

    /**
     * Getting specified color which will set to text in choiceField
     * @return
     */
    protected abstract Paint getTextColors();

    /**
     * Deleting the choice of user from model
     * @param means the deleting means
     * @param selectedText the text which means wrap
     */
    protected abstract void deleteUserChoice(String means, String selectedText);

    /**
     * Initializing window by the content, now:
     * TextArea, button to show text
     */
    protected void initWindow(){
        HBox mHatBox = initHatBox();

        HBox textBox = new HBox(15);
        textBox.setPadding(new Insets(0, WINDOW_RIGHT_PADDING, 30, WINDOW_LEFT_PADDING));

        area = initTextField();

        ScrollPane choicePane = initChoiceScrollPane();

        HBox textBoxLabels = initTextBoxLabels();

        textBox.getChildren().addAll(area, choicePane);

        HBox userActions = initUserActions();

        layout.getChildren().addAll(mHatBox, textBoxLabels, textBox, userActions);
    }

    /**
     * Initializing the hat of window. It contains data about current user and MenuBar
     * @return the hat of window
     */
     protected HBox initHatBox() {
        HBox mUserBox = new HBox(20);
        mUserBox.setAlignment(Pos.CENTER_RIGHT);

        ImageView mImageView = new ImageView();
        Image image = null;
        try {
            File way = new File(new File("").getAbsolutePath() +
                    "/resources/defaultUserIcon.jpg");
            image = new Image(way.toURI().toURL().toString());
        } catch (Exception e) {
            callAlert("Read exception", "File not found",
                    "The user\'s icon won\'t be display");
        }
        mImageView.setImage(image);
        mImageView.setFitWidth(24);
        mImageView.setFitHeight(24);

        Label mUserNameLabel = new Label();

        String userMode = getUserMode() == User.STUDENT_MODE ? "Student:  " : "Teacher:  ";
        mUserNameLabel.setText(userMode + getUserName());

        HBox menuBox = initMenuBar();
         ((MenuBar) menuBox.getChildren().get(0)).getMenus().add(0, initFileMenuItem());

        mUserBox.getChildren().addAll(mImageView, mUserNameLabel);

        HBox mHatBox = new HBox(10);
        mHatBox.setPadding(new Insets(0, 30, 0, 0));
        mHatBox.setHgrow(menuBox, Priority.ALWAYS);
        mHatBox.setHgrow(mUserBox, Priority.ALWAYS);
        mHatBox.getChildren().addAll(menuBox, mUserBox);

        return mHatBox;
    }

    /**
     * Creating item of menu which allow to change user
     * @return
     */
    private Menu initFileMenuItem() {
         Menu menu = new Menu("File");

         MenuItem file = new MenuItem("Change user");
         //Go to LogInView
         file.setOnAction(e -> {
             new AuthentificationPresenter().start();
         });

         menu.getItems().add(file);
        return menu;
    }

    /**
     * Abstract method to initialize menuBar. SubClass fill HBox
     * The 0 child of HBox must be MenuBar
     * @return the menuBar which will set on top
     */
    protected abstract HBox initMenuBar();

    /**
     * Getting specified name of user
     * @return the userName
     */
    protected abstract String getUserName();

    /**
     * Getting specified mode of user
     * @return the userMode
     */
    protected abstract int getUserMode();

    /**
     * Box contains TF with file name and save button
     * @return the box
     */
    protected abstract HBox initUserActions();

    /**
     * Getting specified text which will set to area
     * @param fileName the name of file which text need to get
     */
    protected abstract void getText(String fileName);


    /**
     * Clearing choiceField
     */
    protected void freeUserChoices() {
        clearUserChoice();

        //Clear choiceField
        int count = choiceField.getChildren().size();
        for (int i = 0; i < count; i++)
            choiceField.getChildren().remove(0);
    }

    protected abstract void clearUserChoice();


    /**
     * Initializing box which contains labels for TextBox and ChoiceBox
     * textLabel is changing when user select some task
     * @return the labelsBox
     */
     protected HBox initTextBoxLabels() {
        HBox mLabelBox = new HBox(5);
        mLabelBox.setPadding(new Insets(15, WINDOW_RIGHT_PADDING, 5, WINDOW_LEFT_PADDING));

        textLabel = new Label(TASK_CONST);
        textLabel.setFont(new Font(16));
        HBox textBox = new HBox();
        textBox.getChildren().add(textLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Label choiceLabel = new Label("Selected means of expression");
        choiceLabel.setFont(new Font(16));
        HBox choiceBox = new HBox();
        choiceBox.getChildren().add(choiceLabel);
        choiceBox.setAlignment(Pos.CENTER_RIGHT);

        mLabelBox.setHgrow(textBox, Priority.ALWAYS);
        mLabelBox.setHgrow(choiceBox, Priority.ALWAYS);
        mLabelBox.getChildren().addAll(textBox, choiceBox);

        return mLabelBox;
    }



    /**
     * Initializing text file where will display user's choice
     * @return
     */
     protected ScrollPane initChoiceScrollPane() {
        choiceField = new VBox();

        choiceField.setPrefWidth(CHOICE_FIELD_WIDTH);
        choiceField.setMaxWidth(CHOICE_FIELD_WIDTH);

        choiceField.setPadding(new Insets(10, 10, 10, 10));

         ScrollPane choicePane = new ScrollPane();
         choicePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
         choicePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
         choicePane.setContent(choiceField);
         choicePane.setStyle("-fx-background-color:transparent;");
         choicePane.setBorder(new Border(new BorderStroke(
                 Paint.valueOf("#000000"), BorderStrokeStyle.SOLID,  CornerRadii.EMPTY,
                 BorderWidths.DEFAULT
         )));

        return choicePane;
    }


    /**
     * Call Dialog Alert if something happens
     * @param title the title of Alert
     * @param header the header of Alert
     * @param content the content text of Alert
     */
    protected void callAlert(@NotNull String title, String header, @NotNull String content){
        AlertDialog.callErrorAlert(title, header, content);
    }

    /**
     * Call Dialog Alert if action completed successfully
     * @param title the title of Alert
     * @param header the header of Alert
     * @param content the context of Alert
     */
    protected void callSuccess(@NotNull String title, String header, @NotNull String content) {
        AlertDialog.callSuccessAlert(title, header, content);
    }



    /**
     * Setting text to StyleClassedTextArea
     * @param text
     */
    public void setText(String text) {
        area.clear();
        area.appendText(text);
        area.scrollToPixel(0, 0);
    }

}
