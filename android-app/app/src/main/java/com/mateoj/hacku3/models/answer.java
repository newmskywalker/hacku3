package com.mateoj.hacku3.models;

import java.util.List;

/**
 * Created by Bliss on 2/12/2015.
 */
class Answer {
    private int id;
    private  int userID;
    private String videoLink;
    private String feedBack;
    private int questionID;
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

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
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
}
