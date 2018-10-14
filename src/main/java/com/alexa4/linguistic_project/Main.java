package com.alexa4.linguistic_project;

import com.alexa4.linguistic_project.presenters.AuthentificationPresenter;
import com.alexa4.linguistic_project.presenters.UserPresenter;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Launching the app
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        UserPresenter.setStage(primaryStage);

        AuthentificationPresenter presenter = new AuthentificationPresenter();
        presenter.start();

        primaryStage.show();
    }

    public static void main (String[] args) {
        launch(args);
    }
}
