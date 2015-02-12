package com.mateoj.hacku3.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jose on 2/12/15.
 */
public class Question {

    private int id;
    private String question;
    private int rating;
    private  int status;
    private int topicId;

    private static List<Question> questionsList;

    /**
     * Sets new questionsList.
     *
     * @param questionsList New value of questionsList.
     */
    public static void setQuestionsList(List<Question> questionsList) {
        questionsList = questionsList;
    }

    /**
     * Gets questionsList.
     *
     * @return Value of questionsList.
     */
    public static List<Question> getQuestionsList() {
        if( questionsList == null)
            questionsList = new ArrayList<>();
        return questionsList;
    }

    /**
     * Gets id.
     *
     * @return Value of id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets new id.
     *
     * @param id New value of id.
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets topicId.
     *
     * @return Value of topicId.
     */
    public int getTopicId() {
        return topicId;
    }

    /**
     * Sets new question.
     *
     * @param question New value of question.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Sets new topicId.
     *
     * @param topicId New value of topicId.
     */
    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    /**
     * Gets question.
     *
     * @return Value of question.
     */
    public String getQuestion() {
        return question;
    }
}
