package com.example.tongchaitonsau.smartsmallshowroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Button item1 = (Button) findViewById(R.id.item1_i);
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent purchase = new Intent(MainActivity.this,PurchaseActivity.class);
                startActivity(purchase);

            }
        });
    }

    private void init(){

    }
}
