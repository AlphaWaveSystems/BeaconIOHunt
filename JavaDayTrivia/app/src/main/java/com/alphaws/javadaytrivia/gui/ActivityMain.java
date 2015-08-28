package com.alphaws.javadaytrivia.gui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alphaws.javadaytrivia.R;
import com.alphaws.javadaytrivia.beans.User;
import com.alphaws.javadaytrivia.database.DataBaseHandler;
import com.alphaws.javadaytrivia.database.UserControl;
import com.alphaws.javadaytrivia.json.JSonClient;
import com.alphaws.javadaytrivia.tools.Commons;
import com.alphaws.javadaytrivia.tools.ServiceBM;
import com.alphaws.javadaytrivia.tools.ServiceWifiScan;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Carlos Alexis on 09/07/2015.
 */
public class  ActivityMain extends ActivityParent {

    private Button button_Participate, button_whereAmI, button_conferences, button_android, button_food;
    private  ImageView imageView_whereAmI, imageView_conferences, imageView_android, imageView_food;
    private  TextView textView_whereAmI, textView_conferences, textView_android, textView_food;
    private int REQUEST_CODE_ENABLE_BLUETOOTH = 999;
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private BroadcastReceiver updateUIReciver;
    private IntentFilter filter;
    private NotificationManager mNotifyMgr;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_Participate = (Button) findViewById(R.id.activity_main_button_participate);
        button_whereAmI = (Button) findViewById(R.id.activity_main_button_whereAmI);
        button_conferences = (Button) findViewById(R.id.activity_main_button_conferences);
        button_android = (Button) findViewById(R.id.activity_main_button_android);
        button_food = (Button) findViewById(R.id.activity_main_button_food);
        imageView_whereAmI = (ImageView) findViewById(R.id.activity_main_imageView_whereAmI);
        imageView_conferences = (ImageView) findViewById(R.id.activity_main_imageView_conferences);
        imageView_android = (ImageView) findViewById(R.id.activity_main_imageView_android);
        imageView_food = (ImageView) findViewById(R.id.activity_main_imageView_food);
        textView_whereAmI = (TextView) findViewById(R.id.activity_main_textView_whereAmI);
        textView_conferences = (TextView) findViewById(R.id.activity_main_textView_conferences);
        textView_android = (TextView) findViewById(R.id.activity_main_textView_android);
        textView_food = (TextView) findViewById(R.id.activity_main_textView_food);

