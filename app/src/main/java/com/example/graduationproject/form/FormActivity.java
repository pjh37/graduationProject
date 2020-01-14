package com.example.graduationproject.form;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
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
    public String userEmail;
    private String jsonstr;
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
        userEmail =intent.getStringExtra("userEmail");
        form_id=intent.getIntExtra("form_id",-1);
        jsonstr=intent.getStringExtra("json");
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
                                //Log.v("테스트","FormComponentVO 크기 : "+forms.size() );
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
                FormAbstract layout=FormFactory.getInstance(getApplicationContext(),i).createForm();
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
