package com.example.gear.rppm.activity;

import android.bluetooth.le.ScanCallback;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.example.gear.rppm.fragment.DoingFragment;
import com.example.gear.rppm.fragment.HomeFragment;
import com.example.gear.rppm.fragment.CheckBeaconFragment;
import com.example.gear.rppm.fragment.ArmHomeFragment;
import com.example.gear.rppm.fragment.LegHomeFragment;
import com.example.gear.rppm.fragment.ManualFragment;
import com.example.gear.rppm.fragment.SettingFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Handler mHandler;

    // index to identify current nav menu item
    private int navItemIndex = 0;

    private ScanCallback scanCallback;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_CHECK_BEACON = "check beacon";
    private static final String TAG_ARM = "arm";
    private static final String TAG_LEG = "leg";
    private static final String TAG_MANUAL = "manual";
    private static final String TAG_DOING = "doing";
    private static final String TAG_SETTING = "setting";

    private static String TAG_DOING_CURRENT_DIALOG;

    private static String TAG_DOING_DIALOG_FINISH_ALL = "finishAll";
    private static String TAG_DOING_DIALOG_FINISH_ONE = "finishOne";
    private static String TAG_DOING_DIALOG_PAUSE = "pause";
    //public static String CURRENT_TAG = TAG_HOME;

    private static String CURRENT_TAG;
    private static String CURRENT_LANGUAGE = "th";

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add toolbar to app
        toolbar = (Toolbar) findViewById(R.id.toolbar); //in app_bar_sliding_menu_xml
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            //CURRENT_TAG = TAG_HOME;
            setToolbarTitleById(R.id.nav_home);
            //setToolbarTitleByString("หน้าแรก");
            //loadFragment();
            replaceFragment(getFragment());
        }

    }

    public void replaceFragment(final Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment, CURRENT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();

        //Closing drawer on item click
        drawer.closeDrawers();
    }

    public Fragment getFragment() {
        selectNavMenu();
        switch (navItemIndex) {
            case 0:
                // home fragment
                HomeFragment homeFragment = new HomeFragment();
                CURRENT_TAG = TAG_HOME;
                return homeFragment;
            case 1:
                // check beacon fragment
                CheckBeaconFragment checkBeaconFragment = new CheckBeaconFragment();
                CURRENT_TAG = TAG_CHECK_BEACON;
                return checkBeaconFragment;
            case 2:
                // arm fragment
                ArmHomeFragment armHomeFragment = new ArmHomeFragment();
                CURRENT_TAG = TAG_ARM;
                return armHomeFragment;
            case 3:
                // leg fragment
                LegHomeFragment legHomeFragment = new LegHomeFragment();
                CURRENT_TAG = TAG_LEG;
                return legHomeFragment;
            case 4:
                // Manual fragment
                ManualFragment manualFragment = new ManualFragment();
                CURRENT_TAG = TAG_MANUAL;
                return manualFragment;
            case 5:
                // Setting fragment
                SettingFragment settingFragment = new SettingFragment();
                CURRENT_TAG = TAG_SETTING;
                return settingFragment;
            default:
                return new HomeFragment();
        }
    }

    public void setToolbarTitleById(int title_id) {
        getSupportActionBar().setTitle(getResources().getString(title_id));
    }
    public void setToolbarTitleByString(String title) {
        getSupportActionBar().setTitle(title);
    }
    public void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        //navigationView.getMenu().getItem(navItemIndex).setChecked(true);

    }

    public void setUpNavigationView() {
        /*Setting Navigation View Item Selected Listener to handle the item click of the navigation menu*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            /*This method will trigger on item Click of navigation menu*/
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                /*Check to see which item was being clicked and perform appropriate action*/
                switch (menuItem.getItemId()) {
                    /*Replacing the main content with ContentFragment Which is our Inbox View;*/
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
                    case R.id.nav_manual:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_MANUAL;
                        break;
                    case R.id.nav_setting:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_SETTING;
                        break;
                    default:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                replaceFragment(getFragment());
                //loadFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                /* Code here will be triggered once the drawer closes as
                 * we don't want anything to happen so we leave this blank */
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                /* Code here will be triggered once the drawer open as
                 * we don't want anything to happen so we leave this blank */
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout*/
        drawer.setDrawerListener(actionBarDrawerToggle);


        /*calling sync state is necessary or else your hamburger icon wont show up*/
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

        switch (CURRENT_TAG){
            case TAG_HOME:
                /*Close Application*/
                finishAndRemoveTask();
            case TAG_DOING:
                super.onBackPressed();
                break;
            /*case TAG_DOING:
                if(TAG_DOING_CURRENT_DIALOG.equals(TAG_DOING_DIALOG_FINISH_ALL)){
                    navItemIndex = 0;
                    replaceFragment(getFragment());
                } else if(TAG_DOING_CURRENT_DIALOG.equals(TAG_DOING_DIALOG_FINISH_ONE)){
                    navItemIndex = 0;
                    replaceFragment(getFragment());
                } else if(TAG_DOING_CURRENT_DIALOG.equals(TAG_DOING_DIALOG_PAUSE)){

                }
                else {
                    /*DO NOTHING*/
                //}
                //break;
            default:
                /* checking if user is on other navigation menu
                 * rather than home */
                if (navItemIndex != 0) {
                    navItemIndex = 0;
                    //CURRENT_TAG = TAG_HOME;
                    //loadFragment();
                    replaceFragment(getFragment());
                } else {
                    //super.onBackPressed();
                    setToolbarTitleByString("หน้าแรก");
                    replaceFragment(new HomeFragment());
                }
                break;

        }


        //super.onBackPressed();
    }

    public void setCurrentTag(String currentTag) {
        CURRENT_TAG = currentTag;
    }
    public void setCurrentLanguage(String currentLanguage) {
        CURRENT_LANGUAGE = currentLanguage;
    }

    public static String getCurrentTag() {
        return CURRENT_TAG;
    }

    public void setTagDoingCurrentDialog(String tagDoingCurrentDialog) {
        TAG_DOING_CURRENT_DIALOG = tagDoingCurrentDialog;
    }

    public void updateLanguage(String language){
        Intent refresh = new Intent(this, MainActivity.class);
        switch (language){
            case "th":
                CURRENT_LANGUAGE = "th";
                startActivity(refresh);
                finish();
                //navigationView.getResources().updateConfiguration(configuration, null);
                break;
            case "en":
                CURRENT_LANGUAGE = "en";
                startActivity(refresh);
                finish();
                //navigationView.getResources().updateConfiguration(configuration, null);
                break;
            default:
                CURRENT_LANGUAGE = "th";
                startActivity(refresh);
                finish();
                //navigationView.getResources().updateConfiguration(configuration, null);
                break;
        }
    }

    public void updateNavigationView(){
        setUpNavigationView();
    }

    public void setScanCallback(ScanCallback scanCallback) {
        this.scanCallback = scanCallback;
    }

    public static String getCurrentLanguage() {
        return CURRENT_LANGUAGE;
    }
}
