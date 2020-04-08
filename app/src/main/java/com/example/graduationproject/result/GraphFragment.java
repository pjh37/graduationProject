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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {
    PieChart pieChart;
    BarChart barChart;

    private ArrayList<Integer> type;
    private ArrayList<String> question;

    private String selectedQuestion;
    private ArrayList<ArrayList<String>> answer;

    private ArrayList<ArrayList<String>> optionLabels; // 객관식,체크박스,드롭다운 보기들

    private ArrayList<Integer> lineargridbeginIndex;
    private ArrayList<Integer> lineargridEndIndex;
//    private ArrayList<String> lineargrid_NoOverlappedQuestions = new ArrayList<String>(); // 중복없앤 질문목록


    private ArrayList<ArrayList<String>> grid_rows;
    private ArrayList<ArrayList<String>> grid_cols;
    //    private ArrayList<String> grid_results;
    private ArrayList<ArrayList<String>> grid_checkedValues;

    //private ArrayList<String> get_removerlap = new ArrayList<String>(); //?
//    private ArrayList<Integer> get_basicres = new ArrayList<Integer>();


    View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle getBundle = getArguments();

        if (getBundle != null) {
            selectedQuestion = getBundle.getString("selected_question");

            question = getBundle.getStringArrayList("question");
            answer = (ArrayList<ArrayList<String>>) getBundle.getSerializable("answer");

            type = getBundle.getIntegerArrayList("type");
            optionLabels = (ArrayList<ArrayList<String>>) getBundle.getSerializable("choice_option");
            lineargridbeginIndex = getBundle.getIntegerArrayList("grid_beginIndex");
            lineargridEndIndex = getBundle.getIntegerArrayList("grid_endIndex");


            grid_rows = (ArrayList<ArrayList<String>>) getBundle.getSerializable("grid_rows");
            grid_cols = (ArrayList<ArrayList<String>>) getBundle.getSerializable("grid_cols");
            //            grid_results = getBundle.getStringArrayList("grid_result");
            grid_checkedValues = (ArrayList<ArrayList<String>>) getBundle.getSerializable("grid_checkedValue");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_graph, container, false);
        TableLayout tl = (TableLayout) v.findViewById(R.id.table_layout);

        pieChart = (PieChart) v.findViewById(R.id.piechart);
        barChart = (BarChart) v.findViewById(R.id.barchart);

//        for (int k = 0; k < question.size(); k++) {
//
//            if (!lineargrid_NoOverlappedQuestions.contains(question.get(k))) {
//                lineargrid_NoOverlappedQuestions.add(question.get(k));
//                Log.d("mawang", "GraphFragment onCreateView - 질문 " + k + " : " + question.get(k));
//            } else { // debugging
//                Log.d("mawang", "GraphFragment onCreateView - 중복질문 " + k + " : " + question.get(k));
//            }
//        }
//        Log.d("mawang", "GraphFragment onCreateView - lineargrid_NoOverlappedQuestions " + lineargrid_NoOverlappedQuestions);
        Log.d("mawang", "GraphFragment onCreateView - selectedQuestion = " + selectedQuestion);
        Log.d("mawang", "GraphFragment onCreateView - question = " + question);
//        Log.d("mawang", "GraphFragment onCreateView - question.size() = " + question.size());

        int check_count = 0;

        for (int i = 0; i < question.size(); i++) { // 항목개수만큼의 loop
            if (question.get(i).equalsIgnoreCase(selectedQuestion)) { // 선택한 질문만 보이도록

                if (type.get(i) == FormType.RADIOCHOICE || type.get(i) == FormType.CHECKBOXES || type.get(i) == FormType.DROPDOWN)
                {
                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - answer = " + answer);
                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - optionLabels = " + optionLabels);

                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - answer.size() = " + answer.size());
                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - answer.isEmpty() = " + answer.isEmpty());
//                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - answer.get(i).size() = " + answer.get(i).size());

                    ArrayList<Integer> optionResults = new ArrayList<Integer>();
                    int arr[] = new int[optionLabels.get(i).size()]; // 체크한 답지 카운팅용

                    if(answer != null){ // null check
                        for (int k = 0; k < optionLabels.get(i).size(); k++) {
                            int find_count = 0;

                            //겹치는 글자있으면 카운트 되는 오류 ,, 좀 더 정밀한 체크 필요를 위해 변경
//                                if(answer.get(i).contains(optionLabels.get(i).get(k))) {}

                            if(answer.get(i) != null) { // 이중 null check
                                for (int j = 0; j < answer.get(i).size(); j++) {

                                    if (optionLabels.get(i).get(k).equalsIgnoreCase(answer.get(i).get(j))) {
                                        check_count++; // 선택한 전체 항목 수 카운트
                                        find_count++; // 해당 단어를 찾은 경우
                                        Log.d("mawang", "GraphFragment onCreateView CHECKBOXES -일치! 답 = " + optionLabels.get(i).get(k));
                                    }
                                }
                            }
//                            else{
//                                Log.d("mawang", "GraphFragment onCreateView CHECKBOXES -  answer.get("+i+") == null ");
//                            }

                            optionResults.add(find_count);
                        }



                        for (int p = 0; p < optionLabels.get(i).size(); p++) { // 보기만큼 해줘야함
                            arr[p] += optionResults.get(p);
                        }


                    } else{
                        Log.d("mawang", "GraphFragment onCreateView CHECKBOXES -  answer is null at"+i);
                    }

                    for (int m = 0; m < arr.length; m++) { // 확인용
                        Log.d("mawang", "GraphFragment onCreateView 옵션즈 - arr[" + m + "] = " + arr[m]);
                    }
                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - optionResults = " + optionResults);

                    tl.setVisibility(View.VISIBLE);
                    TextView textView[][] = new TextView[optionLabels.get(i).size()][3];
                    setTextView_defaultLayout(textView, optionLabels.get(i).size(),3 ); // textView 세팅

                    int replyTotalNum = 0;
                    for (int n : arr) {
                        replyTotalNum += n;
                    }
                    Log.d("mawang", "GraphFragment onCreateView 옵션즈 - replyTotalNum = " + replyTotalNum);

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
                                double calc_resrate = (double) arr[k] / check_count;
                                textView[k][l].setText("" + arr[k] + " (" + String.format("%.1f", calc_resrate) + "%)");
                                tr.addView(textView[k][l]);
                            }
                            if (tr.getParent() != null) {
                                ((ViewGroup) tr.getParent()).removeView(tr);
                            }
                        }

                        tl.addView(tr);
                    }

//                    pieChart = (PieChart) v.findViewById(R.id.piechart);
//                    barChart = (BarChart) v.findViewById(R.id.barchart);
                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                    List<BarEntry> barentries = new ArrayList<>();
                    ArrayList<String> xasis_name = new ArrayList<String>();

                    pieChart.setVisibility(View.VISIBLE);
                    pieChart.setUsePercentValues(true);
                    pieChart.setExtraOffsets(5, 10, 5, 5);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.setHoleColor(Color.WHITE);
                    pieChart.setTransparentCircleRadius(61f);
                    pieChart.setEntryLabelTextSize(20);

                    Description description = new Description();
                    description.setText("응답수 :" + replyTotalNum); //라벨
                    description.setTextSize(10);
                    pieChart.setDescription(description);
//                        pieChart.getDescription().setEnabled(false);
                    pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                    PieDataSet piedataSet = new PieDataSet(yValues, "");
                    piedataSet.setSliceSpace(3f);
                    piedataSet.setSelectionShift(5f);
                    piedataSet.setFormSize(15);
                    piedataSet.setValueTextSize(15);
                    piedataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    PieData piedata = new PieData(piedataSet);
                    piedata.setValueTextColor(Color.YELLOW);
                    piedata.setValueTextSize(15);
                    piedata.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));

                    pieChart.setData(piedata);


                    for (int q = 0; q < optionLabels.get(i).size(); q++) {
                        xasis_name.add(optionLabels.get(i).get(q));
                    }

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(15);
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
//                                return xasis_name.get((int) value % xasis_name.size());
                            return xasis_name.get((int) value);
                        }
                    });

                    for (int p = 0; p < optionLabels.get(i).size(); p++) {
                        if (arr[p] != 0) {
                            yValues.add(new PieEntry(arr[p], optionLabels.get(i).get(p)));
                            barentries.add(new BarEntry(p, arr[p]));
                        }
                    }

                    barChart.setVisibility(View.VISIBLE);
                    BarDataSet bardataset = new BarDataSet(barentries, "");
                    bardataset.setValueTextSize(15);
                    bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    bardataset.setFormSize(15);

                    BarData bardata = new BarData(bardataset);
                    bardata.setBarWidth(0.9f);
                    bardata.setValueFormatter(new IntegerFormatter());
                    bardata.setValueTextColor(Color.YELLOW);
                    bardata.setValueTextSize(15);

                    barChart.setData(bardata);
                    barChart.setFitBars(true);
                    barChart.invalidate();

                    Description description2 = new Description();
                    description2.setText("응답수 :" + replyTotalNum); //라벨
                    description2.setTextSize(10);
                    barChart.setDescription(description2);
