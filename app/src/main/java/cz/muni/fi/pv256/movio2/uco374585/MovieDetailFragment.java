package cz.muni.fi.pv256.movio2.uco374585;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.database.loaders.MovieCreateDbLoader;
import cz.muni.fi.pv256.movio2.uco374585.database.loaders.MovieDeleteDbLoader;
import cz.muni.fi.pv256.movio2.uco374585.database.loaders.MovieFindDbLoader;
import cz.muni.fi.pv256.movio2.uco374585.database.MovieManager;
import cz.muni.fi.pv256.movio2.uco374585.model.Movie;
import cz.muni.fi.pv256.movio2.uco374585.ui.MovieDbCallbackPresenter;

import static cz.muni.fi.pv256.movio2.uco374585.utils.Constants.LOADER_CREATE_MOVIE_ID;
import static cz.muni.fi.pv256.movio2.uco374585.utils.Constants.LOADER_DELETE_MOVIE_ID;
import static cz.muni.fi.pv256.movio2.uco374585.utils.Constants.LOADER_SWAP_FAVOURITE_VALUE_OF_MOVIE_ID;

public class MovieDetailFragment extends Fragment {


    private static final String TAG = "MovieDetailFragment";
    public static MovieDetailFragment instance;
    ImageLoader imageLoader;
    private Movie movie;
    private MovieManager movieManager;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(Movie movie) {
        Log.i(TAG, "MovieDetail newInstance");
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MovieDetailFragment getInstance() {
        Log.i(TAG, "MovieDetail getInstance");
        return instance;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        if (args != null) {
            movie = args.getParcelable("movie");
        }
        instance = MovieDetailFragment.this;
        movieManager = new MovieManager(getActivity().getApplicationContext());
        Log.i(TAG, "onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = MovieDetailFragment.this;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        setHasOptionsMenu(true);
        Log.i(TAG, "onCreate() called");
    }

    public void setImages(View view) {
        imageLoader.displayImage(movie.getCoverPath(), (ImageView) view.findViewById(R.id.movie_image));
        imageLoader.displayImage(movie.getBackdrop(), (ImageView) view.findViewById(R.id.movie_backdrop));

        final ProgressBar spinnerCover = (ProgressBar) view.findViewById(R.id.spinner_cover_detail);
        imageLoader.displayImage(movie.getCoverPath(), (ImageView) view.findViewById(R.id.movie_image),
                null, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        spinnerCover.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        spinnerCover.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        spinnerCover.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
    }

    public void setTexts(View view) {
        TextView title = (TextView) view.findViewById(R.id.movie_title);
        title.setText(movie.getTitle());

        TextView releaseDate = (TextView) view.findViewById(R.id.release_date);
        releaseDate.setText(movie.getReleaseDate());

        TextView description = (TextView) view.findViewById(R.id.movie_description);
        description.setText(movie.getDescription());

        TextView popularity = (TextView) view.findViewById(R.id.popularity);
        popularity.setText("" + movie.getPopularity());
        String text = popularity.getText().toString();
        popularity.setText(text.substring(0, text.length() - 2) + "%");
    }

    public void setResources(View view) {
        movie = getArguments().getParcelable("movie");
        setImages(view);
        setTexts(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "+++ onCreateView() called! +++");
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        setResources(view);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (movieManager.findMoviesById(movie.getId()).size() == 0) {
            fab.setImageResource(R.drawable.ic_rating_star);
        } else {
            fab.setImageResource(R.drawable.ic_done);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putLong("id", movie.getId());
                if (getLoaderManager().getLoader(LOADER_SWAP_FAVOURITE_VALUE_OF_MOVIE_ID) == null) {
                    getLoaderManager().initLoader(LOADER_SWAP_FAVOURITE_VALUE_OF_MOVIE_ID, args, new MovieDbCallbackPresenter(getActivity().getApplicationContext())).forceLoad();
                } else {
                    getLoaderManager().restartLoader(LOADER_SWAP_FAVOURITE_VALUE_OF_MOVIE_ID, args, new MovieDbCallbackPresenter(getActivity().getApplicationContext())).forceLoad();
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLoaderManager().destroyLoader(LOADER_DELETE_MOVIE_ID);
        getLoaderManager().destroyLoader(LOADER_CREATE_MOVIE_ID);
        getLoaderManager().destroyLoader(LOADER_SWAP_FAVOURITE_VALUE_OF_MOVIE_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        instance = MovieDetailFragment.this;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        instance = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
        instance = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favourites:
                ListFragment listFragment =
                        (ListFragment) getActivity()
                                .getSupportFragmentManager()
                                .findFragmentByTag("ListFragment");
                if (listFragment == null) {
                    listFragment = ListFragment.newInstance();
                }
                listFragment.setFavourites(true);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (getResources().getBoolean(R.bool.isTablet)) {
                    transaction.replace(R.id.home_fragment, listFragment, "ListFragment");
                } else {
                    transaction.replace(R.id.fragment_container, listFragment, "ListFragment");
                }
                transaction.commit();
                return true;
            case R.id.discover:
                switchToListFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void switchToListFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (getResources().getBoolean(R.bool.isTablet)) {
            transaction.replace(R.id.home_fragment, ListFragment.newInstance(), "ListFragment");
        } else {
            transaction.replace(R.id.fragment_container, ListFragment.newInstance(), "ListFragment");
        }
        transaction.commit();
    }


}
