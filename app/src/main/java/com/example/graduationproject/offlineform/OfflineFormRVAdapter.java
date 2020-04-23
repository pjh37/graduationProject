package com.example.graduationproject.offlineform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.form.FormActivity;
import com.example.graduationproject.form.FormSaveManager;
import com.example.graduationproject.mainActivityViwePager.SurveyDTO;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class OfflineFormRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    private ArrayList<FormItem> formItem;
    public static final int categoryNumber = 120; //숫자는 아무의미없음

    public OfflineFormRVAdapter(Context context, ArrayList<FormItem> formItem) {
        this.mContext = context;
        this.formItem = formItem;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //        ImageView img_icon; // 뭐를 보여주나?
        TextView _id;
        TextView txtTitle;
        TextView txtTime;
        ImageButton deleteBtn;

        public ViewHolder(View v){
            super(v);
            //            img_icon=(ImageView)v.findViewById(R.id.img_icon);
            _id = (TextView) v.findViewById(R.id._id);
            txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            txtTime = (TextView) v.findViewById(R.id.txtTime);
            deleteBtn = (ImageButton) v.findViewById(R.id.deleteBtn);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editRequest();
                    Log.d("mawang", "OfflineFormRVAdapter ViewHolder  - v clicked");
                }
            });


            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(),"deleteBtn not yet",Toast.LENGTH_SHORT).show();
                    Log.d("mawang", "OfflineFormRVAdapter ViewHolder  - deleteBtn clicked");


                    deleteRequest();
                    // 뒤에 위치해야 index 에러 안나옴
                    formItem.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition()); // 사라지기만 하면 상관없는데
                }
            });
        }



        public void deleteRequest() {
            Log.d("mawang", "OfflineFormRVAdapter ViewHolder deleteRequest - called");
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(mContext.getString(R.string.baseUrl) + "deleteDraftForms/" + formItem.get(getAdapterPosition()).get_id()) // 삭제요청
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("mawang", "OfflineFormRVAdapter ViewHolder deleteRequest onFailure - 에러 = " + e.getMessage());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String res = response.body().string();
                    Log.d("mawang", "OfflineFormRVAdapter ViewHolder deleteRequest onResponse - res = " + res);

                    if (res.equals("delete error")) {
                        Log.d("mawang", "OfflineFormRVAdapter ViewHolder deleteRequest onResponse - 에러 = " + res);
                    } else {
                        Log.d("mawang", "OfflineFormRVAdapter ViewHolder  deleteRequest onResponse - 성공 = " + res);
                    }
                }
            });
        }
        public void editRequest() {
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(mContext.getString(R.string.baseUrl) + "draftload/" + formItem.get(getAdapterPosition()).get_id())
                    .build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("mawang", "UploadedFormEditableActivity editRequest Error = " + e.toString());
                    Log.d("mawang", "UploadedFormEditableActivity editRequest Error = " + e.getMessage());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String res = response.body().string();
                    Log.d("mawang", "OfflineFormRVAdapter ViewHolder editRequest onResponse - res = " + res);

                    if (res.equals("draftload error")) {
                        // node 에서 error 문자열을 보냄
                        Log.d("mawang", "OfflineFormRVAdapter ViewHolder editRequest onResponse - err = " + res);
                    } else {
                        // 받아온 데이터를 토대로 뛰우기
                        Intent intent = new Intent(mContext, FormActivity.class);
                        intent.putExtra("form_id", formItem.get(getAdapterPosition()).get_id());
                        intent.putExtra("json", res); // item datas
                        intent.putExtra("category", categoryNumber);
                        mContext.startActivity(intent);
                    }
                }
            });
        }



    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.offline_form_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(itemView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        FormItem item = formItem.get(position);
//        Glide.with(mContext).load(R.drawable.template).into(((ViewHolder)holder).img_icon);
        ((ViewHolder) holder)._id.setText(String.valueOf(item.get_id()));
        ((ViewHolder) holder).txtTitle.setText(item.getTitle());
        ((ViewHolder) holder).txtTime.setText(item.getTime());
    }
    @Override
    public int getItemCount() {
        return formItem.size();
    }

    public void addItems(ArrayList<FormItem> data) {
        formItem.addAll(data);
        notifyDataSetChanged();
    }

    public void ItemsClear() {
        formItem.clear();
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<FormItem> datas) {
        this.formItem = datas;
        notifyDataSetChanged(); // work
    }

    public ArrayList<FormItem> getItems() {
        return formItem;
    }
}
