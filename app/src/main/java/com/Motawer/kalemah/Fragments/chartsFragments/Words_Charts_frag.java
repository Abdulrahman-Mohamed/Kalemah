package com.Motawer.kalemah.Fragments.chartsFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Motawer.kalemah.Models.BarGraphModel;
import com.Motawer.kalemah.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Words_Charts_frag extends Fragment {


    BarChart barChart;
    FirebaseAuth firebaseAuth;


    View view;
    ArrayList<BarGraphModel> barGraphModels = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return view = inflater.inflate(R.layout.fragment_words__charts_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        InitializeView();


    }

    private void InitializeView() {
        barChart = view.findViewById(R.id.bat_chart_words);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setVisibleXRangeMinimum(1);
        barChart.getXAxis().setAxisMaximum(11);
        barChart.getXAxis().setAxisMinimum(1);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.getAxisLeft().setDrawGridLines(true);
        barChart.getAxisLeft().setDrawLabels(true);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawLabels(true);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(true);
        barChart.getAxisRight().setDrawAxisLine(false);
        firebaseAuth = FirebaseAuth.getInstance();

        loadData();

    }

    private void loadData() {
        String uid = firebaseAuth.getUid();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String date1 = dateFormat.format(date);
        String[] words = date1.split("/");//splits the string based on whitespace
        int year = Integer.parseInt(words[0]);
//        int month = Integer.parseInt(words[1]);
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(uid, view.getContext().MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(String.valueOf(year), null);
        Type type = new TypeToken<ArrayList<BarGraphModel>>() {
        }.getType();
        barGraphModels = gson.fromJson(json, type);
        if (barGraphModels == null) {
            barGraphModels = new ArrayList<>();
        }
        setData();
    }

    private void setData() {
        int max=0;
        ArrayList<BarEntry> entries = new ArrayList<>();
        HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();
        int idx = 0;

        if (barGraphModels.size() != 0) {
            int counter = 0;
            for (int i = 0; i < barGraphModels.size(); i++) {
                //for (int j = i+1; j < barGraphModels.size(); j++){
                if (barGraphModels.get(i).getMonth() != 0)
                    if (!hmap.containsKey(barGraphModels.get(i).getMonth())) {
                        idx = barGraphModels.get(i).getMonth();
                        counter = 0;
                        hmap.put(barGraphModels.get(i).getMonth(), counter + 1);
                    } else {
                        if (hmap.get(idx) != null) {
                            counter = hmap.get(idx) + 1;
                            hmap.put(barGraphModels.get(i).getMonth(), counter);

                        }
                    }
            }
            for (Map.Entry me : hmap.entrySet()) {
                int x = Integer.parseInt(me.getKey().toString());
                int y = Integer.parseInt(me.getValue().toString());
                if (max<y)
                {max=y;}
                entries.add(new BarEntry(x, y));
            }
            BarDataSet barDataSet = new BarDataSet(entries, "Words Inserted");
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            BarData barData = new BarData(barDataSet);
            barData.setBarWidth(0.9f);
            barChart.setData(barData);
            YAxis yAxisLeft=barChart.getAxisLeft();
            YAxis yAxisRight=barChart.getAxisRight();
            yAxisRight.setEnabled(false);
            barChart.setVisibleYRange(0,30, YAxis.AxisDependency.LEFT);
            barChart.getDescription().setEnabled(false);
            barChart.animateY(1500);
            barChart.animateX(1500);
//            barChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
//                @Override
//                public String getFormattedValue(float value) {
//                    return String.valueOf((int) Math.floor(value));
//                }
//            });
            yAxisLeft.setAxisMaximum(max);



            String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormater(months));

        } else {
            barChart.setNoDataText("No Current Data");
        }

        //}
    }

}

class MyXAxisValueFormater extends ValueFormatter {
    private final String[] mValues;

    public MyXAxisValueFormater(String[] mValues) {
        this.mValues = mValues;
    }

    @Override
    public String getFormattedValue(float value) {
        return mValues[(int) value];
    }
}