package com.alphaws.javadaytrivia.tools;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class Commons {

    public static String CARRIER;
    public static final String BEACONS_UUID_AWS = "f7826da6-4fa2-4e98-8024-bc5b71e0893e";
    public static final String MY_PREFERENCES = "JavaDevDayPreferences";
    public static final String WHERE_AM_I = "WHERE_AM_I";
    public static final String CONFERENCES = "CONFERENCES";
    public static final String ANDROID = "ANDROID";
    public static final String FOOD = "FOOD";
    public static final String WHERE_AM_I_BEACON_FOUND = "WHERE_AM_I_BEACON_FOUND";
    public static final String CONFERENCES_BEACON_FOUND = "CONFERENCES_BEACON_FOUND";
    public static final String ANDROID_BEACON_FOUND = "ANDROID_BEACON_FOUND";
    public static final String FOOD_BEACON_FOUND = "FOOD_BEACON_FOUND";
    public static final String WHERE_AM_I_CORRECT = "WHERE_AM_I_CORRECT";
    public static final String CONFERENCES_CORRECT = "CONFERENCES_CORRECT";
    public static final String ANDROID_CORRECT = "ANDROID_CORRECT";
    public static final String FOOD_CORRECT = "FOOD_CORRECT";
    public static final String SERVICE_SATARTED = "SERVICE_SATARTED";
    public static final String LOGIN = "LOGIN";
    public static final String BEACONSINDATABASE = "BEACONSINDATABASE";
    public static final String MAJOR_1 = "25030";
    public static final String MINOR_1 = "2941";
    public static final String MAJOR_2 = "28692";
    public static final String MINOR_2 = "57454";
    public static final String MAJOR_3 = "46115";
    public static final String MINOR_3 = "42364";
    public static final String MAJOR_4 = "15442";
    public static final String MINOR_4 = "29258";
    public static final String FROMNOTIFICATION = "FROMNOTIFICATION";
    public static final String WHERE_AM_I_UPDATE = "WHERE_AM_I_UPDATE";
    public static final String CONFERENCES_UPDATE = "CONFERENCES_UPDATE";
    public static final String ANDROID_UPDATE = "ANDROID_UPDATE";
    public static final String FOOD_UPDATE = "FOOD_UPDATE";
    public static boolean isLogged = false;
    public static final String WIFIASBEACON1 = "0c:27:24:87:39:d0";
    public static final String WIFIASBEACON2 = "0c:27:24:87:d1:c0";
    public static final String WIFIASBEACON3 = "9c:d6:43:d3:42:18";
    public static final String WIFIASBEACON4 = "e8:b7:48:40:52:c2";
    public static final String PARTICIPATING = "PARTICIPATING";
    public static final String REMOVED_WHERE = "REMOVED_WHERE";
    public static final String REMOVED_ARRUPE = "REMOVED_ARRUPE";
    public static final String REMOVED_ANDROID = "REMOVED_ANDROID";
    public static final String REMOVED_CAFETERIA = "REMOVED_CAFETERIA";
    public static final String SERVICEWiFi_SATARTED = "SERVICEWiFi_SATARTED";

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), (int) (bitmap.getHeight()*0.8));
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
