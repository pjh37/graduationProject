package com.example.graduationproject.mainActivityViwePager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.UploadedSurveyDTO;
import com.example.graduationproject.UploadedSurveyRV;
import com.example.graduationproject.login.Session;
import com.example.graduationproject.offlineform.FormItem;
import com.example.graduationproject.offlineform.OfflineFormRVAdapter;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainVPMySurveyFragment extends Fragment {
    private static final int SERVER_SURVEY=0;
    private static final int OFFLINE_SURVEY=1;

    RecyclerView offlineSurveyRecycleView;
    RecyclerView responseWaitSurveyRecycleView;
    OfflineFormRVAdapter offlineFormAdapter; //    RecyclerView.Adapter offlineFormAdapter;
    UploadedSurveyRV uploadedSurveyAdapter; //    RecyclerView.Adapter uploadedSurveyAdapter;

//    private RecyclerView.LayoutManager layoutManager;
    private TextView txtMoreOfflineView;
    private TextView txtMoreMySurveyView;

//    private FormSaveManager formSaveManager;
//    public String userEmail;
    private String url;
    private ProgressBar progressBar;
    private boolean isFinish;

    private ArrayList<UploadedSurveyDTO> datas; // summit
    private ArrayList<FormItem> items; // draft


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        isFinish=false;
        //        if (getArguments() != null) { // 여기서 이메일 받는군
//            this.userEmail = getArguments().getString("userEmail");
//        }
        url = getString(R.string.baseUrl);
        datas = new ArrayList<>();
        items = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.activity_main_vp_my_survey,container,false);
        progressBar=(ProgressBar)rootView.findViewById(R.id.progress);
//        layoutManager=new LinearLayoutManager(getActivity());

        txtMoreOfflineView=(TextView)rootView.findViewById(R.id.moreOfflineView);
        txtMoreMySurveyView=(TextView)rootView.findViewById(R.id.moreMySurveyView);
        txtMoreOfflineView.setOnClickListener(new ClickListener());
        txtMoreMySurveyView.setOnClickListener(new ClickListener());

        offlineSurveyRecycleView = (RecyclerView) rootView.findViewById(R.id.offlineSurveyRecycleView);
        offlineSurveyRecycleView.setLayoutManager(new LinearLayoutManager(getContext())); // 이걸 생략하면 화면에 안그려짐 !
        offlineSurveyRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        offlineFormAdapter = new OfflineFormRVAdapter(getContext(), items);
        offlineSurveyRecycleView.setAdapter(offlineFormAdapter);


        responseWaitSurveyRecycleView = (RecyclerView) rootView.findViewById(R.id.responseWaitSurveyRecycleView);
        responseWaitSurveyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        responseWaitSurveyRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        uploadedSurveyAdapter = new UploadedSurveyRV(getContext(), datas);
        responseWaitSurveyRecycleView.setAdapter(uploadedSurveyAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //getResponseWaitSurvey(Session.getUserEmail());
        // 다시 돌아올때마다 해줘야해,그래서 이곳이 아님

        //new LoadTask().execute();
    }

    public class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.moreMySurveyView:{
                    Intent intent =new Intent(getContext(),moreViewActivity.class);
                    intent.putExtra("type",OFFLINE_SURVEY);
//                    intent.putExtra("userEmail",userEmail);
                    startActivity(intent);
                    break;
                }
                case R.id.moreOfflineView:{
                    Intent intent =new Intent(getContext(),moreViewActivity.class);
                    intent.putExtra("type",SERVER_SURVEY);
//                    intent.putExtra("userEmail",userEmail);
                    startActivity(intent);
                    break;
                }
            }
        }
    }

    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
