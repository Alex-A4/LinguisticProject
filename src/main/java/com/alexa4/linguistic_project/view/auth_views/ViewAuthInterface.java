package com.alexa4.linguistic_project.view.auth_views;

import com.alexa4.linguistic_project.view.ViewInterface;

public interface ViewAuthInterface extends ViewInterface {
    /**
     * Call view method if logging response is true
     */
    default void logIn(){}

    /**
     * Call view method if logging response is false
     */
    default void logInError(){}

    /**
     * Call view method if sign in response is true
     */
    default void signIn(){}

    /**
     * Call view method if sign in response is false
     */
    default void signInError(){}
}
