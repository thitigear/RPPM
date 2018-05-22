package com.example.gear.rppm.other;

public class DistanceAngle {
    private static double shin;
    private static double[] defaultBody = new double[]{31.4, 54, 53.55, 88.85};

    public static double findSolutionAndAngle(String treatName, double[] distance){
        //distance = {elbow, wrist, knee, ankle}
        shin = defaultBody[3] - defaultBody[2];
        switch (treatName) {
            case "ยกแขนขึ้นและลง":
                return findSameSideAngle(defaultBody[0], distance[0]);
            case "กางแขนและหุบแขนทางข้างลำตัว":
                return findSameSideAngle(defaultBody[0], distance[0]);
            case "กางแขนและหุบแขนในแนวตั้งฉากกับลำตัว":
                return findSameSideAngle(defaultBody[0], distance[0]);
            case "หมุนข้อไหล่ขึ้นและลง":
                return findSameSideAngle(defaultBody[1], distance[1]);
            case "เหยียดและงอข้อศอก":
                return findSameSideAngle(defaultBody[1], distance[1]);
            case "งอขาและเหยียดข้อตะโพกและข้อเข่าพร้อมกัน":
                return findKneeAngle(distance[2], distance[3]); // สามเหลี่ยมด้านไม่เท่า
            case "กางและหุบข้อตะโพก":
                return findSameSideAngle(defaultBody[3], distance[3]);
            case "ยกขาขึ้นทั้งขา":
                return findSameSideAngle(defaultBody[3], distance[3]); // สามเหลี่ยมด้านไม่เท่า
            default:
                return 0;
        }
    }

    public static double findKneeAngle(double knee, double ankle){
        //power(3, 2) == 9
        double kk = Math.pow(knee,2);
        double ss = Math.pow(shin,2);
        double aa = Math.pow(ankle,2);
        double s = (kk + ss - aa)/(2*knee*shin);
        //radian to Degree
        double r = Math.toDegrees(Math.acos(s));

        //Degree Condition
        if(r > 180){return 180;}
        else if(r < 0){return 0;}
        else {return r;}
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

    public static double calculateDistance(double txPowerLevel, double rssi){
        /* Distance Formula is
         * d = 10 ^ ((P-Rssi) / 10n) (n ranges from 2 to 4);
         */
        double n = 4.0;
        double powValue = (txPowerLevel-rssi)/(10*n);

        //return Math.pow(10.0, powValue); // Distance
        return Math.pow(10.0, powValue); // Distance

    }

}
