package com.helgegren.wallpapercalender;

import android.app.job.JobParameters;
import android.app.job.JobService;

import java.util.Date;

public class ChangeTrigger extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        int month = new Date().getMonth();
        new WallpaperChanger(getApplicationContext()).setBackground(month);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
