package com.example.smaproject;

public class Food {

    private  double Carbs;
    private  double Fats;
    private  double Kcal;
    private  String Name;
    private  double Protein;
    private  String Qt;
    private int grams;

    public Food(){

    }

    public Food(double Carbs, double Fats, double Kcal, String Name, double Protein, String Qt, int grams) {
        this.Carbs = Carbs;
        this.Fats = Fats;
        this.Kcal = Kcal;
        this.Name = Name;
        this.Protein = Protein;
        this.Qt = Qt;
        this.grams = grams;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getQt() {
        return Qt;
    }

    public void setQt(String qt) {
        this.Qt = qt;
    }

    public double getProtein() {
        return Protein;
    }

    public void setProtein(double protein) {
        this.Protein = protein;
    }

    public double getCarbs() {
        return Carbs;
    }

    public void setCarbs(double carbs) {
        this.Carbs = carbs;
    }

    public double getFats() {
        return Fats;
    }

    public void setFats(double fats) {
        this.Fats = fats;
    }

    public double getKcal() {
        return Kcal;
    }

    public int getGrams() {
        return grams;
    }

    public void setGrams(int grams) {
        this.grams = grams;
    }

    public void setKcal(double kcal) {
        this.Kcal = kcal;
    }
}
