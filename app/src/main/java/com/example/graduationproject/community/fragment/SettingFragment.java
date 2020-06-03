package com.example.graduationproject.community.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.graduationproject.R;
import com.example.graduationproject.community.adapter.FriendAdapter;
import com.example.graduationproject.community.model.FriendDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    static final int PICK_FROM_ALBUM = 1;
    static final int SETTING_FRAGMENT_FRIEND_ADAPTER = 0;

    ImageView profileImage;
    ImageView imageAdd;
    TextView friendCount;
    TextView findFriend;

    RecyclerView recyclerView;
    FriendAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FriendDTO> datas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.community_fragment_setting,container,false);
        profileImage=(ImageView)rootView.findViewById(R.id.profile_image);
        imageAdd=(ImageView)rootView.findViewById(R.id.imageAdd);
        imageAdd.setOnClickListener(new ClickListener());

        friendCount = rootView.findViewById(R.id.tvFriendCnt);
        findFriend = rootView.findViewById(R.id.tvFindFriend);
        findFriend.setOnClickListener(new ClickListener());

        recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        datas=new ArrayList<>();
        layoutManager= new LinearLayoutManager(getContext());
        adapter=new FriendAdapter(getContext(),datas,SETTING_FRAGMENT_FRIEND_ADAPTER);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFriendList();
    }

    @Override
    public void onStart() {
        super.onStart();
        Glide.with(getContext()).load(getContext().getString(R.string.baseUrl)+"user/profile/select/"+Session.getUserEmail()+".jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profileImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==PICK_FROM_ALBUM){
            Uri fileUri = data.getData();
            profileChange(getRealPathFromURI(fileUri));
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    public void profileChange(String imgPath){
        File file = new File(imgPath);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(Session.getUserEmail(), file.getName(), fileReqBody);


        RetrofitApi.getService().profileImageUpload(Session.getUserEmail(),part).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
    public void getFriendList(){
        HashMap<String, Object> input = new HashMap<>();
        input.put("userEmail", Session.getUserEmail());
        RetrofitApi.getService().friendSelect(input).enqueue(new retrofit2.Callback<ArrayList<FriendDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<FriendDTO>> call, Response<ArrayList<FriendDTO>> response) {
                if(response.body()!=null){
                    datas=response.body();
                    adapter.addItems(datas);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) { }
        });
    }
    class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageAdd:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);

//                    Snackbar.make(getView(),"imageAdd.",Snackbar.LENGTH_LONG).show();

                    break;

                case R.id.tvFindFriend:
                    Snackbar.make(getView(),"tvFindFriend.",Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
