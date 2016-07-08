package android.vaunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.OnboardingActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.github.OrangeGangsters.circularbarpager.library.CircularBar;
import com.github.OrangeGangsters.circularbarpager.library.CircularBarPager;

public class Signup3rdFragment extends BaseFragment implements OnClickListener{

	// Holds activity delegate instance
	private OnboardingActivity delegate;

	// View members
	private View view;
	CircularBarPager circularBar, circularBarFacebook, circularBarTwitter, circularBarGoogle;


	final int ANIMATE_DURATION = 1000;
	private int profileCompleteness = 67;

	public static Signup3rdFragment newInstance() {
		return new Signup3rdFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_signup_3rd, container,
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
		vi.findViewById(R.id.imgvFacebook).setOnClickListener(this);
		vi.findViewById(R.id.imgvTwitter).setOnClickListener(this);
		vi.findViewById(R.id.imgvGoogle).setOnClickListener(this);
		vi.findViewById(R.id.tvFacebook).setOnClickListener(this);
		vi.findViewById(R.id.tvTwitter).setOnClickListener(this);
		vi.findViewById(R.id.tvGoogle).setOnClickListener(this);

		vi.findViewById(R.id.tvFacebook).setVisibility(View.GONE);
		vi.findViewById(R.id.tvTwitter).setVisibility(View.GONE);
		vi.findViewById(R.id.tvGoogle).setVisibility(View.GONE);

		circularBar = (CircularBarPager) this.view.findViewById(R.id.circle_progress);
		circularBar.getCircularBar().animateProgress(0, profileCompleteness, ANIMATE_DURATION);

		circularBarFacebook = (CircularBarPager)this.view.findViewById(R.id.circle_progress_facebook);
		circularBarTwitter = (CircularBarPager)this.view.findViewById(R.id.circle_progress_twitter);
		circularBarGoogle = (CircularBarPager)this.view.findViewById(R.id.circle_progress_google);
	}

	@Override
	public void onClick(View v) {

        if (v.getId() == R.id.btn_return) {
			Signup2ndFragment fragment = Signup2ndFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        } else if (v.getId() == R.id.btnSkip) {
			Signup4thFragment fragment = Signup4thFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
        } else if(v.getId() == R.id.btnContinue){
			Signup4thFragment fragment = Signup4thFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		} else if(v.getId() == R.id.imgvFacebook){
			view.findViewById(R.id.tvFacebook).setVisibility(View.VISIBLE);
		} else if(v.getId() == R.id.imgvTwitter){
			view.findViewById(R.id.tvTwitter).setVisibility(View.VISIBLE);
		} else if(v.getId() == R.id.imgvGoogle){
			view.findViewById(R.id.tvGoogle).setVisibility(View.VISIBLE);
		} else if(v.getId() == R.id.tvFacebook){
			view.findViewById(R.id.tvFacebook).setVisibility(View.GONE);
			circularBarFacebook.getCircularBar().animateProgress(0, profileCompleteness, ANIMATE_DURATION);
			circularBarFacebook.setVisibility(View.VISIBLE);
		} else if(v.getId() == R.id.tvTwitter){
			view.findViewById(R.id.tvTwitter).setVisibility(View.GONE);
			circularBarTwitter.getCircularBar().animateProgress(0, profileCompleteness, ANIMATE_DURATION);
			circularBarTwitter.setVisibility(View.VISIBLE);
		} else if(v.getId() == R.id.tvGoogle){
			view.findViewById(R.id.tvGoogle).setVisibility(View.GONE);
			circularBarGoogle.getCircularBar().animateProgress(0, profileCompleteness, ANIMATE_DURATION);
			circularBarGoogle.setVisibility(View.VISIBLE);
		}

	}
}
