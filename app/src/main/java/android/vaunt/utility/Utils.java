package android.vaunt.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.vaunt.activities.BaseActivity;
import android.vaunt.fragments.BaseFragment;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

    /**
	 * Print Log
	 */

	public static String TAG = BaseFragment.class.getName();

	public static void setTAG(String tag) {
		TAG = tag;
	}

	public static void printLog(String log) {
		Log.e(TAG, log);
	}

	public static void printLog(int log) {
		Log.e(TAG, getStringFromRes(log));
	}


    public static void initImageLoader(Context context){
        ImageLoaderConfiguration defaultConfiguration
                = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(defaultConfiguration);
    }
	/**
	 * Show toast view
	 */
	public static void showMessage(String message) {
		if (message != null) {
            printLog(message);
			Toast.makeText(BaseActivity.getContext(), message,
					Toast.LENGTH_SHORT).show();
		}
	}

	public static void showMessage(int message) {
		printLog(message);
		Toast.makeText(BaseActivity.getContext(), getStringFromRes(message),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Convert String to java.util.Date
	 */
	public static Date dateFromString(String dateString) {
		Date convertedDate = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			convertedDate = formatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertedDate;
	}

	/**
	 * Convert String to Calendar
	 */
	public static Calendar stringToCalendar(String dateString,
			String formatString) {
		Calendar convertedDate = Calendar.getInstance();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formatString);
			convertedDate.setTime(sdf.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			printLog(e.toString());
			e.printStackTrace();
		}
		return convertedDate;
	}

	public static Calendar stringToCalendar(String dateString, int formatString) {
		Calendar convertedDate = Calendar.getInstance();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					getStringFromRes(formatString));
			convertedDate.setTime(sdf.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			printLog(e.toString());
			e.printStackTrace();
		}
		return convertedDate;
	}

	/**
	 * Convert String to Calendar
	 */
	public static String calendarToString(Calendar date, String formatString) {
		String dateString = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		dateString = sdf.format(date.getTime());
		return dateString;
	}

	public static String calendarToString(Calendar date, int formatString) {
		String dateString = null;
		SimpleDateFormat sdf = new SimpleDateFormat(
				getStringFromRes(formatString));
		dateString = sdf.format(date.getTime());
		return dateString;
	}
	
	public static int minutesBetweenDates(Calendar from, Calendar to) {
		long ms = to.getTimeInMillis() - from.getTimeInMillis();
		int minutes = (int)(ms/60000);
		return minutes;
	}

	/**
	 * Get String from id defined in string.xml
	 */
	public static String getStringFromRes(int resId) {
		return BaseActivity.getContext().getResources().getString(resId);
	}

	/**
	 * Get int from id defined in string.xml
	 */
	public static int getIntFromRes(int resId) {
		return BaseActivity.getContext().getResources().getInteger(resId);
	}

	/** Validate email address **/
	public static boolean validateEmail(String email) {
		Pattern p = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,4}",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/** Validate telephone number **/
	public static boolean validatePhone(String phone, int length) {
		Pattern p = Pattern.compile(String.format("[0-9]{%d}", length),
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * Progress bar
	 */
    public static ProgressDialog m_PrepareDialog;

    public static void showProgDialog(Context context, String title,
                                      String message, DialogInterface.OnDismissListener listener) {
        m_PrepareDialog = new ProgressDialog(context);
        if (title != null && title.length() > 0)
            m_PrepareDialog.setTitle(title);
        if (message != null && message.length() > 0)
            m_PrepareDialog.setMessage(message);
        m_PrepareDialog.setIndeterminate(true);
        m_PrepareDialog.setCanceledOnTouchOutside(false);
        m_PrepareDialog.setCancelable(true);
        if (listener != null)
            m_PrepareDialog.setOnDismissListener(listener);
        m_PrepareDialog.show();
    }

    public static void hideProgDialog() {
        if (m_PrepareDialog != null) {
            m_PrepareDialog.dismiss();
            m_PrepareDialog = null;
        }
    }

    public static int getMipmapResId(Context context, String drawableName) {
        return context.getResources().getIdentifier(
                drawableName.toLowerCase(Locale.ENGLISH), "mipmap", context.getPackageName());
    }

    public static JSONObject getCountriesJSON(Context context) {
        String resourceName = "countries_dialing_code";
        int resourceId = context.getResources().getIdentifier(
                resourceName, "raw", context.getApplicationContext().getPackageName());

        if (resourceId == 0)
            return null;

        InputStream stream = context.getResources().openRawResource(resourceId);

        try {
            return new JSONObject(convertStreamToString(stream));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static ArrayList<String> getNumberList(int min, int max, int interval) {
        ArrayList<String> list = new ArrayList<String>();
        for (int n = min; n <= max; n+= interval) {
            list.add(String.valueOf(n));
        }
        return list;
    }

    public static String getCurrentTime() {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss.SSS a");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String reportDate = df.format(today);
        return reportDate;
    }

    public final static String APP_PATH_SD_CARD = "/DogTraining";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "/Avatars";
    public static String m_avatarPath;
    public static File m_avatarDir;

    public static void InitAppPath() {
        m_avatarPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + APP_PATH_SD_CARD
                + APP_THUMBNAIL_PATH_SD_CARD;
        m_avatarDir = new File(m_avatarPath);
        if (!m_avatarDir.exists()) {
            m_avatarDir.mkdirs();
        }
    }

    public static boolean saveImageToExternalStorage(Context context,
                                                     Bitmap image, String fname) {

        try {
            File dir = new File(m_avatarPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            OutputStream fOut = null;
            File file = new File(m_avatarPath, fname);
            file.createNewFile();
            fOut = new FileOutputStream(file);

            // 100 means no compression, the lower you go, the stronger the
            // compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            // MediaStore.Images.Media.insertImage(context.getContentResolver(),
            // file.getAbsolutePath(), file.getName(), file.getName());

            return true;

        } catch (Exception e) {
            printLog(e.getMessage());
            return false;
        }
    }

    public static void deleteImage(String fname) {

        try {
            File dir = new File(m_avatarPath);
            if (!dir.exists()) {
                return;
            }

            File file = new File(m_avatarPath, fname);
            if (file.exists())
                file.delete();

        } catch (Exception e) {
            printLog(e.getMessage());
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getEncryptedPassword(String clearTextPassword)   {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(clearTextPassword.getBytes("UTF-8"));
            return String.format("%0" + (hash.length * 2) + 'x', new BigInteger(1, hash));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Bitmap GetCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffff0000;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 4);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//and then I convert that image into a byteArray
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();

    }

}