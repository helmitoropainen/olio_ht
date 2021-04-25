package com.example.olio_ht;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class AnalyticsFragment extends Fragment {

    LineChart caloriesChart;
    View view;
    String filename ;
    private static final String TAG = "AnalyticsFragment" ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analytics, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        caloriesChart = (LineChart) this.view.findViewById(R.id.lineChartCalorieChart);

        ArrayList<String> xAXES = new ArrayList<>() ;
        ArrayList<Entry> yAXESSin = new ArrayList<>() ;
        ArrayList<Entry> yAXESCos = new ArrayList<>() ;
        double x = 0;
        int datapoints = 1000 ;
        for (int i=0;i<datapoints;i++) {
            float sinfunction = Float.parseFloat(String.valueOf((Math.sin(x)))) ;
            float cosfunction = Float.parseFloat(String.valueOf((Math.cos(x)))) ;
            x = x+0.1 ;
            yAXESSin.add(new Entry(sinfunction, i));
            yAXESCos.add(new Entry(cosfunction, i));
            xAXES.add(i, String.valueOf(x));
        }
        String[] xaxes = new String[xAXES.size()] ;
        for (int i=0; i<xAXES.size();i++) {
            xaxes[i] = xAXES.get(i).toString() ;
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>() ;
        LineDataSet LineDataSet1  = new LineDataSet(yAXESCos, "intake");
        LineDataSet1.setDrawCircles(false) ;
        LineDataSet1.setColor(Color.rgb(251, 226, 127)) ;

        LineDataSet LineDataSet2  = new LineDataSet(yAXESSin, "burnt");
        LineDataSet2.setDrawCircles(false) ;
        LineDataSet2.setColor(Color.rgb(2, 218, 197)) ;

        lineDataSets.add(LineDataSet1) ;
        lineDataSets.add(LineDataSet2) ;

        caloriesChart.setData(new LineData(xaxes, lineDataSets)) ;
        caloriesChart.setVisibleXRangeMaximum(65f);
    }

    public void setFilename(String f) { filename = f; }
}

