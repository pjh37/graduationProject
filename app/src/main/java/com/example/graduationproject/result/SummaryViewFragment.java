package com.example.graduationproject.result;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.graduationproject.BuildConfig;
import com.example.graduationproject.R;
import com.example.graduationproject.form.FormComponentVO;
import com.example.graduationproject.form.FormType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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

    private String title;
    private String description;
    private int participate_num;

    private String participant_email;

    int file_count = 0;

    ImageButton check_btn;
    ImageButton exportToimage_btn;
    ImageButton doc_btn;
    LinearLayout linearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getString(R.string.baseUrl);

        if (getArguments() != null) {
            form_id = getArguments().getInt("form_id");
            Toast.makeText(getContext(), Integer.toString(form_id), Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(), getArguments().getString("userEmail"), Toast.LENGTH_LONG).show();
        }

        individualViewDTO = new IndividualViewDTO();

        getDataSetting(form_id); // 무조건 먼저 호출되어야함
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_result_summary, container, false);

        spinner = (Spinner) v.findViewById(R.id.summary_spinner);

        check_btn = (ImageButton) v.findViewById(R.id.btn_check);
        exportToimage_btn = (ImageButton) v.findViewById(R.id.btn_exportToimage);
        doc_btn = (ImageButton) v.findViewById(R.id.btn_doc);

        check_btn.setOnClickListener(new ClickListener());
        exportToimage_btn.setOnClickListener(new ClickListener());
        doc_btn.setOnClickListener(new ClickListener());

        spinner_question.add("    === 결과 보기 === ");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spinner_question);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = (String) parent.getItemAtPosition(position); // 항목의 질문

                if (selected_item.equals("    === 결과 보기 === ")) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frag_container_, DescriptionFragment.newInstance(title, description, participate_num,
                                    participant_email
                            )).commit();
                } else {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frag_container_, GraphFragment.newInstance(
                                    selected_item,
                                    survey_question, survey_answer,survey_type,
                                    choice_option,
                                    linear_beginIndex, linear_endIndex,
                                    grid_row,grid_col,grid_checkedValue
                            )).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    public void layoutToPartImage() {
        linearLayout = (LinearLayout) v.findViewById(R.id.transfer_layout);

        // 비트맵 작업
        Bitmap bm = Bitmap.createBitmap(linearLayout.getWidth(), linearLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Drawable bgDrawable = linearLayout.getBackground();

        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        linearLayout.draw(canvas);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


//        linearLayout = (LinearLayout) v.findViewById(R.id.transfer_layout);
////        Bitmap bm = Bitmap.createBitmap(linearLayout.getWidth(), linearLayout.getHeight(), Bitmap.Config.ARGB_8888);
//        Bitmap bm = Bitmap.createBitmap(linearLayout.getChildAt(1).getWidth(), linearLayout.getChildAt(1).getHeight(), Bitmap.Config.ARGB_8888);
//
//
////        ScrollView iv = (ScrollView) v.findViewById(R.id.scroll_view);
////        Bitmap bm = Bitmap.createBitmap(iv.getChildAt(0).getWidth(), iv.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
//
//        Canvas canvas = new Canvas(bm);
////        Drawable bgDrawable = linearLayout.getBackground();
//        Drawable bgDrawable = linearLayout.getChildAt(1).getBackground();
////        Drawable bgDrawable = iv.getChildAt(0).getBackground();
//
//        if(bgDrawable != null)
//            bgDrawable.draw(canvas);
//        else
//            canvas.drawColor(Color.WHITE);
//
////        linearLayout.draw(canvas);
//        linearLayout.getChildAt(1).draw(canvas);
////        iv.getChildAt(0).draw(canvas);
//
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);




        String sdPath = Environment.getExternalStorageDirectory() + File.separator + "설문_" + title;

        File f1 = new File(sdPath);
        if(!f1.exists()) { // 해당 설문 항목의 폴더가 없는 경우, 폴더 생성
            f1.mkdirs();
        }

        file_count++;

        try {
            Log.v("파일경로", "sdPath : " + sdPath);
            File f2 = new File(sdPath + File.separator + "image" + file_count + ".jpg");

            if(!f2.exists()) { // 이미지 파일이 존재하지 않을 경우, 이미지 생성
                Toast.makeText(getContext(), "image" + file_count + ".jpg 생성", Toast.LENGTH_SHORT).show();
                f2.createNewFile();

                FileOutputStream fo = new FileOutputStream(f2);
                fo.write(bytes.toByteArray());
            } else { // 이미지 파일이 존재하는 경우, 가장 끝번호 파일+1 의 이미지 생성
                File[] created_file = f1.listFiles();

                Arrays.sort(created_file);

                int last_file = created_file.length - 1;

                String path_str = created_file[last_file].toString().replace(sdPath + File.separator + "image", "");
                int last_num = Integer.parseInt(path_str.replace(".jpg", ""));

                File f3 = new File(sdPath + File.separator + "image" + (last_num+1) + ".jpg");

                f3.createNewFile();

                Toast.makeText(getContext(), "image" + (last_num+1) + ".jpg 생성", Toast.LENGTH_SHORT).show();

                FileOutputStream fo = new FileOutputStream(f3);
                fo.write(bytes.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ImageToFile() {
        try {
            // 설문 항목 폴더에 있는 이미지들을 PDF로 통합해서 생성
            String sdPath = Environment.getExternalStorageDirectory() + File.separator + "설문_" + title;

            File image_file = new File(sdPath);
            File[] to_file = image_file.listFiles();

            String path_str1 = to_file[0].toString().replace(sdPath + File.separator + "image", "");

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(sdPath + File.separator + title + ".pdf"));
            document.open();

            for(int i=0; i<to_file.length; i++) {
                String path_str = to_file[i].toString().replace(sdPath + File.separator + "image", "");

                Image image = Image.getInstance(sdPath + File.separator + "image" + path_str);

                float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin() - 0) / image.getWidth()) * 100;
                image.scalePercent(scaler);
                image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                document.add(image);
            }
            document.close();

            Toast.makeText(getContext(), "PDF 생성 완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fileShare() {
        // PDF 파일을 공유하는 기능
        String sdPath = Environment.getExternalStorageDirectory() + File.separator + "설문_" + title;

        File pdf_file = new File(sdPath + File.separator + title + ".pdf");

        if(!pdf_file.exists()) { // pdf파일이 존재하지 않는 경우 공유 불가능
            Toast.makeText(getContext(), "PDF 파일이 없습니다", Toast.LENGTH_SHORT).show();
        } else { // pdf파일이 존재하는 경우 공유 가능
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID, new File(sdPath + File.separator + title + ".pdf")));
            Intent chooser = Intent.createChooser(shareIntent, "공유");
            getContext().startActivity(chooser);
        }
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_exportToimage:{
                    layoutToPartImage();

                    break;
                }
                case R.id.btn_doc:{
                    ImageToFile();

                    break;
                }
                case R.id.btn_check:{
                    fileShare();

                    break;
                }
            }
        }
    }

    public void getDataSetting(int form_id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestbody = new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                //.addFormDataPart("userEmail", MainActivity.getUserEmail())
                .addFormDataPart("userEmail", getArguments().getString("userEmail"))
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

                res = res.replace("\"[", "[")
                        .replace("]\"", "]")
                        .replace("}\"", "}")
                        .replace("\"{", "{")
                        .replace("\\", "");

                try {

                    JSONArray jsonArray = new JSONArray(res);
                    participate_num = jsonArray.length();
                    Log.d("partnum", res);
                    HashMap<String, ArrayList<String>> gridParser = new HashMap<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject jsonObjResult = jsonObject.getJSONObject("surveyResult"); // 서버로부터 설문결과 받기

                        participant_email = jsonObjResult.getString("userEmail"); // 1개만 필요,이러면 마지막 이메일로 가네
                        jsonObjResult.remove("form_id");
                        jsonObjResult.remove("userEmail");


                        Iterator<String> keys = jsonObjResult.keys();
                        ArrayList<String> removeKeys = new ArrayList<>();


                        while (keys.hasNext()) {
                            String key = keys.next();

                            if (key.split("-").length == 2)
                            {
                                String[] grids = key.split("-");
                                String gridKey = grids[0];
                                String gridVal = jsonObjResult.getString(key);
                                int keyInt = Integer.parseInt(gridKey);

                                removeKeys.add(key);

                                if (individualViewDTO.getResult().containsKey(keyInt)) {

                                    if (gridParser.containsKey(gridKey)) {
                                        gridParser.get(gridKey).add(gridVal);
//                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - 중복중복 ");
                                    } else {
                                        ArrayList<String> temp = new ArrayList<>();
                                        temp.add(gridVal);
                                        gridParser.put(gridKey, temp);
//                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - 중복x ");
                                    }

                                } else {
                                    if (gridParser.containsKey(gridKey)) {
                                        gridParser.get(gridKey).add(gridVal);
//                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - x중복 ");
                                    } else {
                                        ArrayList<String> temp = new ArrayList<>();
                                        temp.add(gridVal);
                                        gridParser.put(gridKey, temp);
//                                        Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - xx ");
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

                                        for (String s:multiAnswer) {
                                            individualViewDTO.getResult().get(keyInt).add(s);
                                        }

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


                        for (int j = 0; j < removeKeys.size(); j++) {
                            jsonObjResult.remove(removeKeys.get(j));
                        }

                        Iterator<String> iterator = gridParser.keySet().iterator();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            individualViewDTO.setResult(Integer.parseInt(key), gridParser.get(key));
                        }

                    }
//                    Log.d("mawang", "SummaryViewFragment getDataSetting onResponse - individualViewDTO HashMap : " + individualViewDTO.getResult());
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


                    if(participate_num != 0){
                        for (int i = 0; i < componentVOS.size(); i++) { // 질문수로 loop

                            if (componentVOS.get(i).getType() == FormType.ADDSECTION || componentVOS.get(i).getType() == FormType.SUBTEXT
                                    || componentVOS.get(i).getType() == FormType.IMAGE|| componentVOS.get(i).getType() == FormType.VIDEO)
                            {
                                // 그래프쪽에 생성 안함
                            }else{
                                if(componentVOS.get(i).getQuestion().isEmpty()){
                                    spinner_question.add("제목"+(i+1));
                                    survey_question.add("제목"+(i+1));
                                }else{
                                    spinner_question.add(componentVOS.get(i).getQuestion());
                                    survey_question.add(componentVOS.get(i).getQuestion());
                                }
                            }

                            survey_type.add(componentVOS.get(i).getType());
                            choice_option.add(componentVOS.get(i).getAddedOption());
                            linear_beginIndex.add(componentVOS.get(i).getBeginIndex());
                            linear_endIndex.add(componentVOS.get(i).getEndIndex());
                            grid_row.add(componentVOS.get(i).getAddedRowOption());
                            grid_col.add(componentVOS.get(i).getAddedColOption());


                            ArrayList<String> answers = individualViewDTO.getResult().get(i);
                            ArrayList<String> _checkedValue = new ArrayList<String>(); // RADIOCHOICEGRID , CHECKBOXGRID 전용
                            survey_answer.add(answers); // 우선 널 넣어주기
                            grid_checkedValue.add(_checkedValue); // 질문과 인덱스 맞추기 위해서임

                            if (answers != null) {

                                if (componentVOS.get(i).getType() == FormType.RADIOCHOICEGRID )
                                {
                                    for (int j = 0; j < answers.size(); j++) {
                                        _checkedValue.add(grid_col.get(i).get(Integer.parseInt(answers.get(j))));
                                    }

                                    grid_checkedValue.set(i,_checkedValue);

                                }
                                else if (componentVOS.get(i).getType() == FormType.CHECKBOXGRID)
                                {

                                    for (int j = 0; j < answers.size(); j++) {

                                        answers.set(j, answers.get(j).replace("[\"", "")
                                                .replace("\",\"", "")
                                                .replace("\"]", ""));

                                        for (int k = 0; k < answers.get(j).length(); k++) {
                                            _checkedValue.add(grid_col.get(i).get(Character.getNumericValue(answers.get(j).charAt(k))));
                                        }
                                        _checkedValue.add("¿"); // 잘안쓰는 특수문자로 해야함
                                        // 'ㄱ' 한자
                                    }

                                    grid_checkedValue.set(i,_checkedValue);

                                }
                                else if (componentVOS.get(i).getType() == FormType.RADIOCHOICE || componentVOS.get(i).getType() == FormType.CHECKBOXES)
                                {

                                    for (int j = 0; j < answers.size(); j++) {
                                        if (answers.get(j).isEmpty() && !choice_option.get(i).contains(answers.get(j + 1))) {
                                            // etc가 존재할 경우 ,etc text를 보기에 추가
                                            // 겹치지 않게 보기에 추가
                                            choice_option.get(i).add(answers.get(j + 1));
                                        }
                                    }
                                    survey_answer.set(i,answers);
                                } else if (componentVOS.get(i).getType() == FormType.LINEARSCALE)
                                {
                                    survey_answer.set(i,answers);
                                } else if (componentVOS.get(i).getType() == FormType.ADDSECTION || componentVOS.get(i).getType() == FormType.SUBTEXT
                                        || componentVOS.get(i).getType() == FormType.IMAGE|| componentVOS.get(i).getType() == FormType.VIDEO)
                                {
                                    Log.d("mawang", "SummaryViewFragment getDataUsing onResponse - 거르자,"+i);

                                } else { // 단답,장문,시간,날짜 ,드롭다운
                                    survey_answer.set(i,answers);
                                }
                            }



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
