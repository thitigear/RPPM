package com.example.gear.rppm.other;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;

import com.example.gear.rppm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataArray extends Fragment {
    private double lElbow;
    private double rElbow;
    private double lWrist;
    private double rWrist;
    private double lKnee;
    private double rKnee;
    private double lAnkle;
    private double rAnkle;
    private double[] currentTreatData;
    private double[][] defaultBodyData = new double[8][8]; //แยกเป็นท่าๆ
    private String[] treatName = new String[]{
            "ยกแขนขึ้นและลง"
            , "กางแขนและหุบแขนทางข้างลำตัว"
            , "กางแบนและหุบแขนในแนวตั้งฉากกับลำตัว"
            , "หมุนข้อไหล่ขึ้นและลง"
            , "เหยียดและงอข้อศอก"
            , "งอขาและเหยียดข้อสะโพกและข้อเข่าพร้อมกัน"
            , "กางและหุบข้อตะโพก"
            , "หมุนข้อตะโพกเข้าและออก"};

    //private String[] deviceAddress = getResources().getStringArray(R.array.device_address);

    private String[] deviceAddress = new String[]{
            "D4:36:39:DE:54:CB"
            , "D4:36:39:DE:54:CD"
            , "D4:36:39:DE:55:82"
            , "D4:36:39:DE:56:A1"};

    /**= new String[]{
            , "D4:36:39:DE:56:FC"
            , "D4:36:39:DE:57:5D"
            , "D4:36:39:DE:57:AA"
            , "D4:36:39:DE:57:D0"
    };*/

    private static double[][] defaultTreatData = new double[][]
            {                   //{Elbow, Wrist, Knee, Ankle}
                                //Default เหนือไหล่
             {0,0,0,0}            //arm ยกแขนขึ้นและลง------------------ข้างศอกตอนแรก
            ,{0,0,0,0}            //arm กางแขนและหุบแขนทาง ข้างลำตัว---------ข้างศอกตอนแรก ด้านข้างลำตัว
            ,{0,0,0,0}             //arm กางแบนและหุบแขนในแนว ตั้งฉากกับลำตัว-----ปลายมือแนวแขนขนานกับหัวไหล่
            ,{0,0,0,0}             //arm หมุนข้อไหล่ขึ้นและลง---------------ปลายมือ ต้นแขนขนานกับหัวไหล่ และหักข้อศอกให้แขนท่อนล่างขนานกับลำตัว
            ,{0,0,0,0}             //arm เหยียดและงอข้อศอก----------------เหนือหัวไหล่ขนานกับแขน
            ,{0,0,0,0}             //leg งอขาและเหยียดข้อสะโพกและข้อเข่าพร้อมกัน----บริเวณหัวเข็มขัด
            ,{0,0,0,0}             //leg กางและหุบข้อตะโพก----------------วางระหว่างข้อเท้าทั้งสองด้าน
            ,{0,0,0,0}             //leg หมุนข้อตะโพกเข้าและออก-------------
    };

    public DataArray() {

    }

    public double[] getCurrentTreatData() {return currentTreatData;}

    public void setCurrentTreatData(String treatName){currentTreatData = defaultTreatData[getTreatNumber(treatName)]; }


    public static double[] getCurrentTreatData(@NonNull String treatName) {return defaultTreatData[getTreatNumber(treatName)];}

    @NonNull
    public static int getDeviceAdrressNumber(String deviceAddress){
        switch (deviceAddress) {
            case "D4:36:39:DE:54:CB":
                return 0;
            case "D4:36:39:DE:54:CD":
                return 1;
            case "D4:36:39:DE:55:82":
                return 2;
            case "D4:36:39:DE:56:A1":
                return 3;
            case "D4:36:39:DE:56:FC":
                return 4;
            case "D4:36:39:DE:57:5D":
                return 5;
            case "D4:36:39:DE:57:AA":
                return 6;
            case "D4:36:39:DE:57:D0":
                return 7;
            default:
                return -1;
        }
    }

    @NonNull


    public static int getTreatNumber(String treatName){
        switch (treatName) {
            case "ยกแขนขึ้นและลง":
                return 0;
            case "กางแขนและหุบแขนทางข้างลำตัว":
                return 1;
            case "กางแบนและหุบแขนในแนวตั้งฉากกับลำตัว":
                return 2;
            case "หมุนข้อไหล่ขึ้นและลง":
                return 3;
            case "เหยียดและงอข้อศอก":
                return 4;
            case "งอขาและเหยียดข้อสะโพกและข้อเข่าพร้อมกัน":
                return 5;
            case "กางและหุบข้อตะโพก":
                return 6;
            case "หมุนข้อตะโพกเข้าและออก":
                return 7;
            default:
                return -1;
        }
    }

    public static int[] getTreatNodePosition(String[] arrayTreatNode){
        int[] arrayTreatNodePos = new int[]{0,0,0,0,0,0,0,0};
        for (String treatNode: arrayTreatNode){
            switch (treatNode) {
                case "elbow":
                    arrayTreatNodePos[0] = 1;
                    break;
                case "wrist":
                    arrayTreatNodePos[1] = 1;
                    break;
                case "knee":
                    arrayTreatNodePos[2] = 1;
                    break;
                case "ankle":
                    arrayTreatNodePos[3] = 1;
                    break;
                case "default_elbow":
                    arrayTreatNodePos[4] = 1;
                    break;
                case "default_wrist":
                    arrayTreatNodePos[5] = 1;
                    break;
                case "default_knee":
                    arrayTreatNodePos[6] = 1;
                    break;
                case "default_ankle":
                    arrayTreatNodePos[7] = 1;
                    break;
                default:
                    break;
            }
        }
        return arrayTreatNodePos;
    }


}
