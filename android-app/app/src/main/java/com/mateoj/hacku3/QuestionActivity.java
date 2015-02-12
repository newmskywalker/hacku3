package com.mateoj.hacku3;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.mateoj.hacku3.models.Question;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class QuestionActivity extends ActionBarActivity {
    private int topicId;
    private List<Question> questionList = new ArrayList<>();
    private static int CAPTURE_VIDEO_ACTIVITY_RESULT = 200;
    private int currentQuestionIndex = 0;
    private TextView questionDescription;
    private Button practiceButton;
    private Button assesmentButton;
    private Button recordButton;
    private VideoView videoView;
    private Mode currentMode;
    private Camera camera;
    private CameraPreview cameraPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public enum Mode{
        Assesment,
        Practice
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questionDescription = (TextView) findViewById(R.id.questionDescription);
        practiceButton = (Button) findViewById(R.id.practiceButton);
        recordButton = (Button) findViewById(R.id.recordButton);
        videoView = (VideoView) findViewById(R.id.answerVideo);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

//        camera = getCameraInstance();
//        if( camera != null) {
//            cameraPreview = new CameraPreview(this, camera);
//            FrameLayout previewLayout = (FrameLayout) findViewById(R.id.videoPreview);
//            previewLayout.addView(cameraPreview);
//        }

        if( getIntent().hasExtra("categoryId"))
            topicId = getIntent().getIntExtra("categoryId", 0);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });
        practiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginPractice();
            }
        });


        IntervuService.getInstance().getQuestions("getallquestions", topicId, new Callback<List<Question>>() {
            @Override
            public void success(List<Question> questions, Response response) {
                questionList = questions;
                Log.d("Questions", questions.toString());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e("Camera", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam;
    }
    private void startRecording(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, .4);
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_RESULT);
//        camera.unlock();
//        MediaRecorder mediaRecorder = new MediaRecorder();
//        mediaRecorder.setCamera(camera);
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
//        mediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        //mediaRecorder.setPreviewDisplay(cameraPreview);

    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }



    private void setQuestion(Question question) {
        questionDescription.setText(question.getQuestion());

    }

    private void beginPractice() {
        begin();
        currentMode = Mode.Practice;
        setCurrentQuestionIndex(0);
    }

    private void beginAssessment() {
        begin();
        currentMode = Mode.Assesment;
        setCurrentQuestionIndex(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == CAPTURE_VIDEO_ACTIVITY_RESULT) {
            if( resultCode == RESULT_OK) {
                videoView.setVideoURI(data.getData());
                
                videoView.getDuration();
            }
        }
    }

    private void begin(){
        findViewById(R.id.modeButtons).setVisibility(View.GONE);
        findViewById(R.id.questionInfo).setVisibility(View.VISIBLE);
    }

    private void setCurrentQuestionIndex(int pos) {
        currentQuestionIndex = pos;
        setQuestion(questionList.get(pos));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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
