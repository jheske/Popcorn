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
 * The format of results from a call to
 * Tmdb api.
 *
 * I can't figure out whether I should use a serialVersionUID
 */
public class TmdbResults implements Serializable {
    public Integer page;
    public List<Movie> results;
    public Integer total_pages;
    public Integer total_results;
}
