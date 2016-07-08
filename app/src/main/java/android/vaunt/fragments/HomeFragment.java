package android.vaunt.fragments;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.BaseActivity.CUSTOM_ANIMATIONS;
import android.vaunt.activities.MainActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.DBUtil;
import android.vaunt.utility.Publisher;
import android.vaunt.utility.Utils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;

	// View members
	private View view;
    private LinearLayout lyContainer;

    // Data members
    private ArrayList<JSONObject> m_channelData;
    ImageLoader mImageLoader;

	public static HomeFragment newInstance() {
		return new HomeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_home, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();

		// initialize
        prepareData();
		initViewAndClassMembers();
	}

    private void prepareData() {
        m_channelData = new ArrayList<JSONObject>();
        JSONObject channel;
        try {
            channel = new JSONObject();
            channel.put("title", "Original Series : Courtside");
            m_channelData.add(channel);

            channel = new JSONObject();
            channel.put("title", "Models: Runway");
            m_channelData.add(channel);

            channel = new JSONObject();
            channel.put("title", "Comedy: Kevin Hart");
            channel.put("liveProfileIndex", new Integer(Constants.KEVIN_HART_PROFILE_INDEX));
            m_channelData.add(channel);

            channel = new JSONObject();
            channel.put("title", "Sports: World Premiere");
            m_channelData.add(channel);

            channel = new JSONObject();
            channel.put("title", "Music: Chris Brown");
            channel.put("liveProfileIndex", new Integer(Constants.CHRIS_BROWN_PROFILE_INDEX));
            m_channelData.add(channel);

            channel = new JSONObject();
            channel.put("title", "Fitness: Jen Selter");
            channel.put("liveProfileIndex", new Integer(Constants.JEN_SELTER_PROFILE_INDEX));
            m_channelData.add(channel);

            channel = new JSONObject();
            channel.put("title", "Music: Nicki Minaj");
//            channel.put("liveProfileIndex", new Integer(Constants.NICKI_MINAJ_PROFILE_INDEX));
            m_channelData.add(channel);

            channel = new JSONObject();
            channel.put("title", "Lifestyle: Amber Rose");
            channel.put("liveProfileIndex", new Integer(Constants.AMBER_ROSE_PROFILE_INDEX));
            m_channelData.add(channel);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;
        vi.findViewById(R.id.btn_menu).setOnClickListener(this);
        vi.findViewById(R.id.btn_search).setOnClickListener(this);

        lyContainer = (LinearLayout)vi.findViewById(R.id.linearLayoutContainer);
        mImageLoader = ImageLoader.getInstance();

//        Display display = delegate.getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;

        presentData();
	}

    void presentData(){
        lyContainer.removeAllViews();

        for(int i = 0; i < DBUtil.getInstance().homeArray.length(); i ++){
            try {
                final JSONObject jsonObject = DBUtil.getInstance().homeArray.getJSONObject(i);
                final RelativeLayout newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.adapter_channel_list, null));

                String strImageName = jsonObject.getString(Constants.ITEM_COVER_IMAGE);
                String strCategory = jsonObject.getString(Constants.ITEM_CATEGORY);
                String strTitle = jsonObject.getString(Constants.ITEM_TITLE);
                String strIsLive = jsonObject.getString(Constants.ITEM_ISLIVE);

                int resID = getResources().getIdentifier(strImageName, "drawable", getActivity().getPackageName());

                ((TextView)newCell.findViewById(R.id.channel_title)).setText(strCategory + ": " + strTitle);
                ((DynamicImageView)newCell.findViewById(R.id.content_image)).setImageResource(resID);
                ((DynamicImageView)newCell.findViewById(R.id.content_image)).setScaleType(ImageView.ScaleType.FIT_XY);

                if(strIsLive.equals(Constants.YES)){
                    newCell.findViewById(R.id.imgvLive).setVisibility(View.VISIBLE);
                }else{
                    newCell.findViewById(R.id.imgvLive).setVisibility(View.INVISIBLE);
                }

                newCell.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String strCategory = jsonObject.getString(Constants.ITEM_CATEGORY);

                            if(strCategory.equals(Constants.CAT_ORIGINAL_SERIES)){
                                CourtSideNewFragment fragment = CourtSideNewFragment.newInstance();
                                fragment.setJsonObject(jsonObject);

                                baseActivity.showFragment(R.id.fl_fragment_container, fragment, true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
                            }else{
                                HomeDetailFragment fragment = HomeDetailFragment.newInstance();
                                fragment.setMode(Constants.FROM_HOME);

                                if(strCategory.equals(Constants.CAT_MODELS)){
                                    fragment.setJsonObject(null);
                                }else{
                                    fragment.setJsonObject(jsonObject);
                                }

                                baseActivity.showFragment(R.id.fl_fragment_container, fragment, true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                lyContainer.addView(newCell);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

//        for(int i = 0; i < m_channelData.size();i ++){
//            final JSONObject channel = m_channelData.get(i);
//            final RelativeLayout newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.adapter_channel_list, null));
//
//            String title = null;
//            String url = null;
//            try {
//                title = (String)channel.get("title");
//                url = title;
//                Utils.printLog(url);
//                url = url.replace(":", " -");
//                Utils.printLog(url);
//                url = url.replaceAll(" ", "%20");
//                Utils.printLog(url);
//                url = String.format("%s/%s/%s.png", Constants.kS3AWSImageBaseUrl, Constants.kS3CoreImage, url);
//                Utils.printLog(url);
//            } catch (JSONException e) {
//            }
//
//            if (title != null && url != null) {
//                ((TextView)newCell.findViewById(R.id.channel_title)).setText(title);
//                ((DynamicImageView)newCell.findViewById(R.id.content_image)).loadImage(url, R.drawable.placeholder, mImageLoader);
//            }
//
//            final  int position = i;
//
//            newCell.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                if(position == 0){
//                    CourtSideNewFragment fragment = CourtSideNewFragment.newInstance();
//
//                    baseActivity.showFragment(R.id.fl_fragment_container, fragment,
//                            true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
//                }else if(position == 1){
////                    CourtSideFragment fragment = CourtSideFragment.newInstance();
////                    fragment.setChannelIndex(1);
//                    HomeDetailFragment fragment = HomeDetailFragment.newInstance();
//                    fragment.setMode(Constants.FROM_HOME);
//                    baseActivity.showFragment(R.id.fl_fragment_container, fragment,
//                            true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
//                }else if(position == 3){
//                    CourtSideFragment fragment = CourtSideFragment.newInstance();
//                    fragment.setChannelIndex(2);
//                    baseActivity.showFragment(R.id.fl_fragment_container, fragment,
//                            true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
//                }else if(channel.has("liveProfileIndex")){
//                    try {
//                        int nProfileIndex = channel.getInt("liveProfileIndex");
//                        downloadAndPlayLiveVideoAtIndex(nProfileIndex);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                }
//            });
//
//            lyContainer.addView(newCell);
//        }
    }

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_search) {
            SearchFragment fragment = SearchFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		} else if (v.getId() == R.id.btn_menu) {
            delegate.toggleSlideMenu();
        }
	}

//    public void playLiveVideo(JSONObject section){
////        LiveTrialFragment fragment = LiveTrialFragment.newInstance();
////
//////        fragment.setSection(section);
////        fragment.setPublisherObject(new Publisher());
////        fragment.setMode(0);
////
////        baseActivity.showFragment(R.id.fl_fragment_container, fragment,
////                true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
//    }
//
//    public void downloadAndPlayLiveVideoAtIndex(int nIndex){
//        try {
//            JSONObject section = DBUtil.getInstance().liveProfiles.getJSONObject(nIndex);
//
//            playLiveVideo(section);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mImageLoader != null) mImageLoader.stop();
    }
}
