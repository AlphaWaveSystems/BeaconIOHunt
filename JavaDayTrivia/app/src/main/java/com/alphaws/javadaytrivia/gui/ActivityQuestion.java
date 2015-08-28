package com.alphaws.javadaytrivia.gui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alphaws.javadaytrivia.R;
import com.alphaws.javadaytrivia.tools.Commons;

/**
 * Created by Carlos Alexis on 15/07/2015.
 */
public class ActivityQuestion extends ActivityParent {

    private LinearLayout linearLayout_auditorium, linearLayout_android, linearLayout_javaDev, linearLayout_cafeteria;
    private EditText editText_auditorium, editText_android, editText_javaDev, editText_cafeteria;
    private Button button;
    private String origin;
    private String messageForNotificationUpdate;
    private int originForNotificationUpdate;
    //private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        linearLayout_auditorium = (LinearLayout) findViewById(R.id.activity_question_linearLayout_auditorium);
        linearLayout_android = (LinearLayout) findViewById(R.id.activity_question_linearLayout_android);
        linearLayout_javaDev = (LinearLayout) findViewById(R.id.activity_question_linearLayout_javaDev);
        linearLayout_cafeteria = (LinearLayout) findViewById(R.id.activity_question_linearLayout_cafeteria);
        editText_auditorium = (EditText) findViewById(R.id.activity_question_editText_auditorium);
        editText_android = (EditText) findViewById(R.id.activity_question_editText_android);
        editText_javaDev = (EditText) findViewById(R.id.activity_question_editText_javaDev);
        editText_cafeteria = (EditText) findViewById(R.id.activity_question_editText_cafeteria);
        button = (Button) findViewById(R.id.activity_question_button_answer);

        if(getIntent().hasExtra(Commons.WHERE_AM_I)) {
            origin = Commons.WHERE_AM_I;
            linearLayout_android.setVisibility(View.GONE);
            linearLayout_auditorium.setVisibility(View.GONE);
            linearLayout_cafeteria.setVisibility(View.GONE);
            linearLayout_javaDev.setVisibility(View.VISIBLE);
            messageForNotificationUpdate = getResources().getString(R.string.activity_message_welcome) + " " + getResources().getString(R.string.activity_message_javaDev);
            originForNotificationUpdate = 1;
        }else if(getIntent().hasExtra(Commons.CONFERENCES)) {
            origin = Commons.CONFERENCES;
            linearLayout_javaDev.setVisibility(View.GONE);
            linearLayout_android.setVisibility(View.GONE);
            linearLayout_cafeteria.setVisibility(View.GONE);
            linearLayout_auditorium.setVisibility(View.VISIBLE);
            messageForNotificationUpdate = getResources().getString(R.string.activity_message_welcome) + " " + getResources().getString(R.string.activity_message_auditorium);
            originForNotificationUpdate = 2;
        }else if(getIntent().hasExtra(Commons.ANDROID)) {
            origin = Commons.ANDROID;
            linearLayout_javaDev.setVisibility(View.GONE);
            linearLayout_auditorium.setVisibility(View.GONE);
            linearLayout_cafeteria.setVisibility(View.GONE);
            linearLayout_android.setVisibility(View.VISIBLE);
            messageForNotificationUpdate = getResources().getString(R.string.activity_message_welcomeAndroid) + " " + getResources().getString(R.string.activity_message_android);
            originForNotificationUpdate = 3;
        }else {
            origin = Commons.FOOD;
            linearLayout_javaDev.setVisibility(View.GONE);
            linearLayout_android.setVisibility(View.GONE);
            linearLayout_auditorium.setVisibility(View.GONE);
            linearLayout_cafeteria.setVisibility(View.VISIBLE);
            messageForNotificationUpdate = getResources().getString(R.string.activity_message_welcomeCafeteria) + " " + getResources().getString(R.string.activity_message_cafeteria);
            originForNotificationUpdate = 4;
        }

