package com.alexa4.linguistic_project.models;

import com.alexa4.linguistic_project.adapters.TextAdapter;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class TextModel {
    private static final String TASKS_FOLDER = "data/tasks/";
    private static final String ANSWERS_FOLDER = "data/answers/";

    private String mCurrentTaskName = null;

    //Map contains text, which user select
    //Key is a means of choice, value is a list with texts which user select
    private HashMap<String, ArrayList<String>> mUserChoiceList;


    //List of files with tasks
    private ArrayList<String> mTasksFilesNameList = null;
    public List<String> getTasksListOfFiles() {
        return mTasksFilesNameList;
    }


    //Text with the markers of means
    private String mMarkedText = null;

    //Text without markers, needs to be shown to user
    private String mNonMarkedText = null;

    //Collection with the means found in the current text:
    //Key - is a means name, value - list of sentences with this means
    private HashMap<String, ArrayList<String>> mFoundedMeans;
    public HashMap<String, ArrayList<String>> getFoundedMeans() {
        return this.mFoundedMeans;
    }


    public TextModel() {
        clearUserChoice();
        readAllTasksFiles();
    }

    /**
     * Reading all files name from data/tasks folder and add it to filesNameList
     * This list contains all names of tasks
     */
    private void readAllTasksFiles() {
        File folder = new File(TASKS_FOLDER);
        File[] files = folder.listFiles();
        mTasksFilesNameList = new ArrayList<>();
        for (File file: files)
            if (file.isFile())
                mTasksFilesNameList.add(file.getName().replace(".txt", ""));
    }


    /**
     * Reading files with students answer
     * @return the list with all answers
     */
    private ArrayList<String> readAnswerFiles() {
        ArrayList<String> list = new ArrayList<>();

        File folder = new File(ANSWERS_FOLDER);
        File[] files = folder.listFiles();

        for (File file: files)
            if (file.isFile())
                list.add(file.getName().replace(".txt", ""));

        return list;
    }




    /**
     * Reading text from the file and initializing foundedMeans by the means which contains into
     * the current text
     * @return non marked text, which will be shown to user
     */
    public String getText(String taskName){
        mFoundedMeans = new HashMap<String, ArrayList<String>>();
        mCurrentTaskName = taskName;

        String text = null;

        try {
            Scanner scanner = new Scanner(new File(TASKS_FOLDER + taskName + ".txt"));
            StringBuilder builder = new StringBuilder();

            scanner.useDelimiter("\n");

            while(scanner.hasNext()) {
                builder.append(scanner.next());
                builder.append("\n");
            }

            text = builder.toString();

            mMarkedText = text;
            TextAdapter.buildMeansMap(mMarkedText, mFoundedMeans);
            mNonMarkedText = TextAdapter.convertMarkedText(text);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return mNonMarkedText;
    }






    /**
     * Saving the file changes
     * @param text the text which need to save
     * @param fileName the name of file
     * @return writing status
     */
    public boolean saveFileChanges(String text, String fileName) {
        mNonMarkedText = text;
        mMarkedText = text;

        mUserChoiceList.forEach((means, list) -> list
                .forEach(choice -> tryToAddMeansToText(means, choice)));

        try {
            writeTextToFile(TASKS_FOLDER, fileName, mMarkedText);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Writing bytes to file
     * @param filePath the full path of file which need to save
     * @param text the text which need to write into file
     */
    private void writeTextToFile(String filePath, String fileName, String text) throws Exception {
        new File(filePath).mkdirs();
        FileOutputStream stream = new FileOutputStream(new File(filePath + fileName + ".txt"));

        stream.write(text.getBytes());
        stream.close();
    }

    /**
     * Converting choice without marks to marked text
     * @param means the means choice
     * @param choice the choice text
     */
    private void tryToAddMeansToText(String means, String choice) {
        //If markedText already contains means with the mark, then skip
        //Text with mark: <means>choice</means>
        if (mMarkedText.contains("<" + means + ">" + choice + "</" + means + ">"))
            return;

        //If markedText have no mark on the choice, then add it
        mMarkedText = mMarkedText.replace(choice,
                "<" + means + ">" + choice + "</" + means + ">");
    }




    /**
     * Saving user's answers to file which looks like taskName_userName
     * mMarkedText is rewriting, so could not be used later with previous value
     * @return the result of saving
     */
    public boolean saveUserAnswer() {
        String fileName = mCurrentTaskName
                + "_"
                + UserModel.getUserModel().getUserLogin();

        mMarkedText = new String(mNonMarkedText);

        mUserChoiceList.forEach((means, list) -> list
                .forEach(choice -> tryToAddMeansToText(means, choice)));

        try {
            writeTextToFile(ANSWERS_FOLDER, fileName, mMarkedText);
        } catch (Exception e) {
            return false;
        }

        return true;
    }


    /**
     * Decoding file name to array of
     * [0] name of task
     * [1] login of student
     * @param fileName the name of decoding file
     * @return the decoded name of file
     */
    public String[] decodeStudentAnswerFile(String fileName) {
        String[] objects = new String[2];
        int underLinePos = fileName.indexOf("_");
        objects[0] = fileName.substring(0, underLinePos);
        objects[1] = fileName.substring(underLinePos+1);

        return objects;
    }


    /**
     * Clearing user choice for new task
     */
    public void clearUserChoice() {
        mUserChoiceList = new HashMap<>();
    }


    /**
     * Add user's choice to collection
     * @param means the means which user choose
     * @param text the text which user select
     */
    public void addUserChoice(String means, String text) {
        if (mUserChoiceList.containsKey(means))
            mUserChoiceList.get(means).add(text);
        else {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(text);
            mUserChoiceList.put(means, temp);
        }
    }

    /**
     * Delete user choice from collection
     * @param means the removable means
     * @param text the removable text
     */
    public void deleteUserChoice(String means, String text) {
        ArrayList<String> temp = mUserChoiceList.get(means);
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).equals(text))
                temp.remove(i);
        }
    }




    /**
     * Get random color which will set somewhere
     * @return new color
     */
    public Paint getRandomColor() {
        String color = "";
        for (int i = 0; i < 6; i++)
            color = color.concat(getNextColorChar());

        return Paint.valueOf("#" + color);
    }

    /**
     * Generate color char from 0 to f in dex
     * @return next color char
     */
    private String getNextColorChar() {
        Random colorRandom = new Random();
        int nextColor = colorRandom.nextInt(256);
        switch (nextColor%16) {
            case 10: return "a";
            case 11: return "b";
            case 12: return "c";
            case 13: return "d";
            case 14: return "e";
            case 15: return "f";
            default: return String.valueOf(nextColor%16);
        }
    }


    /**
     * Creating map based on students answers
     *
     * Map looks like: Student name - List of student answers
     * @return the map of students answers
     */
    public HashMap<String, ArrayList<String>> getAnswersMap() {
        HashMap<String, ArrayList<String>> answersMap = new HashMap<>();
        ArrayList<String> files = readAnswerFiles();

        //For each file get userName and name of task
        for (String file: files) {
            String[] objects = decodeStudentAnswerFile(file);

            //If map contains key (user login) then add task to the list, else create this pair
            if (answersMap.containsKey(objects[1]))
                answersMap.get(objects[1]).add(objects[0]);
            else {
                answersMap.put(objects[1], new ArrayList<>());
                answersMap.get(objects[1]).add(objects[0]);
            }
        }


        return answersMap;
    }

    /**
     * Reading file of student answer
     * @param userName the name of user
     * @param taskName the name of task which text need return
     * @return the text of answer
     */
    public String getAnswerText(String userName, String taskName) {
        String nameOfFile = taskName + "_" + userName;

        mFoundedMeans = new HashMap<String, ArrayList<String>>();
        mCurrentTaskName = taskName;

        String text = null;

        try {
            Scanner scanner = new Scanner(new File(ANSWERS_FOLDER + nameOfFile + ".txt"));
            StringBuilder builder = new StringBuilder();

            scanner.useDelimiter("\n");

            while(scanner.hasNext()) {
                builder.append(scanner.next());
                builder.append("\n");
            }

            text = builder.toString();

            mMarkedText = text;
            TextAdapter.buildMeansMap(mMarkedText, mFoundedMeans);
            mNonMarkedText = TextAdapter.convertMarkedText(text);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return mNonMarkedText;
    }
}
