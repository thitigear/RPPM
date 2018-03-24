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

public class CautionAdapter extends BaseAdapter {

    private Context mContext;
    private String[] cautionList = new String[]{"1.aaaa"
            ,"2.bbbb"
            ,"3.cccc"
            ,"4.dddd"
            ,"5.eeee"};

    public CautionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return cautionList.length;
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
            convertView = mInflater.inflate(R.layout.list_caution, parent, false);

        TextView textView = (TextView)convertView.findViewById(R.id.clv_row_textView);
        textView.setText(cautionList[position]);

        return convertView;
    }
}
