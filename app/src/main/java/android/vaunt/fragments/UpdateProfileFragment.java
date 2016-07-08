package android.vaunt.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.BaseActivity.CUSTOM_ANIMATIONS;
import android.vaunt.activities.CameraActivity;
import android.vaunt.activities.MainActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.DBUtil;
import android.vaunt.utility.UserInformation;
import android.vaunt.utility.Utils;
import android.vaunt.web.ServerConnect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.OrangeGangsters.circularbarpager.library.CircularBar;
import com.github.OrangeGangsters.circularbarpager.library.CircularBarPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class UpdateProfileFragment extends BaseFragment implements OnClickListener, ServerConnect.APIResponseListener {

	// Holds activity delegate instance
	private MainActivity delegate;

	// View members
	private View view;

	CircularBarPager circularBar;
	boolean isImageUpdated = false;
	final int REQUEST_CAMERA = 1;
	final int REQUEST_GALLERY = 2;
	final int REQUEST_CROP = 3;
	private Uri m_imgDataUri;
	private String m_imgTempName = "tmp_avatar";
	private String m_imgSelName = null;
    Bitmap bmpAvatar;

	public static UpdateProfileFragment newInstance() {
		return new UpdateProfileFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_update_profile, container,
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

		circularBar = (CircularBarPager)vi.findViewById(R.id.circle_progress);
		circularBar.getCircularBar().animateProgress(0, 75, 1000);

        vi.findViewById(R.id.btn_return).setOnClickListener(this);
		vi.findViewById(R.id.tvUpdate).setOnClickListener(this);
		vi.findViewById(R.id.avatar_rect).setOnClickListener(this);

		((TextView)vi.findViewById(R.id.et_firstname)).setText(UserInformation.getInstance().firstName);
		((TextView)vi.findViewById(R.id.et_lastname)).setText(UserInformation.getInstance().lastName);
		((TextView)vi.findViewById(R.id.et_email)).setText(UserInformation.getInstance().email);
		((TextView)vi.findViewById(R.id.et_username)).setText(UserInformation.getInstance().userAccountName);
		((TextView)vi.findViewById(R.id.et_zipcode)).setText(UserInformation.getInstance().zipCode);
		((DynamicImageView)vi.findViewById(R.id.image_avatar)).setImageBitmap(UserInformation.getInstance().bmpUser);
		vi.findViewById(R.id.imgvHeaderLogo).setOnClickListener(this);
	}

	private void onApplyClick() {
		String strFirstName = ((EditText)this.view.findViewById(R.id.et_firstname)).getText().toString();
		String strLastName = ((EditText)this.view.findViewById(R.id.et_lastname)).getText().toString();
		String strEmail = ((EditText)this.view.findViewById(R.id.et_email)).getText().toString();
		String strUserName = ((EditText)this.view.findViewById(R.id.et_username)).getText().toString();
		String strZip = ((EditText)this.view.findViewById(R.id.et_zipcode)).getText().toString();
		Bitmap bitmap = ((BitmapDrawable)((DynamicImageView)this.view.findViewById(R.id.image_avatar)).getDrawable()).getBitmap();
        bmpAvatar = bitmap;
		if (strFirstName.length() == 0) {
			Utils.showMessage("Please enter first name.");
			return;
		}

		if (strLastName.length() == 0) {
			Utils.showMessage("Please enter last name.");
			return;
		}

		if(strEmail.length() == 0){
			Utils.showMessage("Please enter email address.");
			return;
		}

		if(strUserName.length() == 0){
			Utils.showMessage("Please enter user name.");
			return;
		}

		if(strZip.length() == 0){
			Utils.showMessage("Please enter zip code.");
			return;
		}

		if (bitmap == null) {
			Utils.showMessage("Please select user avatar.");
			return;
		}

//		ArrayList<JSONObject> updateData = new ArrayList<JSONObject>();
		JSONObject json = new JSONObject();
		JSONObject params = new JSONObject();
		try {
			json.put("fileType", "image");
			json.put("name", "avatar");
			json.put("fileName", "avatar.jpg");
			json.put("fileType", "image");
			json.put("data", Utils.getBytesFromBitmap(bitmap));
			json.put("mimeType", "image/jpg");
//			updateData.add(json);

			params.put(Constants.kUDAuthToken, UserInformation.getInstance().authTokenKey);
			params.put(Constants.USERINFO_FIRSTNAME_KEY, strFirstName);
			params.put(Constants.USERINFO_LASTNAME_KEY, strLastName);
			params.put(Constants.USERINFO_EMAIL_KEY, strEmail);
			params.put(Constants.USERINFO_USERNAME_KEY, strUserName);
			params.put(Constants.USERINFO_ZIPCODE_KEY, strZip);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Utils.showProgDialog(delegate, null, "Updating...", null);
		ServerConnect.UploadFiles(Constants.kWSApiUpdate, json, params, this);

	}

	private void selectImage() {

		final CharSequence[] items = { "Take a Photo", "Select one from Camera Roll", "Cancel"};

		AlertDialog.Builder builder = new AlertDialog.Builder(delegate);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				m_imgSelName = null;
				if (item == 0) {
					/**
					 * To take a photo from camera, pass intent action
					 * ‘MediaStore.ACTION_IMAGE_CAPTURE‘ to open the camera app.
					 */
//					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//					/**
//					 * Also specify the Uri to save the image on specified path
//					 * and file name. Note that this Uri variable also used by
//					 * gallery app to hold the selected image path.
//					 */
//					m_imgDataUri = Uri.fromFile(new File(Utils.m_avatarDir, m_imgTempName + ".png"));
//					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
//							m_imgDataUri);
////                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(CameraActivity.GetSaveBmpFile())));
//
//					intent.putExtra("return-data", true);
//					intent.putExtra("circleCrop", new String(""));
					Intent intent = new Intent(baseActivity, CameraActivity.class);
					startActivityForResult(intent, REQUEST_CAMERA);

				} else if (item == 1) {
					m_imgDataUri = null;
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							REQUEST_GALLERY);
				}
				dialog.dismiss();
			}
		});
		builder.setCancelable(false);
		builder.show();
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
				((DynamicImageView)this.view.findViewById(R.id.image_avatar)).setImageBitmap(avatar);
				this.isImageUpdated = true;
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
				((DynamicImageView)this.view.findViewById(R.id.image_avatar)).setImageBitmap(avatar);
				this.isImageUpdated = true;

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

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.avatar_rect){
			selectImage();
		}else{
			if (v.getId() == R.id.tvUpdate) {
				onApplyClick();
			}else if(v.getId() == R.id.btn_return){
				SettingFragment fragment = SettingFragment.newInstance();
				baseActivity.showFragment(R.id.fl_fragment_container, fragment,
						true, CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
			}else if(v.getId() == R.id.imgvHeaderLogo){
				delegate.menuFragment.selectHomeItem();

				HomeFragment fragment = HomeFragment.newInstance();
				baseActivity.showFragment(R.id.fl_fragment_container, fragment,
						true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
			}
		}
	}

	@Override
	public void onSuccess(JSONObject json) {
		Utils.hideProgDialog();

		int nCode = 0;
		try {
			nCode = json.getInt(Constants.kWSApiResponseCode);
			String strMsg = json.getString(Constants.kWSApiResponseMessage);

			if(nCode == Constants.SUCCESS_CODE) {
                UserInformation.getInstance().bmpUser = bmpAvatar;
				UserInformation.getInstance().setInformation(UserInformation.getInstance().authTokenKey);
				SettingFragment fragment = SettingFragment.newInstance();
				baseActivity.showFragment(R.id.fl_fragment_container, fragment,
						true, CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
			}else{
				Toast.makeText(baseActivity, strMsg, Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure() {
		Utils.hideProgDialog();
		Toast.makeText(baseActivity, "Updating has been failed", Toast.LENGTH_SHORT).show();
	}
}
