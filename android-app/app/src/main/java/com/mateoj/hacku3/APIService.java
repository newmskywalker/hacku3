package com.mateoj.hacku3;

import com.mateoj.hacku3.models.Category;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by jose on 2/12/15.
 */
public interface APIService {
    @GET("/categories")
    void getCategories(Callback<List<Category>> cb);
}