        if(!getIntent().hasExtra(Commons.FROMNOTIFICATION)) {
            if(!getUpdateNotification((originForNotificationUpdate == 1)? Commons.WHERE_AM_I_UPDATE : (originForNotificationUpdate == 2)? Commons.CONFERENCES_UPDATE : (originForNotificationUpdate == 3)? Commons.ANDROID_UPDATE : Commons.FOOD_UPDATE))
                sendNotificationUpdate(getResources().getString(R.string.app_name2), messageForNotificationUpdate, originForNotificationUpdate, 2);
        } else
            setUpdateNotification(originForNotificationUpdate);
        messageForNotificationUpdate = null;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityQuestion.this, ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                if(origin.equals(Commons.WHERE_AM_I)) {
                    editText_javaDev.setError(null);
                    if(editText_javaDev.getText().toString().trim().length() == 0)
                        editText_javaDev.setError(getString(R.string.activity_question_error_answer_blank));
                    if(editText_javaDev.getError() == null) {
                        try {
                            int answer = Integer.parseInt(editText_javaDev.getText().toString());
                            if(answer == 2) {
                                Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_correct), Toast.LENGTH_SHORT).show();
                                setCorrectAnswer(1);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(origin.equals(Commons.CONFERENCES)) {
                    editText_auditorium.setError(null);
                    if(editText_auditorium.getText().toString().trim().length() == 0)
                        editText_auditorium.setError(getString(R.string.activity_question_error_answer_blank));
                    if(editText_auditorium.getError() == null) {
                        try {
                            int answer = Integer.parseInt(editText_auditorium.getText().toString());
                            if(answer == 34) {
                                Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_correct), Toast.LENGTH_SHORT).show();
                                setCorrectAnswer(2);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(origin.equals(Commons.ANDROID)) {
                    editText_android.setError(null);
                    if(editText_android.getText().toString().trim().length() == 0)
                        editText_android.setError(getString(R.string.activity_question_error_answer_blank));
                    if(editText_android.getError() == null) {
                        try {
                            int answer = Integer.parseInt(editText_android.getText().toString());
                            if(answer == 17) {
                                Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_correct), Toast.LENGTH_SHORT).show();
                                setCorrectAnswer(3);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    editText_cafeteria.setError(null);
                    if(editText_cafeteria.getText().toString().trim().length() == 0)
                        editText_cafeteria.setError(getString(R.string.activity_question_error_answer_blank));
                    if(editText_cafeteria.getError() == null) {
                        try {
                            String text = editText_cafeteria.getText().toString();
                            String answer = text.replaceAll("\\s+", "");
                            boolean correct = true;
                            if(answer.length() < 5) {
                                for (int i = 0; i < answer.length(); i++) {
                                    if (i == 0 || i == answer.length() - 1) {
                                        if (!Character.isDigit(answer.charAt(i))) {
                                            Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                                            correct = false;
                                            break;
                                        }
                                    }
                                    if(Character.isDigit(answer.charAt(i))) {
                                        if(answer.charAt(i) != '5') {
                                            Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                                            correct = false;
                                            break;
                                        }
                                    } else {
                                        if(answer.charAt(i) != '+') {
                                            Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                                            correct = false;
                                            break;
                                        }
                                    }
                                }
                                if(correct) {
                                    Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_correct), Toast.LENGTH_SHORT).show();
                                    setCorrectAnswer(4);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(ActivityQuestion.this, getString(R.string.activity_question_incorrect), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

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
        //Change the language of the errors
        switch(origin) {
            case Commons.WHERE_AM_I:
                if (editText_javaDev.getError() != null)
                    editText_javaDev.setError(getString(R.string.activity_question_error_answer_blank));
                editText_javaDev.setHint(R.string.activity_question_editText_hint);
                break;
            case Commons.CONFERENCES:
                if (editText_auditorium.getError() != null)
                    editText_auditorium.setError(getString(R.string.activity_question_error_answer_blank));
                editText_auditorium.setHint(R.string.activity_question_editText_hint);
                break;
            case Commons.ANDROID:
                if (editText_android.getError() != null)
                    editText_android.setError(getString(R.string.activity_question_error_answer_blank));
                editText_android.setHint(R.string.activity_question_editText_hint);
                break;
            case Commons.FOOD:
                if (editText_cafeteria.getError() != null)
                    editText_cafeteria.setError(getString(R.string.activity_question_error_answer_blank));
                editText_cafeteria.setHint(R.string.activity_question_editText_hint);
                break;
            default:
                break;
        }

        button.setText(getString(R.string.activity_question_button));

        /*if((Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) || !getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            wifiManager.startScan();
        }*/

    }

}
