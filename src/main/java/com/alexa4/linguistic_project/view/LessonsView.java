package com.alexa4.linguistic_project.view;

import com.alexa4.linguistic_project.models.Model;
import com.alexa4.linguistic_project.presenter.Presenter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.File;


/**
 * Class is responsible for displaying window
 */
public class LessonsView implements ViewInterface{
    private Presenter presenter = null;
    private StyleClassedTextArea area;
    private VBox layout;
    private VBox choiceField;

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
        area.setPrefSize(700, 400);
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

        for (Model.MeansOfExpressiveness means: Model.MeansOfExpressiveness.values()) {
            //Initializing menuItem with title of Means. First character is in up case
            MenuItem item = new MenuItem(means.getText().substring(0, 1)
                    .toUpperCase().concat(means.getText().substring(1)));
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Text textMeans = new Text();
                    textMeans.setText(item.getText().toUpperCase() +
                            " is\n");
                    textMeans.setWrappingWidth(choiceField.getWidth()-30);
                    textMeans.setFill(presenter.getTextColors());

                    Text userSelectedText = new Text();
                    userSelectedText.setText(area.getSelectedText().trim() + "\n");
                    userSelectedText.setWrappingWidth(choiceField.getWidth()-30);

                    choiceField.getChildren().addAll(textMeans, userSelectedText);
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
        textBox.setPadding(new Insets(30, 30, 30, 30));

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

        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);

        Button bGetText = new Button("Show text");
        bGetText.setOnAction(event -> {
            getTextFromPresenter();
        });

        buttonsBox.getChildren().add(bGetText);

        layout.getChildren().add(buttonsBox);
    }

    private HBox initHatBox() {
        HBox mHatBox = new HBox(20);
        mHatBox.setAlignment(Pos.CENTER_RIGHT);
        mHatBox.setPadding(new Insets(10, 30, 0, 30));

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
        mUserNameLabel.setText(presenter.getUserName());

        mHatBox.getChildren().addAll(mImageView, mUserNameLabel);

        return mHatBox;
    }

    /**
     * Initializing text file where will display user's choice
     * @return
     */
    private VBox initChoiceVBox() {
        choiceField = new VBox();

        choiceField.setPrefWidth(300);

        choiceField.setPadding(new Insets(10, 10, 10, 10));

        return choiceField;
    }

    private void getTextFromPresenter(){
        presenter.getText();
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
