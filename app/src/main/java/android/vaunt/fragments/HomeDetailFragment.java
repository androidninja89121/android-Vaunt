package android.vaunt.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.BaseActivity.CUSTOM_ANIMATIONS;
import android.vaunt.activities.MainActivity;
import android.vaunt.activities.OnboardingActivity;
import android.vaunt.activities.VideoPlayActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.DBUtil;
import android.vaunt.utility.Utils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeDetailFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;

    private JSONObject jsonObject;
    private int mode;
	// View members
	private View view;
    private LinearLayout lyContainer, lyCommentContainer;
    TextView tvVideos, tvPast, tvRelated, tvTitle, tvText, tvViewers, tvTwSubscribers, tvFbSubscribers;
    ImageView imgv_underbar_videos, imgv_underbar_past, imgv_underbar_related, imgvCover, imgvBig;
    // Data members
    private ArrayList<JSONObject> m_channelData;
    ImageLoader mImageLoader;
    boolean isClicked;

    private int mRes[] = {R.drawable.sample_pic_1, R.drawable.sample_pic_2, R.drawable.sample_pic_3, R.drawable.sample_pic_4, R.drawable.sample_pic_5, R.drawable.sample_pic_6, R.drawable.sample_pic_7};
    private String arrCommentTitle[] = {"Viewr A.//2 Hours ago", "Viewr A.//3 Hours ago", "Viewr A.//3 Hours ago", "Viewr A.//5 Hours ago", "Viewr A.//5 Hours ago"};
    private String arrCommentContent[] = {"Lorem ipsum dolor sit amet, sapien etiam, nunc amet dolor ac odio mauris justo.Luctus arcu, urna praesent at id quisque ac.",
                                        "Lorem ipsum dolor sit amet, sapien etiam, nunc amet dolor ac odio mauris justo.",
                                        "Lorem ipsum dolor sit amet, sapien etiam, nunc amet dolor ac odio",
                                        "Lorem ipsum dolor sit amet, sapien etiam, nunc amet dolor ac odio aliquam wisi. Nulla wisi laoreet suspendisse integer vivamus elit eu mauris",
                                        "Lorem ipsum dolor sit amet, sapien etiam, nunc amet dolor ac odio aliquam wisi. Nulla wisi laoreet suspendisse integer vivamus elit eu mauris"};

    ScrollView parentScrollView, childScrollView, commentScrollView;
    RelativeLayout rlyContent, rlyTotalComment;
    int initialY = 0, initialYComment = 0;
    boolean isPopped = false;

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static HomeDetailFragment newInstance() {
		return new HomeDetailFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_home_detail, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();

		// initialize
//        prepareData();
		initViewAndClassMembers();
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;
        vi.findViewById(R.id.btn_back).setOnClickListener(this);
        lyContainer = (LinearLayout)vi.findViewById(R.id.linearLayoutContainerOther);
        parentScrollView = (ScrollView)vi.findViewById(R.id.scrollView);
        childScrollView = (ScrollView)vi.findViewById(R.id.scrollViewOther);
        rlyContent = (RelativeLayout)vi.findViewById(R.id.rlyContent);
        rlyTotalComment = (RelativeLayout)vi.findViewById(R.id.rlyTotalComment);

        commentScrollView = (ScrollView)vi.findViewById(R.id.scrollViewComment);
        lyCommentContainer = (LinearLayout)vi.findViewById(R.id.lyCommentContainer);

        vi.findViewById(R.id.tab_videos).setOnClickListener(this);
        vi.findViewById(R.id.tab_past).setOnClickListener(this);
        vi.findViewById(R.id.tab_related).setOnClickListener(this);
        vi.findViewById(R.id.rlySubscribe).setOnClickListener(this);
        vi.findViewById(R.id.rlyCommentButton).setOnClickListener(this);
        vi.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);

        tvVideos = (TextView)vi.findViewById(R.id.tv_videos);
        tvPast = (TextView)vi.findViewById(R.id.tv_past);
        tvRelated = (TextView)vi.findViewById(R.id.tv_related);

        imgv_underbar_past = (ImageView)vi.findViewById(R.id.imgv_underbar_past);
        imgv_underbar_videos = (ImageView)vi.findViewById(R.id.imgv_underbar_videos);
        imgv_underbar_related = (ImageView)vi.findViewById(R.id.imgv_underbar_related);

        imgvCover = (ImageView)vi.findViewById(R.id.imgvCover);
        imgvBig = (ImageView)vi.findViewById(R.id.imgvBig);

        tvTitle = (TextView)vi.findViewById(R.id.tvTitle);
        tvText = (TextView)vi.findViewById(R.id.tvText);
        tvViewers = (TextView)vi.findViewById(R.id.tvViewers);
        tvTwSubscribers = (TextView)vi.findViewById(R.id.tvTwSubscribers);
        tvFbSubscribers = (TextView)vi.findViewById(R.id.tvFbSubscribers);

        Display display = delegate.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

