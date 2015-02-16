package com.mateoj.hacku3;

import com.mateoj.hacku3.models.Answer;
import com.mateoj.hacku3.models.Question;
import com.mateoj.hacku3.models.Topic;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by jose on 2/12/15.
 */
public interface APIService {

    @Multipart
    @POST("/handle_upload.php")
    Response uploadVideo(@Part("uploadedfile") TypedFile video);

    @GET("/API.php")
    void getAnswers(@Query("rquest") String action, @Query("UserID") int userId, Callback<List<Answer>> cb);

    @GET("/API.php?rquest=getAllTopics")
    void getCategories(Callback<List<Topic>> cb);

    @GET("/API.php")
    void getQuestions(@Query("rquest") String action, @Query("TopicID") int categoryId, Callback<List<Question>> callback);

    @GET("/API.php")
    void createAssessment(@Query("rquest") String action, @Query("UserID") int userId, Callback<Assessment> cb);

    @GET("/API.php")
    void createAnswer(@Query("rquest") String action,
                      @Query("UserID") int userId,
                      @Query("AssessmentID") int assessmentId,
                      @Query("QuestionID") int questionId,
                      @Query("VideoLink") String VideoUrl,
                      Callback<Answer> cb);
}
