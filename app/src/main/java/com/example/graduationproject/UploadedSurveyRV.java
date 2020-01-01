package com.example.graduationproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UploadedSurveyRV extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    private ArrayList<UploadedSurveyDTO> datas;
    public UploadedSurveyRV(Context context, ArrayList<UploadedSurveyDTO> datas){
        this.mContext=context;
        this.datas=datas;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        TextView txtResponse;
        Button btnSurveyResult;
        TextView txtTime;
        public ViewHolder(View v){
            super(v);
            txtTitle=(TextView)v.findViewById(R.id.txtTitle);
            txtResponse=(TextView)v.findViewById(R.id.txtResponse);
            btnSurveyResult=(Button)v.findViewById(R.id.btnSurveyResult);
            btnSurveyResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //결과보기로 이동
                }
            });
            txtTime=(TextView)v.findViewById(R.id.txtTime);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.activity_uploaded_form_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(itemView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        UploadedSurveyDTO vo=datas.get(position);
        ((ViewHolder)holder).txtTitle.setText(vo.getTitle());
        ((ViewHolder)holder).txtResponse.setText(vo.getResponseCnt()+" response");
        ((ViewHolder)holder).txtTime.setText(vo.getTime());
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
}
