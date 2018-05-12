package com.programmerbaper.focus.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.programmerbaper.focus.entity.Jam;
import com.programmerbaper.fokus.R;

import cn.iwgang.countdownview.CountdownView;

public class MainActivity extends AppCompatActivity {

    private CountdownView mCountDown;
    private Button mStart;

    private Context mContext;
    private Activity mActivity;
    private NotificationManager mNotificationManager;

    private Jam mJam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        mStart = findViewById(R.id.start);
        mCountDown = findViewById(R.id.count_down);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askTime();


            }
        });


    }

    private void askTime() {

        MyTimePickerDialog timePickerDialog = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {

                mJam = new Jam(hourOfDay, minute, seconds);

                Toast.makeText(MainActivity.this,
                        "Focus has been set for " + mJam.getJam() + " hours " + mJam.getMenit() + " minutes " + mJam.getDetik() + " seconds.", Toast.LENGTH_LONG).show();

                mStart.setVisibility(View.GONE);
                mCountDown.setVisibility(View.VISIBLE);
                mCountDown.start(convertJamToMilisecond(mJam));
                changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);
                mCountDown.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        Toast.makeText(MainActivity.this,
                                "The focus time has ended", Toast.LENGTH_LONG).show();
                        changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL);

                    }
                });


            }
        }, 0, 30, 0, true);

        timePickerDialog.show();
        timePickerDialog.setTitle("Focus length");

    }

    private int convertJamToMilisecond(Jam jam) {

        return (jam.getJam() * 3600 + jam.getMenit() * 60 + jam.getDetik()) * 1000;
    }


    protected void changeInterruptionFiler(int interruptionFilter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                mNotificationManager.setInterruptionFilter(interruptionFilter);
            } else {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onPause() {
        Toast.makeText(this, "You've failed to focus...", Toast.LENGTH_LONG).show();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Toast.makeText(this, "You've failed to focus...", Toast.LENGTH_LONG).show();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "You've failed to focus...", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}

