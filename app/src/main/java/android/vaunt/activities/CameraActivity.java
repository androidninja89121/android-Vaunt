package android.vaunt.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.vaunt.R;
import android.vaunt.fragments.CameraFirstFragment;
import android.vaunt.fragments.FirstFragment;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class CameraActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_onboarding);

        showFragment(R.id.fl_fragment_container, CameraFirstFragment.newInstance(),
                CUSTOM_ANIMATIONS.FADE_IN);
    }

    public void sendData(Bitmap bmp){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("data", bmp);
// TODO Add extras or a data URI to this intent as appropriate.
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}