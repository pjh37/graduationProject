package com.example.graduationproject.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class FormTypeImage extends FormAbstract{
    private Context mContext;
    private int mType;

    private LayoutInflater mInflater;
    private View customView;

    private EditText mEditQuestion;
    private Button btnImageAdd;
    private ImageView mAttachedImage;
    private ImageButton mCopyView;
    private ImageButton mDeleteView;

    private File file; // 로컬 저장용
    private Uri uri;
    private Activity activity;
    private int formComponent_id; // 원래는 image local file name 이다. 하지만 파일명으로 바꾸었다.

    public FormTypeImage(Context context, int type) {
        super(context, type);
        mContext=context;
        this.mType=type;

        activity=(Activity)mContext;

        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_image,this,true);

        mEditQuestion=(EditText)findViewById(R.id.editQuestion);
        btnImageAdd=(Button)findViewById(R.id.btnImageAdd);
        mAttachedImage=(ImageView)findViewById(R.id.attached_image);
        mCopyView=(ImageButton)findViewById(R.id.copy_view);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);

        btnImageAdd.setOnClickListener(new ClickListener());
        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

    }
    public FormTypeImage(FormCopyFactory fcf) {
        super(fcf.getmContext(), fcf.getmType());
        mContext = fcf.getmContext();
        this.mType = fcf.getmType();

        activity=(Activity)mContext;

        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView=mInflater.inflate(R.layout.form_type_image,this,true);

        mEditQuestion=(EditText)findViewById(R.id.editQuestion);
        btnImageAdd=(Button)findViewById(R.id.btnImageAdd);
        mAttachedImage=(ImageView)findViewById(R.id.attached_image);
        mCopyView=(ImageButton)findViewById(R.id.copy_view);
        mDeleteView=(ImageButton)findViewById(R.id.delete_view);

        mEditQuestion.setText(fcf.getEditQuestion_text());

        btnImageAdd.setOnClickListener(new ClickListener());
        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

        uri = fcf.getFileUri();
        Glide.with(this).load(uri).into(mAttachedImage);
        formComponent_id = fcf.getFormComponent_id();

    }

//    public String getImageToString(File file){
//        Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
//        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
//        byte[] imageBytes=byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(imageBytes,Base64.NO_WRAP);
//    }
//    public Bitmap getStringToImage(String encodedStr){
//        byte[] decodedByteArray=Base64.decode(encodedStr,Base64.NO_WRAP);
//        return BitmapFactory.decodeByteArray(decodedByteArray,0,decodedByteArray.length);
//    }
    public String getImagePath(Uri uri){
        Log.v("절대경로","uri : "+uri.toString());
        String filePath=null;
        //어느쪽이 실행되는걸까? 대부분이 세그먼트식이네
        if(pathType(uri)){
            String docId= DocumentsContract.getDocumentId(uri);
            String[] split=docId.split(":");
            String type=split[0];
            Uri contentUri=null;
            if("image".equals(type)){
                contentUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            String selection=MediaStore.Images.Media._ID+"=?";
            String[] selectionargs=new String[]{split[1]};
            String columm="_data";
            String[] proj={columm};
            Cursor cursor=activity.getContentResolver().query(contentUri,proj,selection,selectionargs,null);

            if(cursor!=null&&cursor.moveToFirst()){
                int column_index=cursor.getColumnIndexOrThrow(columm);
                filePath=cursor.getString(column_index);
            }
            Log.v("절대경로","도큐먼트식 : "+filePath);
            cursor.close();
            return filePath;
        }else{
            String selection= MediaStore.Images.Media._ID+"=?";
            String[] selectionargs=new String[]{uri.getLastPathSegment()};
            String columm="_data";
            String[] proj={columm};
            Cursor cursor=activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,proj,selection,selectionargs,null);
            if(cursor!=null&&cursor.moveToFirst()){
                int column_index=cursor.getColumnIndexOrThrow(columm);
                filePath=cursor.getString(column_index);
            }
            Log.v("절대경로","세그먼트 : "+filePath);
            cursor.close();
            return filePath;
        }

    }
    public boolean pathType(Uri uri){
        if(uri.toString().contains("com.android.providers")){
            return true;//도큐먼트식
        }
        return false;//세그먼트식
    }
    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("type",mType);
            jsonObject.put("question",mEditQuestion.getText().toString());
            jsonObject.put("media_file", uri.toString());
            jsonObject.put("real_file_data",file);
            jsonObject.put("real_file_name",formComponent_id);
//            Log.v("테스트","getJsonObject 이미지 파일 : "+getImageToString(file));
            Log.d("mawang", "FormTypeImage getJsonObject - file = " + file);
            Log.d("mawang", "FormTypeImage getJsonObject - uri = " + uri);
        }catch (Exception e){
            e.printStackTrace();
        }
        //Log.v("테스트","이미지뷰"+getImageToString(file));
        return jsonObject;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditQuestion.setText(vo.getQuestion());
        this.uri=Uri.parse(vo.getMedia_file());
//        Glide.with(mContext).load(vo.getMedia_file()).into(mAttachedImage);
        Glide.with(mContext).load(uri).into(mAttachedImage);
//        file=new File(getImagePath(Uri.parse(vo.getMedia_file())));
        file = new File(getImagePath(uri));
    }


    public void setFormComponent_id(int id){
        formComponent_id=id;
    }
    public ImageView getmAttachedImage() {
        return mAttachedImage;
    }
    public void setDataUri(Uri uri){
        this.uri=uri;
        file = new File(getImagePath(uri));
        // 해줘야 생성시점에서도 file 이 null 이 아니다. update 도 해당되겠지
    }

    public class ClickListener implements OnClickListener{
        @Override
        public void onClick(View view) {
            if(view==mDeleteView){
                ViewGroup parentView=(ViewGroup)customView.getParent();
                parentView.removeView(customView);
            }else if (view == mCopyView) {
                DragLinearLayout parentView = (DragLinearLayout) customView.getParent();
                int index = parentView.indexOfChild(customView);

                FormAbstract layout = new FormCopyFactory.Builder(mContext,mType)
                        .Question(mEditQuestion.getText().toString())
                        .FileUri(uri)
                        .FormComponentId(index+1)  // setFormComponent_id , 복사되는건 원본 뒤에 생기니까 +1
                        .build()
                        .createCopyForm();

                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0);
                ImageView dragHandle = (ImageView)customlayout.getChildAt(0);
                parentView.addDragView(layout, dragHandle, index + 1);

            }else if(view==btnImageAdd){
//                Intent broadcast=new Intent();
//                broadcast.setAction("com.example.graduationproject.FormTypeImage.IMAGE_ADD_BUTTON_CLICKED");
//                broadcast.putExtra("form_id",formComponent_id);
//                activity.sendBroadcast(broadcast);

                FormActivity.set_FormTypeImage_class(FormTypeImage.this);
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activity.startActivityForResult(intent,10);

            }
        }
    }

}
