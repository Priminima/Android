package com.smallgames.elin.smalllogicgames;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by elin on 2015-04-16.
 */
public class Fifteen extends android.support.v4.app.Fragment implements View.OnClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    Tile gameMap[][] = new Tile[4][4];
    int row; //rowen som den tomma rutan är på
    int col; //columnen som den tomma rutan är på
    int numMoves;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fifteen newInstance(int sectionNumber) {
        Fifteen fragment = new Fifteen();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Fifteen() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        View v = getView();

        GridLayout gMapL = (GridLayout) rootView.findViewById(R.id.gridLM);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gameMap[i][j] = new Tile(i, j, getActivity());
                gameMap[i][j].setText((16 - (j * 4 + i)) + "");
                gameMap[i][j].setLayoutParams(new ViewGroup.LayoutParams(170, 170));
                //gameMap[i][j].setWidth(80);
                gameMap[i][j].setOnClickListener(this);
                gMapL.addView(gameMap[i][j]);
            }
        }

        gameMap[0][0].setText("");
        gameMap[0][0].setEnabled(false);
        row = 0;
        col = 0;

        gMapL.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.gridTF).setVisibility(View.INVISIBLE);

        return rootView;
    }
    public void onClick(View v) {

        Tile t = (Tile) getView().findViewById(v.getId());
        int i = t.r;
        int j = t.c;

        if (row == i) {
            if (col > j) {
                while (col - 1 >= j) {
                    oneMove(i, col - 1);
                }
            } else {
                while (col + 1 <= j) {
                    oneMove(i, col + 1);
                }
            }
        } else if (col == j) {
            if (row > i) {
                while (row - 1 >= i) {
                    oneMove(row - 1, j);
                }
            } else {
                while (row + 1 <= i) {
                    oneMove(row + 1, j);
                }
            }
        } else {
            Toast.makeText(getView().getContext(), "Illegal move", Toast.LENGTH_SHORT).show();
        }
    }

    public void oneMove(int i, int j) {
        gameMap[row][col].setText(gameMap[i][j].getText());
        gameMap[row][col].setEnabled(true);
        gameMap[i][j].setText("");
        gameMap[i][j].setEnabled(false);
        row = i;
        col = j;
        numMoves++;
        TextView v = (TextView) getView().findViewById(R.id.textView);
        v.setText("Antal drag: " + numMoves);
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

        public Tile(int i, int j, Context context) {
            super(context);
            super.setId(1 + j + i * 4);
            r = i;
            c = j;
        }
    }
}



