package android.vaunt.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.vaunt.activities.CameraActivity;
import android.vaunt.ui.DynamicImageView;
import android.vaunt.utility.UserInformation;
import android.vaunt.utility.Utils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.vaunt.R;
import android.vaunt.activities.MainActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.OrangeGangsters.circularbarpager.library.CircularBarPager;

import java.io.File;
import java.net.UnknownServiceException;

public class SideMenuFragment extends BaseFragment implements OnClickListener{

    final int REQUEST_CAMERA = 1;
    final int REQUEST_GALLERY = 2;
    final int REQUEST_CROP = 3;
    final int ANIMATE_DURATION = 1000;

	private View view;
	private MainActivity delegate;
    DynamicImageView avatarView;
    CircularBarPager circularBar;

    private int profileCompleteness;
    private Uri m_imgDataUri;
    private String m_imgTempName = "tmp_avatar";
    private String m_imgSelName = null;

    public interface SideMenuItemClickListener {
		public void onMenuItemSelected(int position);
	}

	private SideMenuItemClickListener mCallback;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_side_menu, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();
		delegate.setMenuFragment(this);
		mCallback = (SideMenuItemClickListener) delegate;

        profileCompleteness = 75;
		// initialize members
		initViewAndClassMembers();

	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {
        this.view.findViewById(R.id.menu_home).setOnClickListener(this);
        this.view.findViewById(R.id.menu_browse).setOnClickListener(this);
        this.view.findViewById(R.id.menu_live).setOnClickListener(this);
        this.view.findViewById(R.id.menu_watch).setOnClickListener(this);
//        this.view.findViewById(R.id.menu_account).setOnClickListener(this);
        this.view.findViewById(R.id.menu_settings).setOnClickListener(this);
        this.view.findViewById(R.id.menu_logout).setOnClickListener(this);
        this.view.findViewById(R.id.menu_about).setOnClickListener(this);

        ((TextView)this.view.findViewById(R.id.tv_home)).setTextColor(getResources().getColor(R.color.white));
        circularBar = (CircularBarPager) this.view.findViewById(R.id.circle_progress);
        avatarView = (DynamicImageView)this.view.findViewById(R.id.image_avatar);
        this.view.findViewById(R.id.select_avatar).setOnClickListener(this);
        animateCircularBar();
    }

    public void selectHomeItem(){
        int whiteColor = getResources().getColor(R.color.white);
        int lightGray = getResources().getColor(R.color.light_gray);


        TextView tvHome = (TextView)this.view.findViewById(R.id.tv_home);
        tvHome.setTextColor(whiteColor);
        this.view.findViewById(R.id.imgv_home).setVisibility(View.VISIBLE);

        TextView tvBrowse = (TextView)this.view.findViewById(R.id.tv_browse);
        tvBrowse.setTextColor(lightGray);
        this.view.findViewById(R.id.imgv_browse).setVisibility(View.INVISIBLE);

        TextView tvLive = (TextView)this.view.findViewById(R.id.tv_live);
        tvLive.setTextColor(lightGray);
        this.view.findViewById(R.id.imgv_live).setVisibility(View.INVISIBLE);

        TextView tvWatch = (TextView)this.view.findViewById(R.id.tv_watch);
        tvWatch.setTextColor(lightGray);
        this.view.findViewById(R.id.imgv_watch).setVisibility(View.INVISIBLE);

        TextView tvAccount = (TextView)this.view.findViewById(R.id.tv_account);
        tvAccount.setTextColor(lightGray);
        this.view.findViewById(R.id.imgv_account).setVisibility(View.INVISIBLE);

        TextView tvSetting = (TextView)this.view.findViewById(R.id.tv_settings);
        tvSetting.setTextColor(lightGray);
        this.view.findViewById(R.id.imgv_setting).setVisibility(View.INVISIBLE);

        TextView tvAbout = (TextView)this.view.findViewById(R.id.tv_about);
        tvAbout.setTextColor(lightGray);
        this.view.findViewById(R.id.imgv_about).setVisibility(View.INVISIBLE);

        TextView tvLogout = (TextView)this.view.findViewById(R.id.tv_logout);
        tvLogout.setTextColor(lightGray);
    }

