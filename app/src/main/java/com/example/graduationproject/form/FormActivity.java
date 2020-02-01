package com.example.graduationproject.form;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.NetworkManager;
import com.example.graduationproject.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class FormActivity extends AppCompatActivity {
    private static final int REQUEST_CODE=10;
    private CustomAlertDialog dialog;
    private EditText editTitle;
    private EditText editDescription;
    private ArrayList<DialogVO> datas;
    private LinearLayout container;
    private static final String KEY="SUBVIEWKEY";
    private static final String AUTOSAVE="SUBVIEWKEY";
    private JSONObject jsonObject;
    private ArrayList<Integer> subViews;

    private FormSaveManager formSaveManager;
    private ArrayList<FormAbstract> layouts;

    private String[] txtTypes={"단답형","장문형","Multiple Choice",
            "Checkboxes","Dropdown","범위 질문",
            "Multiple Choice Grid","날짜","시간","구획분할","이미지"};
    private int[] imgTypes={R.drawable.shortanswer,R.drawable.longanswer,R.drawable.multiplechoice,
            R.drawable.checkbox,R.drawable.dropdown, R.drawable.linear_scale,R.drawable.img_grid,
            R.drawable.date,R.drawable.time,R.drawable.divide_section,R.drawable.image};
    private int form_id;
    public String userEmail;
    private String jsonstr;
    private FormTypeImage formTypeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_form);

        init();
    }
    public void init(){
        Intent intent=getIntent();
        layouts=new ArrayList<>();

        userEmail =intent.getStringExtra("userEmail");
        form_id=intent.getIntExtra("form_id",-1);
        jsonstr=intent.getStringExtra("json");

        formSaveManager= FormSaveManager.getInstance(this);
        container=(LinearLayout)findViewById(R.id.container);
        editTitle=(EditText)findViewById(R.id.editTitle);
        editDescription=(EditText)findViewById(R.id.editDescription);
        Toolbar mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        datas=new ArrayList<>();
        for(int i=0;i<txtTypes.length;i++){
            DialogVO vo=new DialogVO();
            vo.setTxtType(txtTypes[i]);
            vo.setImgType(imgTypes[i]);
            datas.add(vo);
        }
        Log.v("테스트","getChildCount"+container.getChildAt(0)+"");

        load();
        registerReceiver(FormTypeImageBroadcastReceiver,new IntentFilter("com.example.graduationproject.FormTypeImage.IMAGE_ADD_BUTTON_CLICKED"));
    }
    BroadcastReceiver FormTypeImageBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int formTypeImageIndex=intent.getIntExtra("form_id",-1);
            formTypeImage= ((FormTypeImage)(container.getChildAt(formTypeImageIndex+2)));
        }
    };
    public String getTime(){
        return String.valueOf(System.currentTimeMillis());
    }
    public void save(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //기존에 있던것인지 확인
                JSONObject jsonObject=createJsonObject();
                formSaveManager.save(form_id,jsonObject,getTime());
                finish();
            }
        }).start();
    }
    public JSONObject createJsonObject(){
        JSONObject jsonObject=new JSONObject();
        //저장문제로 변경중
        //int formCnt=layouts.size();

        int formCnt=container.getChildCount()-2;
        try {
            jsonObject.put("userEmail", userEmail);
            jsonObject.put("title",editTitle.getText().toString());
            jsonObject.put("description",editDescription.getText().toString());
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<formCnt;i++){
                //jsonArray.put(i,layouts.get(i).getJsonObject());
                jsonArray.put(i,((FormAbstract)container.getChildAt(i+2)).getJsonObject());
            }
            jsonObject.put("formComponents",jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.v("테스트","save jsonobject : "+jsonObject.toString());
        return jsonObject;
    }
    public void load(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(jsonstr!=null){
                    try{
                        jsonObject=new JSONObject(jsonstr);
                    }catch (Exception e){}
                }else{
                    jsonObject=formSaveManager.load(form_id);
                }

                if(jsonObject!=null){
                    Log.v("테스트","load jsonobject : "+jsonObject.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //수정중
                                Gson gson=new Gson();
                                FormDTO formDTO=gson.fromJson(jsonObject.toString(), FormDTO.class);
                                editTitle.setText(formDTO.getTitle());
                                editDescription.setText(formDTO.getDescription());
                                ArrayList<FormComponentVO> forms=formDTO.getFormComponents();
                                if(forms==null){
                                    Log.v("테스트","FormComponentVO 크기 : "+"is null" );
                                }

                                for(int i=0;i<forms.size();i++){
                                    FormAbstract temp=FormFactory.getInstance(FormActivity.this,forms.get(i).getType())
                                            .createForm();
                                    if(temp instanceof FormTypeImage){
                                        ((FormTypeImage) temp).setFormComponent_id(i);
                                    }
                                    temp.formComponentSetting(forms.get(i));
                                    container.addView(temp);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                toastMessage(e.getMessage());
                            }
                        }
                    });
                }else{
                    toastMessage("데이터가 없습니다");
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("저장").setMessage("저장하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                save();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnFormComponentCreate: {
                createDialog();
                break;
            }
            case R.id.submit:{
                Log.v("테스트","submit"+ userEmail);
                submit();
                break;
            }
            case R.id.btnImageAdd:{
                Log.v("테스트","btnImageAdd가 눌렸습니다!!!!");
                break;
            }
        }
    }
    public void submit(){
        /*
        if(formSaveManager.isJsonExist(form_id)){
            String selection="_id=?";
            String[] selectionArgs=new String[]{String.valueOf(form_id)};
            formSaveManager.delete(selection,selectionArgs);
        }
        */
        try{
            jsonObject=createJsonObject();
            jsonObject.put("time",getTime());

            if(jsonObject!=null&&jsonstr==null){
                NetworkManager networkManager=NetworkManager.getInstance(getApplicationContext());
                networkManager.submit(jsonObject);
                finish();
            }else if(jsonstr!=null){
                NetworkManager networkManager=NetworkManager.getInstance(getApplicationContext());
                networkManager.update(jsonObject,form_id);
                finish();
            }
        }catch (Exception e){}
    }
    public void createDialog(){
        CustomAlertDialog dialog=new CustomAlertDialog(this,R.layout.custom_dialog_list_item,datas);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setAdapter(dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FormAbstract layout=FormFactory.getInstance(FormActivity.this,i).createForm();
                if(layout instanceof FormTypeImage){
                    ((FormTypeImage) layout).setFormComponent_id(container.getChildCount()-2);
                }
                container.addView(layout);
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Glide.with(FormActivity.this).load(data.getData()).into(formTypeImage.getmAttachedImage());
                formTypeImage.setDataUri(data.getData());
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void toastMessage(final String msg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(FormTypeImageBroadcastReceiver);
    }
}
