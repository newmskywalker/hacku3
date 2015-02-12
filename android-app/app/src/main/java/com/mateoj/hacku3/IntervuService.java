package com.mateoj.hacku3;

import retrofit.RestAdapter;

/**
 * Created by jose on 2/12/15.
 */
public class IntervuService {
    private static APIService instance;


    /**
     * Gets instance.
     *
     * @return Value of instance.
     */
    public static APIService getInstance() {
        if( instance == null ) {
                    RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://mateoj.com/endpoint")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        instance = restAdapter.create(APIService.class);

        }
        return instance;
    }
}
