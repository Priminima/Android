package com.helgegren.wallpapercalender;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

public class WallpaperChanger {

    private final Context mContext;

    WallpaperChanger(Context context) {
        mContext = context;
    }

    void setBackground(int month) {
        Log.v("ELIN", "setBackround");
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
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
        return BitmapFactory.decodeFile(path);
    }

    @NonNull
    private String getPicturePath(int month) {
        return mContext.getExternalFilesDir("PICTURES").getAbsolutePath() + "/" + month + ".jpg";
    }
}
