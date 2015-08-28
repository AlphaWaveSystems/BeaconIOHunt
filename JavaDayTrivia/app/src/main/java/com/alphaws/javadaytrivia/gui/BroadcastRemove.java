package com.alphaws.javadaytrivia.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.alphaws.javadaytrivia.tools.Commons;

/**
 * Created by Carlos Alexis on 17/08/2015.
 */
public class BroadcastRemove extends BroadcastReceiver {

    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("MAIN", "LLegue");
        if(intent != null) {
            sharedPreferences = context.getSharedPreferences(Commons.MY_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if(intent.hasExtra(Commons.REMOVED_WHERE)) {
                Log.v("MAIN", "LOLLLLLL");
                editor.putBoolean(Commons.REMOVED_WHERE, true);
            } else if(intent.hasExtra(Commons.REMOVED_ARRUPE)) {
                editor.putBoolean(Commons.REMOVED_ARRUPE, true);
            } else if(intent.hasExtra(Commons.REMOVED_ANDROID)) {
                Log.v("MAIN", "LOLLLLLLkjhghgv");
                editor.putBoolean(Commons.REMOVED_ANDROID, true);
            } else if(intent.hasExtra(Commons.REMOVED_CAFETERIA)) {
                editor.putBoolean(Commons.REMOVED_CAFETERIA, true);
            }
            editor.commit();
        }
    }

}
