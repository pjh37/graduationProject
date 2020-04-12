package com.example.graduationproject.result;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.graduationproject.R;
import com.example.graduationproject.form.FormType;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphFragment extends Fragment {
    PieChart pieChart;
    BarChart barChart;
    TableLayout tl;

    private String selectedQuestion;
    private ArrayList<Integer> type;
    private ArrayList<String> question;
    private ArrayList<ArrayList<String>> answer;

    private ArrayList<ArrayList<String>> optionLabels; // 객관식,체크박스,드롭다운 보기들

    private ArrayList<Integer> lineargridbeginIndex;
    private ArrayList<Integer> lineargridEndIndex;


    private ArrayList<ArrayList<String>> grid_rows;
    private ArrayList<ArrayList<String>> grid_cols;
    private ArrayList<ArrayList<String>> grid_checkedValues;

    View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle getBundle = getArguments();

        if (getBundle != null) {
            selectedQuestion = getBundle.getString("selected_question");
            type = getBundle.getIntegerArrayList("type");
            question = getBundle.getStringArrayList("question");
            answer = (ArrayList<ArrayList<String>>) getBundle.getSerializable("answer");

            optionLabels = (ArrayList<ArrayList<String>>) getBundle.getSerializable("choice_option");
            lineargridbeginIndex = getBundle.getIntegerArrayList("grid_beginIndex");
            lineargridEndIndex = getBundle.getIntegerArrayList("grid_endIndex");


            grid_rows = (ArrayList<ArrayList<String>>) getBundle.getSerializable("grid_rows");
            grid_cols = (ArrayList<ArrayList<String>>) getBundle.getSerializable("grid_cols");
            grid_checkedValues = (ArrayList<ArrayList<String>>) getBundle.getSerializable("grid_checkedValue");
        }

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_graph, container, false);

        tl = (TableLayout) v.findViewById(R.id.table_layout);
        pieChart = (PieChart) v.findViewById(R.id.piechart);
        barChart = (BarChart) v.findViewById(R.id.barchart);

//        Log.d("mawang", "GraphFragment onCreateView - selectedQuestion = " + selectedQuestion);
//        Log.d("mawang", "GraphFragment onCreateView - question = " + question);

