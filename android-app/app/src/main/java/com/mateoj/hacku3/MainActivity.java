package com.mateoj.hacku3;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import hugo.weaving.DebugLog;

/**
 * Created by jose on 2/9/15.
 */
public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView helloWorld = (TextView) findViewById(R.id.text1);
        helloWorld.setText(R.string.hello_world);
        helloWorld.setTextColor(Color.BLUE);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    @DebugLog
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings)
            Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}
