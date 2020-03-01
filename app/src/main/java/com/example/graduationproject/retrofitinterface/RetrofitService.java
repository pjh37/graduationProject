package com.example.graduationproject.retrofitinterface;

import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.form.FormDTO;
import com.example.graduationproject.mainActivityViwePager.RequestType;
import com.example.graduationproject.mainActivityViwePager.SurveyDTO;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitService {


    @GET("form/{type}/{pages}")
    Call<ArrayList<SurveyDTO>> getSurveyList(@Path("type") String type, @Path("pages")Integer page);

    @GET("form/{userEmail}")
    Call<ArrayList<UploadedSurveyDTO>> getSurveyList(@Path("userEmail") String userEmail);

    @GET("search/{queryText}/{pages}")
    Call<ArrayList<FriendDTO>> getSearchResult(@Path("queryText") String queryText,@Path("pages")Integer page);

    @GET("user/{userEmail}")
    Call<Boolean> userRegister(@Path("userEmail") String userEmail);

    @Multipart
    @POST("user/profile/upload")
    Call<ResponseBody> profileImageUpload(@Part("userEmail") String userEmail,@Part MultipartBody.Part file);

    @GET("user/{userEmail}/{state}")
    Call<ArrayList<FriendDTO>> getFriendList(@Path("userEmail") String userEmail,@Path("state") Integer state);

    @FormUrlEncoded
    @POST("friend/select")
    Call<ArrayList<FriendDTO>> friendSelect(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("friend/update")
    Call<Boolean> friendUpdate(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("friend/request")
    Call<Boolean> friendRequest(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("upload")
    Call<FormDTO>  formUpload(@Field("params")String userID,
                              @Field("params")String title,
                              @Field("params")String description,
                              @Field("params")String json,
                              @Field("params")String time);

}