        filter = new IntentFilter();
        filter.addAction("com.hello.action");
        updateUIReciver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.hasExtra(Commons.WHERE_AM_I)) {
                    imageView_whereAmI.setVisibility(View.GONE);
                    textView_whereAmI.setVisibility(View.GONE);
                    button_whereAmI.setVisibility(View.VISIBLE);
                } else if(intent.hasExtra(Commons.CONFERENCES)) {
                    imageView_conferences.setVisibility(View.GONE);
                    textView_conferences.setVisibility(View.GONE);
                    button_conferences.setVisibility(View.VISIBLE);
                } else if(intent.hasExtra(Commons.ANDROID)) {
                    imageView_android.setVisibility(View.GONE);
                    textView_android.setVisibility(View.GONE);
                    button_android.setVisibility(View.VISIBLE);
                } else if(intent.hasExtra(Commons.FOOD)) {
                    imageView_food.setVisibility(View.GONE);
                    textView_food.setVisibility(View.GONE);
                    button_food.setVisibility(View.VISIBLE);
                }
            }
        };
        registerReceiver(updateUIReciver,filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                if (isMyServiceRunning(ServiceBM.class)) {
                    if (!hasServiceBeenStarted(Commons.SERVICE_SATARTED))
                        setServiceStarted();
                }
                if (!hasServiceBeenStarted(Commons.SERVICE_SATARTED)) {
                    if (btAdapter.getState() == BluetoothAdapter.STATE_ON) {
                        Log.v("BROADCAST", "Ya estaba Prendido");
                        Intent service = new Intent(getApplicationContext(), ServiceBM.class);
                        getApplicationContext().startService(service);
                        Log.v("BROADCAST", "Service");
                        setServiceStarted();
                    }
                }
            } else {
                if(!hasServiceWiFiBeenStarted(Commons.SERVICEWiFi_SATARTED)) {
                    Intent service = new Intent(this, ServiceWifiScan.class);
                    startService(service);
                    setServiceWiFiStarted();
                }
            }
        } else {
            if(!hasServiceWiFiBeenStarted(Commons.SERVICEWiFi_SATARTED)) {
                Intent service = new Intent(this, ServiceWifiScan.class);
                startService(service);
                setServiceWiFiStarted();
            }
        }
        btAdapter = null;

        button_Participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!button_Participate.getText().equals(getString(R.string.activity_main_useCases))) {
                    if (isInternetAvailability(false, WIFI_ON)) {
                        button_Participate.setEnabled(false);
                        mainThread();
                        //new MainAsyncTask().execute();
                    }
                } else {
                    Intent intent = new Intent(ActivityMain.this, ActivityUseCases.class);
                    startActivity(intent);
                }
            }
        });

        button_whereAmI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(hasBeenShown(Commons.WHERE_AM_I))
                    intent = new Intent(ActivityMain.this, ActivityQuestion.class);
                else
                    intent = new Intent(ActivityMain.this, ActivityMessage.class);
                intent.putExtra(Commons.WHERE_AM_I, 1);
                startActivity(intent);
            }
        });

        button_conferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(hasBeenShown(Commons.CONFERENCES))
                    intent = new Intent(ActivityMain.this, ActivityQuestion.class);
                else
                    intent = new Intent(ActivityMain.this, ActivityMessage.class);
                intent.putExtra(Commons.CONFERENCES, 2);
                startActivity(intent);
            }
        });

        button_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(hasBeenShown(Commons.ANDROID))
                    intent = new Intent(ActivityMain.this, ActivityQuestion.class);
                else
                    intent = new Intent(ActivityMain.this, ActivityMessage.class);
                intent.putExtra(Commons.ANDROID, 3);
                startActivity(intent);
            }
        });

        button_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(hasBeenShown(Commons.FOOD))
                    intent = new Intent(ActivityMain.this, ActivityQuestion.class);
                else
                    intent = new Intent(ActivityMain.this, ActivityMessage.class);
                intent.putExtra(Commons.FOOD, 4);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateUIReciver);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    protected void requestBluetooth() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            // Device does not support Bluetooth
            Log.v("MAIN", "FF");
        } else {
            if (!btAdapter.isEnabled()) {
                final Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BLUETOOTH);
            }
        }
        btAdapter = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_ENABLE_BLUETOOTH) {
            if(resultCode == Activity.RESULT_OK) {
                if(!hasServiceBeenStarted(Commons.SERVICE_SATARTED))
                    setServiceStarted();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(beaconHasBeenFound(Commons.WHERE_AM_I_BEACON_FOUND)) {
            textView_whereAmI.setVisibility(View.GONE);
            imageView_whereAmI.setVisibility(View.GONE);
            button_whereAmI.setVisibility(View.VISIBLE);
        }
        if (beaconHasBeenFound(Commons.CONFERENCES_BEACON_FOUND)) {
            textView_conferences.setVisibility(View.GONE);
            imageView_conferences.setVisibility(View.GONE);
            button_conferences.setVisibility(View.VISIBLE);
        }
        if (beaconHasBeenFound(Commons.ANDROID_BEACON_FOUND)) {
            textView_android.setVisibility(View.GONE);
            imageView_android.setVisibility(View.GONE);
            button_android.setVisibility(View.VISIBLE);
        }
        if (beaconHasBeenFound(Commons.FOOD_BEACON_FOUND)) {
            textView_food.setVisibility(View.GONE);
            imageView_food.setVisibility(View.GONE);
            button_food.setVisibility(View.VISIBLE);
        }
        if(hasBeenAnswered(Commons.WHERE_AM_I_CORRECT)) {
            button_whereAmI.setVisibility(View.GONE);
            mNotifyMgr.cancel(100);
        }
        if(hasBeenAnswered(Commons.CONFERENCES_CORRECT)) {
            button_conferences.setVisibility(View.GONE);
            mNotifyMgr.cancel(200);
        }
        if(hasBeenAnswered(Commons.ANDROID_CORRECT)) {
            button_android.setVisibility(View.GONE);
            mNotifyMgr.cancel(300);
        }
        if(hasBeenAnswered(Commons.FOOD_CORRECT)) {
            button_food.setVisibility(View.GONE);
            mNotifyMgr.cancel(400);
        }

        if(hasBeenAnswered(Commons.WHERE_AM_I_CORRECT) && hasBeenAnswered(Commons.CONFERENCES_CORRECT) && hasBeenAnswered(Commons.ANDROID_CORRECT) && hasBeenAnswered(Commons.FOOD_CORRECT)) {
            button_Participate.setVisibility(View.VISIBLE);
            if(getAlreadyParticipating(Commons.PARTICIPATING)) {
                button_Participate.setText(getString(R.string.activity_main_useCases));
            }
        } else {
            // Use this check to determine whether BLE is supported on the device. Then
            // you can selectively disable BLE-related features.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    requestBluetooth();
                }
            }
        }

    }

    public void showErrorDialog(Context context, String title, String message, String ok) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (isInternetAvailability(false, WIFI_ON)) {
                    //new MainAsyncTask().execute();
                    mainThread();
                    button_Participate.setEnabled(false);
                }
            }
        });
        alert.setNegativeButton(R.string.alertDialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Reacts in accordance to the parent of the activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_cases:
                Intent intent = new Intent(ActivityMain.this, ActivityUseCases.class);
                startActivity(intent);
                break;
            default:
                return false;
        }

        return true;
    }

    private void mainThread() {
        try {
            UserControl userControl = new UserControl(ActivityMain.this);
            DataBaseHandler dataBaseHandler = new DataBaseHandler(ActivityMain.this);
            final ArrayList<User> users = userControl.getAllUsers(dataBaseHandler);
            final JSonClient jSonClient = new JSonClient();
            final String urlString = "http://beaconio.com/mobile/rest/json?id=2100&type=2100&lat=12313123&lng=12313131313&action=2" +
                    "&email=" + Uri.encode(users.get(0).getEmail()) + "&stamp=JAVADAY";
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int res;
                    try {
                        String stream = jSonClient.GetHTTPData(urlString);
                        JSONObject oJsonObject = new JSONObject(stream);
                        int knownBean = oJsonObject.getInt("knownBean");
                        String errorMessage = oJsonObject.getString("errorMessage");
                        if (knownBean == 1) {
                            res = 1;
                        } else {
                            res = 0;
                            Log.v("MAIN", errorMessage);
                        }
                    }catch (Exception e) {
                        res = 0;
                        Log.v("MAIN", e.getMessage());
                    }
                    try {
                        final int ans = res;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(progressDialog != null)
                                    progressDialog.cancel();
                                if (ans == 1) {
                                    Toast.makeText(ActivityMain.this, getString(R.string.activity_main_congratulations), Toast.LENGTH_LONG).show();
                                    button_Participate.setText(getString(R.string.activity_main_useCases));
                                    setAlreadyParticipating();
                                } else {
                                    showErrorDialog(ActivityMain.this, getString(R.string.app_name2), getString(R.string.alertDialog_detail), getString(R.string.alertDialog_ok));
                                }
                                button_Participate.setEnabled(true);
                            }
                        });

                    }catch (Exception e) {

                    }
                }
            });
            progressDialog = ProgressDialog.show(ActivityMain.this,getString(R.string.progressDialog_title), getString(R.string.progressDialog_message), true, false);
            thread.start();

        }catch (Exception e) {

        }
    }

    /*private class MainAsyncTask extends AsyncTask<Void, Void, Integer> {

        private UserControl userControl;
        private DataBaseHandler dataBaseHandler;
        private ArrayList<User> users;
        private JSonClient jSonClient;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ActivityMain.this,getString(R.string.progressDialog_title), getString(R.string.progressDialog_message), true, true);
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
            button_Participate.setEnabled(true);
            try {
                userControl = new UserControl(ActivityMain.this);
                dataBaseHandler = new DataBaseHandler(ActivityMain.this);
                users = userControl.getAllUsers(dataBaseHandler);
            } catch (Exception e) {
                Log.v("MAIN", e.getMessage());
            } finally {
                Log.v("MAIN", "ACABE PREEXECUTE");
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Integer integer) {
            Log.v("MAIN", "CANCELARON ASYNCTASK");
            super.onCancelled(integer);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                Log.v("MAIN", "SI ENTRO A DO IN BACK...");
                if(!isCancelled()) {
                    String urlString = "http://beaconio.com/mobile/rest/json?id=2100&type=2100&lat=12313123&lng=12313131313&action=2" +
                            "&email=" + Uri.encode(users.get(0).getEmail()) + "&stamp=JAVADAY";
                    Log.v("MAIN", urlString);
                    jSonClient = new JSonClient();
                    String stream = jSonClient.GetHTTPData(urlString);
                    JSONObject oJsonObject = new JSONObject(stream);
                    int knownBean = oJsonObject.getInt("knownBean");
                    String errorMessage = oJsonObject.getString("errorMessage");
                    if (knownBean == 1) {
                        return 1;
                    } else {
                        Log.v("MAIN", errorMessage);
                    }
                }
            }catch(Exception e) {
                Log.v("MAIN", e.getMessage());
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(null);
            progressDialog.cancel();
            if(result == 1) {
                Toast.makeText(ActivityMain.this, getString(R.string.activity_main_congratulations), Toast.LENGTH_LONG).show();
                button_Participate.setText(getString(R.string.activity_main_useCases));
                setAlreadyParticipating();
            } else {
                showErrorDialog(ActivityMain.this, getString(R.string.app_name2), getString(R.string.alertDialog_detail), getString(R.string.alertDialog_ok));
            }
        }
    }*/

}
