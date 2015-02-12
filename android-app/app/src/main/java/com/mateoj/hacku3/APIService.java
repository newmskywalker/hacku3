package com.mateoj.hacku3;

import com.mateoj.hacku3.models.Category;
import com.mateoj.hacku3.models.Question;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by jose on 2/12/15.
 */
public interface APIService {
    @GET("/categories")
    void getCategories(Callback<List<Category>> cb);

    @GET("/questions")
    void getQuestions(@Path("categoryId") int categoryId, Callback<List<Question>> callback);
}
