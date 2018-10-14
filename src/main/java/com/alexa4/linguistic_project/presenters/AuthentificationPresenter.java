package com.alexa4.linguistic_project.presenters;

import com.alexa4.linguistic_project.models.AuthentificationModel;
import com.alexa4.linguistic_project.view.auth_views.LogInView;
import com.alexa4.linguistic_project.view.auth_views.SignInView;
import com.alexa4.linguistic_project.view.ViewInterface;
import javafx.scene.Scene;

public class AuthentificationPresenter extends UserPresenter {
    private ViewInterface mView;
    private AuthentificationModel mAuthModel;

    public AuthentificationPresenter() {
        mAuthModel = new AuthentificationModel();
    }

    @Override
    public void start() {
        startLogInView();
    }

    public void startLogInView() {
        mView = new LogInView(this);
        mStage.setTitle("Log in");
        mStage.setScene(new Scene(mView.getLayout()));
    }

    /**
     * Starting signInmView which needs to sign up new user
     */
    public void startSignInView() {
        mView.detachPresenter();
        mView = new SignInView(this);
        mStage.setTitle("Sign in");
        mStage.setScene(new Scene(mView.getLayout()));
    }


    /**
     * Trying to login by check does this account contains in DataBase
     * If success then notify mView about it
     * @param userName the name of user account
     * @param userPassword the password of user account
     * @return is this account contains in DB
     */
    public void tryToLogIn(String userName, String userPassword){
        mAuthModel.tryToLogIn(userName, userPassword, new AuthentificationModel.LogInCallback() {
            @Override
            public void sendLogInResponse(boolean response) {
                if (response)
                    mView.logIn();
                else mView.logInError();
            }
        });
    }


    /**
     * Trying to sign in new account in DB
     * If success then notify mView about it
     */
    public void tryToSignIn(String userName, String userPassword){
        mAuthModel.tryToSignIn(userName, userPassword, new AuthentificationModel.SignInCallback() {
            @Override
            public void sendSignInResponse(boolean response) {
                if (response)
                    mView.signIn();
                else mView.signInError();
            }
        });
    }
}
