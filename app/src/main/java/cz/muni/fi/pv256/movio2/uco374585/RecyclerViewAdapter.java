package cz.muni.fi.pv256.movio2.uco374585;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco374585.model.Movie;

class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private Context context;
    private ImageLoader imageLoader;

    RecyclerViewAdapter(List<Movie> movies, Context context) {
        this.movies.addAll(movies);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_layout, parent, false);
        return new ViewHolder(view);
    }


    private void setMovieImageWithBottomPanel(final ViewHolder holder, Movie movie) {
        final ProgressBar spinner = (ProgressBar) holder.itemView.findViewById(R.id.spinner);
        final ImageView star = (ImageView) holder.itemView.findViewById(R.id.rating);
        imageLoader.displayImage(movie.getCoverPath(), holder.imageView, null, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                spinner.setVisibility(View.VISIBLE);
                star.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                spinner.setVisibility(View.VISIBLE);
                star.setVisibility(View.INVISIBLE);
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
                star.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        TextView movieTitle = (TextView) holder.itemView.findViewById(R.id.movie_title);
        movieTitle.setText(movie.getTitle());
        movieTitle.setSelected(true);
        TextView rating = (TextView) holder.itemView.findViewById(R.id.rating_number);
        rating.setText(String.format("%s", movie.getPopularity()));
    }

    private void viewMovieDetailFragment(Movie movie) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        Fragment movieDetailFragment = fragmentManager.findFragmentByTag("MovieDetailFragment");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (movieDetailFragment == null) {
            movieDetailFragment = MovieDetailFragment.newInstance(movie);
            Bundle bundle = new Bundle();
            bundle.putParcelable("movie", movie);
            movieDetailFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container, movieDetailFragment, "MovieDetailFragment");
        } else {
            movieDetailFragment.getArguments().putParcelable("movie", movie);
        }

        if (context.getResources().getBoolean(R.bool.isTablet)) {
            fragmentTransaction
                    .detach(movieDetailFragment)
                    .attach(movieDetailFragment)
                    .commit();
        } else {
            fragmentTransaction
                    .replace(R.id.fragment_container, movieDetailFragment, "MovieDetailFragment")
                    .addToBackStack(null)
                    .commit();
        }
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

    @UiThread
    void updateList(List<Movie> newData) {
        movies.clear();
        movies.addAll(newData);
        notifyDataSetChanged();
    }

    public List<Movie> getData() {
        return movies;
    }
}