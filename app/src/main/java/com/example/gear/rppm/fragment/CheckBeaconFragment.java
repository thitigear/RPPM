package com.example.gear.rppm.fragment;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gear.rppm.*;
import com.example.gear.rppm.activity.ScanDeviceActivity;
import com.example.gear.rppm.other.CheckBeaconListViewAdapter;
import com.example.gear.rppm.other.CustomListViewAdapter;
import com.example.gear.rppm.other.ScanFilterUtils;
import com.example.gear.rppm.other.UuidAdapter;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckBeaconFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckBeaconFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckBeaconFragment extends Fragment {//implements BeaconConsumer{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";;
    private static final boolean IS_SCAN_BEACON = true;
    private static final String TAG = "MainActivity";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;

    //-------------------------------------------------------------------------------------
    private BeaconManager beaconManager;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

    private CheckBeaconListViewAdapter beaconListAdapter;

    private int[] capsuleRssiTxPower;
    private int[] tempCapsuleRssiTxPower;

    private List<ScanFilter> filter;
    private ListView beaconListView;



    //private Map<String, int[]> deviceList = new HashMap<String, int[]>();
    private Map<String, String> sDeviceList = new HashMap<>();
    //private Map<String, ArrayList<>> oDeviceList = new HashMap<String, Object[]>();

    private ScanResult mScanResult;
    //private ScanFilter mScanFilter;
    //private ScanSettings mScanSettings;

    private String my_UUID = "b911df37-a9d0-43bf-af14-0b4a3e20b50c";

    private String[][] deviceListWithPart = { {"D4:36:39:DE:56:FC","HMSoft"}
            , {"D4:36:39:DE:57:D0", "HMSoft"}
            , {"Beacon3", "Beacon3 UUID"}
            , {"Beacon4", "Beacon4 UUID"}
            , {"Beacon5", "Beacon5 UUID"}
            , {"Beacon6", "Beacon6 UUID"}
            , {"Beacon7", "Beacon7 UUID"}
            , {"Beacon8", "Beacon8 UUID"}};

    /**private int[] resId = { R.drawable.ic_action_menu_add
            , R.drawable.ic_action_menu_all_beacon, R.drawable.ic_action_menu_history};

    */


    public CheckBeaconFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckBeaconFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckBeaconFragment newInstance(String param1, String param2) {
        CheckBeaconFragment fragment = new CheckBeaconFragment();
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
        view = inflater.inflate(R.layout.fragment_check_beacon, container, false);

        //uuidAdapter = new UuidAdapter();

        //Log.e("SCANFILTER",ScanFilterUtils.getScanFilter().getDeviceName());

        //sDeviceList = ((ScanDeviceActivity)getActivity()).getsDeviceList();

        beaconListView = (ListView) view.findViewById(R.id.fragment_chk_beacon_lv);

        beaconListAdapter = new CheckBeaconListViewAdapter(getContext(), sDeviceList);



        //setScanFilter();
        //setScanSettings();

        bluetoothLeScanner.startScan(mScanCallback);


        beaconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), sDeviceList.keySet().toArray()[position].toString(), Toast.LENGTH_LONG).show();
                showDeviceDesc(position);
            }
        });

        /**
         * bluetoothLeScanner.startScan(new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        //register();
        //beaconListAdapter = new CheckBeaconListViewAdapter(getContext(), deviceList);
        //beaconListView.setAdapter(beaconListAdapter);
        }
        });*/

        return view;
    }

    /**private void register() {
        beaconManager = BeaconManager.getInstanceForApplication(getActivity());
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        try {
            // todo
            beaconManager.getBeaconParsers().add(new BeaconParser().
                    setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // CAN SEE THIS LOG CAT, NO EXCEPTION
        Log.i("", "Register Service");

        beaconManager.bind(this);
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    //setScanFilter, setScanSetting

    /**
    private void setScanFilter() {
        ScanFilter.Builder mBuilder = new ScanFilter.Builder();
        ByteBuffer mManufacturerData = ByteBuffer.allocate(23);
        ByteBuffer mManufacturerDataMask = ByteBuffer.allocate(24);
        //byte[] uuid = UuidAdapter.getBytesFromUUID(UUID.fromString(my_UUID));
        byte[] uuid = null;
        mManufacturerData.put(0, (byte)0xBE);
        mManufacturerData.put(1, (byte)0xAC);
        for (int i=2; i<=17; i++) {
            mManufacturerData.put(i, uuid[i-2]);
        }
        for (int i=0; i<=17; i++) {
            mManufacturerDataMask.put((byte)0x01);
        }
        mBuilder.setManufacturerData(224, mManufacturerData.array(), mManufacturerDataMask.array());
        mScanFilter = mBuilder.build();
    }

    /**private void setScanSettings() {
        ScanSettings.Builder mBuilder = new ScanSettings.Builder();
        mBuilder.setReportDelay(0);
        mBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        mScanSettings = mBuilder.build();
    }*/

    protected ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            ScanRecord mScanRecord = result.getScanRecord();
            //byte[] manufacturerData = mScanRecord.getManufacturerSpecificData(224);
            //int mRssi = result.getRssi();

            mScanResult = result;

            Log.e("ScanResult:", ""+ mScanResult.toString());
            Log.e("Device Name:", ""+ mScanResult.getDevice().getName());
            Log.e("Device Address:", ""+ mScanResult.getDevice().getAddress());
            //Log.e("Device UUID:", ""+ Arrays.toString(mScanResult.getDevice().getUuids()));
            Log.e("Device UUID:", ""+ mScanRecord.getServiceUuids().toArray());
            Log.e("Device Type:", ""+ mScanResult.getDevice().getType());
            // int mScanRecord.getTxPowerLevel()


            if (mScanResult.getDevice().getUuids() == null){//-------------------------------------- Update Device Rssi & TxPowerLevel


                if (sDeviceList.containsKey( mScanResult.getDevice().getAddress())) { //getAddress()

                    sDeviceList.put(result.getDevice().getAddress(), ""+result.getDevice().getName());

                    beaconListAdapter.setsBeaconList(sDeviceList);
                    beaconListView.setAdapter(beaconListAdapter);

                    //deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);
                    //beaconListAdapter.setmBeaconList(deviceList);
                    //deviceList.put(scanResult.getDevice().getName(), capsuleRssiTxPower);
                    //tempCapsuleRssiTxPower = capsuleRssiTxPower;

                } else {//-------------------------------------------------------------------------- Add Device to DeviceList

                    sDeviceList.put(result.getDevice().getAddress(), ""+result.getDevice().getName());

                    beaconListAdapter.setsBeaconList(sDeviceList);
                    beaconListView.setAdapter(beaconListAdapter);

                    //deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);
                    //beaconListAdapter.setmBeaconList(deviceList);
                }
                Log.e(TAG, "******************* Device Key : " +sDeviceList);
            }

        }

    };

    public void showDeviceDesc(int position){

        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
        mAlertBuilder.setView(R.layout.chk_beacon_device_desc_view);
        mAlertBuilder.setTitle("Detail");

        mAlertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = mAlertBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
    }




}
