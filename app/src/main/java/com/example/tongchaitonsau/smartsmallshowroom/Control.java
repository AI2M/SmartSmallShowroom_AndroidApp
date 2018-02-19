package com.example.tongchaitonsau.smartsmallshowroom;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Control.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Control#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Control extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;
    MainActivity mainactivity;

    //start var
    private static final String TAG = Main.class.getSimpleName();
    private ProgressDialog progressDialog;

    ArrayList<String> music_box_id = new ArrayList<String>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> mSelected = new ArrayList<String>();
    ArrayList<String> lSelected = new ArrayList<String>();

    CheckBox m1,m2,m3,m4,m5,m6,m7,m8,m9,l1,l2,l3,l4,l5,l6,l7,l8,l9;
    String musicSelected;

    //usb
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;

    //end var

    public Control() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Control.
     */
    // TODO: Rename and change types and number of parameters
    public static Control newInstance(String param1, String param2) {
        Control fragment = new Control();
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
        View view =  inflater.inflate(R.layout.fragment_control, container, false);
        progressDialog  = new ProgressDialog(getActivity());
        mainactivity = (MainActivity) getActivity();
        //usb
        usbManager = (UsbManager) getActivity().getSystemService(getActivity().USB_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        getActivity().registerReceiver(broadcastReceiver, filter);

        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 0x2A03)//Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
        }
        //checkbox
        view.findViewById(R.id.m1).setOnClickListener(this);
        view.findViewById(R.id.m2).setOnClickListener(this);
        view.findViewById(R.id.m3).setOnClickListener(this);
        view.findViewById(R.id.m4).setOnClickListener(this);
        view.findViewById(R.id.m5).setOnClickListener(this);
        view.findViewById(R.id.m6).setOnClickListener(this);
        view.findViewById(R.id.m7).setOnClickListener(this);
        view.findViewById(R.id.m8).setOnClickListener(this);
        view.findViewById(R.id.m9).setOnClickListener(this);
        view.findViewById(R.id.l1).setOnClickListener(this);
        view.findViewById(R.id.l2).setOnClickListener(this);
        view.findViewById(R.id.l3).setOnClickListener(this);
        view.findViewById(R.id.l4).setOnClickListener(this);
        view.findViewById(R.id.l5).setOnClickListener(this);
        view.findViewById(R.id.l6).setOnClickListener(this);
        view.findViewById(R.id.l7).setOnClickListener(this);
        view.findViewById(R.id.l8).setOnClickListener(this);
        view.findViewById(R.id.l9).setOnClickListener(this);
        //button

        Button ControlSend = (Button)view.findViewById(R.id.btn_control);
        ControlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mSelected=", String.valueOf(mSelected));
                Log.d("lSelected=", String.valueOf(lSelected));
                Log.d("musicSelected=",musicSelected);
                String control = "mSelected = "+String.valueOf(mSelected)+ "lSelected = "+ String.valueOf(lSelected)
                        +"musicSelected = "+musicSelected;
                serialPort.write(control.getBytes());
            }
        });

        getData(view,mainactivity.getShowroom_id());

        return  view;
    }
    @Override
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.m1:
                if(checked){
                    mSelected.add("m1");
                }
                else {
                    mSelected.remove("m1");
                }
                break;
            case R.id.m2:
                if(checked){
                    mSelected.add("m2");
                }
                else {
                    mSelected.remove("m2");
                }
                break;

            case R.id.m3:
                if(checked){
                    mSelected.add("m3");
                }
                else {
                    mSelected.remove("m3");
                }
                break;

            case R.id.m4:
                if(checked){
                    mSelected.add("m4");
                }
                else {
                    mSelected.remove("m4");
                }
                break;

            case R.id.m5:
                if(checked){
                    mSelected.add("m5");
                }
                else {
                    mSelected.remove("m5");
                }
                break;

            case R.id.m6:
                if(checked){
                    mSelected.add("m6");
                }
                else {
                    mSelected.remove("m6");
                }
                break;

            case R.id.m7:
                if(checked){
                    mSelected.add("m7");
                }
                else {
                    mSelected.remove("m7");
                }
                break;

            case R.id.m8:
                if(checked){
                    mSelected.add("m8");
                }
                else {
                    mSelected.remove("m8");
                }
                break;

            case R.id.m9:
                if(checked){
                    mSelected.add("m9");
                }
                else {
                    mSelected.remove("m9");
                }
                break;
            case R.id.l1:
                if(checked){
                    lSelected.add("l1");
                }
                else {
                    lSelected.remove("l1");
                }
                break;
            case R.id.l2:
                if(checked){
                    lSelected.add("l2");
                }
                else {
                    lSelected.remove("l2");
                }
                break;
            case R.id.l3:
                if(checked){
                    lSelected.add("l3");
                }
                else {
                    lSelected.remove("l3");
                }
                break;
            case R.id.l4:
                if(checked){
                    lSelected.add("l4");
                }
                else {
                    lSelected.remove("l5");
                }
                break;
            case R.id.l5:
                if(checked){
                    lSelected.add("l5");
                }
                else {
                    lSelected.remove("l5");
                }
                break;
            case R.id.l6:
                if(checked){
                    lSelected.add("l6");
                }
                else {
                    lSelected.remove("l6");
                }
                break;
            case R.id.l7:
                if(checked){
                    lSelected.add("l7");
                }
                else {
                    lSelected.remove("l7");
                }
                break;
            case R.id.l8:
                if(checked){
                    lSelected.add("l8");
                }
                else {
                    lSelected.remove("l8");
                }
                break;
            case R.id.l9:
                if(checked){
                    lSelected.add("l9");
                }
                else {
                    lSelected.remove("l9");
                }
                break;


        }

    }

    private  void MusicSpinner(View view,ArrayList name){
        // start spinner
        Spinner music_spinner = (Spinner) view.findViewById(R.id.music_spin);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < name.size(); i++) // Maximum size of i upto --> Your Array Size
        {
            dataAdapter.add((String) name.get(i));
        }
        music_spinner.setAdapter(dataAdapter);

        music_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                musicSelected = music_box_id.get(i).toString();
               // Log.d("musicid = ",selected_music);
               // toast(selected_music);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //end spinner
    }

    private void getData(final View view, final String showroom_id){
        // Tag used to cancel the request
        String tag_string_req = "req_getdata";
//        progressDialog.setMessage("getting data...");
//        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.GET,
                Utils.GETDATA_URL+"/?showroom_id="+showroom_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get data Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Now store the user in SQLite
                        JSONArray music_boxs = jObj.getJSONArray("m_and_s");

                        for (int i =0; i < music_boxs.length();i++){
                            JSONObject music = music_boxs.getJSONObject(i);

                            name.add(music.getString("name"));
                            music_box_id.add(music.getString("music_box_id"));


                        }

                        Log.d(TAG, "Data === " + name);
                        MusicSpinner(view,name); //add music_name to spinner
                      //  toast("Get Data Success");

                        progressDialog.hide();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        toast(errorMsg);
                        progressDialog.hide();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                  //  toast("Json error: " + e.getMessage());
                    progressDialog.hide();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
               // toast("Unknown Error occurred");
                progressDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Log.d(TAG, "now here");
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("showroom_id", showroom_id);


                return params;
            }

        };
        // Adding request to request queue
        AndroidLoginController.getInstance().addToRequestQueue(strReq, tag_string_req);


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

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                data.concat("/n");
                //tvAppend(textView, data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }
    };
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    connection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                    if (serialPort != null) {
                        if (serialPort.open()) { //Set Serial Connection Parameters.
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback);
//                            String string = "ok_send".toString(); //send data
//                            serialPort.write(string.getBytes());

                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            }
            else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                //onClickCon(con);
            }
            else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                //onClickDiscon(discon);

            }
        }

        ;
    };
    private void toast(String x){
        Toast.makeText(getActivity(), x, Toast.LENGTH_SHORT).show();
    }
}
