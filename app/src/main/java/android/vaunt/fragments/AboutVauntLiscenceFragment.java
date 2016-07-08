package android.vaunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.MainActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class AboutVauntLiscenceFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;

	// View members
	private View view;

    // Data members

	public static AboutVauntLiscenceFragment newInstance() {
		return new AboutVauntLiscenceFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_about_vaunt_liscence, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();
        view.findViewById(R.id.btn_back).setOnClickListener(this);
		view.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_back) {
			AboutVauntFragment fragment = AboutVauntFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
		} else if (v.getId() == R.id.imgvHeaderLogo){
			delegate.menuFragment.selectHomeItem();

			HomeFragment fragment = HomeFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
		}
	}
}
