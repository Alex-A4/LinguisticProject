package com.alexa4.linguistic_project.view.auth_views;

import com.alexa4.linguistic_project.data_stores.User;
import com.alexa4.linguistic_project.models.UserModel;
import com.alexa4.linguistic_project.presenters.AuthentificationPresenter;
import com.alexa4.linguistic_project.presenters.student.StudentPresenter;
import com.alexa4.linguistic_project.presenters.teacher.TeacherPresenter;
import com.alexa4.linguistic_project.view.dialogs.AlertDialog;
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

public class LogInView implements ViewAuthInterface {
    private AuthentificationPresenter mPresenter;
    private TextField mTaUserSecondName;
    private PasswordField mPfPassword;

    public LogInView(AuthentificationPresenter presenter){
        this.mPresenter = presenter;
    }

    /**
     * Return the layout which will set to stage
     * @return the layout of this window
     */
    @Override
    public VBox getLayout(){
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20, 20, 20,20));
        layout.setAlignment(Pos.CENTER);

        HBox labelBox = initTopLabel();

        GridPane pane = initGridPane();

        HBox mButtonsBox = initButtonBox();

        layout.getChildren().addAll(labelBox, pane, mButtonsBox);
        return layout;
    }

    /**
     * Initializing box with buttons
     * @return the box with buttons
     */
    private HBox initButtonBox() {
        HBox mButtonBox = new HBox(20);
        mButtonBox.setAlignment(Pos.CENTER);

        //Button sign in if user was not registered, will start new View to sign up new user
        Label mHaveAccount = new Label("Нет аккаунта?");
        mHaveAccount.setTextFill(Paint.valueOf("#0000AF"));
        mHaveAccount.setCursor(Cursor.CLOSED_HAND);
        mHaveAccount.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mPresenter.startSignInView();
            }
        });


        //Button to log in account
        Button mSignInButton = new Button("Войти");
        mSignInButton.setTextFill(Paint.valueOf("#ffffff"));
        mSignInButton.setStyle("-fx-background-color: #28A745;");
        mSignInButton.setFont(new Font(14));
        mSignInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String userName = mTaUserSecondName.getText();
                String userPassword = mPfPassword.getText();

                if (userName.equals("") || userPassword.equals("")){
                    callAlert("Неправильные данные", null, "Логин или пароль пустые");
                    return;
                }

                if (!userName.replaceAll("[\\W|\\s]+", "").equals(userName) ||
                        !userPassword.replaceAll("[\\W|\\s]+", "").equals(userPassword)){

                    callAlert("Неправильный символ", null, "Логин должен содержать только цифры и буквы!");
                    return;
                }

                mPresenter.tryToLogIn(userName, userPassword);
            }
        });


        mButtonBox.getChildren().addAll(mHaveAccount, mSignInButton);

        return mButtonBox;
    }


    /**
     * Call Dialog Alert if something happens
     * @param title the title of Alert
     * @param header the header of Alert
     * @param content the content text of Alert
     */
    private void callAlert(String title, String header, String content){
        AlertDialog.callErrorAlert(title, header, content);
    }


    /**
     * Initializing box with welcome label
     * @return
     */
    private HBox initTopLabel() {
        HBox labelBox = new HBox(15);
        labelBox.setAlignment(Pos.CENTER);

        Label mLWelcome = new Label("Добро пожаловать");
        mLWelcome.setFont(new Font(18));
        labelBox.getChildren().add(mLWelcome);

        return labelBox;
    }



    /**
     * Initializing grid pane with fields to write login and password
     * @return
     */
    private GridPane initGridPane(){
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(15);
        pane.setAlignment(Pos.CENTER);

        Label mLUserName = new Label("Логин:");
        mLUserName.setFont(new Font(15));

        //Text field to enter login
        mTaUserSecondName = new TextField("");
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
        mTaUserSecondName.setFont(new Font(15));
        mTaUserSecondName.setPrefHeight(10);
        mTaUserSecondName.setTooltip(new Tooltip("Введите логин"));


        Label mLUserPassword = new Label("Пароль:");
        mLUserPassword.setFont(new Font(15));

        //Text field to enter password
        mPfPassword = new PasswordField();
        mPfPassword.setText("");
        mPfPassword.setMaxWidth(200);
        mPfPassword.setTooltip(new Tooltip("Введите пароль"));

        pane.add(mLUserName, 0, 0);
        pane.add(mTaUserSecondName, 1, 0);
        pane.add(mLUserPassword, 0, 1);
        pane.add(mPfPassword, 1, 1);

        return pane;
    }


    /**
     * Reaction on positive logging
     */
    @Override
    public void logIn() {
        if (UserModel.getUserModel().getUserMode() == User.STUDENT_MODE) {
            StudentPresenter studPres = new StudentPresenter();
            studPres.start();
        } else {
            TeacherPresenter teachPres = new TeacherPresenter();
            teachPres.start();
        }
    }

    /**
     * Reaction on negative logging
     */
    @Override
    public void logInError() {
        callAlert("Ошибка авторизации", "Такой аккаунт не существует",
                "Проверьте данные и попробуйте еще");
    }


    /**
     * Detach presenters from the view
     */
    @Override
    public void detachPresenter() {
        mPresenter = null;
    }
}
