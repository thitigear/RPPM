package com.example.gear.rppm.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;
import com.example.gear.rppm.other.CautionAdapter;
import com.example.gear.rppm.other.CustomListViewAdapter;
import com.example.gear.rppm.other.DataArray;
import com.example.gear.rppm.other.FragmentArmLegRecyclerViewAdapter;
import com.example.gear.rppm.other.ReplaceFragment;

import java.util.HashMap;

public class ArmHomeFragment extends Fragment{

    private static String CURRENT_TAG = "arm";

    private String TAG_ARMHOME = "armHome";
    private String TAG_TREAT1 = "ยกแขนขึ้นและลง";
    private String TAG_TREAT2 = "กางแขนและหุบแขนทางข้างลำตัว";
    private String TAG_TREAT3 = "กางแขนและหุบแขนในแนวตั้งฉากกับลำตัว";
    private String TAG_TREAT4 = "หมุนข้อไหล่ขึ้นและลง";
    private String TAG_TREAT5 = "เหยียดและงอข้อศอก";

    private static String CURRENT_TREAT = "";

    private int[] resId = { R.drawable.ic_action_menu_add
            , R.drawable.ic_action_menu_all_beacon, R.drawable.ic_action_menu_history
            , R.drawable.ic_action_menu_home, R.drawable.ic_action_menu_setting};
    private HashMap<Integer, String> armTreatName = new HashMap<>();

    private int setRoundNumber = 0;

    /*PARAMETER*/
    private String[] armTreat;

    /* UI Component */
    private EditText setRoundEditText;
    private ListView armListView;
    private RecyclerView rv_treatName;
    private View view;

    /*Adapter*/
    private CustomListViewAdapter chooseTreatmentAdapter;
    private FragmentArmLegRecyclerViewAdapter recyclerViewAdapter;

    public ArmHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_arm_home, container, false);

        init();


        return view;
    }

    private void init(){

        //((MainActivity)getActivity()).setToolbarTitleByString("การกายภาพบำบัดส่วนแขน");
        ((MainActivity)getActivity()).setCurrentTag(CURRENT_TAG);
<<<<<<< HEAD
        ((MainActivity)getActivity()).setToolbarTitleById(R.string.nav_arm);
=======
        ((MainActivity)getActivity()).setToolbarTitleByString("การกายภาพบำบัดส่วนแขน");
>>>>>>> parent of 6966bb7... final v 3-2

        initUI();

        armTreat = getResources().getStringArray(R.array.treat_arm);
        initAdapter();

        initListView();

        //initRecyclerView();

    }

    private void initAdapter(){
        recyclerViewAdapter = new FragmentArmLegRecyclerViewAdapter(
                getContext(),
                R.id.frag_home_arm_rv_treatName,
                DataArray.getHashMapArmTreat());

        chooseTreatmentAdapter = new CustomListViewAdapter(getContext(),armTreat);
    }

    private void initUI() {
        armListView = (ListView) view.findViewById(R.id.fragment_arm_home_lv);
        rv_treatName = (RecyclerView) view.findViewById(R.id.frag_home_arm_rv_treatName);
    }

    private void initListView(){
        armListView.setAdapter(chooseTreatmentAdapter);
        armListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int treatNumber, long arg3) {
                Log.e("TreatNUM = ", " "+treatNumber);
                showSetRound();
                CURRENT_TREAT = armTreat[treatNumber];
                //replaceDoingFragment(armTreat[treatNumber], "arm");
            }
        });
    }

    private void initRecyclerView(){
        rv_treatName.setAdapter(recyclerViewAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_treatName.setLayoutManager(layoutManager);
    }

    public void replaceDoingFragment(String currentTreat, String flagTreat, int setRoundNumber) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        DoingFragment.setCurrentTreat(currentTreat);
        DoingFragment.setFlagTreat(flagTreat);
        DoingFragment.setMaxSet(setRoundNumber);

        //transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.frame, new DoingFragment(), flagTreat);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showSetRound(){
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
        mAlertBuilder.setTitle("ตั้งค่าการกายภาพบำบัด");
        mAlertBuilder.setView(R.layout.dialog_set_round);

        mAlertBuilder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("setRoundEditText", setRoundEditText.getText().toString());

                try {
                    if(setRoundEditText.getText().toString().equals("")){
                        Toast.makeText(view.getContext(), "กรุณาใส่จำนวนรอบที่ต้องการ", Toast.LENGTH_LONG).show();
                    }
                    else {
                        setRoundNumber = Integer.parseInt(setRoundEditText.getText().toString());

                        if(setRoundNumber > 0 && setRoundNumber <= 5){
                            replaceDoingFragment(CURRENT_TREAT, "arm", setRoundNumber);
                        } else {
                            Toast.makeText(view.getContext(), "จำนวนต้องอยู่ระหว่าง 1 ถึง 5", Toast.LENGTH_LONG).show();
                            Log.e("No. of Round:", ""+ setRoundNumber);
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(view.getContext(), "กรุณาใส่จำนวนรอบเป็นจำนวนเต็ม", Toast.LENGTH_LONG).show();
                }
            }

        });

        mAlertBuilder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = mAlertBuilder.create();
        alertDialog.show();
        setRoundEditText = (EditText) alertDialog.findViewById(R.id.dialog_setRound_editText_num);
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE);


    }

}
