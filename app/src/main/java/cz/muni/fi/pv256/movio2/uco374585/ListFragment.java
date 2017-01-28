package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Fragment;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.Data.MovieDataSingleton;
import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;
import cz.muni.fi.pv256.movio2.uco374585.Service.NotificationConstants;
import cz.muni.fi.pv256.movio2.uco374585.Service.TmdbPullService;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Skylar on 12/27/2016.
 */

public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private BroadcastReceiver receiver;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "ListFragment was attached to its context");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra(TmdbPullService.TMDB_MESSAGE);
                switch (message) {
                    case "FINISHED":
                        getFragmentManager()
                                .beginTransaction()
                                .detach(getFragmentManager().findFragmentByTag("ListFragment"))
                                .attach(getFragmentManager().findFragmentByTag("ListFragment"))
                                .commit();
                        break;
                }
            }
        };
        if (isInternetAvailable() && MovieDataSingleton.getInstance().isEmpty()) {
            Intent mServiceIntent = new Intent(getActivity(), TmdbPullService.class);
            mServiceIntent.putStringArrayListExtra("categories",
                    new ArrayList<>(Arrays.asList("category1", "category2", "category3")));
            getActivity().startService(mServiceIntent);
        }
    }

    @Override
    public void onStart() {
        Log.i(TAG, "ListFragment is now visible to the user");
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver),
                new IntentFilter(TmdbPullService.TMDB_RESULT));
    }

    @Override
    public void onResume() {
        Log.i(TAG, "ListFragment is now able to interact with the user");
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver),
                new IntentFilter(TmdbPullService.TMDB_RESULT));
    }

    @Override
    public void onPause() {
        Log.i(TAG, "ListFragment is no longer interacting with the user either " +
                "because its activity is being paused or a fragment operation is modifying it in the activity.");
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void onStop() {
        Log.i(TAG, "ListFragment fragment is no longer visible to the user either " +
                "because its activity is being stopped or a fragment operation is modifying it in the activity.");
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (MovieDataSingleton.getInstance().isEmpty()) {
            View view = inflater.inflate(R.layout.no_data_screen, container, false);
            if (!isInternetAvailable()) {
                TextView noConnection = (TextView) view.findViewById(R.id.no_internet_connection);
                noConnection.setVisibility(View.VISIBLE);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getActivity())
                                .setSmallIcon(R.drawable.ic_announcement_black_24dp)
                                .setContentTitle("No internet connection")
                                .setContentText("Please check your internet connection");
                NotificationManager mNotifyMgr =
                        (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(NotificationConstants.noConnectionNotifId, mBuilder.build());
            }
            return view;
        } else {
            View view = inflater.inflate(R.layout.main_fragment, container, false);
            return view;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (!MovieDataSingleton.getInstance().isEmpty()) {
            fillRecyclerView(view, R.id.recycler_view_movies_category1, MovieDataSingleton.getInstance().getMoviesThisWeek());
            fillRecyclerView(view, R.id.recycler_view_movies_category2, MovieDataSingleton.getInstance().getMoviesPopularThisYear());
            fillRecyclerView(view, R.id.recycler_view_movies_category3, MovieDataSingleton.getInstance().getMoviesPopularAllTime());
        }
    }

    private void fillRecyclerView(View view, int id, List<Movie> movies) {
        if (movies != null && movies.size() != 0) {
            mRecyclerView = (RecyclerView) view.findViewById(id);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new RecyclerViewAdapter(movies, getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "Here it is possible to clean up resources associated with ListFragment" +
                "because View is being destroyed");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "ListFragment is being destroyed, here clean up its resources");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "ListFragment is being detached from activity");
        super.onDetach();
    }
}