//        int check_count = 0;

        for (int i = 0; i < question.size(); i++) { // 항목개수만큼의 loop
            if (question.get(i).equalsIgnoreCase(selectedQuestion)) { // 선택한 질문만 보이도록

                if (type.get(i) == FormType.RADIOCHOICE || type.get(i) == FormType.CHECKBOXES || type.get(i) == FormType.DROPDOWN)
                {
//                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - answer = " + answer);
//                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - optionLabels = " + optionLabels);
//                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - answer.size() = " + answer.size());
//                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - answer.isEmpty() = " + answer.isEmpty());

                    ArrayList<Integer> optionResults = new ArrayList<Integer>();

                    if (answer != null) { // null check
                        for (int k = 0; k < optionLabels.get(i).size(); k++) {
                            int find_count = 0;

                            if (answer.get(i) != null) { // 이중 null check
                                for (int j = 0; j < answer.get(i).size(); j++) {

                                    if (optionLabels.get(i).get(k).equalsIgnoreCase(answer.get(i).get(j))) {
//                                        check_count++; // 선택한 전체 항목 수 카운트
                                        find_count++; // 해당 단어를 찾은 경우
//                                        Log.d("mawang", "GraphFragment onCreateView CHECKBOXES -일치! 답 = " + optionLabels.get(i).get(k));
                                    }
                                }
                            }

                            optionResults.add(find_count);
                        }


                    }

//                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - optionResults = " + optionResults);

                    tl.setVisibility(View.VISIBLE);
                    TextView textView[][] = new TextView[optionLabels.get(i).size()][3];
                    setTextView_defaultLayout(textView, optionLabels.get(i).size(), 3); // textView 세팅

                    int replyTotalNum = 0;
                    for (int n : optionResults) {
                        replyTotalNum += n;
                    }
//                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - replyTotalNum = " + replyTotalNum);

                    for (int k = 0; k < optionLabels.get(i).size(); k++) {
                        TableRow tr = new TableRow(getContext());
                        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        for (int l = 0; l < 3; l++) {
                            if (l == 0) {
                                textView[k][l].setText("" + (k + 1));
                                tr.addView(textView[k][l]);
                            } else if (l == 1) {
                                textView[k][l].setText(optionLabels.get(i).get(k));
                                tr.addView(textView[k][l]);
                            } else {
//                                double calc_resrate = (double) optionResults.get(k) / check_count;
//                                textView[k][l].setText("" + optionResults.get(k) + " (" + String.format("%.1f", calc_resrate) + "%)");
                                textView[k][l].setText("" + optionResults.get(k));
                                tr.addView(textView[k][l]);
                            }
                            if (tr.getParent() != null) {
                                ((ViewGroup) tr.getParent()).removeView(tr);
                            }
                        }

                        tl.addView(tr);
                    }

                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                    List<BarEntry> barentries = new ArrayList<>();
                    ArrayList<String> xasis_name = new ArrayList<String>();

                    for (int p = 0; p < optionLabels.get(i).size(); p++) {
                        if (optionResults.get(p) != 0) { //훨씬 깔끔
                            yValues.add(new PieEntry(optionResults.get(p), optionLabels.get(i).get(p)));
                            barentries.add(new BarEntry(p, optionResults.get(p)));
                        }

                    }

                    pieChart.setVisibility(View.VISIBLE);
                    pieChart.setUsePercentValues(true); // % value 사용
                    pieChart.setExtraOffsets(5, 10, 5, 5);
                    pieChart.setDragDecelerationFrictionCoef(0.95f); // 파이차트 드래그 멈추는 속도

                    pieChart.setDrawHoleEnabled(false); // center empty
//                    pieChart.setHoleColor(Color.WHITE);
//                    pieChart.setTransparentCircleRadius(61f);

                    pieChart.setEntryLabelTextSize(20);
//                    pieChart.animateY(1000, EaseInOutCubic); // no need

                    Description description = new Description();
                    description.setText("응답수 :" + replyTotalNum); //라벨
                    description.setTextSize(14);
                    pieChart.setDescription(description);
//                        pieChart.getDescription().setEnabled(false);


                    PieDataSet piedataSet = new PieDataSet(yValues, "");
                    piedataSet.setSliceSpace(3f);
                    piedataSet.setSelectionShift(5f);
                    piedataSet.setFormSize(15);
                    piedataSet.setValueTextSize(15);
                    piedataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    PieData piedata = new PieData(piedataSet);
                    piedata.setValueTextColor(Color.YELLOW);
                    piedata.setValueTextSize(15);

                    piedata.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
//                            Log.d("mawang", "객쳌드 piedata - value= " + value);
                            String percent = String.format("%.1f", value);
                            int index = percent.indexOf(".");
//                            Log.d("mawang", "객쳌드 piedata - index= " + index);
                            if(percent.charAt(index+1)=='0'){
                                // 소수점이하 제거
                                return percent.substring(0,index)+"%";
                            }else{
                                return percent+"%";
                            }

                        }
                    });


                    pieChart.setData(piedata);


                    for (int q = 0; q < optionLabels.get(i).size(); q++) {
                        xasis_name.add(optionLabels.get(i).get(q));
                    }
//                    Log.d("mawang", "객쳌드 - xasis_name = " + xasis_name);
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(15);
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new ValueFormatter() {
                        //                        int crazy;
                        @Override
                        public String getFormattedValue(float value) {
//                            crazy++;
                            int index = (int) value;
//                            Log.d("mawang", "객쳌드 -xAxis  float value/index/crazy = " + value+"/"+index+"/"+crazy );

                            if (index < 0 || index >= xasis_name.size()) {
                                return "";
//                                return "L" + index;
                            } else {
                                return xasis_name.get(index);
                            }
                        }
                    });
                    YAxis yAxis = barChart.getAxisLeft();
                    yAxis.setDrawGridLines(true);
                    yAxis.setGranularity(1); // interval 1
                    yAxis.setAxisMinimum(0f); // must
                    yAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) value);
                        }
                    });


                    barChart.getAxisRight().setEnabled(false); // 오른쪽 축 제거


                    barChart.setVisibility(View.VISIBLE);
                    barChart.setTouchEnabled(false); // 막아야 과도한 호출이 안생김..

                    barChart.getLegend().setEnabled(false); // label hide
                    barChart.setExtraOffsets(5, 5, 5, 10);
                    BarDataSet bardataset = new BarDataSet(barentries, "");

                    bardataset.setValueTextSize(15);
                    bardataset.setFormSize(15);
                    bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

                    BarData bardata = new BarData(bardataset);
