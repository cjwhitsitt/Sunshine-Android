package com.jaywhitsitt.sunshine.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }

        // PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        final String BASE = "geo:0,0?";
        final String QUERY_PARAM = "q";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String locationKey = getString(R.string.pref_location_key);
        String locationDefault = getString(R.string.pref_location_default);
        String queryValue = prefs.getString(locationKey, locationDefault);

        Uri uri = Uri.parse(BASE).buildUpon()
                .appendQueryParameter(QUERY_PARAM,queryValue)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(uri);

        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage("Looks like you don't have an app that can handle showing a map. " +
                            "Please install one to display the entered location.")
                    .create();
            alertDialog.show();
        }
    }
}
