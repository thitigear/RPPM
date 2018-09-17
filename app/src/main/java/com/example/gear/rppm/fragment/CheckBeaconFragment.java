package com.example.gear.rppm.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.gear.rppm.*;
import com.example.gear.rppm.activity.MainActivity;
import com.example.gear.rppm.other.CheckBeaconListViewAdapter;
import com.example.gear.rppm.other.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckBeaconFragment extends Fragment {
    private static final boolean IS_SCAN_BEACON = true;
    private static final String TAG = "MainActivity";

    // TODO: Rename and change types of parameters
    private String name;

    private static String CURRENT_TAG = "check beacon";

    private View view;

    //-------------------------------------------------------------------------------------

    BluetoothAdapter bluetoothAdapter;
    static BluetoothLeScanner bluetoothLeScanner;

    private CheckBeaconListViewAdapter beaconListAdapter;

    private List<ScanFilter> filter;
    private ListView beaconListView;

    private static Map<String, String> sDeviceList = new HashMap<>();

    private String my_UUID = "b911df37-a9d0-43bf-af14-0b4a3e20b50c";


    private String[] deviceList = new String[]{"D4:36:39:DE:54:CB",
            "D4:36:39:DE:54:CD",
            "D4:36:39:DE:55:82",
            "D4:36:39:DE:56:FC"};

    /**private int[] resId = { R.drawable.ic_action_menu_add
            , R.drawable.ic_action_menu_all_beacon, R.drawable.ic_action_menu_history};

    */


    public CheckBeaconFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_check_beacon, container, false);
        ((MainActivity)getActivity()).setCurrentTag(CURRENT_TAG);
        ((MainActivity)getActivity()).setToolbarTitleById(R.string.nav_check_beacon);
        //((MainActivity)getActivity()).setToolbarTitleByString("ตรวจสอบสถานะอุปกรณ์");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        startScan(mScanCallback);

        beaconListView = (ListView) view.findViewById(R.id.fragment_chk_beacon_lv);
        beaconListAdapter = new CheckBeaconListViewAdapter(getContext(), sDeviceList);

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


    @Override
    public void onDestroy() {
        super.onDestroy();
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

            Log.e("ScanResult:", ""+ result.toString());
            Log.e("Device Name:", ""+ result.getDevice().getName());
            Log.e("Device Address:", ""+ result.getDevice().getAddress());
            Log.e("Device Type:", ""+ result.getDevice().getType());

            if(result.getDevice().getName() != null){
                if (result.getDevice().getUuids() == null) {//-------------------------------------- Update Device Rssi & TxPowerLevel

                    if (sDeviceList.containsKey(result.getDevice().getAddress())) { //getAddress()

                        if(MainActivity.getCurrentLanguage().equals("th")){
                            switch (result.getDevice().getAddress()) {
                                case "D4:36:39:DE:54:CB":
                                    sDeviceList.put(result.getDevice().getAddress(), "ศอก");
                                    break;
                                case "D4:36:39:DE:54:CD":
                                    sDeviceList.put(result.getDevice().getAddress(), "ข้อมือ");
                                    break;
                                case "D4:36:39:DE:55:82":
                                    sDeviceList.put(result.getDevice().getAddress(), "เข่า");
                                    break;
                                case "D4:36:39:DE:56:FC":
                                    sDeviceList.put(result.getDevice().getAddress(), "ข้อเท้า");
                                    break;
                            }
                        } else {
                            switch (result.getDevice().getAddress()) {
                                case "D4:36:39:DE:54:CB":
                                    sDeviceList.put(result.getDevice().getAddress(), "Elbow");
                                    break;
                                case "D4:36:39:DE:54:CD":
                                    sDeviceList.put(result.getDevice().getAddress(), "Wrist");
                                    break;
                                case "D4:36:39:DE:55:82":
                                    sDeviceList.put(result.getDevice().getAddress(), "Knee");
                                    break;
                                case "D4:36:39:DE:56:FC":
                                    sDeviceList.put(result.getDevice().getAddress(), "Ankle");
                                    break;
                            }
                        }

                        beaconListAdapter.setsBeaconList(sDeviceList);
                        beaconListView.setAdapter(beaconListAdapter);


                    } else {//-------------------------------------------------------------------------- Add Device to DeviceList

                        sDeviceList.put(result.getDevice().getAddress(), "" + result.getDevice().getName());

                        beaconListAdapter.setsBeaconList(sDeviceList);
                        beaconListView.setAdapter(beaconListAdapter);
                    }
                    Log.e(TAG, "******************* Device Key : " + sDeviceList);
                }
            }

        }

    };

    private void startScan(ScanCallback scanCallback){
        bluetoothLeScanner.startScan(scanCallback);
    }

    public static void stopScan(ScanCallback scanCallback){
        bluetoothLeScanner.stopScan(scanCallback);
    }

    public static void clearSDeviceList(){
        sDeviceList.clear();
    }

}
