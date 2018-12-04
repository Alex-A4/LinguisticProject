package com.alexa4.linguistic_project.data_stores;

import java.util.ArrayList;


/**
 * Configuration class which contains information about flags
 * which uses in the task
 */
public class TaskConfig {
    public static class Flag {

        public static final String SELF_TESTING = "SELF-TESTING";
    }
    private ArrayList<String> mFlags;
    public ArrayList<String> getFlags() {
        return mFlags;
    }


    public TaskConfig() {
        mFlags = new ArrayList<>();
    }

    /**
     * Adding flag to configuration
     * @param flag
     */
    public void addFlagToConfig(String flag) {
        if (!mFlags.contains(flag))
            mFlags.add(flag);
    }


}
