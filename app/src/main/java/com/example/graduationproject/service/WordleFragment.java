package com.example.graduationproject.service;


import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.graduationproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordleFragment extends Fragment {

    String[] wordCloud = new String[]{ "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
            "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow"};

    public WordleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_wordle, container, false);
        WebView d3 = (WebView) root.findViewById(R.id.d3_cloud);

        WebSettings ws = d3.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);

        d3.loadUrl("file:///android_asset/d3.html");
        d3.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                StringBuffer sb = new StringBuffer();
                sb.append("wordCloud([");
                for (int i = 0; i < wordCloud.length; i++) {
                    sb.append("'").append(wordCloud[i]).append("'");
                    if (i < wordCloud.length - 1) {
                        sb.append(",");
                    }
                }
                sb.append("])");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    d3.evaluateJavascript(sb.toString(), null);
                } else {
                    d3.loadUrl("javascript:" + sb.toString());
                }
            }
        });

        return root;
    }

}
