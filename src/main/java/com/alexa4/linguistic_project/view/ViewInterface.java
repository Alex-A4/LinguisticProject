package com.alexa4.linguistic_project.view;

import javafx.scene.Parent;

/**
 * Interface needs to flexible connecting between presenters and view
 */
public interface ViewInterface {
    /**
     * Default method to detach presenters from view to lost link
     */
    void detachPresenter();


    /**
     * Default method to get basic layout
     * @return
     */
    Parent getLayout();
}
