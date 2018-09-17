package com.example.gear.rppm.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gear.rppm.R;

/**
 * Created by Gear on 3/23/2018.
 */

public class CustomListViewAdapter extends BaseAdapter{

    private Context mContext;
    private String[] strName;
    private int[] resId;

    public CustomListViewAdapter(Context mContext, String[] strName, int[] resId) {
        this.mContext = mContext;
        this.strName = strName;
        this.resId = resId;
    }

    public CustomListViewAdapter(Context mContext, String[] strName) {
        this.mContext = mContext;
        this.strName = strName;
    }

    @Override
    public int getCount() {
        return strName.length;
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
            convertView = mInflater.inflate(R.layout.clistview_row, parent, false);

        TextView textView = (TextView)convertView.findViewById(R.id.clv_row_textView);
        textView.setText(strName[position]);
        textView.setBackgroundResource(R.color.blue_blue001);

        //ImageView imageView = (ImageView)convertView.findViewById(R.id.clv_row_imageView);
        //imageView.setBackgroundResource(resId[position]);

        return convertView;
    }
}
