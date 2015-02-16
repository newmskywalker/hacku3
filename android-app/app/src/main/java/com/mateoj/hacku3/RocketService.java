package com.mateoj.hacku3;

import retrofit.RestAdapter;

/**
 * Created by jose on 2/13/15.
 */
public class RocketService {
    private static APIService instance;


    /**
     * Gets instance.
     *
     * @return Value of instance.
     */
    public static APIService getInstance() {
        if( instance == null ) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://rocketsciencewebstudio.com")
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .build();
            instance = restAdapter.create(APIService.class);

        }
        return instance;
    }
}
