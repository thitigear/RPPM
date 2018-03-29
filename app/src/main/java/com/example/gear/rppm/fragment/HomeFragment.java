package com.example.gear.rppm.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int navItemIndex = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button frag_home_choice_arm;
    private Button frag_home_choice_leg;

    private static String TAG_CURRENT = "";

    private static String TAG_ARM = "arm";
    private static String TAG_LEG = "leg";

    private OnFragmentInteractionListener mListener;
    private CautionAdapter cautionAdapter;
    private View v;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        v = inflater.inflate(R.layout.fragment_home, container, false);

        frag_home_choice_arm = (Button) v.findViewById(R.id.fragment_home_button1);
        frag_home_choice_leg = (Button) v.findViewById(R.id.fragment_home_button2);

        frag_home_choice_arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG_ARM, "ARM!!!!!!!!!!!!");
                TAG_CURRENT = TAG_ARM;
                showCaution();
            }
        });

        frag_home_choice_leg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG_LEG, "LEG!!!!!!!!!!!!");
                TAG_CURRENT = TAG_LEG;
                showCaution();
            }
        });

        return v;
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

        mAlertBuilder.setPositiveButton("เข้าใจ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Objects.equals(TAG_CURRENT, TAG_ARM)){
                    replaceNewFragment(new ArmHomeFragment(), TAG_ARM);
                    TAG_CURRENT = "";

                } else if (Objects.equals(TAG_CURRENT, TAG_LEG)){
                    replaceNewFragment(new LegHomeFragment(), TAG_LEG);
                    TAG_CURRENT = "";

                }

            }
        });

        AlertDialog alertDialog = mAlertBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.button_buttom);

    }

}
