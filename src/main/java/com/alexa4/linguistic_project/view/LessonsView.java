package com.alexa4.linguistic_project.view;

import com.alexa4.linguistic_project.data_stores.User;
import com.alexa4.linguistic_project.models.Model;
import com.alexa4.linguistic_project.presenter.Presenter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.File;
import java.util.List;


/**
 * Class is responsible for displaying window
 */
public class LessonsView implements ViewInterface{
    private Presenter presenter = null;
    private StyleClassedTextArea area;
    private VBox layout;
    private VBox choiceField;

    private int CHOICE_FIELD_WIDTH = 350;

    /**
     * Initializing main layout and text area
     * @param presenter
     */
    public LessonsView(Presenter presenter){
        this.presenter = presenter;
        area = new StyleClassedTextArea();
        layout = new VBox(15);
    }

    /**
     * Getting the layout to put it into Stage
     * @return
     */
    @Override
    public VBox getLayout(){
        initWindow();

        return layout;
    }


    /**
     * Initializing text area
     */
    private StyleClassedTextArea initTextField(){
        area.setWrapText(true);
        area.setEditable(false);
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
                            /**
                             * Make new record box which contains:
                             * ------------------------------
                             * textMeans        |  Button X |
                             * userSelectedText |           |
                             * ------------------------------
                             */
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
                                    presenter.deleteUserChoice(item.getText().toLowerCase(),
                                            userSelectedText.getText());
                                    choiceField.getChildren().remove(newRecordBox);
                                }
                            });


                            //Box with means name and with text which user selected
                            VBox textBox = new VBox();

                            //textMeans will display which means of expressiveness user select
                            textMeans.setText("\n" + item.getText().toUpperCase() +
                                    " is");
                            textMeans.setWrappingWidth(CHOICE_FIELD_WIDTH - 70);
                            textMeans.setFill(presenter.getTextColors());

                            //userSelectedText will display which text user select
                            userSelectedText.setWrappingWidth(CHOICE_FIELD_WIDTH - 70);
                            userSelectedText.setText(area.getSelectedText().trim());

                            textBox.getChildren().addAll(textMeans, userSelectedText);
                            newRecordBox.getChildren().addAll(textBox, deleteRecordButton);

                            //Add current text to collection
                            presenter.setUserChoice(item.getText().toLowerCase(), area.getSelectedText());

                            //Add current box to view
                            choiceField.getChildren().addAll(newRecordBox);
                        }
                    }
                });
                menu.getItems().add(item);
            }

        return menu;
    }


    /**
     * Initializing window by the content, now:
     * TextArea, button to show text
     */
    private void initWindow(){
        HBox mHatBox = initHatBox();

        HBox textBox = new HBox(15);
        textBox.setPadding(new Insets(0, 30, 30, 30));

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


        textBox.getChildren().addAll(area, choicePane);
        layout.getChildren().addAll(mHatBox, textBox);
    }

    /**
     * Initializing the hat of window. It contains data about current user and MenuBar
     * @return the hat of window
     */
    private HBox initHatBox() {
        HBox mUserBox = new HBox(20);
        mUserBox.setAlignment(Pos.CENTER_RIGHT);

        ImageView mImageView = new ImageView();
        Image  image = null;
        try {
            File way = new File(new File("").getAbsolutePath() +
                    "/src/main/resources/defaultUserIcon.jpg");
            image = new Image(way.toURI().toURL().toString());
        } catch (Exception e) {
            callAlert("Read exception", "File not found",
                    "The user\'s icon won\'t be display");
        }
        mImageView.setImage(image);
        mImageView.setFitWidth(24);
        mImageView.setFitHeight(24);

        Label mUserNameLabel = new Label();

        String userMode = presenter.getUserMode() == User.STUDENT_MODE ? "Student:  " : "Teacher:  ";
        mUserNameLabel.setText(userMode + presenter.getUserName());

        HBox menuBox = initMenuBar();

        mUserBox.getChildren().addAll(mImageView, mUserNameLabel);

        HBox mHatBox = new HBox(10);
        mHatBox.setPadding(new Insets(10, 30, 0, 30));
        mHatBox.setHgrow(menuBox, Priority.ALWAYS);
        mHatBox.setHgrow(mUserBox, Priority.ALWAYS);
        mHatBox.getChildren().addAll(menuBox, mUserBox);

        return mHatBox;
    }

    /**
     * Creating menu bar
     * @return the menu bar
     */
    private HBox initMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(createTasksMenu());
        //Create teacher bar if Mode equals to TEACHER_MODE
        if (presenter.getUserMode() == User.TEACHER_MODE)
            menuBar.getMenus().add(createTeacherMenu());


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
        List<String> fileNames = presenter.getFilesNameList();
        for (int i = 0; i < fileNames.size(); i++) {
            MenuItem item = new MenuItem(fileNames.get(i));
            item.setId(String.valueOf(i));
            item.setOnAction(event -> {
                presenter.getText(fileNames.get(Integer.valueOf(item.getId())));
            });

            taskMenu.getItems().add(item);
        }

        return taskMenu;
    }


    /**
     * Creating menu to check works, ONLY for teacher mode
     * @return
     */
    private Menu createTeacherMenu() {
        Menu teacherMenu = new Menu("Check works");

        MenuItem addItem= new MenuItem("Add new file");
        addItem.setOnAction(event -> {
            presenter.createNewTasksFile();
        });

        teacherMenu.getItems().add(addItem);

        return teacherMenu;
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
    private void callAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
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

    @Override
    public void detachPresenter() {
        presenter = null;
    }
}
