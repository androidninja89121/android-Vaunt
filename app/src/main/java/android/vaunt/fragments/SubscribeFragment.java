package android.vaunt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity.CUSTOM_ANIMATIONS;
import android.vaunt.activities.MainActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import org.json.JSONObject;

public class SubscribeFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;

	// View members
	private View view;

    // Data members
	private JSONObject jsonObject;
	private int mode;

	public static SubscribeFragment newInstance() {
		return new SubscribeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_subscribe, container,
				false);
		return this.view;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
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
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_back) {
			HomeDetailFragment fragment = HomeDetailFragment.newInstance();
			fragment.setJsonObject(this.jsonObject);
			fragment.setMode(this.mode);
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					false, CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
		}
	}
}