//                    bardata.setBarWidth(0.9f); // 0.85 default
                    bardata.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) { // value는 해당 데이터 표시값
                            return String.valueOf((int) value);
                        }
                    });
                    bardata.setValueTextColor(Color.BLACK);
                    bardata.setValueTextSize(15);

                    barChart.setData(bardata);
                    barChart.setFitBars(true);
                    barChart.invalidate();

                    Description description2 = new Description();
                    description2.setText("응답수 :" + replyTotalNum); //라벨
                    description2.setTextSize(14);
                    //description2.setPosition(0.5f,0.5f); // won't work in bar chart
                    barChart.setDescription(description2);

//                        barChart.getDescription().setEnabled(false);



                } else if (type.get(i) == FormType.LINEARSCALE)
                {
//                    Log.d("mawang", "GraphFragment onCreateView LINEARSCALE - answer = " + answer);

                    int index = question.indexOf(selectedQuestion);
                    int begin = lineargridbeginIndex.get(index);
                    int end = lineargridEndIndex.get(index);

//                    Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - index = " + index);
//                    Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - begin = " + begin);
//                    Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - end = " + end);


                    int arr[] = new int[11]; //0-10까지만 있으면 됨

                    if (answer != null) { // null check
                        if (answer.get(i) != null) { // 이중 null check
                            for (int j = 0; j < answer.get(i).size(); j++) {
                                ArrayList<Integer> _res = new ArrayList<Integer>(3);
                                if (begin == 1) {
                                    _res.add(0, -1); // 1부터 add 하면 index out of bound err
                                }

                                for (int h = begin; h < end + 1; h++) {
                                    int find_count = 0;

                                    if (answer.get(i).get(j).equalsIgnoreCase("" + h)) {
//                                        check_count++; // 선택한 전체 항목 수 카운트
                                        find_count++; // 해당 단어를 찾은 경우
//                                        Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - 일치 = " + h);
                                    }
                                    _res.add(h, find_count);
                                }

                                for (int p = begin; p < end + 1; p++) {
                                    arr[p] += _res.get(p);
                                }
//                                Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - id/_res = " + j + "/" + _res);
                            }
                        }
                    }



                    tl.setVisibility(View.VISIBLE);
                    TextView textView[][] = new TextView[11][3]; // 0~10 까지 있을때 최대 경우
                    setTextView_defaultLayout(textView, end + 1, 3); // 세팅

                    int replyTotalNum = 0;
                    for (int n : arr) {
                        replyTotalNum += n;
                    }

                    for (int k = begin; k < end + 1; k++) {
                        TableRow tr = new TableRow(getContext());
                        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        for (int l = 0; l < 3; l++) {
                            if (l == 0) {
                                textView[k][l].setText("" + k);
                                tr.addView(textView[k][l]);
                            } else if (l == 1) {
                                textView[k][l].setText("");
                                tr.addView(textView[k][l]);
                            } else {
//                                double calc_resrate = (double) arr[k] / check_count;
//                                textView[k][l].setText("" + arr[k] + " (" + String.format("%.1f", calc_resrate) + "%)");
                                textView[k][l].setText("" + arr[k]);
                                tr.addView(textView[k][l]);
                            }
                            if (tr.getParent() != null) {
                                ((ViewGroup) tr.getParent()).removeView(tr);
                            }

                            tl.addView(tr);

                        }
                    }


                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                    List<BarEntry> barentries = new ArrayList<>();
                    ArrayList<String> xasis_name = new ArrayList<String>();

                    for (int p = begin; p <= end; p++) {
                        if (arr[p] != 0) {
                            if (begin == 0) {
                                yValues.add(new PieEntry(arr[p], "" + p));
                            } else if (begin == 1) {
                                yValues.add(new PieEntry(arr[p], "" + p));
                            }
                        } // 체크된 데이터만 표시
                        if (begin == 0) {
                            barentries.add(new BarEntry(p, arr[p]));
                        } else if (begin == 1) {
                            barentries.add(new BarEntry(p - 1, arr[p]));
                        }

                    }


                    pieChart.setVisibility(View.VISIBLE);
                    pieChart.setUsePercentValues(true);
                    pieChart.setExtraOffsets(5, 10, 5, 5);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    pieChart.setDrawHoleEnabled(false); // 가운데 빈원생성
                    //pieChart.setHoleColor(Color.WHITE); // 빈원 색깔
                    //pieChart.setTransparentCircleRadius(61f); // 빈원그림자
                    pieChart.setEntryLabelTextSize(20);

                    Description description = new Description();
                    description.setText("응답수 :" + replyTotalNum); //라벨
                    description.setTextSize(14);
                    pieChart.setDescription(description);
//                        pieChart.getDescription().setEnabled(false);

                    PieDataSet dataSet = new PieDataSet(yValues, "");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setFormSize(15);
                    dataSet.setValueTextSize(15);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    PieData piedata = new PieData(dataSet);
                    piedata.setValueTextColor(Color.YELLOW); // 노란색이 best
                    piedata.setValueTextSize(15);
                    piedata.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
//                            Log.d("mawang", "직단 piedata - value= " + value);
                            String percent = String.format("%.1f", value);
                            int index = percent.indexOf(".");
//                            Log.d("mawang", "직단 piedata - index= " + index);
                            if(percent.charAt(index+1)=='0'){
                                // 소수점이하 제거
                                return percent.substring(0,index)+"%";
                            }else{
                                return percent+"%";
                            }



                        }
                    });

                    pieChart.setData(piedata);

                    for (int q = begin; q <= end; q++) {
                        xasis_name.add("" + q);
                    }


                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(15);
                    xAxis.setGranularity(1f);
                    xAxis.setAxisMinimum(0f); // must
                    xAxis.setValueFormatter(new ValueFormatter() {
                        int crazy;
                        @Override
                        public String getFormattedValue(float value) {
                            crazy++;
                            int index = (int) value;
//                            Log.d("mawang", "직단 -xAxis  float value/index/crazy = " + value+"/"+index+"/"+crazy );

                            if (index < 0 || index >= xasis_name.size()) {
                                return "";
                            } else {
                                return xasis_name.get(index);
                            }
                        }
                    });
                    YAxis yAxis = barChart.getAxisLeft();
                    yAxis.setDrawGridLines(true);
                    yAxis.setGranularity(1); // interval 1
                    yAxis.setAxisMinimum(0f); // must
                    yAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) value);
                        }
                    });
                    barChart.getAxisRight().setEnabled(false); // 오른쪽 축 제거

                    barChart.setVisibility(View.VISIBLE);
                    barChart.setTouchEnabled(false); // 막아야 과도한 호출이 안생김..
                    barChart.getLegend().setEnabled(false); // label hide
                    barChart.setExtraOffsets(5, 5, 5, 10);

                    BarDataSet set = new BarDataSet(barentries, "");
                    set.setValueTextSize(15);
                    set.setColors(ColorTemplate.LIBERTY_COLORS);
                    //LIBERTY_COLORS , JOYFUL_COLORS , PASTEL_COLORS , COLORFUL_COLORS , VORDIPLOM_COLORS , MATERIAL_COLORS

                    BarData bardata = new BarData(set);
                    //                    bardata.setBarWidth(0.9f); // 0.85 default
                    bardata.setValueTextColor(Color.BLACK);
                    bardata.setValueTextSize(15);
                    bardata.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) { // value는 해당 데이터 표시값
                            return String.valueOf((int) value);
                        }
                    });

