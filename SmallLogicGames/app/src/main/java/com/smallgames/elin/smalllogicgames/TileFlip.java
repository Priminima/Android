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
import android.widget.Toast;

import java.util.Random;

public class TileFlip extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    int gameSize = 5;
    Tile[][] tile;
    int x = gameSize * 4; //how many steps it shuffles
    int numMoves = 0;
    int colorUp = 0xFF8CF4FF;
    int colorDown = 0xFF12253B;

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
        GridLayout gMapL = (GridLayout) rootView.findViewById(R.id.gridL);

        gMapL.setColumnCount(gameSize);
        gMapL.setRowCount(gameSize);
        tile = new Tile[gameSize][gameSize];

        Tile k;
        for (int i = 0; i < gameSize; i++) {
            for (int j = 0; j < gameSize; j++) {
                k = new Tile(i, j, getActivity());
                k.setText("");
                k.getBackground().setColorFilter(colorUp, PorterDuff.Mode.MULTIPLY);
                k.setOnClickListener(this);
                k.setLayoutParams(new ViewGroup.LayoutParams(140, 140));
                //k.setLayoutParams(new ViewGroup.LayoutParams(140, 140));
                gMapL.addView(k);
                tile[i][j] = k;
            }
        }
        shuffle();

        rootView.findViewById(R.id.NewGame).setOnClickListener(this);
        gMapL.setVisibility(View.VISIBLE);
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
            k.getBackground().setColorFilter(colorDown, PorterDuff.Mode.MULTIPLY);
        } else {
            k.up = true;
            k.getBackground().setColorFilter(colorUp, PorterDuff.Mode.MULTIPLY);
        }
    }

    private boolean isSolved() {
        for (int i = 0; i < gameSize; i++) {
            for (int j = 0; j < gameSize; j++) {
                if (!tile[i][j].up) {
                    return false;
                }
            }
        }
        return true;
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
        if (isSolved()) {
            Toast.makeText(getView().getContext(),
                    getString(R.string.won_fifteen), Toast.LENGTH_SHORT).show();
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
