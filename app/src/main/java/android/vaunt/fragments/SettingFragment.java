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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;

	// View members
	private View view;
	boolean flag_wifi, flag_fb, flag_tw, flag_tips;

    // Data members

	public static SettingFragment newInstance() {
		return new SettingFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_setting, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();

		// initialize
		initViewAndClassMembers();
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;
        vi.findViewById(R.id.btn_back).setOnClickListener(this);
		vi.findViewById(R.id.btn_wifi).setOnClickListener(this);
		vi.findViewById(R.id.btn_fb).setOnClickListener(this);
		vi.findViewById(R.id.btn_tw).setOnClickListener(this);
		vi.findViewById(R.id.btn_tips).setOnClickListener(this);
		vi.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);

		flag_wifi = true;
		flag_fb = false;
		flag_tw = false;
		flag_tips = true;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_back) {
			delegate.menuFragment.selectMenuItem(0);
        }else if(v.getId() == R.id.btn_wifi ){
			flag_wifi = !flag_wifi;
			if(flag_wifi){
				((Button)v).setBackgroundResource(R.drawable.ic_checked);
			}else{
				((Button)v).setBackgroundResource(R.drawable.ic_unchecked);
			}
		}else if(v.getId() == R.id.btn_fb){
			flag_fb = !flag_fb;
			if(flag_fb){
				((Button)v).setBackgroundResource(R.drawable.ic_checked);
			}else{
				((Button)v).setBackgroundResource(R.drawable.ic_unchecked);
			}
		}else if(v.getId() == R.id.btn_tw){
			flag_tw = !flag_tw;
			if(flag_tw){
				((Button)v).setBackgroundResource(R.drawable.ic_checked);
			}else{
				((Button)v).setBackgroundResource(R.drawable.ic_unchecked);
			}
		}else if(v.getId() == R.id.btn_tips){
			flag_tips = !flag_tips;
			if(flag_tips){
				((Button)v).setBackgroundResource(R.drawable.ic_checked);
			}else{
				((Button)v).setBackgroundResource(R.drawable.ic_unchecked);
			}
		}else if(v.getId() == R.id.imgvHeaderLogo){
			delegate.menuFragment.selectHomeItem();

			HomeFragment fragment = HomeFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
		}
	}
}
