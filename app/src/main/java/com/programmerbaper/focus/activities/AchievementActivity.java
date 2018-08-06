package com.programmerbaper.focus.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.programmerbaper.fokus.R;

public class AchievementActivity extends AppCompatActivity {

    private AHBottomNavigation mBottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        mBottomNavigation = findViewById(R.id.bottom_navigation);
        MainActivity.createBottomNav(this, mBottomNavigation);


        setTitle(LoginActivity.user.getName().substring(0, 1).toUpperCase()
                + LoginActivity.user.getName().substring(1) + " 's Achievement");
    }
}
