package com.alexa4.linguistic_project.view;

import com.alexa4.linguistic_project.presenter.Presenter;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class SignInView implements ViewInterface {
    private Presenter presenter;

    public SignInView(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void detachPresenter() {
        presenter = null;
    }

    @Override
    public VBox getLayout() {
        VBox layout = new VBox(20);

        return layout;
    }
}
