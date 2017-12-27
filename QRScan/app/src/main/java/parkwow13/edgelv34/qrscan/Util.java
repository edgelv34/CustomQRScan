package parkwow13.edgelv34.qrscan;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by com on 2017-12-27.
 */

public class Util {

    public static int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static float getDensity(Context context, float value) {
        WindowManager manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.density * value;
    }
}
