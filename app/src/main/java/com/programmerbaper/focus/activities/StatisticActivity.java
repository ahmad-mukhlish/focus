package com.programmerbaper.focus.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.programmerbaper.fokus.R;

public class StatisticActivity extends AppCompatActivity {

    private AHBottomNavigation mBottomNavigation;
    private Drawer mDrawer;
    private Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        mBottomNavigation = findViewById(R.id.bottom_navigation);
        MainActivity.createBottomNav(this, mBottomNavigation);

        setTitle(LoginActivity.user.getName().substring(0, 1).toUpperCase() +
                LoginActivity.user.getName().substring(1) + " 's Statistics");


        //Set Toolbar
        mToolBar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolBar);

        //Init navigation drawer
        new DrawerBuilder().withActivity(this).build();
        mDrawer = MainActivity.createNavigationDrawer(savedInstanceState, mToolBar, this);

    }
}
