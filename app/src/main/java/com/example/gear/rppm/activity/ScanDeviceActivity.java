package com.example.gear.rppm.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ScanDeviceActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

    private Map<String, String> sDeviceList = new HashMap<String, String>();

    private ScanResult mScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startScan();

    }

    public void startScan(){
        bluetoothLeScanner.startScan(mScanCallback);
    }

    public void stopScan(){
        bluetoothLeScanner.stopScan(mScanCallback);
    }

    public void pauseScan(){
        bluetoothLeScanner.flushPendingScanResults(mScanCallback);
    }

    protected ScanCallback mScanCallback = new ScanCallback(){
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

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


            if (mScanResult.getDevice().getUuids() == null) {//-------------------------------------- Update Device Rssi & TxPowerLevel


                if (sDeviceList.containsKey(mScanResult.getDevice().getAddress())) { //getAddress()

                    sDeviceList.put(result.getDevice().getAddress(), "" + result.getDevice().getName());

                    //beaconListAdapter.setsBeaconList(sDeviceList);
                    //beaconListView.setAdapter(beaconListAdapter);

                    //deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);
                    //beaconListAdapter.setmBeaconList(deviceList);
                    //deviceList.put(scanResult.getDevice().getName(), capsuleRssiTxPower);
                    //tempCapsuleRssiTxPower = capsuleRssiTxPower;

                } else {//-------------------------------------------------------------------------- Add Device to DeviceList

                    sDeviceList.put(result.getDevice().getAddress(), "" + result.getDevice().getName());

                    //beaconListAdapter.setsBeaconList(sDeviceList);
                    //beaconListView.setAdapter(beaconListAdapter);

                    //deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);
                    //beaconListAdapter.setmBeaconList(deviceList);
                }
                Log.e("","******************* Device Key : "  + sDeviceList);
            }
        }
    };

    public Map<String, String> getsDeviceList() {
        return sDeviceList;
    }
}
