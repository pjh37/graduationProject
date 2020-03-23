package com.example.graduationproject.result;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.graduationproject.R;
import com.example.graduationproject.login.LogoutCallback;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class    GraphFragment extends Fragment {
    PieChart pieChart;
    BarChart barChart;

    private ArrayList<String> get_question;
    private ArrayList<String> get_answer;
    private String get_selected;
    private ArrayList<Integer> get_type;
    private ArrayList<ArrayList<String>> get_option;
    private ArrayList<Integer> get_gridbegin;
    private ArrayList<Integer> get_gridend;
    private ArrayList<String> get_gridrowcol;
    private ArrayList<String> get_qna;
    private ArrayList<String> get_removlQ = new ArrayList<String>();
    private ArrayList<String> get_removerlap = new ArrayList<String>();
    private ArrayList<Integer> get_optionres = new ArrayList<Integer>();
    private ArrayList<Integer> get_lingridres = new ArrayList<Integer>();
    private ArrayList<Integer> get_chkgridres = new ArrayList<Integer>();
    private ArrayList<Integer> get_basicres = new ArrayList<Integer>();

    int get_participate;
    TextView question;
    TableLayout tableLayout;
    View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle getBundle = getArguments();
        if(getBundle != null) {
            get_question = getBundle.getStringArrayList("question");
            get_answer = getBundle.getStringArrayList("answer");
            get_selected = getBundle.getString("selected_item");
            get_participate = getBundle.getInt("participate");
            get_type = getBundle.getIntegerArrayList("type");
            get_option = (ArrayList<ArrayList<String>>)getBundle.getSerializable("choice_option");
            get_gridbegin = getBundle.getIntegerArrayList("grid_begin");
            get_gridend = getBundle.getIntegerArrayList("grid_end");
            get_gridrowcol = getBundle.getStringArrayList("grid_rowcol");
            get_qna = getBundle.getStringArrayList("survey_qna");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_graph, container, false);

        tableLayout = (TableLayout) v.findViewById(R.id.table_layout);
        question = (TextView) v.findViewById(R.id.question_text);

        for(int k=0; k<get_question.size(); k++) {
            Log.v("질문테스트", "질문" + k + " : " + get_question.get(k));

            if (!get_removlQ.contains(get_question.get(k))) {
                get_removlQ.add(get_question.get(k));
            }
        }

        TableLayout tl = (TableLayout) v.findViewById(R.id.table_layout);

        int check_count = 0;

        for(int i=0; i<get_type.size(); i++) {
            if (get_qna.get(i).contains(get_selected)) {
                if (get_type.get(i) == 2 || get_type.get(i) == 3 || get_type.get(i) == 4) {
                    // 객관식질문, 체크박스, 드롭다운인 경우

                    int arr[] = new int[get_option.get(i).size()+1];

                    for(int k=0; k<get_option.get(i).size(); k++) {
                        int find_count = 0;

                        if(get_answer.get(i).contains(get_option.get(i).get(k))) {
                            check_count++; // 선택한 전체 항목 수 카운트
                            find_count++; // 해당 단어를 찾은 경우
                        }

                        get_optionres.add(find_count);
                    }

                    int get_index = get_question.indexOf(get_selected);

                    for(int p=0; p<get_optionres.size(); p++) {
                        if(i == get_index + get_participate - 1) {
                            if(p % get_option.get(i).size() == 0) {
                                for (int k = 0; k < get_option.get(i).size(); k++) {
                                    arr[k] += get_optionres.get(p + k);
                                }
                            }
                        }
                    }

                    if(i == get_index + get_participate - 1) {
                        for (int k = 0; k < get_option.get(i).size(); k++) {
                            TableRow tr = new TableRow(getContext());
                            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            TextView textView[][] = new TextView[get_option.get(i).size()][3];

                            for (int l = 0; l < 3; l++) {
                                if (l == 0) {
                                    textView[k][l] = new TextView(getContext());
                                    textView[k][l].setText("" + (k + 1));
                                    textView[k][l].setPadding(3, 3, 3, 3);
                                    textView[k][l].setBackgroundColor(Color.WHITE);
                                    textView[k][l].setTextSize(17);
                                    textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                    tr.addView(textView[k][l]);
                                } else if (l == 1) {
                                    textView[k][l] = new TextView(getContext());
                                    textView[k][l].setText(get_option.get(i).get(k));
                                    textView[k][l].setPadding(3, 3, 3, 3);
                                    textView[k][l].setBackgroundColor(Color.WHITE);
                                    textView[k][l].setTextSize(17);
                                    textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                    tr.addView(textView[k][l]);
                                } else {
                                    double calc_resrate = (double)arr[k]/check_count;

                                    textView[k][l] = new TextView(getContext());
                                    textView[k][l].setText("" + arr[k] + " ("+ String.format("%.1f", calc_resrate) + "%)");
                                    textView[k][l].setPadding(3, 3, 3, 3);
                                    textView[k][l].setBackgroundColor(Color.WHITE);
                                    textView[k][l].setTextSize(17);
                                    textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                    tr.addView(textView[k][l]);
                                }

                                if (tr.getParent() != null) {
                                    ((ViewGroup) tr.getParent()).removeView(tr);
                                }
                            }

                            tl.addView(tr);

                            // 원 차트, 막대 차트
                            pieChart = (PieChart)v.findViewById(R.id.piechart);
                            pieChart.setVisibility(View.VISIBLE);

                            pieChart.setUsePercentValues(true);
                            pieChart.getDescription().setEnabled(false);
                            pieChart.setExtraOffsets(5,10,5,5);

                            pieChart.setDragDecelerationFrictionCoef(0.95f);
                            pieChart.setDrawHoleEnabled(false);
                            pieChart.setHoleColor(Color.WHITE);
                            pieChart.setTransparentCircleRadius(61f);
                            pieChart.setEntryLabelTextSize(20);

                            barChart = (BarChart) v.findViewById(R.id.barchart);
                            barChart.setVisibility(View.VISIBLE);

                            ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                            List<BarEntry> barentries = new ArrayList<>();
                            ArrayList<String> xasis_name = new ArrayList<String>();

                            for(int q=0; q<get_option.get(i).size(); q++) {
                                xasis_name.add(get_option.get(i).get(q));
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

                            for(int p=0; p<get_option.get(i).size(); p++) {
                                if(arr[p] != 0) {
                                    yValues.add(new PieEntry(arr[p], get_option.get(i).get(p)));
                                    barentries.add(new BarEntry(p, arr[p]));
                                }
                            }

                            Description description = new Description();
                            description.setText("총합이 100%가 아닐 수도 있습니다."); //라벨
                            description.setTextSize(10);
                            pieChart.setDescription(description);

                            pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                            PieDataSet dataSet = new PieDataSet(yValues,"Data");
                            dataSet.setSliceSpace(3f);
                            dataSet.setSelectionShift(5f);
                            dataSet.setFormSize(15);
                            dataSet.setValueTextSize(15);
                            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                            PieData piedata = new PieData((dataSet));
                            piedata.setValueTextColor(Color.YELLOW);
                            piedata.setValueTextSize(15);
                            piedata.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));

                            pieChart.setData(piedata);

                            BarDataSet set = new BarDataSet(barentries, "Data");
                            set.setValueTextSize(15);
                            set.setColors(ColorTemplate.COLORFUL_COLORS);

                            BarData bardata = new BarData(set);
                            bardata.setBarWidth(0.9f);
                            bardata.setValueFormatter(new IntegerFormatter());

                            barChart.setData(bardata);
                            barChart.setFitBars(true);
                            barChart.invalidate();
                        }
                    }
                } else if (get_type.get(i) == 5) {
                    // 직선 그리드인 경우

                    int str_index = get_removlQ.indexOf(get_selected);
                    int get_index = get_question.indexOf(get_selected);
                    int begin = get_gridbegin.get(str_index);
                    int end = get_gridend.get(str_index)+2;

                    int arr[] = new int[end-begin+2];

                    for(int h=begin; h<=end; h++) {
                        int find_count = 0;

                        if (get_answer.get(i).equals("" + h)) {
                            check_count++; // 선택한 전체 항목 수 카운트
                            find_count++; // 해당 단어를 찾은 경우
                        }

                        get_lingridres.add(find_count);
                    }

                    for(int p=0; p<get_lingridres.size(); p++) {
                        if(i == get_index + get_participate - 1) {
                            if(p % (end-begin+1) == 0) {
                                for (int k = 0; k < (end-begin+1); k++)
                                    arr[k] += get_lingridres.get(p + k);
                            }
                        }
                    }

                    if(i == get_index + get_participate - 1) {
                        for (int k = begin; k <= end; k++) {
                            TableRow tr = new TableRow(getContext());
                            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            TextView textView[][] = new TextView[end - begin + 1][3];

                            for (int l = 0; l < 3; l++) {
                                if (k == 0) {
                                    if (l == 0) {
                                        textView[k][l] = new TextView(getContext());
                                        textView[k][l].setText("" + (k + 1));
                                        textView[k][l].setPadding(3, 3, 3, 3);
                                        textView[k][l].setBackgroundColor(Color.WHITE);
                                        textView[k][l].setTextSize(17);
                                        textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                        tr.addView(textView[k][l]);
                                    } else if (l == 1) {
                                        textView[k][l] = new TextView(getContext());
                                        textView[k][l].setText("" + k) ;
                                        textView[k][l].setPadding(3, 3, 3, 3);
                                        textView[k][l].setBackgroundColor(Color.WHITE);
                                        textView[k][l].setTextSize(17);
                                        textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                        tr.addView(textView[k][l]);
                                    } else {
                                        double calc_resrate = (double)arr[k]/check_count;

                                        textView[k][l] = new TextView(getContext());
                                        textView[k][l].setText("" + arr[k] + " (" + String.format("%.1f", calc_resrate) + "%)");
                                        textView[k][l].setPadding(3, 3, 3, 3);
                                        textView[k][l].setBackgroundColor(Color.WHITE);
                                        textView[k][l].setTextSize(17);
                                        textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                        tr.addView(textView[k][l]);
                                    }

                                    if (tr.getParent() != null) {
                                        ((ViewGroup) tr.getParent()).removeView(tr);
                                    }

                                    tl.addView(tr);

                                    // 원 차트, 막대 차트
                                    pieChart = (PieChart)v.findViewById(R.id.piechart);
                                    pieChart.setVisibility(View.VISIBLE);

                                    pieChart.setUsePercentValues(true);
                                    pieChart.getDescription().setEnabled(false);
                                    pieChart.setExtraOffsets(5,10,5,5);

                                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                                    pieChart.setDrawHoleEnabled(false);
                                    pieChart.setHoleColor(Color.WHITE);
                                    pieChart.setTransparentCircleRadius(61f);
                                    pieChart.setEntryLabelTextSize(20);

                                    barChart = (BarChart) v.findViewById(R.id.barchart);
                                    barChart.setVisibility(View.VISIBLE);

                                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                                    List<BarEntry> barentries = new ArrayList<>();
                                    ArrayList<String> xasis_name = new ArrayList<String >();

                                    for(int q=begin; q<=end; q++) {
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

                                    for (int p = begin; p <= end; p++) {
                                        if(arr[p] != 0) {
                                            yValues.add(new PieEntry(arr[p], "" + p));
                                            barentries.add(new BarEntry(p, arr[p]));
                                        }
                                    }

                                    Description description = new Description();
                                    description.setText("총합이 100%가 아닐 수도 있습니다."); //라벨
                                    description.setTextSize(10);
                                    pieChart.setDescription(description);

                                    pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                                    PieDataSet dataSet = new PieDataSet(yValues,"Data");
                                    dataSet.setSliceSpace(3f);
                                    dataSet.setSelectionShift(5f);
                                    dataSet.setFormSize(15);
                                    dataSet.setValueTextSize(15);
                                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                                    PieData piedata = new PieData((dataSet));
                                    piedata.setValueTextColor(Color.YELLOW);
                                    piedata.setValueTextSize(15);
                                    piedata.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));

                                    pieChart.setData(piedata);

                                    BarDataSet set = new BarDataSet(barentries, "Data");

                                    set.setValueTextSize(15);
                                    set.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData bardata = new BarData(set);
                                    bardata.setBarWidth(0.9f);
                                    bardata.setValueFormatter(new IntegerFormatter());

                                    barChart.setData(bardata);
                                    barChart.setFitBars(true);
                                    barChart.invalidate();
                                } else {
                                    if (l == 0) {
                                        textView[k - begin][l] = new TextView(getContext());
                                        textView[k - begin][l].setText("" + (k - begin + 1));
                                        textView[k - begin][l].setPadding(3, 3, 3, 3);
                                        textView[k - begin][l].setBackgroundColor(Color.WHITE);
                                        textView[k - begin][l].setTextSize(17);
                                        textView[k - begin][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                        tr.addView(textView[k - begin][l]);
                                    } else if (l == 1) {
                                        textView[k - begin][l] = new TextView(getContext());
                                        textView[k - begin][l].setText("" + k);
                                        textView[k - begin][l].setPadding(3, 3, 3, 3);
                                        textView[k - begin][l].setBackgroundColor(Color.WHITE);
                                        textView[k - begin][l].setTextSize(17);
                                        textView[k - begin][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                        tr.addView(textView[k - begin][l]);
                                    } else {
                                        double calc_resrate = (double)arr[k-begin]/check_count;

                                        textView[k - begin][l] = new TextView(getContext());
                                        textView[k - begin][l].setText("" + arr[k-begin] + " (" + String.format("%.1f", calc_resrate) + "%)");
                                        textView[k - begin][l].setPadding(3, 3, 3, 3);
                                        textView[k - begin][l].setBackgroundColor(Color.WHITE);
                                        textView[k - begin][l].setTextSize(17);
                                        textView[k - begin][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                        tr.addView(textView[k - begin][l]);
                                    }

                                    if (tr.getParent() != null) {
                                        ((ViewGroup) tr.getParent()).removeView(tr);
                                    }

                                    tl.addView(tr);

                                    // 원 차트, 막대 차트
                                    pieChart = (PieChart)v.findViewById(R.id.piechart);
                                    pieChart.setVisibility(View.VISIBLE);

                                    pieChart.setUsePercentValues(true);
                                    pieChart.getDescription().setEnabled(false);
                                    pieChart.setExtraOffsets(5,10,5,5);

                                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                                    pieChart.setDrawHoleEnabled(false);
                                    pieChart.setHoleColor(Color.WHITE);
                                    pieChart.setTransparentCircleRadius(61f);
                                    pieChart.setEntryLabelTextSize(20);

                                    barChart = (BarChart) v.findViewById(R.id.barchart);
                                    barChart.setVisibility(View.VISIBLE);

                                    ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                                    List<BarEntry> barentries = new ArrayList<>();
                                    ArrayList<String> xasis_name = new ArrayList<String>();

                                    for(int q=begin; q<=end; q++) {
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

                                    for (int p = begin; p <= end; p++) {
                                        if(arr[p-begin] != 0) {
                                            yValues.add(new PieEntry(arr[p - begin], "" + p));
                                            barentries.add(new BarEntry(p, arr[p-begin]));
                                        }
                                    }

                                    Description description = new Description();
                                    description.setText("총합이 100%가 아닐 수도 있습니다."); //라벨
                                    description.setTextSize(10);
                                    pieChart.setDescription(description);

                                    pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                                    PieDataSet dataSet = new PieDataSet(yValues,"Data");
                                    dataSet.setSliceSpace(3f);
                                    dataSet.setSelectionShift(5f);
                                    dataSet.setFormSize(15);
                                    dataSet.setValueTextSize(15);
                                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                                    PieData piedata = new PieData((dataSet));
                                    piedata.setValueTextColor(Color.YELLOW);
                                    piedata.setValueTextSize(15);
                                    piedata.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));

                                    pieChart.setData(piedata);

                                    BarDataSet set = new BarDataSet(barentries, "Data");

                                    set.setValueTextSize(15);
                                    set.setColors(ColorTemplate.COLORFUL_COLORS);

                                    BarData bardata = new BarData(set);
                                    bardata.setBarWidth(0.9f);
                                    bardata.setValueFormatter(new IntegerFormatter());

                                    barChart.setData(bardata);
                                    barChart.setFitBars(true);
                                    barChart.invalidate();
                                }
                            }
                        }
                    }
                } else if(get_type.get(i) == 6 || get_type.get(i) == 7) {
                    // 객관식 그리드, 체크박스 그리드인 경우

                    int arr[] = new int[get_gridrowcol.size()+1];
                    int get_index = get_question.indexOf(get_selected);

                    for(int k=0; k<get_gridrowcol.size(); k++) {
                        int find_count = 0;

                        if(get_answer.get(i).contains(get_gridrowcol.get(k))) {
                            check_count++;
                            find_count++;
                        }

                        get_chkgridres.add(find_count);
                    }

                    for(int p=0; p<get_chkgridres.size(); p++) {
                        if(i == get_index + get_participate - 1) {
                            if(p % get_gridrowcol.size() == 0) {
                                for(int k=0; k<get_gridrowcol.size(); k++)
                                    arr[k] += get_chkgridres.get(p + k);
                            }
                        }
                    }

                    if(i == get_index + get_participate - 1) {
                        for (int k = 0; k < get_gridrowcol.size(); k++) {
                            TableRow tr = new TableRow(getContext());
                            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            TextView textView[][] = new TextView[get_gridrowcol.size()][3];

                            for (int l = 0; l < 3; l++) {
                                if (l == 0) {
                                    textView[k][l] = new TextView(getContext());
                                    textView[k][l].setText("" + (k + 1));
                                    textView[k][l].setPadding(3, 3, 3, 3);
                                    textView[k][l].setBackgroundColor(Color.WHITE);
                                    textView[k][l].setTextSize(17);
                                    textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                    tr.addView(textView[k][l]);
                                } else if (l == 1) {
                                    textView[k][l] = new TextView(getContext());
                                    textView[k][l].setText(get_gridrowcol.get(k));
                                    textView[k][l].setPadding(3, 3, 3, 3);
                                    textView[k][l].setBackgroundColor(Color.WHITE);
                                    textView[k][l].setTextSize(17);
                                    textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                    tr.addView(textView[k][l]);
                                } else {
                                    double calc_resrate = (double)arr[k]/check_count;

                                    textView[k][l] = new TextView(getContext());
                                    textView[k][l].setText("" + arr[k] + " (" + String.format("%.1f", calc_resrate) + "%)");
                                    textView[k][l].setPadding(3, 3, 3, 3);
                                    textView[k][l].setBackgroundColor(Color.WHITE);
                                    textView[k][l].setTextSize(17);
                                    textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                    tr.addView(textView[k][l]);
                                }

                                if (tr.getParent() != null) {
                                    ((ViewGroup) tr.getParent()).removeView(tr);
                                }
                            }

                            tl.addView(tr);

                            // 원 차트, 막대 차트
                            pieChart = (PieChart)v.findViewById(R.id.piechart);
                            pieChart.setVisibility(View.VISIBLE);

                            pieChart.setUsePercentValues(true);
                            pieChart.getDescription().setEnabled(false);
                            pieChart.setExtraOffsets(5,10,5,5);

                            pieChart.setDragDecelerationFrictionCoef(0.95f);
                            pieChart.setDrawHoleEnabled(false);
                            pieChart.setHoleColor(Color.WHITE);
                            pieChart.setTransparentCircleRadius(61f);
                            pieChart.setEntryLabelTextSize(20);

                            barChart = (BarChart) v.findViewById(R.id.barchart);
                            barChart.setVisibility(View.VISIBLE);

                            ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                            List<BarEntry> barentries = new ArrayList<>();
                            ArrayList<String> xasis_name = new ArrayList<String>();

                            for(int q=0; q<get_gridrowcol.size(); q++) {
                                xasis_name.add(get_gridrowcol.get(q));
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

                            for (int p=0; p<get_gridrowcol.size(); p++) {
                                if(arr[p] != 0) {
                                    yValues.add(new PieEntry(arr[p], get_gridrowcol.get(p)));
                                    barentries.add(new BarEntry(p, arr[p]));
                                }
                            }

                            Description description = new Description();
                            description.setText("총합이 100%가 아닐 수도 있습니다."); //라벨
                            description.setTextSize(10);
                            pieChart.setDescription(description);

                            pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                            PieDataSet dataSet = new PieDataSet(yValues,"Data");
                            dataSet.setSliceSpace(3f);
                            dataSet.setSelectionShift(5f);
                            dataSet.setFormSize(15);
                            dataSet.setValueTextSize(15);
                            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                            PieData piedata = new PieData((dataSet));
                            piedata.setValueTextColor(Color.YELLOW);
                            piedata.setValueTextSize(15);
                            piedata.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));

                            pieChart.setData(piedata);

                            BarDataSet set = new BarDataSet(barentries, "Data");

                            set.setValueTextSize(15);
                            set.setColors(ColorTemplate.COLORFUL_COLORS);

                            BarData bardata = new BarData(set);
                            bardata.setBarWidth(0.9f);
                            bardata.setValueFormatter(new IntegerFormatter());

                            barChart.setData(bardata);
                            barChart.setFitBars(true);
                            barChart.invalidate();
                        }
                    }
                } else {
                    // 단답형, 장문형, 날짜 등 나머지인 경우

                    int str_index = get_question.indexOf(get_selected);

                    for(int h=str_index; h<str_index+get_participate; h++) {
                        if (!get_removerlap.contains(get_answer.get(h))) {
                            get_removerlap.add(get_answer.get(h));
                        }
                    }

                    int arr[] = new int[get_removerlap.size()+1];

                    for(int p=0; p<get_removerlap.size(); p++) {
                        int find_count = 0;

                        if(get_answer.get(i).contains(get_removerlap.get(p))) {
                            check_count++;
                            find_count++;
                        }

                        get_basicres.add(find_count);
                    }

                    for(int q=0; q<get_basicres.size(); q++) {
                        if(i == str_index + get_participate - 1) {
                            if(q % get_removerlap.size() == 0) {
                                for(int r=0; r<get_removerlap.size(); r++)
                                    arr[r] += get_basicres.get(q + r);
                            }
                        }
                    }

                    if (i == str_index + get_participate - 1) {
                        for(int k=0; k<get_removerlap.size(); k++) {
                            TableRow tr = new TableRow(getContext());
                            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                            TextView textView[][] = new TextView[get_removerlap.size()][3];

                            for (int l = 0; l < 3; l++) {
                                if (l == 0) {
                                    textView[k][l] = new TextView(getContext());
                                    textView[k][l].setText("" + (k + 1));
                                    textView[k][l].setPadding(3, 3, 3, 3);
                                    textView[k][l].setBackgroundColor(Color.WHITE);
                                    textView[k][l].setTextSize(17);
                                    textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                    tr.addView(textView[k][l]);
                                } else if (l == 1) {
                                    textView[k][l] = new TextView(getContext());
                                    textView[k][l].setText("" + get_removerlap.get(k));
                                    textView[k][l].setPadding(3, 3, 3, 3);
                                    textView[k][l].setBackgroundColor(Color.WHITE);
                                    textView[k][l].setTextSize(17);
                                    textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                    tr.addView(textView[k][l]);
                                } else {
                                    double calc_resrate = (double)arr[k]/check_count;

                                    textView[k][l] = new TextView(getContext());
                                    textView[k][l].setText("" + arr[k] + " (" + String.format("%.1f", calc_resrate) + "%)");
                                    textView[k][l].setPadding(3, 3, 3, 3);
                                    textView[k][l].setBackgroundColor(Color.WHITE);
                                    textView[k][l].setTextSize(17);
                                    textView[k][l].setGravity(Gravity.CENTER_HORIZONTAL);

                                    tr.addView(textView[k][l]);
                                }

                                if (tr.getParent() != null) {
                                    ((ViewGroup) tr.getParent()).removeView(tr);
                                }

                                tl.addView(tr);

                                // 원 차트, 막대 차트
                                pieChart = (PieChart)v.findViewById(R.id.piechart);
                                pieChart.setVisibility(View.VISIBLE);

                                pieChart.setUsePercentValues(true);
                                pieChart.getDescription().setEnabled(false);
                                pieChart.setExtraOffsets(5,10,5,5);

                                pieChart.setDragDecelerationFrictionCoef(0.95f);
                                pieChart.setDrawHoleEnabled(false);
                                pieChart.setHoleColor(Color.WHITE);
                                pieChart.setTransparentCircleRadius(61f);
                                pieChart.setEntryLabelTextSize(20);

                                barChart = (BarChart) v.findViewById(R.id.barchart);
                                barChart.setVisibility(View.VISIBLE);

                                ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
                                List<BarEntry> barentries = new ArrayList<>();
                                ArrayList<String> xasis_name = new ArrayList<String>();

                                for(int q=0; q<get_removerlap.size(); q++) {
                                    xasis_name.add(get_removerlap.get(q));
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

                                for(int p=0; p<get_removerlap.size(); p++) {
                                    if(arr[p] != 0) {
                                        yValues.add(new PieEntry(arr[p], get_removerlap.get(p)));
                                        barentries.add(new BarEntry(p, arr[p]));
                                    }
                                }

                                Description description = new Description();
                                description.setText("총합이 100%가 아닐 수도 있습니다."); //라벨
                                description.setTextSize(10);
                                pieChart.setDescription(description);

                                pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

                                PieDataSet dataSet = new PieDataSet(yValues,"Data");
                                dataSet.setSliceSpace(3f);
                                dataSet.setSelectionShift(5f);
                                dataSet.setFormSize(15);
                                dataSet.setValueTextSize(15);
                                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                                PieData piedata = new PieData((dataSet));
                                piedata.setValueTextColor(Color.YELLOW);
                                piedata.setValueTextSize(15);
                                piedata.setValueFormatter(new DecimalRemover(new DecimalFormat("###,###,###")));

                                pieChart.setData(piedata);

                                BarDataSet set = new BarDataSet(barentries, "Data");

                                set.setValueTextSize(15);
                                set.setColors(ColorTemplate.COLORFUL_COLORS);

                                BarData bardata = new BarData(set);
                                bardata.setBarWidth(0.9f);
                                bardata.setValueFormatter(new IntegerFormatter());

                                barChart.setData(bardata);
                                barChart.setFitBars(true);
                                barChart.invalidate();
                            }
                        }
                    }
                }
            }
        }

        return v;
    }

    public static GraphFragment newInstance(String selected, int participate_num, ArrayList<String> survey_question, ArrayList<String> survey_answer, ArrayList<Integer> survey_type,
                                            ArrayList<ArrayList<String>> choice_option, ArrayList<Integer> grid_begin, ArrayList<Integer> grid_end, ArrayList<String> grid_rowcol, ArrayList<String> survey_qna) {
        GraphFragment gf = new GraphFragment();
        Bundle putBundle = new Bundle();
        putBundle.putStringArrayList("question", survey_question);
        putBundle.putStringArrayList("answer", survey_answer);
        putBundle.putString("selected_item", selected);
        putBundle.putInt("participate", participate_num);
        putBundle.putIntegerArrayList("type", survey_type);
        putBundle.putSerializable("choice_option", choice_option);
        putBundle.putIntegerArrayList("grid_begin", grid_begin);
        putBundle.putIntegerArrayList("grid_end", grid_end);
        putBundle.putStringArrayList("grid_rowcol", grid_rowcol);
        putBundle.putStringArrayList("survey_qna", survey_qna);

        gf.setArguments(putBundle);

        return gf;
    }
}
