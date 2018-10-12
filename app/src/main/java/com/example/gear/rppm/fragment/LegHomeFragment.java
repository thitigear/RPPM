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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;
import com.example.gear.rppm.other.CustomListViewAdapter;

public class LegHomeFragment extends Fragment {

    private String CURRENT_TAG = "leg";

    private String TAG_LEGHOME = "LegHome";
    private String TAG_TREAT1 = "งอขาและเหยียดข้อสะโพกและข้อเข่าพร้อมกัน";
    private String TAG_TREAT2 = "กางและหุบข้อตะโพก";
    private String TAG_TREAT3 = "ยกขาขึ้นทั้งขา";

    private int[] resId = { R.drawable.ic_action_menu_add
            , R.drawable.ic_action_menu_all_beacon, R.drawable.ic_action_menu_history};

    // TODO: Rename and change types of parameters
    private int setRoundNumber;

    private String[] legTreat;
    private static String CURRENT_TREAT;

    /* UI Component */
    private EditText setRoundEditText;
    private ListView legListView;
    private View view;

    /*Class*/
    private CustomListViewAdapter chooseTreatmentAdapter;

    public LegHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leg_home, container, false);
        ((MainActivity)getActivity()).setCurrentTag(CURRENT_TAG);
        ((MainActivity)getActivity()).setToolbarTitleByString("การกายภาพบำบัดส่วนขา");

        /*Setup Important param*/
        legTreat = getResources().getStringArray(R.array.treat_leg);
        chooseTreatmentAdapter = new CustomListViewAdapter(getContext(),legTreat);

        /*Setup ListView*/
        legListView = (ListView) view.findViewById(R.id.fragment_leg_home_lv);
        legListView.setAdapter(chooseTreatmentAdapter);

        legListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int treatNumber, long arg3) {
                showSetRound();
                CURRENT_TREAT = legTreat[treatNumber];
            }
        });


        return view;
    }

    /**
     * Replace Fragment with
     *
     * @param currentTreat
     * @param flagTreat
     * @param setRoundNumber
     */

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
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE);
        setRoundEditText = (EditText) alertDialog.findViewById(R.id.dialog_setRound_editText_num);
    }
}
