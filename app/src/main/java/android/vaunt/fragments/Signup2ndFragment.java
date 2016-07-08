package android.vaunt.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.CameraActivity;
import android.vaunt.activities.OnboardingActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.Utils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.baoyz.actionsheet.ActionSheet;
import com.github.OrangeGangsters.circularbarpager.library.CircularBarPager;

import java.io.File;

public class Signup2ndFragment extends BaseFragment implements OnClickListener{

	// Holds activity delegate instance
	private OnboardingActivity delegate;

	// View members
	private View view;
	CircularBarPager circularBar;

	final int ANIMATE_DURATION = 1000;
	private int profileCompleteness = 34;

	public static Signup2ndFragment newInstance() {
		return new Signup2ndFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_signup_2nd, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (OnboardingActivity) getActivity();

		// initialize
		initViewAndClassMembers();
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;

		vi.findViewById(R.id.btn_return).setOnClickListener(this);
		vi.findViewById(R.id.btnSkip).setOnClickListener(this);
		vi.findViewById(R.id.btnContinue).setOnClickListener(this);

		circularBar = (CircularBarPager) this.view.findViewById(R.id.circle_progress);
		circularBar.getCircularBar().animateProgress(0, profileCompleteness, ANIMATE_DURATION);
	}

	@Override
	public void onClick(View v) {

        if (v.getId() == R.id.btn_return) {
			Signup1stFragment fragment = Signup1stFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        } else if (v.getId() == R.id.btnSkip) {
			Signup3rdFragment fragment = Signup3rdFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
        } else if(v.getId() == R.id.btnContinue){
			Signup3rdFragment fragment = Signup3rdFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		}
	}
}
