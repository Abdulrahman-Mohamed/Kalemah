package com.Motawer.kalemah.Fragments.chartsFragments;

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Motawer.kalemah.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Tests_Charts_frag extends Fragment {
    View view;
    LineChart lineChart;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int year;
    int month;
    int day;
    ArrayList<Entry> entries;
    ArrayList<Entry> entries1;

    ArrayList<Integer> GREStats = new ArrayList<>();
    ArrayList<Integer> LocalStats = new ArrayList<>();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static Tests_Charts_frag newInstance(String param1, String param2) {
        Tests_Charts_frag fragment = new Tests_Charts_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_tests__charts_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        InitializeView();


    }

    private void InitializeView() {
        lineChart = view.findViewById(R.id.chart_test);
        getStat();
    }

    private void getStat() {
        DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");
        Date date = new Date();
        String date1 = String.valueOf(dateFormat.format(date));
        String[] words = date1.split("/");//splits the string based on whitespace
        year = Integer.parseInt(words[0]);
        month = Integer.parseInt(words[1]);
        day = Integer.parseInt(words[2]);
        myRef.child("Users")
                .child(firebaseAuth.getCurrentUser()
                        .getUid()).child("stat").child("GRE_Test").child(String.valueOf(year))
                .child(String.valueOf(month)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        GREStats.add(dataSnapshot.getValue(Integer.class));
                    entries=new ArrayList<>();
                    if (GREStats.size()!=0)
                    {
                        for (int i = 0; i < 30; i++) {
                            entries.add(new Entry(i + 1, GREStats.get(i)));
                        }
                    }
                    dataValues1();

                } else {
                    dataValues1();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void dataValues1() {
        myRef.child("Users")
                .child(firebaseAuth.getCurrentUser()
                        .getUid()).child("stat").child("Local_Test").child(String.valueOf(year))
                .child(String.valueOf(month)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        LocalStats.add(dataSnapshot.getValue(Integer.class));


                    entries1 = new ArrayList<>();
                    if (LocalStats.size()!=0)
                    for (int i = 0; i < 30; i++) {
                        entries1.add(new Entry(i + 1, LocalStats.get(i)));
                    }
                    System.out.println(entries1.size());
                    LineInitialize();
                } else {
                    entries1=new ArrayList<>();
                    LineInitialize();


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void LineInitialize() {
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        LineDataSet lineDataSet2;
        if (entries.size()!=0 ){
         LineDataSet lineDataSet1 = new LineDataSet(entries, "GRE");
            lineDataSet1.setLineWidth(2);
            lineDataSet1.setColor(R.color.line1);
            lineDataSet1.setFillColor(R.color.line3);
            lineDataSet1.setDrawCircles(false);
            lineDataSet1.setDrawValues(false);
            lineDataSet1.setDrawFilled(true);
            lineDataSet1.setFormLineWidth(1f);
            lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet1.setCubicIntensity(0.2f);
            lineDataSet1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            lineDataSet1.setFormSize(15.f);
            lineDataSets.add(lineDataSet1);
        }
        if (entries1.size()!=0){
         lineDataSet2 = new LineDataSet(entries1, "Local");
            lineDataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet2.setCubicIntensity(0.2f);
            lineDataSet2.setLineWidth(2);
            lineDataSet2.setDrawValues(false);
            lineDataSet2.setDrawCircles(false);
            lineDataSet2.setColor(R.color.line2);
            lineDataSets.add(lineDataSet2);}



if(lineDataSets.size()!=0){
        LineData data = new LineData(lineDataSets);
        lineChart.setData(data);
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisLeft().setDrawLabels(true);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawLabels(true);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.animateX(1000);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawLabels(true);
        lineChart.getAxisRight().setDrawAxisLine(false);
       // lineChart.setVisibleXRange(5,30);
        lineChart.setVisibleYRange(0,6, YAxis.AxisDependency.LEFT);
        lineChart.getDescription().setEnabled(false);
        lineChart.invalidate();
    }
else
    {
        lineChart.setNoDataText("No Current Data");

    }
    }


}