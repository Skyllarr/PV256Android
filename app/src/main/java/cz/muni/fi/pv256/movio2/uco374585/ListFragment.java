package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.concurrent.ExecutionException;
import cz.muni.fi.pv256.movio2.uco374585.Data.MovieDataSingleton;
import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;
import static cz.muni.fi.pv256.movio2.uco374585.Api.ApiQuery.API_KEY;
import static cz.muni.fi.pv256.movio2.uco374585.Api.ApiQuery.DISCOVER_URL;
import static cz.muni.fi.pv256.movio2.uco374585.Api.ApiQuery.MOST_POPULAR_EVER_URL;
import static cz.muni.fi.pv256.movio2.uco374585.Api.ApiQuery.MOST_POPULAR_THIS_YEAR_URL;
import static cz.muni.fi.pv256.movio2.uco374585.Api.ApiQuery.THIS_WEEK_URL;

/**
 * Created by Skylar on 12/27/2016.
 */

public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

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
        try {
            if (isInternetAvailable() && MovieDataSingleton.getInstance().isEmpty()) {
                MovieDataSingleton.getInstance().setMoviesThisWeek(new MovieDownloader().execute(DISCOVER_URL + THIS_WEEK_URL + API_KEY).get()); ;
                MovieDataSingleton.getInstance().setMoviesPopularThisYear(new MovieDownloader().execute(DISCOVER_URL + MOST_POPULAR_THIS_YEAR_URL + API_KEY).get());
                MovieDataSingleton.getInstance().setMoviesPopularAllTime(new MovieDownloader().execute(DISCOVER_URL + MOST_POPULAR_EVER_URL + API_KEY).get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        Log.i(TAG, "ListFragment is now visible to the user");
        super.onStart();
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
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "ListFragment fragment is no longer visible to the user either " +
                "because its activity is being stopped or a fragment operation is modifying it in the activity.");
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (MovieDataSingleton.getInstance().isEmpty()) {
            View view = inflater.inflate(R.layout.no_data_screen, container, false);
            if (!isInternetAvailable()) {
                TextView noConnection = (TextView) view.findViewById(R.id.no_internet_connection);
                noConnection.setVisibility(View.VISIBLE);
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
