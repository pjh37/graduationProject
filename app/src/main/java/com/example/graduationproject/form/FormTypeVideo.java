package com.example.graduationproject.form;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.graduationproject.R;
import com.jmedeisis.draglinearlayout.DragLinearLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONObject;

public class FormTypeVideo extends FormAbstract {
    private Context mContext;
    private int mType;
    private LayoutInflater mInflater;
    private View customView;

    private EditText mEditQuestion;
    private ImageButton mCopyView;
    private ImageButton mDeleteView;


    private YouTubePlayerView player_view;
    private Button btnVideoAdd;
    private EditText mEditYtbURL;
    private String ytbVideoId;
    private boolean Isfirst = true;

    public FormTypeVideo(Context context, int type) {
        super(context, type);
        mContext = context;
        this.mType = type;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_video_withview, this, true);

        mEditQuestion = findViewById(R.id.editQuestion);
        mCopyView = findViewById(R.id.copy_view);
        mDeleteView = findViewById(R.id.delete_view);

        btnVideoAdd = findViewById(R.id.btnVideoAdd);
        mEditYtbURL = findViewById(R.id.editURL);

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());
        btnVideoAdd.setOnClickListener(new ClickListener());


        player_view = findViewById(R.id.youtube_player_view);

    }
    public FormTypeVideo(FormCopyFactory fcf) {
        super(fcf.getmContext(), fcf.getmType());
        mContext = fcf.getmContext();
        this.mType = fcf.getmType();

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = mInflater.inflate(R.layout.form_type_video_withview, this, true);

        mEditQuestion = findViewById(R.id.editQuestion);
        mCopyView = findViewById(R.id.copy_view);
        mDeleteView = findViewById(R.id.delete_view);

        btnVideoAdd = findViewById(R.id.btnVideoAdd);
        mEditYtbURL = findViewById(R.id.editURL);

        mEditQuestion.setText(fcf.getEditQuestion_text());
        mEditYtbURL.setText(fcf.getEditYtbURL_text());

        mCopyView.setOnClickListener(new ClickListener());
        mDeleteView.setOnClickListener(new ClickListener());
        btnVideoAdd.setOnClickListener(new ClickListener());

        player_view = findViewById(R.id.youtube_player_view);

    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", mType);
            jsonObject.put("question", mEditQuestion.getText().toString());
            jsonObject.put("ytburl", mEditYtbURL.getText().toString().replace(" ","").replace("\n",""));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void formComponentSetting(FormComponentVO vo) {
        mEditQuestion.setText(vo.getQuestion());
        mEditYtbURL.setText(vo.getYtburl());
    }

    public class ClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == mDeleteView) {
                ViewGroup parentView = (ViewGroup) customView.getParent();
                parentView.removeView(customView);
            } else if (view == mCopyView) {
                DragLinearLayout parentView = (DragLinearLayout) customView.getParent();
                int index = parentView.indexOfChild(customView); // 위치 인덱스

                FormAbstract layout = new FormCopyFactory.Builder(mContext, mType)
                        .Question(mEditQuestion.getText().toString())
                        .YtbURL(mEditYtbURL.getText().toString())
                        .build()
                        .createCopyForm();

                ViewGroup customlayout = (ViewGroup) layout.getChildAt(0);
                ImageView dragHandle = (ImageView) customlayout.getChildAt(0);
                parentView.addDragView(layout, dragHandle, index + 1);

            } else if (view == btnVideoAdd) {

                if (isCorrectYoutubeURL()) {

//처음은 이니셜해줘야 되고,두번째는 주소만 바꾼다.
                    if (Isfirst) {
                        player_view.setVisibility(VISIBLE);

                        player_view.initialize(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                youTubePlayer.cueVideo(ytbVideoId, 0); // manual play

                            }
                        });

                        Isfirst = false;
                    } else {

                        player_view.getYouTubePlayerWhenReady(youTubePlayer -> {
                            youTubePlayer.cueVideo(ytbVideoId, 0);
                        });

                    }


                } else {
                    new AlertDialog.Builder(mContext)
                            .setMessage("YouTube URL 이 올바른지 확인해 주세요.")
                            .setPositiveButton("OK", null)
//                            .setNeutralButton("OK", null)
//                            .setNegativeButton("OK", null)
                            .show();
                }

            }

        }
    }


    public boolean isCorrectYoutubeURL() {
        // 주소 에러 방지
        String URL = mEditYtbURL.getText().toString().replace(" ", "").replace("\n", "");
        Log.d("mawang", "URL : " + URL);


        if (URL.contains("https://youtu.be/")) {
            // 공유주소
            ytbVideoId = URL.substring(URL.lastIndexOf("/") + 1);
            Log.d("mawang", "공유 ytbVideoId : " + ytbVideoId);
            return true;
        } else if (URL.contains("https://www.youtube.com/watch?v=")) {
            // 혹시나해서 웹주소
            ytbVideoId = URL.substring(URL.lastIndexOf("=") + 1);
            Log.d("mawang", "주소창 ytbVideoId : " + ytbVideoId);
            return true;
        } else {
            return false;
        }

    }



}

