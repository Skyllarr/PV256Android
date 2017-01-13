package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<Movie> movies = Collections.emptyList();
    View view;
    Context context;

    public RecyclerViewAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_layout, parent, false);
        return new ViewHolder(view);
    }


    public void setMovieImageWithBottomPanel(final ViewHolder holder, Movie movie) {
        holder.imageView.setImageResource(movie.getCoverPath());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), movie.getCoverPath());
        if (bitmap != null && !bitmap.isRecycled()) {
            Palette.PaletteAsyncListener listener = new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    ImageView bottomPanel = (ImageView) holder.itemView.findViewById(R.id.movie_bottom_panel);
                    bottomPanel.setBackgroundColor(palette.getDarkMutedColor(0));
                    bottomPanel.setAlpha(0.5f);
                }
            };
            Palette.from(bitmap).generate(listener);
            holder.itemView.findViewById(R.id.movie_title).setAlpha(1);
            TextView movieTitle = (TextView) holder.itemView.findViewById(R.id.movie_title);
            movieTitle.setText(movie.getTitle());
            TextView rating = (TextView) holder.itemView.findViewById(R.id.rating_number);
            rating.setText("" + movie.getPopularity());
        }
    }

    public void viewMovieDetailFragment(Movie movie) {
        FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.getArguments() != null) {
            Movie currentlyShownMovie = currentFragment.getArguments().getParcelable("movie");
            if (currentlyShownMovie.getTitle() == movie.getTitle())
                return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        setMovieImageWithBottomPanel(holder, movie);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMovieDetailFragment(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, Movie movie) {
        movies.add(position, movie);
        notifyItemInserted(position);
    }

    public void remove(Movie movie) {
        int position = movies.indexOf(movie);
        movies.remove(position);
        notifyItemRemoved(position);
    }
}