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
import com.example.gear.rppm.other.CautionAdapter;
import com.example.gear.rppm.other.CustomListViewAdapter;
import com.example.gear.rppm.other.ReplaceFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArmHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArmHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArmHomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG_CURRENT = "";
    private String TAG_ARMHOME = "armHome";
    private String TAG_TREAT1 = "ยกแขนขึ้นและลง";
    private String TAG_TREAT2 = "กางแขนและหุบแขนทางข้างลำตัว";
    private String TAG_TREAT3 = "กางแขนและหุบแขนในแนวตั้งฉากกับลำตัว";
    private String TAG_TREAT4 = "หมุนข้อไหล่ขึ้นและลง";
    private String TAG_TREAT5 = "เหยียดและงอข้อศอก";

    private static String CURRENT_TREAT = "";



    // TODO: Rename and change types of parameters
    private int setRoundNumber = 0;
    private int[] resId = { R.drawable.ic_action_menu_add
            , R.drawable.ic_action_menu_all_beacon, R.drawable.ic_action_menu_history
            , R.drawable.ic_action_menu_home, R.drawable.ic_action_menu_setting};

    /*PARAMETER*/
    private String mParam1;
    private String mParam2;
    private String[] armTreat;

    /* UI Component */
    private EditText setRoundEditText;
    private ListView armListView;
    private View view;

    /*Class*/
    private CustomListViewAdapter chooseTreatmentAdapter;

    /*OnFragmentInteractionListener*/
    private OnFragmentInteractionListener mListener;

    public ArmHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArmHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArmHomeFragment newInstance(String param1, String param2) {
        ArmHomeFragment fragment = new ArmHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_arm_home, container, false);

        ((MainActivity)getActivity()).setToolbarTitleByString("การกายภาพบำบัดส่วนแขน");

        /*Setup important adapter*/
        armTreat = getResources().getStringArray(R.array.treat_arm);
        chooseTreatmentAdapter = new CustomListViewAdapter(getContext(),armTreat);

        /*Setup ListView*/
        armListView = (ListView) view.findViewById(R.id.fragment_arm_home_lv);
        armListView.setAdapter(chooseTreatmentAdapter);
        armListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int treatNumber, long arg3) {
                Log.e("TreatNUM = ", " "+treatNumber);
                showSetRound();
                CURRENT_TREAT = armTreat[treatNumber];
                //replaceDoingFragment(armTreat[treatNumber], "arm");
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void replaceDoingFragment(String currentTreat, String flagTreat, int setRoundNumber) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        DoingFragment.setCurrentTreat(currentTreat);
        DoingFragment.setFlagTreat(flagTreat);
        DoingFragment.setMaxSet(setRoundNumber);

        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
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
