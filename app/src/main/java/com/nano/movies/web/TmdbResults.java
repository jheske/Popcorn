/**
 * Created by Jill Heske
 *
 * Copyright(c) 2015
 */
package com.nano.movies.web;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jill on 7/25/2015.
 *
 * Result of requesting a list of movie summaries from
 * Tmdb api. Summaries are missing a lot of interesting
 * details, like reviews, releases, and trailers, which will
 * have to be filled in by a separate API call when user
 * requests movie details.
 * I can't figure out whether I should use a serialVersionUID
 */
public class TmdbResults implements Serializable {
    public Integer page;
    public List<Movie> results;
    public Integer total_pages;
    public Integer total_results;
}
