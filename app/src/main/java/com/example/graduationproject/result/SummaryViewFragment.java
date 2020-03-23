package com.example.graduationproject.result;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.form.FormComponentVO;
import com.example.graduationproject.form.FormDTO;
import com.example.graduationproject.form.FormType;
import com.example.graduationproject.login.Session;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SummaryViewFragment extends Fragment {
    private View v;
    private Spinner spinner;
    private String all;
    private String url;
    private String userEmail;
    private int form_id;
    private FormDTO formDTO=null;

    private ArrayList<String> spinner_question = new ArrayList<String>();
    private ArrayList<String> survey_question = new ArrayList<String>();
    private ArrayList<ArrayList<String>> choice_option = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> grid_row = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> grid_col = new ArrayList<ArrayList<String>>();
    private ArrayList<String> survey_answer = new ArrayList<String>();
    private ArrayList<String> survey_qna = new ArrayList<String>();
    private ArrayList<Integer> survey_type = new ArrayList<Integer>();
    private ArrayList<Integer> grid_begin = new ArrayList<Integer>();
    private ArrayList<Integer> grid_end = new ArrayList<Integer>();
    private ArrayList<String> grid_rowcol = new ArrayList<String>();

    private ArrayList<IndividualViewDTO> datas1=new ArrayList<>();
    private IndividualViewDTO individualViewDTO;

    String title;
    String description;
    int participate_num;
    int range;

    public SummaryViewFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url=getString(R.string.baseUrl);

        if(getArguments()!=null) {
            userEmail = getArguments().getString("userEmail");
            form_id = getArguments().getInt("form_id");
            individualViewDTO = (IndividualViewDTO) getArguments().getSerializable("resultData");
        }

        Log.v("유저이메일", "userEmail : " + userEmail);

        getDataSetting(userEmail, form_id);
        getDataUsing(form_id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_result_summary, container, false);

        spinner = (Spinner)v.findViewById(R.id.summary_spinner);
        spinner_question.add("   ===== 결과 보기 ===== ");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_question);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = (String) parent.getItemAtPosition(position);

                if(selected_item != "   ===== 결과 보기 ===== ") {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frag_container_, GraphFragment.newInstance(selected_item, participate_num, survey_question, survey_answer, survey_type, choice_option,grid_begin, grid_end, grid_rowcol, survey_qna)).commit();
                }
                else {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frag_container_, DescriptionFragment.newInstance(title, description, participate_num)).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    public void getDataSetting(String userEmail, int form_id){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestbody=new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addFormDataPart("userEmail", Session.getUserEmail())
                .addFormDataPart("form_id",String.valueOf(form_id))
                .build();
        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(url+"load")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.v("테스트","폼 전송 실패");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res=response.body().string();

                Log.v("테스트",res);

                res=res.replace ("\\", "")
                        .replace("\"[","[")
                        .replace("]\"","]")
                        .replace("}\"","}")
                        .replace("\"{","{")
                        .replace("/","");   // 시간,날짜 위해 추가한 행

                Log.v("테스트",res);
                try {

                    JSONArray jsonArray = new JSONArray(res);
                    participate_num = jsonArray.length();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        IndividualViewDTO individualViewDTO = new IndividualViewDTO();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObjResult = jsonObject.getJSONObject("surveyResult");
                        jsonObjResult.remove("form_id");
                        jsonObjResult.remove("userEmail");

                        String time = jsonObject.getString("time");

                        Log.v("테스트", time);
                        Log.v("테스트", jsonObjResult.toString());

                        Iterator<String> keys = jsonObjResult.keys();
                        HashMap<String, ArrayList<String>> gridParser = new HashMap<>();
                        ArrayList<String> removeKeys = new ArrayList<>();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            Log.v("테스트", "key값 : " + key);
                            if (key.split("-").length == 2) {
                                String[] grids = key.split("-");
                                String gridKey = grids[0];
                                String gridVal = jsonObjResult.getString(key);
                                Log.v("테스트", "gridKey : " + gridKey + "  gridVal :" + gridVal + " key : " + key);

                                removeKeys.add(key);

                                if (gridParser.containsKey(gridKey)) {
                                    gridParser.get(gridKey).add(gridVal);
                                } else {
                                    ArrayList<String> temp = new ArrayList<>();
                                    temp.add(gridVal);
                                    gridParser.put(gridKey, temp);
                                }
                            }
                        }
                        for (int j = 0; j < removeKeys.size(); j++) {
                            jsonObjResult.remove(removeKeys.get(j));
                        }
                        Log.v("테스트", jsonObjResult.toString());

                        Iterator<String> iterator = gridParser.keySet().iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            individualViewDTO.setResult(Integer.valueOf(key), gridParser.get(key));
                        }
                        individualViewDTO.setIndex(i + 1);
                        individualViewDTO.setTime(time);

                        Log.v("테스트", "jsonObjResult.keys() : " + jsonObjResult.keys());

                        ///수정중------------------------------------------------
                        keys=jsonObjResult.keys();
                        Log.v("테스트", "keys : " + keys);

                        while(keys.hasNext()){
                            String key=keys.next();
                            Object json=jsonObjResult.get(key);

                            Log.v("테스트", "key : " + key);
                            Log.v("테스트", "json : " + json);

                            if(json instanceof JSONArray){
                                JSONArray multiAnswerJson=(JSONArray)json;
                                ArrayList<String> multiAnswer=new ArrayList<>();
                                for(int k=0;k<multiAnswerJson.length();k++){
                                    multiAnswer.add(multiAnswerJson.getString(k));
                                }
                                individualViewDTO.setResult(Integer.valueOf(key),multiAnswer);
                            }else{
                                ArrayList<String> multiAnswer=new ArrayList<>();
                                multiAnswer.add(String.valueOf(json));
                                individualViewDTO.setResult(Integer.valueOf(key),multiAnswer);
                            }
                        }

                        range = jsonArray.length();

                        datas1.add(individualViewDTO);
                    }
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            individualViewAdapter=new IndividualViewRV(getContext(),datas1,form_id);
//                            individualViewRV.setAdapter(individualViewAdapter);
//                        }
//                    });
                }catch (Exception e){
                    e.printStackTrace();
                    Log.v("테스트",e.getMessage());
                }
            }
        });
    }

    public void getDataUsing(int form_id){
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

                    title = jsonObject.getString("title");
                    description = jsonObject.getString("description");

                    Log.v("테스트", "" + jsonObject);

                    Gson gson=new Gson();
                    formDTO=gson.fromJson(jsonObject.toString(), FormDTO.class);

                    ArrayList<FormComponentVO> componentVOS=formDTO.getFormComponents();

                    for(int i=0;i<componentVOS.size();i++) {
                        spinner_question.add(componentVOS.get(i).getQuestion());
                        grid_begin.add(componentVOS.get(i).getBegin());
                        grid_end.add(componentVOS.get(i).getEnd());
                        grid_row.add(componentVOS.get(i).getAddedRowOption());
                        grid_col.add(componentVOS.get(i).getAddedColOption());

                        for(int a=0; a<grid_row.size(); a++) {
                            if(grid_row.get(a) != null) {
                                for(int b=0; b<grid_row.get(a).size(); b++) {
                                    Log.v("테스트", "로우 : (" + b + ") " + grid_row.get(a).get(b));
                                    Log.v("테스트", "컬럼 : (" + b + ") " + grid_col.get(a).get(b));

                                    grid_rowcol.add(grid_col.get(a).get(b));
                                }
                            }
                        }

                        IndividualResultDTO individualResultDTO = new IndividualResultDTO();

                        for (int p = 0; p < range; p++) {
                            ArrayList<String> answers = datas1.get(p).getResult().get(i);

                            String answer = "";
                            if (componentVOS.get(i).getType() == 6 || componentVOS.get(i).getType() == 7) {
                                for (int j = 0; j < answers.size(); j++) {
                                    answer += (String.valueOf(j + 1) + ". " + componentVOS.get(i).getAddedColOption().get(Integer.valueOf(answers.get(j))) + " ");
                                }
//
                            } else {
                                for (int j = 0; j < answers.size(); j++) {
                                    answer += answers.get(j);
                                }
                            }
                            Log.v("테스트", "answer : " + answer);

                            individualResultDTO.setQuestion(componentVOS.get(i).getQuestion());
                            individualResultDTO.setAnswer(answer);
                            Log.v("테스트", componentVOS.get(i).getQuestion() + "  " + answer);

                            survey_question.add(componentVOS.get(i).getQuestion());
                            survey_answer.add(answer);
                            survey_type.add(componentVOS.get(i).getType());
                            choice_option.add(componentVOS.get(i).getAddedOption());
                            survey_qna.add(componentVOS.get(i).getQuestion() + " " + answer);
                        }
                    }

//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            individualResultAdapter=new IndividualResultRV(getContext(),datas2);
//                            Log.v("테스트테스트", "9");
//                            individualResultRV.setAdapter(individualResultAdapter);
//                            Log.v("테스트테스트", "10");
//                        }
//                    });

                }catch (Exception e){e.printStackTrace();}
            }
        });
    }
}