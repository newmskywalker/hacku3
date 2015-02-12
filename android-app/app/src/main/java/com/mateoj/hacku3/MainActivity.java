package com.mateoj.hacku3;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.mateoj.hacku3.models.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by jose on 2/9/15.
 */
public class MainActivity extends ActionBarActivity {
    private List<Category> allCategories = new ArrayList<>();
    private List<Category> selectedCategories = new ArrayList<>();
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.categoriesList);
//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setEndpoint("http://mateoj.com/endpoint")
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .build();
//        APIService apiService = restAdapter.create(APIService.class);
//        apiService.getCategories(new Callback<List<Category>>() {
//            @Override
//            public void success(List<Category> categories, Response response) {
//                if( simpleAdapter != null)
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });
        final ArrayList<HashMap<String, String>> hashList = new ArrayList<>();
        HashMap<String, String> hashItem;
        for(int i = 0; i < 20; i++){
            Category category = new Category();
            category.setId(i);
            category.setName("Cateogry " + i);
            allCategories.add(category);
            hashItem = new HashMap<>();
            hashItem.put("line1", category.getName());
            hashList.add(hashItem);
        }

        simpleAdapter = new SimpleAdapter(this, hashList, android.R.layout.simple_list_item_1, new String[]{"line1"}, new int[]{android.R.id.text1});
        listView.setAdapter(simpleAdapter);
     

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
