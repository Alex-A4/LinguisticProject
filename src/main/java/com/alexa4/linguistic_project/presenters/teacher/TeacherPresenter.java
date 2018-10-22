package com.alexa4.linguistic_project.presenters.teacher;

import com.alexa4.linguistic_project.models.TextModel;
import com.alexa4.linguistic_project.presenters.TextInterface;
import com.alexa4.linguistic_project.presenters.UserPresenter;
import com.alexa4.linguistic_project.view.ViewTextInterface;
import com.alexa4.linguistic_project.view.teacher_views.FilesEditor;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeacherPresenter extends UserPresenter implements TextInterface {
    private ViewTextInterface mView;
    private TextModel mTextModel;

    public TeacherPresenter() {
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
        mView = new FilesEditor(this);
        mStage.setTitle("Tasks editor");
        mStage.setScene(new Scene(mView.getLayout()));
    }


    /**
     * Getting means of expressiveness which already exist in file
     * @return
     */
    public HashMap<String, ArrayList<String>> getFoundMeans() {
        return mTextModel.getFoundedMeans();
    }

    /**
     * Saving file with text and fileName
     * @param text the text of file
     * @param fileName the name of file
     */
    public boolean saveFileChanges(String text, String fileName) {
        return mTextModel.saveFileChanges(text, fileName);
    }

}
