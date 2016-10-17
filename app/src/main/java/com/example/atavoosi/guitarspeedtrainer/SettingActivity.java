package com.example.atavoosi.guitarspeedtrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Controls
    RadioGroup radioGroup;
    private DrawerLayout drawer;
    SeekBar sbFromBpm;
    TextView tvFromBpm;
    SeekBar sbToBpm;
    TextView tvToBpm;
    SeekBar sbChangeTime;
    TextView tvChangeTime;
    Button btnSave;

    //Preferences
    private static String PrefFromBpm = "PrefFromBpm";
    private static String PrefToBpm = "PrefToBpm";
    private static String PrefChangeTime = "PrefChangeTime";
    private static int PrefFromBpmDefaultValue = 40;
    private static int PrefToBpmDefaultValue = 208;
    private static int PrefChangeTimeDefaultValue = 5000;
    int changeTime;
    int fromBpm;
    int toBpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        fromBpm = PreferenceUtil.readPreferences(SettingActivity.this, PrefFromBpm, PrefFromBpmDefaultValue);
        toBpm = PreferenceUtil.readPreferences(SettingActivity.this, PrefToBpm, PrefToBpmDefaultValue);
        changeTime = PreferenceUtil.readPreferences(SettingActivity.this, PrefChangeTime, PrefChangeTimeDefaultValue);

        sbFromBpm = (SeekBar) findViewById(R.id.sbFromBpm);
        tvFromBpm = (TextView) findViewById(R.id.tvFromBpm);
        sbToBpm = (SeekBar) findViewById(R.id.sbToBpm);
        tvToBpm = (TextView) findViewById(R.id.tvToBpm);
        sbChangeTime = (SeekBar) findViewById(R.id.sbChangeTime);
        tvChangeTime = (TextView) findViewById(R.id.tvChangeTime);
        sbFromBpm.setMax(270);
        sbToBpm.setMax(270);
        sbChangeTime.setMax(59);
        sbFromBpm.setProgress(fromBpm - 30);
        tvFromBpm.setText(String.valueOf(fromBpm));
        sbToBpm.setProgress(toBpm - 30);
        tvToBpm.setText(String.valueOf(toBpm));
        sbChangeTime.setProgress(changeTime - 1);
        tvChangeTime.setText(String.valueOf(changeTime));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        radioGroup = (RadioGroup) findViewById(R.id.bpmType);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton) findViewById(checkedId);
                Toast.makeText(SettingActivity.this, rb.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        sbFromBpm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                //int inVal = Integer.parseInt(String.valueOf(seekBar.getProgress())) + 30;
                //PreferenceUtil.savePreferences(SettingActivity.this, PrefFromBpm, inVal);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvFromBpm.setText(String.valueOf(progress + 30));
            }
        });

        sbToBpm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvToBpm.setText(String.valueOf(progress + 30));
            }
        });

        sbChangeTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvChangeTime.setText(String.valueOf(progress + 1));
            }
        });

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fromBpm = Integer.parseInt(String.valueOf(sbFromBpm.getProgress())) + 30;
                int toBpm = Integer.parseInt(String.valueOf(sbToBpm.getProgress())) + 30;
                int changeTime = Integer.parseInt(String.valueOf(sbChangeTime.getProgress())) + 1;

                if (fromBpm < toBpm) {
                    PreferenceUtil.savePreferences(SettingActivity.this, PrefFromBpm, fromBpm);
                    PreferenceUtil.savePreferences(SettingActivity.this, PrefToBpm, toBpm);
                    PreferenceUtil.savePreferences(SettingActivity.this, PrefChangeTime, changeTime);
                    goToMainActivity();
                } else {
                    Toast.makeText(SettingActivity.this, "'از BPM' باید کوچکتر از 'تا BPM' باشد.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
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

        if (id == R.id.mainPage) {
            goToMainActivity();
        }

        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    private void goToMainActivity() {
        try {
            startActivity(new Intent(SettingActivity.this, MainActivity.class));
        } catch (Exception e) {

        }
    }
}
