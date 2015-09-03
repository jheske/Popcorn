/**
 * Created by Jill Heske
 * <p/>
 * Copyright(c) 2015
 */
package com.nano.movies.web;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.nano.movies.utils.ApiKey;

import java.io.IOException;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonDeserializationContext;
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
    private static final String PARAM_API_KEY = "api_key";
    private boolean isDebug;
    private RestAdapter restAdapter;
    private final String TAG = getClass().getSimpleName();

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
    public static final String IMAGE_POSTER_LARGE = "w500";
    public static final String IMAGE_POSTER_XLARGE = "w780";

    /**
     * Constructor to create a new manager instance.
     */
    public Tmdb() {
    }

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
     * Set RestAdapter log level.
     *
     * @param isDebug true = LogLevel set to FULL
     */
    public void setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        if (restAdapter != null) {
            restAdapter.setLogLevel(isDebug ? RestAdapter.LogLevel.FULL
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
     *  Use this class for debugging json syntax errors,
     *  UNFORTUNATELY, calling mTmdbManager.setIsDebug(false)
     *  causes an internal errors on Retrofit calls.
     *
     *  Usage in RestAdapter class below:
     *  Replace
     *      builder.setConverter(new GsonConverter(gson));
     *  with
     *      builder.setConverter(new DebugGsonConverter(gson));
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

    /**
     * Create the RestAdapter
     */
    private RestAdapter getRestAdapter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapter(Date.class, new JsonDateDeserializer())
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
     * Proxy to the Tmdb Movie service.
     * There are other proxies I can add later,
     * like Search and TV.
     */
    public MovieServiceProxy moviesServiceProxy() {
        return getRestAdapter().create(MovieServiceProxy.class);
    }
}
