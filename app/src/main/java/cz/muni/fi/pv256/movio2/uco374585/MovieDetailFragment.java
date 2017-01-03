package cz.muni.fi.pv256.movio2.uco374585;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;

public class MovieDetailFragment extends Fragment {

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
    }

    public void setImages(View view) {
        ImageView backdrop_imageView = (ImageView) view.findViewById(R.id.movie_backdrop);
        ImageView movie_imageView = (ImageView) view.findViewById(R.id.movie_image);
        backdrop_imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                movie.getBackdrop(), null));
        movie_imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                movie.getCoverPath(), null));
    }

    public void setTexts(View view) {
        TextView title = (TextView) view.findViewById(R.id.movie_title);
        title.setText(movie.getTitle());

        TextView releaseDate = (TextView) view.findViewById(R.id.release_date);
        releaseDate.setText(String.valueOf(new SimpleDateFormat("yyyy/MM/dd").format(movie.getReleaseDate())));

        TextView description = (TextView) view.findViewById(R.id.movie_description);
        description.setText(String.valueOf(movie.getDescription()));

        TextView popularity = (TextView) view.findViewById(R.id.popularity);
        popularity.setText("" + movie.getPopularity());
        String text = popularity.getText().toString();
        popularity.setText(text.substring(0, text.length() - 2) + "%");
    }

    public void setResources(View view) {
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
}
