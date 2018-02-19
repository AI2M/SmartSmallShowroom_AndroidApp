package com.example.tongchaitonsau.smartsmallshowroom;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import prefs.UserInfo;
import prefs.UserSession;

public class LoginActivity extends Activity {

    private EditText id;
    private EditText password;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private UserSession session;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        init();

        Button login  = (Button) findViewById(R.id.login_i);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!id.getText().toString().isEmpty() || !password.getText().toString().isEmpty()){

                    String uId = id.getText().toString().trim();
                    String pass  = password.getText().toString().trim();

                    login(uId, pass);
                }
                else{
                    AlertDialog.Builder loginerror = new AlertDialog.Builder(LoginActivity.this);
                    loginerror.setMessage("Please Login Again").setCancelable(false)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = loginerror.create();
                    alertDialog.setTitle("Login Error");
                    alertDialog.show();

                }
            }
        });

        if(session.isUserLoggedin()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }
    public void init(){
        id = (EditText) findViewById(R.id.id_i);
        password = (EditText) findViewById(R.id.password_i);
        progressDialog  = new ProgressDialog(this);
        session         = new UserSession(this);
        userInfo        = new UserInfo(this);

    }

    private void login(final String id, final String password){
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        progressDialog.setMessage("Logging in...");
        progressDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                Utils.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONArray showrooms = jObj.getJSONArray("showroom");
                        JSONObject showroom = showrooms.getJSONObject(0);
                        String uId = showroom.getString("showroom_id");
                        String uPass = showroom.getString("password");

                        // Inserting row in users table
                        userInfo.setId(uId);
                        userInfo.setPassword(uPass);
                        session.setLoggedin(true);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        toast(errorMsg);
                        Log.d("error login ",errorMsg);
                        progressDialog.hide();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    toast("Json error: " + e.getMessage());
                    Log.d("error catch ",e.getMessage());
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
                params.put("showroom_id", id);
                params.put("password", password);

                return params;
            }

        };
        // Adding request to request queue
        AndroidLoginController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }
    private void toast(String x){
        Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
    }


}
