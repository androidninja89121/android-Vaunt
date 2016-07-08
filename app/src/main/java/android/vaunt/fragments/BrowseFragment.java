package android.vaunt.fragments;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BrowseFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;

	// View members
	private View view;
    private LinearLayout lyContainer;

	public static BrowseFragment newInstance() {
		return new BrowseFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_browse, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();

		initViewAndClassMembers();
        presentData();
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;
        vi.findViewById(R.id.btn_menu).setOnClickListener(this);
        vi.findViewById(R.id.btn_search).setOnClickListener(this);
        vi.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);

        lyContainer = (LinearLayout)vi.findViewById(R.id.linearLayoutContainer);
	}

    void presentData(){
        lyContainer.removeAllViews();

        for(int i = 0; i < DBUtil.getInstance().browseArray.length(); i ++){
            try {
                JSONArray jsonArray = DBUtil.getInstance().browseArray.getJSONArray(i);
                JSONObject jsonObjectSp = jsonArray.getJSONObject(0);

                RelativeLayout newBigCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_browse_scroll, null));

                ((TextView)newBigCell.findViewById(R.id.tvTitle)).setText(jsonObjectSp.getString(Constants.ITEM_CATEGORY));
                LinearLayout lySubContainer = (LinearLayout)newBigCell.findViewById(R.id.linearLayoutContainer);

                for(int j = 0; j < jsonArray.length(); j ++){
                    RelativeLayout newSmallCell = (RelativeLayout)(View.inflate(baseActivity, R.layout.cell_browse_item, null));

                    ImageView imgvPerson = (ImageView)newSmallCell.findViewById(R.id.imgvPhoto);
                    TextView tvName = (TextView)newSmallCell.findViewById(R.id.tvName);

                    final JSONObject jsonObject = jsonArray.getJSONObject(j);

                    String strImageName = jsonObject.getString(Constants.ITEM_BIG_IMAGE);
                    String strPersonName = jsonObject.getString(Constants.ITEM_TITLE);

                    int resID = getResources().getIdentifier(strImageName, "drawable", getActivity().getPackageName());

                    imgvPerson.setImageResource(resID);
                    tvName.setText(strPersonName);

                    newSmallCell.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HomeDetailFragment fragment = HomeDetailFragment.newInstance();
                            fragment.setJsonObject(jsonObject);
                            fragment.setMode(Constants.FROM_BROWSE);
                            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                                    true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
                        }
                    });

                    lySubContainer.addView(newSmallCell);
                }

                lyContainer.addView(newBigCell);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_search) {
            SearchFragment fragment = SearchFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		} else if (v.getId() == R.id.btn_menu) {
            delegate.toggleSlideMenu();
        } else if (v.getId() == R.id.imgvHeaderLogo){
            delegate.menuFragment.selectHomeItem();

            HomeFragment fragment = HomeFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        }
	}

}
