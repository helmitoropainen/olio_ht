package com.example.olio_ht;

import java.util.ArrayList;

public abstract class Entry {

    String date;
    double sum;
    String username;

    public Entry() {
    }

    public String getDate() { return date; }
    public double getSum() { return sum; }
    public String getUsername() { return username; }

    public void setDate( String d ) { date = d; }
    public void setSum( double s ) { sum = s; }
    public void setUsername( String u ) { username = u; }

    // jos sleep entry tarvii näit, kalorrientryt ei tarvi
    public void clearTodaysEntries() {
        sum = 0;
    }

    public void addToTodaysSum(double x) {
        sum = sum + x;
    }

}

