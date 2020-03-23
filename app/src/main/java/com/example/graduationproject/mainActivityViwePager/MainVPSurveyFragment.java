package com.example.graduationproject.mainActivityViwePager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.graduationproject.R;
import com.example.graduationproject.retrofitinterface.RetrofitApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainVPSurveyFragment extends Fragment {
    private Spinner spinner;
    private RecyclerView surveysRecycleView;
    //private RecyclerView.Adapter  surveysAdapter;
    private SurveyRV surveysAdapter;
    private ArrayList<SurveyDTO> datas;
    private String url;
    private ProgressBar progressBar;
    private View footer;
    private boolean isFinish;
    private int pageNum;
    private int spinnerCount;
    public MainVPSurveyFragment(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        datas=new ArrayList<>();
        isFinish=false;
        pageNum=1;
        spinnerCount=0;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.activity_main_vp_survey,container,false);
        spinner=(Spinner)rootView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new ItemSelectListener());
        spinner.setSelection(0);
        footer=rootView.findViewById(R.id.footer);
        surveysAdapter=new SurveyRV(getContext(),datas);
        progressBar=(ProgressBar)rootView.findViewById(R.id.progress);
        surveysRecycleView=(RecyclerView)rootView.findViewById(R.id.allSurveyRecycleView);
        surveysRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        surveysRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),1));
        surveysRecycleView.addOnScrollListener(new ScrollListener());
        surveysRecycleView.setAdapter(surveysAdapter);

        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getSurveyList(RequestType.ALL,pageNum);
    }

    public class ItemSelectListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            datas.clear();
            surveysAdapter=new SurveyRV(getContext(),datas);
            surveysRecycleView.setAdapter(surveysAdapter);
            pageNum=1;
            spinnerCount=position;

            Log.v("테스트","onItemSelected pos : "+position);
            switch(position){
                case 0:{ getSurveyList(RequestType.ALL,pageNum); break;}
                case 1:{ getSurveyList(RequestType.RECENT,pageNum); break;}
                case 2:{ getSurveyList(RequestType.RECOMMEND,pageNum); break;}
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { }
    }
    public class ScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisiblePosition=((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            int totalCount=recyclerView.getAdapter().getItemCount();
            if(lastVisiblePosition==totalCount-1){
                footer.setVisibility(View.VISIBLE);
                pageNum++;
                switch(spinnerCount){
                    case 0:{ getSurveyList(RequestType.ALL,pageNum);break; }
                    case 1:{ getSurveyList(RequestType.RECENT,pageNum); break;}
                    case 2:{ getSurveyList(RequestType.RECOMMEND,pageNum); break;}
                }
            }
        }
    }
    public void getSurveyList(String type,int page){
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
        RetrofitApi.getService().getSurveyList(type,page).enqueue(new retrofit2.Callback<ArrayList<SurveyDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<SurveyDTO>> call, retrofit2.Response<ArrayList<SurveyDTO>> response) {
                isFinish=true;
                footer.setVisibility(View.GONE);
                surveysAdapter.addItem(response.body());
            }
            @Override
            public void onFailure(retrofit2.Call<ArrayList<SurveyDTO>> call, Throwable t) { }
        });
    }
    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
}
