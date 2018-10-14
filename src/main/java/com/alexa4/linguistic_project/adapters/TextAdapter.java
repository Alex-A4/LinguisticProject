package com.alexa4.linguistic_project.adapters;

import com.alexa4.linguistic_project.data_stores.MeansOfExpressiveness;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TextAdapter needs to convert marked text to common text without
 * marks to show it to user
 */
public class TextAdapter {

    /**
     * Converting market text to non-marked by deleting means markers from text
     * @param markedText input text with markers
     * @return non-marked text
     */
    public static String convertMarkedText(String markedText) {
        String commonText = new String(markedText);

        for (MeansOfExpressiveness mean: MeansOfExpressiveness.values()){
            commonText = commonText.replaceAll("<" + mean.getText() + ">", "");
            commonText = commonText.replaceAll("</"+ mean.getText() + ">", "");
        }

        return commonText;
    }


    /**
     * Build a map of all means of expressiveness in current text.
     * On each founded means creating a map's field which filling by all sentences
     * with that means
     * @param text
     * @param map
     */
    public static void buildMeansMap(String text, HashMap<String, ArrayList<String>> map) {
        String markedText = new String(text);

        for (MeansOfExpressiveness means: MeansOfExpressiveness.values()) {

            //If text contains some means of expressiveness, then build a collection which contains
            // all sentences
            while (true) {

                if (markedText.contains("<"+means.getText()+">")) {
                    //If current means exists into collection, then add sentence to list
                    if (map.containsKey(means.getText()))
                        map.get(means.getText()).add(
                                markedText.substring(markedText.indexOf("<" + means.getText() + ">") +
                                        means.getText().length() + 2,
                                        markedText.indexOf("</" + means.getText() + ">"))
                        );
                    //If current means was not exist into collection, then create new KEY and add
                    //sentence to the list
                    else {
                        map.put(means.getText(), new ArrayList<>());
                        map.get(means.getText()).add(
                                markedText.substring(markedText.indexOf("<" + means.getText() + ">")+
                                                means.getText().length() + 2,
                                        markedText.indexOf("</" + means.getText() + ">"))
                        );
                    }

                    //Delete markers of means
                    markedText = markedText.replaceFirst("<" + means.getText() + ">", "");
                    markedText = markedText.replaceFirst("</" + means.getText() + ">", "");

                } else break;  //If there were not current means, then go to next
            }
        }
    }
}
