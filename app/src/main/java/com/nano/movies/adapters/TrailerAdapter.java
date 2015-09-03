package com.nano.movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nano.movies.R;
import com.nano.movies.web.Tmdb;
import com.nano.movies.web.Trailers.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = getClass().getSimpleName();
    private List<Trailer> mTrailers;
    private static final int EMPTY_VIEW = 10;
    private Context mContext;

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imgThumbnail;

        public ViewHolder(View view) {
            super(view);
            imgThumbnail = (ImageView) view.findViewById(R.id.img_youtube_thumbnail);
        }
    }

    // Constructor
    public TrailerAdapter(Context context) {
        mContext = context;
        mTrailers = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == EMPTY_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            return new EmptyViewHolder(view);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * Don't bind anything if holder is not a ViewHolder (it's an EmptyViewHolder)
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final Trailer trailer = getItemAtPosition(position);
            ViewHolder vh = (ViewHolder) holder;
            final String thumbImagePath = Tmdb.getYoutubeThumbnail(trailer.getSource());

            Picasso.with(mContext).load(thumbImagePath)
                    .placeholder(R.drawable.placeholder_poster_w185)
                    .error(R.drawable.no_poster_w185)
                    .into(vh.imgThumbnail);
            vh.imgThumbnail.setContentDescription(trailer.getName());
            vh.imgThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Play Youtube Video " + Uri.parse(Tmdb.getYoutubeUrl(trailer.getSource())));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getSource()));
                    intent.putExtra("VIDEO_ID", trailer.getSource());
                    intent.putExtra("force_fullscreen", true);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public Trailer getItemAtPosition(int position) {
        return mTrailers.get(position);
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     * Return 1 if the list is empty in order
     * to make room for the EMPTY_VIEW message
     */
    @Override
    public int getItemCount() {
        return mTrailers.size() > 0 ? mTrailers.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mTrailers.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    /**
     * Empties out the whole and optionally notifies
     */
    public void clear(boolean notify) {
        mTrailers.clear();
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void add(int position, Trailer trailer) {
        mTrailers.add(position, trailer);
        notifyItemInserted(position);
    }

    public void remove(Trailer trailer) {
        int position = mTrailers.indexOf(trailer);
        mTrailers.remove(position);
        notifyItemRemoved(position);
    }

    public void addAll(List<Trailer> trailers) {
        mTrailers.addAll(trailers);
        notifyDataSetChanged();
    }
}
/**
 * vh.imgThumbnail.setOnClickListener(new View.OnClickListener() {
 *
 * @Override public void onClick(View v) {
 * Log.d(TAG, "Play Youtube Video " + Uri.parse(Tmdb.getYoutubeUrl(trailer.getSource())));
 * Intent intent = new Intent(mContext, VideoViewActivity.class);
 * intent.putExtra(VideoViewActivity.VIDEO_PATH_EXTRA, Tmdb.getYoutubeUrl(trailer.getSource()));
 * mContext.startActivity(intent);
 * }
 * });
 * }
 **/