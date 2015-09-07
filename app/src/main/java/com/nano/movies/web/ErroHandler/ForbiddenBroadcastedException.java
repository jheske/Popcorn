package com.nano.movies.web.ErroHandler;

import android.content.Context;

import retrofit.RetrofitError;

/**
 * Created by jill on 9/6/2015.
 */
public class ForbiddenBroadcastedException extends BroadcastedException {
    public static final String TAG = "ForbiddenBroadcastedException";

    public ForbiddenBroadcastedException(Context ctx, RetrofitError cause) {
        super(ctx, cause, EXCEPTION_TYPE.FORBIDDEN);
        //process forbidden error - inform user that (s)he can’t access this content?
        // . . .

    }
}
