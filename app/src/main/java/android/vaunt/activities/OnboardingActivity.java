package android.vaunt.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.vaunt.R;
import android.vaunt.fragments.FirstFragment;
import android.vaunt.utility.Utils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;


public class OnboardingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_onboarding);

        showFragment(R.id.fl_fragment_container, FirstFragment.newInstance(),
                CUSTOM_ANIMATIONS.FADE_IN);
    }

    public void onLogin() {
        startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
        finish();
    }

}
