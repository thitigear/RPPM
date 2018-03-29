package com.example.gear.rppm.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;
import com.example.gear.rppm.other.CustomListViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LegHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LegHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LegHomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG_CURRENT = "";
    private String TAG_LEGHOME = "armHome";
    private String TAG_TREAT1 = "กางและหุบข้อตะโพก";
    private String TAG_TREAT2 = "กางแขนและหุบแขนทางข้างลำตัว";
    private String TAG_TREAT3 = "หมุนข้อตะโพกเข้าและออก";

    private int[] resId = { R.drawable.ic_action_menu_add
            , R.drawable.ic_action_menu_all_beacon, R.drawable.ic_action_menu_history};

    private String[] legTreat = { "งอขาและเหยียดข้อสะโพกและข้อเข่าพร้อมกัน", "กางและหุบข้อตะโพก", "หมุนข้อตะโพกเข้าและออก"};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static String CURRENT_TREAT;

    private OnFragmentInteractionListener mListener;

    private View view;
    private CustomListViewAdapter chooseTreatmentAdapter;



    public LegHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LegHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LegHomeFragment newInstance(String param1, String param2) {
        LegHomeFragment fragment = new LegHomeFragment();
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
        view = inflater.inflate(R.layout.fragment_leg_home, container, false);

        ((MainActivity)getActivity()).setToolbarTitle("การกายภาพบำบัดส่วนขา");

        //Choose Arm Treatment
        chooseTreatmentAdapter = new CustomListViewAdapter(getContext(),legTreat, resId);

        ListView legListView = (ListView) view.findViewById(R.id.fragment_leg_home_lv);

        legListView.setAdapter(chooseTreatmentAdapter);
        legListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int treatNumber, long arg3) {
                Log.e(TAG_LEGHOME, "Treat:"+legTreat[treatNumber]);
                onSelectTreatment(treatNumber);
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

    public void onSelectTreatment(int itemSelected){
        CURRENT_TREAT = legTreat[itemSelected];
        replaceNewFragment(new StartRecoveringFragment(), ""+CURRENT_TREAT);
    }

    public void replaceNewFragment(final Fragment newFragment, final String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        //transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        StartRecoveringFragment.CURRENT_TREAT = getCURRENT_TREAT();
        StartRecoveringFragment.FLAG_TREAT = "leg";

        transaction.replace(R.id.frame, newFragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static String getCURRENT_TREAT() {
        return CURRENT_TREAT;
    }


}
