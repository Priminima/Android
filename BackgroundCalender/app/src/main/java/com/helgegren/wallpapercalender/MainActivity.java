package com.helgegren.wallpapercalender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private WallpaperChanger mWallpaperChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWallpaperChanger = new WallpaperChanger(getApplicationContext());
        todo();
    }

    private void todo() {
        int month = new Date().getMonth();
        mWallpaperChanger.saveWallpaperForMonth(month,
                getExternalFilesDir("PICTURES").getAbsolutePath() + "/" + month + ".jpg");
        mWallpaperChanger.setBackground(month);
    }

    private void triggerJob() {

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(5, new ComponentName(getApplicationContext(), ChangeTrigger.class))
                .build();
        jobScheduler.schedule(jobInfo);
    }
}

