package com.example.atavoosi.guitarspeedtrainer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.Callable;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingActivity extends BaseNavigationActivity implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    //region ## VARIABLES ##

    //Controls
    SeekBar sbFromBpm;
    TextView tvFromBpm;
    SeekBar sbToBpm;
    TextView tvToBpm;
    SeekBar sbChangeTime;
    TextView tvChangeTime;
    Button btnSave;
    Spinner spinner;
    TextView totalTime;

    //Preferences
    private static String PrefFromBpm = "PrefFromBpm";
    private static String PrefToBpm = "PrefToBpm";
    private static String PrefChangeTime = "PrefChangeTime";
    private static String PrefTickSound = "PrefTickSound";
    private static int PrefFromBpmDefaultValue = 40;
    private static int PrefToBpmDefaultValue = 208;
    private static int PrefChangeTimeDefaultValue = 5;
    private static int Step = 4;
    private static String PrefTickSoundDefaultValue = "tick1";
    int changeTime, fromBpm, toBpm, onlineFromBpm, onlineToBpm, onlineChangeTime;
    String tickSound;

    boolean firstRun;

    //endregion

    // region ## EVENTS ##

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContent(R.layout.activity_setting);
        super.onCreateBase(false);
        super.onCreate(savedInstanceState);

        init();

        setTotalTime();

        //SeekBars
        sbFromBpm.setOnSeekBarChangeListener(this);
        sbToBpm.setOnSeekBarChangeListener(this);
        sbChangeTime.setOnSeekBarChangeListener(this);

        //btnSave
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fromBpm = Integer.parseInt(String.valueOf(sbFromBpm.getProgress() * Step)) + 20;
                int toBpm = Integer.parseInt(String.valueOf(sbToBpm.getProgress() * Step)) + 20;
                int changeTime = Integer.parseInt(String.valueOf(sbChangeTime.getProgress())) + 1;
                String tickSound = spinner.getSelectedItem().toString();

                if (fromBpm < toBpm) {
                    PreferenceUtil.savePreferences(SettingActivity.this, PrefFromBpm, fromBpm);
                    PreferenceUtil.savePreferences(SettingActivity.this, PrefToBpm, toBpm);
                    PreferenceUtil.savePreferences(SettingActivity.this, PrefChangeTime, changeTime);
                    PreferenceUtil.savePreferences(SettingActivity.this, PrefTickSound, tickSound);
                    goToMainActivity();
                } else {
                    Toast.makeText(SettingActivity.this, "'از تمپو' باید کوچکتر از 'تا تمپو' باشد.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Spinner
        spinner = (Spinner) findViewById(R.id.spnTickSound);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.TickSounds, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        String[] TickSounds = getResources().getStringArray(R.array.TickSounds);
        int tickSoundIndex = Arrays.asList(TickSounds).indexOf(tickSound);
        spinner.setSelection(tickSoundIndex);
    }

    @Override
    public void onBackPressed() {
        super.onBackClicked(new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        }, null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenuBase(menu, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelectedBase(item, true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        super.onNavigationItemSelected(item, this);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!firstRun) {
            String selectedItem = parent.getSelectedItem().toString();
            int tickId = getResources().getIdentifier(selectedItem, "raw", getPackageName());
            MediaPlayer mediaPlayer = MediaPlayer.create(SettingActivity.this, tickId);
            mediaPlayer.start();
        }
        firstRun = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sbFromBpm:
                onlineFromBpm = (progress * Step) + 20;
                tvFromBpm.setText(String.valueOf(onlineFromBpm));
                setTotalTime();
                break;
            case R.id.sbToBpm:
                onlineToBpm = (progress * Step) + 20;
                tvToBpm.setText(String.valueOf(onlineToBpm));
                setTotalTime();
                break;
            case R.id.sbChangeTime:
                onlineChangeTime = progress + 1;
                tvChangeTime.setText(String.valueOf(onlineChangeTime));
                setTotalTime();
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //endregion

    // region ## METHODS ##

    public void goToMainActivity() {
        try {
            startActivity(new Intent(SettingActivity.this, MainActivity.class));
        } catch (Exception e) {

        }
    }


    private void setTotalTime() {
        String text = "";
        int totalSec = ((onlineToBpm - onlineFromBpm) * onlineChangeTime);
        if (totalSec > 0) {
            if (totalSec < 60)
                text = "زمان حدودی کل: " + totalSec + "ثانیه";
            else {
                text = "زمان حدودی کل: " + Math.round(totalSec / 60) + "دقیقه";
            }
        }

        totalTime.setText(text);
    }

    private void init() {
        sbFromBpm = (SeekBar) findViewById(R.id.sbFromBpm);
        tvFromBpm = (TextView) findViewById(R.id.tvFromBpm);
        sbToBpm = (SeekBar) findViewById(R.id.sbToBpm);
        tvToBpm = (TextView) findViewById(R.id.tvToBpm);
        sbChangeTime = (SeekBar) findViewById(R.id.sbChangeTime);
        tvChangeTime = (TextView) findViewById(R.id.tvChangeTime);
        totalTime = (TextView) findViewById(R.id.totalTime);

        firstRun = true;

        fromBpm = PreferenceUtil.readPreferences(SettingActivity.this, PrefFromBpm, PrefFromBpmDefaultValue);
        toBpm = PreferenceUtil.readPreferences(SettingActivity.this, PrefToBpm, PrefToBpmDefaultValue);
        changeTime = PreferenceUtil.readPreferences(SettingActivity.this, PrefChangeTime, PrefChangeTimeDefaultValue);
        tickSound = PreferenceUtil.readPreferences(SettingActivity.this, PrefTickSound, PrefTickSoundDefaultValue);
        onlineFromBpm = fromBpm;
        onlineToBpm = toBpm;
        onlineChangeTime = changeTime;

        sbFromBpm.setMax(70);
        sbToBpm.setMax(70);
        sbChangeTime.setMax(29);
        sbFromBpm.setProgress((fromBpm - 20) / Step);
        tvFromBpm.setText(String.valueOf(fromBpm));
        sbToBpm.setProgress((toBpm - 20) / Step);
        tvToBpm.setText(String.valueOf(toBpm));
        sbChangeTime.setProgress(changeTime - 1);
        tvChangeTime.setText(String.valueOf(changeTime));
    }
    //endregion
}