//                    Description description2 = new Description();
//                    description2.setText("응답수 :" + replyTotalNum); //라벨
//                    description2.setTextSize(14);
//                    barChart.setDescription(description2);
                    barChart.getDescription().setEnabled(false);

                    barChart.setData(bardata);
                    barChart.setFitBars(true);
                    barChart.invalidate();



                } else if (type.get(i) == FormType.RADIOCHOICEGRID || type.get(i) == FormType.CHECKBOXGRID)
                {
                    // 객관식 그리드, 체크박스 그리드인 경우
//                    Log.d("mawang", "GraphFragment onCreateView 라그 - answer = " + answer);
//                    Log.d("mawang", "GraphFragment onCreateView 그리드 - grid_rows = " + grid_rows);
//                    Log.d("mawang", "GraphFragment onCreateView 그리드 - grid_cols = " + grid_cols);
//                    Log.d("mawang", "GraphFragment onCreateView 그리드 - grid_checkedValues = " + grid_checkedValues);

                    ArrayList<ArrayList<Integer>> checked_gridresults = new ArrayList<ArrayList<Integer>>();

                    for (int k = 0; k < grid_cols.get(i).size(); k++) {
                        ArrayList<Integer> _gridresults = new ArrayList<Integer>();
                        switch (type.get(i)) {
                            case FormType.RADIOCHOICEGRID:
// 저도 복잡해서,, ... 설명하기 힘듭니다.

                                int pos = 0;

                                for (int j = 0; j < grid_rows.get(i).size(); j++) {
                                    int find_count = 0;

                                    if (!grid_checkedValues.isEmpty()) {
//                                        Log.d("mawang", "GraphFragment onCreateView 그리드 - 본체"+grid_checkedValues.size());

                                        if (!grid_checkedValues.get(i).isEmpty()) {
//                                            Log.d("mawang", "GraphFragment onCreateView 그리드 - 부분,"+grid_checkedValues.get(i).size());
                                            // y+= grid_rows.get(i).size() , 행마다 계산해주기 위해서
                                            for (int y = pos; y < grid_checkedValues.get(i).size(); y += grid_rows.get(i).size()) {

                                                if (grid_cols.get(i).get(k).equalsIgnoreCase(grid_checkedValues.get(i).get(y))) {
                                                    find_count++; // 해당 단어를 찾은 경우
//                                            Log.d("mawang", "GraphFragment onCreateView 그리드 -일치! i/k/j/y = " + i + "/" + k+ "/" + j  + "/" + y +"at pos:"+pos);
                                                }

                                            }
                                        } else {
//                                            Log.d("mawang", "GraphFragment onCreateView 그리드 - 부분null,"+grid_checkedValues.get(i).size());
                                        }

                                    } else {
//                                        Log.d("mawang", "GraphFragment onCreateView 그리드 - 본체null,"+grid_checkedValues.size());
                                    }


                                    _gridresults.add(find_count);
                                    pos++;


                                }
//                                Log.d("mawang", "GraphFragment onCreateView 라그 - _gridresults = " + _gridresults);
                                checked_gridresults.add(_gridresults);

                                break;
                            case FormType.CHECKBOXGRID:
// 체크그리드쪽은 어떻게 돌아가는지도 잘 모르겠습니다..
                                int pos2 = 0;
                                boolean Ismore = false;
                                for (int j = 0; j < grid_rows.get(i).size(); j++) {

                                    int find_count = 0;

                                    if (!grid_checkedValues.isEmpty()) {
                                        if (!grid_checkedValues.get(i).isEmpty()) {

                                            for (int y = pos2; y < grid_checkedValues.get(i).size(); y ++) {

                                                if ("@".equals(grid_checkedValues.get(i).get(y))) {
                                                    pos2 = y+1;
                                                    break;// 오 감사합니다 break 는 누가 만든 키워드인지 정말 감사합니다
                                                }
                                                if (grid_cols.get(i).get(k).equalsIgnoreCase(grid_checkedValues.get(i).get(y))) {
                                                    find_count++; // 해당 단어를 찾은 경우
//                                                    Log.d("mawang", "GraphFragment onCreateView 쳌그 -일치! i/k/j/y = " + i + "/" + k+ "/" + j  + "/" + y +" at pos:"+pos2); //
                                                }

                                            }
                                        }
                                    }
//                                    Log.d("mawang", "======================== j:"+j+"/ find_count:"+find_count+"/ Ismore:"+Ismore+" ========================");

                                    if(pos2 < grid_checkedValues.get(i).size() && j == grid_rows.get(i).size()-1 ){ // still datas left
//                                        Log.d("mawang", "테스트1 - pos:" + pos2);

                                        if(Ismore){
                                            _gridresults.set(j, _gridresults.get(j)+find_count);
                                        }else{
                                            _gridresults.add(find_count);
                                        }

//                                        Log.d("mawang", "테스트1 - _gridresults:"+_gridresults);
                                        Ismore = true;
                                        j=-1; // 이래야 0이 됨
                                        continue;
                                    }

                                    if(Ismore){ // 결과가 여러개일때
                                        _gridresults.set(j, _gridresults.get(j)+find_count);
//                                        Log.d("mawang", "테스트2 - _gridresults:" + _gridresults);
                                    }else{
                                        _gridresults.add(find_count);
//                                        Log.d("mawang", "테스트3 - _gridresults:" + _gridresults);
                                    }



                                }
//                                Log.d("mawang", "GraphFragment onCreateView 쳌그 - _gridresults = " + _gridresults);
                                checked_gridresults.add(_gridresults);



                                break;
                        }
                    }

//                    Log.d("mawang", "GraphFragment onCreateView 그리드 - checked_gridresults = " + checked_gridresults);


// 그래프 표시
                    barChart.setVisibility(View.VISIBLE);
                    barChart.setTouchEnabled(false); // 막아야 과도한 호출이 안생김..
                    ArrayList<String> xasis_name = new ArrayList<String>();

                    ArrayList<ArrayList<BarEntry>> barentries = new ArrayList<ArrayList<BarEntry>>();

                    for (int p = 0; p < grid_cols.get(i).size(); p++) {
                        ArrayList<BarEntry> values = new ArrayList<>();
                        for (int q = 0; q < grid_rows.get(i).size(); q++) {

                            values.add(new BarEntry(q, checked_gridresults.get(p).get(q)));
                        }
//                        Log.d("mawang", "GraphFragment onCreateView 라그 - p/values"+p+"/" + values);
                        barentries.add(values);

                    }
                    for (int p = 0; p < grid_rows.get(i).size(); p++) { // 위 루프랑 합치기 가능
                        xasis_name.add(grid_rows.get(i).get(p));
                    }
//                    Log.d("mawang", "GraphFragment onCreateView 라그 - barentries = " + barentries);
//                    Log.d("mawang", "GraphFragment onCreateView 라그 - xasis_name = " + xasis_name);

                    Legend l = barChart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                    l.setDrawInside(false);
                    l.setTextSize(12f);

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(15);
                    xAxis.setDrawGridLines(true); // 이거 없애고 , temp
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new ValueFormatter() {

                        @Override
                        public String getFormattedValue(float value) {
                            int index = (int) value;

                            if (index < 0 || index >= xasis_name.size()) {
                                return "";
                            } else {
                                return xasis_name.get(index);
                            }
                        }
                    });


                    YAxis yAxis = barChart.getAxisLeft();
                    yAxis.setDrawGridLines(true);
                    yAxis.setGranularity(1); // interval 1
                    yAxis.setAxisMinimum(0f); // must

                    yAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) value);
                        }
                    });


                    barChart.getAxisRight().setEnabled(false); // 오른쪽 축 제거


                    ArrayList<BarDataSet> barDatasets = new ArrayList<BarDataSet>();

                    for (int p = 0; p < grid_cols.get(i).size(); p++) {
                        barDatasets.add(new BarDataSet(barentries.get(p), grid_cols.get(i).get(p)));

                        Random rnd = new Random();
                        int randNum = rnd.nextInt(0xffffff + 1);
                        String colorCode = String.format("#%06x", randNum);
                        barDatasets.get(p).setColor(Color.parseColor(colorCode)); // 색 랜덤생성

//                        barDatasets.get(p).setColors(ColorTemplate.COLORFUL_COLORS);
                        //LIBERTY_COLORS , JOYFUL_COLORS , PASTEL_COLORS , COLORFUL_COLORS , VORDIPLOM_COLORS , MATERIAL_COLORS
//                        Log.d("mawang", "GraphFragment onCreateView 그리드 - colorcode = " + colorCode);
                    }
