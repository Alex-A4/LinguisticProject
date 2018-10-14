package com.alexa4.linguistic_project.models;


import com.alexa4.linguistic_project.data_stores.User;

/**
 * Authentification logic which helps to identity man's
 * @author alexa4
 */
public class AuthentificationModel {
    private UserModel mUser;


    public AuthentificationModel() {
        mUser = UserModel.getUserModel();
    }

    /**
     * Send response to presenters was the log in successful or not
     */
    public interface LogInCallback{
        void sendLogInResponse(boolean response);
    }

    /**
     * Trying to log in user if account contains in DB.
     * If success then set currentUserName and send response
     * @param userName the user name
     * @param userPassword the user password
     * @param callback the callback to send response to presenters
     */
    public void tryToLogIn(String userName, String userPassword, LogInCallback callback) {
        if (userName.equals("admin") && userPassword.equals("admin")) {
            mUser.initUser(userName, User.TEACHER_MODE);
            callback.sendLogInResponse(true);
            return;
        }

        if (userName.length() % 2 == 0) {
            mUser.initUser(userName, User.STUDENT_MODE);
            callback.sendLogInResponse(true);
            return;
        }
        else callback.sendLogInResponse(false);
    }




    /**
     * Send response to presenters was the sign in successful or not
     */
    public interface SignInCallback {
        void sendSignInResponse(boolean response);
    }

    /**
     * Trying to sign up new account.
     * If success then set currentUserName and send response
     * @param userName the user name
     * @param userPassword the user password
     * @param callback the callback to send response to presenters
     */
    public void tryToSignIn(String userName, String userPassword, SignInCallback callback) {
        if (userName.length() % 2 == 0) {
            mUser.initUser(userName, User.STUDENT_MODE);
            callback.sendSignInResponse(true);
        }
        else callback.sendSignInResponse(false);
    }
}
