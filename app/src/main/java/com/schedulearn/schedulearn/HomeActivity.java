package com.schedulearn.schedulearn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button signInBtn;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // We get shared preferences of the user. If he was already logged in but closed the app
        // then we just log him back in.
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.user_preferences_file_name), Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(getString(R.string.user_preferences_auth_status_key), false);
        if (isLoggedIn) {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        signInBtn = (Button) findViewById(R.id.sign_in);
        signUpBtn = (Button) findViewById(R.id.sign_in);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });


    }
}
