package com.example.graduationproject.result;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.graduationproject.R;
import com.example.graduationproject.UploadedSurveyDTO;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IndividualViewFragment extends Fragment {
    private RecyclerView individualViewRV;
    private RecyclerView.Adapter  individualViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<IndividualViewDTO> datas=new ArrayList<>();
    private String userEmail;
    private int form_id;
    private String url;
    public IndividualViewFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url=getString(R.string.baseUrl);
        if(getArguments()!=null) {
            userEmail = getArguments().getString("userEmail");
            form_id = getArguments().getInt("form_id");
        }

        Log.v("테스트","args : "+userEmail+"   "+form_id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.activity_result_individual,container,false);
        individualViewRV=(RecyclerView)rootView.findViewById(R.id.recycleView);
        individualViewRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Log.v("테스트","datas 크기 : "+datas.size());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSurveyResult(userEmail,form_id);
    }

    public void getSurveyResult(String userEmail, int form_id){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestbody=new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addFormDataPart("userEmail",userEmail)
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
                res=res.replace ("\\", "")
                        .replace("\"[","[")
                        .replace("]\"","]")
                        .replace("}\"","}")
                        .replace("\"{","{");

                //Log.v("테스트",res);
                try {

                    JSONArray jsonArray=new JSONArray(res);
                    for(int i=0;i<jsonArray.length();i++){
                        IndividualViewDTO individualViewDTO=new IndividualViewDTO();
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        JSONObject jsonObjResult=jsonObject.getJSONObject("surveyResult");

                        String time=jsonObject.getString("time");
                        Log.v("테스트",time);
                        individualViewDTO.setTime(time);

                        for(int j=0;j<jsonObjResult.length()-2;j++){
                            Object json=jsonObjResult.get(String.valueOf(j));

                            if(json instanceof JSONArray){
                                JSONArray multiAnswerJson=(JSONArray)json;
                                ArrayList<String> multiAnswer=new ArrayList<>();
                                for(int k=0;k<multiAnswerJson.length();k++){
                                    multiAnswer.add(multiAnswerJson.getString(k));
                                }
                                individualViewDTO.setResult(j,multiAnswer);
                            }else{
                                ArrayList<String> multiAnswer=new ArrayList<>();
                                multiAnswer.add(String.valueOf(json));
                                individualViewDTO.setResult(j,multiAnswer);
                            }
                        }

                        datas.add(individualViewDTO);

                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            individualViewAdapter=new IndividualViewRV(getContext(),datas,form_id);
                            individualViewRV.setAdapter(individualViewAdapter);
                        }
                    });

                }catch (Exception e){Log.v("테스트",e.getMessage());}

            }
        });
    }
}
