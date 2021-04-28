package com.example.olio_ht;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AnalyticsFragment extends Fragment {


    LineChart caloriesChart ;
    BarChart sleepChart;
    View view;
    private static final String TAG = "AnalyticsFragment" ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analytics, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        caloriesChart = (LineChart) this.view.findViewById(R.id.lineChartCalorieChart);
        sleepChart = (BarChart) this.view.findViewById(R.id.lineChartSleepChart);

        Analyses analyse = new Analyses(getActivity().getBaseContext());
        analyse.readCSV();

        ArrayList<Entry> calgained = analyse.getCalorieIntake() ;
        ArrayList<Entry> calburned = analyse.getCalorieLoss() ;
        ArrayList<Entry> caldifference = analyse.getCaloriesDifference() ;
        ArrayList<BarEntry> sleep = analyse.getSleptHours() ;

        // ArrayList<String> xAXES = new ArrayList<>() ;

        long calorieGoal = analyse.getCalorieGoal() ;
        double sleepGoal = analyse.getSleepGoal() ; // minuutteina

        ArrayList<Entry> calorieG = new ArrayList<>() ;
        ArrayList<Entry> sleepG = new ArrayList<>();

        String[] xaxes = new String[5] ;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.") ;
        String date = sdf.format(new Date()) ;
        Calendar c = Calendar.getInstance() ;
        try {
            c.setTime(sdf.parse(date)) ;
            c.add(Calendar.DAY_OF_MONTH, -4) ;
            date = sdf.format(c.getTime()) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i=0; i<5;i++) {
            xaxes[i] = date ;
            try {
                c.setTime(sdf.parse(date)) ;
                c.add(Calendar.DAY_OF_MONTH, 1) ;
                date = sdf.format(c.getTime()) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Create a chart and add to it datapoints based on burnt calories and intake
        LineDataSet LineDataSetGained  = new LineDataSet(calgained, "Intake");
        LineDataSetGained.setDrawCircles(true) ;
        LineDataSetGained.setColor(Color.rgb(249, 141, 131)) ;

        LineDataSet LineDataSetBurnt  = new LineDataSet(calburned, "Burnt");
        LineDataSetBurnt.setDrawCircles(true) ;
        LineDataSetBurnt.setColor(Color.rgb(2, 218, 197)) ;

        LineDataSet LineDataSetDiff  = new LineDataSet(caldifference, "Difference");
        LineDataSetDiff.setDrawCircles(false) ;
        LineDataSetDiff.setColor(Color.rgb(229, 172, 240)) ;

        ArrayList<ILineDataSet> ldsCalorie = new ArrayList<ILineDataSet>() ;
        ldsCalorie.add(LineDataSetGained) ;
        ldsCalorie.add(LineDataSetBurnt) ;
        ldsCalorie.add(LineDataSetDiff) ;

        System.out.println("#####################################") ;
        System.out.println(ldsCalorie) ;
        System.out.println(xaxes[0] + "   " + xaxes[1] + "   " + xaxes[2] + "   " + xaxes[3] + "   " + xaxes[4]) ;
        caloriesChart.setData(new LineData(xaxes, ldsCalorie)) ;

        // Create a new chart and add to it datapoints based on hours slept

        BarDataSet BarDataSetSleep  = new BarDataSet(sleep, "Hours slept");
        BarDataSetSleep.setColor(Color.rgb(151, 180, 212)) ;

        ArrayList ldsSleep = new ArrayList() ;
        ldsSleep.add(BarDataSetSleep) ;

        sleepChart.setData(new BarData(xaxes, ldsSleep)) ;

    }

}

