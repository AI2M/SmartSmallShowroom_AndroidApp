package com.example.tongchaitonsau.smartsmallshowroom;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main extends Fragment{
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private ViewStub stubGrid;
    private List<Product> productList;
    private static final String TAG = Main.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String> music_box_id = new ArrayList<String>();
    ArrayList<String> detail = new ArrayList<String>();
    ArrayList<String> position = new ArrayList<String>();
    Array a[] ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    Button connect ,disconnect,open,off;
    //TextView textView;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    private ProgressDialog progressDialog;

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
                            setUiEnabled(true);
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback);
                            //tvAppend(textView,"Serial Connection Opened!\n");
                            toast("Serial Connection Opened!");

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

    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }





    public Main() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main.
     */
    // TODO: Rename and change types and number of parameters
    public static Main newInstance(String param1, String param2) {
        Main fragment = new Main();
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


    public void setUiEnabled(boolean bool) {
        connect.setEnabled(!bool);
        disconnect.setEnabled(bool);
        //open.setEnabled(bool);
        //off.setEnabled(bool);
        //textView.setEnabled(bool);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        progressDialog  = new ProgressDialog(getActivity());
        getData("5");

        stubGrid = (ViewStub) view.findViewById (R.id.stub_grid);
        stubGrid.inflate();
        gridView = (GridView) view.findViewById(R.id.my_grid);

       // textView = (TextView) view.findViewById(R.id.res);

        gridView.setOnItemClickListener(onitemclick);
        stubGrid.setVisibility(View.VISIBLE);

//        getProductList();
//        gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item, productList);
//        gridView.setAdapter(gridViewAdapter);

        usbManager = (UsbManager) getActivity().getSystemService(this.getActivity().USB_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        getActivity().registerReceiver(broadcastReceiver, filter);


        connect  = (Button) view.findViewById(R.id.connect_btn);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


            }
        });

        disconnect  = (Button) view.findViewById(R.id.disconnect_btn);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUiEnabled(false);
                serialPort.close();
                //tvAppend(textView,"\nSerial Connection Closed! \n");
                toast("Serial Connection Closed!");
            }
        });

//        open  = (Button) view.findViewById(R.id.open_btn);
//        open.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String string = "ok_send".toString();
//                serialPort.write(string.getBytes());
//                toast("Open");
//            }
//        });
//        off  = (Button) view.findViewById(R.id.off_btn);
//        off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String string = "no_send".toString();
//                serialPort.write(string.getBytes());
//                toast("Off");
//            }
//        });

        setUiEnabled(false);



        return view;
    }




    public List<Product> getProductList(){

            productList = new ArrayList<>();
            for(int i = 0 ; i<position.size() ;i++) {
                productList.add(new Product(R.drawable.ic_music_note_black_24dp, name.get(i), detail.get(i), price.get(i)));


            }
        return productList;

    }

    AdapterView.OnItemClickListener onitemclick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent goPurchase = new Intent(getActivity(),PurchaseActivity.class);
            goPurchase.putExtra("PASS_NAME",productList.get(i).getName());
            goPurchase.putExtra("POSITION", Integer.toString(i+1));
            goPurchase.putExtra("MUSIC_BOX_ID",productList.get(i).getId());
            startActivity(goPurchase);
        }
    };

    private void getData(final String showroom_id){
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        progressDialog.setMessage("Logging in...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.GET,
                Utils.GETDATA_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Now store the user in SQLite
                        JSONArray music_boxs = jObj.getJSONArray("music_box");
//                        ArrayList<String> name = new ArrayList<String>();
//                        ArrayList<String> price = new ArrayList<String>();
//                        ArrayList<String> music_box_id = new ArrayList<String>();
//                        ArrayList<String> detail = new ArrayList<String>();
//                        ArrayList<String> position = new ArrayList<String>();

                       for (int i =0; i < music_boxs.length();i++){
                           JSONObject music = music_boxs.getJSONObject(i);

                           name.add(music.getString("name"));
                           price.add(music.getString("price"));
                           music_box_id.add(music.getString("music_box_id"));
                           detail.add(music.getString("detail"));
                           position.add(music.getString("position"));


                       }

                        Log.d(TAG, "Data === " + position);


                        toast("Get Data Success");

                        getProductList();
                        gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item, productList);
                        gridView.setAdapter(gridViewAdapter);
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
                    toast("Json error: " + e.getMessage());
                    progressDialog.hide();
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

    private void toast(String x){
        Toast.makeText(getActivity(), x, Toast.LENGTH_SHORT).show();
    }

}
