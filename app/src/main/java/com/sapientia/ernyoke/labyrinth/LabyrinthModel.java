package com.sapientia.ernyoke.labyrinth;

import android.os.Handler;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LabyrinthModel {
    private LabyrinthView LV;
    private int [][]table;
    private int ballcol;
    private int ballrow;
    private int cols;
    private int rows;
    public int a,b;


    public LabyrinthModel() {
        table = null;
    }

    public int getBallcol() {
        return this.ballcol;
    }

    public int getBallrow() {
        return this.ballrow;
    }

    public int getCols() {
        return this.cols;
    }

    public int getRows() {
        return this.rows;
    }

    public int getElement(int x, int y) {
        return table[x][y];
    }

    public void initLabyrinth(String[] lab) {
        rows = lab.length;
        cols = lab[0].length();
        if(table == null) {
            table = new int[rows][cols];
        }
        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < cols; ++j) {
                if(lab[i].charAt(j) == '0') {
                    table[i][j] = 0;
                }
                if(lab[i].charAt(j) == '1') {
                    table[i][j] = 1;
                }
                if(lab[i].charAt(j) == '2') {
                    table[i][j] = 2;
                }
                if(lab[i].charAt(j) == '3') {
                    table[i][j] = 3;
                }
            }
        }

    }

        public void left() {
        if(this.ballcol > 0 && (table[ballrow][ballcol - 1] == 0||table[ballrow][ballcol - 1] == 2 ||table[ballrow][ballcol - 1] == 3)) {
            this.ballcol--;

        }
        if (this.ballcol > 0 &&table[ballrow][ballcol] == 3 ) {
            final Handler handler = new Handler();
            final Timer timer = new Timer(false);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final Random random = new Random();
                            do { a = (random.nextInt(11) + 1);
                                b = (random.nextInt(9) + 1);}
                            while (table[a][b] != 0);
                            if (table[a][b]==0){
                                ballcol = b;
                                ballrow = a;
                                timer.cancel();
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask, 170,60000);
        }
    }

    public void right() {
        if(this.ballcol < cols - 1 && (table[ballrow][ballcol + 1] == 0 || table[ballrow][ballcol + 1] == 2||table[ballrow][ballcol + 1] == 3)) {
            this.ballcol++;
        }
        if (this.ballcol < cols-1 &&table[ballrow][ballcol] == 3) {
            final Handler handler = new Handler();
            final Timer timer = new Timer(false);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final Random random = new Random();
                            do { a = (random.nextInt(11) + 1);
                                b = (random.nextInt(9) + 1);}
                            while (table[a][b] != 0);
                            if (table[a][b]==0){
                                ballcol = b;
                                ballrow = a;
                                timer.cancel();
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask, 170,60000);


        }
    }

    public void up() {
        if(this.ballrow > 0 && (table[ballrow - 1][ballcol] == 0|| table[ballrow - 1][ballcol] == 2 ||table[ballrow - 1][ballcol] == 3 )) {
            this.ballrow--;
        }
        if (this.ballrow > 0 &&table[ballrow][ballcol] == 3 ) {
            final Handler handler = new Handler();
            final Timer timer = new Timer(false);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final Random random = new Random();
                            do { a = (random.nextInt(11) + 1);
                                b = (random.nextInt(9) + 1);}
                            while (table[a][b] != 0);
                            if (table[a][b]==0){
                                ballcol = b;
                                ballrow = a;
                                timer.cancel();
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask, 170,60000);
        }
    }

    public void down() {
        if(this.ballrow < rows - 1 && (table[ballrow + 1][ballcol] == 0||table[ballrow + 1][ballcol] == 2||table[ballrow + 1][ballcol] == 3)) {
            this.ballrow++;
        }
        if (this.ballrow < rows - 1 &&table[ballrow][ballcol] == 3) {
            final Handler handler = new Handler();
            final Timer timer = new Timer(false);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final Random random = new Random();
                            do { a = (random.nextInt(11) + 1);
                                b = (random.nextInt(9) + 1);}
                            while (table[a][b] != 0);
                            if (table[a][b]==0){
                                ballcol = b;
                                ballrow = a;
                                timer.cancel();
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask, 170,60000);
        }
    }

    public boolean isWinner() {
        if(ballcol == cols - 1 && ballrow == rows - 1) {
            return true;
        }
        return false;
    }







}
