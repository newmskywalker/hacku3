package com.mateoj.hacku3.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jose on 2/12/15.
 */
public class Question {

    private int id;
    private String content;
    private Video videoAnswer;
    private Category category;
    private long timeElapsed;
    private long answeredDate;
    private User user;

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
     * Sets new answeredDate.
     *
     * @param answeredDate New value of answeredDate.
     */
    public void setAnsweredDate(long answeredDate) {
        this.answeredDate = answeredDate;
    }

    /**
     * Gets timeElapsed.
     *
     * @return Value of timeElapsed.
     */
    public long getTimeElapsed() {
        return timeElapsed;
    }

    /**
     * Gets answeredDate.
     *
     * @return Value of answeredDate.
     */
    public long getAnsweredDate() {
        return answeredDate;
    }

    /**
     * Sets new videoAnswer.
     *
     * @param videoAnswer New value of videoAnswer.
     */
    public void setVideoAnswer(Video videoAnswer) {
        this.videoAnswer = videoAnswer;
    }

    /**
     * Gets videoAnswer.
     *
     * @return Value of videoAnswer.
     */
    public Video getVideoAnswer() {
        return videoAnswer;
    }

    /**
     * Gets category.
     *
     * @return Value of category.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets new content.
     *
     * @param content New value of content.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Sets new timeElapsed.
     *
     * @param timeElapsed New value of timeElapsed.
     */
    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    /**
     * Sets new category.
     *
     * @param category New value of category.
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Gets content.
     *
     * @return Value of content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets user.
     *
     * @return Value of user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets new user.
     *
     * @param user New value of user.
     */
    public void setUser(User user) {
        this.user = user;
    }
}
