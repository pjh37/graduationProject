package com.example.graduationproject.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.graduationproject.ItemDecorate;
import com.example.graduationproject.R;
import com.example.graduationproject.form.FormComponentVO;
import com.example.graduationproject.form.FormDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class IndividualResultActivity extends AppCompatActivity {
    private String url;//getString(R.string.baseUrl);
    private int form_id;
    private FormDTO formDTO=null;
    private ArrayList<IndividualResultDTO> datas;
    private IndividualViewDTO individualViewDTO;
    private RecyclerView individualResultRV;
    private RecyclerView.Adapter  individualResultAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_result);
        url=getString(R.string.baseUrl);
        Intent intent=getIntent();
        form_id=intent.getIntExtra("form_id",-1);
        individualViewDTO=(IndividualViewDTO)intent.getSerializableExtra("result");
        datas=new ArrayList<>();
        individualResultRV=(RecyclerView)findViewById(R.id.recycleView);
        individualResultRV.setLayoutManager(new LinearLayoutManager(this));
        individualResultRV.addItemDecoration(new ItemDecorate());

        getServerForm(form_id);
    }
    public void getServerForm(int form_id){
        OkHttpClient client=new OkHttpClient();
        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(url+"individual/"+form_id)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try{
                    String res=response.body().string();
                    Log.v("테스트",res);
                    JSONObject jsonObject=new JSONObject(res);
                    Gson gson=new Gson();
                    formDTO=gson.fromJson(jsonObject.toString(), FormDTO.class);
                    ArrayList<FormComponentVO> componentVOS=formDTO.getFormComponents();
                    for(int i=0;i<componentVOS.size();i++){
                        IndividualResultDTO individualResultDTO=new IndividualResultDTO();
                        ArrayList<String> answers=individualViewDTO.getResult().get(i);
                        String answer="";
                        for(int j=0;j<answers.size();j++){
                            answer+=answers.get(j);
                        }
                        individualResultDTO.setQuestion(componentVOS.get(i).getQuestion());
                        individualResultDTO.setAnswer(answer);
                        Log.v("테스트",componentVOS.get(i).getQuestion()+"  "+answer);
                        datas.add(individualResultDTO);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            individualResultAdapter=new IndividualResultRV(getApplicationContext(),datas);
                            individualResultRV.setAdapter(individualResultAdapter);
                        }
                    });


                }catch (Exception e){e.printStackTrace();}
            }
        });


    }
}
