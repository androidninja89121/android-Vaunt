package android.vaunt.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.MainActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.Publisher;
import android.vaunt.utility.UserInformation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 9/25/2015.
 */
public class LiveViewFragment extends BaseFragment implements View.OnClickListener {
    // Holds activity delegate instance
    private MainActivity delegate;

    // View members
    private View view;

    Publisher publisherObject;
    int mode;

    DynamicImageView imgvContent;
    TextView tvLive, tvSubscribe, tvCount;
    EditText etxtComment;
    LinearLayout lyContainer;
    ImageLoader mImageLoader;

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static LiveViewFragment newInstance() {
        return new LiveViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_live_view, container,
                false);
        return this.view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        delegate = (MainActivity) getActivity();

        initViewAndClassMembers();
        setUI();
    }

    private void initViewAndClassMembers() {
        imgvContent = (DynamicImageView)this.view.findViewById(R.id.content_image);
        tvLive = (TextView)this.view.findViewById(R.id.tvLive);
        tvSubscribe = (TextView)this.view.findViewById(R.id.tvSubscribe);
        tvCount = (TextView)this.view.findViewById(R.id.tvCount);
        etxtComment = (EditText)this.view.findViewById(R.id.et_comment);
        lyContainer = (LinearLayout)this.view.findViewById(R.id.linearLayoutContainer);

        this.view.findViewById(R.id.btn_back).setOnClickListener(this);
        this.view.findViewById(R.id.rlySendButton).setOnClickListener(this);
        this.view.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);
        mImageLoader = ImageLoader.getInstance();
    }

    public void setPublisherObject(Publisher publisherObject) {
        this.publisherObject = publisherObject;
    }

    public void setUI(){
        imgvContent.loadImage(publisherObject.banner, R.drawable.placeholder, mImageLoader);
        tvSubscribe.setText("subscribeText");
        tvLive.setText("liveText");
        tvCount.setText("112");
    }

    public void onBack(){
        LiveTrialFragment fragment = LiveTrialFragment.newInstance();
        fragment.setMode(mode);
        fragment.setPublisherObject(publisherObject);

        baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
    }

    public void hideSoftKeyboard(){
        InputMethodManager imm =(InputMethodManager)baseActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onSend(){
        String strComment = etxtComment.getText().toString();
        if(strComment.length() == 0) return;

        if(lyContainer.getChildCount() > 0){
            LinearLayout lySeparator = new LinearLayout(baseActivity);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    5);
            lyContainer.addView(lySeparator, rlp);
        }

        final RelativeLayout newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_comment, null));

        ((ImageView)newCell.findViewById(R.id.imgvCell)).setImageBitmap(UserInformation.getInstance().bmpUser);
        ((TextView)newCell.findViewById(R.id.tvName)).setText(UserInformation.getInstance().userAccountName);
        ((TextView)newCell.findViewById(R.id.tvComment)).setText(strComment);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(new Date());

        ((TextView)newCell.findViewById(R.id.tvTimeStamp)).setText("Send " + currentTime);

        lyContainer.addView(newCell);

        hideSoftKeyboard();
        etxtComment.setText("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {

        } else if (v.getId() == R.id.btn_back) {
            onBack();
        } else if(v.getId() == R.id.rlySendButton){
            onSend();
        } else if(v.getId() == R.id.imgvHeaderLogo){
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
