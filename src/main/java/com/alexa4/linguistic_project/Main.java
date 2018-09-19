package com.alexa4.linguistic_project;

import com.alexa4.linguistic_project.models.Model;
import com.alexa4.linguistic_project.presenter.Presenter;
import com.alexa4.linguistic_project.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launching the app
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Presenter presenter = new Presenter(new Model());
        MainView view = new MainView(presenter);
        presenter.setView(view);

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(1000);

        primaryStage.setMaxHeight(600);
        primaryStage.setMaxWidth(1000);

        primaryStage.setTitle("Linguistic project");

        Scene scene = new Scene(view.getLayout());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main (String[] args) {
        launch(args);
    }
}
