package com.nano.movies.web.ErroHandler;

import android.content.Context;

import retrofit.RetrofitError;

/**
 * Created by jill on 9/6/2015.
 */
public class UnauthorizedBroadcastedException extends BroadcastedException {
    public static final String TAG = "UnauthorizedBroadcastedException";

    public UnauthorizedBroadcastedException(Context ctx, RetrofitError cause) {
        super(ctx, cause, EXCEPTION_TYPE.UNAUTHORIZED);
        //process unauthorized error - logout user/force to login again?
        // . . .
    }
}
