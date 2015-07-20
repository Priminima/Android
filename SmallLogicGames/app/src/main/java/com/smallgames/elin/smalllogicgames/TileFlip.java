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

public class TileFlip extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    int gameSize = 5;
    Tile[][] tile = new Tile[gameSize][gameSize];
    int x = 20; //how many steps it shuffles
    int numMoves = 0;
    int color1 = 0xFF12253B;
    int color2 = 0xFF8CF4FF;

    public TileFlip() {
    }

    public static TileFlip newInstance(int sectionNumber) {
        TileFlip fragment = new TileFlip();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        View v = getView();
        GridLayout gMapL = (GridLayout) rootView.findViewById(R.id.gridTF);

        Tile k;
        for (int i = 0; i < gameSize; i++) {
            for (int j = 0; j < gameSize; j++) {
                k = new Tile(i, j, getActivity());
                k.setText("");
                k.getBackground().setColorFilter(color1, PorterDuff.Mode.MULTIPLY);
                k.setOnClickListener(this);
                k.setLayoutParams(new ViewGroup.LayoutParams(140, 140));
                gMapL.addView(k);
                tile[i][j] = k;
            }
        }
        shuffle();

        rootView.findViewById(R.id.NewGame).setOnClickListener(this);
        gMapL.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.gridLM).setVisibility(View.INVISIBLE);
        return rootView;
    }

    public void shuffle() {
        Random ran = new Random();
        for (int i = 0; i < x; i++) {
            click(ran.nextInt(gameSize), ran.nextInt(gameSize));
        }
    }

    private void newGame() {
        shuffle();
        numMoves = 0;
        TextView v = (TextView) getView().findViewById(R.id.textView);
        String s = getResources().getString(R.string.numMoves);
        v.setText(s + " " + numMoves);
    }

    public void click(int r, int c) {
        int dirsX[] = new int[]{0, 0, 1, 0, -1};
        int dirsY[] = new int[]{0, -1, 0, 1, 0};
        int a, b;
        for (int i = 0; i < dirsX.length; i++) {
            a = r + dirsX[i];
            b = c + dirsY[i];
            if (a >= 0 && a < gameSize && b >= 0 && b < gameSize) {
                FlipOneTile(tile[a][b]);
            }
        }
    }

    private void FlipOneTile(Tile k) {
        if (k.up) {
            k.up = false;
            k.getBackground().setColorFilter(color2, PorterDuff.Mode.MULTIPLY);
        } else {
            k.up = true;
            k.getBackground().setColorFilter(color1, PorterDuff.Mode.MULTIPLY);
        }
    }

    public void onClick(View v) {
        if (getView().findViewById(v.getId()).getClass().equals(Tile.class)) {
            Tile k = (Tile) getView().findViewById(v.getId());
            numMoves++;
            TextView tv = (TextView) getView().findViewById(R.id.textView);
            tv.setText(getResources().getString(R.string.numMoves) + " " + numMoves);
            click(k.r, k.c);
        } else {
            newGame();
        }
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
            super.setId(1 + c + r * gameSize);
            this.r = r;
            this.c = c;
        }
    }
}
