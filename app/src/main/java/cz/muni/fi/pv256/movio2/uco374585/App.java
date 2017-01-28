package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

/**
 * Created by xvilkol1 on 20.9.16.
 */
public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        mContext = this;
        super.onCreate();

        if (BuildConfig.DEBUG) {
            initStrictMode();
        }
    }

    public static Context getContext(){
        return mContext;
    }

    private void initStrictMode() {
        StrictMode.ThreadPolicy.Builder tpb = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tpb.penaltyFlashScreen();
        }
        StrictMode.setThreadPolicy(tpb.build());

        StrictMode.VmPolicy.Builder vmpb = new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            vmpb.detectLeakedClosableObjects();
        }
        StrictMode.setVmPolicy(vmpb.build());
    }
}
