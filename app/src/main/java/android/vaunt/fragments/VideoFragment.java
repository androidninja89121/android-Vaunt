package android.vaunt.fragments;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.MainActivity;
import android.vaunt.utility.Utils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by Administrator on 9/25/2015.
 */
public class VideoFragment extends BaseFragment implements View.OnClickListener {
    // Holds activity delegate instance
    private MainActivity delegate;

    // View members
    private View view;
    VideoView videoview;

    private String url = "https://d27u6i01i8lsjh.cloudfront.net/videos/Candice+Swanepoel+on+Vimeo.mp4";
    private int index;

    public void setIndex(int index) {
        this.index = index;
    }

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_video, container,
                false);
        return this.view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        delegate = (MainActivity) getActivity();

        // initialize
        initViewAndClassMembers();
    }

    void initViewAndClassMembers(){
        View vi = this.view;
        vi.findViewById(R.id.btn_back).setOnClickListener(this);
        vi.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);

        videoview = (VideoView)vi.findViewById(R.id.videoView);

        Utils.showProgDialog(delegate, null, "Buffering...", null);

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(delegate);
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
                Utils.hideProgDialog();
                videoview.start();
            }
        });
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void onBack(){
        videoview.stopPlayback();
        HomeDetailFragment fragment = HomeDetailFragment.newInstance();
        baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);

//        if(index == -1){
//            WatchFragment fragment = WatchFragment.newInstance();
//            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
//                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
//        }else{
//            CourtSideFragment fragment = CourtSideFragment.newInstance();
//            fragment.setChannelIndex(index);
//            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
//                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
//        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {

        } else if (v.getId() == R.id.btn_back) {
            onBack();
        } else if (v.getId() == R.id.imgvHeaderLogo){
            delegate.menuFragment.selectHomeItem();

            HomeFragment fragment = HomeFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }

    }


}
