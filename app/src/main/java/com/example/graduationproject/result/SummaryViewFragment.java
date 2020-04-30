package com.example.graduationproject.result;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.form.FormComponentVO;
import com.example.graduationproject.form.FormType;
import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.form.FormComponentVO;
import com.example.graduationproject.form.FormType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SummaryViewFragment extends Fragment {
    private View v;
    private Spinner spinner;
    private String url;
    private int form_id;

    private ArrayList<String> spinner_question = new ArrayList<String>(); // 항목 전환용

    private ArrayList<Integer> survey_type = new ArrayList<Integer>();
    private ArrayList<String> survey_question = new ArrayList<String>();

    private ArrayList<ArrayList<String>> choice_option = new ArrayList<ArrayList<String>>();

    private ArrayList<ArrayList<String>> grid_row = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> grid_col = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> grid_checkedValue = new ArrayList<ArrayList<String>>();

    private ArrayList<Integer> linear_beginIndex = new ArrayList<Integer>();
    private ArrayList<Integer> linear_endIndex = new ArrayList<Integer>();

    private IndividualViewDTO individualViewDTO;

    private ArrayList<ArrayList<String>> survey_answer = new ArrayList<ArrayList<String>>();

    String title;
    String description;
    int participate_num;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getString(R.string.baseUrl);

        if (getArguments() != null) {
            form_id = getArguments().getInt("form_id");
        }

        individualViewDTO = new IndividualViewDTO();

        getDataSetting(form_id); // 무조건 먼저 호출되어야함
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_result_summary, container, false);

        spinner = (Spinner) v.findViewById(R.id.summary_spinner);
        spinner_question.add("   ===== 결과 보기 ===== ");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_question);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = (String) parent.getItemAtPosition(position); // 항목의 질문

                if (selected_item != "   ===== 결과 보기 ===== ") {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frag_container_, GraphFragment.newInstance(
                                    selected_item,
                                    survey_question, survey_answer,survey_type,
                                    choice_option,
                                    linear_beginIndex, linear_endIndex,
                                    grid_row,grid_col,grid_checkedValue
                            )).commit();
                } else {
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

    public void getDataSetting(int form_id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestbody = new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                //.addFormDataPart("userEmail", MainActivity.getUserEmail())
                .addFormDataPart("form_id", String.valueOf(form_id))
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url + "load")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "SummaryViewFragment getDataSetting onFailure - catch  폼 전송 실패");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
//                Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - res = "+res);

                res = res.replace("\"[", "[")
                        .replace("]\"", "]")
                        .replace("}\"", "}")
                        .replace("\"{", "{")
                        .replace("\\", "");

