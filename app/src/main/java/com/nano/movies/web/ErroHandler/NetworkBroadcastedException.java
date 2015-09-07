package com.nano.movies.web.ErroHandler;

import android.content.Context;

import retrofit.RetrofitError;

/**
 * Created by jill on 9/6/2015.
 */
public class NetworkBroadcastedException extends BroadcastedException {
    public static final String TAG = "NetworkBroadcastedException";

    public NetworkBroadcastedException(Context ctx, RetrofitError cause) {
        super(ctx, cause, EXCEPTION_TYPE.NETWORK);
    }
}
