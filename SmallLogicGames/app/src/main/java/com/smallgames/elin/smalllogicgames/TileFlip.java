package com.smallgames.elin.smalllogicgames;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by elin on 2015-04-17.
 */
public class TileFlip extends android.support.v4.app.Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
    Tile[][] tile = new Tile[5][5];
    int x = 10; //hur många steg den blandar
    int totCounter = 0;
    //JLabel count = new JLabel("Totalt antal klick: " + totCounter);
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
    public static TileFlip newInstance(int sectionNumber) {
        TileFlip fragment = new TileFlip();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TileFlip() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        View v = getView();
        GridLayout gMapL = (GridLayout) rootView.findViewById(R.id.gridTF);

        Tile k = null;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                k = new Tile(i, j, getActivity());
                k.setText("");
                k.getBackground().setColorFilter(0xFF12253B, PorterDuff.Mode.MULTIPLY);
                //k.setBackgroundColor(0xFFFF0000); // 0xAARRGGBB
                k.setOnClickListener(this);
                k.setLayoutParams(new ViewGroup.LayoutParams(140, 140));
                gMapL.addView(k);
                tile[i][j] = k;
            }
        }
        shuffle();

        gMapL.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.gridLM).setVisibility(View.INVISIBLE);
        return rootView;
    }

    public void shuffle() {
        Random ran = new Random();
        for (int i = 0; i < x; i++) {
            click(ran.nextInt(5), ran.nextInt(5));
        }
    }

    public void click(int r, int c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (((Math.abs(r - i) <= 1) && j == c)
                        || (Math.abs(c - j) <= 1) && i == r) {
                    FlipOneTile(tile[i][j]);
                }
            }
        }
    }

    private void FlipOneTile(Tile k) {
        if (k.up) {
            k.up = false;
            k.getBackground().setColorFilter(0xFF8CF4FF, PorterDuff.Mode.MULTIPLY);
        } else {
            k.up = true;
            k.getBackground().setColorFilter(0xFF12253B, PorterDuff.Mode.MULTIPLY);
        }
    }

    public void onClick(View v) {

        Tile k = (Tile) getView().findViewById(v.getId());
//			k.n++;
        totCounter++;
        TextView tv = (TextView) getView().findViewById(R.id.textView);
        tv.setText("Antal drag: " + totCounter);
        click(k.r, k.c);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    class Tile extends Button {
        int r;
        int c;
        boolean up = true;
        public Tile(int r, int c, Context con) {
            super(con);
            super.setId(1 + c + r * 5);
            this.r = r;
            this.c = c;
        }
    }
}
