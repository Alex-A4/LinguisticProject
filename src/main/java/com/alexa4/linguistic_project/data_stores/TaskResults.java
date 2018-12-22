package com.alexa4.linguistic_project.data_stores;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Class contains information about user's result of task
 * @author alexa4
 */
public class TaskResults {
    //Collections with the answers of user
    //Key - text, Value - isCorrect
    private HashMap<String, Boolean> mUserAnswers;

    //Collections which contains not correct answers
    //Key - means name, Value - list of sentences
    private HashMap<String, ArrayList<String>> mNotFoundedMeans;

    //Count of right answers which user select
    private int mCountOfCorrectAnswers;
    //Count of not founded answers
    private int mCountOfNotFounded;
    //Count of extra answers which user select
    private int mCountOfExtraAnswers;


    /**
     * Initializing of results
     * @param correctAnswers
     * @param notCorrectAnswers
     */
    public TaskResults(HashMap<String, Boolean> correctAnswers, HashMap<String,
            ArrayList<String>> notCorrectAnswers) {
        mCountOfNotFounded = 0;
        mCountOfCorrectAnswers = 0;
        mCountOfExtraAnswers = 0;

        mUserAnswers = correctAnswers;
        mNotFoundedMeans = notCorrectAnswers;

        //Check user answers
        mUserAnswers.forEach((text, isCorrect) -> {
            if (isCorrect) {
                mCountOfCorrectAnswers++;
            }
            else mCountOfExtraAnswers++;
        });

        //Check means which user do not found
        mNotFoundedMeans.forEach((means, list) -> {
            mCountOfNotFounded += list.size();
        });
    }

    public HashMap<String, Boolean> getCorrectAnswers() {
        return mUserAnswers;
    }

    public HashMap<String, ArrayList<String>> getNotFoundedMeans() {
        return mNotFoundedMeans;
    }

    public int getCountOfCorrectAnswers() {
        return mCountOfCorrectAnswers;
    }

    public int getCountOfNotFounded() {
        return mCountOfNotFounded;
    }

    public int getCountOfExtraAnswers() {
        return mCountOfExtraAnswers;
    }
}
