package com.alexa4.linguistic_project.presenters.teacher;

import com.alexa4.linguistic_project.data_stores.TaskConfig;
import com.alexa4.linguistic_project.data_stores.TaskResults;
import com.alexa4.linguistic_project.models.TextModel;
import com.alexa4.linguistic_project.presenters.TextInterface;
import com.alexa4.linguistic_project.presenters.UserPresenter;
import com.alexa4.linguistic_project.view.ViewTextInterface;
import com.alexa4.linguistic_project.view.teacher_views.AnswersCheckerView;
import com.alexa4.linguistic_project.view.teacher_views.FilesEditorView;
import com.sun.istack.internal.NotNull;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeacherPresenter extends UserPresenter implements TextInterface {
    private ViewTextInterface mView;
    private TextModel mTextModel;
    //Static field which contains link on current object
    private static TeacherPresenter mPresenter;

    public TeacherPresenter() {
        mTextModel = new TextModel();
        mPresenter = this;
    }

    /**
     * Constructor which needs to create modality window
     * @param taskName the name of selected task
     */
    private TeacherPresenter(String taskName) {
        mTextModel = new TextModel();
    }

    /**
     * @return the object of this class with init fields
     */
    public static TeacherPresenter getPresenter() {
        return mPresenter;
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
     * Starting FilesEditorView
     */
    @Override
    public void start() {
        mView = new FilesEditorView(this);
        mStage.setTitle("Редактор заданий");
        mStage.setScene(new Scene(mView.getLayout()));
    }

    public void runFilesEditor() {
        if (mView.getClass() == FilesEditorView.class)
            return;

        mView.detachPresenter();
        mView = new FilesEditorView(this);
        mStage.setTitle("Редактор заданий");
        mStage.setScene(new Scene(mView.getLayout()));
    }

    public void runAnswersChecker() {
        if (mView.getClass() == AnswersCheckerView.class)
            return;

        mView.detachPresenter();
        mView = new AnswersCheckerView(this);
        mStage.setTitle("Проверка заданий");
        mStage.setScene(new Scene(mView.getLayout()));
    }

    /**
     * Running FilesEditorView like modality window and open task from input taskName
     * @param taskName the name of task which need open
     */
    public void runModalityEditorWithTaskName(@NotNull String taskName) {
        TeacherPresenter tempPresenter = new TeacherPresenter(taskName);
        tempPresenter.startModality();
        tempPresenter.getText(taskName);
        ((FilesEditorView) tempPresenter.mView).fillUserChoice();
        ((FilesEditorView) tempPresenter.mView).updateTaskName(taskName);
    }

    /**
     * Creating new stage and starts it
     */
    private void startModality() {
        mView = new FilesEditorView(this);
        Stage tempStage = new Stage();
        tempStage.setTitle("Редактор заданий");
        tempStage.initModality(Modality.NONE);
        tempStage.setScene(new Scene(mView.getLayout()));
        tempStage.show();
    }

    /**
     * Getting means of expressiveness which already exist in file
     * @return
     */
    public HashMap<String, ArrayList<String>> getFoundMeans() {
        return mTextModel.getFoundedMeans();
    }

    /**
     * Reading original task getting founded means
     * @param taskName the name of task
     * @return the original marking of text
     */
    public HashMap<String, ArrayList<String>> getOriginalMeans(String taskName) {
        //Updating mFoundedMeans to original task
        mTextModel.getText(taskName);

        return mTextModel.getFoundedMeans();
    }

    public TaskResults verifyAndGetCorrectnessOfAnswers(
            HashMap<String, ArrayList<String>> originalMarking,
            HashMap<String, ArrayList<String>> userMarking) {

        return mTextModel.verifyAndGetTaskResultAnswers(originalMarking, userMarking);
    }

    /**
     * Saving file with text and fileName
     * @param text the text of file
     * @param fileName the name of file
     */
    public boolean saveFileChanges(String text, String fileName, TaskConfig config) {
        return mTextModel.saveFileChanges(text, fileName, config);
    }


    /**
     * Getting map of students answers
     * @return the map
     */
    public HashMap<String, ArrayList<String>> getAnswersMap() {
        return mTextModel.getAnswersMap();
    }


    /**
     * Getting text of student answer
     * @param userName the name of user
     * @param taskName the name of task
     */
    public void getAnswerText(String userName, String taskName) {
        mView.setText(mTextModel.getAnswerText(userName, taskName));
    }

    /**
     * Getting config of task
     */
    public TaskConfig getConfig() {
        return mTextModel.getConfig();
    }
}
