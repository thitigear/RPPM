package com.example.gear.rppm.other;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gear.rppm.R;

import java.util.Map;

/**
 * Created by Gear on 3/24/2018.
 */

public class CheckBeaconListViewAdapter extends BaseAdapter {

    private Context mContext;
    private String[][] beaconList;
    private Map<String, int[]> mBeaconList;

    public CheckBeaconListViewAdapter(Context mContext, String[][] beaconList) {
        this.mContext = mContext;
        this.beaconList = beaconList;
    }

    public CheckBeaconListViewAdapter(Context mContext, Map<String,int[]> beaconList) {
        this.mContext = mContext;
        this.mBeaconList = beaconList;
    }

    /*public CheckBeaconListViewAdapter(Context mContext, String[][] beaconList) {
        this.mContext = mContext;
        this.sBeaconList = beaconList;
    }*/

    @Override
    public int getCount() {
        return mBeaconList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            //convertView = mInflater.inflate(android.R.layout.simple_list_item_2 ,parent, false);
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2 ,parent, false);

        //Log.e("SBEACONLIST NAME", beaconList[position][0]);

        TextView beaconName = (TextView)convertView.findViewById(android.R.id.text1);
        //beaconName.setText(sBeaconList.keySet().toArray()[position].toString());
        beaconName.setText(beaconList[position][0]);


        Log.e("SBEACONLIST ADDRESS", beaconList[position][1]);
        TextView beaconDetail = (TextView)convertView.findViewById(android.R.id.text2);
        //beaconDetail.setText(mBeaconList.get(mBeaconList.keySet().toArray()[position].toString()).toString());
        //beaconDetail.setText(sBeaconList.get(sBeaconList.keySet().toArray()[position]).toString());
        beaconDetail.setText(beaconList[position][1]);

        //ImageView imageView = (ImageView)convertView.findViewById(R.id.clv_row_imageView);
        //imageView.setBackgroundResource(resId[position]);

        return convertView;
    }


}
