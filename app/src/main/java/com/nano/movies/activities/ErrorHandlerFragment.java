package com.nano.movies.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.nano.movies.R;
import com.nano.movies.utils.Utils;
import com.nano.movies.web.ErroHandler.BroadcastedException;
import com.nano.movies.web.ErroHandler.IBroadcastedExceptionListener;
import com.nano.movies.web.ErroHandler.INetworkExceptionListener;

import butterknife.BindString;

/**
 * Created by jill on 9/6/2015.
 * Adapted from https://snow.dog/blog/make-life-easier-retrofit/
 * by Wojtek Tyrchan
 */
public abstract class ErrorHandlerFragment extends Fragment
        implements IBroadcastedExceptionListener, INetworkExceptionListener {

    public final String TAG = getClass().getSimpleName();
    protected BroadcastedExceptionReceiver broadcastedExceptionReceiver;
    @BindString(R.string.error_retrofit_unauthorized_exception)
    String errorUnauthorizedException;
    @BindString(R.string.error_retrofit_unhandled_exception)
    String errorUnhandledException;
    @BindString(R.string.error_retrofit_network_exception)
    String errorNetworkException;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastedExceptionReceiver = new BroadcastedExceptionReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastedExceptionReceiver,
                new IntentFilter(BroadcastedException.ACTION_BROADCASTED_EXCEPTION));

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastedExceptionReceiver);
    }

    @Override
    public void processNetworkException(String message) {
        Log.e(TAG, "Network exception: " + message);
        Utils.showToast(getContext(), message);
    }

    @Override
    public void processException(BroadcastedException.EXCEPTION_TYPE type, String message) {
        switch(type) {
            case NETWORK:
                processNetworkException(message);
                break;
            case UNAUTHORIZED:
                Utils.showToast(getContext(),message);
                Log.d(TAG, errorUnauthorizedException + message);
                break;
            default:
                Utils.showToast(getContext(),message);
                Log.d(TAG, errorUnhandledException + " " + message);
                break;
        }
    }

    public class BroadcastedExceptionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null) {
                String message = intent.getStringExtra(BroadcastedException.EXTRA_MESSAGE);
                int nType = intent.getIntExtra(
                        BroadcastedException.EXTRA_TYPE,
                        BroadcastedException.EXCEPTION_TYPE.UNKNOWN.ordinal());
                BroadcastedException.EXCEPTION_TYPE type =
                        BroadcastedException.EXCEPTION_TYPE.getByOrdinal(nType);
                processException(type, message);
            }
        }
    }
}