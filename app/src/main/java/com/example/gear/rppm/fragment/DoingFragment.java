package com.example.gear.rppm.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;
import com.example.gear.rppm.other.DataArray;
import com.example.gear.rppm.other.DistanceAngle;
import com.example.gear.rppm.other.Utils;

import java.util.HashMap;
import java.util.Map;

public class DoingFragment extends Fragment {
    private static String TAG = "DOING Fragment";

    private static String CURRENT_TREAT;
    private static String FLAG_TREAT;       //Pevious Fragment

    /* Beacon */
    private String deviceAddress_elbow = "D4:36:39:DE:54:CB";
    private String deviceAddress_wrist = "D4:36:39:DE:54:CD";
    private String deviceAddress_knee = "D4:36:39:DE:55:82";
    private String deviceAddress_ankle = "D4:36:39:DE:56:FC"; // D4:36:39:DE:56:A1
    //private String deviceAddress_lKnee = "D4:36:39:DE:56:FC";
    //private String deviceAddress_rKnee = "D4:36:39:DE:57:5D";
    //private String deviceAddress_lAnkle = "D4:36:39:DE:57:AA";
    //private String deviceAddress_rAnkle = "D4:36:39:DE:57:D0";

    // TODO: Rename and change types of parameters
    //private static boolean isShowDefaultBodyDialogPosBut = false;
    private static boolean isCountThisRound = false;

    private double[] currentTreatData = DataArray.getCurrentTreatData(CURRENT_TREAT);
    //private double[][] dataGotFromScan = new double[10][8]; //10 row(Do No.) 8 column(LE, RE, LW, RW, LK, RK, LA, RA)
    private static Map<Integer, Object[]> beaconData = new HashMap<>(); //[address]{name, txPowerLevel, rssi}

    /*Distance and Angle Param*/
    private static double angle;
    private static double maxAngle = 0;
    private static double[] maxAnglePerTime = new double[]{0,0,0,0,0,0,0,0,0,0};
    private static double[] maxAngleAllRound = new double[]{0,0,0,0,0};
    private static double tempAngle = 0;
    private static double distance;
    private static double[] nowDistance = new double[]{0,0,0,0};

    /* SET variable */
    private static int deviceAddressNumber;
    private static int currentSet = 1;
    private static int maxSet;
    /* Doing TIME variable */
    private static int currentTime = 0;                             //Max Time = 10


    protected static int c_elbow = 0;
    protected static int c_wrist = 0;
    protected static int c_knee = 0;
    protected static int c_ankle = 0;

    private static double[] body = new double[]{0,0,0,0,0,0,0,0};

    private static String CURRENT_TAG = "doing";
    private static String TAG_DIALOG_FINISH_ALL = "finishAll";
    private static String TAG_DIALOG_FINISH_ONE = "finishOne";
    private static String TAG_DIALOG_PAUSE = "pause";
    private static String deviceAddress;

    /*UI Component*/
    private Button butStop;

    private static TextView tv_treatName;
    private static TextView tv_setNum;
    private static TextView tv_maxSet;
    private static TextView tv_timeNum;
    private static TextView tv_maxTime;
    private static TextView tv_angleNum;

    private static TextView tv_lElbow;
    private static TextView tv_lWrist;
    private static TextView tv_lKnee;
    private static TextView tv_lAnkle;

    private TextView tv_round01;
    private TextView tv_round02;
    private TextView tv_round03;
    private TextView tv_round04;
    private TextView tv_round05;

    /* Import Class */
    private static BluetoothAdapter bluetoothAdapter;
    private static BluetoothLeScanner bluetoothLeScanner;

    private View view;

    protected Handler getDataHandler;
    private Ringtone ringtone;

