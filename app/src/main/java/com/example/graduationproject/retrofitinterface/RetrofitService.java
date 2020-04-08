package com.example.graduationproject.retrofitinterface;

import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.community.model.ChatRoomDTO;
import com.example.graduationproject.community.model.ChatRoomTempDTO;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.form.FormDTO;
import com.example.graduationproject.mainActivityViwePager.RequestType;
import com.example.graduationproject.mainActivityViwePager.SurveyDTO;
import com.example.graduationproject.messageservice.MessageDTO;
import com.example.graduationproject.offlineform.FormItem;

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

    //설문 관련 요청
    @GET("form/{type}/{pages}")
    Call<ArrayList<SurveyDTO>> getSurveyList(@Path("type") String type, @Path("pages")Integer page);

    @GET("form/{userEmail}")
    Call<ArrayList<UploadedSurveyDTO>> getSurveyList(@Path("userEmail") String userEmail);

    @GET("Draftform/{userEmail}")
    Call<ArrayList<FormItem>> getDraftSurveyList(@Path("userEmail") String userEmail);

    @GET("search/{queryText}/{pages}")
    Call<ArrayList<FriendDTO>> getSearchResult(@Path("queryText") String queryText,@Path("pages")Integer page);

    @GET("user/{userEmail}")
    Call<Boolean> userRegister(@Path("userEmail") String userEmail);

    @Multipart
    @POST("user/profile/upload")
    Call<ResponseBody> profileImageUpload(@Part("userEmail") String userEmail,@Part MultipartBody.Part file);

    @GET("user/{userEmail}/{state}")
    Call<ArrayList<FriendDTO>> getFriendList(@Path("userEmail") String userEmail,@Path("state") Integer state);

    @GET("search_keyword/{keyword}")
    Call<ArrayList<SurveyDTO>> getKeywordSurveyList(@Path("keyword") String keyword);

    @GET("search_id/{id}")
    Call<ArrayList<UploadedSurveyDTO>> get_idSurveyList(@Path("id") Integer id);

    //커뮤니티 관련 요청
    @FormUrlEncoded
    @POST("friend/select")
    Call<ArrayList<FriendDTO>> friendSelect(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("friend/update")
    Call<Boolean> friendUpdate(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("friend/request")
    Call<Boolean> friendRequest(@FieldMap HashMap<String, Object> param);

    //채팅방,채팅 관련 요청
    @FormUrlEncoded
    @POST("chat/create")
    Call<Boolean> chatRoomCreateRequest(@FieldMap HashMap<String, Object> param);

    @GET("chat/rooms/{userEmail}")
    Call<ArrayList<ChatRoomTempDTO>> getRoomList(@Path("userEmail") String userEmail);

    //로컬 캐시와 디비의 메세지 캐시가 차이 날경우 디비의 메세지를 긁어 온다
    @GET("chat/rooms/{roomKey}/{count}/{offset}")
    Call<ArrayList<MessageDTO>> getRoomMessages(@Path("roomKey") String roomKey
            ,@Path("count") Integer count
            ,@Path("offset") Integer offset);
}
