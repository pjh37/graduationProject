package com.example.graduationproject.retrofitinterface;

import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.community.model.ChatRoomTempDTO;
import com.example.graduationproject.community.model.CommentDTO;
import com.example.graduationproject.community.model.CommentReplyDTO;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.community.model.GroupDTO;
import com.example.graduationproject.community.model.PostDTO;
import com.example.graduationproject.community.model.PostImageDTO;
import com.example.graduationproject.mainActivityViwePager.SurveyDTO;
import com.example.graduationproject.messageservice.MessageDTO;
import com.example.graduationproject.offlineform.FormItem;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    //그룹 관련 api
    @Multipart
    @POST("group/create")
    Call<Boolean> groupCreate( @PartMap HashMap<String, Object> param,@Part MultipartBody.Part file);

    @GET("group/all/{userEmail}/{count}/{offset}")
    Call<ArrayList<GroupDTO>> grouptGet(@Path("userEmail")String userEmail,@Path("count")Integer count,@Path("offset")Integer offset);

    @FormUrlEncoded
    @POST("group/join")
    Call<Boolean> groupJoin(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("group/passwordCheck")
    Call<RetrofitResponse> groupPasswordCheck(@FieldMap HashMap<String, Object> param);

    @GET("group/my/{userEmail}")
    Call<ArrayList<GroupDTO>> getMyGroup(@Path("userEmail")String userEmail);

    //그룹 탈퇴
    @DELETE("group/withdraw/{id}")
    Call<RetrofitResponse> groupWithdraw(@Path("id")Integer id);

    //그룹 폭파 => 그룹의 호스트가 그룹을 아예 없애버리는것



    //게시글 api
    @Multipart
    @POST("post/create")
    Call<PostDTO> postCreate(@PartMap HashMap<String, Object> param,@Part ArrayList<MultipartBody.Part> files);

    @FormUrlEncoded
    @POST("post/add_comment")
    Call<Boolean> commentCreate(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("post/add_comment_reply")
    Call<Boolean> replyCreate(@FieldMap HashMap<String, Object> param);

    @GET("post/{groupID}/{count}/{offset}")
    Call<ArrayList<PostDTO>> getPost(@Path("groupID")Integer groupID,@Path("count")Integer count,@Path("offset")Integer offset);

    @GET("post/old_comment/{postID}/{count}/{offset}")
    Call<ArrayList<CommentDTO>> getComment(@Path("postID")Integer groupID, @Path("count")Integer count, @Path("offset")Integer offset);

    @GET("post/comment_reply/{postID}/{count}/{offset}")
    Call<ArrayList<CommentReplyDTO>> getReply(@Path("postID")Integer groupID, @Path("count")Integer count, @Path("offset")Integer offset);

    @GET("post/delete_comment/{id}")
    Call<Boolean> deleteComment(@Path("id")Integer _id);

    @GET("post/delete_reply/{id}")
    Call<Boolean> deleteReply(@Path("id")Integer _id);

    @Multipart
    @PUT("post/update")
    Call<PostDTO> postUpdate(@PartMap HashMap<String, Object> param,@Part ArrayList<MultipartBody.Part> files);

    @GET("post/images/{postID}")
    Call<ArrayList<PostImageDTO>> getImageLen (@Path("postID")Integer postID);

    @DELETE("post/delete/{id}")
    Call<RetrofitResponse> postDelete(@Path("id")Integer id);

}
