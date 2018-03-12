package com.example.gear.rppm.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import com.example.gear.rppm.R;
import com.example.gear.rppm.fragment.CautionFragment;
import com.example.gear.rppm.fragment.HomeFragment;
import com.example.gear.rppm.fragment.CheckBeaconFragment;
import com.example.gear.rppm.fragment.ArmHomeFragment;
import com.example.gear.rppm.fragment.LegHomeFragment;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener
        , CheckBeaconFragment.OnFragmentInteractionListener
        , ArmHomeFragment.OnFragmentInteractionListener
        , LegHomeFragment.OnFragmentInteractionListener
        , CautionFragment.OnFragmentInteractionListener{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    //private FloatingActionButton fab;
    private Button fragment_home_choice_arms;
    private Button fragment_home_choice_legs;

    // index to identify current nav menu item
    private int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_CHECK_BEACON = "check beacon";
    private static final String TAG_ARM = "arm";
    private static final String TAG_LEG = "leg";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add toolbar to app
        toolbar = (Toolbar) findViewById(R.id.toolbar); //in app_bar_sliding_menu_xml
        setSupportActionBar(toolbar);

        //selectToolbarNav();

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */

    public void loadFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            /**toggleFab();*/
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        /**toggleFab();*/

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    public Fragment getFragment() {
        switch (navItemIndex) {
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

    public void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public void setToolbarTitle(int navItemIndex) {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public void setToolbarTitleById(int stringID) {
        getSupportActionBar().setTitle(stringID);
    }

    public void selectNavMenu() {
        //navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        navigationView.getMenu().getItem(navItemIndex);
    }

    public void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_check_beacon:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_CHECK_BEACON;
                        break;
                    case R.id.nav_arm:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ARM;
                        break;
                    case R.id.nav_leg:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_LEG;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadFragment(); // old loadHomeFragment

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);


        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadFragment();
                return;
            }
            else {
                setToolbarTitle(0);
            }
        }
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**    private void scanBeacon(){

     final TextView shin = findViewById(R.id.home_fragment_output_shinL);

     bluetoothLeScanner.startScan(new ScanCallback() {
    @Override
    public void onScanResult(int callbackType, ScanResult result) {
    super.onScanResult(callbackType, result);

    shin.setText("" + deviceList.size());
    scanResult = result;

    if (scanResult.getDevice().getName() != null) {
    int[] capsuleRssiTxPower = {scanResult.getRssi(), scanResult.getScanRecord().getTxPowerLevel()};

    Log.e(TAG, "******************* Device Key : " +
    deviceList.keySet());
    if (deviceList.containsKey(scanResult.getDevice().getAddress())) {
    /* Update Device Rssi & TxPowerLevel */
/**                        deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);

 } else {
 /* Add Device to DeviceList */
/**                        deviceList.put(scanResult.getDevice().getAddress(), capsuleRssiTxPower);
 }

 setCoreF_deviceList();

 }
 }
 });
 }*/



}
