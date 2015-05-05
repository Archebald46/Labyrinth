package com.sapientia.ernyoke.labyrinth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;


public class LabyrinthView extends View {
    private LabyrinthModel model;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int height;
    private int width;
    private int blocks_in_a_col, blocks_in_a_row;

    private Rect[][] Rectangles;

    private Canvas canvas;

    private boolean init = false;

    private int labColor = Color.parseColor("#8B4513");
    private int ballColor = Color.parseColor("#A52A2A");
    private int backGroundColor = Color.parseColor("#FFEBCD");

    private int coinColor = Color.parseColor("#FFFF00");
    private int deathColor = Color.parseColor("#000000");

    public LabyrinthView(Context context, LabyrinthModel model) {
        super(context);
        this.model = model;
        blocks_in_a_col = model.getRows();
        blocks_in_a_row = model.getCols();
    }

    protected void onDraw(Canvas c) {
        super.onDraw(c);
        canvas = c;

        if (!init) {
            initialize();
            init = true;
        } else {
            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
            Bitmap mBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.cherep);
            paint.setColor(labColor);
            canvas.drawRect(0, 0, width, height, paint);
            paint.setColor(backGroundColor);
            canvas.drawRect(4, 4, width-4, height-4, paint);
            paint.setColor(labColor);
            paint2.setColor(backGroundColor);
            paint3.setColor(backGroundColor);
            for (int i = 0; i < blocks_in_a_col; i++)
                for (int j = 0; j < blocks_in_a_row; j++)
                    if (model.getElement(i,j) == 1)
                        canvas.drawRect(Rectangles[i][j], paint); else
                    if (model.getElement(i,j) == 2)
                        canvas.drawBitmap(mBitmap,null,Rectangles[i][j],paint2); else
                    if (model.getElement(i,j) == 3)
                       canvas.drawBitmap(mBitmap1,null,Rectangles[i][j],paint3);

            paint.setColor(ballColor);
            canvas.drawRect(
                    Rectangles[blocks_in_a_col - 1][blocks_in_a_row - 1], paint);
            paint.setColor(backGroundColor);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(
                    "EXIT",
                    (Rectangles[blocks_in_a_col - 1][blocks_in_a_row - 1].left + Rectangles[blocks_in_a_col - 1][blocks_in_a_row - 1].right) / 2,
                    (Rectangles[blocks_in_a_col - 1][blocks_in_a_row - 1].top + Rectangles[blocks_in_a_col - 1][blocks_in_a_row - 1].bottom) / 2 + 5,
                    paint);

            drawMyCircle(model.getBallrow(), model.getBallcol(), ballColor);
        }

    }

    private void initialize() {
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
        Bitmap mBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.cherep);
        paint.setColor( labColor );
        height = this.getHeight();// canvas.getHeight();
        width = this.getWidth();// canvas.getWidth();
        canvas.drawRect(0, 0, width, height, paint);
        paint.setColor( backGroundColor );
        canvas.drawRect(4, 4, width - 4, height - 4, paint);
        paint.setColor( labColor );
        paint2.setColor(backGroundColor);
        paint3.setColor(backGroundColor);

        Rectangles = new Rect[ blocks_in_a_col][ blocks_in_a_row ];

        int last_bottom = 4, new_bottom, last_right, new_right;

        for (int i = 0; i <blocks_in_a_col; i++) {
            last_right = 4;
            new_bottom = (height - 5) * (i + 1) / blocks_in_a_col;
            for (int j = 0; j < blocks_in_a_row; j++) {
                new_right = (width - 5) * (j + 1) / blocks_in_a_row;
                Rectangles[i][j] = new Rect();
                Rectangles[i][j].left = last_right + 1;
                Rectangles[i][j].top = last_bottom + 1;
                Rectangles[i][j].right = new_right;
                Rectangles[i][j].bottom = new_bottom;
                        if (model.getElement(i,j) == 1)
                            canvas.drawRect(Rectangles[i][j], paint); else
                        if (model.getElement(i,j) == 2)
                            canvas.drawBitmap(mBitmap,null,Rectangles[i][j],paint2); else
                        if (model.getElement(i,j) == 3)
                            canvas.drawBitmap(mBitmap1,null,Rectangles[i][j],paint3);
                last_right = new_right;
            }
            last_bottom = new_bottom;

        }

        paint.setColor(ballColor);
        canvas.drawRect(Rectangles[blocks_in_a_col - 1][blocks_in_a_row - 1],
                paint);
        paint.setColor(backGroundColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12);
        canvas.drawText(
                "EXIT",
                (Rectangles[blocks_in_a_col - 1][blocks_in_a_row - 1].left + Rectangles[blocks_in_a_col - 1][blocks_in_a_row - 1].right) / 2,
                (Rectangles[blocks_in_a_col - 1][blocks_in_a_row - 1].top + Rectangles[blocks_in_a_col - 1][blocks_in_a_row - 1].bottom) / 2 + 5,
                paint);
        drawMyCircle(model.getBallrow(), model.getBallcol(), ballColor);
    }


    public void drawMyCircle(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawCircle((Rectangles[x][y].left + Rectangles[x][y].right) / 2,
                (Rectangles[x][y].top + Rectangles[x][y].bottom) / 2,
                (Rectangles[x][y].right - Rectangles[x][y].left) / 2, paint);
    }






}
