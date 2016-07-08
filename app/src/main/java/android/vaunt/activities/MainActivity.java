package android.vaunt.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.vaunt.Constants;
import android.vaunt.fragments.AboutVauntFragment;
import android.vaunt.fragments.BrowseFragment;
import android.vaunt.fragments.CourtSideFragment;
import android.vaunt.fragments.HomeFragment;
import android.vaunt.fragments.LiveListFragment;
import android.vaunt.fragments.LiveTrialFragment;
import android.vaunt.fragments.LiveViewFragment;
import android.vaunt.fragments.SettingFragment;
import android.vaunt.fragments.VideoFragment;
import android.vaunt.fragments.WatchFragment;
import android.view.Window;

import android.vaunt.R;
import android.vaunt.fragments.BaseFragment;
import android.vaunt.fragments.SideMenuFragment;
import android.vaunt.slidemenu.SlidingMenu;
import android.vaunt.utility.Utils;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends BaseActivity implements
        SideMenuFragment.SideMenuItemClickListener {

	public static final int
            MENU_HOME = 0,
			MENU_BROWSE = 1,
            MENU_LIVE = 2,
			MENU_WATCH = 3,
            MENU_ACCOUNT = 4,
			MENU_SETTINGS = 5,
			MENU_ABOUT  = 6,
            MENU_LOGOUT = 7;
	// Member variable which holds the instance of sliding menu
	private SlidingMenu sideMenu;
	public SideMenuFragment menuFragment;
	private boolean backPressed;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Initialize side menu and first fragment
		instantiateSlidingMenu();
		showFragment(R.id.fl_fragment_container, HomeFragment.newInstance(),
				false, CUSTOM_ANIMATIONS.FADE_IN);
	}

	/**
	 * Instantiates the sliding menu from SideMenuFragment
	 */
	private void instantiateSlidingMenu() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // initialize sliding menu
		sideMenu = new SlidingMenu(this);
		sideMenu.setMode(SlidingMenu.LEFT);
		sideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sideMenu.setBehindOffset(displayMetrics.widthPixels  * 4 / 11);
		sideMenu.setFadeDegree(0.5f);
		sideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		sideMenu.setMenu(R.layout.slide_menu_container);
		sideMenu.setSlidingEnabled(true);
	}

	/**
	 * Enable/Disable slide menu
	 */
	public void enableSlideMenu(boolean enable) {
		sideMenu.setSlidingEnabled(enable);
	}

	/**
	 * Set side menu fragment
	 */
	public void setMenuFragment(SideMenuFragment fragment) {
		menuFragment = fragment;
	}

	@Override
	public void onMenuItemSelected(int position) {
		selectMenu(position);
		toggleSlideMenu();
	}

	/**
	 * Select menu item and set new fragment
	 */
	public void selectMenu(int position) {
        BaseFragment fragment = null;
		switch (position) {
            case MENU_HOME:
                fragment = HomeFragment.newInstance();
			    break;

            case MENU_BROWSE:
                fragment = BrowseFragment.newInstance();
				break;
		    case MENU_LIVE:
			    fragment = LiveListFragment.newInstance();
			    break;
		    case MENU_WATCH:
			    fragment = WatchFragment.newInstance();
			    break;
		    case MENU_ACCOUNT:
			    break;

            case MENU_SETTINGS:
			    fragment = SettingFragment.newInstance();
                break;
            case MENU_ABOUT:
                fragment = AboutVauntFragment.newInstance();
                break;
            case MENU_LOGOUT:
                signOut();
                return;
		}

		if (fragment != null) {
			showFragment(R.id.fl_fragment_container, fragment, true, true,
					CUSTOM_ANIMATIONS.FADE_IN);
		}
	}

	/**
	 * Toggle slide menu
	 */
	public void toggleSlideMenu() {
		sideMenu.toggle();
        if (sideMenu.isMenuShowing()) {
         	menuFragment.updateUserProfileImage();
			menuFragment.animateCircularBar();
        }
	}

	/**
	 * Sign out and navigate to Log in Activity
	 */
	public void signOut() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		editor.putBoolean(Constants.kUDLoggedIn, false);
		editor.commit();

        startActivity(new Intent(MainActivity.this, OnboardingActivity.class));
		finish();
	}


    @Override
	public void onBackPressed() {
		if(activeFragment.getClass().equals(HomeFragment.class)){

		}else if(activeFragment.getClass().equals(LiveListFragment.class) || activeFragment.getClass().equals(WatchFragment.class)
                ||activeFragment.getClass().equals(SettingFragment.class)){
			selectMenu(MENU_HOME);
		}else if(activeFragment.getClass().equals(CourtSideFragment.class)){
			((CourtSideFragment)activeFragment).onBack();
		}else if(activeFragment.getClass().equals(VideoFragment.class)){
            ((VideoFragment)activeFragment).onBack();
        }else if(activeFragment.getClass().equals(LiveTrialFragment.class)){
            ((LiveTrialFragment)activeFragment).onBack();
        }else if(activeFragment.getClass().equals(LiveViewFragment.class)){
            ((LiveViewFragment)activeFragment).onBack();
        }
	}

	/**
	 * Confirm to finish
	 */
	private void ConfirmFinish() {
		if (backPressed) {
			finish();
		} else {
			Utils.showMessage(getResources().getString(
                    R.string.Confirm_Finish));
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					backPressed = false;
				}
			}, 2000);
			backPressed = true;
		}
	}

}
