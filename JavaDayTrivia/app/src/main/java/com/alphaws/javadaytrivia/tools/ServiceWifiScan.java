package com.alphaws.javadaytrivia.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.alphaws.javadaytrivia.R;
import com.alphaws.javadaytrivia.beans.Beacon;
import com.alphaws.javadaytrivia.database.BeaconControl;
import com.alphaws.javadaytrivia.database.DataBaseHandler;
import com.alphaws.javadaytrivia.gui.ActivityMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by carlo_000 on 27/08/2015.
 */
public class ServiceWifiScan extends Service{

    private WifiManager wifiManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private BeaconControl beaconControl;
    private DataBaseHandler dataBaseHandler;
    private ArrayList<Beacon> listaBeacons;
    private boolean searchWiFi = true;
    private Context context;
    private int beaconsfound = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        context = getApplicationContext();
        try {
            while(searchWiFi) {
                List<ScanResult> scanResults = wifiManager.getScanResults();
                Log.v("SERVICE WIFI", scanResults.toString());
                for (Iterator<ScanResult> i = scanResults.iterator(); i.hasNext(); ) {
                    ScanResult scanResult = i.next();
                    switch (scanResult.BSSID) {
                        case Commons.WIFIASBEACON1:
                            handleWifiAsBeacon(context, new Beacon(Commons.MAJOR_1, Commons.MINOR_1));
                            beaconsfound++;
                            break;
                        case Commons.WIFIASBEACON2:
                            handleWifiAsBeacon(context, new Beacon(Commons.MAJOR_2, Commons.MINOR_2));
                            beaconsfound++;
                            break;
                        case Commons.WIFIASBEACON3:
                            handleWifiAsBeacon(context, new Beacon(Commons.MAJOR_3, Commons.MINOR_3));
                            beaconsfound++;
                            break;
                        case Commons.WIFIASBEACON4:
                            handleWifiAsBeacon(context, new Beacon(Commons.MAJOR_4, Commons.MINOR_4));
                            beaconsfound++;
                            break;
                        default:
                            break;
                    }
                }
                if(beaconsfound >= 4)
                    searchWiFi = false;
                wifiManager.startScan();
                SystemClock.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void handleWifiAsBeacon(Context context, Beacon beacon) {
        beaconControl = new BeaconControl(this);
        dataBaseHandler = new DataBaseHandler(this);
        listaBeacons = beaconControl.getAllBeacons(dataBaseHandler);
        for(Iterator<Beacon> i = listaBeacons.iterator(); i.hasNext(); ) {
            Beacon item = i.next();
            Log.v("WIFIASBEACON", "ITEM: " + item.toString());
            if (beacon.equals(item)) {
                if (item.getSeen() != 1) {
                    beacon.setSeen(1);
                    beacon.setPlace(item.getPlace());
                    Log.v("WIFIASBEACON", "BEACON UPDATE: " + beacon.toString());
                    try {
                        beaconControl.updateBeacon(beacon, dataBaseHandler);
                    }catch (Exception e) {
                        Log.v("WIFIASBEACON", e.getMessage());
                    }
                    String message;
                    int origin;
                    switch (item.getPlace()) {
                        case 1:
                            message = context.getResources().getString(R.string.activity_message_welcome) + " " + context.getResources().getString(R.string.activity_message_javaDev);
                            setBeaconAlreadyFound(1);
                            origin = 1;
                            Intent intent1 = new Intent();
                            intent1.setAction("com.hello.action");
                            intent1.putExtra(Commons.WHERE_AM_I, 1);
                            context.sendBroadcast(intent1);
                            break;
                        case 2:
                            message = context.getResources().getString(R.string.activity_message_welcome) + " " + context.getResources().getString(R.string.activity_message_auditorium);
                            setBeaconAlreadyFound(2);
                            origin = 2;
                            Intent intent2 = new Intent();
                            intent2.setAction("com.hello.action");
                            intent2.putExtra(Commons.CONFERENCES, 2);
                            context.sendBroadcast(intent2);
                            break;
                        case 3:
                            message = context.getResources().getString(R.string.activity_message_welcomeAndroid) + " " + context.getResources().getString(R.string.activity_message_android);
                            setBeaconAlreadyFound(3);
                            origin = 3;
                            Intent intent3 = new Intent();
                            intent3.setAction("com.hello.action");
                            intent3.putExtra(Commons.ANDROID, 3);
                            context.sendBroadcast(intent3);
                            break;
                        default:
                            message = context.getResources().getString(R.string.activity_message_welcomeCafeteria) + " " + context.getResources().getString(R.string.activity_message_cafeteria);
                            setBeaconAlreadyFound(4);
                            origin = 4;
                            Intent intent4 = new Intent();
                            intent4.setAction("com.hello.action");
                            intent4.putExtra(Commons.FOOD, 4);
                            context.sendBroadcast(intent4);
                            break;
                    }
                    sendNotification(context, context.getResources().getString(R.string.app_name2), message, origin);
                    break;
                } else
                    break;
            }
        }
    }

    private void sendNotification(Context context, String title, String message, int origin){
        int mNotificationId;
        Intent intent;
        intent = new Intent(context, ActivityMessage.class);
        intent.putExtra(Commons.FROMNOTIFICATION, true);
        switch (origin) {
            case 1:
                mNotificationId = 100;
                intent.putExtra(Commons.WHERE_AM_I, 1);
                break;
            case 2:
                mNotificationId = 200;
                intent.putExtra(Commons.CONFERENCES, 2);
                break;
            case 3:
                mNotificationId = 300;
                intent.putExtra(Commons.ANDROID, 3);
                break;
            default:
                mNotificationId = 400;
                intent.putExtra(Commons.FOOD, 4);
                break;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, mNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        pendingIntent = PendingIntent.getActivity(context, mNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification;
        if (Build.VERSION.SDK_INT < 11) {
            notification = new Notification(R.mipmap.ic_launcher, title, System.currentTimeMillis());
            notification.setLatestEventInfo(context, title, message, pendingIntent);
        } else {
            notification = new NotificationCompat.Builder(context)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.beaconio_blue)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntent).setWhen(System.currentTimeMillis()).setAutoCancel(true).build();
        }
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        try{
            notification.defaults |= Notification.DEFAULT_SOUND;
        }catch(Exception e){}
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, notification);
    }

    private void setBeaconAlreadyFound(int origin){
        sharedPreferences = context.getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        switch(origin) {
            case 1:
                editor.putBoolean(Commons.WHERE_AM_I_BEACON_FOUND, true);
                break;
            case 2:
                editor.putBoolean(Commons.CONFERENCES_BEACON_FOUND, true);
                break;
            case 3:
                editor.putBoolean(Commons.ANDROID_BEACON_FOUND, true);
                break;
            case 4:
                editor.putBoolean(Commons.FOOD_BEACON_FOUND, true);
                break;
            default:
                break;
        }
        editor.apply();
        sharedPreferences = null;
        editor = null;
    }

}
