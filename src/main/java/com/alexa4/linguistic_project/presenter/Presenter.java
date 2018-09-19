package com.alexa4.linguistic_project.presenter;

import com.alexa4.linguistic_project.models.Model;
import com.alexa4.linguistic_project.view.MainView;
import com.alexa4.linguistic_project.view.ViewInterface;

/**
 * This class needs to link model and view
 */
public class Presenter {

    private final Model model;
    private ViewInterface view = null;

    public Presenter(Model model) {
        this.model = model;
    }

    /**
     * Set view to presenter
     * @param view
     */
    public void setView(ViewInterface view){
        this.view = view;
    }

    /**
     * Detach view from presenter
     */
    public void detachView(){
        this.view = null;
    }

    /**
     * Get text and call view's method to set text to specified text area
     */
    public void getText(){
        view.setText(model.getText());
    }
}
