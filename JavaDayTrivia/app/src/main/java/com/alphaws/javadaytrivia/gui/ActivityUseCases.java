package com.alphaws.javadaytrivia.gui;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.alphaws.javadaytrivia.R;


public class ActivityUseCases extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    public static final int COUNT = 9;
    //public MyAsyncTask asyncTask;
    private Thread thread;
    private boolean i = true;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_cases);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        thread =  new Thread() {
            @Override
            public void run() {
                try {
                    int j = 0;
                    while(i) {
                        final int k = j;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mViewPager.setCurrentItem(k);
                            }
                        });
                        try {
                            Thread.sleep(3000);
                        }catch(InterruptedException e){

                        }
                        if(j == COUNT-1){
                            j = 0;
                        }else{
                            j++;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

        //asyncTask = new MyAsyncTask();
        //asyncTask.execute();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    /*class MyAsyncTask extends AsyncTask<Void, Integer, Void>{

        private boolean running = true;

        @Override
        protected Void doInBackground(Void... params) {

            int i = 0;
            while(running){
                publishProgress(i);
                try {
                    Thread.sleep(3000);
                }catch(InterruptedException e){

                }
                if(i == COUNT-1){
                    i = 0;
                }else{
                    i++;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mViewPager.setCurrentItem(values[0]);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            running = false;
            Log.v("MAIN", "USOS DETENIDOS");
        }

        @Override
        protected void onCancelled() {
            //running = false;
            Log.v("MAIN", "USOS DETENIDOS");
        }

    }*/

    @Override
    protected void onPause() {
        super.onPause();
        i = false;
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    FragmentDeal fragmentDeal = new FragmentDeal();
                    return fragmentDeal;
                case 1:
                    FragmentBusStop fragmentBusStop = new FragmentBusStop();
                    return fragmentBusStop;
                case 2:
                    FragmentStore fragmentStore = new FragmentStore();
                    return fragmentStore;
                case 3:
                    FragmentStoreMap fragmentStoreMap = new FragmentStoreMap();
                    return fragmentStoreMap;
                case 4:
                    FragmentBusStation fragmentBusStation = new FragmentBusStation();
                    return fragmentBusStation;
                case 5:
                    FragmentMuseum fragmentMuseum = new FragmentMuseum();
                    return fragmentMuseum;
                case 6:
                    FragmentConcert fragmentConcert = new FragmentConcert();
                    return fragmentConcert;
                case 7:
                    FragmentCompany fragmentCompany = new FragmentCompany();
                    return fragmentCompany;
                case 8:
                    FragmentLoyalty fragmentLoyality = new FragmentLoyalty();
                    return fragmentLoyality;
                default:
                    FragmentDeal fragmentDeal2 = new FragmentDeal();
                    return fragmentDeal2;
            }
        }

        @Override
        public int getCount() {
            return COUNT;
        }
    }



}
