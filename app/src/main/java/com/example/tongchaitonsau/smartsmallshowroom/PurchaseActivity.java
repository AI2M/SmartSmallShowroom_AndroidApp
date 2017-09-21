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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        String pass_name = getIntent().getStringExtra("PASS_NAME");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button purchase = (Button) findViewById(R.id.purchase_i);
        TextView name = (TextView) findViewById(R.id.name_pc);

        name.setText(pass_name);



        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymoney = new Intent(PurchaseActivity.this,GetmoneyActivity.class);
                startActivity(paymoney);
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
