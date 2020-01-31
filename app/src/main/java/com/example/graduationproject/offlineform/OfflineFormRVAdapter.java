package com.example.graduationproject.offlineform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfflineFormRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    private String userEmail;
    private ArrayList<FormItem> formItem;
    public OfflineFormRVAdapter(Context context, ArrayList<FormItem> formItem, String userEmail){
        this.mContext=context;
        this.formItem=formItem;
        this.userEmail=userEmail;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView _id;
        ImageView img_icon;
        TextView txtTitle;
        TextView txtTime;
        ImageButton deleteBtn;
        public ViewHolder(View v){
            super(v);
            _id=(TextView)v.findViewById(R.id._id);
            img_icon=(ImageView)v.findViewById(R.id.img_icon);
            txtTitle=(TextView)v.findViewById(R.id.txtTitle);
            txtTime=(TextView)v.findViewById(R.id.txtTime);
            deleteBtn=(ImageButton)v.findViewById(R.id.deleteBtn);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, FormActivity.class);
                    intent.putExtra("form_id",Integer.valueOf(_id.getText().toString()));
                    intent.putExtra("userEmail",userEmail);
                    mContext.startActivity(intent);
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String whereClause="_id=?";
                    String[] whereArgs=new String[]{String.valueOf(_id.getText())};
                    FormSaveManager.getInstance(mContext).delete(whereClause,whereArgs);
                    formItem.remove(getAdapterPosition());
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });

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
        FormItem item=formItem.get(position);
        ((ViewHolder)holder)._id.setText(String.valueOf(item.get_id()));
        Glide.with(mContext).load(R.drawable.template).into(((ViewHolder)holder).img_icon);
        ((ViewHolder)holder).txtTitle.setText(item.getTitle());
        ((ViewHolder)holder).txtTime.setText(item.getTime());
    }
    @Override
    public int getItemCount() {
        return formItem.size();
    }
}
