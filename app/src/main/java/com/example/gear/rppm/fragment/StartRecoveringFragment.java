package com.example.gear.rppm.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartRecoveringFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartRecoveringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartRecoveringFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int treatTime = 0;
    private static float[] thisBody;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected static String CURRENT_TREAT;
    protected static String FLAG_TREAT;

    private OnFragmentInteractionListener mListener;

    private View view;

    private TextView treatName;
    private Button butStop;

    public StartRecoveringFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartRecoveringFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartRecoveringFragment newInstance(String param1, String param2) {
        StartRecoveringFragment fragment = new StartRecoveringFragment();
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
        view = inflater.inflate(R.layout.fragment_start_recovering, container, false);

        ((MainActivity)getActivity()).setToolbarTitle("เริ่มทำกายภาพ");

        treatName = (TextView) view.findViewById(R.id.fragment_startRe_treatName);
        treatName.setText(CURRENT_TREAT);

        butStop = (Button) view.findViewById(R.id.fragment_startRe_buttonStop);
        butStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaution();
            }
        });


        //checkAngle(new float[]{1,2,3,4,5,6});


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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    public void checkAngle(float[] correctBody){
        treatTime += 1;
    }


    @SuppressLint("ResourceAsColor")
    private void showCaution(){

        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(view.getContext());
         mAlertBuilder.setTitle("หยุดชั่วคราว");

        mAlertBuilder.setPositiveButton("ืทำต่อ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        mAlertBuilder.setNegativeButton("กลับไปหน้าก่อนหน้า", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /**if(FLAG_TREAT == "arm"){
                    replaceNewFragment(new ArmHomeFragment(), FLAG_TREAT);

                } else if (FLAG_TREAT == "leg"){
                    replaceNewFragment(new LegHomeFragment(), FLAG_TREAT);
                }*/
                ((MainActivity)getActivity()).onBackPressed();
            }
        });

        AlertDialog alertDialog = mAlertBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setBackgroundColor(R.color.buttonBackground);
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setBackgroundColor(R.color.buttonRed);

    }
}
