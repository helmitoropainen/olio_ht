package com.example.olio_ht;

import android.widget.Toast;

public class sleepEntry {

    int h1=0, m1=0, h2=0, m2=0, slepth=0, sleptmin=0, mindifference=0, readiness=0, goal=8;
    double slepttime=0;
    String resultText, adviceText ;

    public sleepEntry(int hour1, int hour2, int min1, int min2) {
        h1 = hour1 ;
        h2 = hour2 ;
        m1 = min1 ;
        m2 = min2 ;
    }

    public int calculateTime() {
        if (h1>12) {
            m1 = 24*60 - 60*h1 - m1 ;
            if (h2>h1) {
                m2 = 24*60 - h2*60 - m2 ;
                mindifference = m2 - m1 ;
            } else {
                m2 = h2*60 + m2 ;
                mindifference = m2 + m1 ;
            }
        } else if (h1<12) {
            m1 = 60*h1 + m1 ;
            m2 = h2*60 + m2 ;
            mindifference = m2 - m1 ;
        }

        slepttime = (double) mindifference/60;

        return mindifference ;
    }

    public String getHoursAndMinsText(int mins) {
        slepth = (int) mins/60 ;
        sleptmin = mins - slepth*60 ;

        if (sleptmin == 0) {
            resultText = "You slept\n"+slepth+" hours" ;
        } else if (slepth == 0) {
            resultText = "You slept\n"+sleptmin+" hours" ;
        }
        else {
            resultText = "You slept\n" + slepth + " hours and " + sleptmin + " minutes" ;
        }

        return resultText ;
    }

    public int getReadiness() {
        readiness = (int) ((slepttime/goal) * 100) ;
        return readiness ;
    }

    public String getAdvice(int readiness) {
        if (readiness<30) {
            adviceText = "Take it easy on yourself today and try to sleep early!" ;
        } else if (readiness<50) {
            adviceText = "Remember to stay hydrated and eat nutritious food. You can get through this day!" ;
        } else if (readiness<75) {
            adviceText = "A bit of exercising could help you feel more awake!" ;
        } else if (readiness<95) {
            adviceText = "You're off to a good start today, just remember to take consistent breaks from your work!" ;
        } else if (readiness<100) {
            adviceText = "Alright! You're getting there!" ;
        } else if (readiness<115) {
            adviceText = "Yay, you've reached your goal! Now you can go out there and conquer this day!" ;
        } else {
            adviceText = "Remember that too much sleep can increase your tiredness." ;
        }

        return adviceText ;
    }


}
