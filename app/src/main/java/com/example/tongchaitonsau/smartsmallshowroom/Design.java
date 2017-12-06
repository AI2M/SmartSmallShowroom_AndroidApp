package com.example.tongchaitonsau.smartsmallshowroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


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
    private String TAG = Design.class.getSimpleName();

    RadioButton rb_salary;
    RadioGroup rg_salary;
    RadioGroup ageGroup,salaryGroup;
    EditText tel;
    Button submit;

    private String sex_,job_,age_="",phone_num_,salary_="";
    private ProgressDialog progressDialog;

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

        progressDialog  = new ProgressDialog(getActivity());


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tel =  (EditText)rootView.findViewById(R.id.tel_edt);
        tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This can be ignored
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This can be ignored
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditText(); // OR validation can be specific (only for this EditText)
                validatePhone();
            }
        });
        //submit = (Button)rootView.findViewById(R.id.submit_question);

        Sex(rootView);
        Job(rootView);

        // Set the onClick for each of our views as the one implemented by this Fragment
        //age
         ageGroup = (RadioGroup)rootView.findViewById(R.id.age_data);
         salaryGroup = (RadioGroup)rootView.findViewById(R.id.salary_data);

        //rootView.findViewById(R.id.age_data).setOnClickListener(this);
        rootView.findViewById(R.id.age15low_data).setOnClickListener(this);
        rootView.findViewById(R.id.age15to20_data).setOnClickListener(this);
        rootView.findViewById(R.id.age20to30_data).setOnClickListener(this);
        rootView.findViewById(R.id.age30to40_data).setOnClickListener(this);
        rootView.findViewById(R.id.age40up).setOnClickListener(this);
        //salary
        //rootView.findViewById(R.id.salary_data).setOnClickListener(this);
        rootView.findViewById(R.id.salary1).setOnClickListener(this);
        rootView.findViewById(R.id.salary2).setOnClickListener(this);
        rootView.findViewById(R.id.salary3).setOnClickListener(this);
        rootView.findViewById(R.id.salary4).setOnClickListener(this);
        rootView.findViewById(R.id.salary5).setOnClickListener(this);

        rootView.findViewById(R.id.submit_question).setOnClickListener(this);


        return rootView;
    }

    private boolean validateEditText() {
        boolean isValidated = true;
        if (tel.getText().toString().length() == 0) {
            tel.setError("Required");
            isValidated = false;
        }
        return isValidated;
    }
    private boolean validatePhone() {

        boolean phoneIsValidated = true;
        String text = tel.getText().toString();
        if (!text.matches("^(?:0091|\\\\+91|0)[7-9][0-9]{7,8}$")) {
            phoneIsValidated = false;
            tel.setError("Invalid format");
        }
        return phoneIsValidated;
    }
    @Override
    public void onClick(View view) {

       // boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            //age

            case R.id.age15low_data:
                //Toast.makeText(getActivity(), "น้อยกว่า15", Toast.LENGTH_LONG ).show();
                age_ = "less than 15";
                break;
            case R.id.age15to20_data:
                //Toast.makeText(getActivity(), "15-20", Toast.LENGTH_LONG ).show();
                age_ = "15-20";
                break;
            case R.id.age20to30_data:
                //.makeText(getActivity(), "21-30", Toast.LENGTH_LONG ).show();
                age_ = "21-30";
                break;
            case R.id.age30to40_data:
                //Toast.makeText(getActivity(), "31-40", Toast.LENGTH_LONG ).show();
                age_ = "31-40";
                break;
            case R.id.age40up:
                //Toast.makeText(getActivity(), "มากกว่า41", Toast.LENGTH_LONG ).show();
                age_ = "more than 41";
                break;

            //salary
            case R.id.salary1:
                //Toast.makeText(getActivity(), "น้อบกว่า10000", Toast.LENGTH_LONG ).show();
                salary_ = "less than 10000";
                break;
            case R.id.salary2:
                //Toast.makeText(getActivity(), "10001-20000", Toast.LENGTH_LONG ).show();
                salary_ = "10001-20000";
                break;
            case R.id.salary3:
                //Toast.makeText(getActivity(), "20001-30000", Toast.LENGTH_LONG ).show();
                salary_ = "20001-30000";
                break;
            case R.id.salary4:
                //Toast.makeText(getActivity(), "30001-40000", Toast.LENGTH_LONG ).show();
                salary_ = "30001-40000";
                break;
            case R.id.salary5:
                //Toast.makeText(getActivity(), "มากกว่า40001", Toast.LENGTH_LONG ).show();
                salary_ = "more than 40001";
                break;
            //button

            case R.id.submit_question:

                if(!validatePhone()&&!validateEditText()){
                    toast("wrong phone number");
                }
                else  if(salary_.toString().isEmpty()||age_.toString().isEmpty()){
                    toast("please enter all questions");
                }
                else {
                    phone_num_ = tel.getText().toString();
                    storeCustomer(sex_,job_,age_,phone_num_,salary_,"5");
                    tel.setText("");
                    ageGroup.clearCheck();
                    salaryGroup.clearCheck();


                }


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
                    sex_= "male";
                    //toast("male");
                }
                else if(i == 1){
                    //female
                    Log.d("sex = ", "female");
                    sex_= "female";
                    //toast("female");
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
                    job_ = "other";
                   // toast("other");

                }
                else if(i == 1){
                    job_ = "government official";
                    //toast("government official");
                }
                else if(i == 2){
                    job_ = "nurse";
                    //toast("nurse");
                }
                else if(i == 3){
                    job_ = "doctor";
                    //toast("doctor");
                }
                else if(i == 4){
                    job_ = "personal business";
                    //toast("personal business");
                }
                else if(i == 5){
                    job_ = "merchant";
                    //toast("merchant");
                }
                else if(i == 6){
                    job_ = "officer";
                    //toast("officer");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    //store data into database

    private void storeCustomer (final String sex, final String job, final String age
            , final String phone_num, final String salary ,final String showroom_id){
        // Tag used to cancel the request
        String tag_string_req = "req_storecustomer";
        progressDialog.setMessage("Storing up...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Utils.STORECUSTOMER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "addCustomer Response: " + response);

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject Customer = jObj.getJSONObject("Customer");
                        String sex = Customer.getString("sex");
                        String job = Customer.getString("job");
                        String age = Customer.getString("age");
                        String phone_num = Customer.getString("phone_num");
                        String  salary = Customer.getString("salary");
                        String  showroom_id = Customer.getString("showroom_id");


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        //Log.d("error ---------", errorMsg);
                        toast(errorMsg);
                        progressDialog.hide();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    progressDialog.hide();
                    toast("Send Questionnaire Success");
                    //Log.d("error ---------2", e.getMessage());
                    //toast("Json error: " + e.getMessage());

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                toast("Unknown Error occurred");
                progressDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("sex", sex);
                params.put("job", job);
                params.put("age", String.valueOf(age));
                params.put("phone_num", phone_num);
                params.put("salary", String.valueOf(salary));
                params.put("showroom_id", showroom_id);

                return params;
            }

        };

        // Adding request to request queue
        AndroidLoginController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void toast(String x){
        Toast.makeText(getActivity(), x, Toast.LENGTH_SHORT).show();
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
