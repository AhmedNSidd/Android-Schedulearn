package com.schedulearn.schedulearn;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        addFragmentFromId(bottomNavigationView.getSelectedItemId());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    addFragmentFromId(item.getItemId());
                    return true;
                }
        };

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
}
