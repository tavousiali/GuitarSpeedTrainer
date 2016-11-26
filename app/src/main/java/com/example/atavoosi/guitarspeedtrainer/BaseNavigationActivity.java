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

    protected void onCreateBase(boolean hasDrawer) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!hasDrawer)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        try {
            String label = getResources().getString(getPackageManager().getActivityInfo(getComponentName(), 0).labelRes);
            TextView textView = (TextView) toolbar.findViewById(R.id.activity_title);
            textView.setText(label);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void onBackClicked(Callable func, String className) {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (className == "MainActivity") {
                if (doubleBackToExitPressedOnce) {
                    try {
                        func.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    super.onBackPressed();
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
            } else {
                super.onBackPressed();
            }
        }

    }

    public void onNavigationItemSelected(MenuItem item, Context context) {
        drawer.closeDrawer(GravityCompat.END);

        if ((context.getClass().getName().contains("MainActivity") && item.getItemId() == R.id.mainPage) ||
                (context.getClass().getName().contains("SettingActivity") && item.getItemId() == R.id.setting) ||
                (context.getClass().getName().contains("SimilarAppActivity") && item.getItemId() == R.id.similarapp) ||
                (context.getClass().getName().contains("ContactusActivity") && item.getItemId() == R.id.contactus) ||
                (context.getClass().getName().contains("AboutActivity") && item.getItemId() == R.id.about))
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
        } else if (id == R.id.contactus) {
            try {
                startActivity(new Intent(context, ContactusActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.similarapp) {
            try {
                startActivity(new Intent(context, SimilarAppActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreateOptionsMenuBase(Menu menu, boolean isBackButton) {
        getMenuInflater().inflate(R.menu.main, menu);

        if (isBackButton)
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_menu_back));
    }

    public boolean onOptionsItemSelectedBase(MenuItem item, boolean isBackButton) {
        int id = item.getItemId();

        if (id == R.id.navigetion_icon) {
            if (isBackButton) {
                finish();
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
