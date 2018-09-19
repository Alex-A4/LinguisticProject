package com.alexa4.linguistic_project.view;

import com.alexa4.linguistic_project.models.Model;
import com.alexa4.linguistic_project.presenter.Presenter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.StyleClassedTextArea;


/**
 * Class is responsible for displaying window
 */
public class MainView implements ViewInterface{
    private Presenter presenter = null;
    private StyleClassedTextArea area;
    private VBox layout;

    /**
     * Initializing main layout and text area
     * @param presenter
     */
    public MainView(Presenter presenter){
        this.presenter = presenter;
        area = new StyleClassedTextArea();
        layout = new VBox(15);
    }

    /**
     * Getting the layout to put it into Stage
     * @return
     */
    public VBox getLayout(){
        initWindow();

        return layout;
    }


    /**
     * Initializing text area
     */
    private void initTextField(){
        area.setWrapText(true);
        area.setEditable(false);
        area.setPadding(new Insets(30, 15, 15, 30));
        area.setPrefSize(700, 400);

        //Setting pop-up menu, which will offer user select one of Means of expressiveness
        ContextMenu menu = initContextMenu();
        area.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                menu.show(area, event.getScreenX(), event.getScreenY());
            }
        });

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
                    System.out.println(area.getSelectedText() + " is " + item.getText());
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
        initTextField();
        HBox textBox = new HBox(15);

        textBox.getChildren().add(area);
        layout.getChildren().add(textBox);

        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);

        Button bGetText = new Button("Show text");
        bGetText.setOnAction(event -> {
            getTextFromPresenter();
        });

        buttonsBox.getChildren().add(bGetText);

        layout.getChildren().add(buttonsBox);
    }

    private void getTextFromPresenter(){
        presenter.getText();
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
