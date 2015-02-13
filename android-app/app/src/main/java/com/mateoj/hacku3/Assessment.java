package com.mateoj.hacku3;

import com.mateoj.hacku3.models.Question;
import com.mateoj.hacku3.models.Topic;
import com.mateoj.hacku3.models.Video;

import java.util.List;

/**
 * Created by jose on 2/12/15.
 */
public class Assessment {
    public enum Mode{
        Practice,
        ForReals
    }

    private static Assessment currentAssessment;

    public static Assessment getCurrentAssessment() {
        if( currentAssessment == null) {
            currentAssessment = new Assessment();
        }

        return currentAssessment;
    }

    List<Question> questionsAnswered;

    List<Video> videos;

    Mode currentMode;

    Topic topic;


    /**
     * Gets topic.
     *
     * @return Value of topic.
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * Sets new topic.
     *
     * @param topic New value of topic.
     */
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    /**
     * Gets questionsAnswered.
     *
     * @return Value of questionsAnswered.
     */
    public List<Question> getQuestionsAnswered() {
        return questionsAnswered;
    }

    /**
     * Sets new videos.
     *
     * @param videos New value of videos.
     */
    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    /**
     * Gets videos.
     *
     * @return Value of videos.
     */
    public List<Video> getVideos() {
        return videos;
    }

    /**
     * Sets new questionsAnswered.
     *
     * @param questionsAnswered New value of questionsAnswered.
     */
    public void setQuestionsAnswered(List<Question> questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    /**
     * Sets new currentMode.
     *
     * @param currentMode New value of currentMode.
     */
    public void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
    }

    /**
     * Gets currentMode.
     *
     * @return Value of currentMode.
     */
    public Mode getCurrentMode() {
        return currentMode;
    }
}
