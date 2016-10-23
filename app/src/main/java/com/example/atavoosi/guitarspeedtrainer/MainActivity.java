package com.example.atavoosi.guitarspeedtrainer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Controls
    public static Button start, stop;
    public static TextView bpm;
    SoundPool mySound;
    private DrawerLayout drawer;

    //Task
    private ScheduledExecutorService scheduledExecutorService;
    public static ScheduledFuture<?> futureTask;
    private Runnable myTask;
    private Runnable myTaskInSecond;


    //Preferences
    private static String PrefFromBpm = "PrefFromBpm";
    private static String PrefToBpm = "PrefToBpm";
    private static String PrefChangeTime = "PrefChangeTime";
    private static String PrefTickSound = "PrefTickSound";
    private static int PrefFromBpmDefaultValue = 40;
    private static int PrefToBpmDefaultValue = 208;
    private static int PrefChangeTimeDefaultValue = 5;
    private static String PrefTickSoundDefaultValue = "tick1";
    int changeTime;
    public static int fromBpm;
    public static int toBpm;
    String tickSound;

    //Other
    Date baseDate;
    public static int sleepTime;
    int musicId;
    final int iterator = 5;
    final int iteratorInSecond = 5;
    int j = 0;

    Thread thread = new Thread() {

        public void run() {

            for (int i = 0; i < iterator; i++) {

                try {
                    sleep(sleepTime);

                    j++;

                    //Log
//                    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:SS");
//                    Date dateobj = new Date();
//                    Log.i("Beep", df.format(dateobj) + "_____" + sleepTime + "_____" + j);

                    mySound.play(musicId, 1, 1, 1, 0, 1);
//
                    if (i == iterator - 1) {
                        sleepTime = sleepTime - 100;
                        i = 0;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public void changeReadInterval(long time) {
        if (time > 0) {
            if (futureTask != null) {
                futureTask.cancel(true);
            }

            //futureTask = scheduledExecutorService.scheduleAtFixedRate(myTask, 0, time, TimeUnit.MILLISECONDS);
            futureTask = scheduledExecutorService.scheduleAtFixedRate(myTaskInSecond, 0, time, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromBpm = PreferenceUtil.readPreferences(MainActivity.this, PrefFromBpm, PrefFromBpmDefaultValue);
        toBpm = PreferenceUtil.readPreferences(MainActivity.this, PrefToBpm, PrefToBpmDefaultValue);
        changeTime = PreferenceUtil.readPreferences(MainActivity.this, PrefChangeTime, PrefChangeTimeDefaultValue) * 1000;
        tickSound = PreferenceUtil.readPreferences(MainActivity.this, PrefTickSound, PrefTickSoundDefaultValue);
        sleepTime = ConvertUtil.ConvertBpmToMs(fromBpm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        bpm = (TextView) findViewById(R.id.bpm);
        bpm.setText(String.valueOf(ConvertUtil.ConvertMsToBpm(sleepTime)));

        mySound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        int tickId = getResources().getIdentifier(tickSound, "raw", getPackageName());
        musicId = mySound.load(this, tickId, 1);

        // Your executor, you should instanciate it once for all
        scheduledExecutorService = Executors.newScheduledThreadPool(5);

        // Since your task won't change during runtime, you can instanciate it too once for all
        myTask = new Runnable() {
            @Override
            public void run() {
                //Log
//                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:SS");
//                Date date = new Date();
//                Log.i("Beep", df.format(date) + "_____" + sleepTime);

                //Play sound
                mySound.play(musicId, 1, 1, 1, 0, 1);

                bpm.post(new Runnable() {
                    public void run() {
                        bpm.setText(String.valueOf(ConvertUtil.ConvertMsToBpm(sleepTime)));
                    }
                });

                if (j == iteratorInSecond) {
                    j = 0;
                    sleepTime = sleepTime - 10;
                }
                try {
                    thread.sleep(sleepTime);
                    changeReadInterval(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        myTaskInSecond = new Runnable() {

            @Override
            public void run() {

                //Play sound
                mySound.play(musicId, 1, 1, 1, 0, 1);


                bpm.post(new Runnable() {
                    public void run() {
                        bpm.setText(String.valueOf(ConvertUtil.ConvertMsToBpm(sleepTime)));
                    }
                });


                Calendar cal = Calendar.getInstance();
                Date currentDate = cal.getTime();

                //Log
//                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:SS");
//                Log.i("Beep", df.format(currentDate) + "_____" + df.format(baseDate) + "______" + sleepTime);

                long baseDateTime = baseDate.getTime();
                long currentDateTime = currentDate.getTime();
                if (baseDateTime < currentDateTime) {
                    sleepTime = ConvertUtil.ConvertMsToBpm(ConvertUtil.ConvertBpmToMs(sleepTime) + 1);

                    baseDate.setTime(baseDate.getTime() + changeTime);
                }
                try {
                    if (sleepTime <= ConvertUtil.ConvertBpmToMs(toBpm))
                        stop();

                    thread.sleep(sleepTime);
                    changeReadInterval(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

    }

    private void start(){
        start.setVisibility(View.GONE);
        stop.setVisibility(View.VISIBLE);
        Calendar cal = Calendar.getInstance();
        baseDate = cal.getTime();
        baseDate.setTime(baseDate.getTime() + changeTime);

        changeReadInterval(ConvertUtil.ConvertBpmToMs(fromBpm));

    }
    public static void stop() {
        pause();

        // اگر بخواهیم ریست کنیم
        sleepTime = ConvertUtil.ConvertBpmToMs(fromBpm);
        bpm.setText(String.valueOf(ConvertUtil.ConvertMsToBpm(sleepTime)));
        // اگر بخواهیم ادامه بدهیم، هیچ چیزی لازم نیست بنویسیم
    }

    public static void pause() {
        stop.setVisibility(View.GONE);
        start.setVisibility(View.VISIBLE);
        if (futureTask != null)
            futureTask.cancel(true);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (doubleBackToExitPressedOnce) {
                moveTaskToBack(true);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "برای خروج بار دیگر کلیک کنید", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.navigetion_icon) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.setting) {
            try {
                stop();
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            } catch (Exception e) {

            }
        }

        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

}
