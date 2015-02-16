package com.mateoj.hacku3.models;

import java.util.List;

/**
 * Created by Bliss on 2/12/2015.
 */
public class Answer {
    private int id;
    private  int userID;
    private String videolink;
    private String feedback;
    private int questionID;
    private String question;
    private int assessmentID;

    private static List<Answer> answerList;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getVideolink() {
        return videolink;
    }

    public void setVideolink(String videolink) {
        this.videolink = videolink;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public static List<Answer> getAnswerList() {
        return answerList;
    }

    public static void setAnswerList(List<Answer> answerList) {
        Answer.answerList = answerList;
    }

    /**
     * Gets question.
     *
     * @return Value of question.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets new question.
     *
     * @param question New value of question.
     */
    public void setQuestion(String question) {
        this.question = question;
    }
}