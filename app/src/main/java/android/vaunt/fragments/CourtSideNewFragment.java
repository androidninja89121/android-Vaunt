package android.vaunt.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.MainActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.DBUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 9/25/2015.
 */
public class CourtSideNewFragment extends BaseFragment implements View.OnClickListener {
    // Holds activity delegate instance
    private MainActivity delegate;

    // View members
    private View view;

    private JSONObject jsonObject;

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public static CourtSideNewFragment newInstance() {
        return new CourtSideNewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_courtside_new, container,
                false);
        return this.view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        delegate = (MainActivity) getActivity();

        initViewAndClassMembers();
    }

    private void initViewAndClassMembers() {

        View vi = this.view;
        vi.findViewById(R.id.btn_back).setOnClickListener(this);
        vi.findViewById(R.id.btn_search).setOnClickListener(this);
        vi.findViewById(R.id.btnWatchNow).setOnClickListener(this);
        vi.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);

        try {
            String strTitle = jsonObject.getString(Constants.ITEM_TITLE);
            String strSubTitle = jsonObject.getString(Constants.ITEM_SUB_TITLE);
            String strText = jsonObject.getString(Constants.ITEM_TEXT);

            String strImageNameTop = jsonObject.getString(Constants.ITEM_COVER_IMAGE);
            String strImageNameDown = jsonObject.getString(Constants.ITEM_BIG_IMAGE);

            int resIDTop = getResources().getIdentifier(strImageNameTop, "drawable", getActivity().getPackageName());
            int resIDDown = getResources().getIdentifier(strImageNameDown, "drawable", getActivity().getPackageName());

            ((TextView) vi.findViewById(R.id.tvTitle)).setText(strTitle);
            ((TextView) vi.findViewById(R.id.tvTitleDescription)).setText(strSubTitle);
            ((TextView) vi.findViewById(R.id.tvText)).setText(strText);

            ((ImageView) vi.findViewById(R.id.imgvTop)).setImageResource(resIDTop);
            ((ImageView) vi.findViewById(R.id.imgvBottom)).setImageResource(resIDDown);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onBack(){
        HomeFragment fragment = HomeFragment.newInstance();
        baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                false, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {

        } else if (v.getId() == R.id.btn_back) {
            onBack();
        } else if(v.getId() == R.id.btnWatchNow){
            WatchNowFragment fragment = WatchNowFragment.newInstance();
            fragment.setJsonObject(jsonObject);
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    false, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
        } else if (v.getId() == R.id.imgvHeaderLogo){
            delegate.menuFragment.selectHomeItem();

            HomeFragment fragment = HomeFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }
    }
}
