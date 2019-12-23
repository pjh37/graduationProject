package com.example.graduationproject.createform;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.graduationproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {
    private CustomAlertDialog dialog;
    private EditText editTitle;
    private EditText editDescription;
    private ArrayList<DialogVO> datas;
    private LinearLayout container;
    private static final String KEY="SUBVIEWKEY";
    private static final String AUTOSAVE="SUBVIEWKEY";
    private ArrayList<Integer> subViews;
    private ArrayList<FormComponent> formComponents;
    private FormSaveManager formSaveManager;
    private String[] txtTypes={"단답형","장문형","Multiple Choice",
            "Checkboxes","Dropdown","범위 질문",
            "Multiple Choice Grid","날짜","시간","구획분할"};
    private int[] imgTypes={R.drawable.shortanswer,R.drawable.longanswer,R.drawable.multiplechoice,
            R.drawable.checkbox,R.drawable.dropdown, R.drawable.linear_scale,R.drawable.img_grid,
            R.drawable.date,R.drawable.time,R.drawable.divide_section};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        subViews=new ArrayList<>();
        formComponents=new ArrayList<>();
        formSaveManager=FormSaveManager.getInstance(this);
        container=(LinearLayout)findViewById(R.id.container);
        editTitle=(EditText)findViewById(R.id.editTitle);
        editDescription=(EditText)findViewById(R.id.editDescription);
        if(savedInstanceState!=null){
            subViews=savedInstanceState.getIntegerArrayList(KEY);
            for(int i=0;i<subViews.size();i++){
                FormComponent formComponent=new FormComponent(getApplicationContext(),i);
                container.addView(formComponent);
            }
        }
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
    public void save(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("title",editTitle.getText().toString());
                    jsonObject.put("description",editDescription.getText().toString());
                    JSONArray jsonArray=new JSONArray();
                    for(int i=0;i<formComponents.size();i++){
                        jsonArray.put(i,formComponents.get(i).getJsonObject());
                    }
                    jsonObject.put("formComponents",jsonArray);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //Log.v("테스트",jsonObject.toString());
                ContentValues addValue=new ContentValues();
                addValue.put("json",jsonObject.toString());
                formSaveManager.insert(addValue);
            }
        }).start();
    }
    public void load(){

        new Thread(new Runnable() {
            JSONObject jsonObject=null;
            @Override
            public void run() {

                String[] columns=new String[]{"_id","json"};
                Cursor cursor=formSaveManager.query(columns,null,null,null,null,null);
                if(cursor!=null){
                    while(cursor.moveToNext()){
                        try{
                            jsonObject=new JSONObject(cursor.getString(1));
                            //1개만 가져옴,,  테스트용
                            break;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    cursor.close();
                }
                if(jsonObject!=null){
                    Log.v("테스트",jsonObject.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                editTitle.setText(jsonObject.getString("title"));
                                editDescription.setText(jsonObject.getString("description"));
                                JSONArray jsonArray=jsonObject.getJSONArray("formComponents");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject temp=jsonArray.getJSONObject(i);
                                    FormComponent formComponent=new FormComponent(getApplicationContext(),temp.getInt("type"));
                                    formComponent.setComponent(temp);
                                    container.addView(formComponent);
                                    subViews.add(i);
                                    formComponents.add(formComponent);
                                }
                            }catch (Exception e){}

                        }
                    });

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(KEY,subViews);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnFormComponentCreate:
                createDialog();
                break;
        }
    }
    public void createDialog(){
        dialog=new CustomAlertDialog(this,R.layout.custom_dialog_list_item,datas);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setAdapter(dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FormComponent formComponent=new FormComponent(getApplicationContext(),i);
                container.addView(formComponent);
                subViews.add(i);
                formComponents.add(formComponent);
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
}
