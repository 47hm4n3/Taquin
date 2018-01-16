package com.pixel.taquin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener {

    int guiltyI, guiltyJ;
    Button[][] matButton;
    TextView tv;
    TextView b;
    int nbMoves = 0;
    int[][] results;
    int best=1000;
    Button reset;
    ArrayList<Integer> list;
    ArrayList<Integer> colors;
    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBestPerf(best);
        gameInitialization();
        while (!checkPossible()) {
            gameInitialization();
        }
        reset = findViewById(R.id.raz);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gameInitialization();
                while (!checkPossible()) {
                    gameInitialization();
                }
            }
        });
        Log.d("toto", "best perf = "+ getBestPerf());
        detector = new GestureDetector(this, this);
    }

    public void gameInitialization() {
        nbMoves = 0;
        tv = findViewById(R.id.moves);
        tv.setText("Moves : "+ nbMoves);

        b = findViewById(R.id.best);
        b.setText("Best : "+ getBestPerf());

        list = new ArrayList<>();
        colors = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            list.add(i + 1);

        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.WHITE);
        colors.add(Color.LTGRAY);

        Collections.shuffle(list);
        Collections.shuffle(colors);

        results = new int[3][3];
        matButton = new Button[3][3];

        matButton[0][0] = findViewById(R.id.topleft);
        matButton[0][0].setText(String.valueOf(list.get(0)));
        matButton[0][0].setBackgroundColor(colors.get(0));
        results[0][0] = list.get(0);

        matButton[1][0] = findViewById(R.id.topcenter);
        matButton[1][0].setText(String.valueOf(list.get(1)));
        matButton[1][0].setBackgroundColor(colors.get(1));
        results[1][0] = list.get(1);

        matButton[2][0] = (Button) findViewById(R.id.topright);
        matButton[2][0].setText(String.valueOf(list.get(2)));
        matButton[2][0].setBackgroundColor(colors.get(2));
        results[2][0] = list.get(2);

        matButton[0][1] = (Button) findViewById(R.id.centerleft);
        matButton[0][1].setText(String.valueOf(list.get(3)));
        matButton[0][1].setBackgroundColor(colors.get(3));
        results[0][1] = list.get(3);

        matButton[1][1] = (Button) findViewById(R.id.centercenter);
        matButton[1][1].setText("");
        matButton[1][1].setBackgroundColor(colors.get(4));
        results[1][1] = 0;

        matButton[2][1] = (Button) findViewById(R.id.centerright);
        matButton[2][1].setText(String.valueOf(list.get(4)));
        matButton[2][1].setBackgroundColor(colors.get(4));
        results[2][1] = list.get(4);

        matButton[0][2] = (Button) findViewById(R.id.bottomleft);
        matButton[0][2].setText(String.valueOf(list.get(5)));
        matButton[0][2].setBackgroundColor(colors.get(5));
        results[0][2] = list.get(5);

        matButton[1][2] = (Button) findViewById(R.id.bottomcenter);
        matButton[1][2].setText(String.valueOf(list.get(6)));
        matButton[1][2].setBackgroundColor(colors.get(6));
        results[1][2] = list.get(6);

        matButton[2][2] = (Button) findViewById(R.id.bottomright);
        matButton[2][2].setText(String.valueOf(list.get(7)));
        matButton[2][2].setBackgroundColor(colors.get(7));
        results[2][2] = list.get(7);

        guiltyI = 1;
        guiltyJ = 1;
        matButton[guiltyI][guiltyJ].setBackgroundColor(Color.BLACK);
    }

    private int getBestPerf() {
        //getting preferences
        SharedPreferences prefs = getSharedPreferences("taquinBestPerf", Context.MODE_PRIVATE);
        return prefs.getInt("bestPerf", 0);
    }

    private void setBestPerf(int val) {
            //setting preferences
            SharedPreferences prefs = getSharedPreferences("taquinBestPerf", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("bestPerf", val);
            editor.commit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (detector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        System.out.println("velocityX : " + velocityX + " ; velocityY : " + velocityY);
        try {
            if (e1.getX() - e2.getX() > 60 && Math.abs(velocityX) > 200) //Left swipe
            {
                if (guiltyI != 2) //on est pas sur la paroie de droite
                {
                    nbMoves++;
                    matButton[guiltyI][guiltyJ].setText(matButton[guiltyI + 1][guiltyJ].getText());
                    matButton[guiltyI][guiltyJ].setBackgroundColor(((ColorDrawable) matButton[guiltyI + 1][guiltyJ].getBackground()).getColor());
                    results[guiltyI][guiltyJ] = results[guiltyI + 1][guiltyJ];
                    guiltyI++;
                    matButton[guiltyI][guiltyJ].setBackgroundColor(Color.BLACK);
                }
            } else if (e2.getX() - e1.getX() > 60 && Math.abs(velocityX) > 200) //Right swipe
            {
                if (guiltyI != 0) //on est pas sur la paroie de gauche
                {
                    nbMoves++;
                    matButton[guiltyI][guiltyJ].setText(matButton[guiltyI - 1][guiltyJ].getText());
                    matButton[guiltyI][guiltyJ].setBackgroundColor(((ColorDrawable) matButton[guiltyI - 1][guiltyJ].getBackground()).getColor());
                    results[guiltyI][guiltyJ] = results[guiltyI - 1][guiltyJ];
                    guiltyI--;
                    matButton[guiltyI][guiltyJ].setBackgroundColor(Color.BLACK);
                }
            } else if (e1.getY() - e2.getY() > 60 && Math.abs(velocityY) > 200) //Swipe up
            {
                if (guiltyJ != 2) //on est pas sur la paroie du bas
                {
                    nbMoves++;
                    matButton[guiltyI][guiltyJ].setText(matButton[guiltyI][guiltyJ + 1].getText());
                    matButton[guiltyI][guiltyJ].setBackgroundColor(((ColorDrawable) matButton[guiltyI][guiltyJ + 1].getBackground()).getColor());
                    results[guiltyI][guiltyJ] = results[guiltyI][guiltyJ + 1];
                    guiltyJ++;
                    matButton[guiltyI][guiltyJ].setBackgroundColor(Color.BLACK);

                }
            } else if (e2.getY() - e1.getY() > 60 && Math.abs(velocityY) > 200) //Swipe down
            {
                if (guiltyJ != 0) //on est pas sur la paroie du haut
                {
                    nbMoves++;
                    matButton[guiltyI][guiltyJ].setText(matButton[guiltyI][guiltyJ - 1].getText());
                    matButton[guiltyI][guiltyJ].setBackgroundColor(((ColorDrawable) matButton[guiltyI][guiltyJ - 1].getBackground()).getColor());
                    results[guiltyI][guiltyJ] = results[guiltyI][guiltyJ - 1];
                    guiltyJ--;
                    matButton[guiltyI][guiltyJ].setBackgroundColor(Color.BLACK);
                }
            }
            tv.setText("Moves : " + nbMoves);
            checkWin();

        } catch (Exception e) {
            Log.e("MainActivity", "Error on gesture");
        }
        return true;
    }

    private boolean checkPossible() {

        int inv = 0;
        for(int i = 0;i < list.size();i++){
            for(int j = i+1;j<list.size();j++){
                if(list.get(j)>list.get(i)){
                    inv++;
                }
            }
        }

        if(inv%2 == 1){
            return false;
        }else{
            return true;
        }
    }


    public void checkWin() {
        if (results[0][0] == 1 && results[1][0] == 2 && results[2][0] == 3 && results[0][1] == 4 &&
                results[1][1] == 5 && results[2][1] == 6 && results[0][2] == 7 && results[1][2] == 8) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            if (nbMoves < getBestPerf()) {
                builder1.setMessage("The Best Performance !");
                setBestPerf(nbMoves);
            } else {
                builder1.setMessage("Congratulations !");
            }
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "New Game",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            gameInitialization();
                        }
                    });

            builder1.setNegativeButton(
                    "Quit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

}
