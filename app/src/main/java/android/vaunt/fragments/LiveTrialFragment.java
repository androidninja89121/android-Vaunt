package android.vaunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.MainActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.Publisher;
import android.vaunt.utility.UserInformation;
import android.vaunt.web.ServerConnect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 9/25/2015.
 */
public class LiveTrialFragment extends BaseFragment implements View.OnClickListener, ServerConnect.APIResponseListener{
    // Holds activity delegate instance
    private MainActivity delegate;

    // View members
    private View view;

    Publisher publisherObject;
    int mode;

    public void setPublisherObject(Publisher publisherObject) {
        this.publisherObject = publisherObject;
    }

    DynamicImageView imgvContent, imgvThumb;
    TextView tvName, tvLive, tvSubscribe, tvCount;
    ImageLoader mImageLoader;
    public void setMode(int mode) {
        this.mode = mode;
    }

    public static LiveTrialFragment newInstance() {
        return new LiveTrialFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_live_trial, container,
                false);
        return this.view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        delegate = (MainActivity) getActivity();

        initViewAndClassMembers();
        getData();
    }

    public void getData(){
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put(Constants.kUDAuthToken, UserInformation.getInstance().authTokenKey);
            ServerConnect.POST(Constants.kWSApiStreamInfo + "/" + publisherObject.uid, jsonParam.toString(), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initViewAndClassMembers() {
        imgvContent = (DynamicImageView)this.view.findViewById(R.id.content_image);
        imgvThumb = (DynamicImageView)this.view.findViewById(R.id.thumb_image);

        tvName = (TextView)this.view.findViewById(R.id.tvName);
        tvLive = (TextView)this.view.findViewById(R.id.tvLive);
        tvSubscribe = (TextView)this.view.findViewById(R.id.tvSubscribe);
        tvCount = (TextView)this.view.findViewById(R.id.tvCount);

        mImageLoader = ImageLoader.getInstance();

        ((RelativeLayout)this.view.findViewById(R.id.rlySubscribe)).setOnClickListener(this);
        ((RelativeLayout)this.view.findViewById(R.id.btn_back)).setOnClickListener(this);
        this.view.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);
    }

    public void setUI(){
        imgvContent.loadImage(publisherObject.banner, R.drawable.placeholder, mImageLoader);
        imgvThumb.loadImage(publisherObject.avatar, R.drawable.placeholder, mImageLoader);
        tvName.setText(publisherObject.fullName());
        tvSubscribe.setText("subscribeText");
        tvLive.setText("liveText");
        tvCount.setText("112");
    }

    public void onBack(){
        if(mode == 0){
            HomeFragment fragment = HomeFragment.newInstance();

            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }else if(mode == 1){
            LiveListFragment fragment = LiveListFragment.newInstance();

            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }
    }

    @Override
    public void onSuccess(JSONObject json) {
//        setUI();
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {

        } else if (v.getId() == R.id.btn_back) {
            onBack();
        } else if (v.getId() == R.id.rlySubscribe){
            LiveViewFragment fragment = LiveViewFragment.newInstance();

            fragment.setPublisherObject(publisherObject);
            fragment.setMode(mode);

            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
        } else if (v.getId() == R.id.imgvHeaderLogo){
            delegate.menuFragment.selectHomeItem();

            HomeFragment fragment = HomeFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mImageLoader != null) mImageLoader.stop();
    }
}
