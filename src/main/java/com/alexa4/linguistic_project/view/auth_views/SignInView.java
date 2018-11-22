package com.alexa4.linguistic_project.view.auth_views;

import com.alexa4.linguistic_project.data_stores.User;
import com.alexa4.linguistic_project.models.UserModel;
import com.alexa4.linguistic_project.presenters.AuthentificationPresenter;
import com.alexa4.linguistic_project.presenters.student.StudentPresenter;
import com.alexa4.linguistic_project.presenters.teacher.TeacherPresenter;
import com.alexa4.linguistic_project.view.dialogs.AlertDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class SignInView implements ViewAuthInterface {
    private AuthentificationPresenter mPresenter;
    private TextField mTaUserSecondName;
    private TextField mInitials;
    private PasswordField mPfPassword;
    private PasswordField mPfConfirmPassword;

    /**
     * Set presenters to view
     * @param presenter
     */
    public SignInView(AuthentificationPresenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * Detach presenters from view
     */
    @Override
    public void detachPresenter() {
        mPresenter = null;
    }


    /**
     * Get basic layout which will connect to stage
     * @return the basic layout
     */
    @Override
    public VBox getLayout() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        HBox labelBox = initTopLabelBox();

        GridPane pane = initGridPane();

        HBox buttonsBox = initButtonsBox();

        layout.getChildren().addAll(labelBox, pane, buttonsBox);

        return layout;
    }

    /**
     * Initializing box which contains buttons
     * @return the box with buttons
     */
    private HBox initButtonsBox() {
        HBox mButtonsBox = new HBox(20);
        mButtonsBox.setAlignment(Pos.CENTER);

        Button mSignUpButton = new Button("Зарегистрироваться");
        mSignUpButton.setFont(new Font(20));
        mSignUpButton.setTextFill(Paint.valueOf("#ffffff"));
        mSignUpButton.setStyle("-fx-background-color: #28A745;");
        mSignUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //If passwords is different
                if (!mPfPassword.getText().equals(mPfConfirmPassword.getText())){
                    callAlert("Пароли не совпадают", null, "Пароли должны совпадать");
                    mPfPassword.requestFocus();
                    return;
                }

                //If some of fields is empty
                if (mPfPassword.getText().equals("") || mTaUserSecondName.getText().equals("")
                        || mInitials.getText().equals("")){
                    callAlert("Пустые поля", null, "Инициалы, логин или пароли пустые");
                    return;
                }

                mPresenter.tryToSignIn(mTaUserSecondName.getText(), mPfPassword.getText(), mInitials.getText());
            }
        });


        mButtonsBox.getChildren().addAll(mSignUpButton);

        return mButtonsBox;
    }


    /**
     * Initializing box with welcome label
     * @return the welcome box
     */
    private HBox initTopLabelBox() {
        HBox mLabelBox = new HBox(10);
        mLabelBox.setAlignment(Pos.CENTER);

        Label mTopLabel = new Label("Регистрация");
        mTopLabel.setFont(new Font(18));

        mLabelBox.getChildren().addAll(mTopLabel);

        return mLabelBox;
    }


    /**
     * Initializing grid pane with fields to write login and password
     * @return the box with the fields
     */
    private GridPane initGridPane(){
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(15);
        pane.setAlignment(Pos.CENTER);

        //Left column with labels
        ColumnConstraints mLabelsColumn = new ColumnConstraints();
        mLabelsColumn.setHalignment(HPos.RIGHT);
        mLabelsColumn.setHgrow(Priority.ALWAYS);


        Label mLUserInitials = new Label("Инициалы:");
        mLUserInitials.setFont(new Font(13));

        mInitials = new TextField("");
        mInitials.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB){
                    mTaUserSecondName.requestFocus();
                    event.consume();
                }
            }
        });
        mInitials.setMaxWidth(200);
        mInitials.setFont(new Font(13));
        mInitials.setPrefHeight(10);
        mInitials.setTooltip(new Tooltip("Введите инициалы"));



        Label mLUserName = new Label("Логин:");
        mLUserName.setFont(new Font(13));

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
        mTaUserSecondName.setFont(new Font(13));
        mTaUserSecondName.setPrefHeight(10);
        mTaUserSecondName.setTooltip(new Tooltip("Введите логин"));


        Label mLUserPassword = new Label("Пароль:");
        mLUserPassword.setFont(new Font(13));

        //Text field to enter password
        mPfPassword = new PasswordField();
        mPfPassword.setMaxWidth(200);
        mPfPassword.setTooltip(new Tooltip("Введите пароль"));
        mPfPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB){
                    mPfConfirmPassword.requestFocus();
                    event.consume();
                }
            }
        });

        Label mLConfirmPassword = new Label("Повторить пароль:");
        mLUserPassword.setFont(new Font(13));

        //Text field to confirm password
        mPfConfirmPassword = new PasswordField();
        mPfConfirmPassword.setMaxWidth(200);
        mPfConfirmPassword.setTooltip(new Tooltip("Повторить пароль"));
        mPfConfirmPassword.setFont(new Font(13));

        //Add labels to first column
        pane.getColumnConstraints().add(0, mLabelsColumn);
        pane.add(mLUserInitials, 0, 0);
        pane.add(mLUserName, 0, 1);
        pane.add(mLUserPassword, 0, 2);
        pane.add(mLConfirmPassword, 0, 3);

        //Add text fields to the second column
        pane.add(mInitials, 1, 0);
        pane.add(mTaUserSecondName, 1, 1);
        pane.add(mPfPassword, 1, 2);
        pane.add(mPfConfirmPassword, 1, 3);

        return pane;
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
     * If Sign in passed successful then start lesson view
     */
    @Override
    public void signIn() {
        if (UserModel.getUserModel().getUserMode() == User.STUDENT_MODE) {
            StudentPresenter studPres = new StudentPresenter();
            studPres.start();
        } else {
            TeacherPresenter teachPres = new TeacherPresenter();
            teachPres.start();
        }
    }

    /**
     * Error while sign in
     */
    @Override
    public void signInError() {
        callAlert("Ошибка регистрации", "Невозможно зарегистрировать", "Непредвиденная ошибка");
    }
}
