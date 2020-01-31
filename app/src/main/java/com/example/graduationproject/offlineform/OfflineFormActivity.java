package com.example.graduationproject.offlineform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OfflineFormActivity extends AppCompatActivity {
    RecyclerView offlineForm;
    RecyclerView.Adapter  offlineFormAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FormItem> formItem;
    private FormSaveManager formSaveManager;
    public String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_form);
        Intent intent=getIntent();
        userEmail=intent.getStringExtra("userEmail");
        formSaveManager=FormSaveManager.getInstance(this);
        formItem=new ArrayList<>();
        offlineForm=(RecyclerView)findViewById(R.id.recycleView);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        offlineForm.addItemDecoration(new DividerItemDecoration(this,1));
        new LoadTask().execute();
    }

    public String getTime(String str){
        long now=Long.valueOf(str);
        Date date=new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = simpleDate.format(date);
        return time;
    }
    public class LoadTask extends AsyncTask<Void,FormItem,Void>{
        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //offlineFormAdapter.notifyDataSetChanged();
            offlineFormAdapter=new OfflineFormAdapter(getApplicationContext(),formItem);
            offlineForm.setAdapter(offlineFormAdapter);
            offlineForm.setLayoutManager(layoutManager);
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(FormItem... values) {
            formItem.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String[] columns=new String[]{"_id","json","time"};
            Cursor cursor= formSaveManager.query(columns,null,null,null,null,null);
            if(cursor!=null){
                while(cursor.moveToNext()){
                    FormItem item=new FormItem();
                    item.set_id(cursor.getInt(0));
                    try{
                        JSONObject jsonObject=new JSONObject(cursor.getString(1));
                        item.setTitle(jsonObject.getString("title"));
                        Log.v("테스트",jsonObject.getString("title"));
                    }catch (Exception e){e.printStackTrace();}
                    item.setTime(getTime(cursor.getString(2)));
                    publishProgress(item);
                }
                cursor.close();
            }
            return null;
        }
    }

    public class OfflineFormAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        Context mContext;
        private ArrayList<FormItem> formItem;
        public OfflineFormAdapter(Context context, ArrayList<FormItem> formItem){
            this.mContext=context;
            this.formItem=formItem;
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
                        Intent intent=new Intent(getApplicationContext(), FormActivity.class);
                        intent.putExtra("form_id",Integer.valueOf(_id.getText().toString()));
                        intent.putExtra("userEmail",userEmail);
                        startActivity(intent);
                    }
                });
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String whereClause="_id=?";
                        String[] whereArgs=new String[]{String.valueOf(_id.getText())};
                        FormSaveManager.getInstance(OfflineFormActivity.this).delete(whereClause,whereArgs);
                        formItem.remove(getAdapterPosition());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                offlineFormAdapter.notifyDataSetChanged();
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
}
