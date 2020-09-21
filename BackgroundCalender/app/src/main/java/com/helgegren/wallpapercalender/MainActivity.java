package com.helgegren.wallpapercalender;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private WallpaperChanger mWallpaperChanger;
    private LinearLayout mLayout;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.spinner_view);

        mWallpaperChanger = new WallpaperChanger(getApplicationContext());

        setUpButtons();
        triggerJob();
    }

    private void setUpButtons() {
        Calendar calendar;
        for (int i = 0; i <= Calendar.DECEMBER; i++) {
            Button button = new Button(this);
            calendar = Calendar.getInstance();
            String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            button.setText(month);
        }

        String[] arraySpinner = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
        };
        mSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mLayout.addView(mSpinner);

        Button setButton = new Button(this);
        setButton.setOnClickListener(this::onSetWallpaper);
        setButton.setText("Set wallpaper");
        mLayout.addView(setButton);
    }

    private void onSetWallpaper(View view) {
        int selectedMonth = mSpinner.getSelectedItemPosition();
        mWallpaperChanger.setBackground(selectedMonth);
    }

    private void triggerJob() {

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (null == jobScheduler) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        long currentMs = cal.getTimeInMillis();
        //cal.set(cal.get(Calendar.YEAR), currentMonth + 1 % 12, cal.get(Calendar.DATE));
        //cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 10);
        long timeToTrigger = cal.getTimeInMillis() - currentMs;
        Log.v("ELIN", cal.getTime() + " date");
        Log.v("ELIN", timeToTrigger + " ms");

        JobInfo jobInfo = new JobInfo.Builder(5, new ComponentName(getApplicationContext(), ChangeTrigger.class))
                .setMinimumLatency(timeToTrigger)
                .build();

        jobScheduler.schedule(jobInfo);
    }
}

