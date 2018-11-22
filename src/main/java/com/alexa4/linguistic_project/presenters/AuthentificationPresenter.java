package com.alexa4.linguistic_project.presenters;

import com.alexa4.linguistic_project.models.AuthentificationModel;
import com.alexa4.linguistic_project.view.auth_views.ViewAuthInterface;
import com.alexa4.linguistic_project.view.auth_views.LogInView;
import com.alexa4.linguistic_project.view.auth_views.SignInView;
import javafx.scene.Scene;

public class AuthentificationPresenter extends UserPresenter {
    private ViewAuthInterface mView;
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
        mStage.setTitle("Авторизация");
        mStage.setScene(new Scene(mView.getLayout()));
    }

    /**
     * Starting signInmView which needs to sign up new user
     */
    public void startSignInView() {
        mView.detachPresenter();
        mView = new SignInView(this);
        mStage.setTitle("Регистрация");
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
     * @param login the user's login
     * @param userPassword the user's password
     * @param initials the user's initials
     */
    public void tryToSignIn(String login, String userPassword, String initials){
        mAuthModel.tryToSignIn(login, userPassword, initials, new AuthentificationModel.SignInCallback() {
            @Override
            public void sendSignInResponse(boolean response) {
                if (response)
                    mView.signIn();
                else mView.signInError();
            }
        });
    }
}
