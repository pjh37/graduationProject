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
    String get_title;
    String get_description;
    int get_participate;

    TextView set_title;
    TextView set_description;
    TextView set_participate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle getBundle = getArguments();
        if(getBundle != null) {
            get_title = getBundle.getString("title");
            get_description = getBundle.getString("description");
            get_participate = getBundle.getInt("participate");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_description, container, false);

        set_title = (TextView) v.findViewById(R.id.survey_title);
        set_description = (TextView) v.findViewById(R.id.survey_description);
        set_participate = (TextView) v.findViewById(R.id.survey_participate);

        set_title.setText(get_title);
        set_description.setText(get_description);
        set_participate.setText("" + get_participate + "명");

        return v;
    }

    public static DescriptionFragment newInstance(String title, String description, int participate_num) {
        DescriptionFragment df = new DescriptionFragment();
        Bundle putBundle = new Bundle();
        putBundle.putString("title", title);
        putBundle.putString("description", description);
        putBundle.putInt("participate", participate_num);
        df.setArguments(putBundle);

        Log.d("호출테스트", "Description newInstance called");

        return df;
    }
}
