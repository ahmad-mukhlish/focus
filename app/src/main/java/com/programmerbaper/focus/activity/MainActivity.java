package com.programmerbaper.focus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.programmerbaper.focus.entity.Jam;
import com.programmerbaper.fokus.R;

import cn.iwgang.countdownview.CountdownView;

public class MainActivity extends AppCompatActivity {

    private Jam jam;
    private CountdownView countDown;
    private Button start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.start);
        countDown = findViewById(R.id.count_down);

        start.setOnClickListener(new View.OnClickListener() {
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

                jam = new Jam(hourOfDay, minute, seconds);

                Toast.makeText(MainActivity.this,
                        "Focus has been set for " + jam.getJam() + " hours " + jam.getMenit() + " minutes " + jam.getDetik() + " seconds.", Toast.LENGTH_LONG).show();

                start.setVisibility(View.GONE);
                countDown.setVisibility(View.VISIBLE);
                countDown.start(convertJamToMilisecond(jam));
                countDown.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        Toast.makeText(MainActivity.this,
                               "The focus time has ended", Toast.LENGTH_LONG).show();
                    }
                });


            }
        }, 0, 30, 0, true);

        timePickerDialog.show();
        timePickerDialog.setTitle("Focus length");

    }

    private int convertJamToMilisecond(Jam jam) {

        return (jam.getJam() * 3600 + jam.getMenit() * 60 + jam.getDetik()) * 1000  ;
    }

}