//                        barChart.getDescription().setEnabled(false);


                } else if (type.get(i) == FormType.LINEARSCALE)
                {
                    Log.d("mawang", "GraphFragment onCreateView LINEARSCALE - answer = " + answer);

                    //int unique_index = lineargrid_NoOverlappedQuestions.indexOf(selectedQuestion); //??
                    int index = question.indexOf(selectedQuestion);
                    int begin = lineargridbeginIndex.get(index);
                    int end = lineargridEndIndex.get(index);

//                    Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - unique_index = " + unique_index);
                    Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - index = " + index);
                    Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - begin = " + begin);
                    Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - end = " + end);


//                    ArrayList<ArrayList<Integer>> lineargrid_res = new ArrayList<ArrayList<Integer>>(3);

                    int arr[] = new int[11]; //0-10까지만 있으면 됨

                    if(answer != null) { // null check
                        if(answer.get(i) != null) { // 이중 null check
                            for (int j = 0; j < answer.get(i).size(); j++) {
                                ArrayList<Integer> _res = new ArrayList<Integer>(3);
                                if (begin == 1) {
                                    _res.add(0, -1); // 1부터 add 하면 index out of bound err
                                }

                                for (int h = begin; h < end + 1; h++) {
                                    int find_count = 0;

                                    if (answer.get(i).get(j).equalsIgnoreCase("" + h)) {
                                        check_count++; // 선택한 전체 항목 수 카운트
                                        find_count++; // 해당 단어를 찾은 경우
                                        Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - 일치 = " + h);
                                    }
                                    _res.add(h, find_count);
//                            _res.add(find_count);
                                }
//                        lineargrid_res.add(_res);

                                for (int p = begin; p < end + 1; p++) {
                                    arr[p] += _res.get(p);
                                }
                                Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - id/_res = " + j + "/" + _res);
                            }
                        }
                    }


//                    Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - lineargrid_res = " + lineargrid_res);
//                    for (int p = begin; p < end + 1; p++) {
//                        arr[p] += lineargrid_res.get(i).get(p);
//                    }

                    for (int p = begin; p < end + 1; p++) {
                        Log.d("mawang", "GraphFragment onCreateView 직선 그리드 - arr[" + p + "] = " + arr[p]);
                    }


                    TextView textView[][] = new TextView[11][3]; // 0~10 까지 있을때 최대 경우
                    setTextView_defaultLayout(textView, end+1, 3); // 세팅

                    int replyTotalNum = 0;
                    for (int n : arr) {
                        replyTotalNum += n;
                    }

                    for (int k = begin; k < end + 1; k++) {
                        TableRow tr = new TableRow(getContext());
                        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        for (int l = 0; l < 3; l++) {

                            if (l == 0) {
                                textView[k][l].setText("");
                                tr.addView(textView[k][l]);
                            } else if (l == 1) {
                                textView[k][l].setText("" + k);
                                tr.addView(textView[k][l]);
                            } else {
                                double calc_resrate = (double) arr[k] / check_count;
                                textView[k][l].setText("" + arr[k] + " (" + String.format("%.1f", calc_resrate) + "%)");
//                                textView[k][l].setText("" + arr[k - 1] + " (" + String.format("%.1f", calc_resrate) + "%)");
                                tr.addView(textView[k][l]);
                            }
                            if (tr.getParent() != null) {
                                ((ViewGroup) tr.getParent()).removeView(tr);
                            }

                            tl.addView(tr);

                        }
                    }


//                    pieChart = (PieChart) v.findViewById(R.id.piechart);
//                    barChart = (BarChart) v.findViewById(R.id.barchart);
                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                    List<BarEntry> barentries = new ArrayList<>();
                    ArrayList<String> xasis_name = new ArrayList<String>();

                    for (int p = begin; p <= end; p++) {
                        if (arr[p] != 0) { // 체크된 데이터만 표시

                            if (begin == 0) {
                                yValues.add(new PieEntry(arr[p], "" + p));
                                barentries.add(new BarEntry(p, arr[p]));
                            } else if (begin == 1) {
                                yValues.add(new PieEntry(arr[p], "" + p));
                                barentries.add(new BarEntry(p - 1, arr[p]));
                                // p-1 을 해야 올바른 매칭이 된다.
                                // 왜지..? 이유는 잘 모르겠네
                            }
                        }
                    }


                    pieChart.setVisibility(View.VISIBLE);
                    pieChart.setUsePercentValues(true);
                    pieChart.setExtraOffsets(5, 10, 5, 5);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.setHoleColor(Color.WHITE);
                    pieChart.setTransparentCircleRadius(61f);
                    pieChart.setEntryLabelTextSize(20);

                    Description description = new Description();
                    description.setText("응답수 :" + replyTotalNum); //라벨
                    description.setTextSize(10);
                    pieChart.setDescription(description);
//                        pieChart.getDescription().setEnabled(false);
                    pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                    PieDataSet dataSet = new PieDataSet(yValues, "");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setFormSize(15);
                    dataSet.setValueTextSize(15);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    PieData piedata = new PieData(dataSet);
                    piedata.setValueTextColor(Color.YELLOW);
                    piedata.setValueTextSize(15);
                    piedata.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));

                    pieChart.setData(piedata);

                    for (int q = begin; q <= end; q++) {
                        xasis_name.add("" + q);
                    }


                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(15);
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            return xasis_name.get((int) value % xasis_name.size());
                        }
                    });

