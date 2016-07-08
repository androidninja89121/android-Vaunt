package android.vaunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.BaseActivity.CUSTOM_ANIMATIONS;
import android.vaunt.activities.MainActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.DBUtil;
import android.vaunt.utility.Utils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AboutVauntFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;

	// View members
	private View view;

    // Data members

	public static AboutVauntFragment newInstance() {
		return new AboutVauntFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_about_vaunt, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();
        ((RelativeLayout)view.findViewById(R.id.btn_back)).setOnClickListener(this);
		view.findViewById(R.id.menu_about_vaunt).setOnClickListener(this);
		view.findViewById(R.id.menu_terms).setOnClickListener(this);
		view.findViewById(R.id.menu_privacy).setOnClickListener(this);
		view.findViewById(R.id.menu_liscence).setOnClickListener(this);
		view.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_back || v.getId() == R.id.imgvHeaderLogo) {
			delegate.menuFragment.selectHomeItem();

			HomeFragment fragment = HomeFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
		} else if(v.getId() == R.id.menu_about_vaunt){
			AboutVauntAboutFragment fragment = AboutVauntAboutFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		} else if(v.getId() == R.id.menu_terms){
			AboutVauntTermsFragment fragment = AboutVauntTermsFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		} else if(v.getId() == R.id.menu_privacy){
			AboutVauntPrivacyFragment fragment = AboutVauntPrivacyFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		} else if(v.getId() == R.id.menu_liscence){
			AboutVauntLiscenceFragment fragment = AboutVauntLiscenceFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		}
	}
}
