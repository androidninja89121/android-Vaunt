package android.vaunt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.utility.Utils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;


public class VideoPlayActivity extends BaseActivity implements View.OnClickListener{
    ImageView imgvSpinner;
    VideoView videoview;
    private String url = "https://d27u6i01i8lsjh.cloudfront.net/videos/Candice+Swanepoel+on+Vimeo.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_play);

        imgvSpinner = (ImageView)findViewById(R.id.imgvSpinner);

        AnimationDrawable spinner = (AnimationDrawable) imgvSpinner.getBackground();
        spinner.start();

        findViewById(R.id.rlytDone).setOnClickListener(this);

        videoview = (VideoView)findViewById(R.id.videoView);

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(url);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                imgvSpinner.setVisibility(View.INVISIBLE);
                videoview.start();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.rlytDone){
            videoview.stopPlayback();
            finish();
        }
    }
}
