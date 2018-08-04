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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        //Init navigation drawer
        new DrawerBuilder().withActivity(this).build();
        createNavigationDrawer(savedInstanceState);


    }

    private void createNavigationDrawer(Bundle savedInstanceState) {

        PrimaryDrawerItem aboutUs = new PrimaryDrawerItem().
                withIdentifier(1).
                withName(R.string.drawer_about_us)
                .withIcon(R.mipmap.about_us);

        PrimaryDrawerItem help = new PrimaryDrawerItem().
                withIdentifier(1).
                withName(R.string.drawer_cara_bayar)
                .withIcon(R.mipmap.help);


        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.drawer_header)
                .withDrawerGravity(Gravity.START)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(-1)
                .addDrawerItems(help, aboutUs, new DividerDrawerItem()
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
}

