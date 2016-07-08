package android.vaunt.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.vaunt.utility.UserInformation;
import android.vaunt.utility.Utils;
import android.vaunt.web.ServerConnect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LiveListFragment extends BaseFragment implements OnClickListener{

	// Holds activity delegate instance
	private MainActivity delegate;

	// View members
	private View view;
    private LinearLayout lyContainer;

    // Data members
//    private ArrayList<JSONObject> sections;
    ImageLoader mImageLoader;

	public static LiveListFragment newInstance() {
		return new LiveListFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_live_list, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();

		initViewAndClassMembers();
        presentData();
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;
        vi.findViewById(R.id.btn_menu).setOnClickListener(this);
        vi.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);

        lyContainer = (LinearLayout)vi.findViewById(R.id.linearLayoutContainer);
        mImageLoader = ImageLoader.getInstance();
	}

    void presentData(){
        lyContainer.removeAllViews();

        for(int i = 0; i < DBUtil.getInstance().liveArray.length();i ++){
            if(i > 0){
                final RelativeLayout rlySeperator = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_separator, null));

                lyContainer.addView(rlySeperator);
            }

            try {
                final JSONObject jsonObject = DBUtil.getInstance().liveArray.getJSONObject(i);

                final RelativeLayout newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_live_list, null));

                String strName = jsonObject.getString(Constants.ITEM_TITLE).toUpperCase();
                String strText = jsonObject.getString(Constants.ITEM_TEXT);
                String strImageName = jsonObject.getString(Constants.ITEM_BIG_IMAGE);
                String strOnline = jsonObject.getString(Constants.ITEM_ISONLINE);
                String strMA = jsonObject.getString(Constants.ITEM_MA);

                int resID = getResources().getIdentifier(strImageName, "drawable", getActivity().getPackageName());

                ((ImageView)newCell.findViewById(R.id.imgvPhoto)).setImageResource(resID);

                if(strOnline.equals(Constants.NO)){
                    ((TextView)newCell.findViewById(R.id.tvOnlineStatus)).setText("Offline");
                    ((TextView)newCell.findViewById(R.id.tvOnlineStatus)).setTextColor(0xFFF15022);
                }

                if(strMA.equals(Constants.NO)){
                    ((ImageView)newCell.findViewById(R.id.imgvMA)).setVisibility(View.INVISIBLE);
                }

                ((TextView)newCell.findViewById(R.id.tvName)).setText(strName);
                ((TextView)newCell.findViewById(R.id.tvDescription)).setText(strText);

                newCell.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeDetailFragment fragment = HomeDetailFragment.newInstance();
                        fragment.setJsonObject(jsonObject);
                        fragment.setMode(Constants.FROM_LIVE);
                        baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                                true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
                    }
                });

                lyContainer.addView(newCell);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_search) {

		} else if (v.getId() == R.id.btn_menu) {
            delegate.toggleSlideMenu();
        } else  if (v.getId() == R.id.imgvHeaderLogo){
            delegate.menuFragment.selectHomeItem();

            HomeFragment fragment = HomeFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }
	}

    public void playLiveVideo(Publisher publisherObj){
        LiveTrialFragment fragment = LiveTrialFragment.newInstance();

        fragment.setPublisherObject(publisherObj);
        fragment.setMode(1);

        baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mImageLoader != null) mImageLoader.stop();
    }
}
