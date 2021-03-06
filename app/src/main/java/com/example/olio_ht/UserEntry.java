package com.example.olio_ht;

import java.io.Serializable;

// Class is serializable so objects can be sent between activities
public class UserEntry implements Serializable {

    String date;
    double sum;
    String username;

    public UserEntry() { }

    public String getDate() { return date; }
    public double getSum() { return sum; }
    public String getUsername() { return username; }

    public void setDate( String d ) { date = d; }
    public void setSum( double s ) { sum = s; }
    public void setUsername( String u ) { username = u; }

}
