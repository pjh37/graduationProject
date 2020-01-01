package com.example.graduationproject.form;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.graduationproject.NetworkManager;
import com.example.graduationproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseFormActivity extends AppCompatActivity {
    private EditText editTitle;
    private EditText editDescription;
    private LinearLayout parentContainer;
    private ArrayList<DialogVO> datas;
    private FormSaveManager formSaveManager;
    private String[] txtTypes={"단답형","장문형","Multiple Choice",
            "Checkboxes","Dropdown","범위 질문",
            "Multiple Choice Grid","날짜","시간","구획분할"};
    private int[] imgTypes={R.drawable.shortanswer,R.drawable.longanswer,R.drawable.multiplechoice,
            R.drawable.checkbox,R.drawable.dropdown, R.drawable.linear_scale,R.drawable.img_grid,
            R.drawable.date,R.drawable.time,R.drawable.divide_section};
    private ArrayList<FormAbstract> layouts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_form);
        init();
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
    public void save(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //기존에 있던것인지 확인
                JSONObject jsonObject=createJsonObject();
                //Log.v("테스트",jsonObject.toString());
                //처음 저장할때 insert를 위해 id=-1
                formSaveManager.save(-1,jsonObject,getTime());
                finish();
            }
        }).start();
    }
    public String getTime(){
        return String.valueOf(System.currentTimeMillis());
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnFormComponentCreate: {
                createDialog();
            }
            case R.id.submit:{
                submit();
            }
        }
    }
    public void submit(){
        try{
            JSONObject jsonObject;
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

    public JSONObject createJsonObject(){
        JSONObject jsonObject=new JSONObject();
        int formCnt=layouts.size();
        try {
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
    public void init(){
        formSaveManager= FormSaveManager.getInstance(this);
        layouts=new ArrayList<>();
        editTitle=(EditText)findViewById(R.id.editTitle);
        editDescription=(EditText)findViewById(R.id.editDescription);
        parentContainer=(LinearLayout)findViewById(R.id.container);
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
    }
}
