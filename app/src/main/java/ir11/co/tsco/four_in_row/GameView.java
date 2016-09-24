package ir11.co.tsco.four_in_row;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ir11.co.tsco.four_in_row.framework.Assets;
import ir11.co.tsco.four_in_row.framework.Painter;

/**
 * Created by Farhad on 4/3/2016.
 */
public class GameView extends SurfaceView implements Runnable, View.OnTouchListener,OnPlayed {



    private boolean boardHasWinner = false;
    private byte allMovesCount = 0;
    private  boolean dropSoundPlayed = false;
    private  OnPlayed mListener;
    //Graphic Details
    private byte firstMarginX = 60;
    private byte firstMarginY = 90;
    private byte marginX = 102;
    private byte marginY = 110;
    private byte marginDesignLeft = 47;

    private byte movingSleepDuration = 2;//2
    private int staticSleepDuration = 300;
    private int showArrowSleepDuration = 5;//5
    private int showEndingSleepDuration = 5;//5
    public static byte movingIncreamentPixel = 16;
    private  byte hitHeight = 15;

    private byte hitTime = 0;
    private boolean directionDownward = true; //true = downward, false = upward



    private Point to,from;
    private byte targetIndex_z;
    private boolean ismoving = false;
    private Rect gameImageSrc;
    private Rect gameImageDst;
    private Bitmap gameImage;
    private Painter painter;
    private Canvas gameCanvas;
    private Thread gameThread;
    private volatile boolean running = false;
    private  Activity ac;

    /*
     {0,0,1,0,0,0,0,},
     {0,0,2,2,0,0,0,},
     {0,2,2,1,0,0,0,},
     {0,1,2,1,0,0,0,},
     {0,2,1,2,0,0,0,},
     {0,1,2,1,1,0,0,},
     */




