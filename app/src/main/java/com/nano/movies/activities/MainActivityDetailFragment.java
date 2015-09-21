package com.nano.movies.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nano.movies.R;

import butterknife.ButterKnife;

public class MainActivityDetailFragment extends DetailFragment {

    /**
     * The layout has to be selected according to the calling Activity.
     * MainActivity and DetailActivity each has its own layout, NOT
     * necessarily dependent on device size or orientation.
     * This one is for MainActivity (two-pane landscape) detail pane only.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_main_activity_detail, container, false);
        ButterKnife.bind(this, rootView);
        setupRecyclerView(rootView);
        return rootView;
    }
}
