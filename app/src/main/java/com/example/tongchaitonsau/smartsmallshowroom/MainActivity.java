package com.example.tongchaitonsau.smartsmallshowroom;

import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.nfc.Tag;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

public class MainActivity extends AppCompatActivity implements Main.OnFragmentInteractionListener, Design.OnFragmentInteractionListener, Control.OnFragmentInteractionListener {

    private UserInfo userInfo;
    private UserSession userSession;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    Menu mymenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        String strTime = sdf2.format(c.getTime());
        Log.d("datetime2************",strDate + " Time =="+strTime);

        userInfo        = new UserInfo(this);
        userSession     = new UserSession(this);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Questionnaire"));
        tabLayout.addTab(tabLayout.newTab().setText("Music Box Showroom"));
        tabLayout.addTab(tabLayout.newTab().setText("Control Showroom"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //usb
        usbManager = (UsbManager) this.getSystemService(this.USB_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        this.registerReceiver(broadcastReceiver, filter);
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
                           // setUiEnabled(true);
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




    @Override
    public void onFragmentInteraction (Uri uri){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        mymenu = menu;
        setUiEnabled(true);

        return super.onCreateOptionsMenu(menu);
        //return  true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_update:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_update,null);
                final EditText mPassword = (EditText) mView.findViewById(R.id.password_updated);
                Button mLogin = (Button) mView.findViewById(R.id.button_updated);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                final String password = userInfo.getKeyPassword();

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mPassword.getText().toString().equals(password)){
                            Toast.makeText(getApplicationContext(),"Update",Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Wrong password",Toast.LENGTH_SHORT).show();
                            mPassword.setText("");
                        }
                    }
                });
                return true;
            case R.id.action_logout:
                //Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();
//                userSession.setLoggedin(false);
//                userInfo.clearUserInfo();
//                Intent logout = new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(logout);
//                finish();
//                return true;
            case R.id.action_connect:
                HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
                if (!usbDevices.isEmpty()) {
                    boolean keep = true;
                    for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                        device = entry.getValue();
                        int deviceVID = device.getVendorId();
                        if (deviceVID == 0x2A03)//Arduino Vendor ID
                        {
                            PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                            usbManager.requestPermission(device, pi);

                            setUiEnabled(false);
                            keep = false;
                        } else {
                            connection = null;
                            device = null;
                        }

                        if (!keep)
                            break;
                    }
                }
                return true;
            case R.id.action_disconnect:
                setUiEnabled(true);

                serialPort.close();
                //tvAppend(textView,"\nSerial Connection Closed! \n");
                toast("Serial Connection Closed!");
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void setUiEnabled(boolean bool) {
        mymenu.findItem(R.id.action_disconnect).setEnabled(!bool);
        mymenu.findItem(R.id.action_connect).setEnabled(bool);



    }
    private void toast(String x){
        Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
    }


}
