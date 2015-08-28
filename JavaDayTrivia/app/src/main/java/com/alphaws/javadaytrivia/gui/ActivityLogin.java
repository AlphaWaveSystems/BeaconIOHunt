package com.alphaws.javadaytrivia.gui;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alphaws.javadaytrivia.R;
import com.alphaws.javadaytrivia.beans.Beacon;
import com.alphaws.javadaytrivia.beans.User;
import com.alphaws.javadaytrivia.database.BeaconControl;
import com.alphaws.javadaytrivia.database.DataBaseHandler;
import com.alphaws.javadaytrivia.database.UserControl;
import com.alphaws.javadaytrivia.json.JSonClient;
import com.alphaws.javadaytrivia.json.JSonClientBackUp;
import com.alphaws.javadaytrivia.tools.Commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ActivityLogin extends ActivityParent implements AdapterView.OnItemSelectedListener {

    private EditText editText_Name, editText_LastName, editText_Email;
    private Button button;
    private Spinner spinner;
    private int occupation;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText_Name = (EditText) findViewById(R.id.activity_login_editText_name);
        editText_LastName = (EditText) findViewById(R.id.activity_login_editText_lastName);
        editText_Email = (EditText) findViewById(R.id.activity_login_editText_email);
        button = (Button) findViewById(R.id.activity_login_button_register);
        spinner = (Spinner) findViewById(R.id.activity_login_spinner);

        if(isLogged(Commons.LOGIN)) {
            Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
            startActivity(intent);
            finish();
        }

        if(!areBeaconsInDatabase()) {
            setBeacons();
            setBeaconsInDatabase();
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.occupation_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                //startActivity(intent);
                //The error of all the EditText are equal to null.
                editText_Name.setError(null);
                editText_LastName.setError(null);
                editText_Email.setError(null);

                //If any EditText is empty, an error is assigned to it.
                if(editText_Name.getText().toString().trim().length() == 0)
                    editText_Name.setError(getString(R.string.activity_login_error_name_blank));
                if(editText_LastName.getText().toString().trim().length() == 0)
                    editText_LastName.setError(getString(R.string.activity_login_error_lastName_blank));
                if(editText_Email.getText().toString().trim().length() == 0)
                    editText_Email.setError(getString(R.string.activity_login_error_email_blank));
                else
                    isValidEmail(editText_Email);

                //If there is no error in all the EditText, the Async Task is called
                if(editText_Name.getError() == null && editText_LastName.getError() == null && editText_Email.getError() == null) {
                    if(isInternetAvailability(false, WIFI_ON)) {
                        //new LoginAsyncTask().execute();
                        button.setEnabled(false);
                        loginThread();
                    }
                }

            }
        });

    }

    public void setBeacons() {
        BeaconControl beaconControl = new BeaconControl(ActivityLogin.this);
        long inserted;
        DataBaseHandler dataBaseHandler = new DataBaseHandler(ActivityLogin.this);
        inserted = beaconControl.addBeacon(new Beacon(Commons.MAJOR_1,Commons.MINOR_1,1), dataBaseHandler);
        Log.v("LOGIN", "INSERTED1: " + String.valueOf(inserted));
        inserted = beaconControl.addBeacon(new Beacon(Commons.MAJOR_2,Commons.MINOR_2,2), dataBaseHandler);
        Log.v("LOGIN", "INSERTED2: " + String.valueOf(inserted));
        inserted = beaconControl.addBeacon(new Beacon(Commons.MAJOR_3,Commons.MINOR_3,3), dataBaseHandler);
        Log.v("LOGIN", "INSERTED3: " + String.valueOf(inserted));
        inserted = beaconControl.addBeacon(new Beacon(Commons.MAJOR_4,Commons.MINOR_4,4), dataBaseHandler);
        Log.v("LOGIN", "INSERTED4: " + String.valueOf(inserted));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using parent.getItemAtPosition(position)
        occupation = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
        if(editText_Name.getError() != null)
            editText_Name.setError(getString(R.string.activity_login_error_name_blank));
        if(editText_LastName.getError() != null)
            editText_LastName.setError(getString(R.string.activity_login_error_lastName_blank));
        if(editText_Email.getError() != null)
            editText_Email.setError(getString(R.string.activity_login_error_email_blank));


        //Changes the language of the predetermined text of the window (hints of the EditTexts, the button text and title).
        editText_Name.setHint(getString(R.string.activity_login_name));
        editText_LastName.setHint(getString(R.string.activity_login_lastName));
        editText_Email.setHint(getString(R.string.activity_login_email));
        button.setText(getString(R.string.activity_login_register));

        //Show or hide the keyboard as necessary.
        if(editText_Name.getText().toString().trim().equals(""))
            requestKeyBoard(editText_Name);
        else if(editText_LastName.getText().toString().trim().equals(""))
            requestKeyBoard(editText_LastName);
        else if(editText_Email.getText().toString().trim().equals(""))
            requestKeyBoard(editText_Email);
        else
            requestKeyBoard(null);

    }

    /**
     * Open or close the keyboard if necessary, depending of the onResume function.
     */
    private void requestKeyBoard(EditText currentEditText) {
        if(currentEditText == null) {
            // Close the keyboard from the window and then current focus is cleared.
            try {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                getWindow().getCurrentFocus().clearFocus();
            } catch (Exception e) {
                Log.v("LOGIN",e.getMessage());
            }
        }else {
            //Open the keyboard	and then the current EditText request the focus.
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            currentEditText.requestFocus();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case WIFI_ON:
                //TODO

                break;
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
                    //new LoginAsyncTask().execute();
                    button.setEnabled(false);
                    loginThread();
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

    public boolean isValidEmail(EditText email) {

        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email.getText().toString();

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            email.setError(null);
            return true;
        } else {
            email.setError(getString(R.string.activity_login_error_email_wrong));
            return false;
        }
    }

    private void loginThread() {
        try {
            final String name = editText_Name.getText().toString(), lastName = editText_LastName.getText().toString(), email = editText_Email.getText().toString();
            final UserControl userControl = new UserControl(ActivityLogin.this);
            final DataBaseHandler dataBaseHandler = new DataBaseHandler(ActivityLogin.this);
            final JSonClient jSonClient = new JSonClient();
            final String url = "http://beaconio.com/mobile/rest/json?id=2100&type=2100&lat=12313123&lng=12313131313&" +
                    "action=1&email="+ Uri.encode(email) + "&name=" + Uri.encode(name) + "&lname=" + Uri.encode(lastName) +
                    "&work=" + Uri.encode(String.valueOf(occupation)) + "&stamp=JAVADAY";
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int res;
                    try {
                        String stream = jSonClient.GetHTTPData(url);
                        JSONObject oJsonObject = new JSONObject(stream);
                        int knownBean = oJsonObject.getInt("knownBean");
                        String errorMessage = oJsonObject.getString("errorMessage");
                        if(knownBean == 1) {
                            res = 1;
                        } else {
                            Log.v("LOGIN", errorMessage);
                            res = 2;
                        }
                    }catch(Exception e) {
                        res = 0;
                        Log.v("LOGIN", e.getMessage());
                    }
                    try {
                        final int ans = res;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(progressDialog != null)
                                    progressDialog.cancel();
                                if(ans == 1) {
                                    userControl.addUser(new User(name, lastName, email, occupation), dataBaseHandler);
                                    setLogged();
                                    Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if(ans == 2)
                                        Toast.makeText(ActivityLogin.this, getString(R.string.activity_login_error_email_alreadyParticipating), Toast.LENGTH_LONG).show();
                                    else
                                        showErrorDialog(ActivityLogin.this, getString(R.string.app_name2), getString(R.string.alertDialog_detail), getString(R.string.alertDialog_ok));
                                    button.setEnabled(true);
                                }
                            }
                        });

                    }catch (Exception e) {

                    }
                }
            });
            progressDialog = ProgressDialog.show(ActivityLogin.this, getString(R.string.progressDialog_title), getString(R.string.progressDialog_message), true, false);
            thread.start();
        }catch (Exception e) {

        }
    }

    /*private class LoginAsyncTask extends AsyncTask<Void, Void, Integer> {

        private String name, lastName, email;
        private UserControl userControl;
        private long inserted;
        private DataBaseHandler dataBaseHandler;
        private JSonClient jSonClient;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressDialog = ProgressDialog.show(ActivityLogin.this, getString(R.string.progressDialog_title), getString(R.string.progressDialog_message), true, false);
                name = editText_Name.getText().toString();
                lastName = editText_LastName.getText().toString();
                email = editText_Email.getText().toString();
            } catch(Exception e) {}
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                userControl = new UserControl(ActivityLogin.this);
                dataBaseHandler = new DataBaseHandler(ActivityLogin.this);
                String url = "http://beaconio.com/mobile/rest/json?id=2100&type=2100&lat=12313123&lng=12313131313&" +
                             "action=1&email="+ Uri.encode(email) + "&name=" + Uri.encode(name) + "&lname=" + Uri.encode(lastName) +
                             "&work=" + Uri.encode(String.valueOf(occupation)) + "&stamp=JAVADAY";
                jSonClient = new JSonClient();
                String stream = jSonClient.GetHTTPData(url);
                JSONObject oJsonObject = new JSONObject(stream);
                int knownBean = oJsonObject.getInt("knownBean");
                String errorMessage = oJsonObject.getString("errorMessage");
                if(knownBean == 1) {
                    return 1;
                } else {
                    Log.v("LOGIN", errorMessage);
                    return 2;
                }
            }catch(Exception e) {
                Log.v("LOGIN", e.getMessage());
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(null);
            progressDialog.cancel();
            if(result == 1) {
                inserted = userControl.addUser(new User(name, lastName, email, occupation), dataBaseHandler);
                setLogged();
                Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                startActivity(intent);
                finish();
            } else {
                if(result == 2)
                    Toast.makeText(ActivityLogin.this, getString(R.string.activity_login_error_email_alreadyParticipating), Toast.LENGTH_LONG).show();
                else
                    showErrorDialog(ActivityLogin.this, getString(R.string.app_name2), getString(R.string.alertDialog_detail), getString(R.string.alertDialog_ok));
            }
        }
    }*/
}
