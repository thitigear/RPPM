package com.example.gear.rppm.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;
import com.example.gear.rppm.other.CautionAdapter;

import java.util.Objects;

public class HomeFragment extends Fragment{

    private int navItemIndex = 0;


    private static String CURRENT_TAG = "home";

    private Button frag_home_choice_arm;
    private Button frag_home_choice_leg;

    private static String TAG_CHOOSE_CURRENT = "";

    private static String TAG_ARM = "arm";
    private static String TAG_LEG = "leg";

    private CautionAdapter cautionAdapter;
    private View v;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);

        ((MainActivity) getActivity()).setCurrentTag(CURRENT_TAG);
        ((MainActivity) getActivity()).setToolbarTitleById(R.string.nav_home);

        frag_home_choice_arm = (Button) v.findViewById(R.id.fragment_home_button1);
        frag_home_choice_leg = (Button) v.findViewById(R.id.fragment_home_button2);

        frag_home_choice_arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG_ARM, "ARM!!!!!!!!!!!!");
                TAG_CHOOSE_CURRENT = TAG_ARM;
                showCaution();
            }
        });

        frag_home_choice_leg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG_LEG, "LEG!!!!!!!!!!!!");
                TAG_CHOOSE_CURRENT = TAG_LEG;
                showCaution();
            }
        });

        return v;
    }

    public void replaceNewFragment(final Fragment newFragment, final String tag) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.frame, newFragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showCaution(){

        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(v.getContext());
        mAlertBuilder.setView(R.layout.list_caution);

        mAlertBuilder.setPositiveButton(getResources().getString(R.string.caution_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Objects.equals(TAG_CHOOSE_CURRENT, TAG_ARM)){
                    replaceNewFragment(new ArmHomeFragment(), TAG_ARM);
                    TAG_CHOOSE_CURRENT = "";

                } else if (Objects.equals(TAG_CHOOSE_CURRENT, TAG_LEG)){
                    replaceNewFragment(new LegHomeFragment(), TAG_LEG);
                    TAG_CHOOSE_CURRENT = "";
                }
            }
        });

        AlertDialog alertDialog = mAlertBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.button_buttom);
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.textSecondary));
    }

}
