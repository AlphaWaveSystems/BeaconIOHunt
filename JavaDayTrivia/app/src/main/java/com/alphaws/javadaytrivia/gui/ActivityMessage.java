package com.alphaws.javadaytrivia.gui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alphaws.javadaytrivia.R;
import com.alphaws.javadaytrivia.beans.User;
import com.alphaws.javadaytrivia.database.DataBaseHandler;
import com.alphaws.javadaytrivia.database.UserControl;
import com.alphaws.javadaytrivia.tools.Commons;

import java.util.ArrayList;

/**
 * Created by Carlos Alexis on 15/07/2015.
 */
public class ActivityMessage extends ActivityParent {

    private Button button;
    private TextView textView;
    private String origin, message;
    private UserControl userControl;
    private DataBaseHandler dataBaseHandler;
    private User user;
    private ArrayList<User> arrayList;
    private String messageForNotificationUpdate;
    private int originForNotificationUpdate;
    //private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        button = (Button) findViewById(R.id.activity_message_button_seeQuestion);
        textView = (TextView) findViewById(R.id.activity_message_textView);

        userControl = new UserControl(ActivityMessage.this);
        dataBaseHandler = new DataBaseHandler(ActivityMessage.this);
        arrayList = userControl.getAllUsers(dataBaseHandler);
        user = arrayList.get(0);
        Log.v("MESSAGE", user.toString());

        if(getIntent().hasExtra(Commons.WHERE_AM_I)) {
            setMessageAlredyShown(1);
            origin = Commons.WHERE_AM_I;
            messageForNotificationUpdate = getResources().getString(R.string.activity_message_welcome) + " " + getResources().getString(R.string.activity_message_javaDev);
            originForNotificationUpdate = 1;
        }else if(getIntent().hasExtra(Commons.CONFERENCES)) {
            setMessageAlredyShown(2);
            origin = Commons.CONFERENCES;
            messageForNotificationUpdate = getResources().getString(R.string.activity_message_welcome) + " " + getResources().getString(R.string.activity_message_auditorium);
            originForNotificationUpdate = 2;
        }else if(getIntent().hasExtra(Commons.ANDROID)) {
            setMessageAlredyShown(3);
            origin = Commons.ANDROID;
            messageForNotificationUpdate = getResources().getString(R.string.activity_message_welcomeAndroid) + " " + getResources().getString(R.string.activity_message_android);
            originForNotificationUpdate = 3;
        }else {
            setMessageAlredyShown(4);
            origin = Commons.FOOD;
            messageForNotificationUpdate = getResources().getString(R.string.activity_message_welcomeCafeteria) + " " + getResources().getString(R.string.activity_message_cafeteria);
            originForNotificationUpdate = 4;
        }
        if(!getIntent().hasExtra(Commons.FROMNOTIFICATION)) {
            if(!getUpdateNotification((originForNotificationUpdate == 1)? Commons.WHERE_AM_I_UPDATE : (originForNotificationUpdate == 2)? Commons.CONFERENCES_UPDATE : (originForNotificationUpdate == 3)? Commons.ANDROID_UPDATE : Commons.FOOD_UPDATE)) {
                sendNotificationUpdate(getResources().getString(R.string.app_name2), messageForNotificationUpdate, originForNotificationUpdate, 1);
            }
        } else
            setUpdateNotification(originForNotificationUpdate);
        messageForNotificationUpdate = null;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMessage.this, ActivityQuestion.class);
                switch(origin) {
                    case Commons.WHERE_AM_I:
                        intent.putExtra(Commons.WHERE_AM_I, 1);
                        startActivity(intent);
                        break;
                    case Commons.CONFERENCES:
                        intent.putExtra(Commons.CONFERENCES, 2);
                        startActivity(intent);
                        break;
                    case Commons.ANDROID:
                        intent.putExtra(Commons.ANDROID, 3);
                        startActivity(intent);
                        break;
                    case Commons.FOOD:
                        intent.putExtra(Commons.FOOD, 4);
                        startActivity(intent);
                        break;
                }
                finish();
            }
        });

    }

    /**
     * Handle a configuration change.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onResume();
    }

    /**
     * Handle the onResume.
     */
    @Override
    protected void onResume() {
        super.onResume();

        //Changes the language of the predetermined text of the window.
        textView.setText(getString(R.string.activity_message_hi));
        if(origin.equals(Commons.WHERE_AM_I)) {
            message = textView.getText().toString() + " " + "<b>" + user.getFirstName() + " " + user.getLastName() + "</b>" + ",<br><br>" + getString(R.string.activity_message_welcome) +
                    " " + "<b>" + getString(R.string.activity_message_javaDev) + "</b>" + "<br><br>" + getString(R.string.activity_message_javaDevMessage) +
                    "<br><br>" + getString(R.string.activity_message_cafeteriaMessage);
        }else if(origin.equals(Commons.CONFERENCES)) {
            message = textView.getText().toString() + " " + "<b>" + user.getFirstName() + " " + user.getLastName() + "</b>" + ",<br><br>" + getString(R.string.activity_message_welcome) +
                    " " + "<b>" + getString(R.string.activity_message_auditorium) + "</b>" + "<br><br>" + getString(R.string.activity_message_auditoriumMessage) +
                    "<br><br>" + getString(R.string.activity_message_cafeteriaMessage);
        }else if(origin.equals(Commons.FOOD)) {
            message = textView.getText().toString() + " " + "<b>" + user.getFirstName() + " " + user.getLastName() + "</b>" + ",<br><br>" + getString(R.string.activity_message_welcomeCafeteria) +
                    " " + "<b>" + getString(R.string.activity_message_cafeteria) + "</b>" + "<br><br>" + getString(R.string.activity_message_cafeteriaMessage);
        } else {
            message = textView.getText().toString() + " " + "<b>" + user.getFirstName() + " " + user.getLastName() + "</b>" + ",<br><br>" + getString(R.string.activity_message_welcomeAndroid) +
                    " " + "<b>" + getString(R.string.activity_message_android) + "</b>" + "<br><br>" + getString(R.string.activity_message_androidMessage) +
                    "<br><br>" + getString(R.string.activity_message_cafeteriaMessage);
        }
        textView.setText(Html.fromHtml(message));
        button.setText(getString(R.string.activity_message_button));

        /*if((Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) || !getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            wifiManager.startScan();
        }*/

    }

}
