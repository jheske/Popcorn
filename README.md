<a href="https://www.linkedin.com/pub/jill-heske/13/836/635">
                <img src="https://static.licdn.com/scds/common/u/img/webpromo/btn_viewmy_160x33.png" width="160" height="33" border="0" alt="View Jill Heske's profile on LinkedIn"></a>


## Synopsis

![Popcorn icon](https://github.com/jheske/Popcorn/blob/master/app/src/main/res/mipmap-xhdpi/ic_launcher.png?raw=true)

This repository contains **Popcorn**, a movie listing app I designed and implemented for Udacity Android Developer Nanodegree Project 2: Popular Movies.


## Features

This project is designed to meet all of the requirements as per Udacity's rubric for P2.  Features may or may not be completed and include, but are not limited to:

Material Design conforms to Google's latest UI/UX standards. 

Features Google's latest Material AppCompat and Design library widgets, including CoordinatorLayout, AppBarLayout, CollapsingToolbarLayout, Toolbar, NestedScrollView, TabLayout, ViewPager, RecyclerView, and CardView.

Uses Retrofit 1.0 to retrieve current movies from themoviedb.org and parse JSON results into POJOs.  POJOs are Parcelable to facilitate passing movie objects between Activities.

Uses a SQLite database and a ContentProvider for storing and retrieving movies.

Tabbed layout on home page allows user to view Most Popular, Highest Rated, or user-selected Favorite movies.

A movie detail screen shows runtime, rating, release date, genres, and reviews.  It allows user to view YouTube trailers and has a Share option.
 
Provides a variety of portrait and landscape layouts to support both phones and tablets in a variety of screen resolutions.  The tablet version provides a master/detail layout in landscape mode.

## Screenshots

<img src="landscape-master-detail.png" alt="Popcorn landscape master-detail" width="600">


<img src="portrait-most-popular.png" alt="Popcorn home screen portrait" width="300"> <img src="portrait-movie-details.png" alt="Popcorn movie details" width="300">



## build.gradle

    compile 'com.android.support:appcompat-v7:23.0.0'
    compile 'com.android.support:recyclerview-v7:23.0.0'
    compile 'com.android.support:cardview-v7:23.0.0'
    compile 'com.android.support:support-v4:23.0.0'
    compile 'com.android.support:design:23.0.0'
    compile 'com.android.support:gridlayout-v7:23.0.0'
    compile 'com.squareup.phrase:phrase:1.1.0'
    compile 'com.squareup.retrofit:retrofit:1.9.0'
    compile 'com.squareup.picasso:picasso:2.5.2'
    compile 'com.squareup.okhttp:okhttp:2.4.0'
    compile 'com.google.code.gson:gson:2.3'
    compile 'com.facebook.stetho:stetho:1.1.1'
    compile 'com.jakewharton:butterknife:7.0.1'

## Icon 

http://www.iconarchive.com/show/button-ui-requests-2-icons-by-blackvariant/PopcornTime-icon.html


## Testing

This project has been tested on:

* Samsung Tab 4 running Android 4.4.2
* LG Optimus L90 running Android 4.4.2
* GenyMotion emulator running Android 5.1

			
## Installation

You can fork this repo or clone it using `git clone https://github.com/jheske/popular-movies.git`


## Required api key

All calls to api.themoviedb.org require a valid api key. 
 
**themoviedb.org api keys may not be shared publicly so I have removed my key from this app**

In order to compile and run the app, you will need to acquire your own api key.  See [https://www.themoviedb.org/faq/api?language=en](https://www.themoviedb.org/faq/api?language=en) for instructions.

    
Once you have your key, you must edit `tmdb.java` and replace the line:

    requestFacade.addQueryParam(PARAM_API_KEY, ApiKey.getApiKey());

with

    requestFacade.addQueryParam(PARAM_API_KEY, "your api key");

and remove the ApiKey class reference by commenting out

	import com.nano.movies.utils.ApiKey;
    
**REMEMBER to remove your api key before sharing your app.**

Alternatively, you may provide your own ApiKey.java class as follows:
    
    public class ApiKey {

    	private static final String apiKey = "your api key";

    	public static String getApiKey() {return apiKey;}

    	//Make ApiKey a utility class by preventing instantiation.
    	private ApiKey() {throw new AssertionError();}

    }

**REMEMBER to .gitignore ApiKey.java to keep it out of your repo.**


## Contributors

Jill Heske

## License

See LICENSE file at top level of repo.