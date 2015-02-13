package com.mateoj.hacku3;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by jose on 2/12/15.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "1szB5VAWP1Zy3XQ8soea34thR6L1QdugCBLLTmHw", "2bTcQGBP6u64ylYQmC58eQoK0C7RHKpjFRLxp0q9");

    }
}
