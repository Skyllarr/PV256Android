package cz.muni.fi.pv256.movio2.uco374585;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.Data.Loaders.MovieFindAllDbLoader;
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
    private static final int LOADER_FIND_ALL_MOVIE = 4;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mAdapter;
    private BroadcastReceiver receiver;
    private LocalBroadcastManager mBroadcastManager;
    private boolean favourites = false;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

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

    public void setFavourites(boolean favourites) {
        this.favourites = favourites;
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "ListFragment was attached to its context");
        mBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra(TmdbPullService.TMDB_MESSAGE);
                switch (message) {
                    case "FINISHED":
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .detach(getActivity().getSupportFragmentManager().findFragmentByTag("ListFragment"))
                                .attach(getActivity().getSupportFragmentManager().findFragmentByTag("ListFragment"))
                                .commit();
                        break;
                }
            }
        };
        if (isInternetAvailable() && MovieDataSingleton.getInstance().isEmpty() && !favourites) {
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
    }

    @Override
    public void onPause() {
        Log.i(TAG, "ListFragment is no longer interacting with the user either " +
                "because its activity is being paused or a fragment operation is modifying it in the activity.");
        mBroadcastManager.unregisterReceiver(mBroadcastReceiver);
        super.onPause();

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
        if (favourites) {
            View view = inflater.inflate(R.layout.main_fragment, container, false);
            TextView category1 = (TextView) view.findViewById(R.id.category1);
            category1.setText("Favourites");
            getLoaderManager().initLoader(LOADER_FIND_ALL_MOVIE, null, new ListCallback(getActivity().getApplicationContext())).forceLoad();

            return view;
        }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favourites:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                TextView category1 = (TextView) getView().findViewById(R.id.category1);
                category1.setText("Favourites");
                getLoaderManager().initLoader(LOADER_FIND_ALL_MOVIE, null, new ListCallback(getActivity().getApplicationContext())).forceLoad();
                return true;
            case R.id.discover:
                RecyclerView recyclerViewCat1 = (RecyclerView) getActivity()
                        .findViewById(R.id.recycler_view_movies_category1);
                mAdapter = (RecyclerViewAdapter) recyclerViewCat1.getAdapter();
                mAdapter.updateList(MovieDataSingleton.getInstance().getMoviesThisWeek());
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private class ListCallback implements LoaderManager.LoaderCallbacks<List<Movie>> {

        private static final String TAG = "ListCallback";
        Context mContext;

        public ListCallback(Context context) {
            mContext = context;
            Log.i("ListCallback", "+++ ListCallback constructor called! +++");
        }

        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "+++ onCreateLoader() called! +++");
            switch (id) {
                case LOADER_FIND_ALL_MOVIE:
                    return new MovieFindAllDbLoader(mContext);
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            Log.i(TAG, "+++ onLoadFinished() called! +++");
            switch (loader.getId()) {
                case LOADER_FIND_ALL_MOVIE:
                    Log.i("MovieCallback", "+++ LOADER_FIND_ALL_MOVIE() called! +++");
                    RecyclerView recyclerViewCat1 = (RecyclerView) getActivity()
                            .findViewById(R.id.recycler_view_movies_category1);
                    mAdapter = (RecyclerViewAdapter) recyclerViewCat1.getAdapter();
                    mAdapter.updateList(data);
                    RecyclerView recyclerViewCat2 = (RecyclerView) getActivity()
                            .findViewById(R.id.recycler_view_movies_category2);
                    RecyclerView recyclerViewCat3 = (RecyclerView) getActivity()
                            .findViewById(R.id.recycler_view_movies_category3);
                    recyclerViewCat2.setVisibility(View.GONE);
                    recyclerViewCat3.setVisibility(View.GONE);
                    TextView textViewCat2 = (TextView) getActivity()
                            .findViewById(R.id.category2);
                    TextView textViewCat3 = (TextView) getActivity()
                            .findViewById(R.id.category3);
                    textViewCat2.setVisibility(View.GONE);
                    textViewCat3.setVisibility(View.GONE);
                    break;
                default:
                    throw new UnsupportedOperationException("Not know loader id");
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            Log.i(TAG, "+++ onLoaderReset() called! +++");
        }
    }
}
