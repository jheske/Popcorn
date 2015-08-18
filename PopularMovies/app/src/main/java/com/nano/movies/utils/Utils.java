/**
 * Created by Jill Heske
 *
 * Copyright(c) 2015
 */
package com.nano.movies.utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {

    /**
     * Show a toast message.
     */
    public static void showToast(Context context,
                                 String message) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Make Utils a utility class by preventing instantiation.
     */
    private Utils() {
        throw new AssertionError();
    }
}
