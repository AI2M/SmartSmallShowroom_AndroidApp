package com.example.tongchaitonsau.smartsmallshowroom;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PurchaseActivity extends AppCompatActivity {
    private TextView name;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bindView();
        initView();

    }

    public void initView(){
        initOnClickListener();

        findViewById(R.id.purchase_i).setOnClickListener(onClickListener);
    }
    public void bindView(){
        String pass_name = getIntent().getStringExtra("PASS_NAME");
        name = (TextView) findViewById(R.id.name_pc);
        name.setText(pass_name);
    }

    public void initOnClickListener(){
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.purchase_i :
                        Intent next = new Intent(PurchaseActivity.this, GetmoneyActivity.class);
                        startActivity(next);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
