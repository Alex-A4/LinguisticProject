package com.alexa4.linguistic_project.view;

import javafx.scene.Parent;

/**
 * Interface needs to flexible connecting between presenter and view
 */
public interface ViewInterface {
    /**
     * Setting the text to specified text area
     * @param text which need to set somewhere
     */
    default void setText(String text){}

    /**
     * Call view method if logging response is true
     */
    default void logIn(){}

    /**
     * Call view method if logging response is false
     */
    default void logInError(){}

    /**
     * Detach presenter from view to lost link
     */
    default void detachPresenter(){}


    default Parent getLayout(){return new Parent() {
    };}
}
