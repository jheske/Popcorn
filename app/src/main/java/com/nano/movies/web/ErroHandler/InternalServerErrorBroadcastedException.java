package com.nano.movies.web.ErroHandler;

import android.content.Context;

import retrofit.RetrofitError;

/**
 * Created by jill on 9/6/2015.
 */
public class InternalServerErrorBroadcastedException extends BroadcastedException {
    public static final String TAG = "InternalServerErrorBroadcastedException";

    public InternalServerErrorBroadcastedException(Context ctx, RetrofitError cause) {
        super(ctx, cause, EXCEPTION_TYPE.INTERNAL_SERVER);
        //process internal server error - inform user that server is unable to process request due to internal error?
        // . . .
    }
}
