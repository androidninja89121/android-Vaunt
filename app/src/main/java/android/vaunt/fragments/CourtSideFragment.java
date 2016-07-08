package android.vaunt.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
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
import android.vaunt.utility.Utils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 9/25/2015.
 */
public class CourtSideFragment extends BaseFragment implements View.OnClickListener {
    // Holds activity delegate instance
    private MainActivity delegate;

    // View members
    private View view;
    LinearLayout lyContainer;
    private int channelIndex;
    private ArrayList<JSONObject> sections;
    ImageLoader mImageLoader;

    public static CourtSideFragment newInstance() {
        return new CourtSideFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_courtside, container,
                false);
        return this.view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        delegate = (MainActivity) getActivity();

        prepareData();
        initViewAndClassMembers();
    }

    public void prepareData(){
        sections = new ArrayList<JSONObject>();
        try {
            JSONArray arrOne = DBUtil.getInstance().channelsArray.getJSONArray(channelIndex);

            for(int i = 0; i < arrOne.length(); i ++){
                JSONArray arrSecond = arrOne.getJSONArray(i);

                for(int j = 0; j < arrSecond.length(); j ++){
                    JSONObject jsonObj = arrSecond.getJSONObject(j);

                    jsonObj.put("section", i);
                    sections.add(jsonObj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initViewAndClassMembers() {

        View vi = this.view;
        vi.findViewById(R.id.btn_back).setOnClickListener(this);
        vi.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);
        lyContainer = (LinearLayout)vi.findViewById(R.id.linearLayoutContainer);
        mImageLoader = ImageLoader.getInstance();
        presentData();
    }

    void presentData(){
        lyContainer.removeAllViews();

        for(int i = 0; i < sections.size();i ++){
            final JSONObject element = sections.get(i);
            RelativeLayout newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.adapter_content_list_photo, null));
            try {
                String type = element.getString("type");

                if(type.equals("coverImage")){
                    newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.adapter_content_list_photo, null));

                    String strURL = element.getString("coverImage");

                    ((Button)newCell.findViewById(R.id.btnPlay)).setVisibility(View.INVISIBLE);
                    ((TextView)newCell.findViewById(R.id.content_title)).setVisibility(View.INVISIBLE);
                    ((DynamicImageView)newCell.findViewById(R.id.content_image)).loadImage(strURL, R.drawable.placeholder, mImageLoader);

                    int nSection = element.getInt("section");

                    if(nSection == 0){
                        ((RelativeLayout)newCell.findViewById(R.id.relativeLayoutSpace)).setVisibility(View.GONE);
                    }else{
                        ((RelativeLayout)newCell.findViewById(R.id.relativeLayoutSpace)).setVisibility(View.VISIBLE);
                    }

                }else if(type.equals("title")){
                    newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.adapter_content_list_title, null));

                    String strTitle = element.getString("title");
                    ((TextView)newCell.findViewById(R.id.tvTitle)).setText(strTitle);

                    if(element.has("titleDescription")){
                        String strTitleDescription = element.getString("titleDescription");
                        ((TextView)newCell.findViewById(R.id.tvTitleDescription)).setText(strTitleDescription);
                    }else{
                        ((TextView)newCell.findViewById(R.id.tvTitleDescription)).setVisibility(View.GONE);
                    }

                    if(!element.has("viewTrailer")){
                        ((Button)newCell.findViewById(R.id.btnWatchNow)).setVisibility(View.GONE);
                    }else{
                        ((Button)newCell.findViewById(R.id.btnWatchNow)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String strURL = null;
                                try {
                                    strURL = element.getString("trailerVideoURL");
                                    playVideo(strURL);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    if(element.has("text")){
                        String strText = element.getString("text");
                        ((TextView)newCell.findViewById(R.id.tvText)).setText(strText);
                    }else{
                        ((TextView)newCell.findViewById(R.id.tvText)).setVisibility(View.GONE);
                    }

                }else if(type.equals("videoCover")){
                    newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.adapter_content_list_photo, null));

                    String strURL = element.getString("videoCover");

                    ((Button)newCell.findViewById(R.id.btnPlay)).setVisibility(View.VISIBLE);
                    ((TextView)newCell.findViewById(R.id.content_title)).setVisibility(View.INVISIBLE);
                    ((DynamicImageView)newCell.findViewById(R.id.content_image)).loadImage(strURL, R.drawable.placeholder, mImageLoader);
                    ((RelativeLayout)newCell.findViewById(R.id.relativeLayoutSpace)).setVisibility(View.GONE);

                    ((Button) newCell.findViewById(R.id.btnPlay)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String strURL = null;
                            try {
                                strURL = element.getString("videoURL");
                                playVideo(strURL);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    ((Button)newCell.findViewById(R.id.btnPlay)).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            playLongClicked(element);
                            return false;
                        }
                    });
                }else if(type.equals("image")){
                    newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.adapter_content_list_photo, null));

                    String strURL = element.getString("filename");

                    ((Button)newCell.findViewById(R.id.btnPlay)).setVisibility(View.INVISIBLE);
                    ((DynamicImageView)newCell.findViewById(R.id.content_image)).loadImage(strURL, R.drawable.placeholder, mImageLoader);
                    ((RelativeLayout)newCell.findViewById(R.id.relativeLayoutSpace)).setVisibility(View.GONE);

                    if(element.has("text")){
                        String strText = element.getString("text");

                        strText = strText.replace("\r\r", "\n\n");

                        Spannable word = new SpannableString(strText);

                        int nPos = strText.indexOf("\n\n");

                        word.setSpan(new ForegroundColorSpan(0xFFF15022), nPos + 2, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
                        ((TextView)newCell.findViewById(R.id.content_title)).setText(word);
//                        ((TextView)newCell.findViewById(R.id.content_title)).setText(strText);
                    }else{
                        ((TextView)newCell.findViewById(R.id.content_title)).setVisibility(View.INVISIBLE);
                    }
                }
            } catch (JSONException e) {
            }

            lyContainer.addView(newCell);
        }
    }

    public void setChannelIndex(int channelIndex) {
        this.channelIndex = channelIndex;
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
        } else if (v.getId() == R.id.imgvHeaderLogo){
            delegate.menuFragment.selectHomeItem();

            HomeFragment fragment = HomeFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }
    }

    public void playVideo(String strURL){
        VideoFragment fragment = VideoFragment.newInstance();
        fragment.setUrl(strURL);
        fragment.setIndex(channelIndex);
        baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);

    }

    @SuppressLint("NewApi")
    public void playLongClicked(JSONObject section){

        section.remove("section");

        SharedPreferences mPrefs = baseActivity.getPreferences(Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mPrefs.getString(Constants.kVODWatchList, "");
        ArrayList<JSONObject> arrWatchList = gson.fromJson(json, new TypeToken<ArrayList<JSONObject>>() {
        }.getType());

        if(arrWatchList == null) arrWatchList = new ArrayList<JSONObject>();


        boolean isExist = false;

        String strJsonSection = gson.toJson(section);

        for(int i = 0; i < arrWatchList.size(); i ++){
            JSONObject jsonObj = arrWatchList.get(i);
            jsonObj.remove("section");
            String strJsonTmp = gson.toJson(jsonObj);

            if(strJsonSection.equals(strJsonTmp)){
                arrWatchList.remove(i);
                isExist = true;
                break;
            }
        }

        if(!isExist) arrWatchList.add(section);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        Gson gson1 = new Gson();
        String json1 = gson1.toJson(arrWatchList);
        prefsEditor.putString(Constants.kVODWatchList, json1);
        prefsEditor.commit();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mImageLoader != null) mImageLoader.stop();
    }
}
