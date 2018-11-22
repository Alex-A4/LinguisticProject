package com.alexa4.linguistic_project.presenters.student;

import com.alexa4.linguistic_project.models.TextModel;
import com.alexa4.linguistic_project.presenters.TextInterface;
import com.alexa4.linguistic_project.presenters.UserPresenter;
import com.alexa4.linguistic_project.view.ViewTextInterface;
import com.alexa4.linguistic_project.view.student_views.LessonsView;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;

import java.util.List;

public class StudentPresenter extends UserPresenter implements TextInterface {
    private ViewTextInterface mView;
    private TextModel mTextModel;


    public StudentPresenter() {
        mTextModel = new TextModel();
    }

    /**
     * Getting the text of current open task
     * @param taskName the name of selected task
     */
    @Override
    public void getText(String taskName) {
        mView.setText(mTextModel.getText(taskName));
    }

    /**
     * Getting the list of files names to view
     * @return the list of files names
     */
    @Override
    public List<String> getFilesNameList() {
        return mTextModel.getTasksListOfFiles();
    }

    /**
     * Getting random color which will set to text
     * @return the color
     */
    @Override
    public Paint getTextColors() {
        return mTextModel.getRandomColor();
    }


    /**
     * Deleting user's choice of means from model's container
     * @param means the means which need delete
     * @param text the text which need delete
     */
    @Override
    public void deleteUserChoice(String means, String text) {
        mTextModel.deleteUserChoice(means, text);
    }

    /**
     * Add user's choice of means to model container
     * @param means the means of expressiveness that user choose
     * @param text the text which user select
     */
    @Override
    public void addUserChoice(String means, String text) {
        mTextModel.addUserChoice(means, text);
    }

    /**
     * Clearing userChoice
     */
    @Override
    public void clearUserChoice() {
        mTextModel.clearUserChoice();
    }


    /**
     * Starting FilesEditor view
     */
    @Override
    public void start() {
        mView = new LessonsView(this);
        mStage.setTitle("Решение заданий");
        mStage.setScene(new Scene(mView.getLayout()));
    }


    /**
     * Saving user answer
     * @return
     */
    public boolean saveUserAnswer() {
        return mTextModel.saveUserAnswer();
    }

}
