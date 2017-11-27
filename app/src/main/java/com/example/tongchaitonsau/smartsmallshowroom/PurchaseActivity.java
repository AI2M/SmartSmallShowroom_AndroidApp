package com.example.tongchaitonsau.smartsmallshowroom;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PurchaseActivity extends AppCompatActivity {
    private TextView name;
    private ImageView pic_music;
    Button play,stop;
    int posmusic;
    private View.OnClickListener onClickListener;
    MediaPlayer music ;

    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";
    Button connect ,disconnect;
    //TextView textView;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bindView();
        initView();

        music.start();
        music.setLooping(true);

    }

    public void initView(){
        usbManager = (UsbManager) this.getSystemService(this.USB_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        this.registerReceiver(broadcastReceiver, filter);

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
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
        }

        initOnClickListener();
        findViewById(R.id.play_btn).setOnClickListener(onClickListener);
        findViewById(R.id.stop_btn).setOnClickListener(onClickListener);
    }
    public void bindView(){
        String pass_name = getIntent().getStringExtra("PASS_NAME");
        name = (TextView) findViewById(R.id.name_pc);
        name.setText(pass_name);
        music = MediaPlayer.create(this,R.raw.paino);
        play = (Button) findViewById(R.id.play_btn);
        pic_music = (ImageView) findViewById(R.id.music_box_picture);
        stop = (Button) findViewById(R.id.stop_btn);
        play.setEnabled(false);
        stop.setEnabled(true);
        //new LoadimageTask().execute("https://wallpaperscraft.com/image/milky_way_stars_mountains_night_germany_bavaria_sky_45888_1920x1080.jpg");

    }

    @Override
    protected void onPause() {
        super.onPause();
        music.release();
        music=null;
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
                            String string = "ok_send".toString();
                            serialPort.write(string.getBytes());

                            //tvAppend(textView,"Serial Connection Opened!\n");
                            //toast("Serial Connection Opened!");

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

    public void initOnClickListener(){

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                switch (view.getId()){
                    case  R.id.play_btn :
                        music.seekTo(posmusic);
                        music.start();
                        String string = "ok_send".toString();
                        serialPort.write(string.getBytes());
                        toast("open");
                        play.setEnabled(false);
                        stop.setEnabled(true);
                        break;
                    case R.id.stop_btn :
                        music.pause();
                        posmusic = music.getCurrentPosition();
                        String string2 = "no_send".toString();
                        serialPort.write(string2.getBytes());
                        toast("off");
                        stop.setEnabled(false);
                        play.setEnabled(true);
                        break;
                }
            }
        };
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== android.R.id.home)
        {
            this.finish();
            String string2 = "no_send".toString();
            serialPort.write(string2.getBytes());
            music.release();
        }
        return super.onOptionsItemSelected(item);
    }

    class LoadimageTask extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url = null;
            try {
                url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        protected void onPostExecute(Bitmap result){
            pic_music.setImageBitmap(result);
        }


    }

    private void toast(String x){
        Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
    }




}
