package com.nano.movies.web.ErroHandler;

import android.content.Context;
import android.util.Log;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jill on 9/6/2015.
 *
 */
public class ApiErrorHandler implements ErrorHandler {
    public static final String TAG = "MyErrorHandler";

    private Context mContext;


    public ApiErrorHandler(Context ctx) {
        mContext = ctx;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        if (cause != null) {
            switch (cause.getKind()) {
                case NETWORK:
                    return new NetworkBroadcastedException(mContext, cause);
                default:
                    Response r = cause.getResponse();
                    if (r == null) return cause;
                    if (r.getStatus() == 401) {
                        return new UnauthorizedBroadcastedException(mContext, cause);
                    } else if (r.getStatus() == 403) {
                        return new ForbiddenBroadcastedException(mContext, cause);
                    } else if (r.getStatus() >= 500) {
                        return new InternalServerErrorBroadcastedException(mContext, cause);
                    } else if (r.getStatus() == 404) {
                        Log.e(TAG, "error 404");
                        return cause;
                    }
            }
        }
        return cause;
    }
}