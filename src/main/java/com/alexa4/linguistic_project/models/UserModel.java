package com.alexa4.linguistic_project.models;

import com.alexa4.linguistic_project.data_stores.User;


/**
 * Singleton model which contains information about current User
 * @author alexa4
 */
public class UserModel {
    //Singleton object
    private static UserModel sModel = null;

    //Current user name
    private User currentUser;

    /**
     * Getting user name
     * @return the current user name
     */
    public String getUserLogin() {
        return currentUser.getUserLogin();
    }

    /**
     * Getting user mode
     * @return
     */
    public int getUserMode() {return currentUser.getUserMode(); }


    /**
     * Getting the instance of model
     * @return
     */
    public static UserModel getUserModel() {
        if (sModel == null)
            sModel = new UserModel();

        return sModel;
    }

    /**
     * Initializing current user
     * TODO: Add logic to get user's first and last names from dataBase
     * @param userName the login of user
     * @param mode the mode of user
     */
    public void initUser(String userName, int mode) {
        currentUser = new User(userName, mode, "Persons person");
    }
}
