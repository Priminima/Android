package com.helgegren.wallpapercalender;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

public class WallpaperChanger {

    private static final String SHARED_PREFS_NAME = "com.helgegren.wallpaperchanger.SHARED_PREFS";
    private static final String MONTH = "month";
    private final Context mContext;

    WallpaperChanger(Context context) {
        mContext = context;
    }

    void setBackground(int month) {
        WallpaperManager wallpaperManager =
                WallpaperManager.getInstance(mContext);
        String path = getPicturePath(month);
        Bitmap monthPicture = getBitmap(path);
        if (null != monthPicture) {
            try {
                wallpaperManager.setBitmap(monthPicture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    private Bitmap getBitmap(@Nullable String path) {
        if (path == null) {
            return null;
        }
        Bitmap picture = BitmapFactory.decodeFile(path);
        if (null != picture) {
            //picture = getScaledBitmap(picture);
        }
        return picture;
    }

    private Bitmap getScaledBitmap(@NonNull Bitmap picture) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int originalHeight = picture.getHeight();
        int originalWidth = picture.getWidth();
        double ratio = (double) size.y / originalHeight;
        Log.v("ELIN", "h" + originalHeight);
        Log.v("ELIN", "h" + size.y);
        Log.v("ELIN", "w" + originalWidth);
        Log.v("ELIN", "w" + size.x);
        Log.v("ELIN", "r" + ratio);
        return Bitmap.createScaledBitmap(picture, (int) (originalWidth * ratio), size.y, false);
    }

    @NonNull
    private String getPicturePath(int month) {
        return mContext.getExternalFilesDir("PICTURES").getAbsolutePath() + "/" + month + ".jpg";
    }

    @Nullable
    private String getPicturePathFromShared(int month) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(MONTH + month, null);
    }

    void saveWallpaperForMonth(int month, String path) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MONTH + month, path);
        editor.apply();
    }
}
