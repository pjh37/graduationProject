package com.example.graduationproject.mainActivityViwePager;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.UploadedFormEditableActivity;

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
        TextView txtResponse;
        TextView txtTime;
        ImageButton btnShare;
        public ViewHolder(View v){
            super(v);
            txtTitle=(TextView)v.findViewById(R.id.txtTitle);
            txtResponse=(TextView)v.findViewById(R.id.txtResponse);
            btnShare=(ImageButton)v.findViewById(R.id.btnShare);
            txtTime=(TextView)v.findViewById(R.id.txtTime);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(mContext, UploadedFormEditableActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("form_id",datas.get(getAdapterPosition()).get_id());
                    mContext.startActivity(intent);
                }
            });
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,mContext.getString(R.string.baseUrl)+"survey/"+datas.get(getAdapterPosition()).get_id());
                    Intent chooser=Intent.createChooser(intent,"공유");
                    chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(chooser);
                }
            });
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
        SurveyDTO vo=datas.get(position);
        ((ViewHolder)holder).txtTitle.setText(vo.getTitle());
        ((ViewHolder)holder).txtResponse.setText(vo.getResponse_cnt()+" response");
        ((ViewHolder)holder).txtTime.setText(getTime(vo.getTime()));
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM월 dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
    public void addItem(ArrayList<SurveyDTO> data){
        datas.addAll(data);
        notifyDataSetChanged();
    }
    public void clear(){
        datas.clear();
        notifyDataSetChanged();
    }
}
