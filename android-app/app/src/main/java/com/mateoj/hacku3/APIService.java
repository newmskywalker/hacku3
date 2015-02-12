package com.mateoj.hacku3;

import com.mateoj.hacku3.models.Question;
import com.mateoj.hacku3.models.Topic;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by jose on 2/12/15.
 */
public interface APIService {
    @GET("/API.php?rquest=getAllTopics")
    void getCategories(Callback<List<Topic>> cb);

    @GET("/API.php")
    void getQuestions(@Query("rquest") String action, @Query("TopicID") int categoryId, Callback<List<Question>> callback);
}
