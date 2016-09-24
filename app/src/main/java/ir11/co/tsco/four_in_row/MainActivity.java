package ir11.co.tsco.four_in_row;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import ir11.co.tsco.four_in_row.framework.Assets;
import ir11.co.tsco.four_in_row.framework.Utils;

public class MainActivity extends AppCompatActivity {

    public static boolean VsCPU = true;
    private boolean timerOn = false;
    private CountDownTimer cdTimer;
    public static int GAME_WIDTH = 800;
    public static int GAME_HEIGHT = 800;
    private int leftRightMarginForSurgaceview = 20;
    public static Activity ac;
    public static GameView sGame;
    public static AssetManager assets;
    private LinearLayout lnGame;
    private CheckBox chk;
    private RadioButton rd_human,rd_cpu, rd_tree,rd_static;
    public static boolean treeAlgorithm = true;

    private TextView tv,tvTimer;
    private ImageView iv;

    public  boolean redTurnn = true;
    public static boolean redTurn = true;
    public static byte[][] pre = new byte[][]{
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
    };
    public static  byte[][] board = new byte[][]{
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
            {0,0,0,0,0,0,0,},
    };

    public void test(View v)
    {
        App.Show("" + Helper.testForWinner(board));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();



        sGame.setOnPlayed(new OnPlayed() {
            @Override
            public void onEvent(final boolean firstTouch, final boolean color) {
                redTurnn = color;
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(!firstTouch) {
                            if (color) {
                                // App.Show("Red Played!");
                                tv.setText(getResources().getString(R.string.yellowturn));
                                iv.setImageBitmap(Assets.yellow);
                                if(timerOn)startTimer();

                            } else {
                                tv.setText(getResources().getString(R.string.redturn));
                                iv.setImageBitmap(Assets.red);
                                if(timerOn)startTimer();
                                //App.Show("Yellow Played!");
                            }
                        }
                        else
                        {
                           hideTimer();
                        }
                    }
                });
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

   private void initial()
   {
       tv = (TextView)findViewById(R.id.tv);
       tvTimer = (TextView)findViewById(R.id.tvTimer);
       iv = (ImageView)findViewById(R.id.iv);
       chk = (CheckBox)findViewById(R.id.chk);
       rd_human = (RadioButton)findViewById(R.id.rd_human);
       rd_cpu = (RadioButton)findViewById(R.id.rd_cpu);
       rd_tree = (RadioButton)findViewById(R.id.rd_tree);
       rd_static = (RadioButton)findViewById(R.id.rd_static);
       rd_tree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               treeAlgorithm = b;
           }
       });
       rd_static.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               treeAlgorithm = !b;
           }
       });
       rd_cpu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               VsCPU = b;
           }
       });
       rd_human.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               VsCPU = !b;
           }
       });
       chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               timerOn = b;
           }
       });
       assets = getAssets();
       iv.setImageBitmap(Assets.red);
       ac = this;
       Point device_screen = Utils.get_Device_Size_in_pixel();
       int margin = Utils.dpToPx(leftRightMarginForSurgaceview);
       int device_width = device_screen.x;
       int newWidth = device_width - (margin * 2);
       int newHeight = (newWidth * GAME_HEIGHT) / GAME_WIDTH;
       lnGame = (LinearLayout)findViewById(R.id.lnGame);
       lnGame.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, newHeight));
       sGame = new GameView(this, GAME_WIDTH, GAME_HEIGHT,this);
       lnGame.addView(sGame);
       iv.setImageBitmap(Assets.red);


       int currentapiVersion = android.os.Build.VERSION.SDK_INT;
       //App.Show("" + currentapiVersion);
       if(currentapiVersion<=17)
           GameView.movingIncreamentPixel = 35;
       else GameView.movingIncreamentPixel = 20;
   }

    private void hideTimer()
    {
        tvTimer.setVisibility(View.INVISIBLE);
        if(cdTimer!=null)
          cdTimer.cancel();
    }
    private void startTimer()
    {
        tvTimer.setVisibility(View.VISIBLE);
        if(cdTimer!=null)cdTimer.cancel();
        cdTimer = new CountDownTimer(10000,500)
        {

            @Override
            public void onTick(long l) {

                if(timerOn == false) {
                    tvTimer.setVisibility(View.INVISIBLE);
                    cdTimer.cancel();
                    return;
                }else
                    tvTimer.setVisibility(View.VISIBLE);
                Log.i("1010","" + l);
                if(l>=1000) {
                    long x = l/1000;
                    x++;
                    tvTimer.setText("" + x);
                }
                else tvTimer.setText("1");
            }

            @Override
            public void onFinish() {

                //tvTimer.setText("0");
                if(timerOn == false) {
                    tvTimer.setVisibility(View.INVISIBLE);
                    cdTimer.cancel();
                    return;
                }else
                tvTimer.setVisibility(View.VISIBLE);
                if(redTurnn) {

                   // App.Show(getResources().getString(R.string.timeup_red));
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle(getResources().getString(R.string.you_loose));
                    alertDialog.setMessage(getResources().getString(R.string.timeup_yellow));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    restart(tv);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    //App.Show(getResources().getString(R.string.timeup_yellow));
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle(getResources().getString(R.string.you_loose));
                    alertDialog.setMessage(getResources().getString(R.string.timeup_red));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    restart(tv);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }



            }
        }.start();
    }
    public void restart(View v)
    {

        System.gc();
        sGame.restart();
        iv.setImageBitmap(Assets.red);
        tv.setText(getResources().getString(R.string.redturn));
        tvTimer.setVisibility(View.INVISIBLE);
        if(cdTimer!=null)
        cdTimer.cancel();
        chk.setChecked(false);
        rd_static.setChecked(false);
        rd_tree.setChecked(true);
        rd_cpu.setChecked(true);
        rd_human.setChecked(false);
        VsCPU = true;
        treeAlgorithm = true;
        timerOn = false;
        //Assets.playSound(Assets.moveID);

    }
}
