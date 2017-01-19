package cz.muni.fi.pv256.movio2.uco374585.Service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;

import cz.muni.fi.pv256.movio2.uco374585.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Skylar on 1/19/2017.
 */

public class InternetConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_announcement_black_24dp)
                        .setContentTitle("No internet connection")
                        .setContentText("Please check your internet connection");
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (isConnected(context)) {
            Intent mServiceIntent = new Intent(context, TmdbPullService.class);
            mServiceIntent.putStringArrayListExtra("categories",
                    new ArrayList<>(Arrays.asList("category1", "category2", "category3")));
            context.startService(mServiceIntent);
            mNotifyMgr.cancel(NotificationConstants.noConnectionNotifId);
        } else {
            mNotifyMgr.notify(NotificationConstants.noConnectionNotifId, mBuilder.build());
        }
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }
}
