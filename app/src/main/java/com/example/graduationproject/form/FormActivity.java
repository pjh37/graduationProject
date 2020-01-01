package com.example.graduationproject.form;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.graduationproject.NetworkManager;
import com.example.graduationproject.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class FormActivity extends AppCompatActivity {
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
    private LinearLayout parentContainer;
    private String[] txtTypes={"단답형","장문형","Multiple Choice",
            "Checkboxes","Dropdown","범위 질문",
            "Multiple Choice Grid","날짜","시간","구획분할"};
    private int[] imgTypes={R.drawable.shortanswer,R.drawable.longanswer,R.drawable.multiplechoice,
            R.drawable.checkbox,R.drawable.dropdown, R.drawable.linear_scale,R.drawable.img_grid,
            R.drawable.date,R.drawable.time,R.drawable.divide_section};
    private int form_id;
    public String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_form);
        init();
    }
    public void init(){
        Intent intent=getIntent();
        layouts=new ArrayList<>();
        parentContainer=(LinearLayout)findViewById(R.id.container);
        userID=intent.getStringExtra("userNicName");
        form_id=intent.getIntExtra("_id",-1);
        subViews=new ArrayList<>();
        formSaveManager= FormSaveManager.getInstance(this);
        container=(LinearLayout)findViewById(R.id.container);
        editTitle=(EditText)findViewById(R.id.editTitle);
        editDescription=(EditText)findViewById(R.id.editDescription);
        Toolbar mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        datas=new ArrayList<>();
        for(int i=0;i<10;i++){
            DialogVO vo=new DialogVO();
            vo.setTxtType(txtTypes[i]);
            vo.setImgType(imgTypes[i]);
            datas.add(vo);
        }
        Log.v("테스트","getChildCount"+container.getChildAt(0)+"");
        load();
    }
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

        int formCnt=layouts.size();
        try {
            jsonObject.put("userID",userID);
            jsonObject.put("title",editTitle.getText().toString());
            jsonObject.put("description",editDescription.getText().toString());
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<formCnt;i++){
                jsonArray.put(i,layouts.get(i).getJsonObject());
            }
            jsonObject.put("formComponents",jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    public void load(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonObject=formSaveManager.load(form_id);
                if(jsonObject!=null){
                    Log.v("테스트",jsonObject.toString());
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

                                for(int i=0;i<forms.size();i++){
                                    FormAbstract temp=FormFactory.getInstance(getApplicationContext(),forms.get(i).getType())
                                            .createForm();
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
                //finish();
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
            }
            case R.id.submit:{
                Log.v("테스트","submit"+userID);
                submit();
            }
        }
    }
    public void submit(){
        try{
            jsonObject=createJsonObject();
            jsonObject.put("time",getTime());

            if(jsonObject!=null){
                NetworkManager networkManager=NetworkManager.getInstance(getApplicationContext());
                networkManager.submit(jsonObject);
            }
        }catch (Exception e){}
    }
    public void createDialog(){
        CustomAlertDialog dialog=new CustomAlertDialog(this,R.layout.custom_dialog_list_item,datas);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setAdapter(dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FormAbstract layout=FormFactory.getInstance(getApplicationContext(),i).createForm();
                layouts.add(layout);
                parentContainer.addView(layout);
            }
        });
        builder.show();
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
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
