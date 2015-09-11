package com.nano.movies.web.ErroHandler;

/**
 * Created by jill on 9/6/2015.
 */

public interface IBroadcastedExceptionListener {
    void processException(BroadcastedException.EXCEPTION_TYPE type, String message);
}