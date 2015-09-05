package com.nano.movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nano.movies.R;
import com.nano.movies.web.Reviews.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * !!!RecyclerView does not handle empty lists!!!
 * So we have to do it ourselves and it's messy.
 *
 * http://blabadi.blogspot.com/2014/12/android-recyclerview-adding-empty-view.html
 */
public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Review> mReviews;
    private static final int EMPTY_VIEW = 10;

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
        @Bind(R.id.txt_author)
        public TextView txtAuthor;
        @Bind(R.id.txt_content)
        public TextView txtContent;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    // Constructor
    public ReviewAdapter() {
        mReviews = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == EMPTY_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
            return new EmptyViewHolder(view);
        }

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_card, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * Don't bind anything if holder is not a ViewHolder (it's an EmptyViewHolder)
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Review review = mReviews.get(position);
            ViewHolder vh = (ViewHolder) holder;
            vh.txtAuthor.setText(review.getAuthor());
            vh.txtContent.setText(String.valueOf(review.getContent()));
        }
    }

    /**
     *  Return the size of your dataset (invoked by the layout manager)
     *  Return 1 if the list is empty in order
     *  to make room for the EMPTY_VIEW message
     */

    @Override
    public int getItemCount() {
        return mReviews.size() > 0 ? mReviews.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mReviews.size() == 0) {
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    /**
     * Empties out the whole and optionally notifies
     */
    public void clear(boolean notify) {
        mReviews.clear();
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void add(int position, Review item) {
        mReviews.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Review item) {
        int position = mReviews.indexOf(item);
        mReviews.remove(position);
        notifyItemRemoved(position);
    }

    public void addAll(List<Review> reviews) {
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }
}