//    public class LoadTask extends AsyncTask<Void,FormItem,Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            //offlineFormAdapter.notifyDataSetChanged();
//            offlineFormAdapter=new OfflineFormRVAdapter(getContext(),formItem,userEmail);
//            offlineSurveyRecycleView.setAdapter(offlineFormAdapter);
//            offlineSurveyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
//            super.onPostExecute(aVoid);
//        }
//
//        @Override
//        protected void onProgressUpdate(FormItem... values) {
//            formItem.add(values[0]);
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            String[] columns=new String[]{"_id","json","time"};
//            Cursor cursor= formSaveManager.query(columns,null,null,null,null,null);
//            if(cursor!=null){
//                while(cursor.moveToNext()){
//                    FormItem item=new FormItem();
//                    item.set_id(cursor.getInt(0));
//                    try{
//                        JSONObject jsonObject=new JSONObject(cursor.getString(1));
//                        item.setTitle(jsonObject.getString("title"));
//                        Log.v("테스트",jsonObject.getString("title"));
//                    }catch (Exception e){e.printStackTrace();}
//                    item.setTime(getTime(cursor.getString(2)));
//                    publishProgress(item);
//                }
//                cursor.close();
//            }
//            return null;
//        }
//    }

    public void getResponseWaitSurvey(){
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isFinish){ }
                if(isFinish){
                    getActivity().runOnUiThread(()->{ progressBar.setVisibility(View.GONE); });
                }

            }
        }).start();

        OkHttpClient client=new OkHttpClient();
        RequestBody requestbody=new MultipartBody.Builder().
                setType(MultipartBody.FORM)
                .addFormDataPart("userEmail", Session.getUserEmail())//userEmail 부분 교체
                .build();
        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(url+"user/forms")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //toastMessage("폼 전송 실패");
                Log.v("테스트","폼 전송 실패");
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //toastMessage("폼 전송 완료");
                //Log.v("테스트","받은 폼 : "+response.body().string());
                isFinish=true;

                String res=response.body().string();

                if (res.length() == 2) { // 아하!
                    Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - 첫방문");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getActivity(), "첫 방문을 환영합니다.", Toast.LENGTH_SHORT).show();
                            // 이 멘트는 디비 이메일 체크해서 말해줘야겠다.
                            Toast.makeText(getActivity(), "초기화 상태입니다.", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    // 수정 success

                    try{
                        JSONArray jsonArray=new JSONArray(res);
                        Gson gson=new Gson();

//                        int len=(jsonArray.length()>3) ? 3:jsonArray.length();
                        for(int i=0;i<jsonArray.length();i++){ // 어차피 스크롤 되니까
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
//                            old_FormDTO formDTO=gson.fromJson(jsonObject.getString("json"),old_FormDTO.class);

                            UploadedSurveyDTO uploadedSurveyDTO=new UploadedSurveyDTO();
                            uploadedSurveyDTO.set_id(jsonObject.getInt("_id"));
//                            uploadedSurveyDTO.setTitle(formDTO.getTitle());
                            uploadedSurveyDTO.setTitle(jsonObject.getString("title"));
                            uploadedSurveyDTO.setResponse_cnt(jsonObject.getInt("response_cnt"));
                            uploadedSurveyDTO.setTime(getTime(jsonObject.getString("time")));

                            datas.add(uploadedSurveyDTO);
                        }
                        Log.v("테스트",datas.size()+"");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadedSurveyAdapter=new UploadedSurveyRV(getContext(),datas);
                                responseWaitSurveyRecycleView.setAdapter(uploadedSurveyAdapter);
                                Toast.makeText(getActivity(), "설문지들을 불러옵니다.", Toast.LENGTH_SHORT).show(); // work
                                uploadedSurveyAdapter.setDatas(datas); // 안에다 집어넣어야 하나보다
                            }
                        });


                    }catch (Exception e){
                        e.printStackTrace();
                        Log.v("테스트","받은 폼 error: "+e.getMessage());
                    }
                }
            }
        });
    }

    public void getDraftSurvey() {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestbody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userEmail", Session.getUserEmail())
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url + "user/draftForms")
                .header("Content-Type", "multipart/form-data")
                .post(requestbody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey - 폼 전송 실패");
            }

            @Override
            // response.body().string() 2번 쓰면 터짐
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string(); //string !
//                Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey - 성공 res  = " + res);
//                Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - res 길이 = " + res.length());

                if (res.length() == 2) {
                    Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey - 첫방문");

                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(res);
//                        Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey - jsonArray = " + jsonArray);
                        //Log.d("mawang", "MainVPMySurveyFragment getResponseWaitSurvey - jsonArray.length = " + jsonArray.length()); // unique 항상 1

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            FormItem formitem = new FormItem();
                            formitem.set_id(jsonObject.getInt("_id"));
                            formitem.setTitle(jsonObject.getString("title"));
                            formitem.setTime(getTime(jsonObject.getString("time")));

                            items.add(formitem);
                        }
//                        Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey onResponse - OFFdatas.size = " + items.size());

                        getActivity().runOnUiThread(new Runnable() {
                            // 살짝늦는군 실행이
                            @Override
                            public void run() {

                                offlineFormAdapter.setItems(items); // 안에다 집어넣어야 하나보다
                            }
                        });

//                        Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey -after items =  " + items);
//                        Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey -after offlineFormAdapter datas =  " + offlineFormAdapter.getItems());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("mawang", "MainVPMySurveyFragment getDraftSurvey - 폼 error: " + e.getMessage());
                    }
                }


            }
        });
    }






    @Override
    public void onStart() {
        super.onStart();

//        Log.d("mawang", "MainVPMySurveyFragment onStart -befo datas =  " + datas);
//        Log.d("mawang", "MainVPMySurveyFragment onStart -befo OFFdatas =  " + items);
        datas.clear();
        items.clear();

//        Log.d("mawang", "MainVPMySurveyFragment onStart -befo uploadedSurveyAdapter datas =  " + uploadedSurveyAdapter.getDatas());
//        Log.d("mawang", "MainVPMySurveyFragment onStart -befo uploadedSurveyAdapter datas =  " + offlineFormAdapter.getItems());
        uploadedSurveyAdapter.datasClear();
        offlineFormAdapter.ItemsClear();

        getResponseWaitSurvey();
        getDraftSurvey(); // work
    }




}
