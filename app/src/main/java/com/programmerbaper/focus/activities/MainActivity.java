package com.programmerbaper.focus.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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


    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    public static NotificationManager mNotificationManager;
    public static Boolean mIsFocus = false;

    private AHBottomNavigation mBottomNavigation;
    private CountdownView mCountDown;
    private Button mStart;
    private Button mCancel;
    private Drawer mDrawer;
    private Toolbar mToolBar;
    private RelativeLayout mFocus;

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
        startService(new Intent(this, NotificationService.class));

        //Binding views
        mStart = findViewById(R.id.start);
        mCancel = findViewById(R.id.cancel);
        mCountDown = findViewById(R.id.count_down);
        mFocus = findViewById(R.id.focus);
        mBottomNavigation = findViewById(R.id.bottom_navigation);


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
        mDrawer = createNavigationDrawer(savedInstanceState, mToolBar, this);

        //Init bottom navigation
        createBottomNav(this, mBottomNavigation);


    }

    public static Drawer createNavigationDrawer(Bundle savedInstanceState, Toolbar toolBar, final Activity activity) {

        Drawer drawer;

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

        PrimaryDrawerItem logout = new PrimaryDrawerItem().
                withIdentifier(5).
                withName(R.string.label_logout)
                .withIcon(R.drawable.logout_grey);


        PrimaryDrawerItem aboutUs = new PrimaryDrawerItem().
                withIdentifier(6).
                withName(R.string.drawer_about_us)
                .withIcon(R.drawable.about_us);


        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withHeader(R.layout.drawer_header)
                .withDrawerGravity(Gravity.START)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(-1)
                .withToolbar(toolBar)
                .addDrawerItems(todo, achievement, statistic, logout, aboutUs, new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {

                            case 1: {
                                activity.startActivity(new Intent(activity, TodoActivity.class));
                                break;
                            }

                            case 2: {
                                activity.startActivity(new Intent(activity, AchievementActivity.class));
                                break;
                            }

                            case 3: {
                                activity.startActivity(new Intent(activity, StatisticActivity.class));
                                break;
                            }


                            case 4: {
                                activity.startActivity(new Intent(activity, LoginActivity.class));
                                LoginActivity.user = null;
                                break;
                            }

                            case 5: {
                                activity.startActivity(new Intent(activity, AboutUsActivity.class));
                                break;
                            }

                        }

                        return true;
                    }
                })
                .build();

         TextView email = drawer.getHeader().findViewById(R.id.email);

        email.setText(LoginActivity.user.getEmail());

        return drawer;

    }


    public static void createBottomNav(final Context context, final AHBottomNavigation ahBottomNavigation) {


        AHBottomNavigationItem home = new AHBottomNavigationItem(R.string.label_home, R.drawable.home_white, R.color.primary);
        AHBottomNavigationItem todo = new AHBottomNavigationItem(R.string.label_todo, R.drawable.todo_white, R.color.primary);
        AHBottomNavigationItem achievement = new AHBottomNavigationItem(R.string.label_achievement, R.drawable.achievement_white, R.color.primary);
        AHBottomNavigationItem statistic = new AHBottomNavigationItem(R.string.label_statistic, R.drawable.statistic_white, R.color.primary);

        ahBottomNavigation.addItem(home);
        ahBottomNavigation.addItem(todo);
        ahBottomNavigation.addItem(achievement);
        ahBottomNavigation.addItem(statistic);

        if (context instanceof MainActivity)
            ahBottomNavigation.setCurrentItem(0);
        else if (context instanceof TodoActivity)
            ahBottomNavigation.setCurrentItem(1);
        else if (context instanceof AchievementActivity)
            ahBottomNavigation.setCurrentItem(2);
        else if (context instanceof StatisticActivity)
            ahBottomNavigation.setCurrentItem(3);


        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            @Override
            public boolean onTabSelected(final int position, boolean wasSelected) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (position) {

                            case 0: {
                                if (!(context instanceof MainActivity)) {
                                    context.startActivity(new Intent(context, MainActivity.class));
                                }
                                break;
                            }

                            case 1: {
                                if (!(context instanceof TodoActivity)) {
                                    context.startActivity(new Intent(context, TodoActivity.class));
                                }
                                break;
                            }

                            case 2: {
                                if (!(context instanceof AchievementActivity)) {
                                    context.startActivity(new Intent(context, AchievementActivity.class));
                                }
                                break;
                            }

                            case 3: {
                                if (!(context instanceof StatisticActivity)) {
                                    context.startActivity(new Intent(context, StatisticActivity.class));
                                }
                                break;
                            }


                        }

                    }
                }, 100);
                return true;
            }
        });


        ahBottomNavigation.setColored(true);
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
                mToolBar.setVisibility(View.GONE);
                mBottomNavigation.setVisibility(View.GONE);
                mFocus.setVisibility(View.VISIBLE);
                mCountDown.setVisibility(View.VISIBLE);
                mCountDown.start(convertJamToMilisecond(clock));
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endFocus(true);
                    }
                });
                mIsFocus = true;
                changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);
                mCountDown.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        endFocus(false);
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
                    }
                });
        return (alertDialogBuilder.create());
    }

    private void endFocus(boolean cancel) {

        if (cancel)

            Toast.makeText(MainActivity.this,
                    "You've canceled the focus time", Toast.LENGTH_LONG).show();

        else

            Toast.makeText(MainActivity.this,
                    "The focus time has ended", Toast.LENGTH_LONG).show();


        changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL);
        mIsFocus = false;
        mStart.setVisibility(View.VISIBLE);
        mToolBar.setVisibility(View.VISIBLE);
        mBottomNavigation.setVisibility(View.VISIBLE);
        mFocus.setVisibility(View.GONE);
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