//                Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - res 후 = "+res);

                try {

                    JSONArray jsonArray = new JSONArray(res);
                    participate_num = jsonArray.length();

                    HashMap<String, ArrayList<String>> gridParser = new HashMap<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObjResult = jsonObject.getJSONObject("surveyResult"); // 서버로부터 설문결과 받기
                        jsonObjResult.remove("form_id");
                        jsonObjResult.remove("userEmail");

                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse -jsonObjResult = " + jsonObjResult.toString());

                        Iterator<String> keys = jsonObjResult.keys();
//                        ArrayList<String> removeKeys = new ArrayList<>(); // 개발중


                        while (keys.hasNext()) {
                            String key = keys.next();
//                            Log.d("mawang", "SummaryViewFragment getDataSetting onResponse -key값 : " + key);

                            if (key.split("-").length == 2)
                            {
                                String[] grids = key.split("-");
                                String gridKey = grids[0];
                                String gridVal = jsonObjResult.getString(key);
                                int keyInt = Integer.parseInt(gridKey);

//                                removeKeys.add(key);

                                if (individualViewDTO.getResult().containsKey(keyInt)) {

                                    if (gridParser.containsKey(gridKey)) {
                                        gridParser.get(gridKey).add(gridVal);
                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - 중복중복 ");
                                    } else {
                                        ArrayList<String> temp = new ArrayList<>();
                                        temp.add(gridVal);
                                        gridParser.put(gridKey, temp);
                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - 중복x ");
                                    }

                                } else {
                                    if (gridParser.containsKey(gridKey)) {
                                        gridParser.get(gridKey).add(gridVal);
                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - x중복 ");
                                    } else {
                                        ArrayList<String> temp = new ArrayList<>();
                                        temp.add(gridVal);
                                        gridParser.put(gridKey, temp);
                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - xx ");
                                    }

                                }
                            } else
                            {

                                int keyInt = Integer.parseInt(key);
                                Object json = jsonObjResult.get(String.valueOf(keyInt)); // get value by key

                                if (json instanceof JSONArray) { // 체크박스 중복선택일때

                                    if (individualViewDTO.getResult().containsKey(keyInt)) {

                                        JSONArray multiAnswerJson = (JSONArray) json;
                                        ArrayList<String> multiAnswer = new ArrayList<>();
                                        for (int k = 0; k < multiAnswerJson.length(); k++) {
                                            multiAnswer.add(multiAnswerJson.getString(k));
                                        }
                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse JSONArray- multiAnswerJson : " + multiAnswerJson);
                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse JSONArray- multiAnswer : " + multiAnswer);


                                        for (String s:multiAnswer) {
                                            individualViewDTO.getResult().get(keyInt).add(s);
                                        }

                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse JSONArray- individualViewDTO : " + individualViewDTO.getResult());
                                    } else {
                                        JSONArray multiAnswerJson=(JSONArray)json;
                                        ArrayList<String> multiAnswer=new ArrayList<>();
                                        for(int k=0;k<multiAnswerJson.length();k++){
                                            multiAnswer.add(multiAnswerJson.getString(k));
                                        }
                                        individualViewDTO.setResult(Integer.valueOf(key),multiAnswer);
                                    }
                                } else {

                                    // 결과가 여러개이면 동일 키값에서 마지막 결과가 덮어쒸어짐
                                    // 동일키값이라면
                                    if (individualViewDTO.getResult().containsKey(keyInt)) {
                                        String singleAnswer = String.valueOf(json);
                                        individualViewDTO.getResult().get(keyInt).add(singleAnswer);
                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - singleAnswer : " + singleAnswer);
                                    } else {
                                        ArrayList<String> multiAnswer = new ArrayList<>();
                                        multiAnswer.add(String.valueOf(json));
                                        individualViewDTO.setResult(keyInt, multiAnswer);
                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - multiAnswer : " + multiAnswer);
                                    }

                                }

                            }


                        }
//                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - removeKeys : " + removeKeys);
//                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - gridParser : " + gridParser);


//                        for (int j = 0; j < removeKeys.size(); j++) {
//                            jsonObjResult.remove(removeKeys.get(j));
//                        }

                        Iterator<String> iterator = gridParser.keySet().iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            individualViewDTO.setResult(Integer.parseInt(key), gridParser.get(key));
                        }

                    }
                    Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - individualViewDTO HashMap : " + individualViewDTO.getResult());
                    getDataUsing(form_id);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("mawang", "SummaryViewFragment getDataSetting - catch  = " + e.getMessage());
                }
            }
        });
    }

    public void getDataUsing(int form_id) {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url + "individualChart/" + form_id) // 바뀜 !
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "SummaryViewFragment getDataUsing onFailure - catch  폼 전송 실패");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String res = response.body().string();
                    JSONObject jsonObject = new JSONObject(res);

                    title = jsonObject.getString("title");
                    description = jsonObject.getString("description");

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<FormComponentVO>>() {
                    }.getType();
                    ArrayList<FormComponentVO> componentVOS = gson.fromJson(jsonObject.getString("json"), type);


                    for (int i = 0; i < componentVOS.size(); i++) { // 질문수로 loop
                        spinner_question.add(componentVOS.get(i).getQuestion());

                        survey_question.add(componentVOS.get(i).getQuestion());
                        survey_type.add(componentVOS.get(i).getType());

                        choice_option.add(componentVOS.get(i).getAddedOption());

                        linear_beginIndex.add(componentVOS.get(i).getBeginIndex());
                        linear_endIndex.add(componentVOS.get(i).getEndIndex());

                        grid_row.add(componentVOS.get(i).getAddedRowOption());
                        grid_col.add(componentVOS.get(i).getAddedColOption());



                        ArrayList<String> answers = individualViewDTO.getResult().get(i);

                        if (answers != null) {
//                            Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - answers= " + answers);
                            if (componentVOS.get(i).getType() == FormType.RADIOCHOICEGRID || componentVOS.get(i).getType() == FormType.CHECKBOXGRID) { // 임시임
                                ArrayList<String> _checkedValue = new ArrayList<String>();

                                for (int j = 0; j < answers.size(); j++) {
                                    _checkedValue.add(grid_col.get(i).get(Integer.parseInt(answers.get(j))));
                                }

                                grid_checkedValue.add(_checkedValue); // 이게 더편함
                                //survey_answer.add(answers); // 인덱스 맞추는거 불편

                            }

//                            else if (componentVOS.get(i).getType() == FormType.CHECKBOXGRID){} // 추후업데이트
                            else if (componentVOS.get(i).getType() == FormType.RADIOCHOICE || componentVOS.get(i).getType() == FormType.CHECKBOXES) {

                                for (int j = 0; j < answers.size(); j++) {
                                    if (answers.get(j).isEmpty()) { // etc가 존재할 경우 , 보기에 추가
                                        choice_option.get(i).add(answers.get(j + 1));
                                    }
                                }
                                survey_answer.add(answers);
                            } else if (componentVOS.get(i).getType() == FormType.LINEARSCALE)
                            {
                                survey_answer.add(answers);
                            } else if (componentVOS.get(i).getType() == FormType.ADDSECTION || componentVOS.get(i).getType() == FormType.SUBTEXT
                                    || componentVOS.get(i).getType() == FormType.IMAGE)
                            {
                                Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - 이/섹/설 거르자");
                                // 이미지 ,섹션, 설명 거른다.
                            } else { // 단답,장문,시간,날짜 ,드롭다운
                                survey_answer.add(answers);
                            }
                        }
                        else{ // null 일 경우
//                            Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - answers 널 = " + answers);
                            survey_answer.add(answers);
                        }


                    }


//                    Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - survey_question = " + survey_question);
//                    Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - choice_option = " + choice_option);//보기들
//                    Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - survey_answer = " + survey_answer);

//                    Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - grid_row = " +grid_row);
//                    Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - grid_col = " +grid_col);
//                    Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - grid_checkedValue = " + grid_checkedValue);

//                    Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - linear_beginIndex = " + linear_beginIndex);
//                    Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - linear_endIndex = " + linear_endIndex);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
