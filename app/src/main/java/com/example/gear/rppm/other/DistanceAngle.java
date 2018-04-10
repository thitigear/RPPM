package com.example.gear.rppm.other;

import android.support.annotation.NonNull;

public class DistanceAngle {
    private double lElbow;
    private double rElbow;
    private double lWrist;
    private double rWrist;
    private double lKnee;
    private double rKnee;
    private double lAnkle;
    private double rAnkle;

    public double getDistance(int[] data){

        return 0;
    }

    public double[] getAngle(double a, double b, double c){
        return new double[]{Math.acos(b / a), Math.acos(c / a), Math.acos(c / b)};
    }

    public static double findSolutionAndAngle(String treatName, double[] defaultBody, double distance){
        switch (treatName) {
            case "ยกแขนขึ้นและลง":
                return findTreat1_2Angle(defaultBody[0], distance);
            case "กางแขนและหุบแขนทางข้างลำตัว":
                return findTreat1_2Angle(defaultBody[0], distance);
            case "กางแบนและหุบแขนในแนวตั้งฉากกับลำตัว":
                return 0;
            case "หมุนข้อไหล่ขึ้นและลง":
                return 0;
            case "เหยียดและงอข้อศอก":
                return 0;
            case "งอขาและเหยียดข้อสะโพกและข้อเข่าพร้อมกัน":
                return 0;
            case "กางและหุบข้อตะโพก":
                return 0;
            case "หมุนข้อตะโพกเข้าและออก":
                return 0;
            default:
                return -1;
        }
    }

    public static double findKneeAngle(double fKnee, double fAnkle, double fShin){
        //power(3, 2) == 9
        double s = (Math.pow(fKnee,2)+Math.pow(fShin,2)-Math.pow(fAnkle,2))/(2*fKnee*fShin);
        //radian to Degree
        double r = Math.toDegrees(Math.acos(s));

        //Degree Condition
        if (r == 180){return r;}
        else {return r - 20;}
    }

    public static double findTreat1_2Angle(double defaultElbow, double distance){
        // a^2 = b^2 + c^2 - 2bc cosA
        //A = cos^(-1) (b^2 + c^2 - a^2) / (2bc)
        double aa = Math.pow(distance,2);
        double bb = Math.pow(defaultElbow,2);

        double u = bb+bb-aa;                        //(b^2 + c^2 - a^2)
        double d = 2*bb*bb;                         //(2bc)
        double s = u/d;                             //(b^2 + c^2 - a^2)/(2bc)
        //radian to Degree
        double r = Math.toDegrees(Math.acos(s));    //cos^(-1) {(b^2 + c^2 - a^2)/(2bc)}
        return r;
    }

    public static String getTextDouble(Double d){return String.format("%.4f", d);}

    public static double calculateDistance(double txPowerLevel, double rssi){
        /* Distance Formula is
         * d = 10 ^ ((P-Rssi) / 10n) (n ranges from 2 to 4);
         */
        //double dTxPowerLevel = txPowerLevel*1.0;
        double dTxPowerLevel = 0.0;
        double n = 4.0;
        double powValue = (txPowerLevel-rssi)/(10*n);

        //return Math.pow(10.0, powValue); // Distance
        return Math.pow(10.0, powValue); // Distance

    }
    public static Double[] getBodyHalf(double arm, double knee, double ankle, double shin){
        return new Double[]{arm, knee, ankle, shin};
    }
}
