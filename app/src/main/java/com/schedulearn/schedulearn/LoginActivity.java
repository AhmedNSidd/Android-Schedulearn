package com.schedulearn.schedulearn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText emailAddress;
    private EditText password;
    private TextView responseTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailAddress = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);
        responseTv = findViewById(R.id.response);

//        emailAddress.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                emailAddress.setText(charSequence.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        password.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                password.setText(charSequence.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

    }

    public void login(View v) {
        Log.d("LoginActivity.class", emailAddress.getText().toString());
        Log.d("LoginActivity.class", password.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.schedulearn.com/api/v1/rest-auth/login/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("com.schedulearn.schedulearn.USER_INFO", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userToken", response);
                        editor.putBoolean("isLoggedIn", true);
                        editor.commit();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseTv.setText("Unable to login with the provided credentials");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map <String, String> params = new HashMap<String, String>();
                params.put("email", emailAddress.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
