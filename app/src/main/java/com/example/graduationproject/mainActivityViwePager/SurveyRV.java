package com.example.graduationproject.mainActivityViwePager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.graduationproject.MainActivity;
import com.example.graduationproject.R;
import com.example.graduationproject.UploadedFormEditableActivity;
import com.example.graduationproject.result.ResultActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SurveyRV extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private ArrayList<SurveyDTO> datas;

    public SurveyRV(Context context, ArrayList<SurveyDTO> datas){
        this.mContext=context;
        this.datas=datas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        TextView txtWriterName;
        TextView txtTime;
        TextView txtResponse;
        Button resultBtn;
        Button participateBtn;
        ImageButton ibtnShare;

        public ViewHolder(View v){
            super(v);
            txtTitle=(TextView)v.findViewById(R.id.txtTitle);
            txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            txtWriterName = (TextView) v.findViewById(R.id.txtWriterName);
            txtTime = (TextView) v.findViewById(R.id.txtTime);
            txtResponse = (TextView) v.findViewById(R.id.txtResponse);
            resultBtn = v.findViewById(R.id.btnSurveyResult);
            participateBtn = v.findViewById(R.id.btnSurveyParticipate);
            ibtnShare = v.findViewById(R.id.btnShare);

            resultBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(),"resultBtn not yet",Toast.LENGTH_SHORT).show();
                    resultRequest();
                }
            });
            participateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(),"participateBtn not yet",Toast.LENGTH_SHORT).show();
                    previewRequest();
                }
            });
            ibtnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(),"공유 not yet",Toast.LENGTH_SHORT).show();
                    shareRequest();
                }
            });
        }



        public void resultRequest() {
            Intent intent = new Intent(mContext, ResultActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("form_id", datas.get(getAdapterPosition()).get_id());
            mContext.startActivity(intent);
        }
        public void previewRequest() {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getString(R.string.baseUrl) + "survey/" + datas.get(getAdapterPosition()).get_id()));
            mContext.startActivity(intent);
        }
        public void shareRequest() {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.baseUrl) + "survey/" + datas.get(getAdapterPosition()).get_id()); // 링크

            Intent chooser = Intent.createChooser(intent, "공유"); // 다른앱으로 보내기
            chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(chooser);
        }




    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.activity_main_rv_survey_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(itemView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        SurveyDTO vo = datas.get(position);
        ((ViewHolder)holder).txtTitle.setText(vo.getTitle());
        ((ViewHolder) holder).txtWriterName.setText(MainActivity.getUserName());
        ((ViewHolder)holder).txtTime.setText(getTime(vo.getTime()));
        ((ViewHolder) holder).txtResponse.setText(vo.getResponse_cnt() + " 참여"); //response
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
    public String getTime(String str){
        long now;
        Date date;
        if(str != null) {
            now = Long.valueOf(str);
            date = new Date(now);
        }
        else
            date = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
    public void addDatas(ArrayList<SurveyDTO> data)
    {
        datas.addAll(data);
        notifyDataSetChanged();
    }

    public void datasClear(){
        datas.clear();
        notifyDataSetChanged();
    }
    public void setDatas(ArrayList<SurveyDTO> datas) {
        this.datas = datas;
        notifyDataSetChanged(); // work
    }
    public ArrayList<SurveyDTO> getDatas() {return datas;}
}
