package com.mateoj.hacku3.models;

import android.net.Uri;

/**
 * Created by jose on 2/12/15.
 */
public class Video {
    private Uri localUri;
    private int questionId;

    /**
     * Sets new questionId.
     *
     * @param questionId New value of questionId.
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    /**
     * Gets localUri.
     *
     * @return Value of localUri.
     */
    public Uri getLocalUri() {
        return localUri;
    }

    /**
     * Gets questionId.
     *
     * @return Value of questionId.
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * Sets new localUri.
     *
     * @param localUri New value of localUri.
     */
    public void setLocalUri(Uri localUri) {
        this.localUri = localUri;
    }
}
