package com.alexa4.linguistic_project.presenter;

import com.alexa4.linguistic_project.models.Model;
import com.alexa4.linguistic_project.view.SignInView;
import com.alexa4.linguistic_project.view.ViewInterface;
import com.alexa4.linguistic_project.view.LessonsView;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class needs to link model and view
 */
public class Presenter {

    private final Stage stage;
    private final Model model;
    private ViewInterface view = null;

    public Presenter(Model model, Stage stage) {
        this.stage = stage;
        this.model = model;
    }

    /**
     * Set view to presenter
     * @param view
     */
    public void setView(ViewInterface view){
        this.view = view;
    }

    /**
     * Detach view from presenter
     */
    public void detachView(){
        this.view = null;
    }

    /**
     * Get text and call view's method to set text to specified text area
     */
    public void getText(){
        view.setText(model.getText());
    }

    /**
     * Trying to login by check does this account contains in DataBase
     * If success then notify view about it
     * @param userName the name of user account
     * @param userPassword the password of user account
     * @return is this account contains in DB
     */
    public void tryToLogIn(String userName, String userPassword){
        model.tryToLogIn(userName, userPassword, new Model.LogInCallback() {
            @Override
            public void sendLogInResponse(boolean response) {
                if (response)
                    view.logIn();
                else view.logInError();
            }
        });
    }


    /**
     * Trying to sign in new account in DB
     * If success then notify view about it
     */
    public void tryToSignIn(String userName, String userPassword){
        model.tryToSignIn(userName, userPassword, new Model.SignInCallback() {
            @Override
            public void sendSignInResponse(boolean response) {
                if (response)
                    view.signIn();
                else view.signInError();
            }
        });
    }


    /**
     * Starting lessonsView which needs to work
     */
    public void startLessonsView(){
        view.detachPresenter();
        view = new LessonsView(this);
        stage.setScene(new Scene(view.getLayout()));
    }


    /**
     * Starting signInView which needs to sign up new user
     */
    public void startSignInView(){
        view.detachPresenter();
        view = new SignInView(this);
        stage.setScene(new Scene(view.getLayout()));
    }
}
