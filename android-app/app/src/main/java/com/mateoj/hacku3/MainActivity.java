package com.mateoj.hacku3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mateoj.hacku3.models.Topic;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jose on 2/9/15.
 */
public class MainActivity extends ActionBarActivity {
    private List<Topic> allCategories = new ArrayList<>();
    private List<Topic> selectedCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.categoriesList);
        final TopicsAdapter topicsAdapter = new TopicsAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(topicsAdapter);
        IntervuService.getInstance().getCategories(new Callback<List<Topic>>() {
            @Override
            public void success(List<Topic> categories, Response response) {
                allCategories = categories;
                topicsAdapter.setItems(categories);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> {
        private List<Topic> items = new ArrayList<>();

        public TopicsAdapter() {
            for(int i = 0; i < 20; i++) {
                Topic topic = new Topic();
                topic.setName("Topic " + i);
                topic.setId(i);
                items.add(topic);
            }
        }

        public void setItems(List<Topic> theItems) {
            this.items = theItems;
            notifyDataSetChanged();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public ViewHolder(View itemView) {
                super(itemView);
                   name = (TextView) itemView.findViewById(R.id.categoryText);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Topic topic = allCategories.get(viewHolder.getPosition());
                    Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                    intent.putExtra("categoryId", topic.getId());
                    intent.putExtra("categoryName", topic.getName());
                    startActivity(intent);
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
           Topic topic = items.get(position);
           holder.name.setText(topic.getName());
        }

        @Override
        public int getItemCount() {
            return (items == null) ? 0 : items.size();
        }
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
