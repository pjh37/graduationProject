package com.example.graduationproject.result;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.form.FormType;

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
        Log.d("mawang", "IndividualResultRV onCreateViewHolder - viewType = " + viewType);


        if(viewType == FormType.ADDSECTION)
        {
            Log.d("mawang", "IndividualResultRV onCreateViewHolder - ADDSECTION ");
            View sectionView = LayoutInflater.from(mContext).inflate(R.layout.activity_individual_result_item_section, parent, false);
            ViewHolder viewHolder1 = new ViewHolder(sectionView);
            return viewHolder1;
        }
        else if(viewType == FormType.SUBTEXT)
        {
            Log.d("mawang", "IndividualResultRV onCreateViewHolder - SUBTEXT ");
            View subtextView = LayoutInflater.from(mContext).inflate(R.layout.activity_individual_result_item_subtext, parent, false);
            ViewHolder viewHolder2 = new ViewHolder(subtextView);

            return viewHolder2;
        }
        else
        {
            Log.d("mawang", "IndividualResultRV onCreateViewHolder - else ");
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.activity_individual_result_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);


            return viewHolder;
        }
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

    public void setDatas(ArrayList<IndividualResultDTO> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        return datas.get(position).getType();
    }


}
