package com.example.atavoosi.guitarspeedtrainer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SimilarAppActivity extends BaseNavigationActivity {
// region ## EVENTS ##

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cdn.persiangig.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);

        Call<SimilarAppModel> call = service.getSimilarApp();
        call.enqueue(new Callback<SimilarAppModel>() {
            @Override
            public void onResponse(Call<SimilarAppModel> call, Response<SimilarAppModel> response) {
                Log.d("Ali", response.body().SimilarApp.toString());
            }

            @Override
            public void onFailure(Call<SimilarAppModel> call, Throwable t) {

            }
        });

        super.setContent(R.layout.activity_similar_app);
        super.onCreate(savedInstanceState);
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
        super.onNavigationItemSelected(item, this);
        return true;
    }

    //endregion
}
