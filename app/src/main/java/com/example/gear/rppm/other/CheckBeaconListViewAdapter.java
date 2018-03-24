package com.example.gear.rppm.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gear.rppm.R;

/**
 * Created by Gear on 3/24/2018.
 */

public class CheckBeaconListViewAdapter extends BaseAdapter {

    private Context mContext;
    private String[][] beaconList;

    public CheckBeaconListViewAdapter(Context mContext, String[][] beaconList) {
        this.mContext = mContext;
        this.beaconList = beaconList;
    }

    @Override
    public int getCount() {
        return beaconList.length;
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
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2 ,parent, false);

        TextView beaconName = (TextView)convertView.findViewById(android.R.id.text1);
        beaconName.setText(beaconList[position][0]);

        TextView beaconDetail = (TextView)convertView.findViewById(android.R.id.text2);
        beaconDetail.setText(beaconList[position][1]);

        //ImageView imageView = (ImageView)convertView.findViewById(R.id.clv_row_imageView);
        //imageView.setBackgroundResource(resId[position]);

        return convertView;
    }
}