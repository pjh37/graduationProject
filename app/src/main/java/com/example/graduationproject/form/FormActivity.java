package com.example.graduationproject.form;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.NetworkManager;
import com.example.graduationproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class FormActivity extends AppCompatActivity {
//    private LinearLayout container;
    private EditText editTitle;
    private EditText editDescription;
    private ArrayList<DialogVO> datas;

//    private static final String KEY="SUBVIEWKEY";
//    private static final String AUTOSAVE="SUBVIEWKEY";

    private JSONObject jsonObject;
//    private ArrayList<Integer> subViews;

    private FormSaveManager formSaveManager;

    private String[] txtTypes = {"단답형", "장문형",
            "객관식 질문", "체크박스", "드롭다운"
            , "직선 그리드", "객관식 그리드", "체크박스 그리드",
            "날짜", "시간",
            "구획분할"
            , "설명추가"};

    private int[] imgTypes = {R.drawable.shortanswer, R.drawable.longanswer
            , R.drawable.multiplechoice, R.drawable.checkbox, R.drawable.dropdown,
            R.drawable.linear_scale, R.drawable.radiogrid, R.drawable.checkboxgrid,
            R.drawable.date, R.drawable.time
            , R.drawable.divide_section
            , R.drawable.subtext};

    private int form_id;
    public String userEmail;
    private String jsonstr;

    private FloatingActionButton fab_main, fab1_video, fab2_picture, fab3_item;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    Boolean isOpen = false;

    private static FormTypeImage formTypeImage;
    private static final int REQUEST_CODE = 10;

    private DragLinearLayout container;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_form);
        init();
    }
    public void init(){
        Toolbar mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        userEmail =intent.getStringExtra("userEmail");
        form_id=intent.getIntExtra("form_id",-1);
        jsonstr=intent.getStringExtra("json");

        formSaveManager= FormSaveManager.getInstance(this);

        editTitle=(EditText)findViewById(R.id.editTitle);
        editDescription=(EditText)findViewById(R.id.editDescription);

        datas=new ArrayList<>();
        for(int i=0;i<txtTypes.length;i++){
            DialogVO vo=new DialogVO();
            vo.setTxtType(txtTypes[i]);
            vo.setImgType(imgTypes[i]);
            datas.add(vo);
        }

        load();
//        registerReceiver(FormTypeImageBroadcastReceiver,new IntentFilter("com.example.graduationproject.FormTypeImage.IMAGE_ADD_BUTTON_CLICKED"));

        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        fab_main = findViewById(R.id.fab);
        fab1_video = findViewById(R.id.fab_video);
        fab2_picture = findViewById(R.id.fab_pic);
        fab3_item = findViewById(R.id.fab_item);

        fab_main.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && isOpen == false) { // focus 이벤트 가 먼저 occur 되고 그 다음 click 이벤트가 인식된다.
                    fab_open();
                }
                else if(hasFocus==false && isOpen){ // when lose focus and isOpen true
                    fab_close();
                }
            }
        });

        container = (DragLinearLayout) findViewById(R.id.container);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        container.setContainerScrollView(scrollView);
    }
//    BroadcastReceiver FormTypeImageBroadcastReceiver=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int formTypeImageIndex=intent.getIntExtra("form_id",-1);
//            formTypeImage= ((FormTypeImage)(container.getChildAt(formTypeImageIndex+2)));
//        }
//    };
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
        if (isOpen) {
            fab_close();
        }else{
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
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.submit: {
                submit();
                fab_close();
                break;
            }
            case R.id.fab: {
                if (isOpen) {
                    fab_close();
                }
                else {
                    fab_open();
                }
                break;
            }
            case R.id.fab_video: {
                Toast.makeText(getApplicationContext(), "fab_video", Toast.LENGTH_SHORT).show();
                fab_close();
                break;
            }
            case R.id.fab_pic: {
                FormAbstract layout = FormFactory.getInstance(FormActivity.this, FormType.IMAGE)
                        .createForm();

                ((FormTypeImage) layout).setFormComponent_id(container.getChildCount()-2);

                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0); // 이미 부모 존재
                ImageView dragHandle = (ImageView)customlayout.findViewById(R.id.drag_view);

                container.addDragView(layout, dragHandle);
                fab_close();
                break;
            }
            case R.id.fab_item: { //  R.id.btnFormComponentCreate 바꾸자
                createDialog();
                fab_close();
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
                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0); // 이미 부모 존재
                ImageView dragHandle = (ImageView)customlayout.findViewById(R.id.drag_view);
                container.addDragView(layout, dragHandle);
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Uri fileUri = data.getData();
                Glide.with(FormActivity.this).load(fileUri).into(formTypeImage.getmAttachedImage());
                formTypeImage.setDataUri(fileUri);
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(FormTypeImageBroadcastReceiver);
//    }

    public void fab_open() {
        fab1_video.startAnimation(fab_open);
        fab2_picture.startAnimation(fab_open);
        fab3_item.startAnimation(fab_open);

        fab_main.startAnimation(fab_clock);

        fab1_video.setClickable(true);
        fab2_picture.setClickable(true);
        fab3_item.setClickable(true);

        isOpen = true;
    }
    public void fab_close(){

        fab1_video.startAnimation(fab_close);
        fab2_picture.startAnimation(fab_close);
        fab3_item.startAnimation(fab_close);

        fab_main.startAnimation(fab_anticlock);

        fab1_video.setClickable(false);  // anim 사용하려면 invisible 그래서 clickable -false
        fab2_picture.setClickable(false);
        fab3_item.setClickable(false);

        isOpen = false;
    }
    public static void set_FormTypeImage_class(FormTypeImage fti){
        formTypeImage = fti;
    }
}
