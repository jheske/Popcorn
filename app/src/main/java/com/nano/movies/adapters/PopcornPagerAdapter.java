package com.nano.movies.adapters;

/**
 * Created by jill on 9/7/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nano.movies.activities.FavoritesGridFragment;
import com.nano.movies.activities.MovieGridFragment;
import com.nano.movies.web.MovieService;

public class PopcornPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PopcornPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MovieGridFragment popularityFragment = new MovieGridFragment();
                Bundle args = new Bundle();
                args.putString("SORT_BY", MovieService.POPULARITY_DESC);
                popularityFragment.setArguments(args);
                return popularityFragment;
            case 1:
                MovieGridFragment voteAvgFragment = new MovieGridFragment();
                args = new Bundle();
                args.putString("SORT_BY", MovieService.VOTE_AVERAGE_DESC);
                voteAvgFragment.setArguments(args);
                return voteAvgFragment;
            case 2:
                FavoritesGridFragment favoritesFragment = new FavoritesGridFragment();
                return favoritesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