//        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height - 550);
//        childScrollView.setLayoutParams(parms);
        rlyContent.getLayoutParams().width = width;
        rlyContent.getLayoutParams().height = height - 450;

        parentScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.v(TAG, "PARENT TOUCH");

                view.findViewById(R.id.scrollViewOther).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                view.findViewById(R.id.scrollViewComment).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        childScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.v(TAG, "CHILD TOUCH");

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        commentScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.v(TAG, "CHILD TOUCH");

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        childScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                isClicked = false;
                int scrollY = childScrollView.getScrollY();
                int newY = parentScrollView.getScrollY() + (scrollY - initialY) / 2;

                parentScrollView.scrollTo(0, newY);
                initialY = scrollY;
            }
        });

        commentScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollY = commentScrollView.getScrollY();
                int newY = parentScrollView.getScrollY() + (scrollY - initialYComment) / 2;

                parentScrollView.scrollTo(0, newY);
                initialYComment = scrollY;
            }
        });

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlyTotalComment.getLayoutParams();
        params.leftMargin = width;
        rlyTotalComment.setLayoutParams(params);

        presentScrollViewData();

        if(this.jsonObject != null){
            setHiddenScrollViewData(true);
            presentMainData();
        }

        presentCommentData();
    }

    void presentMainData(){
        try {
            String strCoverImageName = this.jsonObject.getString(Constants.ITEM_COVER_IMAGE);
            int resCoverImageID = getResources().getIdentifier(strCoverImageName, "drawable", getActivity().getPackageName());
            String strBigImageName = this.jsonObject.getString(Constants.ITEM_BIG_IMAGE);
            int resBigImageID = getResources().getIdentifier(strBigImageName, "drawable", getActivity().getPackageName());

            String strTitle = this.jsonObject.getString(Constants.ITEM_TITLE).toUpperCase();
            String strText = this.jsonObject.getString(Constants.ITEM_TEXT);
            String strViewers = this.jsonObject.getString(Constants.ITEM_VIEWERS);
            String strFbSubscribers = this.jsonObject.getString(Constants.ITEM_FB_SCRIBERS);
            String strTwSubscribers = this.jsonObject.getString(Constants.ITEM_TW_SCRIBERS);

            this.imgvCover.setImageResource(resCoverImageID);
            this.imgvBig.setImageResource(resBigImageID);
            this.tvTitle.setText(strTitle);
            this.tvText.setText(strText);
            this.tvViewers.setText(strViewers);
            this.tvTwSubscribers.setText(strTwSubscribers);
            this.tvFbSubscribers.setText(strFbSubscribers);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void presentScrollViewData(){
        lyContainer.removeAllViews();

        for(int i = 0; i < mRes.length;i ++){
            final RelativeLayout newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_home_detail, null));

            if(this.jsonObject == null){
                int nRes = mRes[i];

                ((ImageView)newCell.findViewById(R.id.imgvPhoto)).setImageResource(nRes);

                if(i < 3){
                    ((ImageView)newCell.findViewById(R.id.imgvCover)).setVisibility(View.INVISIBLE);
                    ((ImageView)newCell.findViewById(R.id.imgvLoc)).setVisibility(View.INVISIBLE);

                    newCell.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 500ms
                                    if (isClicked) {
                                        startActivity(new Intent(delegate, VideoPlayActivity.class));
                                    }

                                }
                            }, 500);
                            isClicked = true;
                            return false;
                        }
                    });


                }else{
                    ((ImageView)newCell.findViewById(R.id.imgvCover)).setVisibility(View.VISIBLE);
                    ((ImageView)newCell.findViewById(R.id.imgvLoc)).setVisibility(View.VISIBLE);
                }
            }else{
                newCell.findViewById(R.id.imgvPhoto).setVisibility(View.INVISIBLE);
                newCell.findViewById(R.id.imgvCover).setVisibility(View.INVISIBLE);
                newCell.findViewById(R.id.tvText).setVisibility(View.INVISIBLE);
                newCell.findViewById(R.id.tvTitle).setVisibility(View.INVISIBLE);
                newCell.findViewById(R.id.imgvLoc).setVisibility(view.INVISIBLE);
            }

            lyContainer.addView(newCell);
        }
    }

    void presentCommentData(){
        lyCommentContainer.removeAllViews();

        for(int i = 0; i < arrCommentTitle.length;i ++){
            if(i > 0){
                final RelativeLayout newCellSeparator = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_separator_blue, null));
                lyCommentContainer.addView(newCellSeparator);
            }
            final RelativeLayout newCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_comment_list, null));

            ((TextView)newCell.findViewById(R.id.tvCommentTitle)).setText(arrCommentTitle[i]);
            ((TextView)newCell.findViewById(R.id.tvCommentContent)).setText(arrCommentContent[i]);

            lyCommentContainer.addView(newCell);
        }
    }

	@Override
	public void onClick(View v) {
        if(v.getId() == R.id.rlySubscribe){
            SubscribeFragment fragment = SubscribeFragment.newInstance();
            fragment.setJsonObject(this.jsonObject);
            fragment.setMode(this.mode);
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    false, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
            return;
        }

        if(v.getId() == R.id.imgvHeaderLogo){
            delegate.menuFragment.selectHomeItem();

            HomeFragment fragment = HomeFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }

        if(v.getId() == R.id.rlyCommentButton){
            Display display = delegate.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            final int width = size.x;

            isPopped = !isPopped;

            Animation a = new Animation() {

                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int nLeft = (int)(width * interpolatedTime);

                    if(isPopped){
                        nLeft = width - nLeft;
                    }

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlyTotalComment.getLayoutParams();
                    params.leftMargin = nLeft;
                    rlyTotalComment.setLayoutParams(params);
//                    rlyTotalComment.requestLayout();
                }
            };
            a.setDuration(100); // in ms
            rlyTotalComment.startAnimation(a);
            parentScrollView.scrollTo(parentScrollView.getScrollX(), parentScrollView.getScrollY() + 1);
            parentScrollView.scrollTo(parentScrollView.getScrollX(), parentScrollView.getScrollY() - 1);
        }

		if (v.getId() == R.id.btn_back) {
            BaseFragment fragment = new BaseFragment();

            if(this.mode == Constants.FROM_HOME){
                fragment = HomeFragment.newInstance();
            }else if(this.mode == Constants.FROM_BROWSE){
                fragment = BrowseFragment.newInstance();
            }else if(this.mode == Constants.FROM_LIVE){
                fragment = LiveListFragment.newInstance();
            }

            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    false, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
		}else if(v.getId() == R.id.tab_videos || v.getId() == R.id.tab_past || v.getId() == R.id.tab_related){
            tvVideos.setTypeface(null, Typeface.NORMAL);
            tvPast.setTypeface(null, Typeface.NORMAL);
            tvRelated.setTypeface(null, Typeface.NORMAL);

            imgv_underbar_past.setVisibility(View.INVISIBLE);
            imgv_underbar_videos.setVisibility(View.INVISIBLE);
            imgv_underbar_related.setVisibility(View.INVISIBLE);

            if(v.getId() == R.id.tab_videos){
                tvVideos.setTypeface(null, Typeface.BOLD);
                imgv_underbar_videos.setVisibility(View.VISIBLE);
                if(jsonObject == null){
                    setHiddenScrollViewData(false);
                }else{
                    setHiddenScrollViewData(true);
                }

            }

            if(v.getId() == R.id.tab_past){
                tvPast.setTypeface(null, Typeface.BOLD);
                imgv_underbar_past.setVisibility(View.VISIBLE);
                setHiddenScrollViewData(true);
            }

            if(v.getId() == R.id.tab_related){
                tvRelated.setTypeface(null, Typeface.BOLD);
                imgv_underbar_related.setVisibility(View.VISIBLE);
                setHiddenScrollViewData(true);
            }

        }
	}

    void setHiddenScrollViewData(boolean flag){
        for(int i = 0; i < lyContainer.getChildCount(); i ++){
            View view = lyContainer.getChildAt(i);

            if(flag){
                view.setVisibility(View.INVISIBLE);
            }else{
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mImageLoader != null) mImageLoader.stop();
    }
}
