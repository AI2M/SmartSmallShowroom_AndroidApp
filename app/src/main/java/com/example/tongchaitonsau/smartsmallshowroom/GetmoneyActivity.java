package com.example.tongchaitonsau.smartsmallshowroom;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class GetmoneyActivity extends AppCompatActivity {
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getmoney);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initView();

    }
    public void initView(){
        initOnClickListener();
        findViewById(R.id.button_full).setOnClickListener(onClickListener);

    }

    public void initOnClickListener(){
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_full:
                       addDataDialog();
                        break;
                }

            }
        };
    }


    private void addDataDialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GetmoneyActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_getdata,null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        Button mbtnOk = (Button) mView.findViewById(R.id.button_ok_user);
        mbtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }




}
