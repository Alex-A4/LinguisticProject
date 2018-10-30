package com.alexa4.linguistic_project.data_stores;

public class User {
    //Variable of user login
    private final String mUserLogin;
    //Variable which contains mode of user
    private final int userMode;
    //Variable which contains LastName + FirstName
    private final String mLastFirstNames;

    //The teacher can check the student work and evaluate it
    public static final int TEACHER_MODE = 1;

    //The student can solve task and send the result
    public static final int STUDENT_MODE = 2;

    /**
     * Init user with name and mode
     * @param userLogin the login name of user
     * @param userMode the mode of user
     */
    public User (String userLogin, int userMode, String lastFirstNames) {
        this.mUserLogin = userLogin;
        this.mLastFirstNames = lastFirstNames;

        if (userMode == TEACHER_MODE || userMode == STUDENT_MODE)
            this.userMode = userMode;
        else this.userMode = STUDENT_MODE;
    }

    public String getLastFirstNames() {
        return mLastFirstNames;
    }

    public String getUserName() {
        return mUserLogin;
    }

    public int getUserMode() {
        return userMode;
    }
}
