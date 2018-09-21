package com.alexa4.linguistic_project.view;

import com.alexa4.linguistic_project.presenter.Presenter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class LogInView implements ViewInterface{
    private Presenter presenter;
    private TextArea mTaUserSecondName;
    private PasswordField mPfPassword;

    public LogInView(Presenter presenter){
        this.presenter = presenter;
    }

    public VBox getLogInView(){
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20, 20, 20,20));
        layout.setAlignment(Pos.CENTER);

        HBox labelBox = initTopLabel();

        GridPane pane = initGridPane();

        HBox mButtonsBox = initButtonBox();

        layout.getChildren().addAll(labelBox, pane, mButtonsBox);
        return layout;
    }

    private HBox initButtonBox() {
        HBox mButtonBox = new HBox(20);
        mButtonBox.setAlignment(Pos.CENTER);

        Label mHaveAccount = new Label("Have no account?");
        mHaveAccount.setTextFill(Paint.valueOf("#0000AF"));
        mHaveAccount.setCursor(Cursor.CLOSED_HAND);
        mHaveAccount.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("YOU CLICKED!!!");
            }
        });


        Button mSignInButton = new Button("Sign in");
        mSignInButton.setTextFill(Paint.valueOf("#ffffff"));
        mSignInButton.setStyle("-fx-background-color: #28A745;");
        mSignInButton.setFont(new Font(14));
        mSignInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String userName = mTaUserSecondName.getText();
                String userPassword = mPfPassword.getText();

                if (userName.equals("") || userPassword.equals("")){
                    callAlert("Incorrect data", null, "Login or password is empty");
                    return;
                }

                if (!userName.replaceAll("[\\W|\\s]+", "").equals(userName) ||
                        !userPassword.replaceAll("[\\W|\\s]+", "").equals(userPassword)){

                    callAlert("Wrong symbol", null, "Login must not have wrong symbols!");
                    return;
                }

                presenter.tryToLogIn(userName, userPassword);
            }
        });


        mButtonBox.getChildren().addAll(mHaveAccount, mSignInButton);

        return mButtonBox;
    }

    private void callAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        if (header != null)
            alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private HBox initTopLabel() {
        HBox labelBox = new HBox(15);
        labelBox.setAlignment(Pos.CENTER);

        Label mLWelcome = new Label("Log in your account");
        mLWelcome.setFont(new Font(18));
        labelBox.getChildren().add(mLWelcome);

        return labelBox;
    }


    private GridPane initGridPane(){
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(15);
        pane.setAlignment(Pos.CENTER);

        Label mLUserName = new Label("User login:");
        mLUserName.setFont(new Font(13));

        mTaUserSecondName = new TextArea("");
        mTaUserSecondName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB){
                    mPfPassword.requestFocus();
                    event.consume();
                }
            }
        });
        mTaUserSecondName.setMaxWidth(200);
        mTaUserSecondName.setFont(new Font(13));
        mTaUserSecondName.setPrefHeight(10);
        mTaUserSecondName.setWrapText(true);
        mTaUserSecondName.setTooltip(new Tooltip("Enter your login"));

        Label mLUserPassword = new Label("Password:");
        mLUserPassword.setFont(new Font(13));

        mPfPassword = new PasswordField();
        mPfPassword.setMaxWidth(200);
        mPfPassword.setTooltip(new Tooltip("Enter your password"));

        pane.add(mLUserName, 0, 0);
        pane.add(mTaUserSecondName, 1, 0);
        pane.add(mLUserPassword, 0, 1);
        pane.add(mPfPassword, 1, 1);

        return pane;
    }


    @Override
    public void logIn() {
        presenter.startLessonsView();
    }

    @Override
    public void logInError() {
        callAlert("Logging error", "This account does'nt exist", "Check your data and try again");
    }

    @Override
    public void detachPresenter() {
        presenter = null;
    }
}
