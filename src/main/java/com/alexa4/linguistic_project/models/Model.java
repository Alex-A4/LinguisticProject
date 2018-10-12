package com.alexa4.linguistic_project.models;

import com.alexa4.linguistic_project.adapters.TextAdapter;
import com.alexa4.linguistic_project.data_stores.User;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class Model {
    private static final String TASKS_FOLDER = "data/tasks/";
    private static final String ANSWERS_FOLDER = "data/answers";

    //Current user name
    private User currentUser;


    //Map contains text, which user select
    //Key is a means of choice, value is a list with texts which user select
    private HashMap<String, ArrayList<String>> userChoiceList;

    private ArrayList<String> filesNameList = null;

    //Text with the markers of means
    private String markedText = null;

    //Text without markers, needs to be shown to user
    private String nonMarkedText = null;

    //Collection with the means found in the current text:
    //Key - is a means name, value - list of sentences with this means
    private HashMap<String, ArrayList<String>> foundedMeans;

    /**
     * Initializing model by reading all files names
     */
    public Model() {
        userChoiceList = new HashMap<>();
        readAllFiles();
    }

    /**
     * Getter of current user name
     * @return the current user name
     */
    public String getCurrentUserName() {
        return currentUser.getUserName();
    }

    public int getCurrentUserMode() {return currentUser.getUserMode(); }

    public HashMap<String, ArrayList<String>> getFoundMeans() {
        return this.foundedMeans;
    }


    /**
     * Saving the file changes
     * @param text the text which need to save
     * @param fileName the name of file
     * @return writing status
     */
    public boolean saveFileChanges(String text, String fileName) {
        nonMarkedText = text;
        markedText = text;

        userChoiceList.forEach((means, list) -> list
                .forEach(choice -> tryToAddMeansToText(means, choice)));

        try {
            writeTextToFile(fileName, markedText);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Writing text to file
     * @param fileName the name of file which need save
     * @param text the text which need to write into file
     */
    private void writeTextToFile(String fileName, String text) throws Exception {
        FileOutputStream stream = new FileOutputStream(new File(TASKS_FOLDER
                + fileName + ".txt"));

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
        if (markedText.contains("<" + means + ">" + choice + "</" + means + ">"))
            return;

        //If markedText have no mark on the choice, then add it
        markedText = markedText.replace(choice,
                "<" + means + ">" + choice + "</" + means + ">");
    }


    /**
     * Saving user's answers
     * TODO add logic
     * @return the result of saving
     */
    public boolean saveUserAnswer() {

        return true;
    }


    /**
     * LIst of means of expressiveness
     * text field is a parameter which need to display means in text form
     */
    public enum MeansOfExpressiveness{
        METAPHOR ("metaphor"),
        SIMILE ("simile"),
        EPITHET ("epithet"),
        EUPHEMISM ("euphemism"),
        PARADOX ("paradox"),
        PUN ("pun"),
        ANTITHESIS ("antithesis"),
        ALLUSION ("allusion"),
        METONYMY ("metonymy"),
        REPETITION ("repetition"),
        PARALLELISM ("parallelism"),
        DETACHMENT ("detachment"),
        ONOMATOPOEIA ("onomatopoeia"),
        ELLIPSIS ("ellipsis"),
        POLYSYNDETON ("polysyndeton"),
        PERSONIFICATION ("personification"),
        GRAPHON ("graphon"),
        HYPERBOLE ("hyperbole"),
        RHETORICAL_QUESTION ("rhetorical_question"),
        INVERSION ("inversion");

        private final String text;
        MeansOfExpressiveness(String text){
            this.text = text;
        }
        public String getText() {
            return text;
        }
    }



    /**
     * Reading text from the file and initializing foundedMeans by the means which contains into
     * the current text
     * @return non marked text, which will be shown to user
     */
    public String getText(String taskName){
        foundedMeans = new HashMap<String, ArrayList<String>>();

        String text = null;

        try {
            Scanner scanner = new Scanner(new File("data/tasks/" + taskName + ".txt"));
            StringBuilder builder = new StringBuilder();

            scanner.useDelimiter("\n");

            while(scanner.hasNext())
                builder.append(scanner.next() + "\n");

            text = builder.toString();

            markedText = text;
            TextAdapter.buildMeansMap(markedText, foundedMeans);
            nonMarkedText = TextAdapter.convertMarkedText(text);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return nonMarkedText;
    }


    /**
     * Reading all files name from data/tasks folder and add it to filesNameList
     * This list contains all names of tasks
     */
    public void readAllFiles() {
        File folder = new File(TASKS_FOLDER);
        File[] files = folder.listFiles();
        filesNameList = new ArrayList<>();
        for (File file: files)
            if (file.isFile())
                filesNameList.add(file.getName().replace(".txt", ""));
    }

    /**
     * Getter list of files names
     * @return the list of files names
     */
    public List<String> getListOfFiles() {
        return filesNameList;
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
     * Send response to presenter was the log in successful or not
     */
    public interface LogInCallback{
        void sendLogInResponse(boolean response);
    }

    /**
     * Trying to log in user if account contains in DB.
     * If success then set currentUserName and send response
     * @param userName the user name
     * @param userPassword the user password
     * @param callback the callback to send response to presenter
     */
    public void tryToLogIn(String userName, String userPassword, LogInCallback callback) {
        if (userName.equals("admin") && userPassword.equals("admin")) {
            currentUser = new User(userName, User.TEACHER_MODE);
            callback.sendLogInResponse(true);
        }

        if (userName.length() % 2 == 0) {
            currentUser = new User(userName, User.STUDENT_MODE);
            callback.sendLogInResponse(true);
        }
        else callback.sendLogInResponse(false);
    }




    /**
     * Send response to presenter was the sign in successful or not
     */
    public interface SignInCallback {
        void sendSignInResponse(boolean response);
    }

    /**
     * Trying to sign up new account.
     * If success then set currentUserName and send response
     * @param userName the user name
     * @param userPassword the user password
     * @param callback the callback to send response to presenter
     */
    public void tryToSignIn(String userName, String userPassword, SignInCallback callback) {
        if (userName.length() % 2 == 0) {
            currentUser = new User(userName, User.STUDENT_MODE);
            callback.sendSignInResponse(true);
        }
        else callback.sendSignInResponse(false);
    }




    /**
     * Add user's choice to collection
     * @param means the means which user choose
     * @param text the text which user select
     */
    public void setUserChoice(String means, String text) {
        if (userChoiceList.containsKey(means))
            userChoiceList.get(means).add(text);
        else {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(text);
            userChoiceList.put(means, temp);
        }
    }

    /**
     * Delete user choice from collection
     * @param means the removable means
     * @param text the removable text
     */
    public void deleteUserChoice(String means, String text) {
        ArrayList<String> temp = userChoiceList.get(means);
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).equals(text))
                temp.remove(i);
        }
    }

}
