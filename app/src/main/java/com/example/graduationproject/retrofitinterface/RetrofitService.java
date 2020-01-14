package com.example.graduationproject.retrofitinterface;

import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.form.FormDTO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {


    @POST("form/{id}")
    Call<UploadedSurveyDTO> getMySurveyList(@Path("id")String userID);

    @FormUrlEncoded
    @POST("upload")
    Call<FormDTO>  formUpload(@Field("params")String userID,
                              @Field("params")String title,
                              @Field("params")String description,
                              @Field("params")String json,
                              @Field("params")String time);
}
