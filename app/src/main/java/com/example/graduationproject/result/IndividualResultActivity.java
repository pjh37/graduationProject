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
import com.example.graduationproject.form.FormType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class IndividualResultActivity extends AppCompatActivity {
    private String url;//getString(R.string.baseUrl);
    private int form_id;

    private ArrayList<IndividualResultDTO> datas;
    private IndividualViewDTO individualViewDTO;

    private RecyclerView individualResultRV;
    private IndividualResultRV individualResultAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_result);
        url=getString(R.string.baseUrl);
        Intent intent=getIntent();
        form_id=intent.getIntExtra("form_id",-1);
        individualViewDTO=(IndividualViewDTO)intent.getSerializableExtra("result");
        datas=new ArrayList<>();


        individualResultRV = (RecyclerView) findViewById(R.id.recycleView);
        individualResultRV.setLayoutManager(new LinearLayoutManager(this));
        individualResultRV.addItemDecoration(new ItemDecorate());
        individualResultAdapter = new IndividualResultRV(getApplicationContext(), datas); //
        individualResultRV.setAdapter(individualResultAdapter);

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
                    Log.d("각결과테스트","IndividualResultActivity  : "+res);
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<FormComponentVO>>() {}.getType();
                    ArrayList<FormComponentVO> componentVOS = gson.fromJson(res, type);

                    for(int i=0;i<componentVOS.size();i++){
                        IndividualResultDTO individualResultDTO=new IndividualResultDTO();
                        String answer="";
                        ArrayList<String> answers = individualViewDTO.getResult().get(i);

                        if (answers != null) {

                            if (componentVOS.get(i).getType() == FormType.RADIOCHOICEGRID) {
                                for (int j = 0; j < answers.size(); j++) {
                                    answer += (componentVOS.get(i).getAddedRowOption().get(j)
                                            + " - "
                                            + componentVOS.get(i).getAddedColOption().get(Integer.parseInt(answers.get(j))));
                                    if (j != answers.size() - 1) {
                                        answer += "\n";
                                    }
                                }
                            }else if (componentVOS.get(i).getType() == FormType.CHECKBOXGRID) { // 따로해줘야됨 , 멀티체크가 되는거라
                                for (int j = 0; j < answers.size(); j++) {
                                    answer += componentVOS.get(i).getAddedRowOption().get(j) + " - ";

                                    answers.set(j, answers.get(j).replace("[\"", "")
                                            .replace("\",\"", "")
                                            .replace("\"]", ""));

                                    for (int k = 0; k < answers.get(j).length(); k++) {

                                        if (k == answers.get(j).length() - 1) {
                                            answer += componentVOS.get(i).getAddedColOption().get(Character.getNumericValue(answers.get(j).charAt(k)));

                                        } else {
                                            answer += componentVOS.get(i).getAddedColOption().get(Character.getNumericValue(answers.get(j).charAt(k)));
                                            answer += ",";
                                        }
                                    }

                                    if (j != answers.size() - 1) {
                                        answer += "\n";
                                    }
                                }
                            } else if (componentVOS.get(i).getType() == FormType.CHECKBOXES) {

                                for (int j = 0; j < answers.size(); j++) {
                                    if (j == answers.size() - 1 && !answers.get(j).isEmpty()) { // etc에 text존재
                                        answer += answers.get(j);
                                    }else if(j == answers.size() - 1 && answers.get(j).isEmpty()){ // etc에 text 없음
                                        answer = answer.substring(0,answer.length()-1); // 끝에 comma 제거
                                    }else if(j == answers.size() - 2 && answers.get(j).isEmpty()){ // etc 표시
                                        answer += " 기타) ";
                                    } else { // 체크박스들 답안
                                        answer += answers.get(j) + ",";
                                    }
                                }
//                            Log.d("mawang", "IndividualResultActivity onResponse FormType.CHECKBOXES - 문자열 = " + answer);
                            }else if (componentVOS.get(i).getType() == FormType.RADIOCHOICE) {
                                for (int j = 0; j < answers.size(); j++) {
                                    if (j == answers.size() - 2 && answers.get(j).isEmpty()) {
                                        answer += "기타) ";
                                    } else {
                                        answer += answers.get(j);
                                    }
                                }
                            } else if (componentVOS.get(i).getType() == FormType.LONGTEXT)
                            {
                                answer +=answers.get(0).replace("rn", "\n"); // escape 문자 처리
                            } else {
                                // loop 필요없음
//                            for (int j = 0; j < answers.size(); j++)
//                            {
//                                answer += answers.get(j);
//                            }
                                answer += answers.get(0);

                            }

                        }else{ // 일반항목에서 답을 고르지 않았을 경우
                            Log.d("mawang", "IndividualResultActivity getServerForm onResponse - answers is null");
                            answer +="(응답X)";
                        }


                        individualResultDTO.setQuestion(componentVOS.get(i).getQuestion());
                        individualResultDTO.setAnswer(answer);
                        individualResultDTO.setType(componentVOS.get(i).getType());

                        datas.add(individualResultDTO);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            individualResultAdapter.setDatas(datas);
                        }
                    });


                }catch (Exception e){e.printStackTrace();}
            }
        });


    }
}
