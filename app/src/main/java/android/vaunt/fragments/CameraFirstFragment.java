package android.vaunt.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraFirstFragment extends BaseFragment implements SurfaceHolder.Callback, View.OnClickListener, Camera.ShutterCallback, Camera.PictureCallback{

	// Holds activity delegate instance
	private CameraActivity delegate;

	// View members
	private View view;
    Camera mCamera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean isPreviewRunning = false;
    boolean isFlashOn = false;
    boolean isFrontCamera = false;
    boolean isGrid = false;
    ImageView imgvFlash, imgvOverlay;
    final int REQUEST_CAMERA = 1;
    final int REQUEST_GALLERY = 2;
    final int REQUEST_CROP = 3;
    final int ANIMATE_DURATION = 1000;
    private int profileCompleteness = 75;
    private Uri m_imgDataUri;

	public static CameraFirstFragment newInstance() {
		return new CameraFirstFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_camera_first, container,
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

        surfaceView = (SurfaceView)vi.findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        imgvFlash = (ImageView)vi.findViewById(R.id.imgvFlash);
        imgvOverlay = (ImageView)vi.findViewById(R.id.imgvOverlay);
        vi.findViewById(R.id.rlyGrid).setOnClickListener(this);
        vi.findViewById(R.id.rlyRear).setOnClickListener(this);
        vi.findViewById(R.id.rlyFlash).setOnClickListener(this);
        vi.findViewById(R.id.rlyClose).setOnClickListener(this);
        vi.findViewById(R.id.rlyShot).setOnClickListener(this);
        vi.findViewById(R.id.rlyGallery).setOnClickListener(this);
	}

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.rlyGrid){
            isGrid = !isGrid;
            if(isGrid){
                imgvOverlay.setImageResource(R.drawable.circle_overlay_grid);
            }else{
                imgvOverlay.setImageResource(R.drawable.circle_overlay);
            }
        }else if(view.getId() == R.id.rlyRear){

            isFrontCamera = !isFrontCamera;

            int nCameraId = 0;

            if(isFrontCamera){
                nCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                imgvFlash.setVisibility(View.INVISIBLE);
            }else{
                nCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                imgvFlash.setVisibility(View.VISIBLE);
            }

            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;

            mCamera = Camera.open(nCameraId);

            Camera.Parameters parameters = mCamera.getParameters();

            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            Camera.Size selected = sizes.get(0);

            surfaceChanged(surfaceHolder, 0, selected.width, selected.height);

        }else if(view.getId() == R.id.rlyFlash){
            if(isFrontCamera) return;

            if (isPreviewRunning)
            {
                mCamera.stopPreview();
                isPreviewRunning = false;
            }

            Camera.Parameters parameters = mCamera.getParameters();

            isFlashOn = !isFlashOn;

            if(isFlashOn){
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                imgvFlash.setImageResource(R.drawable.camera_flash_on);
            }else{
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                imgvFlash.setImageResource(R.drawable.camera_flash_off);
            }

            mCamera.setParameters(parameters);

            if (mCamera != null){
                try {
                    mCamera.setPreviewDisplay(surfaceHolder);
                    mCamera.startPreview();
                    isPreviewRunning = true;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }else if(view.getId() == R.id.rlyClose){
            delegate.finish();
        }else if(view.getId() == R.id.rlyShot){
            mCamera.takePicture(this, null, null, this);
        }else if(view.getId() == R.id.rlyGallery){
            m_imgDataUri = null;
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"),REQUEST_GALLERY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                Uri selectedImageUri = data.getData();
                PerformCrop (selectedImageUri);
            }
            else if (requestCode == REQUEST_CROP) {
                Bundle extras = data.getExtras();
                Bitmap bmp = extras.getParcelable("data");

                CameraSecondFragment fragment = CameraSecondFragment.newInstance();
                fragment.setBmpPreview(bmp);
                baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                        true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
            }
        }
    }

    private void PerformCrop(Uri picUri) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra("circleCrop", new String(""));

            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQUEST_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bmpOfTheImageFromCamera = BitmapFactory.decodeByteArray(
                data, 0, data.length);

        Matrix matrix = new Matrix();

        if(isFrontCamera){
            matrix.postRotate(270);
        }else{
            matrix.postRotate(90);
        }

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmpOfTheImageFromCamera,surfaceView.getMeasuredWidth(),
                surfaceView.getMeasuredHeight(),true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmpOfTheImageFromCamera.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(delegate.getContentResolver(), rotatedBitmap,
                "Title", null);

//        String path = MediaStore.Images.Media.insertImage(delegate.getContentResolver(), bmpOfTheImageFromCamera,
//                "Title", null);

        Uri imageUri = Uri.parse(path);

        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PerformCrop(imageUri);

        isFlashOn = false;
        isFrontCamera = false;
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        imgvFlash.setImageResource(R.drawable.camera_flash_off);

        mCamera.setParameters(parameters);

        if (mCamera != null){
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();
                isPreviewRunning = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onShutter() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        mCamera = Camera.open();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        isPreviewRunning = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

        Log.e("camera dimension", "width:" + width + " height:" + height);
        if (isPreviewRunning)
        {
            mCamera.stopPreview();
            isPreviewRunning = false;
        }

        Camera.Parameters parameters = mCamera.getParameters();
        Display display = delegate.getWindowManager().getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0)
        {
            parameters.setPreviewSize(height, width);
            mCamera.setDisplayOrientation(90);
        }

        if(display.getRotation() == Surface.ROTATION_90)
        {
            parameters.setPreviewSize(width, height);
        }

        if(display.getRotation() == Surface.ROTATION_180)
        {
            parameters.setPreviewSize(height, width);
        }

        if(display.getRotation() == Surface.ROTATION_270)
        {
            parameters.setPreviewSize(width, height);
            mCamera.setDisplayOrientation(180);
        }

        if (mCamera != null){
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
//                mCamera.setParameters(parameters);
                mCamera.startPreview();
                isPreviewRunning = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
