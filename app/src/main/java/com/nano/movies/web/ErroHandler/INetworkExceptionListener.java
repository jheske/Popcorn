package com.nano.movies.web.ErroHandler;

/**
 * Created by jill on 9/6/2015.
 * Adapted from https://snow.dog/blog/make-life-easier-retrofit/
 * by Wojtek Tyrchan
 */

public interface INetworkExceptionListener {
    void processNetworkException(String message);
}