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
    //Count of wrong answers which user select
    private int mCountOfWrongAnswers;


    /**
     * Initializing of results
     * @param correctAnswers
     * @param notCorrectAnswers
     */
    public TaskResults(HashMap<String, Boolean> correctAnswers, HashMap<String,
            ArrayList<String>> notCorrectAnswers) {
        mCountOfWrongAnswers = 0;
        mCountOfCorrectAnswers = 0;

        mUserAnswers = correctAnswers;
        mNotFoundedMeans = notCorrectAnswers;

        //Check user answers
        mUserAnswers.forEach((text, isCorrect) -> {
            if (isCorrect) {
                mCountOfCorrectAnswers++;
            }
            else mCountOfWrongAnswers++;
        });

        //Check means which user do not found
        mNotFoundedMeans.forEach((means, list) -> {
            mCountOfWrongAnswers += list.size();
        });
        
        System.out.println("mNotFoundedMeans = " + mNotFoundedMeans);
        System.out.println("mUserAnswers = " + mUserAnswers);
        System.out.println("mCountOfCorrectAnswers = " + mCountOfCorrectAnswers);
        System.out.println("mCountOfWrongAnswers = " + mCountOfWrongAnswers);
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

    public int getCountOfWrongAnswers() {
        return mCountOfWrongAnswers;
    }
}
