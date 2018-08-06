package com.programmerbaper.focus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.programmerbaper.fokus.R;

public class StatisticActivity extends AppCompatActivity {

    private AHBottomNavigation mBottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        mBottomNavigation = findViewById(R.id.bottom_navigation);
        MainActivity.createBottomNav(this, mBottomNavigation);

        setTitle(LoginActivity.user.getName().substring(0, 1).toUpperCase() +
                LoginActivity.user.getName().substring(1) + " 's Statistics");

    }
}
