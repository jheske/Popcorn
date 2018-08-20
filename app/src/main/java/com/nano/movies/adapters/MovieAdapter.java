/**
 * Created by Jill Heske
 * <p>
 * Copyright(c) 2015
 */
package com.nano.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nano.movies.R;
import com.nano.movies.web.Movie;
import com.nano.movies.web.Tmdb;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for connecting the RecyclerView with
 * the list of Movies.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final List<Movie> mMovies;

    public MovieAdapter(Context context) {
        super();
        mMovies = new ArrayList<>();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_poster)
        public ImageView imgPoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_grid_item, viewGroup, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        //Movie movie = mMovies.get(holder.getAdapterPosition());
        Movie movie = getItemAtPosition(position);
        final String movieImageUrl = Tmdb.getMoviePosterUrl(movie.getPosterPath(),
                Tmdb.IMAGE_POSTER_MED);

        Picasso.get()
                .load(movieImageUrl)
                .placeholder(R.drawable.placeholder_poster_w185)
                .error(R.drawable.no_poster_w185)
                .into(holder.imgPoster);

        holder.imgPoster.setContentDescription(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public Movie getItemAtPosition(int position) {
        return mMovies.get(position);
    }

    public void addAll(List<Movie> movies) {
        for (int position = 0; position < movies.size(); position++) {
            addItem(movies.get(position), position);
        }
    }

    private void addItem(Movie movie, int position) {
        mMovies.add(position, movie);
        notifyItemInserted(position);
    }

    /**
     * Clear all the movies from the RecyclerView,
     * usually in preparation for re-downloading
     * the list with different sort criteria.
     */
    public void clear() {
        int size = mMovies.size();
        if (size <= 0)
            return;
        for (int i = 0; i < size; i++)
            mMovies.remove(0);
        notifyItemRangeRemoved(0, size);
    }
}
