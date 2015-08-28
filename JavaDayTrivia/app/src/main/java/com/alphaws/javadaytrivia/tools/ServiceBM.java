package com.alphaws.javadaytrivia.tools;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alphaws.javadaytrivia.R;
import com.alphaws.javadaytrivia.beans.Beacon;
import com.alphaws.javadaytrivia.database.BeaconControl;
import com.alphaws.javadaytrivia.database.DataBaseHandler;
import com.alphaws.javadaytrivia.gui.ActivityMessage;
import com.alphaws.javadaytrivia.gui.BroadcastRemove;
import com.kontakt.sdk.android.ble.configuration.ActivityCheckConfiguration;
import com.kontakt.sdk.android.ble.configuration.ForceScanConfiguration;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.configuration.scan.IBeaconScanContext;
import com.kontakt.sdk.android.ble.configuration.scan.ScanContext;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.discovery.BluetoothDeviceEvent;
import com.kontakt.sdk.android.ble.discovery.DistanceSort;
import com.kontakt.sdk.android.ble.discovery.EventType;
import com.kontakt.sdk.android.ble.discovery.ibeacon.IBeaconDeviceEvent;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.rssi.RssiCalculators;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class ServiceBM extends Service {

	public static ProximityManager proximityManager;
	private ScanContext scanContext;
	private ArrayList<Beacon> listaBeacons;
	private List<IBeaconDevice> iBeaconDevices;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private BeaconControl beaconControl;
	private DataBaseHandler dataBaseHandler;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		beaconControl = new BeaconControl(getApplicationContext());
		dataBaseHandler = new DataBaseHandler(getApplicationContext());
		if(sharedPreferences.contains(Commons.BEACONSINDATABASE)) {
			listaBeacons = beaconControl.getAllBeacons(dataBaseHandler);
		}else {
		 	setBeacons();
			editor.putBoolean(Commons.BEACONSINDATABASE, true);
			editor.apply();
			listaBeacons = beaconControl.getAllBeacons(dataBaseHandler);
		}
		scanContext = new ScanContext.Builder()
				.setScanMode(ProximityManager.SCAN_MODE_BALANCED)
				.setScanPeriod(new ScanPeriod(TimeUnit.SECONDS.toMillis(3), 0))
				.setForceScanConfiguration(ForceScanConfiguration.DEFAULT)
				.setActivityCheckConfiguration(ActivityCheckConfiguration.DEFAULT)
						.setIBeaconScanContext(new IBeaconScanContext.Builder()
										.setRssiCalculator(RssiCalculators.newLimitedMeanRssiCalculator(5))
										.setEventTypes(EnumSet.of(EventType.DEVICE_DISCOVERED))
										.setDevicesUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(2))
										.setDistanceSort(DistanceSort.DESC)
								.build()
				).build();
		proximityManager = new ProximityManager(getApplicationContext());
		proximityManager.initializeScan(scanContext, new OnServiceReadyListener() {
			@Override
			public void onServiceReady() {
				proximityManager.attachListener(new ProximityManager.ProximityListener() {

					@Override
					public void onScanStart() {

					}

					@Override
					public void onScanStop() {

					}

					@Override
					public void onEvent(BluetoothDeviceEvent event) {

						switch (event.getDeviceProfile()) {
							case IBEACON:
								final IBeaconDeviceEvent iBeaconDeviceEvent = (IBeaconDeviceEvent) event;
								iBeaconDevices = iBeaconDeviceEvent.getDeviceList();
								for(Iterator<IBeaconDevice> j = iBeaconDevices.iterator(); j.hasNext(); ) {
									IBeaconDevice iBeaconDevice = j.next();
									if (iBeaconDevice.getProximityUUID().toString().equals(Commons.BEACONS_UUID_AWS)) {
										Beacon beacon = new Beacon(String.valueOf(iBeaconDevice.getMajor()), String.valueOf(iBeaconDevice.getMinor()));
										Log.v("BROADCAST", beacon.toString());
										for (Iterator<Beacon> i = listaBeacons.iterator(); i.hasNext(); ) {
											Beacon item = i.next();
											Log.v("BROADCAST", "ITEM: " + item.toString());
											if (beacon.equals(item)) {
												if (item.getSeen() != 1) {
													beacon.setSeen(1);
													beacon.setPlace(item.getPlace());
													Log.v("BROADCAST", "BEACON UPDATE: " + beacon.toString());
													try {
														beaconControl.updateBeacon(beacon, dataBaseHandler);
														listaBeacons = null;
														listaBeacons = beaconControl.getAllBeacons(dataBaseHandler);
													}catch (Exception e) {
														Log.v("SERVICE", e.getMessage());
													}
													String message;
													int origin;
													switch (item.getPlace()) {
														case 1:
															message = getResources().getString(R.string.activity_message_welcome) + " " + getResources().getString(R.string.activity_message_javaDev);
															setBeaconAlreadyFound(1);
															origin = 1;
															Intent intent1 = new Intent();
															intent1.setAction("com.hello.action");
															intent1.putExtra(Commons.WHERE_AM_I, 1);
															getApplicationContext().sendBroadcast(intent1);
															break;
														case 2:
															message = getResources().getString(R.string.activity_message_welcome) + " " + getResources().getString(R.string.activity_message_auditorium);
															setBeaconAlreadyFound(2);
															origin = 2;
															Intent intent2 = new Intent();
															intent2.setAction("com.hello.action");
															intent2.putExtra(Commons.CONFERENCES, 2);
															getApplicationContext().sendBroadcast(intent2);
															break;
														case 3:
															message = getResources().getString(R.string.activity_message_welcomeAndroid) + " " + getResources().getString(R.string.activity_message_android);
															setBeaconAlreadyFound(3);
															origin = 3;
															Intent intent3 = new Intent();
															intent3.setAction("com.hello.action");
															intent3.putExtra(Commons.ANDROID, 3);
															getApplicationContext().sendBroadcast(intent3);
															break;
														default:
															message = getResources().getString(R.string.activity_message_welcomeCafeteria) + " " + getResources().getString(R.string.activity_message_cafeteria);
															setBeaconAlreadyFound(4);
															origin = 4;
															Intent intent4 = new Intent();
															intent4.setAction("com.hello.action");
															intent4.putExtra(Commons.FOOD, 4);
															getApplicationContext().sendBroadcast(intent4);
															break;
													}
													sendNotification(getResources().getString(R.string.app_name2), message, origin);
													break;
												} else
													break;
											}
										}
									}

								}
								break;
							default:
								break;
						}
					}
				});
			}

			@Override
			public void onConnectionFailure() {

			}
		});

		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void setBeacons() {
		BeaconControl beaconControl = new BeaconControl(getApplicationContext());
		long inserted;
		DataBaseHandler dataBaseHandler = new DataBaseHandler(getApplicationContext());
		inserted = beaconControl.addBeacon(new Beacon(Commons.MAJOR_1, Commons.MINOR_1, 1), dataBaseHandler);
		Log.v("BROADCAST", "INSERTED1: " + String.valueOf(inserted));
		inserted = beaconControl.addBeacon(new Beacon(Commons.MAJOR_2, Commons.MINOR_2, 2), dataBaseHandler);
		Log.v("BROADCAST", "INSERTED2: " + String.valueOf(inserted));
		inserted = beaconControl.addBeacon(new Beacon(Commons.MAJOR_3, Commons.MINOR_3, 3), dataBaseHandler);
		Log.v("BROADCAST", "INSERTED3: " + String.valueOf(inserted));
		inserted = beaconControl.addBeacon(new Beacon(Commons.MAJOR_4, Commons.MINOR_4, 4), dataBaseHandler);
		Log.v("BROADCAST", "INSERTED4: " + String.valueOf(inserted));
	}

	private void sendNotification(String title, String message, int origin){
		int mNotificationId;
		Intent intent;
		intent = new Intent(getApplicationContext(), ActivityMessage.class);
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
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), mNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		pendingIntent.cancel();
		pendingIntent = PendingIntent.getActivity(getApplicationContext(), mNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification;
		if (Build.VERSION.SDK_INT < 11) {
			notification = new Notification(R.mipmap.ic_launcher, title, System.currentTimeMillis());
			notification.setLatestEventInfo(getApplicationContext(), title, message, pendingIntent);
		} else {
			PendingIntent deleteIntent = PendingIntent.getBroadcast(getApplicationContext(), mNotificationId + 1000, new Intent(getApplicationContext(), BroadcastRemove.class).putExtra((mNotificationId == 100) ?
					Commons.REMOVED_WHERE : (mNotificationId == 200) ? Commons.REMOVED_ARRUPE : (mNotificationId == 300) ? Commons.REMOVED_ANDROID : Commons.REMOVED_CAFETERIA, true), 0);
			deleteIntent.cancel();
			deleteIntent = PendingIntent.getBroadcast(getApplicationContext(), mNotificationId + 1000, new Intent(getApplicationContext(), BroadcastRemove.class).putExtra((mNotificationId == 100) ?
					Commons.REMOVED_WHERE : (mNotificationId == 200) ? Commons.REMOVED_ARRUPE : (mNotificationId == 300) ? Commons.REMOVED_ANDROID : Commons.REMOVED_CAFETERIA, true), 0);
			notification = new NotificationCompat.Builder(getApplicationContext())
					.setPriority(NotificationCompat.PRIORITY_HIGH)
					.setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.beaconio_blue)
					.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
					.setContentIntent(pendingIntent).setDeleteIntent(deleteIntent).setWhen(System.currentTimeMillis()).setAutoCancel(true).build();
		}
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		try{
			notification.defaults |= Notification.DEFAULT_SOUND;
		}catch(Exception e){}
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, notification);
	}

	private void setBeaconAlreadyFound(int origin){
		sharedPreferences = getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
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
	}

}
