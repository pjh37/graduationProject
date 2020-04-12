package com.example.graduationproject.result;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.graduationproject.R;

public class DescriptionFragment extends Fragment {
    View v;
    String title;
    String description;
    int participateNum;
    String writerEmail;


    TextView tv_title;
    TextView tv_description;
    TextView tv_participate;
    TextView tv_writerEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle getBundle = getArguments();
        if (getBundle != null) {
            title = getBundle.getString("title");
            description = getBundle.getString("description");
            participateNum = getBundle.getInt("participate");
            writerEmail = getBundle.getString("email");
        }

        Log.d("mawang", "DescriptionFragment onCreate - emails = " + writerEmail);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_description, container, false);

        tv_title = (TextView) v.findViewById(R.id.survey_title);
        tv_description = (TextView) v.findViewById(R.id.survey_description);
        tv_participate = (TextView) v.findViewById(R.id.survey_participate);
        tv_writerEmail= (TextView) v.findViewById(R.id.survey_writer);


        tv_title.setText(title);
        tv_description.setText(description);
        tv_participate.setText(participateNum + "명");
        tv_writerEmail.setText(writerEmail);


        return v;
    }

    public static DescriptionFragment newInstance(String title, String description, int participate_num,
                                                  String participant_email
    ) {
        DescriptionFragment df = new DescriptionFragment();
        Bundle putBundle = new Bundle();
        putBundle.putString("title", title);
        putBundle.putString("description", description);
        putBundle.putInt("participate", participate_num);

        putBundle.putString("email", participant_email);

        df.setArguments(putBundle);


        return df;
    }
}
