package android.vaunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.activities.OnboardingActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.vaunt.activities.BaseActivity.CUSTOM_ANIMATIONS;

import android.vaunt.R;
import android.vaunt.activities.MainActivity;

public class FirstFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private OnboardingActivity delegate;

	// View members
	private View view;

	public static FirstFragment newInstance() {
		return new FirstFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_first, container,
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
        vi.findViewById(R.id.btn_login).setOnClickListener(this);
        vi.findViewById(R.id.btn_register).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_login) {
            LoginFragment fragment = LoginFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		} else if (v.getId() == R.id.btn_register) {
            Signup1stFragment fragment = Signup1stFragment
                    .newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
        }

	}

}
