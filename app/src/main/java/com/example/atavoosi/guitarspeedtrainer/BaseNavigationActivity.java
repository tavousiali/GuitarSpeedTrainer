package com.example.atavoosi.guitarspeedtrainer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Callable;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private DrawerLayout drawer;
    boolean doubleBackToExitPressedOnce = false;

    protected void setContent(int layout) {
        setContentView(layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            String label = getResources().getString(getPackageManager().getActivityInfo(getComponentName(), 0).labelRes);
            TextView textView = (TextView) toolbar.findViewById(R.id.activity_title);
            textView.setText(label);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void onBackClicked(Callable func) {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (doubleBackToExitPressedOnce) {
                try {
                    func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void onNavigationItemSelected(MenuItem item, Context context) {
        drawer.closeDrawer(GravityCompat.END);

        if ((context.getClass().getName().contains("MainActivity") && item.getItemId() == R.id.mainPage) ||
                (context.getClass().getName().contains("SettingActivity") && item.getItemId() == R.id.setting))
            return;

        int id = item.getItemId();

        if (id == R.id.setting) {
            try {
                startActivity(new Intent(context, SettingActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.mainPage) {
            try {
                startActivity(new Intent(context, MainActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.about) {
            try {
                startActivity(new Intent(context, AboutActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
