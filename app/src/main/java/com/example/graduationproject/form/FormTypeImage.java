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
    private ImageButton mCopyView;
    private ImageButton mDeleteView;
    private Button btnImageAdd;
    private ImageView mAttachedImage;

    private File file; // 로컬 저장용
    private Uri uri;
    private Activity activity;
    private int formComponent_id;

    private boolean IsPoseted; // (수정불가용 불변수) ,null 처리용

    public FormTypeImage(Context context, int type) {
        super(context, type);
        mContext = context;
        this.mType = type;

        activity = (Activity) mContext;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_image, this, true);

        mEditQuestion = (EditText) findViewById(R.id.editQuestion);
        btnImageAdd = (Button) findViewById(R.id.btnImageAdd);
        mAttachedImage = (ImageView) findViewById(R.id.attached_image);
        mCopyView = (ImageButton) findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);

        btnImageAdd.setOnClickListener(new ClickListener());
        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

        IsPoseted = false;
    }
    public FormTypeImage(FormCopyFactory fcf) {
        super(fcf.getmContext(), fcf.getmType());
        mContext = fcf.getmContext();
        this.mType = fcf.getmType();

        activity = (Activity) mContext;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_image, this, true);

        mEditQuestion = (EditText) findViewById(R.id.editQuestion);
        btnImageAdd = (Button) findViewById(R.id.btnImageAdd);
        mAttachedImage = (ImageView) findViewById(R.id.attached_image);
        mCopyView = (ImageButton) findViewById(R.id.copy_view);
        mDeleteView = (ImageButton) findViewById(R.id.delete_view);

        mEditQuestion.setText(fcf.getEditQuestion_text());

        btnImageAdd.setOnClickListener(new ClickListener());
        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());

        uri = fcf.getFileUri();
        Glide.with(this).load(uri).into(mAttachedImage);
        formComponent_id = fcf.getFormComponent_id();

        IsPoseted = fcf.isImagePosted();
//        if(IsPoseted){ // == true
//            btnImageAdd.setVisibility(GONE); // 수정불가로 만들기
//        }

        file = new File(getImagePath(uri));
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", mType);
            jsonObject.put("question", mEditQuestion.getText().toString());
            jsonObject.put("real_file_name", formComponent_id);
            jsonObject.put("posted", IsPoseted);

            // null 처리
            if(IsPoseted) // == true
            {
                jsonObject.put("media_file", uri.toString()); // string
                jsonObject.put("real_file_data", file); // 처음엔 null 아니야?
            }

            Log.d("mawang", "FormTypeImage getJsonObject - file = " + file);
            Log.d("mawang", "FormTypeImage getJsonObject - uri = " + uri);
            Log.d("mawang", "FormTypeImage getJsonObject - IsPoseted = " + IsPoseted);
            Log.d("mawang", "FormTypeImage getJsonObject - formComponent_id = " + formComponent_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditQuestion.setText(vo.getQuestion());

        this.IsPoseted = vo.isPosted();

        if(IsPoseted) // == true
        {
            this.uri = Uri.parse(vo.getMedia_file());
            //        Glide.with(mContext).load(vo.getMedia_file()).into(mAttachedImage);
            Glide.with(mContext).load(uri).into(mAttachedImage);
//        file=new File(getImagePath(Uri.parse(vo.getMedia_file())));
            file = new File(getImagePath(uri));
            //formComponent_id 는 FormActivity 에서 set 해준다.
        }

        Log.d("mawang", "FormTypeImage formComponentSetting - formComponent_id = " + formComponent_id);
        Log.d("mawang", "FormTypeImage formComponentSetting - uri = " + uri);
        Log.d("mawang", "FormTypeImage formComponentSetting - file = " + file);
        Log.d("mawang", "FormTypeImage formComponentSetting - IsPoseted = " + IsPoseted);

//        if(IsPoseted){ // == true
//            btnImageAdd.setVisibility(GONE); // 수정불가로 만들기
//        }
    }


    public void setFormComponent_id(int id){
        formComponent_id=id;
    }
    public ImageView getmAttachedImage() {
        return mAttachedImage;
    }
    public void setDataUri(Uri uri) {
        this.uri = uri;
        file = new File(getImagePath(uri));
        IsPoseted = true;
        // 해줘야 생성시점에서도 file 이 null 이 아니다. update 도 해당된다.

    }

    public class ClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == mDeleteView) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                parentView.removeView(customView);
            }
            else if (view == mCopyView) {
                DragLinearLayout parentView = (DragLinearLayout) customView.getParent();
                int index = parentView.indexOfChild(customView);

                FormAbstract layout = new FormCopyFactory.Builder(mContext, mType)
                        .Question(mEditQuestion.getText().toString())
                        .FileUri(uri)
                        .FormComponentId(index + 1)  // setFormComponent_id , 복사되는건 원본 뒤에 생기니까 +1
                        .ImagePosted(IsPoseted)
                        .build()
                        .createCopyForm();

                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0);
                ImageView dragHandle = (ImageView) customlayout.getChildAt(0);
                parentView.addDragView(layout, dragHandle, index + 1);

            }
            else if (view == btnImageAdd) {

                FormActivity.set_FormTypeImage_class(FormTypeImage.this);

//                btnImageAdd.setVisibility(GONE); // 수정불가로 만들기

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                activity.startActivityForResult(gallery, 10);

            }
        }
    }

}
