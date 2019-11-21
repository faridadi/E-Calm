package com.ecalm.e_calm.math;

public class BMR {

    public BMR() {

    }

    public float getMaleBmr(float bb, float tb, float um) {
        float result = 88.362f + (13.397f * bb) + (4.799f * tb) - (5.677f * um);
        int round = Math.round(result);
        return (float)round;
    }

    public float getFemaleBmr(float bb, float tb, float um) {
        float result = 447.593f + (9.247f * bb) + (3.098f * tb) - (4.330f * um);
        int round = Math.round(result);
        return (float)round;
    }
}