//                    Log.d("mawang", "GraphFragment onCreateView 그리드 - barDatasets = " + barDatasets);


                    BarData barData = new BarData();
                    for (int p = 0; p < barDatasets.size(); p++) {
                        barData.addDataSet(barDatasets.get(p)); // 그냥은 되나?
                    }


                    barData.setValueFormatter(new ValueFormatter() {

                        @Override
                        public String getFormattedValue(float value) { // value는 해당 데이터 표시값
                            int index = (int) value;
                            return String.valueOf(index);
                        }
                    });

                    barChart.setData(barData);


                    // col,row에 따라 조절해야함
// 아래 3개 변수는 수많은 착오끝에 얻은 최적값이니 절대 바꾸지 마시오!
                    float barSpace = 0f; // 바끼리의 공간 , 0.01f
                    float barWidth = 1f / barData.getDataSetCount();
                    float groupSpace = 1f - (barWidth * barData.getDataSetCount()); // 합이 1? 이 되야 깔끔


                    int replyTotalNum = 0;
                    for(ArrayList<Integer> row : checked_gridresults) {
                        for(int col : row) {
                            replyTotalNum += col;
//                            if(isMax<col){isMax=col;}
                        }
                    }

//                    barChart.getDescription().setEnabled(false); // 대기
                    Description description = new Description();
                    description.setText("응답수 :" + replyTotalNum); //라벨
                    description.setTextSize(18); //ㅅㅂ
                    barChart.setDescription(description);

                    if ( grid_cols.get(i).size() == 1 || barDatasets.isEmpty()) { // 1행만 존재하면 groupBars에서 err
                        barChart.setFitBars(true);
                        Log.d("mawang", "GraphFragment onCreateView 그리드 - setFitBars @@@");
                    } else {
                        xAxis.setAxisMinimum(0f); // what happen? 안하면 디자인 엉망진창
                        xAxis.setCenterAxisLabels(true);
                        barData.setBarWidth(barWidth);
//                        barChart.getXAxis().setAxisMaximum(barChart.getBarData().getGroupWidth(groupSpace, barSpace) * grid_rows.get(i).size());
                        barChart.getXAxis().setAxisMaximum(barData.getGroupWidth(groupSpace, barSpace) * grid_rows.get(i).size());
                        barChart.groupBars(0.01f, groupSpace, barSpace); // 0f는 안먹히는가?
                        Log.d("mawang", "GraphFragment onCreateView 그리드 - groupBars @@@");
                    }



                    barChart.invalidate();


