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
import com.example.graduationproject.MainActivity;
import com.example.graduationproject.NetworkManager;
import com.example.graduationproject.R;
import com.example.graduationproject.UploadedFormEditableActivity;
import com.example.graduationproject.UploadedSurveyRV;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.offlineform.OfflineFormRVAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class FormActivity extends AppCompatActivity {

    private ArrayList<DialogVO> datas;

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

    private FloatingActionButton fab_main, fab1_video, fab2_picture, fab3_item;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    Boolean isOpen = false;

    private static FormTypeImage formTypeImage;
    private static final int REQUEST_CODE = 10;

    private DragLinearLayout container;
    ScrollView scrollView;


    private JSONObject jsonObject;
    private int form_id;
    //    public String userEmail;
    private String jsonstr; // json을 string한거
    private EditText editTitle;
    private EditText editDescription;

    private int classifyByCategory;

    private boolean IsDraftEdit = false;

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

        editTitle=(EditText)findViewById(R.id.editTitle);
        editDescription=(EditText)findViewById(R.id.editDescription);

        datas=new ArrayList<>();
        for(int i=0;i<txtTypes.length;i++){
            DialogVO vo=new DialogVO();
            vo.setTxtType(txtTypes[i]);
            vo.setImgType(imgTypes[i]);
            datas.add(vo);
        }

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
                if (hasFocus && isOpen == false) { // IsFisrt
                    fab_open();
                } else if (hasFocus == false && isOpen) { // when lose focus and isOpen true
                    fab_close();
                }
            }
        });

        container = (DragLinearLayout) findViewById(R.id.container);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        container.setContainerScrollView(scrollView);

        //        registerReceiver(FormTypeImageBroadcastReceiver,new IntentFilter("com.example.graduationproject.FormTypeImage.IMAGE_ADD_BUTTON_CLICKED"));
        //formSaveManager= FormSaveManager.getInstance(this);

        Intent intent=getIntent();
        form_id=intent.getIntExtra("form_id",-1);
        jsonstr=intent.getStringExtra("json");
        classifyByCategory = intent.getIntExtra("category", -111);


        // 첫 시작인지, 중간편집 , 가편집인지 구분
        if (classifyByCategory == MainActivity.categoryNumber) {
            // 첫시작
        } else if (classifyByCategory == UploadedFormEditableActivity.categoryNumber || classifyByCategory == UploadedSurveyRV.categoryNumber) {
            // 중간편집
            load();
        }else if ( classifyByCategory == OfflineFormRVAdapter.categoryNumber) {
            // 가편집
            IsDraftEdit = true;
            Log.d("mawang", "FormActivity init - from OfflineFormRVAdapter ,IsDraftEdit:"+IsDraftEdit);
            load();
        }




    }

    public void load(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (jsonstr != null) { // edit 일때
                    try {
                        jsonObject = new JSONObject(jsonstr); // res 임
                    } catch (Exception e) {
                    }
                }
                // 위에서 분기해서 여기 안옴
//                else{Log.d("mawang","FormActivity load - jsonstr is null "); jsonObject=formSaveManager.load(form_id);}

                if(jsonObject!=null){
                    Log.v("테스트","load jsonobject : "+jsonObject.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Gson gson=new Gson();
                                //old_FormDTO formDTO=gson.fromJson(jsonObject.toString(), old_FormDTO.class);
//                                editTitle.setText(formDTO.getTitle());
//                                editDescription.setText(formDTO.getDescription());
//                                ArrayList<FormComponentVO> forms=formDTO.getFormComponents();

                                editTitle.setText(jsonObject.getString("title")); //good
                                editDescription.setText(jsonObject.getString("description")); //good
                                Type type = new TypeToken<ArrayList<FormComponentVO>>() {}.getType();
//                                Log.d("mawang","FormActivity load - type : "+type);
//                                 ArrayList<FormComponentVO> formItems = gson.fromJson(jsonObject.getString("json"), ArrayList.class); // not working
                                ArrayList<FormComponentVO> formItems = gson.fromJson(jsonObject.getString("json"), type);
//                                Log.d("mawang","FormActivity load - forms : "+formItems);

                                if (formItems == null) {
                                    Log.d("mawang", "FormActivity load - forms is null");
                                } else {
                                    Log.d("mawang", "FormActivity load - forms size : " + formItems.size());
                                }

                                for(int i=0;i<formItems.size();i++){
                                    FormAbstract temp = FormFactory.getInstance(FormActivity.this, formItems.get(i).getType()).createForm();

                                    if (temp instanceof FormTypeImage) {
                                        ((FormTypeImage) temp).setFormComponent_id(i); // i = 0 부터
                                    }
                                    // 먼저 생성하고 데이터 세팅이라,, oncreate는 실행되는거네
                                    temp.formComponentSetting(formItems.get(i)); // 데이터 세팅

                                    ViewGroup customlayout = (ViewGroup) temp.getChildAt(0); // 이미 부모 존재
                                    ImageView dragHandle = (ImageView)customlayout.findViewById(R.id.drag_view);
                                    //container.addView(temp); 이렇게 하면 다시 로드할 경우 드래그 안됨
                                    container.addDragView(temp, dragHandle);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                toastMessage(e.getMessage());
                            }
                        }
                    });
                }else{
                    //toastMessage("FormActivity 데이터가 없습니다"); // no need
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
            builder.setTitle("저장").setMessage("작성중인 설문은 저장하시겠습니까?");
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
    public void save(){

        try {
            jsonObject = createJsonObject();
            NetworkManager networkManager = NetworkManager.getInstance(getApplicationContext());

            if (jsonstr == null) {
                networkManager.draftSave(jsonObject);
                Log.d("mawang", "FormActivity save first : " + jsonObject.toString());
            } else { //jsonstr != null
                networkManager.draftUpdate(jsonObject, form_id);
                Log.d("mawang", "FormActivity save update : " + jsonObject.toString());
            }
            finish();
        } catch (Exception e) {
        }

        finish();

    }

    public void submit(){

        try {
            jsonObject = createJsonObject();
            NetworkManager networkManager = NetworkManager.getInstance(getApplicationContext());

            if (jsonstr == null || IsDraftEdit==true ) {
                // 가편집에서 완료는 첫 서밋이다.
                networkManager.submit(jsonObject);
//                Log.d("mawang", "FormActivity submit first : " + jsonObject.toString());
            } else { //jsonstr != null
                networkManager.update(jsonObject, form_id);
//                Log.d("mawang", "FormActivity submit update : " + jsonObject.toString());
            }
            finish();
        } catch (Exception e) {}
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
                FormAbstract layout = FormFactory.getInstance(FormActivity.this, FormType.VIDEO).createForm();
                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0); // 이미 부모 존재
                ImageView dragHandle = (ImageView) customlayout.findViewById(R.id.drag_view);
                container.addDragView(layout, dragHandle);
                fab_close();
                break;
            }
            case R.id.fab_pic: {
                FormAbstract layout = FormFactory.getInstance(FormActivity.this, FormType.IMAGE)
                        .createForm();

                ((FormTypeImage) layout).setFormComponent_id(container.getChildCount()-2); // 클래스 밖에서해야 imgId 가 똑같지 않구나

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                fab_close();
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
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
    public void fab_close() {
        fab1_video.startAnimation(fab_close);
        fab2_picture.startAnimation(fab_close);
        fab3_item.startAnimation(fab_close);
        fab_main.startAnimation(fab_anticlock);
        fab1_video.setClickable(false);  // anim 사용하려면 invisible 그래서 clickable -false
        fab2_picture.setClickable(false);
        fab3_item.setClickable(false);
        isOpen = false;
    }
//    BroadcastReceiver FormTypeImageBroadcastReceiver=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int formTypeImageIndex=intent.getIntExtra("form_id",-1);
//            formTypeImage= ((FormTypeImage)(container.getChildAt(formTypeImageIndex+2)));
//        }
//    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                Glide.with(FormActivity.this).load(fileUri).into(formTypeImage.getmAttachedImage());
                formTypeImage.setDataUri(fileUri);
            }
        }
    }

    public static void set_FormTypeImage_class(FormTypeImage fti){
        formTypeImage = fti;
    }

    public JSONObject createJsonObject(){
        JSONObject jsonObject=new JSONObject();
        int formCnt=container.getChildCount()-2;

        try {
            jsonObject.put("userEmail", Session.getUserEmail());
            //jsonObject.put("userEmail", MainActivity.getUserEmail());
            jsonObject.put("title",editTitle.getText().toString());
            jsonObject.put("description",editDescription.getText().toString());
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<formCnt;i++){
                jsonArray.put(i,((FormAbstract)container.getChildAt(i+2)).getJsonObject());
            }
            jsonObject.put("formComponents",jsonArray);
            jsonObject.put("time", getTime()); // 순서를 지키자
        }catch (Exception e){
            e.printStackTrace();
        }
        //Log.v("테스트","save jsonobject : "+jsonObject.toString());
        return jsonObject;
    }
    public String getTime(){
        return String.valueOf(System.currentTimeMillis());
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


}
