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

    public UploadedSurveyRV(Context context, ArrayList<UploadedSurveyDTO> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        TextView txtWriterName;
        TextView txtTime;
        Button btnSurveyResult;
        TextView txtResponse;
        ImageButton btnShare;

        public ViewHolder(View v){
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            txtWriterName = (TextView) v.findViewById(R.id.txtWriterName);
            txtTime = (TextView) v.findViewById(R.id.txtTime);
            btnSurveyResult = v.findViewById(R.id.btnSurveyResult);
            txtResponse = (TextView) v.findViewById(R.id.txtResponse);
            btnShare = v.findViewById(R.id.btnShare);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, UploadedFormEditableActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("form_id", datas.get(getAdapterPosition()).get_id());
                    intent.putExtra("title", datas.get(getAdapterPosition()).getTitle());

                    mContext.startActivity(intent);
                }
            });

            btnSurveyResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(),"btnSurveyResult",Toast.LENGTH_SHORT).show();
//                    Log.d("mawang", "UploadedSurveyRV ViewHolder constructor - btnSurveyResult clicked ");
                    // 작업하자
                    resultRequest();
                }
            });
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(),"btnShare",Toast.LENGTH_SHORT).show();
//                    Log.d("mawang", "UploadedSurveyRV ViewHolder constructor - btnShare clicked ");
                    shareRequest();
                }
            });
        }


        // 이거 static 으로 하면 form_id 가 null 이라 에러남 , 이게 맞음
        public void shareRequest() {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.baseUrl) + "survey/" + datas.get(getAdapterPosition()).get_id()); // 링크

            Intent chooser = Intent.createChooser(intent, "공유"); // 다른앱으로 보내기
            chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(chooser);
        }

        public void resultRequest() {
            Intent intent = new Intent(mContext, ResultActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("form_id", datas.get(getAdapterPosition()).get_id());
            mContext.startActivity(intent);
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
        UploadedSurveyDTO vo = datas.get(position);
        ((ViewHolder) holder).txtTitle.setText(vo.getTitle());
        ((ViewHolder) holder).txtWriterName.setText(MainActivity.getUserName());
        ((ViewHolder) holder).txtTime.setText(vo.getTime());
        ((ViewHolder) holder).txtResponse.setText(vo.getResponse_cnt() + " 참여"); //response
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addItem(ArrayList<UploadedSurveyDTO> data){
        datas.addAll(data);
        notifyDataSetChanged();
    }

    public void addDatas(ArrayList<UploadedSurveyDTO> data) { // 더보기에서 쓰는군
        datas.addAll(data);
        notifyDataSetChanged();
//        Log.d("mawang", "UploadedSurveyRV addItem - called");
    }

    public void datasClear() { // 삭제 즉각 반영
        datas.clear();
        notifyDataSetChanged();
//        Log.d("mawang", "UploadedSurveyRV datasClear - called");
    }

    public void setDatas(ArrayList<UploadedSurveyDTO> datas) {
        this.datas = datas;
        notifyDataSetChanged(); // work
    }
    //public ArrayList<UploadedSurveyDTO> getDatas() {return datas;}





}
