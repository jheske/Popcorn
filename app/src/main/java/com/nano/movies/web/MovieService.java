/**
 * Created by Jill Heske
 *
 * Copyright(c) 2015
 */
package com.nano.movies.web;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Retrofit Service to send requests to TheMovieDatabase (Tmdb) web service and
 * convert the Json response to POJO class.
 *
 * For now put everything in here I think I might need for P1, P2, etc.
 *
 */
public interface MovieService {
    String POPULARITY_DESC = "popularity.desc";
    String VOTE_AVERAGE_DESC = "vote_average.desc";
    String TRAILERS = "trailers";
    //For later use
    String PARAM_APPEND = "append_to_response";
    String RELEASES = "releases";
    String REVIEWS = "reviews";
    String RELEASES_AND_TRAILERS = "releases,trailers";
    String REVIEWS_AND_TRAILERS = "reviews,trailers";

    /**
     * Get basic movie information for specific movie.
     *
     * @param tmdbId Tmdb-assigned Unique movie id
     */
    @GET("/movie/{id}")
    void summary(
            @Path("id") int tmdbId,
            @Query("append_to_response") String appendToResponse,
            Callback<Movie> resultsCallback
    );

    /**
     * Get list of top-rated movies (10 or more votes)
     */
    @GET("/movie/top_rated")
    TmdbResults topRated();

    /**
     * Get the cast and crew information for a specific movie id.
     *
     * @param tmdbId TMDb id.

    @GET("/movie/{id}/credits")
    Credits credits(
            @Path("id") int tmdbId
    );
     */


    /**
     * Get list of movies that have 10
     * or more votes.
     *
     * @param page     integer >= 1
     * @param language optional (use language code)
     */
    @GET("/movie/top_rated")
    TmdbResults topRated(
            @Query("page") Integer page,
            @Query("language") String language
    );

    @GET("/discover/movie")
    void discoverMovies(@Query("page") int page,
                        @Query("sort_by") String sort_by,
                   Callback<TmdbResults> resultsCallback);

    //Get Movie's list of Mpaa ratings by country
    //We only want USA, but got to get them all
    @GET("/movie/{id}/releases")
    void releases(
            @Path("id") int tmdbId,
            Callback<Releases> resultsCallback
    );
/*
    @GET("/certification/movie/list")
    void certifications(
            @Path("id") int tmdbId,
            @Query("certification_country") String country
            Callback<Certifications> resultsCallback
    ); */
}