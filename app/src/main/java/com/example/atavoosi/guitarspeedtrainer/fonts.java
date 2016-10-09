package com.example.atavoosi.guitarspeedtrainer;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by A.Tavoosi on 02/10/2016.
 */
public class fonts extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/BYekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
