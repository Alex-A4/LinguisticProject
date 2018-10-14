package com.alexa4.linguistic_project.view.auth_views;

import com.alexa4.linguistic_project.data_stores.User;
import com.alexa4.linguistic_project.models.UserModel;
import com.alexa4.linguistic_project.presenters.AuthentificationPresenter;
import com.alexa4.linguistic_project.presenters.student.StudentPresenter;
import com.alexa4.linguistic_project.presenters.teacher.TeacherPresenter;
import com.alexa4.linguistic_project.view.ViewInterface;
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

public class SignInView implements ViewInterface {
    private AuthentificationPresenter mPresenter;
    private TextField mTaUserSecondName;
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

        Button mSignUpButton = new Button("Sign up");
        mSignUpButton.setFont(new Font(20));
        mSignUpButton.setTextFill(Paint.valueOf("#ffffff"));
        mSignUpButton.setStyle("-fx-background-color: #28A745;");
        mSignUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!mPfPassword.getText().equals(mPfConfirmPassword.getText())){
                    callAlert("Passwords do not much", null, "Passwords must be equals");
                    mPfPassword.requestFocus();
                    return;
                }

                if (mPfPassword.getText().equals("") || mTaUserSecondName.equals("")){
                    callAlert("Empty fields", null, "User login or password is empty");
                    return;
                }

                mPresenter.tryToSignIn(mTaUserSecondName.getText(), mPfPassword.getText());
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

        Label mTopLabel = new Label("Sign in");
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


        Label mLUserName = new Label("User login:");
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
        mTaUserSecondName.setTooltip(new Tooltip("Enter your login"));


        Label mLUserPassword = new Label("Password:");
        mLUserPassword.setFont(new Font(13));

        //Text field to enter password
        mPfPassword = new PasswordField();
        mPfPassword.setMaxWidth(200);
        mPfPassword.setTooltip(new Tooltip("Enter your password"));
        mPfPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB){
                    mPfConfirmPassword.requestFocus();
                    event.consume();
                }
            }
        });

        Label mLConfirmPassword = new Label("Confirm password:");
        mLUserPassword.setFont(new Font(13));

        //Text field to confirm password
        mPfConfirmPassword = new PasswordField();
        mPfConfirmPassword.setMaxWidth(200);
        mPfConfirmPassword.setTooltip(new Tooltip("Confirm your password"));
        mPfConfirmPassword.setFont(new Font(13));

        //Add labels to first column
        pane.getColumnConstraints().add(0, mLabelsColumn);
        pane.add(mLUserName, 0, 0);
        pane.add(mLUserPassword, 0, 1);
        pane.add(mLConfirmPassword, 0, 2);

        //Add text fields to the second column
        pane.add(mTaUserSecondName, 1, 0);
        pane.add(mPfPassword, 1, 1);
        pane.add(mPfConfirmPassword, 1, 2);

        return pane;
    }

    /**
     * Call Dialog Alert if something happens
     * @param title the title of Alert
     * @param header the header of Alert
     * @param content the content text of Alert
     */
    private void callAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        if (header != null)
            alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * If Sign in passed successful then start lesson view
     */
    @Override
    public void signIn() {
        if (UserModel.getUserModel().getCurrentUserMode() == User.STUDENT_MODE) {
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
        callAlert("Sign in error", "Can not sign in new account", "Incredible error");
    }
}
