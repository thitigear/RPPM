package com.example.gear.rppm.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;
import com.example.gear.rppm.other.CustomListViewAdapter;

import java.util.List;

public class ManualFragment extends Fragment {

    private static String CURRENT_TAG = "manual";

    private String[] manualChoice;

    /*UI Component*/
    private ListView frag_manual_lv_01;
    private View view;

    /*Import Class*/
    private CustomListViewAdapter adapter;

    public ManualFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_manual, container, false);

        ((MainActivity)getActivity()).setCurrentTag(CURRENT_TAG);
        ((MainActivity)getActivity()).setToolbarTitleById(R.string.nav_manual);

        /*Setup important param*/
        manualChoice = getResources().getStringArray(R.array.manual_choice);
        adapter = new CustomListViewAdapter(getContext(), manualChoice);

        /*Setup ListView*/
        frag_manual_lv_01 = (ListView) view.findViewById(R.id.fragment_manual_lv);
        frag_manual_lv_01.setAdapter(adapter);

        frag_manual_lv_01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("MANUAL Fragment:", manualChoice[position]);
                showManualDialog(manualChoice[position]);
            }
        });

        return view;
    }

    public void showManualDialog(String title){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(title);

        switch (title){
            case "เมนูของแอพพลิเคชัน":
            case "Application's menu":
                alertDialogBuilder.setView(R.layout.manual_basic);
                alertDialogBuilder.setPositiveButton(getResources().getText(R.string.manual_button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            case "การใช้งานฟังก์ชันกายภาพบำบัด":
            case "How to use physical therapy function":
                alertDialogBuilder.setView(R.layout.manual_function);
                alertDialogBuilder.setPositiveButton(getResources().getText(R.string.manual_button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            case "ตำแหน่งเซ็นเซอร์และตำแหน่งการวางโทรศัพท์มือถือ":
            case "How to set sensor and smart phone position":
                alertDialogBuilder.setView(R.layout.manual_position);
                alertDialogBuilder.setPositiveButton(getResources().getText(R.string.manual_button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            case "ข้อแนะนำอื่นๆ":
            case "Others":
                alertDialogBuilder.setView(R.layout.manual_rec);
                alertDialogBuilder.setPositiveButton(getResources().getText(R.string.manual_button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;

        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE);

    }
}
