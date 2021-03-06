package com.example.olio_ht;


public class SleepEntry extends UserEntry {

    int h1 = 0, m1 = 0, h2 = 0, m2 = 0, slepth = 0, sleptmin = 0, mindifference = 0, readiness = 0;
    long goal;
    double slepttime = 0;
    String resultText, adviceText;

    public SleepEntry(int hour1, int hour2, int min1, int min2) {
        h1 = hour1;
        h2 = hour2;
        m1 = min1;
        m2 = min2;
    }

    // This method calculates and returns the time that the user has slept in minutes. The time is
    // also converted into hours.
    public int calculateTime() {
        if (h1 > h2) {
            mindifference = 24 * 60 - (60 * h1 + m1) + (60 * h2 + m2);
        } else {
            mindifference = 60 * h2 + m2 - (60 * h1 + m1);
        }

        slepttime = (double) mindifference / 60;

        return mindifference;
    }

    // Here the time slept is set for the user to see.
    public String getHoursAndMinsText(int mins) {
        slepth = (int) mins / 60;
        sleptmin = mins - slepth * 60;

        if (sleptmin == 0) {
            resultText = "You slept\n" + slepth + " hours";
        } else if (slepth == 0) {
            resultText = "You slept\n" + sleptmin + " minutes";
        } else {
            resultText = "You slept\n" + slepth + " hours and " + sleptmin + " minutes";
        }

        return resultText;
    }

    // Readiness is calculated as a ratio of the time slept and the user's own goal
    public int getReadiness () {
        float goalh = (float) goal/60;
        readiness = (int) ((slepttime / goalh) * 100);
        return readiness;
    }

    public void setGoal( long g ) { goal = g; }
    // "Advice text" is printed for the user based on their readiness
    public String getAdvice ( int readiness){
        if (readiness < 30) {
            adviceText = "Take it easy on yourself today and try to sleep early!";
        } else if (readiness < 50) {
            adviceText = "Remember to stay hydrated and eat nutritious food. You can get through this day!";
        } else if (readiness < 75) {
            adviceText = "A bit of exercising could help you feel more awake!";
        } else if (readiness < 95) {
            adviceText = "You're off to a good start today, just remember to take consistent breaks from your work!";
        } else if (readiness < 100) {
            adviceText = "Alright! You're getting there!";
        } else if (readiness < 115) {
            adviceText = "Yay, you've reached your goal! Now you can go out there and conquer this day!";
        } else {
            adviceText = "Remember that too much sleep can increase your tiredness.";
        }
      return adviceText;
    }

    public double getSleptTime() { return slepttime; }
  
}
