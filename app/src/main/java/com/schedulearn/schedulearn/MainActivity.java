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
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    protected static String mToken;
    protected static String userPictureUrl;

    protected static ArrayList<String> mConnectionsPfps = new ArrayList<>();
    protected static ArrayList<String> mConnectionsNames = new ArrayList<>();

    protected static ArrayList<String> mLessonsNames = new ArrayList<>();
    protected static ArrayList<String> mLessonsPictures = new ArrayList<>();
    protected static ArrayList<String> mLessonsLocations = new ArrayList<>();
    protected static ArrayList<Date> mLessonsDates = new ArrayList<>();


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
        // If we haven't got the token, then get it, else, the token exists.
        if (mToken == null) {
            SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.user_preferences_file_name), Context.MODE_PRIVATE);
            mToken = sharedPreferences.getString(getString(R.string.user_preferences_token_key), getString(R.string.user_preferences_token_default_val));
        }
        // If the size is 0, that means the connections haven't been gotten before, however, if they
        // are 0, that means the screen was rotated, etc, but the connections exist.
        if (mConnectionsNames.size() == 0) {
            getConnections(new VolleyCallback() {
                @Override
                public void onSuccess() {
                    if (mLessonsNames.size() == 0) {
                        getLessons(new VolleyCallback() {
                            @Override
                            public void onSuccess() {
                                addFragmentFromId(R.id.action_agenda);
                            }
                        });
                    }
                }
            });
        }

        // Get User picture if we haven't got it yet.
        if (userPictureUrl == null) {
            getUserPfp();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        addFragmentFromId(bottomNavigationView.getSelectedItemId());
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
    private void getConnections(final VolleyCallback callback) {
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
                                mConnectionsNames.add(connection.getString("first_name") + " " + connection.getString("last_name"));
                            }
                            callback.onSuccess();
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

    private void getLessons(final VolleyCallback volleyCallback) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.schedulearn.com/api/v1/lessons/" + mToken + "?format=json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);
                            JSONArray lessons = data.getJSONArray("lessons");
                            for (int i = 0; i < lessons.length(); i++) {
                                JSONObject lesson = lessons.getJSONObject(i);
                                String lessonWithPerson = lesson.getString("lesson_with");
                                int indexOfPerson = mConnectionsNames.indexOf(lessonWithPerson);
                                Log.d(TAG, mConnectionsNames + "");
                                mLessonsPictures.add(mConnectionsPfps.get(indexOfPerson));
                                mLessonsNames.add(lesson.getString("name"));
                                String startTimeString = lesson.getString("start_time");
                                ParsePosition pos = new ParsePosition(0);
                                Date startTimeDate = new SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss'Z'")
                                        .parse(startTimeString, pos);
                                mLessonsDates.add(startTimeDate);
                                mLessonsLocations.add(lesson.getString("location"));
                            }
                            volleyCallback.onSuccess();

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong, while parsing your lessons."
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                "Something went wrong, try again.",
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(stringRequest);

    }

    private void getUserPfp() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.schedulearn.com/api/v1/profile/" + mToken + "?format=json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);
                            userPictureUrl = "https://www.schedulearn.com" + data.getString("profile_pic");
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
