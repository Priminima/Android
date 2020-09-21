package com.helgegren.wallpapercalender;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class ChangeTrigger extends JobService {

    private static final String LOG_TAG = "ELIN";

    static void triggerJob(Context context) {

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (null == jobScheduler) {
            Log.w(LOG_TAG, "Could not trigger job, no jobscheduler availible from context");
            return;
        }
        Calendar cal = Calendar.getInstance();
        long currentMs = cal.getTimeInMillis();
        //cal.set(cal.get(Calendar.YEAR), currentMonth + 1 % 12, 1);
        //cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 10);
        long timeToTrigger = cal.getTimeInMillis() - currentMs;
        Log.v(LOG_TAG, cal.getTime() + " date");
        Log.v(LOG_TAG, timeToTrigger + " ms");

        JobInfo jobInfo = new JobInfo.Builder(5, new ComponentName(context, ChangeTrigger.class))
                .setMinimumLatency(timeToTrigger)
                .build();

        jobScheduler.schedule(jobInfo);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        Log.v("ELIN", "triggered!" + month);
        new WallpaperChanger(getApplicationContext()).setBackground(month);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
