package com.example.elin.sudoku;

import android.app.ActionBar;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Tile gameMap[][] = new Tile[4][4];
    int row; //rowen som den tomma rutan är på
    int col; //columnen som den tomma rutan är på
    int numMoves;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gMapL = (GridLayout) findViewById(R.id.gameMap);
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gameMap[i][j] = new Tile(i, j, this);
                gameMap[i][j].setText((16-(j*4+i)) + "");
                gameMap[i][j].setLayoutParams(new ViewGroup.LayoutParams(170,170));
                //gameMap[i][j].setWidth(80);
                gameMap[i][j].setOnClickListener(this);
                gMapL.addView(gameMap[i][j]);
            }
        }

        gameMap[0][0].setText("");
        gameMap[0][0].setEnabled(false);
        row = 0;
        col = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        Tile t = (Tile) findViewById(v.getId());
        int i = t.r;
        int j = t.c;

        if (row == i) {
            if (col > j) {
                while (col - 1 >= j){
                    oneMove(i, col - 1);
                }
            } else {
                while (col + 1 <= j){
                    oneMove(i, col + 1);
                }
            }
        } else if (col == j) {
            if (row > i) {
                while (row - 1 >= i){
                    oneMove(row - 1, j);
                }
            } else {
                while (row + 1 <= i){
                    oneMove(row + 1, j);
                }
            }
        } else {

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
        //d.setText("Antal drag: " + drag);
    }
}

class Tile extends Button {
    int r;
    int c;
    public Tile (int i, int j, Context context) {
        super(context);
        super.setId(1 + j + i*4);
        r = i;
        c = j;
    }
}
