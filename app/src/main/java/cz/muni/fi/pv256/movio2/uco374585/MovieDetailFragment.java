package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;

public class MovieDetailFragment extends Fragment {

    ImageLoader imageLoader;
    private Movie movie;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable("movie");
        }
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
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
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        setResources(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onDetach();
    }
}
