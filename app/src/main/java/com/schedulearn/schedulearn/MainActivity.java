package com.schedulearn.schedulearn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String mToken;
    protected static ArrayList<String> mConnectionsPfps = new ArrayList<>();
    protected static ArrayList<String> mConnectionsNames = new ArrayList<>();
    private boolean mConnectionsSuccessful = false;
    // Create a listener for clicking a nav item for BottomNavigation.
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            addFragmentFromId(item.getItemId());
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mToken = savedInstanceState.getString("token", getString(R.string.user_preferences_token_default_val));
        }
        // If we haven't got the token, then get it, else, the token exists.
        if (mToken == null || mToken == getString(R.string.user_preferences_token_default_val)) {
            SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.user_preferences_file_name), Context.MODE_PRIVATE);
            mToken = sharedPreferences.getString(getString(R.string.user_preferences_token_key), getString(R.string.user_preferences_token_default_val));
        }
        // If the size is 0, that means the connections haven't been gotten before, however, if they
        // are 0, that means the screen was rotated, etc, but the connections exist.
        if (mConnectionsNames.size() == 0) {
            getConnections();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        addFragmentFromId(bottomNavigationView.getSelectedItemId());
    }

    // Store the token if activity is reset.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("token", mToken);
    }

    private void addFragmentFromId(int id) {
        Fragment currFragment = null;
        switch (id) {
            case R.id.action_agenda:
                currFragment = new AgendaFragment();
                break;
            case R.id.action_connections:
                currFragment = new ConnectionsFragment();
                break;
            case R.id.action_profile:
                currFragment = new ProfileFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, currFragment)
                .commit();
    }


    // Establish a connection and get the connections of the user.
    private void getConnections() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.schedulearn.com/api/v1/connections/" + mToken + "?format=json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray connections = data.getJSONArray("connections");
                            for (int i = 0; i < connections.length(); i++) {
                                JSONObject connection = connections.getJSONObject(i);
                                mConnectionsPfps.add("https://www.schedulearn.com" + connection.getString("profile_pic"));
                                mConnectionsNames.add(connection.getString("first_name") + " "+ connection.getString("last_name"));
                            }


                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Something went wrong, try again.", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Something went wrong, try again.", Toast.LENGTH_LONG).show();

                    }
                }
        );
        queue.add(stringRequest);
    }

}
