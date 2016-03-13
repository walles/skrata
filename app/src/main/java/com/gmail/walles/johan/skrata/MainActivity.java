package com.gmail.walles.johan.skrata;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private Intent shareIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem share = menu.findItem(R.id.menu_item_share);

        ShareActionProvider shareActionProvider = new ShareActionProvider(this);
        MenuItemCompat.setActionProvider(share, shareActionProvider);

        shareIntent = new Intent(Intent.ACTION_SEARCH);
        shareActionProvider.setShareIntent(shareIntent);

        return true;
    }

    public void setSearchString(CharSequence searchString) {
        if (shareIntent == null) {
            return;
        }

        // FIXME: Disable on empty search string? I tried to do that using
        // menuItem.setEnabled(false), but I didn't see any effect from that:
        // https://code.google.com/p/android/issues/detail?id=82318

        shareIntent.putExtra(SearchManager.QUERY, searchString.toString());
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
}
