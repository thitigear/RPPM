package com.example.gear.rppm.other;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.gear.rppm.R;
import com.example.gear.rppm.fragment.DoingFragment;


public class ReplaceFragment extends Fragment{

    public ReplaceFragment() {
    }

    public void replaceNewFragment(final Fragment newFragment, final String flagTreat) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.frame, newFragment, flagTreat);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceDoingFragment(final String currentTreat, final String flagTreat){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        DoingFragment.setCurrentTreat(currentTreat);
        DoingFragment.setFlagTreat(flagTreat);

        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.frame, new DoingFragment(), flagTreat);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
