package com.example.graduationproject.result;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IndividualResultRV extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private ArrayList<IndividualResultDTO> datas;
    public IndividualResultRV(Context context,ArrayList<IndividualResultDTO> datas){
        this.mContext=context;
        this.datas=datas;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtQuestion;
        TextView txtAnswer;
        public ViewHolder(View v){
            super(v);
            txtQuestion=(TextView)v.findViewById(R.id.txtQuestion);
            txtAnswer=(TextView)v.findViewById(R.id.txtAnswer);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.activity_individual_result_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(itemView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        IndividualResultDTO vo=datas.get(position);
        ((ViewHolder)holder).txtQuestion.setText(vo.getQuestion());
        ((ViewHolder)holder).txtAnswer.setText(vo.getAnswer());

    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
}
