package com.alphaws.javadaytrivia.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.bluetooth.BluetoothAdapter;

import com.alphaws.javadaytrivia.tools.Commons;
import com.alphaws.javadaytrivia.tools.ServiceBM;

public class BroadcastReceiverBeacon extends BroadcastReceiver {

	private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();

		if (Commons.isLogged) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
				// It means the user has changed his bluetooth state.
				if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

					if (btAdapter.getState() == BluetoothAdapter.STATE_TURNING_OFF) {
						Log.v("BROADCAST", "Se esta apagando");
						try {
							ServiceBM.proximityManager.finishScan();
							ServiceBM.proximityManager.disconnect();
							ServiceBM.proximityManager = null;
						} catch (Exception e) {
							Log.v("BROADCAST", "ERROR AL DESCONECTAR BeaconManager");
						}
					} else if (btAdapter.getState() == BluetoothAdapter.STATE_OFF) {
						Log.v("BROADCAST", "Apagado");
					} else if (btAdapter.getState() == BluetoothAdapter.STATE_ON) {
						Log.v("BROADCAST", "Prendido");
						Intent service = new Intent(context, ServiceBM.class);
						context.startService(service);
						Log.v("BROADCAST", "Service");
					} else if (btAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON) {
						Log.v("BROADCAST", "Se esta prendiendo");
					}

				}
			}
		}
	}

}