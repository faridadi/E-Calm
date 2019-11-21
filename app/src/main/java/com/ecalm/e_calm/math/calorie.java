package com.ecalm.e_calm.math;

import com.ecalm.e_calm.model.Food;

public class calorie {

    public calorie() {

    }

    public int calculateCalorie(int weight, int kal){
        float calorie = (weight * kal/100);
        int round = Math.round(calorie);
        return round;
    }
}
