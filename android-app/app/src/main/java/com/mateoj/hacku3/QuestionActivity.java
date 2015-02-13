package com.mateoj.hacku3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.mateoj.hacku3.models.Question;
import com.mateoj.hacku3.models.Video;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class QuestionActivity extends ActionBarActivity {
    private List<Question> questionList = new ArrayList<>();
    private static int CAPTURE_VIDEO_ACTIVITY_RESULT = 200;
    private int currentQuestionIndex = 0;
    private TextView questionDescription;
    private TextView recordButton;
    private VideoView videoView;
    private Camera mCamera;
    private CameraPreview cameraPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private TextView countDown;
    private CountDownTimer countDownTimer;
    ProgressDialog progressDialog;
    MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        mCamera = getCameraInstance();
        cameraPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.videoPreview);
        preview.addView(cameraPreview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        questionDescription = (TextView) findViewById(R.id.questionDescription);
        recordButton = (TextView) findViewById(R.id.recordButton);
        videoView = (VideoView) findViewById(R.id.answerVideo);
        countDown = (TextView) findViewById(R.id.countDown);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        if( Assessment.getCurrentAssessment().getTopic() == null) {
            throw new IllegalStateException("The topic has not been set");
        }

        countDownTimer = new CountDownTimer(15 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText("" + millisUntilFinished / 1000);
                if( millisUntilFinished / 1000 <= 5){
                    countDown.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }

            public void onFinish() {
                countDown.setText("Recording...");
                isTimerRunning = false;
                toggleRecording();
            }
        };
//        mCamera = getCameraInstance();
//        if( mCamera != null) {
//            cameraPreview = new CameraPreview(this, mCamera);
//            FrameLayout previewLayout = (FrameLayout) findViewById(R.id.videoPreview);
//            previewLayout.addView(cameraPreview);
//        }

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRecording();
            }
        });

        if( Assessment.getCurrentAssessment().currentMode == null)
            Assessment.getCurrentAssessment().currentMode = Assessment.Mode.Practice;

        IntervuService.getInstance().getQuestions("getallquestions", Assessment.getCurrentAssessment().getTopic().getId(), new Callback<List<Question>>() {
            @Override
            public void success(List<Question> questions, Response response) {
                questionList = questions;
                Log.d("Questions", questions.toString());
                progressDialog.dismiss();
                findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if( Assessment.getCurrentAssessment().currentMode == Assessment.Mode.Practice)
                            beginPractice();
                        else if( Assessment.getCurrentAssessment().currentMode == Assessment.Mode.ForReals)
                            beginAssessment();
                    }
                });
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
    private void toggleRecording(){
//        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, .4);
//        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_RESULT);
        if (isRecording) {
            // stop recording and release camera
            mMediaRecorder.stop();  // stop the recording

            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            setCurrentQuestionIndex(++currentQuestionIndex);
            setCaptureButtonText("Capture");
            isRecording = false;
        } else {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                Uri videoLocation = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                Video video = new Video();
                video.setLocalUri(videoLocation);
                video.setQuestionId(questionList.get(currentQuestionIndex).getId());
                Assessment.getCurrentAssessment().addVideo(video);
                Assessment.getCurrentAssessment().addQuestionAnswered(questionList.get(currentQuestionIndex));
                mMediaRecorder.setOutputFile(videoLocation.toString());
                mMediaRecorder.start();

                // inform the user that recording has started
                if( isTimerRunning)
                    countDownTimer.onFinish();

                setCaptureButtonText("Stop");
                isRecording = true;
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                // inform user
            }
        }

    }

    private void setCaptureButtonText(String text) {
        recordButton.setText(text);
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "EnterVu");
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
        isTimerRunning = true;
        countDownTimer.start();

    }

    private void beginPractice() {
        begin();
        setCurrentQuestionIndex(0);
    }

    private void beginAssessment() {
        begin();
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
        findViewById(R.id.readyInfo).setVisibility(View.INVISIBLE);
        findViewById(R.id.questionInfo).setVisibility(View.VISIBLE);
    }

    private void setCurrentQuestionIndex(int pos) {
        if( pos < questionList.size()) {
            currentQuestionIndex = pos;
            setQuestion(questionList.get(pos));
        } else {
            endAssesment();
        }
    }
    private void endAssesment() {
        startActivity(new Intent(this, AssessmentOverviewActivity.class));
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
    /** Check if this device has a camera */
    private boolean checkCameraHardware() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private boolean prepareVideoRecorder(){

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));

        // Step 4: Set output file
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(cameraPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d("Camera", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d("Camera", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(isTimerRunning)
            countDownTimer.cancel();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

}
