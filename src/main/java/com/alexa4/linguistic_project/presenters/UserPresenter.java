package com.alexa4.linguistic_project.presenters;

import com.alexa4.linguistic_project.models.UserModel;
import javafx.stage.Stage;

public abstract class UserPresenter {
    private UserModel mUserModel;
    public Stage mStage;
    private static Stage sStage;

    public UserPresenter() {
        this.mUserModel = UserModel.getUserModel();
        mStage = UserPresenter.getStage();
    }

    public final String getUserName() {
        return mUserModel.getCurrentUserName();
    }

    public final int getUserMode() {
        return mUserModel.getCurrentUserMode();
    }

    /**
     * Starting the view which should be open first
     */
    public abstract void start();

    /**
     * Setting singleton stage
     * @param stage the stage of app
     */
    public static void setStage(Stage stage) {
        sStage = stage;
    }

    /**
     * Getting singleton stage
     * @return the stage of app
     */
    public static Stage getStage() {
        return sStage;
    }
}
