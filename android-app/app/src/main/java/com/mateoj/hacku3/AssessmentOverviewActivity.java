package com.mateoj.hacku3;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.mateoj.hacku3.models.Question;
import com.mateoj.hacku3.models.Video;

import java.util.ArrayList;
import java.util.List;


public class AssessmentOverviewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_overview);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        VideosAdapter adapter = new VideosAdapter();
        adapter.setQuestions(Assessment.getCurrentAssessment().getQuestionsAnswered());
        adapter.setVideos(Assessment.getCurrentAssessment().getVideos());
        recyclerView.setAdapter(adapter);

    }

    public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {
        private List<Video> videos = new ArrayList<>();
        private List<Question> questions = new ArrayList<>();

        public VideosAdapter() {
        }

        public void setVideos(List<Video> theItems) {
            this.videos = theItems;
            notifyDataSetChanged();
        }

        public void setQuestions(List<Question> questions) {
            this.questions = questions;
            notifyDataSetChanged();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public VideoView videoView;
            public MediaController mediaController;
            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.questionName);
                videoView = (VideoView) itemView.findViewById(R.id.answerVideo);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assesment_overview_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Question question = questions.get(position);
            Video video = videos.get(position);
            holder.name.setText(question.getQuestion());
            holder.mediaController = new MediaController(holder.videoView.getContext());
            holder.mediaController.setAnchorView(holder.videoView);
            holder.videoView.setMediaController(holder.mediaController);
            holder.videoView.setVideoURI(video.getLocalUri());
            holder.videoView.start();
        }

        @Override
        public int getItemCount() {
            return (questions == null) ? 0 : questions.size();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assessment_overview, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
