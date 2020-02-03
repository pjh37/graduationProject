package com.example.graduationproject.service;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.os.Handler;
import android.webkit.JavascriptInterface;


import com.example.graduationproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordleFragment extends Fragment {

    String[] wordCloud = new String[]{ "IT/전자기기", "식품", "의류", "부동산", "뉴스/정치",
            "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop", "Marshmallow",
            "Oreo", "AndroidX", "CheckMate"};

    private class AndroidBridge{
        final public Handler handler = new Handler();

        @JavascriptInterface
        public void getClickedWord(final String arg){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),arg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(),WordleClickedActivity.class);
                    intent.putExtra("Keyword",arg);
                    startActivity(intent);

                }
            });

        }
    }
    public WordleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_wordle, container, false);
        WebView d3 = (WebView) root.findViewById(R.id.d3_cloud);
        d3.addJavascriptInterface(new AndroidBridge(), "GetWord");

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

                d3.evaluateJavascript(sb.toString(), null);

            }
        });

        d3.setOnTouchListener(new OnClickWithOnTouchListener(root.getContext(), new OnClickWithOnTouchListener.OnClickListener() {
            @Override
            public void onClick() {
                //Toast.makeText(getContext(),"WebViewClickTest",Toast.LENGTH_SHORT).show();

            }
        }));
        return root;
    }

}
