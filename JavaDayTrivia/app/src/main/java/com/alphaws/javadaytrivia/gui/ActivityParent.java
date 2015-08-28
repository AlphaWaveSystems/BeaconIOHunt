package com.alphaws.javadaytrivia.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.alphaws.javadaytrivia.R;
import com.alphaws.javadaytrivia.tools.Commons;
import com.alphaws.javadaytrivia.tools.ConnectionDetector;


/**
 * Created by Carlos Alexis on 09/07/2015.
 */
public class ActivityParent extends Activity {

    public ConnectionDetector connectionDetector;
    protected static final int WIFI_ON = 500;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setMessageAlredyShown(int origin){
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch(origin) {
            case 1:
                editor.putBoolean(Commons.WHERE_AM_I, true);
                break;
            case 2:
                editor.putBoolean(Commons.CONFERENCES, true);
                break;
            case 3:
                editor.putBoolean(Commons.ANDROID, true);
                break;
            case 4:
                editor.putBoolean(Commons.FOOD, true);
                break;
            default:
                break;
        }
        editor.commit();
    }

    public boolean hasBeenShown(String origin){
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(origin))
            return true;
        else
            return false;
    }

    public void setCorrectAnswer(int origin){
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch(origin) {
            case 1:
                editor.putBoolean(Commons.WHERE_AM_I_CORRECT, true);
                break;
            case 2:
                editor.putBoolean(Commons.CONFERENCES_CORRECT, true);
                break;
            case 3:
                editor.putBoolean(Commons.ANDROID_CORRECT, true);
                break;
            case 4:
                editor.putBoolean(Commons.FOOD_CORRECT, true);
                break;
            default:
                break;
        }
        editor.commit();
    }

    public boolean hasBeenAnswered(String origin){
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(origin))
            return true;
        else
            return false;
    }

    public void setServiceStarted() {
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Commons.SERVICE_SATARTED, true);
        editor.commit();
    }

    public boolean hasServiceBeenStarted(String key) {
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(key))
            return true;
        else
            return false;
    }

    public void setLogged() {
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Commons.LOGIN, true);
        editor.commit();
        Commons.isLogged = true;
    }

    public boolean isLogged(String key) {
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(key))
            return true;
        else
            return false;
    }

    public void setBeaconsInDatabase() {
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Commons.BEACONSINDATABASE, true);
        editor.commit();
    }

    public boolean areBeaconsInDatabase() {
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(Commons.BEACONSINDATABASE))
            return true;
        else
            return false;
    }

    public boolean beaconHasBeenFound(String origin){
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(origin))
            return true;
        else
            return false;
    }

    public void setUpdateNotification(int origin) {
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch(origin) {
            case 1:
                editor.putBoolean(Commons.WHERE_AM_I_UPDATE, true);
                break;
            case 2:
                editor.putBoolean(Commons.CONFERENCES_UPDATE, true);
                break;
            case 3:
                editor.putBoolean(Commons.ANDROID_UPDATE, true);
                break;
            case 4:
                editor.putBoolean(Commons.FOOD_UPDATE, true);
                break;
            default:
                break;
        }
        editor.commit();
    }

    public boolean getUpdateNotification(String key) {
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(key))
            return true;
        else
            return false;
    }

    public boolean getAlreadyParticipating(String origin){
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(origin))
            return true;
        else
            return false;
    }

    public void setAlreadyParticipating(){
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Commons.PARTICIPATING, true);
        editor.commit();
    }

    public void setServiceWiFiStarted() {
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Commons.SERVICEWiFi_SATARTED, true);
        editor.commit();
    }

    public boolean hasServiceWiFiBeenStarted(String key) {
        sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(key))
            return true;
        else
            return false;
    }

    /**
     * Validate if is Internet available
     *
     * @return
     */
    public boolean isInternetAvailability(boolean isAsyncTask, int requestCode) {

        connectionDetector = new ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            return true;
        } else {
            if (!isAsyncTask) {
                enableConnectivity(requestCode);
            }
            return false;
        }
    }

    /**
     * Enable Internet connectivity
     */
    public void enableConnectivity(int requestCodeIn) {
        final int requestCode = requestCodeIn;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);
        alert.setTitle(getString(R.string.activity_parent_connectivity_title));
        alert.setMessage(getString(R.string.activity_parent_connectivity_detail));
        alert.setPositiveButton(R.string.activity_parent_connectivity_ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                intent.setComponent(cn);
                startActivityForResult(intent, requestCode);
            }

        });
        alert.setNegativeButton(R.string.activity_parent_connectivity_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alert.show();
    }

    public void sendNotificationUpdate(String title, String message, int origin, int destiny) {
        int mNotificationId;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ActivityQuestion.class);
        switch (origin) {
            case 1:
                intent.putExtra(Commons.WHERE_AM_I, 1);
                mNotificationId = 100;
                break;
            case 2:
                intent.putExtra(Commons.CONFERENCES, 2);
                mNotificationId = 200;
                break;
            case 3:
                intent.putExtra(Commons.ANDROID, 3);
                mNotificationId = 300;
                break;
            default:
                intent.putExtra(Commons.FOOD, 4);
                mNotificationId = 400;
                break;
        }
        if(destiny == 2) {
            mNotifyMgr.cancel(mNotificationId);
            setUpdateNotification(mNotificationId / 100);
            return;
        }
        intent.putExtra(Commons.FROMNOTIFICATION, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if ((mNotificationId == 100 && !getUpdateNotification(Commons.REMOVED_WHERE)) || (mNotificationId == 200 && !getUpdateNotification(Commons.REMOVED_ARRUPE)) || (mNotificationId == 300 && !getUpdateNotification(Commons.REMOVED_ANDROID)) || (mNotificationId == 400 && !getUpdateNotification(Commons.REMOVED_CAFETERIA))) {
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), mNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), mNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification;
            if (Build.VERSION.SDK_INT < 11) {
                notification = new Notification(R.mipmap.ic_launcher, title, System.currentTimeMillis());
                notification.setLatestEventInfo(getApplicationContext(), title, message, pendingIntent);
            } else {
                notification = new NotificationCompat.Builder(getApplicationContext())
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.beaconio_blue)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(pendingIntent).setWhen(System.currentTimeMillis()).setAutoCancel(true).build();
            }
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, notification);
        }

    }
}
