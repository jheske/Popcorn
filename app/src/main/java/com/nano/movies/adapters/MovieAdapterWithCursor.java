package com.nano.movies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nano.movies.R;
import com.nano.movies.data.movie.MovieCursor;
import com.nano.movies.web.Movie;
import com.nano.movies.web.Tmdb;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for connecting the RecyclerView with
 * the list of Movies.
 *
 * Udacity RecyclerView gist
 * https://github.com/udacity/Advanced_Android_Development/blob/6.18_Bonus_RecyclerView_Code/app/src/main/java/com/example/android/sunshine/app/ForecastAdapter.java
 */
public class MovieAdapterWithCursor extends RecyclerView.Adapter<MovieAdapterWithCursor.MovieViewHolder> {
//    private final List<Movie> mMovies;
    private Context mContext;
    private Cursor mCursor;
   // final private View mEmptyView;

    public MovieAdapterWithCursor(Context context) {
        super();
        mContext = context;
//        mMovies = new ArrayList<>();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imgPoster = (ImageView) itemView.findViewById(R.id.img_poster);
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
        mCursor.moveToPosition(position);
        Movie movie = new Movie(mCursor);
        final String movieImageUrl = Tmdb.getMovieImageUrl(movie.getPosterPath(),
                Tmdb.IMAGE_POSTER_MED);
        Picasso.with(mContext).load(movieImageUrl)
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

/** Replaced with database inserts in MovieGridFragment
    public void addAll(List<Movie> movies) {
        for (int position = 0; position < movies.size(); position++) {
            addItem(movies.get(position), position);
        }
    }

    private void addItem(Movie movie, int position) {
        mMovies.add(position, movie);
        notifyItemInserted(position);
    }
*/

    /**
     * Clear all the movies from the RecyclerView,
     * usually in preparation for re-downloading
     * the list with different sort criteria.
     *
    public void clear() {
        int size = mMovies.size();
        if (size <= 0)
            return;
        for (int i = 0; i < size; i++)
            mMovies.remove(0);
        notifyItemRangeRemoved(0, size);
    } */

    public void moveCursorToPosition(int position) {
        mCursor.moveToPosition(position);
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
//        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return mCursor;
    }
}

