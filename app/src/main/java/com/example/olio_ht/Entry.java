package com.example.olio_ht;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;


public abstract class Entry implements Serializable {

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

}

