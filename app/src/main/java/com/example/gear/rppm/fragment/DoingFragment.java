package com.example.gear.rppm.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;
import com.example.gear.rppm.other.DataArray;
import com.example.gear.rppm.other.DistanceAngle;
import com.example.gear.rppm.other.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DoingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DoingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String TAG = "DOING Fragment";

    private static String CURRENT_TREAT;
    private static String FLAG_TREAT;       //Pevious Fragment

    /* Beacon */
    private String deviceAddress_elbow = "D4:36:39:DE:54:CB";
    private String deviceAddress_wrist = "D4:36:39:DE:54:CD";
    private String deviceAddress_knee = "D4:36:39:DE:55:82";
    private String deviceAddress_ankle = "D4:36:39:DE:56:A1";
    //private String deviceAddress_lKnee = "D4:36:39:DE:56:FC";
    //private String deviceAddress_rKnee = "D4:36:39:DE:57:5D";
    //private String deviceAddress_lAnkle = "D4:36:39:DE:57:AA";
    //private String deviceAddress_rAnkle = "D4:36:39:DE:57:D0";

    // TODO: Rename and change types of parameters
    //private static boolean isShowDefaultBodyDialogPosBut = false;
    private static boolean isCountThisRound = false;
    private static boolean isShowDefaultBodyDialogPosBut;
    private static boolean endThisFragment = false;

    private double[] currentTreatData = DataArray.getCurrentTreatData(CURRENT_TREAT);
    //private double[][] dataGotFromScan = new double[10][8]; //10 row(Do No.) 8 column(LE, RE, LW, RW, LK, RK, LA, RA)
    private static double[] defaultBody = new double[]{31.4, 54, 53.55, 88.85};
    private static Map<Integer, Object[]> beaconData = new HashMap<>(); //[address]{name, txPowerLevel, rssi}

    /*Distance and Angle Param*/
    private static double angle;
    private static double maxAngle = 0;
    private static double[] maxAngleAllRound = new double[]{0,0,0,0,0};
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

    private static String deviceAddress;

    private String mParam1;
    private String mParam2;

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
    private OnFragmentInteractionListener mListener;

    protected Handler getDataHandler;
    private Ringtone ringtone;



    public DoingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoingFragment newInstance(String param1, String param2) {
        DoingFragment fragment = new DoingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_doing, container, false);

        ((MainActivity)getActivity()).setToolbarTitleByString("เริ่มทำกายภาพ");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        Utils.setupSound(view.getContext());

        /*Show Getting Default body Dialog */
        //showDefaultBodyDialog();

        /* UI Component */
        defineUI();
        setFirstUI();

        /*Button Do Something*/
        butStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPauseCaution();
            }
        });

        /*Run Beacon Scanner*/
        startScan(backgroundScanCallback);

        /*getDataEvery X second*/
        try {
            getDataHandler = new Handler();
            int delay = 500;
            getDataHandler.postDelayed(new Runnable(){
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
            }, delay);
        } catch (Exception e) {
            Log.e("Error!!!!!!!!!!!!!! :", e.getMessage());
        }

        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


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
            case "D4:36:39:DE:56:A1":
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

        //tv_lElbow = (TextView) view.findViewById(R.id.doing_elbow);
        //tv_rElbow = (TextView) view.findViewById(R.id.doing_rElbow);
        //tv_lWrist = (TextView) view.findViewById(R.id.doing_lWrist);
        //tv_rWrist = (TextView) view.findViewById(R.id.doing_rWrist);
        //tv_lKnee = (TextView) view.findViewById(R.id.doing_lKnee);
        //tv_rKnee = (TextView) view.findViewById(R.id.doing_rKnee);
        //tv_lAnkle = (TextView) view.findViewById(R.id.doing_lAnkle);
        //tv_rAnkle = (TextView) view.findViewById(R.id.doing_rAnkle);

        butStop = (Button) view.findViewById(R.id.fragment_doing_buttonStop);           //Button Stop
    }
    private void setFirstUI(){
        tv_treatName.setText(CURRENT_TREAT);
        tv_setNum.setText(Utils.intToString(1));                                                //Default Set Number
        tv_maxSet.setText(Utils.intToString(maxSet));                                          //Max number of SET                                               //Default number of Doing
        tv_maxTime.setText(Utils.intToString(10));                                              //MAX TIME to Do
    }
    private void finishDialogDefineUI(){
        tv_round01 = (TextView) view.findViewById(R.id.doing_finish_R01_angle);
        tv_round02 = (TextView) view.findViewById(R.id.doing_finish_R02_angle);
        tv_round03 = (TextView) view.findViewById(R.id.doing_finish_R03_angle);
        tv_round04 = (TextView) view.findViewById(R.id.doing_finish_R04_angle);
        tv_round05 = (TextView) view.findViewById(R.id.doing_finish_R05_angle);

        finishDialogUISetTextCondition();
    }
    private void finishDialogUISetTextCondition(){
        switch (maxSet){
            case 1:
                tv_round01.setText(Utils.doubleToString(maxAngleAllRound[0]));
                tv_round02.setText("-");
                tv_round03.setText("-");
                tv_round04.setText("-");
                tv_round05.setText("-");
                break;
            case 2:
                tv_round01.setText(Utils.doubleToString(maxAngleAllRound[0]));
                tv_round02.setText(Utils.doubleToString(maxAngleAllRound[1]));
                tv_round03.setText("-");
                tv_round04.setText("-");
                tv_round05.setText("-");
                break;
            case 3:
                tv_round01.setText(Utils.doubleToString(maxAngleAllRound[0]));
                tv_round02.setText(Utils.doubleToString(maxAngleAllRound[1]));
                tv_round03.setText(Utils.doubleToString(maxAngleAllRound[2]));
                tv_round04.setText("-");
                tv_round05.setText("-");
                break;
            case 4:
                tv_round01.setText(Utils.doubleToString(maxAngleAllRound[0]));
                tv_round02.setText(Utils.doubleToString(maxAngleAllRound[1]));
                tv_round03.setText(Utils.doubleToString(maxAngleAllRound[2]));
                tv_round04.setText(Utils.doubleToString(maxAngleAllRound[3]));
                tv_round05.setText("-");
                break;
            case 5:
                tv_round01.setText(Utils.doubleToString(maxAngleAllRound[0]));
                tv_round02.setText(Utils.doubleToString(maxAngleAllRound[1]));
                tv_round03.setText(Utils.doubleToString(maxAngleAllRound[2]));
                tv_round04.setText(Utils.doubleToString(maxAngleAllRound[3]));
                tv_round05.setText(Utils.doubleToString(maxAngleAllRound[4]));
                break;
        }

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
    private void showPauseCaution(){
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
        mAlertBuilder.setTitle("หยุด");
        mAlertBuilder.setMessage("หยุดชั่วคราว");

        mAlertBuilder.setPositiveButton("ทำต่อ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        mAlertBuilder.setNegativeButton("กลับไปหน้าเลือกท่า", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setZero();
                stopScan(backgroundScanCallback);
                getActivity().onBackPressed();
            }
        });

        AlertDialog alertDialog = mAlertBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE);
    }
    private void showFinishOneSetDialog(){
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
        mAlertBuilder.setTitle("หยุดพักชั่วคราว");
        mAlertBuilder.setMessage(String.format("คุณทำกายภาพครบ %d เซ็ตแล้ว %n มุมมากที่สุดที่ทำได้ = %.2f", currentSet, maxAngle));

        mAlertBuilder.setPositiveButton("ทำต่อ", new DialogInterface.OnClickListener() {
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

        mAlertBuilder.setNegativeButton("กลับไปหน้าก่อนหน้า", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    setZero();
                    Utils.stopNotificationSound();
                    stopScan(backgroundScanCallback);
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
    private void showFinishDialog(){
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
        mAlertBuilder.setTitle("คุณทำกายภาพครบทั้งหมดแล้ว");
        //mAlertBuilder.setView(R.layout.doing_finish_round_dialog);
        mAlertBuilder.setMessage(String.format("มุมมากที่สุดที่ทำได้ = %.2f", maxAngle));
        //maxAngleAllRound[currentSet] = maxAngle;

        //finishDialogDefineUI();

        mAlertBuilder.setNegativeButton("กลับไปหน้าเลือกท่า", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setZero();
                stopScan(backgroundScanCallback);
                getActivity().onBackPressed();
                try {

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
            //ARM
    private void calculateArmRound(double angle){
        try {
            if (!isCountThisRound){
                if(angle >= 90){
                    isCountThisRound = true;
                    currentTime += 1;
                }
            }else{
                if(angle <= 30){
                    tv_timeNum.setText(Utils.intToString(currentTime));
                    isCountThisRound = false;
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
                if(angle <= 90.0){
                    isCountThisRound = true;
                    currentTime += 1;
                }
            }else{
                if(angle >= 160){
                    tv_timeNum.setText(Utils.intToString(currentTime));
                    isCountThisRound = false;
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
                if(angle >= 30.0){
                    isCountThisRound = true;
                    currentTime += 1;
                }
            }else{
                if(angle <= 30){
                    tv_timeNum.setText(Utils.intToString(currentTime));
                    isCountThisRound = false;

                    conditionToShowFinishDialog();
                }
            }
        } catch (Exception e){
            Log.e("Exception", ""+e);
        }
    }
    private void calculateLegTreat3Round(double angle){
        try {
            if (!isCountThisRound){
                if(angle <= 90.0){
                    isCountThisRound = true;
                    currentTime += 1;
                }
            }else{
                if(angle >= 160){
                    tv_timeNum.setText(String.format("%d",currentTime));
                    isCountThisRound = false;
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
    }

        /*Start Stop Beacon Scanner*/
    public static void startScan(ScanCallback mScanCallback){ bluetoothLeScanner.startScan(mScanCallback); }
    public static void stopScan(ScanCallback mScanCallback){ bluetoothLeScanner.stopScan(mScanCallback); }

        /*Test TextView SetText*/
    private static void setTestTextView(String address,double distance, double angle){

        Log.e("ANGLE!!!!!!!!!!!!:", String.format("%.2f",angle));

        //angle = DistanceAngle.findSolutionAndAngle(CURRENT_TREAT, defaultBody, distance);

        switch (address){
            case "D4:36:39:DE:54:CB":
                //tv_lElbow.setText(textDistance);
                tv_lElbow.setText(Utils.doubleToString(distance));
                tv_angleNum.setText(Utils.doubleToString(angle));
                break;
            case "D4:36:39:DE:54:CD":
                //tv_rElbow.setText(textDistance);
                tv_lWrist.setText(Utils.doubleToString(distance));
                tv_angleNum.setText(Utils.doubleToString(angle));
                break;
            case "D4:36:39:DE:55:82":
                tv_lKnee.setText(Utils.doubleToString(distance));
                tv_angleNum.setText(Utils.doubleToString(angle));
                break;
            case "D4:36:39:DE:56:A1":
                tv_lAnkle.setText(Utils.doubleToString(distance));
                tv_angleNum.setText(Utils.doubleToString(angle));
                break;
            case "D4:36:39:DE:56:FC":
                //tv_lKnee.setText(Utils.doubleToString(distance));
                break;
            case "D4:36:39:DE:57:5D":
                //tv_rKnee.setText(Utils.doubleToString(distance));
                break;
            case "D4:36:39:DE:57:AA":
                //tv_lAnkle.setText(Utils.doubleToString(distance));
                break;
            case "D4:36:39:DE:57:D0":
                //tv_rAnkle.setText(Utils.doubleToString(distance));
                break;
            default:
                break;
        }
    }
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
                    Log.e(TAG, "Device Name:" + deviceName);
                    Log.e(TAG, "Device Address:" + deviceAddress);
                    Log.e(TAG, "Distance:" + distance);
                    Log.e("Now Distance Array", msg);
                    Log.e("Count Test!!", String.format("Count = %d", currentTime));

                    beaconData.put(deviceAddressNumber, new Object[]{deviceAddress, txPowerLevel, rssi});

                    angle = DistanceAngle.findSolutionAndAngle(CURRENT_TREAT, defaultBody, nowDistance);

                }

            }
        }
    };
}
