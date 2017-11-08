package com.example.tongchaitonsau.smartsmallshowroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class Questionnaire extends AppCompatActivity {
    RadioButton rb_age;
    RadioGroup rg_age;
    RadioButton rb_salary;
    RadioGroup rg_salary;
    EditText tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tel =  (EditText)findViewById(R.id.tel_edt);
        SubmitButton();
        Sex();
        Job();

    }
    public void ageclick(View v){
        rg_age = (RadioGroup) findViewById(R.id.age_data);
        int radiobuttonage= rg_age.getCheckedRadioButtonId();
        RadioButton rb_age = (RadioButton)findViewById(radiobuttonage);
        Toast.makeText(getBaseContext(),rb_age.getText(),Toast.LENGTH_LONG).show();


    }
    public void salaryclick(View v){
        rg_salary = (RadioGroup) findViewById(R.id.salary_data);
        int radiobuttonsalary = rg_salary.getCheckedRadioButtonId();
        RadioButton rb_salary = (RadioButton)findViewById(radiobuttonsalary);
        Toast.makeText(getBaseContext(),rb_salary.getText(),Toast.LENGTH_LONG).show();


    }

    public void Sex(){
        Spinner sex_spinner = (Spinner)findViewById(R.id.sex_data);
        ArrayAdapter<String> myAdapterSex= new ArrayAdapter<String>(Questionnaire.this,
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

    public void Job(){
        Spinner job_spinner = (Spinner)findViewById(R.id.job_data);
        ArrayAdapter<String> myAdapterJob= new ArrayAdapter<String>(Questionnaire.this,
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
    public void SubmitButton(){
        Button submit_question  = (Button) findViewById(R.id.submit_question);

        submit_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(Questionnaire.this,MainActivity.class);
                startActivity(back);
                finish();

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

    //hide keyboard


    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Questionnaire.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
