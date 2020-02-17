package com.example.graduationproject.retrofitinterface;

import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.form.FormDTO;
import com.example.graduationproject.mainActivityViwePager.RequestType;
import com.example.graduationproject.mainActivityViwePager.SurveyDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {


    @GET("form/{type}/{pages}")
    Call<ArrayList<SurveyDTO>> getSurveyList(@Path("type") String type, @Path("pages")Integer page);

    @GET("search_keyword/{keyword}")
    Call<ArrayList<SurveyDTO>> getKeywordSurveyList(@Path("keyword") String keyword);

    @GET("form/{userEmail}")
    Call<ArrayList<UploadedSurveyDTO>> getSurveyList(@Path("userEmail") String userEmail);


    @FormUrlEncoded
    @POST("upload")
    Call<FormDTO>  formUpload(@Field("params")String userID,
                              @Field("params")String title,
                              @Field("params")String description,
                              @Field("params")String json,
                              @Field("params")String time);

}
