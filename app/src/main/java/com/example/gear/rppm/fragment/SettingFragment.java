package com.example.gear.rppm.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;

import java.util.Locale;

public class SettingFragment extends Fragment {

    private static int spinnerItemPosition;

    private static String CURRENT_TAG = "setting";
    private static String[] languageName;

    private ArrayAdapter<String> languageAdapter;

    private static Button frag_setting_but_apply;
    private static Button frag_setting_but_cancel;
    private static Spinner frag_setting_spinner_language;

    private View view;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        ((MainActivity)getActivity()).setCurrentTag(CURRENT_TAG);
        ((MainActivity)getActivity()).setToolbarTitleById(R.string.nav_setting);

        setUI();

        languageName = getResources().getStringArray(R.array.language);

        Log.e("Language", String.format("%s, %s", languageName[0], languageName[1]));

        languageAdapter = new ArrayAdapter<String>(view.getContext()
                , android.R.layout.simple_spinner_dropdown_item, languageName);

        frag_setting_spinner_language.setAdapter(languageAdapter);


        frag_setting_but_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("item pos select", languageName[frag_setting_spinner_language.getSelectedItemPosition()]);
                CheckBeaconFragment.clearSDeviceList();
                Configuration config = new Configuration();
                config.locale = new Locale(getLocaleFromSpinner(frag_setting_spinner_language.getSelectedItemPosition()));
                getResources().updateConfiguration(config, null);

            }
        });
        frag_setting_but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });


        return view;
    }

    private void setUI(){
        frag_setting_spinner_language = (Spinner) view.findViewById(R.id.fragment_setting_spinner_language);
        frag_setting_but_apply = (Button) view.findViewById(R.id.fragment_setting_but_apply);
        frag_setting_but_cancel = (Button) view.findViewById(R.id.fragment_setting_but_cancel);

    }

    private String getLocaleFromSpinner(int itemSelected_position){
        switch (itemSelected_position){
            case 0:
                ((MainActivity)getActivity()).updateLanguage("th");
                return "th";
            case 1:
                ((MainActivity)getActivity()).updateLanguage("en");
                return "en";
            default:
                ((MainActivity)getActivity()).updateLanguage("th");
                return "th";
        }
    }

}
