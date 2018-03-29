package com.example.gear.rppm.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
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

import com.example.gear.rppm.*;
import com.example.gear.rppm.other.CheckBeaconListViewAdapter;
import com.example.gear.rppm.other.CustomListViewAdapter;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;
    private CheckBeaconListViewAdapter beaconListAdapter;

    private BeaconManager beaconManager;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

    private static final String TAG = "MainActivity";
    //private CoreF coreF = new CoreF();
    private Map<String, int[]> deviceList = new HashMap<String, int[]>();
    private String[][] sDeviceList;
    private int[] capsuleRssiTxPower;
    private int[] tempCapsuleRssiTxPower;

    private ScanResult scanResult;


    private int[] resId = { R.drawable.ic_action_menu_add
            , R.drawable.ic_action_menu_all_beacon, R.drawable.ic_action_menu_history};

    private String[][] beaconList = { {"Beacon1","Beacon1 UUID"}
            , {"Beacon2", "Beacon2 UUID"}
            , {"Beacon3", "Beacon3 UUID"}
            , {"Beacon4", "Beacon4 UUID"}
            , {"Beacon5", "Beacon5 UUID"}
            , {"Beacon6", "Beacon6 UUID"}
            , {"Beacon7", "Beacon7 UUID"}
            , {"Beacon8", "Beacon8 UUID"}};


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

        //beaconListAdapter = new CheckBeaconListViewAdapter(getContext(), deviceList);

        final ListView beaconListView = (ListView) view.findViewById(R.id.fragment_chk_beacon_lv);


        bluetoothLeScanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);

                scanResult = result;

                //if ("hm".contains(scanResult.getDevice().getName().toLowerCase())) {

                    capsuleRssiTxPower = new int[]{scanResult.getRssi(), scanResult.getScanRecord().getTxPowerLevel()};

                    Log.e(TAG, "******************* Device NAME : " +scanResult.getDevice().getName());
                    Log.e(TAG, "******************* Device Key : " +deviceList.keySet());

                Log.e(TAG, "!!!!!!!!!!!!!!!!!! BeaconList : " +beaconList);
                beaconListAdapter = new CheckBeaconListViewAdapter(getContext(), beaconList);

                    //if(sDeviceList[0].equals())

                    //if (sDeviceList.containsKey(scanResult.getDevice().getName())) { //getAddress()
                    /* Update Device Rssi & TxPowerLevel*/
                        //deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);
                     /**   deviceList.put(scanResult.getDevice().getName(), capsuleRssiTxPower);
                        sDeviceList.put(scanResult.getDevice().getName(), scanResult.getDevice().getAddress());*/
                        //tempCapsuleRssiTxPower = capsuleRssiTxPower;

                    //} else {
                    /* Add Device to DeviceList */
                        //deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);}
                     /**   deviceList.put(scanResult.getDevice().getName(), capsuleRssiTxPower);
                        sDeviceList.put(scanResult.getDevice().getName(), scanResult.getDevice().getAddress());*/
                        //beaconListAdapter = new CheckBeaconListViewAdapter(getContext(), deviceList);

                        //Log.e("UUID", ""+result.getDevice().getUuids());

                    /**if (deviceList.size() >= 3) {
                        long start = System.currentTimeMillis();
                        long elapsedTimeMillis = System.currentTimeMillis()-start;
                        while(elapsedTimeMillis < 2){
                             //op_kneeL.setText("" + core.calculateDistance(deviceList.get("D4:36:39:DE:56:C6")[0], deviceList.get("D4:36:39:DE:56:C6")[1]));
                             //op_kneeL.setText("" + core.calculateDistance(deviceList.get("50:8C:B1:75:1C:3C")[0], deviceList.get("50:8C:B1:75:1C:3C")[1]));
                             //op_ankleL.setText("" + core.calculateDistance(deviceList.get("D4:36:39:DE:57:D0")[0], deviceList.get("D4:36:39:DE:57:D0")[1]-20));
                             start = System.currentTimeMillis();
                        }
                    }*/

                    Log.e(TAG, "******************* Device Key : " +sDeviceList);
                    //beaconListAdapter = new CheckBeaconListViewAdapter(getContext(), deviceList);

                //}

                beaconListAdapter = new CheckBeaconListViewAdapter(getContext(), beaconList);
                beaconListView.setAdapter(beaconListAdapter);
            }
        });

        //register();
        //beaconListAdapter = new CheckBeaconListViewAdapter(getContext(), deviceList);
        //beaconListView.setAdapter(beaconListAdapter);

        beaconListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
        });

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
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            // Unbind scan beacon progress
            if (beaconManager != null)
                beaconManager.unbind(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
/*
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(
                    Collection<Beacon> beacons, Region region) {
                Log.i("", "IS_SCAN_BEACON " + CheckBeaconFragment.IS_SCAN_BEACON);

                if (CheckBeaconFragment.IS_SCAN_BEACON) {
                    Log.i("", "Found " + beacons.size() + " beacon!");

                    if (beacons.size() > 0) {*/
                        /**
                         * Begin transfer data
                         */
                        /**for (final Beacon beacon : beacons) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    /*getDataViaBLE(getActivity(), beacon.getId1() + "",
                                            beacon.getId2() + "", beacon.getId3() + "");*/
                                    /**Log.e("com.example.gear.rppm", ":"+ beacon.getId1()+","+beacon.getId2()+","+beacon.getId3());
                                }
                            });

                        }
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(
                    new Region(UUID, null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }*/
/**
    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
    }*/

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

    public static String UUID = "ok";

    //private void getDataViaBLE(FragmentActivity activity, Identifier id1, Identifier id2, Identifier id3);
}
