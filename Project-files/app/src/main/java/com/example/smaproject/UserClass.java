package com.example.smaproject;

public class UserClass {
    public String name, id, gender, goal,email;
    public int age, height, weight;

    public UserClass() {
    }

    public UserClass(String name, String id, String gender, String goal, String email, int age, int height, int weight) {
        this.name = name;
        this.id = id;
        this.gender = gender;
        this.goal = goal;
        this.email = email;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

 public String weightToString(){
        return String.valueOf(this.weight);
 }


    public String heightToString(){
        return String.valueOf(this.height);
    }
    public String ageToString(){
        return String.valueOf(this.age);
    }


}
