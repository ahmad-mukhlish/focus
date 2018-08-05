package com.programmerbaper.focus.activities;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.programmerbaper.focus.entities.Clock;
import com.programmerbaper.focus.helper.NotificationService;
import com.programmerbaper.fokus.R;

import cn.iwgang.countdownview.CountdownView;

public class MainActivity extends AppCompatActivity {


    public static NotificationManager mNotificationManager;
    public static Boolean mIsFocus = false;


    private Context mContext = MainActivity.this;
    private CountdownView mCountDown;
    private Button mStart;
    private Drawer mDrawer;
    private Toolbar mToolBar;


    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check wether notification service is enabled or not
        if (!isNotificationServiceEnabled()) {
            AlertDialog enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        //Activate notification manager and notification service
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startService(new Intent(mContext, NotificationService.class));

        //Binding views
        mStart = findViewById(R.id.start);
        mCountDown = findViewById(R.id.count_down);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askTime();
            }
        });

        //Set Toolbar
        mToolBar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolBar);
        setTitle("Home");

        //Init navigation drawer
        new DrawerBuilder().withActivity(this).build();
        createNavigationDrawer(savedInstanceState);

        //Init bottom navigation
        createBottomNav();


    }

    private void createNavigationDrawer(Bundle savedInstanceState) {

        PrimaryDrawerItem todo = new PrimaryDrawerItem().
                withIdentifier(1).
                withName(R.string.label_todo)
                .withIcon(R.drawable.todo_grey);

        PrimaryDrawerItem achievement = new PrimaryDrawerItem().
                withIdentifier(2).
                withName(R.string.label_achievement)
                .withIcon(R.drawable.achievement_grey);

        PrimaryDrawerItem statistic = new PrimaryDrawerItem().
                withIdentifier(3).
                withName(R.string.label_statistic)
                .withIcon(R.drawable.statistic_grey);

        PrimaryDrawerItem setting = new PrimaryDrawerItem().
                withIdentifier(4).
                withName(R.string.label_setting)
                .withIcon(R.drawable.settings_grey);

        PrimaryDrawerItem logout = new PrimaryDrawerItem().
                withIdentifier(5).
                withName(R.string.label_logout)
                .withIcon(R.drawable.logout_grey);


        PrimaryDrawerItem aboutUs = new PrimaryDrawerItem().
                withIdentifier(6).
                withName(R.string.drawer_about_us)
                .withIcon(R.drawable.about_us);


        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.drawer_header)
                .withDrawerGravity(Gravity.START)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(-1)
                .withToolbar(mToolBar)
                .addDrawerItems(todo, achievement, statistic, setting, logout, aboutUs, new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {

                            case 1: {

                            }

                            case 2: {

                            }

                            case 3: {

                            }

                            case 4: {

                                break;
                            }

                        }

                        return true;
                    }
                })
                .build();

        TextView email = mDrawer.getHeader().findViewById(R.id.email);

//            email.setText(LoginActivity.user.getEmail());


    }


    private void createBottomNav() {

        AHBottomNavigation bottomNavigation = findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem home = new AHBottomNavigationItem(R.string.label_home, R.drawable.home_white, R.color.primary);
        AHBottomNavigationItem todo = new AHBottomNavigationItem(R.string.label_todo, R.drawable.todo_white, R.color.primary);
        AHBottomNavigationItem achievement = new AHBottomNavigationItem(R.string.label_achievement, R.drawable.achievement_white, R.color.primary);
        AHBottomNavigationItem statistic = new AHBottomNavigationItem(R.string.label_statistic, R.drawable.statistic_white, R.color.primary);


        bottomNavigation.addItem(home);
        bottomNavigation.addItem(todo);
        bottomNavigation.addItem(achievement);
        bottomNavigation.addItem(statistic);

        bottomNavigation.setColored(true);
    }

    private void askTime() {

        final MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {
                Clock clock;
                clock = new Clock(hourOfDay, minute, seconds);

                //Time is zero validation
                if (convertJamToMilisecond(clock) == 0) {
                    Toast.makeText(MainActivity.this,
                            "Time must not be zero", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MainActivity.this,
                        createStartMessage(clock), Toast.LENGTH_LONG).show();


                mStart.setVisibility(View.GONE);
                mCountDown.setVisibility(View.VISIBLE);
                mCountDown.start(convertJamToMilisecond(clock));
                mIsFocus = true;
                changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);
                mCountDown.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        Toast.makeText(MainActivity.this,
                                "The focus time has ended", Toast.LENGTH_LONG).show();
                        changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL);
                        mIsFocus = false;
                        mStart.setVisibility(View.VISIBLE);
                        mCountDown.setVisibility(View.GONE);
                    }
                });


            }
        }, 0, 30, 0, true);

        timePickerDialog.setCancelable(true);
        timePickerDialog.updateTime(0, 0, 30);
        timePickerDialog.show();
        timePickerDialog.setTitle("Focus length");


    }

    private int convertJamToMilisecond(Clock clock) {

        return (clock.getHours() * 3600 + clock.getMinutes() * 60 + clock.getSeconds()) * 1000;
    }


    protected void changeInterruptionFiler(int interruptionFilter) {

        if (mNotificationManager.isNotificationPolicyAccessGranted()) {
            mNotificationManager.setInterruptionFilter(interruptionFilter);
        } else {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }


    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private AlertDialog buildNotificationServiceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return (alertDialogBuilder.create());
    }

    private String createStartMessage(Clock clock) {

        String hours = "";
        String minutes = "";
        String seconds = "";


        if (clock.getHours() != 0) {
            hours = clock.getHours() + " hours ";
        }

        if (clock.getMinutes() != 0) {
            minutes = clock.getMinutes() + " minutes ";
        }

        if (clock.getSeconds() != 0) {
            seconds = clock.getSeconds() + " seconds ";
        }


        return "Focus has been set for " + hours + minutes + seconds;

    }


    //Reminder to user when the user trying to open another app
    @Override
    protected void onPause() {
        if (mIsFocus) {
            Toast.makeText(this, "You've failed to focus...", Toast.LENGTH_LONG).show();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mIsFocus) {
            Toast.makeText(this, "You've failed to focus...", Toast.LENGTH_LONG).show();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mIsFocus) {
            Toast.makeText(this, "You've failed to focus...", Toast.LENGTH_LONG).show();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen())
            mDrawer.closeDrawer();
        else
            Toast.makeText(this, "You've reached the main menu", Toast.LENGTH_SHORT).show();
    }
}

