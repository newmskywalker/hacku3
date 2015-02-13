package com.mateoj.hacku3;

import com.mateoj.hacku3.models.Question;
import com.mateoj.hacku3.models.Video;

import java.util.List;

/**
 * Created by jose on 2/12/15.
 */
public class Assessment {
    private enum Mode{
        Practice,
        ForReals
    }

    List<Question> questionsAnswered;

    List<Video> videos;

}
