package com.example.gear.rppm.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.gear.rppm.R;
import com.example.gear.rppm.other.CautionAdapter;
import com.example.gear.rppm.other.CustomListViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArmHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArmHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArmHomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private CustomListViewAdapter chooseTreatmentAdapter;
    private CautionAdapter cautionAdapter;

    private int[] resId = { R.drawable.ic_action_menu_add
            , R.drawable.ic_action_menu_all_beacon, R.drawable.ic_action_menu_history
            , R.drawable.ic_action_menu_home, R.drawable.ic_action_menu_setting};

    private String[] armTreat = { "ยกแขนขึ้นและลง", "กางแขนและหุบแขนทางข้างลำตัว", "กางแบนและหุบแขนในแนวตั้งฉากกับลำตัว"
            , "หมุนข้อไหล่ขึ้นและลง", "เหยียดและงอข้อศอก"};

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
        View view = inflater.inflate(R.layout.fragment_arm_home, container, false);

        //Choose Arm Treatment
        chooseTreatmentAdapter = new CustomListViewAdapter(getContext(),armTreat, resId);

        ListView armListView = (ListView) view.findViewById(R.id.fragment_arm_home_lv);

        armListView.setAdapter(chooseTreatmentAdapter);
        armListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

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
            //throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
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
}
