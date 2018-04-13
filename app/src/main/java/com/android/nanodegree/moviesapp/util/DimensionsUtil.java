package com.android.nanodegree.moviesapp.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.android.nanodegree.moviesapp.MoviesApp;

/**
 * Created by Khalifa on 4/13/2018.
 *
 */
public class DimensionsUtil {

    private static final int TABLET_STARTING_WIDTH = 600;

    private DimensionsUtil(){

    }

    private static int getScreenWidthInDp(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) MoviesApp
                .getApplication()
                .getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowmanager != null) {
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            return Math.round(displayMetrics.widthPixels / displayMetrics.density);
        } else {
            throw new IllegalStateException("Window manager is null!");
        }
    }

    public static boolean isTablet() {
        return getScreenWidthInDp() >= TABLET_STARTING_WIDTH;
    }

    public static int getGridSpanCount(int gritItemWidth) {
        return getScreenWidthInDp() / gritItemWidth;
    }
}