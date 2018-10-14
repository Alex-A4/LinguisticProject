package com.alexa4.linguistic_project.data_stores;

public class User {
    private final String userName;
    private final int userMode;

    //The teacher can check the student work and evaluate it
    public static final int TEACHER_MODE = 1;

    //The student can solve task and send the result
    public static final int STUDENT_MODE = 2;

    /**
     * Init user with name and mode
     * @param userName
     * @param userMode
     */
    public User (String userName, int userMode) {
        this.userName = userName;

        if (userMode == TEACHER_MODE || userMode == STUDENT_MODE)
            this.userMode = userMode;
        else this.userMode = STUDENT_MODE;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserMode() {
        return userMode;
    }
}
