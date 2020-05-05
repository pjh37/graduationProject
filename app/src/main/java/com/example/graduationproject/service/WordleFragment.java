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
import com.example.graduationproject.mainActivityViwePager.RequestType;
import com.example.graduationproject.mainActivityViwePager.SurveyDTO;
import com.example.graduationproject.retrofitinterface.RetrofitApi;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WordleFragment extends Fragment {

    private String[] wordCloud;
    private ArrayList<SurveyDTO> datas = new ArrayList<>();
    private ArrayList<String> extractedTitles = new ArrayList<>();
    private KoreanPhraseExtractorApi koreanPhraseExtractor;
    private int pageCount = 1;
    private WebView d3;
    private boolean isFinish = false;
    private boolean isWordReady = false;
    private View root;

    private class AndroidBridge{ // webview - android 통신
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
    public WordleFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // 서버에 등록된 설문조사의 정보를 받아옵니다.
        getSurveyTitles();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isFinish){ }
                if(isFinish){
                    extractedTitles = extractTitleFromSurveyDTO(datas);

                    koreanPhraseExtractor = new KoreanPhraseExtractorApi(extractedTitles);
                    ArrayList<String> str = koreanPhraseExtractor.extractPhrase();

                    wordCloud = new String[str.size()];
                    int i = 0;
                    for(String string : str){
                        wordCloud[i++] = string;
                    }

                    isWordReady = true;
                }
            }
        }).start();

        root = inflater.inflate(R.layout.fragment_wordle, container, false);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // Word Cloud 띄워주는 스레드
        // asset 폴더의 d3.html 참조
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isWordReady){ }
                if(isWordReady){
                    getActivity().runOnUiThread(()->{
                        d3 = (WebView) root.findViewById(R.id.d3_cloud);
                        d3.addJavascriptInterface(new AndroidBridge(), "GetWord");
                        WebSettings ws = d3.getSettings();
                        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                        ws.setJavaScriptEnabled(true);
                        ws.setLoadWithOverviewMode(true);
                        ws.setUseWideViewPort(true);

                        d3.setOnTouchListener(new OnClickWithOnTouchListener(root.getContext(), new OnClickWithOnTouchListener.OnClickListener() {
                            @Override
                            public void onClick() {
                                //Toast.makeText(getContext(),"WebViewClickTest",Toast.LENGTH_SHORT).show();

                            }
                        }));

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
                    });
                }
            }
        }).start();

    }

    // 서버에 등록된 설문의 pageCount 를 어디까지 상한을 둬야할지는 후에 테스트에서 실험해야할 듯.
    // 서버에서 연산을 해주는 방법도 고려해 볼 수 있습니다.
    public void getSurveyTitles(){
        RetrofitApi.getService().getSurveyList("all",pageCount).enqueue(new retrofit2.Callback<ArrayList<SurveyDTO>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<SurveyDTO>> call, retrofit2.Response<ArrayList<SurveyDTO>> response) {
                if(!response.body().isEmpty()){
                    datas.addAll(response.body());
                    //Toast.makeText(getContext(), Integer.toString(datas.size()),Toast.LENGTH_LONG).show();
                    pageCount++;
                    getSurveyTitles();
                }
                else {isFinish = true;}
            }
            @Override
            public void onFailure(retrofit2.Call<ArrayList<SurveyDTO>> call, Throwable t) {Toast.makeText(getContext(), "fail",Toast.LENGTH_LONG).show();}
        });

    }

    public ArrayList<String> extractTitleFromSurveyDTO(ArrayList<SurveyDTO> datas){
        ArrayList<String> arrayList = new ArrayList<>();
        String node;

        for(int position = 0; position  < datas.size(); position++){
            node = datas.get(position).getTitle();
            arrayList.add(node);
        }

        return arrayList;
    }

}
