package com.example.gear.rppm.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.gear.rppm.R;
import com.example.gear.rppm.activity.MainActivity;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static int spinnerItemPosition;

    private static String CURRENT_TAG = "setting";
    private static String[] languageName;

    private ArrayAdapter<String> languageAdapter;

    private static Button frag_setting_but_apply;
    private static Button frag_setting_but_cancel;
    private static Spinner frag_setting_spinner_language;

    private View view;

    private OnFragmentInteractionListener mListener;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Setting.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        ((MainActivity)getActivity()).setCurrentTag(CURRENT_TAG);
        ((MainActivity)getActivity()).setToolbarTitleById(R.string.nav_setting);

        setUI();

        languageName = getResources().getStringArray(R.array.language);

        Log.e("Language", String.format("%s, %s", languageName[0], languageName[1]));

        languageAdapter = new ArrayAdapter<String>(view.getContext()
                , android.R.layout.simple_spinner_dropdown_item, languageName);

        frag_setting_spinner_language.setAdapter(languageAdapter);


        frag_setting_but_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("item pos select", languageName[frag_setting_spinner_language.getSelectedItemPosition()]);
                CheckBeaconFragment.clearSDeviceList();
                Configuration config = new Configuration();
                config.locale = new Locale(getLocaleFromSpinner(frag_setting_spinner_language.getSelectedItemPosition()));
                getResources().updateConfiguration(config, null);

            }
        });
        frag_setting_but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
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

    private void setUI(){
        frag_setting_spinner_language = (Spinner) view.findViewById(R.id.fragment_setting_spinner_language);
        frag_setting_but_apply = (Button) view.findViewById(R.id.fragment_setting_but_apply);
        frag_setting_but_cancel = (Button) view.findViewById(R.id.fragment_setting_but_cancel);

    }

    private String getLocaleFromSpinner(int itemSelected_position){
        switch (itemSelected_position){
            case 0:
                ((MainActivity)getActivity()).updateLanguage("th");
                return "th";
            case 1:
                ((MainActivity)getActivity()).updateLanguage("en");
                return "en";
            default:
                ((MainActivity)getActivity()).updateLanguage("th");
                return "th";
        }
    }

}
