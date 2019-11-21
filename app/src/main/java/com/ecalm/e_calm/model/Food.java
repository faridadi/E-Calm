package com.ecalm.e_calm.model;

public class Food {
    int id;
    String name;
    float calorie;
    float weight;
    public Food() {

    }

    public Food(int id, String name, float calorie ,float weight  ) {
        this.weight = weight;
        this.name = name;
        this.calorie = calorie;
        this.id = id;
    }

    public Food(String name, float calorie, float weight) {
        this.name = name;
        this.calorie = calorie;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
