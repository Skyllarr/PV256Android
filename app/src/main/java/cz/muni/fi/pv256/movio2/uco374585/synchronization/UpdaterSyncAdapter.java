package cz.muni.fi.pv256.movio2.uco374585.synchronization;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.R;
import cz.muni.fi.pv256.movio2.uco374585.api.MovieDTO;
import cz.muni.fi.pv256.movio2.uco374585.api.TmdbAPI;
import cz.muni.fi.pv256.movio2.uco374585.database.MovieManager;
import cz.muni.fi.pv256.movio2.uco374585.model.Movie;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static cz.muni.fi.pv256.movio2.uco374585.api.Query.API_KEY;
import static cz.muni.fi.pv256.movio2.uco374585.api.Query.DISCOVER_URL;

/**
 * Created by Skylar on 1/24/2017.
 */

public class UpdaterSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final int SYNC_INTERVAL = 60 * 60 * 24; //day // 60 * 5
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final String TAG = "UpdaterSyncAdapter";

    /**
     * Creates an {@link AbstractThreadedSyncAdapter}.
     *
     * @param context        the {@link Context} that this is running within.
     * @param autoInitialize if true then sync requests that have
     *                       {@link ContentResolver#SYNC_EXTRAS_INITIALIZE} set will be internally handled by
     *                       {@link AbstractThreadedSyncAdapter} by calling
     *                       {@link ContentResolver#setIsSyncable(Account, String, int)} with 1 if it
     */
    UpdaterSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    private static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(Bundle.EMPTY) //enter non null Bundle, otherwise on some phones it crashes sync
                    .build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    private static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        UpdaterSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    /**
     * Perform a sync for this account. SyncAdapter-specific parameters may
     * be specified in extras, which is guaranteed to not be null. Invocations
     * of this method are guaranteed to be serialized.
     *
     * @param account    the account that should be synced
     * @param extras     SyncAdapter-specific parameters
     * @param authority  the authority of this sync request
     * @param provider   a ContentProviderClient that points to the ContentProvider for this
     *                   authority
     * @param syncResult SyncAdapter-specific parameters
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "onPerformSync");
        Thread syncMoviesWithTmdb = new Thread() {
            @Override
            public void run() {
                MovieManager mMovieManager = new MovieManager(getContext());
                Retrofit rest = new Retrofit.Builder()
                        .baseUrl(DISCOVER_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                TmdbAPI tmdbAPI = rest.create(TmdbAPI.class);
                List<Movie> MoviesDb = mMovieManager.findMovies();
                boolean updated = false;
                for (Movie savedMovie : MoviesDb) {
                    final Call<MovieDTO> findMoviebyIdcall = tmdbAPI.findMovieById(savedMovie.getId(), API_KEY);
                    try {
                        Response<MovieDTO> findMoviebyIdResponse = findMoviebyIdcall.execute();
                        int statusCode = findMoviebyIdResponse.code();
                        if (statusCode == 200) {
                            MovieDTO serverMovie = findMoviebyIdResponse.body();
                            if (!areSynced(savedMovie, serverMovie.create())) {
                                mMovieManager.updateMovie(serverMovie.create());
                                updated = true;
                            }
                        } else {
                            Log.e(TAG, "Server did not respond properly");
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
                if (updated) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Movies have been updated.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Up to date.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        syncMoviesWithTmdb.start();
    }

    private static boolean compareStrings(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

    private static boolean areSynced(Movie savedMovie, Movie serverMovie) {
        return !(!compareStrings(savedMovie.getTitle(), serverMovie.getTitle()) ||
                !compareStrings(savedMovie.getReleaseDate(), serverMovie.getReleaseDate()) ||
                savedMovie.getVoteAverage() != serverMovie.getVoteAverage() ||
                !compareStrings(savedMovie.getDescription(), serverMovie.getDescription()));
    }
}
