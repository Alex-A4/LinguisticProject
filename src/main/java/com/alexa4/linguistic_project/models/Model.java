package com.alexa4.linguistic_project.models;

import com.alexa4.linguistic_project.adapters.TextAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Model {

    private String currentUserName;

    public String getCurrentUserName() {
        return currentUserName;
    }

    /**
     * LIst of means of expressiveness
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


    //Text with the markers of means
    private String markedText = null;

    //Text without markers, needs to be shown to user
    private String nonMarkedText = null;

    //Collection with the means found in the current text:
    //Key - is a means name, value - list of sentences with this means
    private HashMap<String, ArrayList<String>> foundedMeans;

    /**
     * Reading text from the file and initializing foundedMeans by the means which contains into
     * the current text
     * @return non marked text, which will be shown to user
     */
    public String getText(){
        foundedMeans = new HashMap<String, ArrayList<String>>();

        String text = null;

        try {
            Scanner scanner = new Scanner(new File("data/text.txt"));
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
        if (userName.length() % 2 == 0) {
            currentUserName = userName;
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
            currentUserName = userName;
            callback.sendSignInResponse(true);
        }
        else callback.sendSignInResponse(false);
    }


}
