package com.programmerbaper.focus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.programmerbaper.fokus.R;
import com.programmerbaper.focus.entity.Jam;

public class MainActivity extends AppCompatActivity {

    Jam jam ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = findViewById(R.id.start);
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

                jam = new Jam(hourOfDay,minute,seconds) ;

                Toast.makeText(MainActivity.this,
                        "Jam diset untuk " + jam.getJam() + " jam " + jam.getMenit() + " menit " + jam.getDetik() +" detik.", Toast.LENGTH_LONG).show();


            }
        }, 0, 30, 0, true);

        timePickerDialog.show();
        timePickerDialog.setTitle("Focus length");






    }

}
