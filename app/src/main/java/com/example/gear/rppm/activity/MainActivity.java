package com.example.gear.rppm.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.gear.rppm.R;
import com.example.gear.rppm.fragment.HomeFragment;
import com.example.gear.rppm.fragment.CheckBeaconFragment;
import com.example.gear.rppm.fragment.ArmHomeFragment;
import com.example.gear.rppm.fragment.LegHomeFragment;
import com.example.gear.rppm.fragment.StartRecoveringFragment;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener
        , CheckBeaconFragment.OnFragmentInteractionListener
        , ArmHomeFragment.OnFragmentInteractionListener
        , LegHomeFragment.OnFragmentInteractionListener
        , StartRecoveringFragment.OnFragmentInteractionListener{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

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
    private BeaconManager mBeaconManager;
    private Collection<Beacon> beacons = new HashSet<>();

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
            setToolbarTitle("หน้าแรก");
            loadFragment();
        }

        //Beacon

        /**mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.setForegroundScanPeriod(100);
        mBeaconManager.setForegroundBetweenScanPeriod(500);
        mBeaconManager.setBackgroundScanPeriod(100);
        mBeaconManager.setBackgroundBetweenScanPeriod(500);

        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.bind((BeaconConsumer) this);*/
        /**Beacon beacon = new Beacon.Builder()
                .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                .setId2("1")
                .setId3("2")
                .setManufacturer(0x0118)
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[] {0l}))
                .build();
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon);*/


    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */

    public void loadFragment() {

        // selecting appropriate nav menu item
        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        // loaded with cross fade effect
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

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
                setToolbarTitle("หน้าแรก");
                return homeFragment;
            case 1:
                // check beacon fragment
                CheckBeaconFragment checkBeaconFragment = new CheckBeaconFragment();
                setToolbarTitle("ตรวจสอบสภานะอุปกรณ์");
                return checkBeaconFragment;
            case 2:
                // arm fragment
                ArmHomeFragment armHomeFragment = new ArmHomeFragment();
                setToolbarTitle("การทำกายภาพบำบัดส่วนแขน");
                return armHomeFragment;
            case 3:
                // leg fragment
                LegHomeFragment legHomeFragment = new LegHomeFragment();
                setToolbarTitle("การทำกายภาพบำบัดส่วนขา");
                return legHomeFragment;

            default:
                return new HomeFragment();
        }
    }

    /**
     * public void setToolbarTitle(int navItemIndex) {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);

     }*/

    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public void selectNavMenu() { navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        //navigationView.getMenu().getItem(navItemIndex).setChecked(true);

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

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
            fragmentTransaction.commitAllowingStateLoss();
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
                setToolbarTitle("หน้าแรก");
            }
        }
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**private void startBeaconRangeFinderService() {
        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                try {
                    if (beacons.size() > 0) {
                        for (Beacon b : beacons) {
                            Log.e(TAG_HOME, ""+b);
                        }
                    }
                } catch (Exception ex) {
                    Log.e(TAG_HOME, "Error was thrown: " + ex.getMessage());
                }
            }
        });
        mBeaconManager.startRangingBeaconsInRegion(new Region());

    }*/

}