    public void selectMenuItem(int position) {
        int whiteColor = getResources().getColor(R.color.white);
        int lightGray = getResources().getColor(R.color.light_gray);


        TextView tvHome = (TextView)this.view.findViewById(R.id.tv_home);
        tvHome.setTextColor(position == 0 ? whiteColor : lightGray);
        this.view.findViewById(R.id.imgv_home).setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);

        TextView tvBrowse = (TextView)this.view.findViewById(R.id.tv_browse);
        tvBrowse.setTextColor(position == 1 ? whiteColor:lightGray);
        this.view.findViewById(R.id.imgv_browse).setVisibility(position == 1 ? View.VISIBLE : View.INVISIBLE);

        TextView tvLive = (TextView)this.view.findViewById(R.id.tv_live);
        tvLive.setTextColor(position == 2 ? whiteColor : lightGray);
        this.view.findViewById(R.id.imgv_live).setVisibility(position == 2 ? View.VISIBLE : View.INVISIBLE);

        TextView tvWatch = (TextView)this.view.findViewById(R.id.tv_watch);
        tvWatch.setTextColor(position == 3 ? whiteColor : lightGray);
        this.view.findViewById(R.id.imgv_watch).setVisibility(position == 3 ? View.VISIBLE : View.INVISIBLE);

        TextView tvAccount = (TextView)this.view.findViewById(R.id.tv_account);
        tvAccount.setTextColor(position == 4 ?whiteColor:lightGray);
        this.view.findViewById(R.id.imgv_account).setVisibility(position == 4 ? View.VISIBLE : View.INVISIBLE);

        TextView tvSetting = (TextView)this.view.findViewById(R.id.tv_settings);
        tvSetting.setTextColor(position == 5 ? whiteColor : lightGray);
        this.view.findViewById(R.id.imgv_setting).setVisibility(position == 5 ? View.VISIBLE : View.INVISIBLE);

        TextView tvAbout = (TextView)this.view.findViewById(R.id.tv_about);
        tvAbout.setTextColor(position == 6 ? whiteColor : lightGray);
        this.view.findViewById(R.id.imgv_about).setVisibility(position == 6 ? View.VISIBLE : View.INVISIBLE);

        TextView tvLogout = (TextView)this.view.findViewById(R.id.tv_logout);
        tvLogout.setTextColor(position == 7 ? whiteColor : lightGray);

		mCallback.onMenuItemSelected(position);
	}

    private void deselectAllMenu() {
    }

    public void animateCircularBar(){
        circularBar.getCircularBar().animateProgress(0, profileCompleteness, ANIMATE_DURATION);
    }

    public void updateUserProfileImage(){
//        avatarView.setImageBitmap(UserInformation.getInstance().bmpUser);
        avatarView.setImageResource(R.drawable.pic_sample_neymar);
    }

    private void selectImage() {

        final CharSequence[] items = { "Take a Photo", "Select one from Camera Roll", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(delegate);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                m_imgSelName = null;
                if (item == 0) {
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
    public void onClick(View view) {
        deselectAllMenu();

        if (view.getId() == R.id.menu_home) {
            selectMenuItem(0);
        } else  if (view.getId() == R.id.menu_browse) {
            selectMenuItem(1);
        } else  if (view.getId() == R.id.menu_live) {
            selectMenuItem(2);
        } else  if (view.getId() == R.id.menu_watch) {
            selectMenuItem(3);
        } else  if (view.getId() == R.id.menu_account) {
            selectMenuItem(4);
        } else  if (view.getId() == R.id.menu_settings) {
            selectMenuItem(5);
        } else  if (view.getId() == R.id.menu_about) {
            selectMenuItem(6);
        } else if  (view.getId() == R.id.menu_logout){
            selectMenuItem(7);
        } else if (view.getId() == R.id.select_avatar) {
            selectImage();
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
