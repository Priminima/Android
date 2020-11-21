package com.helgegren.wallpapercalender;

import android.os.Bundle;
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
    }

    private void setUpButtons() {
        Calendar calendar;
        String[] arraySpinner = new String[Calendar.UNDECIMBER];
        for (int i = 0; i <= Calendar.DECEMBER; i++) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, i);
            String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            arraySpinner[i] = month;
        }

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
        mWallpaperChanger.setBackground(selectedMonth + 1);
    }

}

