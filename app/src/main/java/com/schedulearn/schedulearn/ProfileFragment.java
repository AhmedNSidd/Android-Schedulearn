package com.schedulearn.schedulearn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ProfileFragment extends Fragment {

    private Button mSignOut;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.user_preferences_file_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.user_preferences_token_key), getString(R.string.user_preferences_token_default_val));
        String url = "https://www.schedulearn.com/api/v1/profile/" + token + "?format=json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MainActivity.java", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            String message = data.getString("detail");
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                        } catch (UnsupportedEncodingException errorr) {
                        }
                    }
                }
        );
        queue.add(stringRequest);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.user_preferences_file_name), Context.MODE_PRIVATE);
        View v = inflater.inflate(R.layout.fragment_profile, container,false);
        mSignOut = v.findViewById(R.id.sign_out_button);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit()
                        .remove(getString(R.string.user_preferences_auth_status_key))
                        .remove(getString(R.string.user_preferences_token_key))
                        .commit();
                startActivity(new Intent(view.getContext(), HomeActivity.class));
                Activity activity = (Activity) view.getContext();
                activity.finish();
            }
        });
        return v;
    }
}
