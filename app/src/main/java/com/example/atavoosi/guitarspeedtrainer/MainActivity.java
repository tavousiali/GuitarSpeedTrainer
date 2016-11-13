package com.example.atavoosi.guitarspeedtrainer;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseNavigationActivity {

    //region ## VARIABLES ##

    //Controls
    public static Button start, stop, pause;
    public static TextView bpm;
    SoundPool mySound;

    //Task
    private ScheduledExecutorService scheduledExecutorService;
    public static ScheduledFuture<?> futureTask;
    private Runnable task;

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

    //endregion

    // region ## EVENTS ##

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContent(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        init();

        //تنظیم صدا با ولوم گوشی
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onBackPressed() {
        super.onBackClicked(new Callable() {
            @Override
            public Object call() throws Exception {
                stop();
                return null;
            }
        }, "MainActivity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        stop();
        super.onNavigationItemSelected(item, this);
        return true;
    }

    //endregion

    // region ## METHODS ##

    public void changeReadInterval(long time) {
        if (time > 0) {
            if (futureTask != null) {
                futureTask.cancel(true);
            }

            futureTask = scheduledExecutorService.scheduleAtFixedRate(task, 0, time, TimeUnit.MILLISECONDS);
        }
    }

    private void init() {
        fromBpm = PreferenceUtil.readPreferences(MainActivity.this, PrefFromBpm, PrefFromBpmDefaultValue);
        toBpm = PreferenceUtil.readPreferences(MainActivity.this, PrefToBpm, PrefToBpmDefaultValue);
        changeTime = PreferenceUtil.readPreferences(MainActivity.this, PrefChangeTime, PrefChangeTimeDefaultValue) * 1000;
        tickSound = PreferenceUtil.readPreferences(MainActivity.this, PrefTickSound, PrefTickSoundDefaultValue);
        sleepTime = ConvertUtil.ConvertBpmToMs(fromBpm);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        pause = (Button) findViewById(R.id.pause);
        bpm = (TextView) findViewById(R.id.bpm);
        bpm.setText(String.valueOf(ConvertUtil.ConvertMsToBpm(sleepTime)));

        mySound = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        int tickId = getResources().getIdentifier(tickSound, "raw", getPackageName());
        musicId = mySound.load(this, tickId, 1);

        // Your executor, you should instanciate it once for all
        scheduledExecutorService = Executors.newScheduledThreadPool(5);

        task = new Runnable() {

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
                //DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:SS");
                //Log.i("Beep", df.format(currentDate) + "_____" + df.format(baseDate) + "______" + sleepTime);

                long baseDateTime = baseDate.getTime();
                long currentDateTime = currentDate.getTime();
                if (baseDateTime < currentDateTime) {
                    sleepTime = ConvertUtil.ConvertMsToBpm(ConvertUtil.ConvertBpmToMs(sleepTime) + 1);

                    baseDate.setTime(baseDate.getTime() + changeTime);
                }
                try {
                    if (sleepTime < ConvertUtil.ConvertBpmToMs(toBpm))
                        stop();

                    Thread.sleep(sleepTime);
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

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });
    }

    private void start() {
        start.post(new Runnable() {
            public void run() {
                start.setVisibility(View.GONE);
            }
        });
        stop.post(new Runnable() {
            public void run() {
                stop.setVisibility(View.VISIBLE);
            }
        });
        pause.post(new Runnable() {
            public void run() {
                pause.setVisibility(View.VISIBLE);
            }
        });

        Calendar cal = Calendar.getInstance();
        baseDate = cal.getTime();
        baseDate.setTime(baseDate.getTime() + changeTime);

        changeReadInterval(ConvertUtil.ConvertBpmToMs(fromBpm));

    }

    public static void stop() {
        start.post(new Runnable() {
            public void run() {
                start.setVisibility(View.VISIBLE);
            }
        });
        stop.post(new Runnable() {
            public void run() {
                stop.setVisibility(View.GONE);
            }
        });
        pause.post(new Runnable() {
            public void run() {
                pause.setVisibility(View.GONE);
            }
        });

        if (futureTask != null)
            futureTask.cancel(true);

        // اگر بخواهیم ریست کنیم
        sleepTime = ConvertUtil.ConvertBpmToMs(fromBpm);
        bpm.setText(String.valueOf(ConvertUtil.ConvertMsToBpm(sleepTime)));
        // اگر بخواهیم ادامه بدهیم، هیچ چیزی لازم نیست بنویسیم
    }

    public static void pause() {
        start.post(new Runnable() {
            public void run() {
                start.setVisibility(View.VISIBLE);
            }
        });
        stop.post(new Runnable() {
            public void run() {
                stop.setVisibility(View.VISIBLE);
            }
        });
        pause.post(new Runnable() {
            public void run() {
                pause.setVisibility(View.GONE);
            }
        });

        if (futureTask != null)
            futureTask.cancel(true);

    }

    //endregion

}