//                            Log.d("mawang", "GraphFragment onCreateView 직선 그리드 -barentries = " + barentries);

                    barChart.setVisibility(View.VISIBLE);

                    BarDataSet set = new BarDataSet(barentries, "");
                    set.setValueTextSize(15);
                    set.setColors(ColorTemplate.COLORFUL_COLORS);

                    BarData bardata = new BarData(set);
                    bardata.setBarWidth(0.9f);
                    bardata.setValueFormatter(new IntegerFormatter());

                    Description description2 = new Description();
                    description2.setText("응답수 :" + replyTotalNum); //라벨
                    description2.setTextSize(10);

                    barChart.setData(bardata);
                    barChart.setFitBars(true);
                    barChart.setDescription(description2);
                    barChart.invalidate();
//                        barChart.getDescription().setEnabled(false);


                } else if (type.get(i) == FormType.RADIOCHOICEGRID || type.get(i) == FormType.CHECKBOXGRID)
                {
                    // 객관식 그리드, 체크박스 그리드인 경우
                    Log.d("mawang", "GraphFragment onCreateView 라그 - answer = " + answer);
                    Log.d("mawang", "GraphFragment onCreateView 라그 - grid_rows = " + grid_rows);
                    Log.d("mawang", "GraphFragment onCreateView 라그 - grid_cols = " + grid_cols);
                    Log.d("mawang", "GraphFragment onCreateView 라그 - grid_checkedValues = " + grid_checkedValues);

                    ArrayList<Integer> checked_gridresults = new ArrayList<Integer>();

//                    for (int k = 0; k < grid_cols.get(i).size(); k++) {
                    for (int k = 0; k < grid_checkedValues.get(i).size(); k++) {

                        switch (type.get(i)) {
                            case FormType.RADIOCHOICEGRID:
                                int find_count = 0;

//                                for (int j = 0; j < grid_checkedValues.get(i).size(); j++) {
                                for (int j = 0; j < grid_cols.get(i).size(); j++) {
//                                    if (grid_cols.get(i).get(k).equalsIgnoreCase(grid_checkedValues.get(i).get(j)))
                                    if (grid_checkedValues.get(i).get(k).equalsIgnoreCase(grid_cols.get(i).get(j))) {
                                        check_count++; // 선택한 전체 항목 수 카운트
                                        find_count++; // 해당 단어를 찾은 경우
                                        Log.d("mawang", "GraphFragment onCreateView 라그 -일치! i/k/j = " + i + "/" + k + "/" + j);
                                    }
                                }

                                checked_gridresults.add(find_count); // 라디오는 한개만 체크하지만

                                break;
                            case FormType.CHECKBOXGRID:
//                                int find_count2 = 0;
//                                String[] eachGridColAnswer2 = eachGridAnswer[k].split("-");
//                                eachGridAnswer_row.add(eachGridColAnswer2[0]); // 행 라벨 표시용
//                                String[] eachGridColAnswer_multi = eachGridColAnswer2[1].split(",");
//
//
//                                for (String ans : eachGridColAnswer_multi) { // column 개수 loop
//                                    Log.d("mawang", "GraphFragment onCreateView CHECKBOXGRID - ans/pos = " + ans+"/"+pos);
//
//                                    if (ans.equalsIgnoreCase(grid_results.get(pos))) {
//                                        check_count++; // 선택한 전체 항목 수 카운트
//                                        find_count2++; // 해당 단어를 찾은 경우
//                                        Log.d("mawang", "GraphFragment onCreateView CHECKBOXGRID -일치 카운트! ");
//                                    }
//
//                                    checked_gridresults.add(find_count2); // 체크박스는 여러개를 체크해서이다.
//
//
//                                    eachGridAnswer_row_numOfcols.add(k);
//                                    pos++;
//                                    find_count2=0;
//                                }

                                break;
                        }
                    }


//                    int arr[] = new int[grid_cols.get(i).size()];
                    int arr[] = new int[grid_checkedValues.get(i).size()];

                    for (int m = 0; m < checked_gridresults.size(); m++) {
                        arr[m] += checked_gridresults.get(m);
                    }
                    for (int m = 0; m < arr.length; m++) {
                        Log.d("mawang", "GraphFragment onCreateView 라그 - arr[" + m + "] = " + arr[m]);
                    }


                    TextView textView[][] = new TextView[grid_rows.get(i).size()][3];
                    setTextView_defaultLayout(textView, grid_rows.get(i).size(),3 ); // textView 세팅

                    TextView tv_num = v.findViewById(R.id.tablerow_number);
                    TextView tv_content = v.findViewById(R.id.tablerow_content);
                    tv_num.setText("질문");
                    tv_content.setText("답");

                    int replyTotalNum = 0;
                    for (int n : arr) {
                        replyTotalNum += n;
                    }
                    Log.d("mawang", "GraphFragment onCreateView 라그 - replyTotalNum = " + replyTotalNum);

                    for (int k = 0; k < grid_rows.get(i).size(); k++) { // 행수만큼만 반복
                        TableRow tr = new TableRow(getContext());
                        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        for (int l = 0; l < 3; l++) {
                            if (l == 0) {
//                                    textView[k][l].setText("" + (k + 1));

                                if (type.get(i) == FormType.RADIOCHOICEGRID) {
                                    textView[k][l].setText(grid_rows.get(i).get(k));
                                    // row사이즈에 맞게
                                }
//                                    else if (type.get(i) == FormType.CHECKBOXGRID) {
//                                        textView[k][l].setText(eachGridAnswer_row.get(eachGridAnswer_row_numOfcols.get(k)));
//                                    }

                                tr.addView(textView[k][l]);
                            } else if (l == 1) {
                                textView[k][l].setText(grid_checkedValues.get(i).get(k));
//                                    textView[k][l].setText(grid_cols.get(i).get(Integer.parseInt(answer.get(i).get(k))));
                                tr.addView(textView[k][l]);
                            } else if (l == 2) {
                                double calc_resrate = (double) arr[k] / check_count;
                                textView[k][l].setText("" + arr[k] + " (" + String.format("%.1f", calc_resrate) + "%)");
                                tr.addView(textView[k][l]);
                            }

                            if (tr.getParent() != null) {
                                ((ViewGroup) tr.getParent()).removeView(tr);
                            }
                        }

                        tl.addView(tr);
                    } // 그렇다면 다른 것들도?

//                    pieChart = (PieChart) v.findViewById(R.id.piechart);
//                    barChart = (BarChart) v.findViewById(R.id.barchart);
                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                    List<BarEntry> barentries = new ArrayList<>();
                    ArrayList<String> xasis_name = new ArrayList<String>();

                    for (int p = 0; p < grid_checkedValues.get(i).size(); p++) {
                        if (arr[p] != 0) {
                            yValues.add(new PieEntry(arr[p], grid_checkedValues.get(i).get(p)));
//                                    yValues.add(new PieEntry(arr[p], grid_cols.get(i).get(Integer.parseInt(answer.get(i).get(p)))));

                            barentries.add(new BarEntry(p, arr[p]));
                        }
                    }
//                            Log.d("mawang", "GraphFragment onCreateView 라그 - yValues = " + yValues);
//                            Log.d("mawang", "GraphFragment onCreateView 라그 - barentries = " + barentries);


                    pieChart.setVisibility(View.VISIBLE);
                    pieChart.setUsePercentValues(true);
                    pieChart.setExtraOffsets(5, 10, 5, 5);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.setHoleColor(Color.WHITE);
                    pieChart.setTransparentCircleRadius(61f);
                    pieChart.setEntryLabelTextSize(20);
                    pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                    Description description = new Description();
                    description.setText("총 응답수 :" + replyTotalNum); //라벨
                    description.setTextSize(10);
//                        pieChart.getDescription().setEnabled(false);

                    PieDataSet dataSet = new PieDataSet(yValues, "");
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setFormSize(15);
                    dataSet.setValueTextSize(15);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    PieData piedata = new PieData(dataSet);
                    piedata.setValueTextColor(Color.YELLOW);
                    piedata.setValueTextSize(15);
                    piedata.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));

                    pieChart.setDescription(description);
                    pieChart.setData(piedata);

                    for (int q = 0; q < grid_checkedValues.get(i).size(); q++) {
                        xasis_name.add(grid_checkedValues.get(i).get(q));
//                                xasis_name.add(grid_cols.get(i).get(Integer.parseInt(answer.get(i).get(q))));
                    }
