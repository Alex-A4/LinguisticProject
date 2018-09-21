package com.alexa4.linguistic_project;

import com.alexa4.linguistic_project.models.Model;
import com.alexa4.linguistic_project.presenter.Presenter;
import com.alexa4.linguistic_project.view.LogInView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launching the app
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Presenter presenter = new Presenter(new Model(), primaryStage);
        LogInView view = new LogInView(presenter);
        presenter.setView(view);

        primaryStage.setTitle("Log in");

        Scene scene = new Scene(view.getLogInView());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main (String[] args) {
        launch(args);
    }
}
