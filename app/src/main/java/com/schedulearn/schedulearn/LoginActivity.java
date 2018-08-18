package com.schedulearn.schedulearn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailAddressField;
    private EditText mPasswordField;
    private TextView mResponseTextView;
    private Button mSignInButton;
    private ProgressBar mProgressBar;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailAddressField = findViewById(R.id.email_field);
        mPasswordField = findViewById(R.id.password_field);
        mResponseTextView = findViewById(R.id.response_tv);
        mSignInButton = findViewById(R.id.sign_in_btn);
        mProgressBar = findViewById(R.id.progress_bar);
        mPasswordField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    mSignInButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    public void signIn(View v) {
        mProgressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String loginUrl = "https://www.schedulearn.com/api/v1/rest-auth/login/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String token = jsonResponse.getString("key");
                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.user_preferences_file_name), Context.MODE_PRIVATE);SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.user_preferences_token_key), token);
                            editor.putBoolean(getString(R.string.user_preferences_auth_status_key), true);
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        } catch (JSONException e) {
                            mResponseTextView.setText("Something went wrong, try again.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mResponseTextView.setText("Unable to login with the provided credentials");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map <String, String> params = new HashMap<String, String>();
                params.put("email", mEmailAddressField.getText().toString());
                params.put("password", mPasswordField.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
