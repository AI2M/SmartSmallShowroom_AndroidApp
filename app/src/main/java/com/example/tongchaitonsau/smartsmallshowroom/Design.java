package com.example.tongchaitonsau.smartsmallshowroom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Design.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Design#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Design extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RadioButton rb_salary;
    RadioGroup rg_salary;
    EditText tel;
    Button submit;

    public Design() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Design.
     */
    // TODO: Rename and change types and number of parameters
    public static Design newInstance(String param1, String param2) {
        Design fragment = new Design();
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
        View rootView = inflater.inflate(R.layout.fragment_design, container, false);

        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tel =  (EditText)rootView.findViewById(R.id.tel_edt);

        Sex(rootView);
        Job(rootView);

        // Set the onClick for each of our views as the one implemented by this Fragment
        //age
        rootView.findViewById(R.id.age_data).setOnClickListener(this);
        rootView.findViewById(R.id.age15low_data).setOnClickListener(this);
        rootView.findViewById(R.id.age15to20_data).setOnClickListener(this);
        rootView.findViewById(R.id.age20to30_data).setOnClickListener(this);
        rootView.findViewById(R.id.age30to40_data).setOnClickListener(this);
        rootView.findViewById(R.id.age40up).setOnClickListener(this);
        //salary
        rootView.findViewById(R.id.salary_data).setOnClickListener(this);
        rootView.findViewById(R.id.salary1).setOnClickListener(this);
        rootView.findViewById(R.id.salary2).setOnClickListener(this);
        rootView.findViewById(R.id.salary3).setOnClickListener(this);
        rootView.findViewById(R.id.salary4).setOnClickListener(this);
        rootView.findViewById(R.id.salary5).setOnClickListener(this);


        return rootView;
    }
    @Override
    public void onClick(View view) {

       // boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            //age

            case R.id.age15low_data:
                Toast.makeText(getActivity(), "น้อยกว่า15", Toast.LENGTH_LONG ).show();
                break;
            case R.id.age15to20_data:
                Toast.makeText(getActivity(), "15-20", Toast.LENGTH_LONG ).show();
                break;
            case R.id.age20to30_data:
                Toast.makeText(getActivity(), "21-30", Toast.LENGTH_LONG ).show();
                break;
            case R.id.age30to40_data:
                Toast.makeText(getActivity(), "31-40", Toast.LENGTH_LONG ).show();
                break;
            case R.id.age40up:
                Toast.makeText(getActivity(), "มากกว่า41", Toast.LENGTH_LONG ).show();
                break;

            //salary
            case R.id.salary1:
                Toast.makeText(getActivity(), "น้อบกว่า10000", Toast.LENGTH_LONG ).show();
                break;
            case R.id.salary2:
                Toast.makeText(getActivity(), "10001-20000", Toast.LENGTH_LONG ).show();
                break;
            case R.id.salary3:
                Toast.makeText(getActivity(), "20001-30000", Toast.LENGTH_LONG ).show();
                break;
            case R.id.salary4:
                Toast.makeText(getActivity(), "30001-40000", Toast.LENGTH_LONG ).show();
                break;
            case R.id.salary5:
                Toast.makeText(getActivity(), "มากกว่า40001", Toast.LENGTH_LONG ).show();
                break;


        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void Sex(View view){
        Spinner sex_spinner = (Spinner) view.findViewById(R.id.sex_data);
        ArrayAdapter<String> myAdapterSex= new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,getResources().getStringArray(R.array.sex));
        myAdapterSex.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sex_spinner.setAdapter(myAdapterSex);

        sex_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    //male
                    Log.d("sex = ", "male");
                }
                else if(i == 1){
                    //female
                    Log.d("sex = ", "female");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void Job(View view){
        Spinner job_spinner = (Spinner)view.findViewById(R.id.job_data);
        ArrayAdapter<String> myAdapterJob= new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_list_item_activated_1,getResources().getStringArray(R.array.Job));
        myAdapterJob.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        job_spinner.setAdapter(myAdapterJob);

        job_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    //male
                    Log.d("job = ", "a");
                }
                else if(i == 1){
                    //female
                    Log.d("job = ", "b");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //hide keyboard


    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager =(InputMethodManager)this.getActivity().getSystemService(Questionnaire.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
}
