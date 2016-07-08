package android.vaunt.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.MainActivity;
import android.vaunt.activities.VideoPlayActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 9/25/2015.
 */
public class WatchNowFragment extends BaseFragment implements View.OnClickListener {
    // Holds activity delegate instance
    private MainActivity delegate;
    private JSONObject jsonObject;

    // View members
    private View view;
    private LinearLayout lyContainer;
    private int nCurrentVideo;
    ArrayList<String> arrVideo;
    ImageView imgvMain;
    TextView tvMain;

    public static WatchNowFragment newInstance() {
        return new WatchNowFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_watch_now, container,
                false);
        return this.view;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        delegate = (MainActivity) getActivity();

        initViewAndClassMembers();
        prepareData();
        presentData();
        setMainVideo(nCurrentVideo);
    }

    void prepareData(){
        arrVideo = new ArrayList<String>();
        for(int i = 0; i < 6; i ++){
            try {
                String strVideo = jsonObject.getString(Constants.ITEM_COVER_IMAGE);

                if(i % 2 == 1) strVideo = jsonObject.getString(Constants.ITEM_BIG_IMAGE);

                arrVideo.add(strVideo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        nCurrentVideo = 0;
    }

    void setMainVideo(int nIdx){
        int otherIdx = nIdx + 1;
        tvMain.setText("Courtside video" + otherIdx);
        String strImageName = arrVideo.get(nIdx);
        int resID = getResources().getIdentifier(strImageName, "drawable", getActivity().getPackageName());
        imgvMain.setImageResource(resID);
    }

    private void initViewAndClassMembers() {

        View vi = this.view;
        vi.findViewById(R.id.btn_back).setOnClickListener(this);
        vi.findViewById(R.id.btn_search).setOnClickListener(this);
        vi.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);
        lyContainer = (LinearLayout)vi.findViewById(R.id.linearLayoutContainer);
        imgvMain = (ImageView)vi.findViewById(R.id.imgvMain);
        tvMain = (TextView)vi.findViewById(R.id.tvMain);
        vi.findViewById(R.id.btnPlay).setOnClickListener(this);
    }

    public void presentData(){
        lyContainer.removeAllViews();

        for(int i = 0; i < 6; i ++){
            final RelativeLayout newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_video_item, null));

            String strImageName = arrVideo.get(i);
            int resID = getResources().getIdentifier(strImageName, "drawable", getActivity().getPackageName());
            final int j = i + 1;

            ((ImageView)newCell.findViewById(R.id.imgvVideo)).setImageResource(resID);
            ((TextView)newCell.findViewById(R.id.tvVideo)).setText("Courtside video " + j);

            newCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nCurrentVideo = j - 1;
                    setMainVideo(nCurrentVideo);
                }
            });

            lyContainer.addView(newCell);
        }
    }

    public void onBack(){
        CourtSideNewFragment fragment = CourtSideNewFragment.newInstance();
        fragment.setJsonObject(jsonObject);

        baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                false, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {

        } else if (v.getId() == R.id.btn_back) {
            onBack();
        } else if(v.getId() == R.id.btnPlay){
            startActivity(new Intent(delegate, VideoPlayActivity.class));
        } else if(v.getId() == R.id.imgvHeaderLogo){
            delegate.menuFragment.selectHomeItem();

            HomeFragment fragment = HomeFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }

    }
}