//                            Log.d("mawang", "GraphFragment onCreateView 라그 - xasis_name = " + xasis_name);

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(15);
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            return xasis_name.get((int) value % xasis_name.size());
                        }
                    });

                    barChart.setVisibility(View.VISIBLE);

                    BarDataSet set = new BarDataSet(barentries, "");
                    set.setValueTextSize(15);
                    set.setColors(ColorTemplate.COLORFUL_COLORS);

                    BarData bardata = new BarData(set);
                    bardata.setBarWidth(0.9f);
                    bardata.setValueFormatter(new IntegerFormatter());


                    Description description2 = new Description();
                    description2.setText("총 응답수 :" + replyTotalNum); //라벨
                    description2.setTextSize(10);
                    //                        barChart.getDescription().setEnabled(false);

                    barChart.setData(bardata);
                    barChart.setFitBars(true);
                    barChart.invalidate();
                    barChart.setDescription(description2);


                } else
                    {
                    // 단답형, 장문형, 날짜,시간
                    // 섹션,서브설명,이미지 제외
                    Log.d("mawang", "GraphFragment onCreateView 한줄파트 - answer = " + answer);

                    TableLayout tl2 = (TableLayout) v.findViewById(R.id.tablelayout_forShort);
                    tl2.setVisibility(View.VISIBLE);

                    TextView TotalNum = v.findViewById(R.id.tablelayout_forShort_totalNum);
                    TotalNum.setText("총 응답수 : "+answer.get(i).size());

                    if (type.get(i) == FormType.DATE ){
                        for (int k = 0; k < answer.get(i).size(); k++) {
                            if(!answer.get(i).get(k).isEmpty()){ // substring error 방지
                                answer.get(i).set(k,answer.get(i).get(k).substring(0,4)+"년 "+
                                        answer.get(i).get(k).substring(4,6)+"월 "+
                                        answer.get(i).get(k).substring(6,8)+"일");
                            }
                        }
                    }else  if (type.get(i) == FormType.LONGTEXT){
                        for (int k = 0; k < answer.get(i).size(); k++) {
                            answer.get(i).set(k,answer.get(i).get(k).replace("rn", "\n"));
                            Log.d("mawang", "GraphFragment onCreateView LONGTEXT - escape work = "+answer.get(i).get(k));
                        }

                    }


                    TextView textView[][] = new TextView[answer.get(i).size()][2];
                    setTextView_defaultLayout(textView, answer.get(i).size(), 2);


                    for (int k = 0; k < answer.get(i).size(); k++) {
                        TableRow tr = new TableRow(getContext());
//                        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        TableLayout.LayoutParams tlp = new  TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        tlp.setMargins(0,0,0,5);
                        tr.setLayoutParams(tlp);

                        for (int l = 0; l < 2; l++) {
                            if (l == 0) {
                                textView[k][l].setText("" + (k + 1));

                                TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f);
                                textView[k][l].setLayoutParams(params2);

                                tr.addView(textView[k][l]);
                            } else if (l == 1) {
                                if(answer.get(i).get(k).isEmpty()){
                                    textView[k][l].setText("(응답X)");
                                }else{
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