    public DoingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_doing, container, false);
        ((MainActivity)getActivity()).setCurrentTag(CURRENT_TAG);
        ((MainActivity)getActivity()).setToolbarTitleById(R.string.load_app_button);
        //((MainActivity)getActivity()).setToolbarTitleByString("เริ่มทำกายภาพ");

        /* UI Component */
        defineUI();
        setFirstUI();
        /*Pause Button*/
        butStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPauseCaution();
            }
        });
        /*Notification Sound Setup*/
        Utils.setupSound(view.getContext());

        /*set Bluetooth var*/
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        /*Run Beacon Scanner*/
        startScan(backgroundScanCallback);

        /*getDataEvery X second*/
        try {
            getDataHandler = new Handler();
            int delay = 250;
            getDataHandler.postDelayed(setTextViewAndCountRound, delay);

        } catch (Exception e) {
            Log.e("Error!!!!!!!!!!!!!! :", e.getMessage());
        }
        return view;
    }
    /*getDataHandler.postDelayed(new Runnable(){
                    public void run(){
                        //do something
                        calculateMaxAnglePerRound(angle);
                        //setTestTextView("" + deviceAddress, distance, angle);
                        setAngleTextView(angle);
                        double nowAngle = Double.parseDouble(""+tv_angleNum.getText());
                        int treatNum = DataArray.getTreatNumber(CURRENT_TREAT);
                        if(treatNum >= 0 && treatNum <= 4){
                            calculateArmRound(nowAngle); }
                        else if(treatNum == 5){
                            calculateLegTreat1Round(nowAngle);}
                        else if(treatNum == 6){
                            calculateLegTreat2Round(nowAngle); }
                        else if(treatNum == 7){
                            calculateLegTreat3Round(nowAngle); }
                        getDataHandler.postDelayed(this, 500);
                    }
                }, delay);*/

    /*Setter*/
    public static void setCurrentTreat(String currentTreat) {
        CURRENT_TREAT = currentTreat;
    }
    public static void setFlagTreat(String flagTreat) {
        FLAG_TREAT = flagTreat;
    }
    public static void setMaxSet(int setRoundNumber) {
        maxSet = setRoundNumber;
    }

    /*Set Variable*/
    private void setZero(){
        maxAngle = 0;
        currentTime = 0;
        currentSet = 1;
        Utils.stopNotificationSound();
    }
    private static void setNowDistance(String deviceAddress, double distance){
        switch (deviceAddress){
            case "D4:36:39:DE:54:CB":
                nowDistance[0] = distance;
                break;
            case "D4:36:39:DE:54:CD":
                nowDistance[1] = distance;
                break;
            case "D4:36:39:DE:55:82":
                nowDistance[2] = distance;
                break;
            case "D4:36:39:DE:56:FC": // D4:36:39:DE:56:A1
                nowDistance[3] = distance;
                break;
            default:
                break;
        }
    }

    /* Method */
        /* Set UI Component while Start Up Fragment*/
    private void defineUI() {
        tv_treatName = (TextView) view.findViewById(R.id.fragment_startRe_treatName);   //Treat NAME
        tv_setNum = (TextView) view.findViewById(R.id.fragment_doing_setNum);           //SET
        tv_maxSet = (TextView) view.findViewById(R.id.fragment_doing_setMax);           // Max number of SET
        tv_timeNum = (TextView) view.findViewById(R.id.fragment_doing_timeNum);         //จำนวนครั้งที่ทำได้ตอนนี้
        tv_maxTime = (TextView) view.findViewById(R.id.fragment_doing_timeMax);         //จำนวนครั้งสูงสุดที่ทำได่้ในรอบนั้นๆ
        tv_angleNum = (TextView) view.findViewById(R.id.fragment_doing_angleNum);       //จำนวนมุมที่ทำได้ตอนนี้

        butStop = (Button) view.findViewById(R.id.fragment_doing_buttonStop);           //Button Stop
    }
    private void setFirstUI(){
        tv_treatName.setText(CURRENT_TREAT);
        tv_setNum.setText(Utils.intToString(1));                                                //Default Set Number
        tv_maxSet.setText(Utils.intToString(maxSet));                                          //Max number of SET                                               //Default number of Doing
        tv_maxTime.setText(Utils.intToString(10));                                              //MAX TIME to Do
    }

        /*Dialog Condition when finish round or set*/
    private void conditionToShowFinishDialog(){
        if(currentTime == 10 && currentSet == maxSet){
            currentTime = 0;
            tv_timeNum.setText(Utils.intToString(currentTime));
            showFinishDialog();
            Utils.playNotificationSound();
        } else if(currentTime == 10 && currentSet != maxSet){
            showFinishOneSetDialog();
            Utils.playNotificationSound();
        }
    }

        /*Show Dialog While Condition(TRUE)*/
    @SuppressLint("DefaultLocale")
    private void showPauseCaution(){
        /*Calculate Average Angle with time you're doing*/
        int averageAngle = Utils.calculateAverageAngleWhenDoing(maxAnglePerTime, currentTime);

        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
        mAlertBuilder.setTitle(getResources().getText(R.string.fragment_doing_dialog_pause_title));
        mAlertBuilder.setMessage(String.format("%s %d %s %n%s %d %s %n%s %d %s"
                , getResources().getText(R.string.fragment_doing_dialog_maxAngle), (int)maxAngle
                , getResources().getText(R.string.fragment_doing_tv_angle02)
                , getResources().getText(R.string.fragment_doing_dialog_averageAngle), averageAngle
                , getResources().getText(R.string.fragment_doing_tv_angle02)
                , getResources().getText(R.string.fragment_doing_dialog_pause_doTime),currentTime
                , getResources().getText(R.string.fragment_doing_tv_time02)
        ));

        mAlertBuilder.setPositiveButton(getResources().getText(R.string.fragment_doing_dialog_but_001), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        mAlertBuilder.setNegativeButton(getResources().getText(R.string.fragment_doing_dialog_but_002), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setZero();
                stopScan(backgroundScanCallback);
                getDataHandler.removeCallbacks(setTextViewAndCountRound);
                getActivity().onBackPressed();
            }
        });

        AlertDialog alertDialog = mAlertBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE);
    }
    @SuppressLint("DefaultLocale")
    private void showFinishOneSetDialog(){
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
        mAlertBuilder.setTitle(getResources().getText(R.string.fragment_doing_dialog_finishOne_title));

        if (FLAG_TREAT.equals("leg")){
            mAlertBuilder.setMessage(String.format("คุณทำกายภาพครบ %d เซ็ตแล้ว %nมุมเฉลี่ย = %d องศา"
                    , currentSet, (int)calculateAverageAngleFromSumAngle()));
        }else {
            mAlertBuilder.setMessage(String.format("คุณทำกายภาพครบ %d เซ็ตแล้ว %nมุมมากที่สุดที่ทำได้ = %d องศา%nมุมเฉลี่ย = %d องศา"
                    , currentSet, (int)maxAngle, (int)calculateAverageAngleFromSumAngle()));
        }

        mAlertBuilder.setPositiveButton(getResources().getText(R.string.fragment_doing_dialog_but_001), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentTime = 0;
                maxAngle = 0;
                Utils.stopNotificationSound();
                currentSet += 1;

                tv_timeNum.setText(Utils.intToString(currentTime));
                tv_setNum.setText(Utils.intToString(currentSet));
            }
        });

        mAlertBuilder.setNegativeButton(getResources().getText(R.string.fragment_doing_dialog_but_002), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    setZero();
                    Utils.stopNotificationSound();
                    stopScan(backgroundScanCallback);
                    getDataHandler.removeCallbacks(setTextViewAndCountRound);
                    ((MainActivity)getActivity()).setTagDoingCurrentDialog(TAG_DIALOG_FINISH_ONE);
                    getActivity().onBackPressed();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        AlertDialog alertDialog = mAlertBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE);
        maxAngleAllRound[currentSet] = maxAngle;
    }
    @SuppressLint("DefaultLocale")
    private void showFinishDialog(){
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
        mAlertBuilder.setTitle(getResources().getText(R.string.fragment_doing_dialog_finishAll_title));
        if (FLAG_TREAT.equals("leg")){
            mAlertBuilder.setMessage(String.format("คุณทำกายภาพครบ %d เซ็ตแล้ว %nมุมเฉลี่ย = %d องศา"
                    , currentSet, (int)calculateAverageAngleFromSumAngle()));
        }else {
            mAlertBuilder.setMessage(String.format("คุณทำกายภาพครบ %d เซ็ตแล้ว %nมุมมากที่สุดที่ทำได้ = %d องศา%nมุมเฉลี่ย = %d องศา"
                    , currentSet, (int)maxAngle, (int)calculateAverageAngleFromSumAngle()));
        }

        mAlertBuilder.setNegativeButton(getResources().getText(R.string.fragment_doing_dialog_but_002), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    setZero();
                    stopScan(backgroundScanCallback);
                    getDataHandler.removeCallbacks(setTextViewAndCountRound);
                    ((MainActivity)getActivity()).setTagDoingCurrentDialog(TAG_DIALOG_FINISH_ALL);
                    getActivity().onBackPressed();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        AlertDialog alertDialog = mAlertBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE);
    }

        /*Calculate Round*/
    private void calculateRound(double angle, double count_angle, double reset_angle, int treat_num){
        try {
            if(treat_num == 5){
                if (!isCountThisRound){
                    if(angle <= count_angle){
                        isCountThisRound = true;
                        currentTime += 1;
                    }
                }else{
                    if(angle >= reset_angle){
                        tv_timeNum.setText(Utils.intToString(currentTime));
                        isCountThisRound = false;
                        maxAnglePerTime[currentTime-1] = tempAngle;
                        tempAngle = 0;
                        conditionToShowFinishDialog();
                    }
                }
            }
            /*treatNum != 5*/
            else {
                if (!isCountThisRound){
                    if(angle >= count_angle){
                        isCountThisRound = true;
                        currentTime += 1;
                    }
                }else{
                    if(angle <= reset_angle){
                        tv_timeNum.setText(Utils.intToString(currentTime));
                        isCountThisRound = false;
                        maxAnglePerTime[currentTime-1] = tempAngle;
                        tempAngle = 0;
                        conditionToShowFinishDialog();
                    }
                }
            }

        } catch (Exception e){
            Log.e("Exception :", "");
            e.printStackTrace();
        }
    }
    private void calculateRoundCondition(){
        //define value
        double nowAngle = Double.parseDouble(""+tv_angleNum.getText());
        int treatNum = DataArray.getTreatNumber(CURRENT_TREAT);
        //condition & condition value
        if(treatNum == 5){
            //leg
            calculateRound(nowAngle, 60,160, treatNum);
        } else if (treatNum == 6 || treatNum == 7){
            //leg
            calculateRound(nowAngle, 30, 25, treatNum);
        } else {
            //arm
            calculateRound(nowAngle, 45, 30, treatNum);
        }
    }

            //ARM
    private void calculateArmRound(double angle){
        try {
            if (!isCountThisRound){
                if(angle >= 45){
                    isCountThisRound = true;
                    currentTime += 1;
                }
            }else{
                if(angle <= 30){
                    tv_timeNum.setText(Utils.intToString(currentTime));
                    isCountThisRound = false;
                    maxAnglePerTime[currentTime-1] = tempAngle;
                    tempAngle = 0;
                    conditionToShowFinishDialog();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
            //LEG
    private void calculateLegTreat1Round(double angle){
        try {
            if (!isCountThisRound){
                if(angle <= 60.0){
                    isCountThisRound = true;
                    currentTime += 1;
                }
            }else{
                if(angle >= 160){
                    tv_timeNum.setText(Utils.intToString(currentTime));
                    isCountThisRound = false;
                    maxAnglePerTime[currentTime-1] = tempAngle;
                    tempAngle = 0;
                    conditionToShowFinishDialog();
                }
            }
        } catch (Exception e){
            Log.e("CalcLegTreat 1 E:", "");
            e.printStackTrace();
        }
    }
    private void calculateLegTreat2Round(double angle){
        try {
            if (!isCountThisRound){
                if(angle >= 30){
                    isCountThisRound = true;
                    currentTime += 1;
                }
            }else{
                if(angle <= 25){
                    tv_timeNum.setText(Utils.intToString(currentTime));
                    isCountThisRound = false;
                    maxAnglePerTime[currentTime-1] = tempAngle;
                    tempAngle = 0;
                    conditionToShowFinishDialog();
                }
            }
        } catch (Exception e){
            Log.e("Exception", ""+e);
        }
    }

        /*Calculate Summary Angle*/
    private void calculateMaxAnglePerRound(double angle){
        if(angle > maxAngle) {
            maxAngle = angle;
        }
        if(angle > tempAngle){
            tempAngle = angle;
        }
    }
    private double calculateAverageAngleFromSumAngle(){
        double sumAngle = 0;
        for (double angle: maxAnglePerTime){
            sumAngle += angle;
        }
        return sumAngle/10;
    }

    private Runnable setTextViewAndCountRound = new Runnable() {
        @Override
        public void run() {
            //do something
            calculateMaxAnglePerRound(angle);
            //setTestTextView("" + deviceAddress, distance, angle);
            setAngleTextView(angle);
            double nowAngle = Double.parseDouble(""+tv_angleNum.getText());
            int treatNum = DataArray.getTreatNumber(CURRENT_TREAT);

            calculateRoundCondition();

            getDataHandler.postDelayed(this, 500);
        }

    };/*
            if(treatNum == 5){
                calculateRound(nowAngle, 60,160, treatNum);
            } else if (treatNum == 6 || treatNum == 7){
                calculateRound(nowAngle, 30, 25, treatNum);
            } else {
                calculateRound(nowAngle, 45, 30, treatNum);
            }
         */

        /*Start Stop Beacon Scanner*/
    public static void startScan(ScanCallback mScanCallback){ bluetoothLeScanner.startScan(mScanCallback); }
    public static void stopScan(ScanCallback mScanCallback){ bluetoothLeScanner.stopScan(mScanCallback); }

        /*TextView SetText*/
    private static void setAngleTextView(double angle){
        int iAngle = (int) angle;
        Log.e("ANGLE!!!!!!!!!!!!:", String.format("%d",iAngle));
        tv_angleNum.setText(Utils.intToString((int)angle));
    }

        /*ScanCallBack*/
    protected static ScanCallback backgroundScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            ScanRecord mScanRecord = result.getScanRecord();

            if(result.getDevice().getName() != null) {
                if (result.getDevice().getUuids() == null){

                    deviceAddress = result.getDevice().getAddress();
                    deviceAddressNumber = DataArray.getDeviceAdrressNumber(deviceAddress);

                    String deviceName = result.getDevice().getName();
                    double txPowerLevel = mScanRecord.getTxPowerLevel()*1.0;
                    double rssi = result.getRssi()*1.0;

                    distance = DistanceAngle.calculateDistance(txPowerLevel, rssi);
                    setNowDistance(deviceAddress, distance);
                    String msg = "";
                    for (double distance: nowDistance){
                        msg += Utils.doubleToString(distance) + ", ";
                    }
                    beaconData.put(deviceAddressNumber, new Object[]{deviceAddress, txPowerLevel, rssi});
                    angle = DistanceAngle.findSolutionAndAngle(CURRENT_TREAT, nowDistance);
                }
            }
        }
    };
/*Log.e(TAG, "Device Name:" + deviceName);
                    Log.e(TAG, "Device Address:" + deviceAddress);
                    Log.e(TAG, "Distance:" + distance);
                    Log.e("Now Distance Array", msg);
                    Log.e("Count Test!!", String.format("Count = %d", currentTime));*/

}
