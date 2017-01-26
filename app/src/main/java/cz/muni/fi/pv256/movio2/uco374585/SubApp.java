package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Application;
import android.content.Context;

/**
 * Created by Skylar on 1/26/2017.
 */

public class SubApp extends Application{
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
