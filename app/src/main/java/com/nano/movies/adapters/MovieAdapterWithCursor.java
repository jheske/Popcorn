package com.nano.movies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nano.movies.R;
import com.nano.movies.web.Movie;
import com.nano.movies.web.Tmdb;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter for connecting the RecyclerView with
 * the list of Movies.
 *
 * Udacity RecyclerView gist
 * https://github.com/udacity/Advanced_Android_Development/blob/6.18_Bonus_RecyclerView_Code/app/src/main/java/com/example/android/sunshine/app/ForecastAdapter.java
 */
public class MovieAdapterWithCursor extends RecyclerView.Adapter<MovieAdapterWithCursor.MovieViewHolder> {
    private Cursor mCursor;

    public MovieAdapterWithCursor(Context context) {
        super();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_poster)
        public ImageView imgPoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
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
        mCursor.moveToPosition(position);
        Movie movie = new Movie(mCursor);
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
        if ( mCursor == null ) return 0;
          return mCursor.getCount();
    }

    public Movie getItemAtPosition(int position) {
        // Get the movie out of the database
        mCursor.getCount();
        return (new Movie(mCursor));
    }

    public void moveCursorToPosition(int position) {
        mCursor.moveToPosition(position);
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }
}

