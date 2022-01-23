package com.example.smaproject;

public class UserNeeds {
    private double kCal, protein, carbs, fats;
    private String day;
    public UserNeeds() {
    }

    public UserNeeds(double kCal, double protein, double carbs, double fats) {
        this.kCal = kCal;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

    public UserNeeds(double kCal, double protein, double carbs, double fats, String day) {
        this.kCal = kCal;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public double getkCal() {
        return kCal;
    }

    public void setkCal(double kCal) {
        this.kCal = kCal;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }
}
