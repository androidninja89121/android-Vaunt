package android.vaunt.fragments;

import android.graphics.drawable.AnimationDrawable;
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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Administrator on 9/25/2015.
 */
public class VideoNewFragment extends BaseFragment implements View.OnClickListener {
    // Holds activity delegate instance
    private MainActivity delegate;

    // View members
    private View view;
    VideoView videoview;
    ImageView imgvSpinner;
    private String url;
    private int index;

    public void setIndex(int index) {
        this.index = index;
    }

    public static VideoNewFragment newInstance() {
        return new VideoNewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_video_new, container,
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

        vi.findViewById(R.id.rlytDone).setOnClickListener(this);

        imgvSpinner = (ImageView)vi.findViewById(R.id.imgvSpinner);

        AnimationDrawable spinner = (AnimationDrawable) imgvSpinner.getBackground();
        spinner.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlytDone) {
            WatchFragment fragment = WatchFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }
    }


}
