package com.alexa4.linguistic_project.view;

import javafx.scene.Parent;

/**
 * Interface needs to flexible connecting between presenters and view
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
     * Default method to detach presenters from view to lost link
     */
    void detachPresenter();

    /**
     * Call view method if sign in response is true
     */
    default void signIn(){}

    /**
     * Call view method if sign in response is false
     */
    default void signInError(){}

    /**
     * Default method to get basic layout
     * @return
     */
    Parent getLayout();
}
