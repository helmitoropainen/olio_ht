package com.example.olio_ht;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AnalyticsFragment extends Fragment {

    LineChart caloriesChart, sleepChart;
    View view;
    private static final String TAG = "AnalyticsFragment" ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analytics, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        caloriesChart = (LineChart) this.view.findViewById(R.id.lineChartCalorieChart);
        sleepChart = (LineChart) this.view.findViewById(R.id.lineChartSleepChart);

        Analyses analyse = new Analyses(getActivity().getBaseContext());
        analyse.readCSV();

        ArrayList<Entry> calgained = analyse.getCalorieIntake() ;
        ArrayList<Entry> calburned = analyse.getCalorieLoss() ;
        ArrayList<Entry> sleep = analyse.getSleptHours() ;

        ArrayList<String> xAXES = new ArrayList<>() ;

        long calorieGoal = analyse.getCalorieGoal() ;
        double sleepGoal = analyse.getSleepGoal() ;

        ArrayList<Entry> calorieG = new ArrayList<>() ;
        ArrayList<Entry> sleepG = new ArrayList<>();

        double x = 0;
        int datapoints = 5 ;

        String[] xaxes = new String[5] ;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.M.") ;
        String date = sdf.format(new Date()) ;
        Calendar c = Calendar.getInstance() ;

        for (int i=0; i<5;i++) {
            xaxes[i] = date ;


            try {
                c.setTime(sdf.parse(date)) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DAY_OF_MONTH, -1) ;
            date = sdf.format(c.getTime()) ;
        }

        // Create a chart and add to it datapoints based on burnt calories and intake
        ArrayList<ILineDataSet> ldsCalorie = new ArrayList<>() ;
        LineDataSet LineDataSetGained  = new LineDataSet(calgained, "intake");
        LineDataSetGained.setDrawCircles(true) ;
        LineDataSetGained.setColor(Color.rgb(239, 108, 0)) ;

        LineDataSet LineDataSetBurnt  = new LineDataSet(calburned, "burnt");
        LineDataSetBurnt.setDrawCircles(true) ;
        LineDataSetBurnt.setColor(Color.rgb(2, 218, 197)) ;

        ldsCalorie.add(LineDataSetGained) ;
        ldsCalorie.add(LineDataSetBurnt) ;

        caloriesChart.setData(new LineData(xaxes, ldsCalorie)) ;

        // Create a new chart and add to it datapoints based on hours slept
        ArrayList<ILineDataSet> ldsSleep = new ArrayList<>() ;
        LineDataSet LineDataSetSleep  = new LineDataSet(sleep, "Hours slept");
        LineDataSetSleep.setDrawCircles(true) ;
        LineDataSetSleep.setColor(Color.rgb(151, 180, 212)) ;

        ldsSleep.add(LineDataSetSleep) ;

        sleepChart.setData(new LineData(xaxes, ldsSleep)) ;

    }

}

