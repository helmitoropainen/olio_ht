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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


public class AnalyticsFragment extends Fragment {

    LineChart caloriesChart ;
    BarChart sleepChart;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analytics, container, false);
        return view;
    }

    // When the analytics fragment is opened, array lists containing data required for the graphs
    // are retrieved form the Analyses class. The graphs are defined and the data is added to them.

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        caloriesChart = (LineChart) this.view.findViewById(R.id.lineChartCalorieChart);
        sleepChart = (BarChart) this.view.findViewById(R.id.lineChartSleepChart);

        Analyses analyse = new Analyses(getActivity().getBaseContext());
        analyse.readCSV();

        ArrayList<Entry> calgained = analyse.getCalorieIntake() ;
        ArrayList<Entry> calburned = analyse.getCalorieLoss() ;
        ArrayList<Entry> caldifference = analyse.getCaloriesDifference() ;
        ArrayList<BarEntry> sleep = analyse.getSleptHours() ;

        ArrayList<Entry> calorieGoal = analyse.getCalorieGoal() ;
        ArrayList<BarEntry> sleepGoal = analyse.getSleepGoal() ;

        String[] xaxes = analyse.createXAxisData() ;

        LineDataSet LineDataSetGained  = new LineDataSet(calgained, "Intake");
        LineDataSetGained.setDrawCircles(true) ;
        LineDataSetGained.setColor(Color.rgb(249, 141, 131)) ;
        LineDataSetGained.setLineWidth(2);

        LineDataSet LineDataSetBurnt  = new LineDataSet(calburned, "Burnt");
        LineDataSetBurnt.setDrawCircles(true) ;
        LineDataSetBurnt.setColor(Color.rgb(2, 218, 197)) ;
        LineDataSetBurnt.setLineWidth(2);

        LineDataSet LineDataSetDiff  = new LineDataSet(caldifference, "Difference");
        LineDataSetDiff.setDrawCircles(false) ;
        LineDataSetDiff.setColor(Color.rgb(229, 172, 240)) ;

        LineDataSet LineDataSetCalGoal = new LineDataSet(calorieGoal, "Your goal");
        LineDataSetCalGoal.setDrawCircles(false) ;
        LineDataSetCalGoal.enableDashedLine(10,5,1);
        LineDataSetCalGoal.setValueTextSize(0);
        LineDataSetCalGoal.setLineWidth(2);
        LineDataSetCalGoal.setColor(Color.rgb(207,207,207)) ;

        ArrayList<ILineDataSet> ldsCalorie = new ArrayList<ILineDataSet>() ;
        ldsCalorie.add(LineDataSetGained) ;
        ldsCalorie.add(LineDataSetBurnt) ;
        ldsCalorie.add(LineDataSetDiff) ;
        ldsCalorie.add(LineDataSetCalGoal) ;

        caloriesChart.setData(new LineData(xaxes, ldsCalorie)) ;

        BarDataSet BarDataSetSleep  = new BarDataSet(sleep, "Hours slept");
        BarDataSetSleep.setColor(Color.rgb(151, 180, 212)) ;

        BarDataSet BarDataSetSleepGoal  = new BarDataSet(sleepGoal, "Your goal");
        BarDataSetSleepGoal.setColor(Color.rgb(207,207,207)) ;
        BarDataSetSleepGoal.setValueTextSize(0);

        ArrayList<IBarDataSet> ldsSleep = new ArrayList() ;
        ldsSleep.add(BarDataSetSleep) ;
        ldsSleep.add(BarDataSetSleepGoal) ;

        sleepChart.setData(new BarData(xaxes, ldsSleep)) ;

    }


}

