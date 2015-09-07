/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.web;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


//import com.nano.movies.utils.ApiKey;

import java.io.IOException;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;

import com.nano.movies.web.ErroHandler.ApiErrorHandler;

/**
 * This class serves as a connection between the UI and
 * the themoviedb service api proxies . At present I'm supporting only
 * the Movie service, but themoviedb.org offers other services, too, such as
 * People and TV, which I might like to integrate later on.
 */
public class Tmdb extends Application {
    /**
     * Tmdb API URL.
     */
    private static final String MOVIE_SERVICE_URL = "https://api.themoviedb.org/3";
    /**
     * Format for decoding JSON dates in string format.
     */
    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");

    /**
     * API key query parameter name.
     * This has to be appended to every request.
     * Its value is in ApiKey.java, which is not
     * in my git repo and thus will never
     * be shared publicly
     */
    private final String TAG = getClass().getSimpleName();
    private static final String PARAM_API_KEY = "api_key";
    private boolean isDebug;
    private RestAdapter mRestAdapter;
    private MovieService mMovieService;


    /**
     * A few image size constants selected from
     * https://api.themoviedb.org/3/configuration?api_key=xxx
     * They're kind of arbitrary,
     * but I don't know how else to pick them.
     * Put them here vs MovieService in case
     * other service might need them.
     */
    public final String IMAGE_POSTER_ORIGINAL = "original";
    public static final String IMAGE_POSTER_XSMALL = "w92";
    public static final String IMAGE_POSTER_SMALL = "w185";
    public static final String IMAGE_POSTER_MED = "w342";
    public static final String IMAGE_POSTER_LARGE = "w500";
    public static final String IMAGE_POSTER_XLARGE = "w780";

    public static String getYoutubeUrl(String source) {
        return ("http://www.youtube.com/watch?v=" + source);
    }

    public static String getYoutubeThumbnail(String source) {
        return ("http://img.youtube.com/vi/" + source + "/mqdefault.jpg");
    }

    public static String getMoviePosterUrl(String mImagePath, String imageSize) {
        return ("https://image.tmdb.org/t/p/" + imageSize + "/" + mImagePath);
    }

    public static String getMovieBackdropUrl(String mImagePath, String imageSize) {
        return ("https://image.tmdb.org/t/p/" + imageSize + "/" + mImagePath);
    }

    /**
     * Create the RestAdapter
     * See https://snow.dog/blog/make-life-easier-retrofit/
     * for error handling code.
     */
    //   private RestAdapter getRestAdapter() {
    @Override
    public void onCreate() {
        super.onCreate();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapter(Date.class, new JsonDateDeserializer())
                .create();

        RestAdapter.Builder builder = restAdapterBuilder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(MOVIE_SERVICE_URL)
                .setErrorHandler(new ApiErrorHandler(getApplicationContext()))
                .setRequestInterceptor(new RequestInterceptor() {
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
                        requestFacade.addQueryParam(PARAM_API_KEY, "ce554ff7189861a8ef07517840922909");
                    }
                });
        if (isDebug) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        mRestAdapter = builder.build();
        mMovieService = mRestAdapter.create(MovieService.class);
    }

    /**
     * Proxy to the Tmdb Movie service.
     * There are other proxies I can add later,
     * like Search and TV.
     * <p/>
     * This should be called one time only in the app
     * and then referenced from other parts of the app.
     */
    public MovieService getMovieService() {
        return mMovieService;
    }

    /**
     * Set RestAdapter log level.
     *
     * @param isDebug true = LogLevel set to FULL
     */
    public void setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        if (mRestAdapter != null) {
            mRestAdapter.setLogLevel(isDebug ? RestAdapter.LogLevel.FULL
                    : RestAdapter.LogLevel.NONE);
        }
    }

    /**
     * Gson does a terrible job handling dates!!!  It especially hates
     * if date is an empty string "".
     */
    private class JsonDateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            try {
                return JSON_STRING_DATE.parse(json.getAsString());
            } catch (ParseException e) {
                Log.d(TAG, "JSon date is NULL!!");
                return null;
            }
        }
    }

    /**
     * Create a RestAdapterBuilder to help build the adapter
     */
    private RestAdapter.Builder restAdapterBuilder() {
        return new RestAdapter.Builder();
    }

    /**
     * Use this class for debugging json syntax errors,
     * UNFORTUNATELY, when using this class,
     * calling mTmdbManager.setIsDebug(false)
     * causes an internal error on Retrofit calls.
     * <p/>
     * Usage in RestAdapter class below:
     * Replace
     * builder.setConverter(new GsonConverter(gson));
     * with
     * builder.setConverter(new DebugGsonConverter(gson));
     */
    private class DebugGsonConverter extends GsonConverter {
        public DebugGsonConverter(Gson gson) {
            super(gson);
        }

        @Override
        public Object fromBody(TypedInput body, Type type) throws ConversionException {
            try {
                byte[] buffer = new byte[(int) body.length()];
                body.in().read(buffer);
                body.in().reset();
            } catch (IOException e) {
                throw new ConversionException(e);
            }
            return super.fromBody(body, type);
        }
    }
}
