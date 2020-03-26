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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainVPAllSurveyFragment extends Fragment {
    private ProgressBar progressBar;
    private View footer;
    private boolean isFinish;

    private Spinner spinner;
    private RecyclerView surveysRecycleView;
    private SurveyRV surveysAdapter;
    private ArrayList<SurveyDTO> Alldatas;

    private int pageNum;
    private int spinnerIndex;
    private int totalCount = 10; // test

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Alldatas=new ArrayList<>();
        isFinish=false;
        pageNum = 0; //1
        spinnerIndex=0;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.activity_main_vp_all_survey,container,false);
        spinner=(Spinner)rootView.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new ItemSelectListener());
        spinner.setSelection(0);

        footer=rootView.findViewById(R.id.footer);
        progressBar=(ProgressBar)rootView.findViewById(R.id.progress);

        surveysAdapter=new SurveyRV(getContext(),Alldatas);


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
        //getAllSurveyList(RequestType.ALL, pageNum); // 어차피 스피너를 통해 불러오니까 일단 주석
        // 여기서도 호출하면 데이터 중복됨
    }

    public class ItemSelectListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            Alldatas.clear();
            //surveysAdapter=new SurveyRV(getContext(),Alldatas);
            //surveysRecycleView.setAdapter(surveysAdapter);
            pageNum = 0; // 초기화이고 , 1
            spinnerIndex=position;

            Log.v("테스트","onItemSelected pos : "+position);
            switch(position){
                case 0:{ getAllSurveyList(RequestType.ALL,pageNum); break;}
                case 1:{ getAllSurveyList(RequestType.RECENT,pageNum); break;}
                case 2:{ getAllSurveyList(RequestType.RECOMMEND,pageNum); break;}
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { }
    }
    public class ScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            // 완전히 view 가 보여질때의 인덱스이다.
            int firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            int lastVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            //int totalCount = recyclerView.getAdapter().getItemCount(); // 현재 보여지는 view 개수이겠지?
            // 아님, 현재 존재하는 view 개수임 , 많으면 이게 맞음


            if(lastVisiblePosition==totalCount-1){
                footer.setVisibility(View.VISIBLE);
                pageNum++;
                switch(spinnerIndex){
                    case 0:{ getAllSurveyList(RequestType.ALL,pageNum);break; }
                    case 1:{ getAllSurveyList(RequestType.RECENT,pageNum); break;}
                    case 2:{ getAllSurveyList(RequestType.RECOMMEND,pageNum); break;}
                }
            }
            if (firstVisiblePosition == 0 && pageNum >0) {
                pageNum--;
                if(pageNum<0){pageNum=0;}
                Log.d("mawang", "MainVPAllSurveyFragment ScrollListener onScrolled - page down = "+pageNum);
                switch (spinnerIndex) {
                    // 서버통신인데
                    case 0: {
                        getAllSurveyList(RequestType.ALL, pageNum);
                        break;
                    }
                    case 1: {
                        getAllSurveyList(RequestType.RECENT, pageNum);
                        break;
                    }
                    case 2: {
                        getAllSurveyList(RequestType.RECOMMEND, pageNum);
                        break;
                    }
                }
            }
        }
    }
    public void getAllSurveyList(String type,int page){
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
                surveysAdapter.addDatas(response.body());
            }
            @Override
            public void onFailure(retrofit2.Call<ArrayList<SurveyDTO>> call, Throwable t) { }
        });
    }

}
