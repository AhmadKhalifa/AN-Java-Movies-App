package com.android.nanodegree.moviesapp.util;

import java.text.SimpleDateFormat;

/**
 * Created by Khalifa on 4/12/2018.
 *
 */
public class DateFormatter {

    private DateFormatter() {

    }

    @SuppressWarnings("all")
    public static String format(String ddMMyyyyFormattedDate) {
        try {
            return new SimpleDateFormat("MMM d, yyyy").format(
                    new SimpleDateFormat("yyyy-MM-dd").parse(ddMMyyyyFormattedDate)
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            return ddMMyyyyFormattedDate;
        }
    }
}
