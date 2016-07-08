package android.vaunt.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity.CUSTOM_ANIMATIONS;
import android.vaunt.activities.CameraActivity;
import android.vaunt.activities.MainActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.DBUtil;
import android.vaunt.utility.Utils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CameraSecondFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private CameraActivity delegate;

	// View members
	private View view;
    Bitmap bmpPreview;

	public void setBmpPreview(Bitmap bmpPreview) {
		this.bmpPreview = bmpPreview;
	}

	public static CameraSecondFragment newInstance() {
		return new CameraSecondFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_camera_second, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (CameraActivity) getActivity();

		// initialize
		initViewAndClassMembers();
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;

		Display display = delegate.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,width);
		((RelativeLayout)vi.findViewById(R.id.rlyPreview)).setLayoutParams(parms);

        vi.findViewById(R.id.rlyBack).setOnClickListener(this);
        vi.findViewById(R.id.rlyShot).setOnClickListener(this);

		((ImageView)vi.findViewById(R.id.imgvPreview)).setImageBitmap(bmpPreview);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.rlyBack) {
            baseActivity.showFragment(R.id.fl_fragment_container, CameraFirstFragment.newInstance(),
                    CUSTOM_ANIMATIONS.FADE_IN);
		} else if (v.getId() == R.id.rlyShot) {
			delegate.sendData(bmpPreview);
        }
	}
}
