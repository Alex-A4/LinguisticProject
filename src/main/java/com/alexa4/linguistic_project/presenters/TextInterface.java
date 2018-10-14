package com.alexa4.linguistic_project.presenters;

import javafx.scene.paint.Paint;

import java.util.List;


/**
 * Interface which helps to interact with text and choice collections
 * @author alexa4
 */
public interface TextInterface {
    /**
     * Request to get text of selected task
     * @param taskName the name of selected task
     */
    void getText(String taskName);

    /**
     * Request to get list of files names
     * @return the list of files names
     */
    List<String> getFilesNameList();

    /**
     * Request to get random color to text
     * @return the color
     */
    Paint getTextColors();

    /**
     * Delete user's choice of means from model's container
     * @param means the means which need delete
     * @param text the text which need delete
     */
    void deleteUserChoice(String means, String text);

    /**
     * Add user's choice of means to model container
     * @param means the means of expressiveness that user choose
     * @param text the text which user select
     */
    void addUserChoice(String means, String text);
}
