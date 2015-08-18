/**
 * Created by Jill Heske
 *
 * Copyright(c) 2015
 */
package com.nano.movies.data;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * THIS CLASS IS NOT USED YET.  IT WILL BE INTEGRATED
 * IN P1, STAGE 2
 */
public class RecyclerCursorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor dataCursor;

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }


    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    private Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    private Object getItem(int position) {
        dataCursor.moveToPosition(position);
        // Load data from dataCursor and return it...
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
