package com.mateoj.hacku3;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mateoj.hacku3.models.Answer;
import com.mateoj.hacku3.models.Question;
import com.mateoj.hacku3.models.Video;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class AssessmentOverviewActivity extends ActionBarActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_overview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(Assessment.getCurrentAssessment().getCurrentMode() == Assessment.Mode.ForReals || Assessment.getCurrentAssessment().getCurrentMode() == Assessment.Mode.Practice) {

            VideosAdapter adapter = new VideosAdapter();
            adapter.setQuestions(Assessment.getCurrentAssessment().getQuestionsAnswered());
            adapter.setVideos(Assessment.getCurrentAssessment().getVideos());
            recyclerView.setAdapter(adapter);

        } else if( Assessment.getCurrentAssessment().getCurrentMode() == Assessment.Mode.History) {
            showHistory();
        }

        if( Assessment.getCurrentAssessment().getCurrentMode() == Assessment.Mode.ForReals)
            submitAssessment();
    }

    private void showHistory() {

        IntervuService.getInstance().getAnswers("getuserdata", 0, new Callback<List<Answer>>() {
            @Override
            public void success(List<Answer> answers, Response response) {
                RemoteVideosAdapter remoteVideosAdapter = new RemoteVideosAdapter();
                remoteVideosAdapter.setAnswers(answers);
                recyclerView.setAdapter(remoteVideosAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private boolean uploadVideos() {
//        Uri[] uris = new Uri[Assessment.getCurrentAssessment().getVideos().size()];
//        for(int i = 0; i < Assessment.getCurrentAssessment().getVideos().size(); i++){
//            uris[i] = Assessment.getCurrentAssessment().getVideos().get(i).getLocalUri();
//        }
//        new VideoAsyncUpload().execute(uris);
        return  true;
    }

    public class VideoAsyncUpload extends AsyncTask<Uri, Void, Void>{

        @Override
        protected Void doInBackground(Uri... params) {
            for(Uri videoLocalUri: params) {
                //uploadVideo(videoLocalUri);
                uploadVideoRetrofit(videoLocalUri);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(AssessmentOverviewActivity.this, "Videos uploaded", Toast.LENGTH_SHORT).show();
        }

        private void uploadVideoRetrofit(Uri uri) {
            try{
                FileInputStream fileInputStream = new FileInputStream(new File(uri.getPath()) );
                File file = new File(uri.getPath());
                Response response = RocketService.getInstance().uploadVideo(new TypedFile("video/mp4", file));
                Log.d("RESPONSE", response.toString());
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }

        private void uploadVideo(Uri uri) {
            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;
            DataInputStream inputStream = null;
            Uri pathToOurFile = uri;
            String urlServer = "http://mateoj.com/hacku3/handle_upload.php";
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary =  "*****";

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1*1024*1024;

            try
            {
                FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile.getPath()) );
                URL url = new URL(urlServer);
                connection = (HttpURLConnection) url.openConnection();

                // Allow Inputs & Outputs
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                // Enable POST method
                connection.setRequestMethod("POST");

                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary");

                outputStream = new DataOutputStream( connection.getOutputStream() );
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile );
                outputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // Read file
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();
                Log.d("ServerCode", "" + serverResponseCode);
                Log.d("serverResponseMessage",""+serverResponseMessage);
                fileInputStream.close();
                outputStream.flush();
                outputStream.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void createAnswers() {
        for(Question question : Assessment.getCurrentAssessment().getQuestionsAnswered()){
            IntervuService.getInstance().createAnswer("insertvideo",
                    1,
                    Assessment.getCurrentAssessment().getId(),
                    question.getId(),
                    "video-url",
                    new Callback<Answer>() {
                        @Override
                        public void success(Answer answer, Response response) {

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                        }
                    });
        }
    }

    private void submitAssessment() {
        IntervuService.getInstance().createAssessment("createassessment", 0, new Callback<Assessment>() {
            @Override
            public void success(Assessment assessment, Response response) {
                Assessment.getCurrentAssessment().setId(assessment.getId());
                if(uploadVideos()){
                    createAnswers();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
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

    public class RemoteVideosAdapter extends RecyclerView.Adapter<RemoteVideosAdapter.ViewHolder> {
        private List<Answer> videos = new ArrayList<>();

        public RemoteVideosAdapter() {
        }

        private void  setAnswers(List<Answer> videos) {
            this.videos = videos;
            notifyDataSetChanged();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public TextView feedback;
            public ImageView videoView;
            public MediaController mediaController;
            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.questionName);
                videoView = (ImageView) itemView.findViewById(R.id.answerVideo);
                feedback = (TextView) itemView.findViewById(R.id.feedback);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_overview_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Answer answer = videos.get(position);
            holder.name.setText(answer.getQuestion());
            holder.feedback.setText(answer.getFeedback());
            holder.mediaController = new MediaController(holder.videoView.getContext());
            holder.mediaController.setAnchorView(holder.videoView);
            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(answer.getVideolink())));
                }
            });
        }

        @Override
        public int getItemCount() {
            return (videos == null) ? 0 : videos.size();
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
