package com.alexa4.linguistic_project.models;

import com.alexa4.linguistic_project.adapters.TextAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Model {

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



}