    public GameView(Context context, int gameWidth, int gameHeight, Activity act) {
        super(context);
        Assets.load();

        ac = act;
        gameImage = Bitmap.createBitmap(gameWidth, gameHeight, Bitmap.Config.ARGB_4444);
        //gameImage.setDensity(Bitmap.DENSITY_NONE);
        //The Rect gameImageSrc will be used to specify which region of the gameImage should be drawn to the screen.
        gameImageSrc = new Rect(0, 0, gameImage.getWidth(), gameImage.getHeight());
        //The Rect gameImageDst will be used to specify how the gameImage should be scaled when drawn to the screen.
        gameImageDst = new Rect();
        gameCanvas = new Canvas(gameImage);
        painter = new Painter(gameCanvas);
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                setOnTouchListener(GameView.this);
                initGame();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                pauseGame();
            }
        });
    }

    public GameView(Context context) {
        super(context);
    }

    @Override
    public void run()
    {

        long updateDurationMillis = 0;
        long sleepDurationMillis = 0;
        while (running ) {

            long beforeUpdateRender = System.nanoTime();
            long deltaMillis = sleepDurationMillis + updateDurationMillis;


            updateAndRender(deltaMillis);
            updateDurationMillis = (System.nanoTime() - beforeUpdateRender) / 1000000L;
            sleepDurationMillis = Math.max(2, 30 - updateDurationMillis);

            int restTime;
            if(ismoving) restTime = movingSleepDuration;
            else if(showCursor)restTime = showArrowSleepDuration;
            else if(showending)restTime = showEndingSleepDuration;
            else {restTime = staticSleepDuration; System.gc();}
            try {

                Thread.sleep(restTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void initGame() {

        /*for(int row = 0;row < 6;row++) {
            for(int col = 0;col < 7;col++) {
                MainActivity.board[row][col] = MainActivity.pre[row][col];
            }
        }*/
        running = true;
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }


    private void updateAndRender(long delta) {


        float x = delta / 1000f;// x is the time has taken since the previous iteration of update() in second

        painter.setColor(Color.rgb(208, 244, 247));
        painter.fillRect(0, 0, MainActivity.GAME_WIDTH, MainActivity.GAME_HEIGHT);

        renderBeads();
        painter.drawImage(Assets.board, 0, 0);
        if(showCursor)
            renderHereArrow();
        if(showending)  renderending();
        renderGameImage();

    }

    int step = 4;
    boolean displayonce = false;

    private void renderending()
    {


        int xx = 4;int yy = 3;
        int temp = 0;
        if (endingImageIndex >= 0 && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_9, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_9, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_9, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_9, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_8, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_8, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_8, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_8, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_7, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_7, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_7, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_7, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_6, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_6, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_6, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_6, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_5, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_5, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_5, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_5, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_4, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_4, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_4, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_4, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_3, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_3, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_3, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_3, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_2, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_2, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_2, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_2, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp && endingImageIndex <= temp + step) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
        }
        temp = temp + step + 1;
        if (endingImageIndex >= temp ) {
            Point p1 = getXY(t1);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t2);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t3);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            p1 = getXY(t4);
            painter.drawImage(Assets.decor_1, p1.x - xx, p1.y - yy);
            showEndingSleepDuration = staticSleepDuration;

            if(displayonce == false)
            {
                displayonce = true;
                if(!MainActivity.redTurn) {

                    MainActivity.ac.runOnUiThread(new Runnable() {
                        public void run() {

                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.ac).create();
                            // alertDialog.setTitle(getResources().getString(R.string.you_loose));
                            alertDialog.setMessage(getResources().getString(R.string.yellowwin));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            //restart();
                                        }
                                    });
                            alertDialog.show();
                            //App.Show("Yellow Win!");
                            boardHasWinner = true;
                        }
                    });
                }
                else
                {
                    MainActivity.ac.runOnUiThread(new Runnable() {
                        public void run() {
                            //App.Show("Red Win!");
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.ac).create();
                            // alertDialog.setTitle(getResources().getString(R.string.you_loose));
                            alertDialog.setMessage(getResources().getString(R.string.redWin));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            //restart();
                                        }
                                    });
                            alertDialog.show();
                            boardHasWinner = true;
                        }
                    });
                }
            }
        }
        endingImageIndex++;
    }
    private void renderHereArrow()
    {
        int x = 72;
        int yy = 30;
        int xx= hereCol * marginX + x;
        painter.drawImage(Assets.here, xx, yy);
    }
    boolean firstHit = true;

    boolean showending = false;
    byte endingImageIndex =0;
    public static byte t1,t2,t3,t4;


    private void renderBeads()
    {
        for(byte i=0;i<6;i++)
            for(byte j=0;j<7;j++)
            {
                byte bead = MainActivity.board[i][j];
                if(bead == 0)continue;
                Point xy = getXY((byte) (i*7 + j));
                if(bead == 1)
                    painter.drawImage(Assets.red, xy.x, xy.y);
                else
                    painter.drawImage(Assets.yellow, xy.x, xy.y);
            }

        if(ismoving)
        {

            // Log.i("ffff","ismoving");
            if(MainActivity.redTurn)
                painter.drawImage(Assets.red, from.x, from.y);
            else
                painter.drawImage(Assets.yellow, from.x, from.y);


            if(directionDownward) {
                if (from.y >= to.y) {
                    if (hitTime == 0) {
                        ismoving = false;
                        allMovesCount++;
                        if(allMovesCount == 42)
                        {
                            MainActivity.ac.runOnUiThread(new Runnable() {
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.ac).create();
                                    // alertDialog.setTitle(getResources().getString(R.string.you_loose));
                                    alertDialog.setMessage(getResources().getString(R.string.tied));
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    restart();
                                                }
                                            });
                                    alertDialog.show();
                                    boardHasWinner = true;
                                }
                            });

                        }

                        byte color = 1; //1 = red. 2 = yellow
                        if (!MainActivity.redTurn) color = 2;
                        MainActivity.board[targetIndex_z /7][targetIndex_z %7] = color;

                        if(Helper.checkIfWin(targetIndex_z,color,MainActivity.board,true))
                        {

                            endingImageIndex = 0;
                            showending = true;
                            Log.i("eeee","t1:" + t1);
                            Log.i("eeee","t2:" + t2);
                            Log.i("eeee","t3:" + t3);
                            Log.i("eeee","t4:" + t4);
                            if(!MainActivity.redTurn) {

                                MainActivity.ac.runOnUiThread(new Runnable() {
                                    public void run() {

                                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.ac).create();
                                        // alertDialog.setTitle(getResources().getString(R.string.you_loose));
                                        alertDialog.setMessage(getResources().getString(R.string.yellowwin));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                        //restart();
                                                    }
                                                });
                                        //alertDialog.show();
                                        //App.Show("Yellow Win!");
                                        boardHasWinner = true;
                                    }
                                });
                            }
                            else
                            {
                                MainActivity.ac.runOnUiThread(new Runnable() {
                                    public void run() {
                                        //App.Show("Red Win!");
                                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.ac).create();
                                        // alertDialog.setTitle(getResources().getString(R.string.you_loose));
                                        alertDialog.setMessage(getResources().getString(R.string.redWin));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        //restart();
                                                    }
                                                });
                                        //alertDialog.show();
                                        boardHasWinner = true;
                                    }
                                });
                            }

                        }
                        else
                        {
                            MainActivity.redTurn = !MainActivity.redTurn;
                            if(mListener!=null)
                                mListener.onEvent(false, !MainActivity.redTurn);

                            if(MainActivity.VsCPU && !MainActivity.redTurn)
                            {
                                byte z_col = (byte) getNextMove();
                                startMovingBead(z_col);

                            }else showCursor = false;
                        }
                    } else {
                        if(!dropSoundPlayed) {
                            if(hitTime - 1 >0)
                                Assets.playSound(Assets.moveID);
                            //dropSoundPlayed = true;
                        }
                        hitTime--;
                        firstHit = false;
                        directionDownward = false;
                    }
                }
            }
            else
            {
                if (from.y <=  (to.y - (hitTime * hitHeight)) ) {
                    directionDownward = true;
                }
            }


            if(directionDownward) {
                if(firstHit)
                    from.y += movingIncreamentPixel;
                else
                    from.y = from.y + ( movingIncreamentPixel - (movingIncreamentPixel/4));
                if (from.y > to.y) from.y = to.y;
            }
            else
            {
                from.y = from.y - ( movingIncreamentPixel - (movingIncreamentPixel/4));
                if (from.y < (to.y - (hitTime * hitHeight))) from.y = to.y - (hitTime * hitHeight);
            }
        }
    }
    private int [][] bestMove = {
            {-1,-1}
    };
    int treeDepth = 6;
    public int maxMove(int depth,int alpha,int beta) {
        int max = -500025;
        int m = Helper.testForWinner(MainActivity.board);// اگه کسی برنده نمیشه صفر برگردون یا منهای پونصدهزار یا مثبت پونصد هزار
        if(m != 0) {//Base Case 1
            return m;
        }
        if(depth >= treeDepth || Helper.isDraw(MainActivity.board)) { //Base Case 2
            return Helper.analysis(MainActivity.board);
        }

        int [][] lMoves = Helper.findAllLegalMoves(MainActivity.board);

        for(int move = 0;move < 7;move++) {
            if(lMoves[move][0] == -1 || lMoves[move][1] == -1) {
                continue;
            } else {
                MainActivity.board[lMoves[move][0]][lMoves[move][1]] = 2;
                int temp = minMove(depth + 1,alpha,beta);
                MainActivity.board[lMoves[move][0]][lMoves[move][1]] = 0;
                if(temp > max) {
                    max = temp;
                    if(depth == 0) {
                        bestMove[0][0] = lMoves[move][0];
                        bestMove[0][1] = lMoves[move][1];
                    }
                }
                if(temp > alpha) {
                    alpha = temp;
                }
                if(alpha >= beta) {
                    return alpha;
                }
            }
        }
        return max;
    }

    public int minMove(int depth,int alpha,int beta) {
        int min = 500025;
        int m = Helper.testForWinner(MainActivity.board);
        if(m != 0) {
            return m;
        }
        if(depth >= treeDepth || Helper.isDraw(MainActivity.board)) {
            return Helper.analysis(MainActivity.board);
        }

        int [][] lMoves = Helper.findAllLegalMoves(MainActivity.board);

        for(int move = 0;move < 7;move++) {
            if(lMoves[move][0] == -1 || lMoves[move][1] == -1) {
                continue;
            } else {
                MainActivity.board[lMoves[move][0]][lMoves[move][1]] = 1;
                int temp = maxMove(depth + 1,alpha,beta);
                MainActivity.board[lMoves[move][0]][lMoves[move][1]] = 0;
                if(temp < min) {
                    min = temp;
                    if(depth == 0) {
                        bestMove[0][0] = lMoves[move][0];
                        bestMove[0][1] = lMoves[move][1];
                    }
                }
                if(temp < beta) {
                    beta = temp;
                }
                if(alpha >= beta) {
                    return beta;
                }
            }
        }
        return min;
    }
    public int minimax(int depth,int alpha,int beta) {
        return (maxMove(depth,alpha,beta));
    }
    private int getNextMove()
    {
        int move = 0;

        if (MainActivity.treeAlgorithm) {

            byte index = Helper.checkIfCanWin((byte) 2, MainActivity.board);
            if (index != -1)
                move =   Helper.Column_Of(index);
            else {
                final int i = minimax(0, -1000000, 1000000);
                Log.i("rrrr", "i = " + i);
                move = bestMove[0][1];
            }
        } else
            move =  getNextMoveByStaticCalculation();

        int a=-1,b=-1,c=-1;
        if(move>6)
        {
            for(byte i=0;i<7;i++)
            {
                if(MainActivity.board[0][i] != 0) continue;
                a = i;
                if(Helper.isBlackList(i,MainActivity.board))continue;
                b = i;
                if( Helper.isBuggable(i,MainActivity.board, (byte) 2) >=0 ) continue;
                c = i;
            }
            if(c>=0)move = c;
            else  if(b>=0)move = b;
            else  move = a;

        }
        return move;
    }
    private byte getNextMoveByStaticCalculation()
    {
        byte ret = 0;

        //if cpu can win
        byte index = Helper.checkIfCanWin((byte) 2, MainActivity.board);
        if (index != -1)
            return (byte) Helper.Column_Of(index);

        //if human can win then prevent
        index = Helper.checkIfCanWin((byte) 1, MainActivity.board);
        if (index != -1)
            return (byte) Helper.Column_Of(index);
        Random rnd = new Random();


        //fix bug
        byte res = Helper.isBuggy(MainActivity.board, (byte) 2);
        if(res != -1) return  res;


        //find a randon, non blacklist move
        boolean doAgain = false, isBuggy = false;


        List<Byte> x = new ArrayList<>();
        for(byte i=0;i<7;i++)
        {
            if(MainActivity.board[0][i] != 0) continue;
            ret = i;
            if(Helper.isBlackList(i,MainActivity.board))continue;

            if( Helper.isBuggable(i,MainActivity.board,(byte) 2) >=0) continue;

            x.add(i);
        }

        Random rr = new Random();
        if(x.size()>0)
            return (byte) x.get(rr.nextInt(x.size()));
        else return ret;
    }

    int x = 0;
    private void renderGameImage() {
        x++;
        Canvas screen = getHolder().lockCanvas();
        if (screen != null) {
            screen.getClipBounds(gameImageDst);
            screen.drawBitmap(gameImage, gameImageSrc, gameImageDst, null);
            getHolder().unlockCanvasAndPost(screen);
        }
    }
    private void pauseGame() {
        running = false;
        while (gameThread.isAlive()) {
            try {
                gameThread.join();
                break;
            } catch (InterruptedException e) {
            }
        }
    }



    private boolean startMovingBead(byte col_zBased)
    {


        byte index = col_zBased;
        //Calculating target x,y;
        if(MainActivity.board[0][index]!=0) return  false;

        index = (byte) (col_zBased+7);
        for(byte i=1;i<=5;i++)
        {
            if(MainActivity.board[index/7][index%7] != 0)
                break;
            else index +=7;
        }
        index -=7;
        targetIndex_z = index;

        to = getXY(index);
        from = new Point(to.x,-60);

        hitTime = 0;
        if(index>=7)hitTime = 2;
        if(index>=14)hitTime = 3;
        if(index>=21)hitTime = 3;
        if(index>=28)hitTime = 4;
        if(index>=35)hitTime = 5;
        directionDownward = true;
        dropSoundPlayed = false;
        firstHit = true;
        ismoving = true;
        return  true;
    }
    private Point getXY(byte index)
    {
        //index--;//index is now zero based
        byte row = (byte) (index / 7);
        byte col = (byte) (index % 7);
        int x,y;
        if(col == 0) x = firstMarginX;else x = (col * marginX)+firstMarginX;
        if(row == 0) y = firstMarginY;else y = (row * marginY)+firstMarginY;


        if(row == 0) y+=4;
        if(row == 0 && col == 3 ) x-=2;
        if(row == 0 && col == 1 ) x+=2;
        if(row == 0 && col == 4 ) x-=2;
        if(row == 0 && col == 5 ) x-=1;

        if(row == 1) y+=2;
        if(row == 1 && col == 4 ) x-=3;
        if(row == 1 && col == 3 ) x-=2;
        if(row == 1 && col == 5 ) x-=1;
        if(row == 1 && col == 5 ) y+=1;
        if(row == 1 && col == 6 ) y+=1;

        if(row == 2 && col == 6 ) y+=2;
        if(row == 2 && col == 6 ) x+=1;
        if(row == 2 && col == 0 ) y+=2;
        if(row == 2 && col == 1 ) y+=2;
        if(row == 2 && col == 1 ) x+=2;
        if(row == 2 && col == 3 ) {x-=2;y+=2;}
        if(row == 2 && col == 4 ) {x-=2;y+=2;}

        if(row == 3 && col == 3 ) x-=2;
        if(row == 3 && col == 4 ) x-=2;
        if(row == 3 && col == 1 ) x+=2;

        if(row == 4 && col == 3 ) x-=2;
        if(row == 4 && col == 4 ) x-=2;

        if(row == 5 && col == 3 ) x-=2;
        if(row == 5 && col == 4 ) x-=2;



        Point target = new Point(x,y);
        return  target;
    }
    public void restart()
    {
        for(byte i=0;i<6;i++)
            for(byte j=0;j<7;j++)
                MainActivity.board[i][j] = 0;
        MainActivity.redTurn = true;
        dropSoundPlayed = false;
        allMovesCount = 0;
        boardHasWinner = false;
        MainActivity.treeAlgorithm = true;
        showending = false;
        endingImageIndex = 0;
        showEndingSleepDuration = 5;
        displayonce = false;
        System.gc();
    }

    boolean showCursor = false;
    byte hereCol = 0;
    boolean isMyTurn = true;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(boardHasWinner)return  false;
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //Log.i("aaaa",event.getAction() + "     up="+MotionEvent.ACTION_UP+ " down="+MotionEvent.ACTION_DOWN+ " move="+MotionEvent.ACTION_MOVE);
            if (ismoving) return false;
            int scaledX = (int) ((event.getX() / v.getWidth()) * MainActivity.GAME_WIDTH);
            //int scaledY = (int) ((event.getY() / v.getHeight()) * MainActivity.GAME_HEIGHT);


            if (scaledX < marginDesignLeft) return false;
            scaledX = scaledX - marginDesignLeft;
            byte col = (byte) (scaledX / marginX);

            if(col>6)return false;
            if (isMyTurn) {


                boolean result = startMovingBead(col);
                if (result == false) {
                    App.Show("This column is full!, select another one");
                    return false;
                }
                showCursor = false;
                if (mListener != null)
                    mListener.onEvent(true, !MainActivity.redTurn);
                return true;
            } else
                return false;
        }else if(event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {

            if (ismoving) return false;
            showCursor = true;
            int scaledX = (int) ((event.getX() / v.getWidth()) * MainActivity.GAME_WIDTH);
            //int scaledY = (int) ((event.getY() / v.getHeight()) * MainActivity.GAME_HEIGHT);


            if (scaledX < marginDesignLeft) { showCursor = false;return false;}
            if (scaledX > marginDesignLeft + (7 * marginX)) { showCursor = false;return false;}
            scaledX = scaledX - marginDesignLeft;
            hereCol = (byte) (scaledX / marginX);
            //Log.i("aaaa","coll = " + j);

            return true;
        }
        else  return   true;
    }
    public void setOnPlayed(OnPlayed eventListener) {
        mListener = eventListener;
    }
    @Override
    public void onEvent(boolean firstTouch, boolean color) {

    }





}
