package com.nano.movies.adapters;

/**
 * Created by jill on 9/7/2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.nano.movies.R;
import com.nano.movies.activities.FavoritesGridFragment;
import com.nano.movies.activities.MovieGridFragment;
import com.nano.movies.web.MovieService;

import butterknife.BindString;

//Most recent solution
//http://guides.codepath.com/android/ViewPager-with-FragmentPagerAdapter#dynamic-viewpager-fragments
//and its example http://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout
//http://android-developers.blogspot.com/2015/05/android-design-support-library.html
//http://stackoverflow.com/questions/30539772/android-tablayout-android-design
//http://developer.android.com/reference/android/support/v13/app/FragmentPagerAdapter.html
//http://codetheory.in/android-swipe-views-with-tabs/
//public class PopcornPagerAdapter extends FragmentStatePagerAdapter {
public class PopcornPagerAdapter extends FragmentPagerAdapter {
    int mTabCount;
    public final int FRAGMENT_MOST_POPULAR = 0;
    public final int FRAGMENT_HIGHEST_RATED = 1;
    public final int FRAGMENT_FAVORITES = 2;
    int mPageCount;
    Context context;
    @BindString(R.string.tab_highest_rated)
    String mTabHighestRated;
    @BindString(R.string.tab_most_popular)
    String mTabMostPopular;
    @BindString(R.string.tab_favorites)
    String mTabFavorites;
    private String tabTitles[] = new String[]{"Most Popular","Highest Rated", "Favorites"};

    public PopcornPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.mPageCount = 3;
    }

    @Override
    public int getCount() {
        return mPageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_MOST_POPULAR:
                return MovieGridFragment.newInstance(MovieService.POPULARITY_DESC);
            /*    MovieGridFragment popularityFragment = new MovieGridFragment();
                args.putString("SORT_BY", MovieService.POPULARITY_DESC);
                popularityFragment.setArguments(args);
                return popularityFragment; */
            case FRAGMENT_HIGHEST_RATED:
                return MovieGridFragment.newInstance(MovieService.VOTE_AVERAGE_DESC);
                /*MovieGridFragment voteAvgFragment = new MovieGridFragment();
                args = new Bundle();
                args.putString("SORT_BY", MovieService.VOTE_AVERAGE_DESC);
                voteAvgFragment.setArguments(args);
                return voteAvgFragment; */
            case FRAGMENT_FAVORITES:
                return FavoritesGridFragment.newInstance();
                /*FavoritesGridFragment favoritesFragment = new FavoritesGridFragment();
                return favoritesFragment;*/
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }
}

    /*
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public PopcornPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.mTabCount = tabCount;
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();

        switch (position) {
            case FRAGMENT_MOST_POPULAR:
                return MovieGridFragment.newInstance(MovieService.POPULARITY_DESC);
            case FRAGMENT_HIGHEST_RATED:
                return MovieGridFragment.newInstance(MovieService.VOTE_AVERAGE_DESC);
            case FRAGMENT_FAVORITES:
                return FavoritesGridFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }



    int tabCount;
    public final int FRAGMENT_MOST_POPULAR = 0;
    public final int FRAGMENT_HIGHEST_RATED = 1;
    public final int FRAGMENT_FAVORITES = 2;
    private Fragment[] mFragments;


    public PopcornPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
        mFragments = new Fragment[tabCount];
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    //http://stackoverflow.com/questions/22737168/how-to-use-actionbar-viewpager-and-fragments-and-correctly-replace-fragments-in
    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();

        switch (position) {
            case FRAGMENT_MOST_POPULAR:
               if (mFragments[position] == null) {
                    MovieGridFragment popularityFragment = new MovieGridFragment();
                    args.putString("SORT_BY", MovieService.POPULARITY_DESC);
                    popularityFragment.setArguments(args);
                    mFragments[position] = popularityFragment;
                }
                break;
            case FRAGMENT_HIGHEST_RATED:
                if (mFragments[position] == null) {
                    MovieGridFragment voteAvgFragment = new MovieGridFragment();
                    args = new Bundle();
                    args.putString("SORT_BY", MovieService.VOTE_AVERAGE_DESC);
                    voteAvgFragment.setArguments(args);
                    mFragments[position] = voteAvgFragment;
                }
                break;
            case FRAGMENT_FAVORITES:
                if (mFragments[position] == null) {
                    FavoritesGridFragment favoritesFragment = new FavoritesGridFragment();
                    mFragments[position] = favoritesFragment;
                }
                break;
            default:
                return null;
        }
        return mFragments[position];
    }
*/
