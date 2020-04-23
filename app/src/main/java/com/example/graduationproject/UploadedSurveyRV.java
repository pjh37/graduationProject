package com.example.graduationproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.form.FormActivity;
import com.example.graduationproject.mainActivityViwePager.MainVPMySurveyFragment;
import com.example.graduationproject.mainActivityViwePager.SurveyDTO;
import com.example.graduationproject.result.ResultActivity;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class UploadedSurveyRV extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private ArrayList<UploadedSurveyDTO> datas;

    Dialog mBottomSheetDialog;
    private OkHttpClient client;
    private int form_id;
    private String SurveyRoomTitle;
    public static final int categoryNumber = 130; // 숫자는 아무의미 없음, 그냥 정한거

    MainVPMySurveyFragment mySurveyFragment;

    public UploadedSurveyRV(Context context, ArrayList<UploadedSurveyDTO> datas) {
        this.mContext = context;
        this.datas = datas;
    }
    public UploadedSurveyRV(Context context, ArrayList<UploadedSurveyDTO> datas,MainVPMySurveyFragment mySurveyFragment) {
        this.mContext = context;
        this.datas = datas;
        this.mySurveyFragment = mySurveyFragment;
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
//                    Intent intent = new Intent(mContext, UploadedFormEditableActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("form_id", datas.get(getAdapterPosition()).get_id());
//                    intent.putExtra("title", datas.get(getAdapterPosition()).getTitle());
//                    mContext.startActivity(intent);

                    client=new OkHttpClient();
                    form_id = datas.get(getAdapterPosition()).get_id();
                    SurveyRoomTitle =datas.get(getAdapterPosition()).getTitle();
                    openBottomSheet();
                }
            });

            btnSurveyResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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


    public void openBottomSheet() {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View view = inflater.inflate(R.layout.activity_uploaded_form, null);

        ImageView imgSurveyWriterPhoto = view.findViewById(R.id.survey_writer_photo);;
        Glide.with(mContext).load(MainActivity.getUserImage()).into(imgSurveyWriterPhoto);
        TextView tvSurveyWriterEmail= view.findViewById(R.id.survey_writer_email);
        tvSurveyWriterEmail.setText(MainActivity.getUserEmail());
        TextView tvSurveyRoomTitle = view.findViewById(R.id.survey_room_title);
        tvSurveyRoomTitle.setText(SurveyRoomTitle);

        mBottomSheetDialog = new Dialog(mContext);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL );
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM );
        mBottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogSheetAnimation;
        mBottomSheetDialog.show();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnResult: {
                        resultRequest();
//                        Toast.makeText(mContext,"btnResult",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.btnShare: {
                        shareRequest();
//                        Toast.makeText(mContext,"btnShare",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.btnEdit: {
                        editRequest();
//                        Toast.makeText(mContext,"btnEdit",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.btnPreview: {
                        previewRequest();
//                        Toast.makeText(mContext,"btnPreview",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.btnDelete: {

                        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                        builder.setTitle("삭제").setMessage("해당 설문을 삭제하시겠습니까?");
                        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteRequest();
                                //mBottomSheetDialog.dismiss();
                                //약간의 시간차를 두기위해 함수안에서 dismiss 호출
                            }
                        });
                        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(mContext,"삭제취소",Toast.LENGTH_SHORT).show();
                                mBottomSheetDialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();

//                        Toast.makeText(mContext,"btnDelete",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        };


        LinearLayout Result = view.findViewById(R.id.btnResult);
        LinearLayout Share = view.findViewById(R.id.btnShare);
        LinearLayout Edit = view.findViewById(R.id.btnEdit);
        LinearLayout Preview = view.findViewById(R.id.btnPreview);
        LinearLayout Delete = view.findViewById(R.id.btnDelete);

        Result.setOnClickListener(onClickListener);
        Share.setOnClickListener(onClickListener);
        Edit.setOnClickListener(onClickListener);
        Preview.setOnClickListener(onClickListener);
        Delete.setOnClickListener(onClickListener);

    }

    public void editRequest(){

        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(mContext.getString(R.string.baseUrl)+"load/"+form_id)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res=response.body().string();
                if(res.equals("load error")){
                    Toast.makeText(mContext,"요청 실패",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(mContext, FormActivity.class);
                    intent.putExtra("form_id",form_id);
                    intent.putExtra("json",res);
                    intent.putExtra("category", categoryNumber);
                    mContext.startActivity(intent);

                    mBottomSheetDialog.dismiss();
                }
            }
        });
    }
    public void deleteRequest(){
        Log.d("mawang", "deleteRequest 실행 - form_id = "+form_id);

        okhttp3.Request request=new okhttp3.Request.Builder()
                .url(mContext.getString(R.string.baseUrl) + "deleteform/" + form_id+"/"+MainActivity.getUserEmail())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(mContext,"요청 실패",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res=response.body().string();
                if(res.equals("delete error")){
                    Toast.makeText(mContext,"요청 실패",Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("mawang", "res = "+res);
                }
            }
        });
        mySurveyFragment.refreshData();
        mBottomSheetDialog.dismiss();
    }
    public void resultRequest(){
        Intent intent =new Intent(mContext, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("form_id",form_id);
        mContext.startActivity(intent);

        mBottomSheetDialog.dismiss();
    }
    public void shareRequest(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,mContext.getString(R.string.baseUrl)+"survey/"+form_id);
        Intent chooser=Intent.createChooser(intent,"공유");
        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(chooser);

        mBottomSheetDialog.dismiss();
    }
    public void previewRequest(){
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getString(R.string.baseUrl)+"survey/"+form_id));
        mContext.startActivity(intent);

        mBottomSheetDialog.dismiss();
    }


}
