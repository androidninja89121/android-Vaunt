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

import com.github.OrangeGangsters.circularbarpager.library.CircularBarPager;

public class Signup4thFragment extends BaseFragment implements OnClickListener{

	// Holds activity delegate instance
	private OnboardingActivity delegate;

	// View members
	private View view;
	CircularBarPager circularBar, circularBarFacebook, circularBarTwitter, circularBarGoogle;


	final int ANIMATE_DURATION = 1000;
	private int profileCompleteness = 100;

	public static Signup4thFragment newInstance() {
		return new Signup4thFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_signup_4th, container,
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
		vi.findViewById(R.id.btnContinue).setOnClickListener(this);

		circularBar = (CircularBarPager) this.view.findViewById(R.id.circle_progress);
		circularBar.getCircularBar().animateProgress(0, profileCompleteness, ANIMATE_DURATION);
	}

	@Override
	public void onClick(View v) {

        if (v.getId() == R.id.btn_return) {
			Signup3rdFragment fragment = Signup3rdFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
		} else if(v.getId() == R.id.btnContinue){
			delegate.onLogin();
		}
	}
}
