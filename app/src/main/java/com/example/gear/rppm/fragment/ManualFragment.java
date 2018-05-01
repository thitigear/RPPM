package com.example.gear.rppm.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;
import com.example.gear.rppm.other.CustomListViewAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManualFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManualFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] manualChoice;
    private String[] manualBasic;

    /*UI Component*/
    private ListView frag_manual_lv_01;
    private View view;

    /*Import Class*/
    private CustomListViewAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public ManualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManualFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManualFragment newInstance(String param1, String param2) {
        ManualFragment fragment = new ManualFragment();
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
        view = inflater.inflate(R.layout.fragment_manual, container, false);
        ((MainActivity)getActivity()).setToolbarTitleById(R.string.nav_manual);

        /*Setup important param*/
        manualChoice = getResources().getStringArray(R.array.manual_choice);
        adapter = new CustomListViewAdapter(getContext(), manualChoice);

        /*Setup ListView*/
        frag_manual_lv_01 = (ListView) view.findViewById(R.id.fragment_manual_lv);
        frag_manual_lv_01.setAdapter(adapter);

        frag_manual_lv_01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("MANUAL Fragment:", manualChoice[position]);
                showManualDialog(manualChoice[position]);
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

    //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
    public void showManualDialog(String title){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(title);

        switch (title){
            case "เมนูของแอพพลิเคชัน":
                alertDialogBuilder.setView(R.layout.manual_basic);

                alertDialogBuilder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            case "การใช้งานฟังก์ชันกายภาพบำบัด":
                alertDialogBuilder.setView(R.layout.manual_function);
                alertDialogBuilder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            case "ตำแหน่งเซ็นเซอร์และตำแหน่งการวางโทรศัพท์มือถือ":
                alertDialogBuilder.setView(R.layout.manual_position);
                alertDialogBuilder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
            case "ข้อแนะนำอื่นๆ":
                alertDialogBuilder.setView(R.layout.manual_rec);
                alertDialogBuilder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE);

    }

    /**private void settingDialog(int layout_id,int array_id){
        manualBasic = getResources().getStringArray(R.array.manual_basic_array);
        alertDialogBuilder.setView(R.layout.manual_basic);

        CustomListViewAdapter dialogAdapter = new CustomListViewAdapter(getContext(), manualBasic);
        ListView manual_basic_lv = (ListView) view.findViewById(R.id.manual_basic_lv_001);
        manual_basic_lv.setAdapter(dialogAdapter);

        alertDialogBuilder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }*/
}
