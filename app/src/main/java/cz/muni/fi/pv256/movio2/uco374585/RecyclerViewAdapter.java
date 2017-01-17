package cz.muni.fi.pv256.movio2.uco374585;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Collections;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.Models.Movie;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<Movie> movies = Collections.emptyList();
    View view;
    Context context;
    ImageLoader imageLoader;

    public RecyclerViewAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_layout, parent, false);
        return new ViewHolder(view);
    }


    public void setMovieImageWithBottomPanel(final ViewHolder holder, Movie movie) {
        //holder.imageView.setImageResource(R.drawable.loading);
        /*RelativeLayout layout = new RelativeLayout(context);
        final ProgressBar spinner = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        spinner.setIndeterminate(true);
        spinner.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(spinner,params);*/
        final ProgressBar spinner = (ProgressBar) holder.itemView.findViewById(R.id.spinner);
        imageLoader.displayImage(movie.getCoverPath(), holder.imageView, null, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.imageView.setImageBitmap(loadedImage);
                if (loadedImage != null && !loadedImage.isRecycled()) {
                    Palette.PaletteAsyncListener listener = new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            ImageView bottomPanel = (ImageView) holder.itemView.findViewById(R.id.movie_bottom_panel);
                            bottomPanel.setBackgroundColor(palette.getDarkMutedColor(0));
                            bottomPanel.setAlpha(0.8f);
                        }
                    };
                    Palette.from(loadedImage).generate(listener);
                    holder.itemView.findViewById(R.id.movie_title).setAlpha(1);
                }
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        TextView movieTitle = (TextView) holder.itemView.findViewById(R.id.movie_title);
        movieTitle.setText(movie.getTitle());
        movieTitle.setSelected(true);
        TextView rating = (TextView) holder.itemView.findViewById(R.id.rating_number);
        rating.setText("" + movie.getPopularity());
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