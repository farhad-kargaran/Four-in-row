package ir11.co.tsco.four_in_row.framework;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;
import java.io.InputStream;

import ir11.co.tsco.four_in_row.MainActivity;


/**
 * Created by Farhad on 4/4/2016.
 */
public class Assets {

    private static SoundPool soundPool;
    public static Bitmap board,red,yellow,here,decor_1,decor_2,decor_3,decor_4,decor_5,decor_6,decor_7,decor_8,decor_9;
    public static int moveID;

    public static void load() {
        here = loadBitmap("here.png", true);
        board = loadBitmap("boarddd.png", true);
        red = loadBitmap("red.png", false);
        yellow = loadBitmap("yellow.png", false);
        moveID = loadSound("drop.ogg");


        decor_1 = loadBitmap("decor_1.png", true);
        decor_2 = loadBitmap("decor_2.png", true);
        decor_3 = loadBitmap("decor_3.png", true);
        decor_4 = loadBitmap("decor_4.png", true);
        decor_5 = loadBitmap("decor_5.png", true);
        decor_6 = loadBitmap("decor_6.png", true);
        decor_7 = loadBitmap("decor_7.png", true);
        decor_8 = loadBitmap("decor_8.png", true);
        decor_9 = loadBitmap("decor_9.png", true);
    }

    private static Bitmap loadBitmap(String filename, boolean transparency) {

        InputStream inputStream = null;
        try {
            inputStream = MainActivity.assets.open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (transparency) {
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        } else {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
                new BitmapFactory.Options());
        return bitmap;
    }

    private static int loadSound(String filename) {
        int soundID = 0;
        if (soundPool == null) {
            soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            soundID = soundPool.load(MainActivity.assets.openFd(filename),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return soundID;
    }

    public static void playSound(int soundID) {
        soundPool.play(soundID, 1, 1, 1, 0, 1);
    }
}
