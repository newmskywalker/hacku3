package com.mateoj.hacku3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
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

        private void fade(View outgoing, View incoming) {
            AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
            AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setDuration(500);
            fadeOut.setDuration(500);
            outgoing.setAnimation(fadeOut);
            incoming.setAnimation(fadeIn);
            outgoing.setVisibility(View.INVISIBLE);
            incoming.setVisibility(View.VISIBLE);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            final CardView topicSelection = (CardView) view.findViewById(R.id.topicSelection);
            topicSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CardView modeSelection = (CardView) view.findViewById(R.id.modeSelection);
                    fade(topicSelection, modeSelection);
                    modeSelection.findViewById(R.id.dismissModeSelection).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fade(modeSelection, topicSelection);
                        }
                    });
                    modeSelection.findViewById(R.id.assessmentButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fade(modeSelection, topicSelection);
                            Assessment.getCurrentAssessment().setCurrentMode(Assessment.Mode.ForReals);
                            Assessment.getCurrentAssessment().setTopic(items.get(viewHolder.getPosition()));
                            startAssessment();
                        }
                    });
                    modeSelection.findViewById(R.id.practiceButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fade(modeSelection, topicSelection);
                            Assessment.getCurrentAssessment().setCurrentMode(Assessment.Mode.Practice);
                            Assessment.getCurrentAssessment().setTopic(items.get(viewHolder.getPosition()));
                            startAssessment();
                        }
                    });
//                    Topic topic = allCategories.get(viewHolder.getPosition());
//                    Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
//                    intent.putExtra("categoryId", topic.getId());
//                    intent.putExtra("categoryName", topic.getName());
//                    startActivity(intent);
                }
            });
            return viewHolder;
        }

        private void startAssessment() {
            Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
            startActivity(intent);
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
