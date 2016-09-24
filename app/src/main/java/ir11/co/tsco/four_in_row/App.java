package ir11.co.tsco.four_in_row;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Farhad on 4/9/2016.
 */
public class App extends android.app.Application {

    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    public static void Show(String txt)
    {
        Toast.makeText(context,txt,Toast.LENGTH_SHORT).show();
    }
}
