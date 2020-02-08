package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.graduationproject.mainActivityViwePager.SurveyDTO;
import com.example.graduationproject.result.ResultActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UploadedSurveyRV extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private ArrayList<UploadedSurveyDTO> datas;
    private String userEmail;
    public UploadedSurveyRV(Context context, String userEmail,ArrayList<UploadedSurveyDTO> datas){
        this.mContext=context;
        this.datas=datas;
        this.userEmail=userEmail;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        TextView txtResponse;
        TextView txtTime;
        public ViewHolder(View v){
            super(v);
            txtTitle=(TextView)v.findViewById(R.id.txtTitle);
            txtResponse=(TextView)v.findViewById(R.id.txtResponse);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(mContext,UploadedFormEditableActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("form_id",datas.get(getAdapterPosition()).get_id());
                    intent.putExtra("userEmail",userEmail);
                    mContext.startActivity(intent);
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
        ((ViewHolder)holder).txtResponse.setText(vo.getResponse_cnt()+" response");
        ((ViewHolder)holder).txtTime.setText(vo.getTime());
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
    public void addItem(ArrayList<UploadedSurveyDTO> data){
        datas.addAll(data);
        notifyDataSetChanged();
    }
}
