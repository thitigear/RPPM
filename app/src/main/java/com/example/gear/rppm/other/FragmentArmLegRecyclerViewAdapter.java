package com.example.gear.rppm.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gear.rppm.R;

import java.util.HashMap;

public class FragmentArmLegRecyclerViewAdapter extends RecyclerView.Adapter<FragmentArmLegRecyclerViewAdapter.ViewHolder> {

    private int targetListView;
    private HashMap<Integer, String> treatNameList = new HashMap<>();

    private Context context;

    private View view;

    public FragmentArmLegRecyclerViewAdapter(Context context,
                                             int targetListView_id,
                                             HashMap<Integer, String> treatNameList) {
        this.context = context;
        this.targetListView = targetListView_id;
        this.treatNameList = treatNameList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_treatName;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_treatName = (TextView) view.findViewById(R.id.frag_home_arm_leg_tv_treatName);
        }

        public TextView tv_treatName() {
            return tv_treatName;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(targetListView, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_treatName().setText(treatNameList.get(position));
    }

    @Override
    public int getItemCount() {

        return treatNameList.size();
    }
}
