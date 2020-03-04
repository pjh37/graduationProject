package com.example.graduationproject.service;

// 키워드를 추출하는 클래스
// komoran api 를 사용합니다.

import java.util.ArrayList;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import java.util.List;


public class KoreanPhraseExtractorApi {

    private ArrayList<String> gettedTitleList;
    private String madeTitleString = "";
    private ArrayList<String> resultList;

    public KoreanPhraseExtractorApi(ArrayList<String> arrayList){
        gettedTitleList = arrayList;
    }

    private void makeListToString(){

        if(gettedTitleList.isEmpty())
            return;

        for(int i = 0; i < gettedTitleList.size(); i++){
            madeTitleString += gettedTitleList.get(i) + " ";
        }

    }

    public ArrayList<String> extractPhrase(){
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL); // LIGHT : 분류 성능 떨어지지만 성능 이득
        makeListToString();
        String test = madeTitleString;

        KomoranResult komoranResult = komoran.analyze(test);

        resultList = (ArrayList<String>)komoranResult.getMorphesByTags("NP","NNP","NNG","SL");

        return resultList;
    }

}
