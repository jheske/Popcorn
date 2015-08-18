## Synopsis

This repository contains my Udacity Android Developer Project 1: Popular Movies, Stage 1

Udacity's description of the project is as follows:

In this project, you will build an app to help users discover popular and recent movies. You will build a clean UI, sync to a server, and present information to the user.

## State of the Project

This project is a work in progress.  It is designed to meet all of the requirements as per Udacity's rubric for P1, Stage 1.  In addition, it contains some features in preparation for Project 1: Popular Movies, Stage 2.  These features may or may not be completed and include, but are not limited to:

* Add `trailers.java`, marshalls/unmarshalls movie trailers, implemented but not integrated.
* Add `reviews.java`, marshalls/unmarshalls user reviews, implemented but not integrated.
* Include elements in Movie Details layout for displaying trailer videos and user reviews. These features are not yet integrated.
* Include a Mark as Favorite button in Movie Details layout. This feature is not implemented yet.
* Include Spinner option for displaying Favorites.  It is not functional yet, so selecting it does nothing.
* Include RecyclerCursorAdapter.java.  In P1, Stage 2 it will connect the database with the RecyclerView

## Testing

This project has been tested on:

* Samsung Tab 4 running Android 4.4.2.
* Huawei Prism running Android 2.3
			
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