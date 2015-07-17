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

import java.util.Random;

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
    LogicTile logicTile[][] = new LogicTile[4][4];
    int gameSize = 4;
    int row; //rowen som den tomma rutan är på
    int col; //columnen som den tomma rutan är på
    int numMoves;
    public Fifteen() {

    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //View v = getView();
        GridLayout gMapL = (GridLayout) rootView.findViewById(R.id.gridLM);
        init();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gMapL.addView(gameMap[i][j]);
                //gameMap[i][j].update(logicTile[i][j]);
            }
        }

        rootView.findViewById(R.id.NewGame).setOnClickListener(this);
        gMapL.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.gridTF).setVisibility(View.INVISIBLE);

        return rootView;
    }

    public void init (){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                logicTile[i][j] = new LogicTile(i, j);
                gameMap[i][j] = new Tile(i, j, getActivity(), logicTile[i][j]);
                gameMap[i][j].setLayoutParams(new ViewGroup.LayoutParams(170, 170));
                //gameMap[i][j].setWidth(80);
                gameMap[i][j].setOnClickListener(this);
            }
        }


        //gameMap[0][0].setEnabled(false);
        row = 0;
        col = 0;

        newGame();
    }

    public void onClick(View v) {
        if (getView().findViewById(v.getId()).getClass().equals(Tile.class)) {
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
        } else {
            newGame();
        }
        Button b = (Button) getView().findViewById(R.id.NewGame);
        b.setText(isSolvable() + " " + sumInversions());
        isSolved();
    }

    public void oneMove(int i, int j) {
        /*
        gameMap[row][col].setText(gameMap[i][j].getText());
        gameMap[row][col].setEnabled(true);
        gameMap[i][j].setText("");
        gameMap[i][j].setEnabled(false);

        */
        LogicTile t = logicTile[i][j]; // t is ij
        logicTile[i][j] = logicTile[row][col];
        logicTile[row][col] = t;

        gameMap[row][col].update(logicTile[row][col]);
        gameMap[i][j].update(logicTile[i][j]);

        numMoves++;
        TextView v = (TextView) getView().findViewById(R.id.textView);
        String s = getResources().getString(R.string.numMoves);
        v.setText(s + " " + numMoves);



    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private boolean isSolved() {
        if (row == 3 && col == 3) {
            if (sumInversions() == 0) {
                Toast.makeText(getView().getContext(), "YOU WON!!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private void shuffle() {
        LogicTile lt;
        Random ran = new Random();
        int i, j;
        boolean s;

        for (int k = 0; k < 100; k++) {
            i = ran.nextInt(3); // Random row
            j = ran.nextInt(4); // Random col
            s = ran.nextBoolean();


            if (s) {
                lt = logicTile[i][j]; // get random cell
                logicTile[i][j] = logicTile[i+1][j];
                logicTile[i+1][j] = lt;
            } else {
                lt = logicTile[j][i]; // get random cell
                logicTile[j][i] = logicTile[j][i+1];
                logicTile[j][i+1] = lt;
            }
        }
        updateAll();

    }

    private void newGame() {
        LogicTile lt;
        shuffle();

        updateAll();
        if (!isSolvable()) {
            if (row == 0 && col <= 1) {
                lt = logicTile[gameSize-1][gameSize-2];
                logicTile[gameSize-1][gameSize-2] = logicTile[gameSize-1][gameSize-1];
                logicTile[gameSize-1][gameSize-1] = lt;
            } else {
                lt = logicTile[0][0]; // get random cell
                logicTile[0][0] = logicTile[0][1];
                logicTile[0][1] = lt;
            }
        }

        numMoves = 0;
        updateAll();
//        TextView v = (TextView) getView().findViewById(R.id.textView);
//        String s = getResources().getString(R.string.numMoves);
//        v.setText(s + " " + numMoves);
    }

    private void updateAll() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gameMap[i][j].update(logicTile[i][j]);
            }
        }
    }

    private int countInversions(int i, int j) {
        int inversions = 0;
        int tileNum = i * gameSize + j + 1;
        int lastTile = gameSize * gameSize;
        int tileValue = logicTile[i][j].n;
        if (tileValue < lastTile) {
            int l, k, compValue;
            for (int q = tileNum; q < lastTile; q++) {
                l = q % gameSize;
                k = q / gameSize;
                compValue = logicTile[k][l].n;
                if (tileValue > compValue && compValue < lastTile) {
                    inversions++;
                }
            }
        }
        return inversions;
    }

    private int sumInversions() {
        int inversions = 0;
        for (int i = 0; i < gameSize; i++) {
            for (int j = 0; j < gameSize; j++) {
                inversions += countInversions(i, j);
            }
        }
        return inversions;
    }

    private boolean isSolvable() {
        if (gameSize % 2 == 1) {
            return (sumInversions() % 2 == 0);
        } else {
            return ((sumInversions() - (row + 1)) % 2 == 0);
        }
    }

    class Tile extends Button {
        int r;
        int c;

        public Tile(int i, int j, Context context, LogicTile lt) {
            super(context);
            super.setId(lt.n);
            setText(lt.n + "");
            r = i;
            c = j;
        }

        void update(LogicTile l) {
            if (l.n == 16) {
                setText("");
                row = r;
                col = c;
                setEnabled(false);
            } else {
                setText(l.n + "");
                setEnabled(true);
            }
        }
    }

    class LogicTile {
        int n, r, c;

        public LogicTile(int i, int j) {
            n = (i * 4 + j) + 1;
            r = i;
            c = j;
        }
    }
}
