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
import android.net.Uri;
import android.os.Bundle;
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
    private String deviceAddress_lElbow = "D4:36:39:DE:54:CB";
    private String deviceAddress_rElbow = "D4:36:39:DE:54:CD";
    private String deviceAddress_lWrist = "D4:36:39:DE:55:82";
    private String deviceAddress_rWrist = "D4:36:39:DE:56:A1";
    private String deviceAddress_lKnee = "D4:36:39:DE:56:FC";
    private String deviceAddress_rKnee = "D4:36:39:DE:57:5D";
    private String deviceAddress_lAnkle = "D4:36:39:DE:57:AA";
    private String deviceAddress_rAnkle = "D4:36:39:DE:57:D0";

    // TODO: Rename and change types of parameters
    private static boolean isShowDefaultBodyDialogPosBut = false;

    private double[] currentTreatData = DataArray.getCurrentTreatData(CURRENT_TREAT);
    private double[][] dataGotFromScan = new double[10][8]; //10 row(Do No.) 8 column(LE, RE, LW, RW, LK, RK, LA, RA)
    private static double[] defaultBody = new double[]{35, 35, 60, 60, 0, 0, 0, 0};
    private static Map<Integer, Object[]> beaconData = new HashMap<>(); //[address]{name, txPowerLevel, rssi}

    private static double angle;
    private static double distance;

    /* SET variable */
    private static int defaultSet = 1;
    private static int currentSet;
    private static int maxSet;
    /* Doing TIME variable */
    private static int defaultTime = 0;
    private static int currentTime;
    private static int maxTime = 10;

    private String mParam1;
    private String mParam2;

    /*UI Component*/
    private Button butStop;
    private EditText editText_setRound;
    private static TextView tv_treatName;
    private static TextView tv_setNum;
    private static TextView tv_maxSet;
    private static TextView tv_timeNum;
    private static TextView tv_maxTime;
    private static TextView tv_angleNum;
    private static TextView tv_maxAngle;

    private static TextView tv_lElbow;
    private static TextView tv_rElbow;
    private static TextView tv_lWrist;
    private static TextView tv_rWrist;
    private static TextView tv_lKnee;
    private static TextView tv_rKnee;
    private static TextView tv_lAnkle;
    private static TextView tv_rAnkle;

    /* Import Class */
    private static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

    private DataArray doingDataArray = new DataArray();

    private View view;
    private OnFragmentInteractionListener mListener;

    private AlertDialog defaultBodyAlertDialog;
    private static ScanResult mScanResult;



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

        ((MainActivity)getActivity()).setToolbarTitle("เริ่มทำกายภาพ");

        /*Show Getting Default body Dialog */
        //showDefaultBodyDialog();

        /* Run Beacon Scanner */

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

        //bluetoothLeScanner.startScan(mScanCallback);

        startScan();

        return view;}

    private void defineUI() {
        tv_treatName = (TextView) view.findViewById(R.id.fragment_startRe_treatName);   //Treat NAME
        tv_setNum = (TextView) view.findViewById(R.id.fragment_doing_setNum);           //SET
        tv_maxSet = (TextView) view.findViewById(R.id.fragment_doing_setMax);           // Max number of SET
        tv_timeNum = (TextView) view.findViewById(R.id.fragment_doing_timeNum);         //จำนวนครั้งที่ทำได้ตอนนี้
        tv_maxTime = (TextView) view.findViewById(R.id.fragment_doing_timeMax);         //จำนวนครั้งสูงสุดที่ทำได่้ในรอบนั้นๆ
        tv_angleNum = (TextView) view.findViewById(R.id.fragment_doing_angleNum);       //จำนวนมุมที่ทำได้ตอนนี้
        tv_maxAngle = (TextView) view.findViewById(R.id.fragment_doing_angleMax);       //จำนวนมุมสูงสุด

        tv_lElbow = (TextView) view.findViewById(R.id.doing_lElbow);
        tv_rElbow = (TextView) view.findViewById(R.id.doing_rElbow);
        tv_lWrist = (TextView) view.findViewById(R.id.doing_lWrist);
        tv_rWrist = (TextView) view.findViewById(R.id.doing_rWrist);
        tv_lKnee = (TextView) view.findViewById(R.id.doing_lKnee);
        tv_rKnee = (TextView) view.findViewById(R.id.doing_rKnee);
        tv_lAnkle = (TextView) view.findViewById(R.id.doing_lAnkle);
        tv_rAnkle = (TextView) view.findViewById(R.id.doing_rAnkle);

        butStop = (Button) view.findViewById(R.id.fragment_doing_buttonStop);           //Button Stop
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

    /* Set UI Component while Start Up Fragment*/
    private void setFirstUI(){
        tv_treatName.setText(CURRENT_TREAT);
        tv_setNum.setText("1");                                                //Default Set Number
        tv_maxSet.setText(""+maxSet);                                          //Max number of SET
        tv_timeNum.setText("0");                                               //Default number of Doing
        tv_maxTime.setText("10");                                              //MAX TIME to Do
    }

    /* Method */
    private void showPauseCaution(){

        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
        mAlertBuilder.setTitle("หยุด");
        mAlertBuilder.setMessage("หยุดชั่วคราว");

        mAlertBuilder.setPositiveButton("ทำต่อ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        mAlertBuilder.setNegativeButton("กลับไปหน้าก่อนหน้า", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopScan();
                ((MainActivity)getActivity()).onBackPressed();
            }
        });

        AlertDialog alertDialog = mAlertBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE);
    }

    public static void startScan(){
        bluetoothLeScanner.startScan(mScanCallback);
    }
    public static void stopScan(){
        bluetoothLeScanner.stopScan(mScanCallback);
    }

    protected static ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            ScanRecord mScanRecord = result.getScanRecord();
            mScanResult = result;

            if(mScanResult != null) {

                if (result.getDevice().getUuids() == null) {


                    String deviceAddress = result.getDevice().getAddress();
                    int deviceAddressNumber = DataArray.getDeviceAdrressNumber(deviceAddress);

                    String deviceName = mScanResult.getDevice().getName();
                    double txPowerLevel = mScanRecord.getTxPowerLevel();
                    double rssi = mScanResult.getRssi();

                    distance = DistanceAngle.calculateDistance(txPowerLevel, rssi);

                    //Log.e(TAG, "ScanResult:" + mScanResult.toString());
                    Log.e(TAG, "Device Name:" + deviceName);
                    Log.e(TAG, "Device Address:" + deviceAddress);
                    //Log.e("Device UUID:", ""+ Arrays.toString(mScanResult.getDevice().getUuids()));
                    //Log.e(TAG, "Device UUID:" + mScanRecord.getServiceUuids().toArray());
                    //Log.e(TAG, "Device Type:" + mScanResult.getDevice().getType());
                    Log.e(TAG, "TxPowerLevel:" + txPowerLevel);
                    Log.e(TAG, "Rssi:" + rssi);
                    Log.e(TAG, "Distance:" + distance);


                    //beaconData = {deviceAddress : {deviceName, txPowerLevel, Rssi}}
                    beaconData.put(deviceAddressNumber, new Object[]{deviceName, txPowerLevel, rssi});

                    //Log.e(TAG, "beaconData[" + deviceAdrressNumber + "] :" + beaconData.get(deviceAdrressNumber)[0] + "," + beaconData.get(deviceAdrressNumber)[1] + "," + beaconData.get(deviceAdrressNumber)[2]);

                    setTestTextView("" + deviceAddress, distance);

                }
            }
        }
    };

    private void setValueToParam(String deviceAddress){
        switch (deviceAddress){
            case "D4:36:39:DE:54:CB": //L Elbow
                break;
            case "D4:36:39:DE:54:CD": //R Elbow
                break;
            case "D4:36:39:DE:55:82": //L Wrist
                break;
            case "D4:36:39:DE:56:A1": //R Wrist
                break;
            case "D4:36:39:DE:56:FC": //L Knee
                break;
            case "D4:36:39:DE:57:5D": //R Knee
                break;
            case "D4:36:39:DE:57:AA": //L Ankle
                break;
            case "D4:36:39:DE:57:D0": //R Ankle
                break;
            default:
                break;
        }
    }

    private void showDefaultBodyDialog(){
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
        mAlertBuilder.setTitle("กำลังเก็บข้อมูลที่สำคัญ");
        mAlertBuilder.setView(R.layout.dialog_default_body);

        mAlertBuilder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShowDefaultBodyDialogPosBut = true;

            }
        });

        if(isShowDefaultBodyDialogPosBut){
            stopScan();
            //Close Dialog
        }
        else {
            startScan();

        }

        defaultBodyAlertDialog = mAlertBuilder.create();
        defaultBodyAlertDialog.show();
        defaultBodyAlertDialog.getButton(defaultBodyAlertDialog.BUTTON_POSITIVE);

        TextView dialog_default_lElbow = (TextView) defaultBodyAlertDialog.findViewById(R.id.doing_default_lElbow_status);
        TextView dialog_default_rElbow = (TextView) defaultBodyAlertDialog.findViewById(R.id.doing_default_rElbow_status);
        TextView dialog_default_lWrist = (TextView) defaultBodyAlertDialog.findViewById(R.id.doing_default_lWrist_status);
        TextView dialog_default_rWrist = (TextView) defaultBodyAlertDialog.findViewById(R.id.doing_default_rWrist_status);
        TextView dialog_default_lKnee = (TextView) defaultBodyAlertDialog.findViewById(R.id.doing_default_lKnee_status);
        TextView dialog_default_rKnee = (TextView) defaultBodyAlertDialog.findViewById(R.id.doing_default_rKnee_status);
        TextView dialog_default_lAnkle = (TextView) defaultBodyAlertDialog.findViewById(R.id.doing_default_lAnkle_status);
        TextView dialog_default_rAnkle = (TextView) defaultBodyAlertDialog.findViewById(R.id.doing_default_rAnkle_status);

        //setRoundEditText = (EditText) alertDialog.findViewById(R.id.dialog_setRound_editText_num);
        //setRoundEditText.setHint("ไม่ควรเกิน 5 รอบ");
    }

    private static void setDefaultBody(String address, double distance){
        switch (address) {
            case "D4:36:39:DE:54:CB":
                defaultBody[0] = distance;
                break;
            case "D4:36:39:DE:54:CD":
                defaultBody[1] = distance;
                break;
            case "D4:36:39:DE:55:82":
                defaultBody[2] = distance;
                break;
            case "D4:36:39:DE:56:A1":
                defaultBody[3] = distance;
                break;
            case "D4:36:39:DE:56:FC":
                defaultBody[4] = distance;
                break;
            case "D4:36:39:DE:57:5D":
                defaultBody[5] = distance;
                break;
            case "D4:36:39:DE:57:AA":
                defaultBody[6] = distance;
                break;
            case "D4:36:39:DE:57:D0":
                defaultBody[7] = distance;
                break;
            default:
                break;
        }
    }

    private static void setTestTextView(String address,double distance){
        //String textDistance = DistanceAngle.getTextDouble(distance);
        //String textDistance = ""+distance;

        angle = DistanceAngle.findSolutionAndAngle(CURRENT_TREAT, defaultBody, distance);

        switch (address){
            case "D4:36:39:DE:54:CB":
                //tv_lElbow.setText(textDistance);

                tv_lElbow.setText(String.format("%.2f",distance-20));
                //tv_angleNum.setText(""+angle);
                break;
            case "D4:36:39:DE:54:CD":
                //tv_rElbow.setText(textDistance);
                tv_rElbow.setText(String.format("%.2f",distance-20));
                break;
            case "D4:36:39:DE:55:82":
                //tv_lWrist.setText(textDistance);
                break;
            case "D4:36:39:DE:56:A1":
                //tv_rWrist.setText(textDistance);
                break;
            case "D4:36:39:DE:56:FC":
                //tv_lKnee.setText(textDistance);
                break;
            case "D4:36:39:DE:57:5D":
                //tv_rKnee.setText(textDistance);
                break;
            case "D4:36:39:DE:57:AA":
                //tv_lAnkle.setText(textDistance);
                break;
            case "D4:36:39:DE:57:D0":
                //tv_rAnkle.setText(textDistance);
                break;
            default:
                break;
        }
    }

}
