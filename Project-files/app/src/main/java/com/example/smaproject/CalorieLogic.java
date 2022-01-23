package com.example.smaproject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalorieLogic {

    public static double getKcalFloss(String gender, int age, int weight, int height) {
        double rez = 1;

        if (gender.equals("Male")) {
            rez = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            rez = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        rez = rez + 200;

        return rez;
    }


    public static double getKcalGain(String gender, int age, int weight, int height) {
        double rez = 1;

        if (gender.equals("Male")) {
            rez = 15 * weight + 6.25 * height - 5 * age + 5;
        } else {
            rez = 15 * weight + 6.25 * height - 5 * age - 161;
        }

        rez = rez + 200;

        return rez;
    }

    public static double getKcalMaintain(String gender, int age, int weight, int height) {
        double rez = 1;

        if (gender.equals("Male")) {
            rez = 13 * weight + 6.25 * height - 5 * age + 5;
        } else {
            rez = 13 * weight + 6.25 * height - 5 * age - 161;
        }

        rez = rez + 200;

        return rez;
    }

    public static double getProtein(double kCal) {
        double rez;
        rez = kCal * 0.3;
        rez = rez / 4;
        return rez;
    }

    public static double getCarb(double kCal) {
        double rez;
        rez = kCal * 0.5;
        rez = rez / 4;
        return rez;
    }

    public static double getFats(double kCal) {
        double rez;
        rez = kCal * 0.2;
        rez = rez / 9;
        return rez;
    }

    public static String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }





}
