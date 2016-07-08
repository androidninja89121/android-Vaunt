package android.vaunt.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.CameraActivity;
import android.vaunt.activities.OnboardingActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.UserInformation;
import android.vaunt.utility.Utils;
import android.vaunt.web.ServerConnect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.github.OrangeGangsters.circularbarpager.library.CircularBarPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Signup1stFragment extends BaseFragment implements OnClickListener, ActionSheet.ActionSheetListener{

	// Holds activity delegate instance
	private OnboardingActivity delegate;

	// View members
	private View view;
	DynamicImageView avatarView;
	CircularBarPager circularBar;

	final int REQUEST_CAMERA = 1;
	final int REQUEST_GALLERY = 2;
	final int REQUEST_CROP = 3;
	final int ANIMATE_DURATION = 1000;
	private int profileCompleteness = 75;
	private Uri m_imgDataUri;
	private String m_imgTempName = "tmp_avatar";
	private String m_imgSelName = null;

	public static Signup1stFragment newInstance() {
		return new Signup1stFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_signup_1st, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (OnboardingActivity) getActivity();

		// initialize
		initViewAndClassMembers();
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;

		vi.findViewById(R.id.btn_return).setOnClickListener(this);
		vi.findViewById(R.id.btnSelectAvatar).setOnClickListener(this);
		vi.findViewById(R.id.btnContinue).setOnClickListener(this);

		avatarView = (DynamicImageView)this.view.findViewById(R.id.image_avatar);
		circularBar = (CircularBarPager) this.view.findViewById(R.id.circle_progress);
	}

	@Override
	public void onClick(View v) {

        if (v.getId() == R.id.btn_return) {
			FirstFragment fragment = FirstFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        } else if (v.getId() == R.id.btnSelectAvatar) {
			ActionSheet.createBuilder(delegate, delegate.getSupportFragmentManager())
					.setCancelButtonTitle("Cancel")
					.setOtherButtonTitles("Take a Photo", "Select one from camera roll")
					.setCancelableOnTouchOutside(true)
					.setListener(this).show();
        } else if(v.getId() == R.id.btnContinue){
			Signup2ndFragment fragment = Signup2ndFragment.newInstance();
			baseActivity.showFragment(R.id.fl_fragment_container, fragment,
					true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
		}
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		if(index == 0){
			Intent intent = new Intent(baseActivity, CameraActivity.class);
			startActivityForResult(intent, REQUEST_CAMERA);
		}else{
			m_imgDataUri = null;
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(	Intent.createChooser(intent, "Select File"), REQUEST_GALLERY);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap img = extras.getParcelable("data");
				// retrieve a reference to the ImageView
				Bitmap avatar = Utils.GetCircleBitmap(img);
				avatarView.setImageBitmap(avatar);
				circularBar.getCircularBar().animateProgress(0, profileCompleteness, ANIMATE_DURATION);
			} else if (requestCode == REQUEST_GALLERY) {
				Uri selectedImageUri = data.getData();
				m_imgSelName = Utils.getRealPathFromURI(delegate, selectedImageUri);
				PerformCrop (selectedImageUri);
			}
			else if (requestCode == REQUEST_CROP) {
				// get the returned data
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap img = extras.getParcelable("data");
				// retrieve a reference to the ImageView
				Bitmap avatar = Utils.GetCircleBitmap(img);
				avatarView.setImageBitmap(avatar);
				circularBar.getCircularBar().animateProgress(0, profileCompleteness, ANIMATE_DURATION);

				if (m_imgDataUri != null || m_imgSelName != null) {
					DeleteTempImgs();
				}
			}
		}
	}

	public void DeleteTempImgs()
	{
		File folder = new File(Utils.m_avatarPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String fileName = listOfFiles[i].getName();
				if (fileName.startsWith(m_imgTempName)) {
					Utils.deleteImage(fileName);
				}
			}
		}

		if (m_imgSelName != null) {
			int pos = m_imgSelName.lastIndexOf("/");
			String path = m_imgSelName.substring(0, pos+1);
			String fname = m_imgSelName.substring(pos+1, m_imgSelName.length());
			String prefix = fname.substring(0, fname.lastIndexOf("."));
			File dir = new File(path);
			File[] files = dir.listFiles();

			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					String fileName = files[i].getName();
					if (!fileName.equals(fname) &&
							fileName.startsWith(prefix)) {
						files[i].delete();
					}
				}
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
}
