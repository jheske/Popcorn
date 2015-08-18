/**
 * Created by Jill Heske
 *
 * Copyright(c) 2015
 */
package com.nano.movies.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nano.movies.utils.ApiKey;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * This class serves as a connection between the UI and
 * the themoviedb service api proxies . At present I'm supporting only
 * the Movie service, but themoviedb.org offers other services, too, such as
 * People and TV, which I might like to integrate later on.
 */
public class Tmdb {

    /**
     * Tmdb API URL.
     */
    private static final String MOVIE_SERVICE_URL = "https://api.themoviedb.org/3";

    /**
     * API key query parameter name.
     * This has to be appended to every request.
     * Its value is in ApiKey.java, which is not
     * in my git repo and thus will never
     * be shared publicly
     */
    private static final String PARAM_API_KEY = "api_key";
    private boolean isDebug;
    private RestAdapter restAdapter;


    /**
     * A few image size constants selected from
     * https://api.themoviedb.org/3/configuration?api_key=xxx
     * They're kind of arbitrary,
     * but I don't know how else to pick them.
     * Put them here vs MovieServiceProxy in case
     * other service might need them.
     */
    public final String IMAGE_POSTER_ORIGINAL = "original";
    public static final String IMAGE_POSTER_XSMALL = "w92";
    public static final String IMAGE_POSTER_SMALL = "w185";
    public static final String IMAGE_POSTER_MED = "w342";
    public final String IMAGE_POSTER_LARGE = "w500";
    public final String IMAGE_POSTER_XLARGE = "w780";

    /**
     * Constructor to create a new manager instance.
     */
    public Tmdb() {
    }

    public static String getMovieImageUrl(String mImagePath, String imageSize) {
        return ("http://image.tmdb.org/t/p/" + imageSize + "/" + mImagePath);
    }

    /**
     * Set RestAdapter log level.
     *
     * @param isDebug true = LogLevel set to FULL
     */
    public void setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        if (restAdapter != null) {
            restAdapter.setLogLevel(isDebug ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);
        }
    }

    /**
     * Create a RestAdapterBuilder to help build the adapter
     */
    private RestAdapter.Builder restAdapterBuilder() {
        return new RestAdapter.Builder();
    }

    /**
     * Create the RestAdapter
     */
    private RestAdapter getRestAdapter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        if (restAdapter == null) {
            RestAdapter.Builder builder = restAdapterBuilder();
            builder.setConverter(new GsonConverter(gson));
            builder.setEndpoint(MOVIE_SERVICE_URL);
            builder.setRequestInterceptor(new RequestInterceptor() {
                // Add API_KEY to every API request
                // Always request releases and trailers

                /**********
                 *
                 *  !!!!IMPORTANT -
                 *  themoviedb.org API KEYS
                 *  MAY NOT BE SHARED PUBLICLY!!!!
                 *
                 *  I get mine using call to ApiKey.getApiKey()
                 *  The ApiKey Class is in .gitignore so it is
                 *  excluded from my repo.
                 *
                 *  @See https://www.themoviedb.org/faq/api?language=en
                 *  to find instructions for obtaining your own key.
                 *
                 *  ONCE YOU HAVE YOUR KEY, YOU MUST REPLACE
                 *      requestFacade.addQueryParam(PARAM_API_KEY, ApiKey.getApiKey());
                 *  WITH
                 *      requestFacade.addQueryParam(PARAM_API_KEY, "YOUR API KEY");
                 *
                 *  OR
                 *      You may provide your own ApiKey.java class as follows:
                 *
                 *         public class ApiKey {
                 *             private static final String apiKey = "xxx";
                 *              public static String getApiKey() {return apiKey;}
                 *              //Make ApiKey a utility class by preventing instantiation.
                 *              private ApiKey() {throw new AssertionError();}
                 *         }
                 *
                 *      REMEMBER to .gitignore it to keep it out of your repo.
                 *
                 */
                @Override
                public void intercept(RequestInterceptor.RequestFacade requestFacade) {
                    requestFacade.addQueryParam(PARAM_API_KEY, ApiKey.getApiKey());
                }
            });
            if (isDebug) {
                builder.setLogLevel(RestAdapter.LogLevel.FULL);
            }
            restAdapter = builder.build();
        }
        return restAdapter;
    }

    /**
     *  Proxy to the Tmdb Movie service.
     *  There are other proxies I can add later,
     *  like Search and TV.
     */
    public MovieServiceProxy moviesServiceProxy() {
        return getRestAdapter().create(MovieServiceProxy.class);
    }
}
