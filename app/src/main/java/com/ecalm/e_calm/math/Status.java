package com.ecalm.e_calm.math;

public class Status {

    public Status() {

    }

    public String cekStatus(float calorie, float recomend){
        if (calorie <= recomend){
            return "Healthy";
        }else if(calorie > recomend){
            return "Not Healthy";
        }else{
            return "Health";
        }
    }
}
