package com.autoban.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

    public static String toDateNow() {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:SS.sss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
