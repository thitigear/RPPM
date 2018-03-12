package com.example.gear.rppm.other;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.gear.rppm.R;
import com.example.gear.rppm.fragment.ArmHomeFragment;
import com.example.gear.rppm.fragment.CheckBeaconFragment;
import com.example.gear.rppm.fragment.HomeFragment;
import com.example.gear.rppm.fragment.LegHomeFragment;

/**
 * Created by Gear on 3/1/2018.
 */

public class LoadFragment extends AppCompatActivity{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    // index to identify current nav menu item
    public static int navItemIndex;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_CHECK_BEACON = "check beacon";
    private static final String TAG_ARM = "arm";
    private static final String TAG_LEG = "leg";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    public LoadFragment() {

    }

    public LoadFragment(int navIndex) {
        selectNavMenu(navIndex);
        setToolbarTitle();
        getFragment(navIndex);
    }




    public void selectNavMenu() {
        //navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        //navigationView.getMenu().getItem(navItemIndex);
        navigationView.getMenu().getItem(navItemIndex);
    }

    public void selectNavMenu(int navIndex) {
        //navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        //navigationView.getMenu().getItem(navItemIndex);
        navigationView.getMenu().getItem(navItemIndex);
    }

    public void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public static Fragment getFragment(int navIndex) {
        switch (navIndex) {
            case 0:
                // home fragment
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // check beacon fragment
                CheckBeaconFragment checkBeaconFragment = new CheckBeaconFragment();
                return checkBeaconFragment;
            case 2:
                // arm fragment
                ArmHomeFragment armHomeFragment = new ArmHomeFragment();
                return armHomeFragment;
            case 3:
                // leg fragment
                LegHomeFragment legHomeFragment = new LegHomeFragment();
                return legHomeFragment;

            default:
                return new HomeFragment();
        }
    }


}
