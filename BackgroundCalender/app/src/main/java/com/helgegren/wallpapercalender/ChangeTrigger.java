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

    static void triggerJobNextMonth(Context context) {
        Calendar cal = Calendar.getInstance();
        long currentMs = cal.getTimeInMillis();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1, 0,0,0);
        long timeToTrigger = cal.getTimeInMillis() - currentMs;

        if (BuildConfig.DEBUG) {
            timeToTrigger = 60 * 1000;
        }
        Log.v(LOG_TAG, cal.getTime() + " date");
        Log.v(LOG_TAG, timeToTrigger + " ms");
        scheduleJob(context, timeToTrigger);
    }

    static void triggerJobNow(Context context) {
        scheduleJob(context, 0);
    }

    private static void scheduleJob(Context context, long latency) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (null == jobScheduler) {
            Log.w(LOG_TAG, "Could not trigger job, no jobscheduler availible from context");
            return;
        }

        JobInfo jobInfo = new JobInfo.Builder(5, new ComponentName(context, ChangeTrigger.class))
                .setMinimumLatency(latency)
                .build();

        jobScheduler.schedule(jobInfo);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        if (BuildConfig.DEBUG) {
            month = Calendar.getInstance().get(Calendar.MINUTE) % 12 + 1;
        }

        Log.v(LOG_TAG, "triggered!" + month);
        new WallpaperChanger(getApplicationContext()).setBackground(month);
        triggerJobNextMonth(getApplicationContext());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
