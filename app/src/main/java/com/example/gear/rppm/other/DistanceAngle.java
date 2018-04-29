package com.example.gear.rppm.other;

import android.support.annotation.NonNull;

public class DistanceAngle {
    private static double elbow;
    //private double rElbow;
    private static double wrist;
    //private double rWrist;
    private static double knee;
    //private double rKnee;
    private static double ankle;
    //private double rAnkle;
    private static double shin;

    public double getDistance(int[] data){
        return 0;
    }

    public double[] getAngle(double a, double b, double c){
        return new double[]{Math.acos(b / a), Math.acos(c / a), Math.acos(c / b)};
    }

    public static double findSolutionAndAngle(String treatName, double[] defaultBody, double[] distance){
        //distance = {elbow, wrist, knee, ankle}
        shin = distance[3] - distance[2];
        switch (treatName) {
            case "ยกแขนขึ้นและลง":
                return findSameSideAngle(defaultBody[0], distance[0]);
            case "กางแขนและหุบแขนทางข้างลำตัว":
                return findSameSideAngle(defaultBody[0], distance[0]);
            case "กางแบนและหุบแขนในแนวตั้งฉากกับลำตัว":
                return findSameSideAngle(defaultBody[0], distance[0]);
            case "หมุนข้อไหล่ขึ้นและลง":
                return findSameSideAngle(defaultBody[1], distance[1]);
            case "เหยียดและงอข้อศอก":
                return findSameSideAngle(defaultBody[1], distance[1]);
            case "งอขาและเหยียดข้อสะโพกและข้อเข่าพร้อมกัน":
                return findKneeAngle(distance, distance[2], distance[3], shin);
            case "กางและหุบข้อตะโพก":
                return findSameSideAngle(distance[2], distance[2]);
            case "หมุนข้อตะโพกเข้าและออก":
                return findKneeAngle(distance, distance[2], distance[3], shin);
            default:
                return -1;
        }
    }

    public static double findKneeAngle(double[] distance,double fKnee, double fAnkle, double fShin){
        //power(3, 2) == 9
        double s = (Math.pow(fKnee,2)+Math.pow(fShin,2)-Math.pow(fAnkle,2))/(2*fKnee*fShin);
        //radian to Degree
        double r = Math.toDegrees(Math.acos(s));

        //Degree Condition
        if (r == 180){return r;}
        else if(r > 180){return 180;}
        else if(r < 0){return 0;}
        else {return r - 20;}
    }

    public static double findSameSideAngle(double defaultElbow, double distance){
        // a^2 = b^2 + c^2 - 2bc cosA
        //A = cos^(-1) (b^2 + c^2 - a^2) / (2bc)
        if(distance > 2*defaultElbow){distance = 2*defaultElbow;}
        double aa = Math.pow(distance,2);
        double bb = Math.pow(defaultElbow,2);

        double u = bb+bb-aa;                        //(b^2 + c^2 - a^2)
        double d = 2*defaultElbow*defaultElbow;     //(2bc)
        if(d <= 0){d = 0.01;}
        double s = u/d;                             //(b^2 + c^2 - a^2)/(2bc)
        //radian to Degree
        double r = Math.toDegrees(Math.acos(s));    //cos^(-1) {(b^2 + c^2 - a^2)/(2bc)}

        if(r >= 180){return 180;}
        else if(r < 0){return 0;}
        else {return r;}
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