//                    Log.d("mawang", "GraphFragment onCreateView 그리드 - BarData getDataSetCount= " + barData.getDataSetCount());
//                    Log.d("mawang", "GraphFragment onCreateView 그리드 - BarData getBarWidth= " + barData.getBarWidth());
//                    Log.d("mawang", "GraphFragment onCreateView 그리드 - BarDataget GroupWidth = " + barData.getGroupWidth(groupSpace, barSpace));
//                    Log.d("mawang", "GraphFragment onCreateView 그리드 - BarDataget 체크용 = " + (barChart.getBarData().getGroupWidth(groupSpace, barSpace) * grid_rows.get(i).size())); //
//                    Log.d("mawang", "GraphFragment onCreateView 그리드 - BarDataget 체크용 = " + (barData.getGroupWidth(groupSpace, barSpace) * grid_rows.get(i).size())); //


                } else {
                    // 단답형, 장문형, 날짜,시간
                    // 섹션,서브설명,이미지 제외
                    Log.d("mawang", "GraphFragment onCreateView 한줄파트 - answer = " + answer);

                    TableLayout tl2 = (TableLayout) v.findViewById(R.id.tablelayout_forShort);
                    tl2.setVisibility(View.VISIBLE);

                    TextView TotalNum = v.findViewById(R.id.tablelayout_forShort_totalNum);
                    TotalNum.setText("총 응답수 : " + answer.get(i).size());

                    if (type.get(i) == FormType.DATE) {
                        for (int k = 0; k < answer.get(i).size(); k++) {
                            if (!answer.get(i).get(k).isEmpty()) { // substring error 방지
                                answer.get(i).set(k, answer.get(i).get(k).substring(0, 4) + "년 " +
                                        answer.get(i).get(k).substring(4, 6) + "월 " +
                                        answer.get(i).get(k).substring(6, 8) + "일");
                            }
                        }
                    } else if (type.get(i) == FormType.LONGTEXT) {
                        for (int k = 0; k < answer.get(i).size(); k++) {
                            answer.get(i).set(k, answer.get(i).get(k).replace("rn", "\n"));
                            Log.d("mawang", "GraphFragment onCreateView LONGTEXT - escape work = " + answer.get(i).get(k));
                        }

                    }


                    TextView textView[][] = new TextView[answer.get(i).size()][2];
                    setTextView_defaultLayout(textView, answer.get(i).size(), 2);


                    for (int k = 0; k < answer.get(i).size(); k++) {
                        TableRow tr = new TableRow(getContext());
//                        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        TableLayout.LayoutParams tlp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        tlp.setMargins(0, 0, 0, 5);
                        tr.setLayoutParams(tlp);

                        for (int l = 0; l < 2; l++) {
                            if (l == 0) {
                                textView[k][l].setText("" + (k + 1));

                                TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
                                textView[k][l].setLayoutParams(params2);

                                tr.addView(textView[k][l]);
                            } else if (l == 1) {
                                if (answer.get(i).get(k).isEmpty()) {
                                    textView[k][l].setText("(응답X)");
                                } else {
                                    textView[k][l].setText(answer.get(i).get(k));
                                }

                                TableRow.LayoutParams params8 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 8f);
                                textView[k][l].setLayoutParams(params8);

                                tr.addView(textView[k][l]);
                            }

                        }

                        tl2.addView(tr);
                    }


                }
            }
        }

        return v;
    }

    public static GraphFragment newInstance(String selectedQ,
                                            ArrayList<String> survey_question,
                                            ArrayList<ArrayList<String>> survey_answer,
                                            ArrayList<Integer> survey_type,
                                            ArrayList<ArrayList<String>> choice_option, ArrayList<Integer> grid_beginIndex, ArrayList<Integer> grid_endIndex,
                                            ArrayList<ArrayList<String>> grid_rows, ArrayList<ArrayList<String>> grid_cols, ArrayList<ArrayList<String>> grid_checkedValues
    ) {
        GraphFragment gf = new GraphFragment();
        Bundle putBundle = new Bundle();

        putBundle.putString("selected_question", selectedQ);

        putBundle.putStringArrayList("question", survey_question);
        putBundle.putSerializable("answer", survey_answer);
        putBundle.putIntegerArrayList("type", survey_type);


        putBundle.putSerializable("choice_option", choice_option);
        putBundle.putIntegerArrayList("grid_beginIndex", grid_beginIndex);
        putBundle.putIntegerArrayList("grid_endIndex", grid_endIndex);


        putBundle.putSerializable("grid_rows", grid_rows);
        putBundle.putSerializable("grid_cols", grid_cols);
        putBundle.putSerializable("grid_checkedValue", grid_checkedValues);

        gf.setArguments(putBundle);

        return gf;
    }

    public void setTextView_defaultLayout(TextView textView[][], int row, int col) {
        for (int k = 0; k < row; k++) {
            for (int l = 0; l < col; l++) {
                textView[k][l] = new TextView(getContext());
                textView[k][l].setPadding(3, 3, 3, 3);
                textView[k][l].setBackgroundColor(Color.WHITE);
                textView[k][l].setTextSize(17);
                textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
    }




























}

