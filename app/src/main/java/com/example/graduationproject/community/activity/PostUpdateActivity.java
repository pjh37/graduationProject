package com.example.graduationproject.community.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.community.model.PostDTO;
import com.example.graduationproject.community.model.PostImageDTO;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PostUpdateActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int PICK_FROM_ALBUM=1;
    private Integer imageLen;
    private Integer postID;
    private File file;
    private ArrayList<MultipartBody.Part> files;
    private ArrayList<PostImageDTO> images;
    private ArrayList<Integer> deleteImageList;
    private AlertDialog alertDialog;
    Button btnPost;
    ImageButton imageAdd;
    EditText content;
    LinearLayout imageList;
    LinearLayout existedImageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_update);
        postID=getIntent().getIntExtra("postID",-1);
        content=(EditText) findViewById(R.id.content);
        content.setText(getIntent().getStringExtra("content"));
        btnPost=(Button)findViewById(R.id.btnPost);
        btnPost.setOnClickListener(this);
        imageAdd=(ImageButton)findViewById(R.id.imageAdd);
        imageAdd.setOnClickListener(this);

        imageList=(LinearLayout) findViewById(R.id.imageListView);
        deleteImageList=new ArrayList<>();
        existedImageList=(LinearLayout) findViewById(R.id.existedImageListView);
        files=new ArrayList<>();
        getImageInfo();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPost:{
                postUpdate();
                break;
            }
            case R.id.imageAdd:{
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
                break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {
            Uri fileUri = data.getData();
            ImageView img=new ImageView(this);
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(PostUpdateActivity.this).load(fileUri).into(img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageDeleteDialog(img,-1);
                }
            });
            imageList.addView(img);
            file=new File(getRealPathFromURI(fileUri));
            RequestBody fileReqBody = RequestBody.create(file, MediaType.parse("image/*"));
            MultipartBody.Part part = MultipartBody.Part.createFormData(file.getName(), file.getName(), fileReqBody);
            files.add(part);

        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getBaseContext().getContentResolver().query(contentURI, null, null, null, null);
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

    public void getImageInfo(){
        RetrofitApi.getService().getImageLen(postID).enqueue(new retrofit2.Callback<ArrayList<PostImageDTO>>(){
            @Override
            public void onResponse(Call<ArrayList<PostImageDTO>> call, @NonNull Response<ArrayList<PostImageDTO>> response) {
                Log.v("포스트","image info response");
                images=response.body();
                imageLoad();
            }
            @Override
            public void onFailure(Call<ArrayList<PostImageDTO>> call, Throwable t) { }
        });
    }
    public void imageLoad(){
        for(PostImageDTO image: images){
            ImageView img=new ImageView(this);
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(PostUpdateActivity.this).load(getString(R.string.baseUrl)+"post/image/thumbnail/"+postID+"/"+image.get_id()).into(img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //삭제되는 id를 서버로 보내자
                    imageDeleteDialog(img,image.get_id());
                }
            });
            existedImageList.addView(img);
        }

    }
    public void postUpdate(){
        Log.v("포스트","postUpdate");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("postID",postID);
        hashMap.put("content",content.getText().toString());
//        hashMap.put("userEmail", Session.getUserEmail()); // DB 쪽에서 필요없지 않은가
//        hashMap.put("time",System.currentTimeMillis()); // DB 쪽에서 필요없지 않은가
        hashMap.put("deleteList",deleteImageList);
        RetrofitApi.getService().postUpdate(hashMap,files).enqueue(new retrofit2.Callback<PostDTO>(){
            @Override
            public void onResponse(Call<PostDTO> call, @NonNull Response<PostDTO> response) {
                Log.v("포스트","postCreate response");
                finish();
            }
            @Override
            public void onFailure(Call<PostDTO> call, Throwable t) { }
        });
    }
    public void imageDeleteDialog(final ImageView img,int imageID){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("삭제하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(imageID!=-1){
                    deleteImageList.add(imageID);
                    existedImageList.removeView(img);
                }
                if(imageID==-1){
                    int index=imageList.indexOfChild(img);
                    files.remove(index);
                    imageList.removeView(img);
                }
                dialogInterface.cancel(); // 안해도 닫힘
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog=builder.create();
        alertDialog.show();
    }
}
