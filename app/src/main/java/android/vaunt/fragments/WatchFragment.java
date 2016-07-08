package android.vaunt.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.BaseActivity.CUSTOM_ANIMATIONS;
import android.vaunt.activities.MainActivity;
import android.vaunt.activities.VideoPlayActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.DBUtil;
import android.vaunt.utility.Publisher;
import android.vaunt.utility.Utils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class WatchFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;

	// View members
	private View view;
    private LinearLayout lyContainer;
    private TextView tvVideo, tvChannel;
    // Data members
    int[] arrResID = {R.drawable.sample_pic_1, R.drawable.sample_pic_2, R.drawable.sample_pic_3, R.drawable.sample_pic_4, R.drawable.sample_pic_5};
    int[] arrProgress = {45, 60, 50, 10, 30};
    int[] arrTime = {11, 8, 6, 23, 15};
    ImageLoader mImageLoader;

	public static WatchFragment newInstance() {
		return new WatchFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_watch, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();

		// initialize
		initViewAndClassMembers();
        presentData(0);
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;
        vi.findViewById(R.id.btn_menu).setOnClickListener(this);

        lyContainer = (LinearLayout)vi.findViewById(R.id.linearLayoutContainer);
        (vi.findViewById(R.id.rlytVideos)).setOnClickListener(this);
        (vi.findViewById(R.id.rlytChannels)).setOnClickListener(this);
        tvVideo = (TextView)vi.findViewById(R.id.tvVideos);
        tvChannel = (TextView)vi.findViewById(R.id.tvChannels);

        tvVideo.setTypeface(null, Typeface.BOLD);
        tvChannel.setTypeface(null, Typeface.NORMAL);
        mImageLoader = ImageLoader.getInstance();
	}

    void presentData(int mode){
        lyContainer.removeAllViews();

        if(mode == 0){
            for(int i = 0; i < 5; i ++){
                final RelativeLayout newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_watch_video, null));

                ((ImageView)newCell.findViewById(R.id.imgvVideo)).setImageResource(arrResID[i]);
                ((ProgressBar)newCell.findViewById(R.id.progressBar)).setProgress(arrProgress[i]);
                ((TextView)newCell.findViewById(R.id.tvTime)).setText(arrTime[i] + " min");

                newCell.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(delegate, VideoPlayActivity.class));
                    }
                });

                lyContainer.addView(newCell);
            }
        }else{
            for(int i = 0; i < DBUtil.getInstance().channelsArray.length(); i ++){
                try {
                    JSONObject jsonObject = DBUtil.getInstance().channelsArray.getJSONObject(i);

                    String strImageName = jsonObject.getString(Constants.ITEM_COVER_IMAGE);
                    String strTitle = jsonObject.getString(Constants.ITEM_TITLE);
                    String strIsMA = jsonObject.getString(Constants.ITEM_MA);
                    String strIsOnline = jsonObject.getString(Constants.ITEM_ISONLINE);
                    int resID = getResources().getIdentifier(strImageName, "drawable", getActivity().getPackageName());

                    final RelativeLayout newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_watch_channel, null));

                    ((DynamicImageView)newCell.findViewById(R.id.content_image)).setImageResource(resID);
                    ((TextView)newCell.findViewById(R.id.channel_title)).setText(strTitle);

                    if(strIsMA.equals(Constants.YES)){
                        newCell.findViewById(R.id.imgvMA).setVisibility(View.VISIBLE);
                    }else{
                        newCell.findViewById(R.id.imgvMA).setVisibility(View.INVISIBLE);
                    }

                    if(strIsOnline.equals(Constants.YES)){
                        newCell.findViewById(R.id.tvOnline).setVisibility(View.VISIBLE);
                        newCell.findViewById(R.id.tvOffline).setVisibility(View.INVISIBLE);
                    }else{
                        newCell.findViewById(R.id.tvOnline).setVisibility(View.INVISIBLE);
                        newCell.findViewById(R.id.tvOffline).setVisibility(View.VISIBLE);
                    }

                    lyContainer.addView(newCell);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public void playVideo(String strURL){


        VideoFragment fragment = VideoFragment.newInstance();
        fragment.setUrl(strURL);
        fragment.setIndex(-1);
        baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);

    }

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_menu) {
            delegate.toggleSlideMenu();
		} else if (v.getId() == R.id.rlytVideos) {
            presentData(0);
            tvVideo.setTypeface(null, Typeface.BOLD);
            tvChannel.setTypeface(null, Typeface.NORMAL);
        }else if(v.getId() == R.id.rlytChannels){
            presentData(1);
            tvVideo.setTypeface(null, Typeface.NORMAL);
            tvChannel.setTypeface(null, Typeface.BOLD);
        }
	}

    public void playLiveVideo(JSONObject section){
        LiveTrialFragment fragment = LiveTrialFragment.newInstance();

        fragment.setPublisherObject(new Publisher());
        fragment.setMode(0);

        baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
    }

    public void downloadAndPlayLiveVideoAtIndex(int nIndex){
//        try {
//            JSONObject section = DBUtil.getInstance().liveProfiles.getJSONObject(nIndex);
//
//            playLiveVideo(section);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mImageLoader != null) mImageLoader.stop();
    }
}
