package com.example.graduationproject.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.UploadedSurveyDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WidgetListRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<UploadedSurveyDTO> datas;
    private ArrayList<Integer>  expectResponse = new ArrayList<>();

    FrameLayout container;
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
    EditText editText;


    public WidgetListRV(Context context, ArrayList<UploadedSurveyDTO> datas){
        this.mContext=context;
        this.datas=datas;
    }

    private AlertDialog.Builder createDialog(Context context){
        this.container = new FrameLayout(context);
        this.editText = new EditText(context);
        params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        editText.setLayoutParams(params);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        editText.setTextColor(Color.WHITE);
        container.addView(editText);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.ConfigDialogStyle);
        dialogBuilder
                .setTitle("목표 응답자 수 설정")
                .setMessage("목표 응답자 수를 입력해주세요")
                .setIcon(R.drawable.ic_settings_24px)
                .setCancelable(false)
                .setView(container)
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });

        return dialogBuilder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        TextView txtResponse;
        TextView txtTime;
        public ViewHolder(View v){
            super(v);
            txtTitle=(TextView)v.findViewById(R.id.widget_Title);
            txtResponse=(TextView)v.findViewById(R.id.widget_Response);
            txtTime=(TextView)v.findViewById(R.id.widget_Time);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = createDialog(mContext);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.setMessage("현재 응답자 수 : " + datas.get(getAdapterPosition()).getResponse_cnt());
                    alertDialog.show();
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int et_getInt = Integer.parseInt(editText.getText().toString());
                            if(editText.getText() != null
                                    && datas.get(getAdapterPosition()).getResponse_cnt() <= et_getInt
                                    && et_getInt >= 0) {
                                expectResponse.set(getAdapterPosition(), et_getInt);
                                Intent expectSetIntent = new Intent();
                                expectSetIntent.setAction(WidgetConfigActivity.SET_EXPECT_RESPONSE);
                                expectSetIntent.putExtra("expectValue", et_getInt);
                                expectSetIntent.putExtra("position", getAdapterPosition());
                                mContext.sendBroadcast(expectSetIntent);
                                alertDialog.dismiss();
                            }
                            else{
                                alertDialog.setMessage("현재 응답자 수보다 크거나 같은 값을 입력하세요");
                            }
                        }
                    });
                }
            });
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.widget_rv_survey_item,parent,false);
        WidgetListRV.ViewHolder viewHolder=new WidgetListRV.ViewHolder(itemView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        UploadedSurveyDTO vo=datas.get(position);
        ((WidgetListRV.ViewHolder)holder).txtTitle.setText(vo.getTitle());
        ((WidgetListRV.ViewHolder)holder).txtResponse.setText(vo.getResponse_cnt()+" response");
        ((WidgetListRV.ViewHolder)holder).txtTime.setText(getTime(vo.getTime()));
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
    public void addItem(ArrayList<UploadedSurveyDTO> data){
        datas.addAll(data);
        for(int i = 0; i<datas.size(); i++){
            expectResponse.add(0);
        }
        notifyDataSetChanged();
    }
    public void clear(){
        datas.clear();
        expectResponse.clear();
        notifyDataSetChanged();
    }
}
