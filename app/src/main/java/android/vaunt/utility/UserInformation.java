package android.vaunt.utility;

import android.graphics.Bitmap;
import android.vaunt.Constants;
import android.vaunt.web.ServerConnect;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 9/25/2015.
 */
public class UserInformation implements ServerConnect.APIResponseListener{
    static UserInformation instance = null;

    public String firstName;
    public String lastName;
    public String email;
    public String zipCode;
    public String userAccountName;
    public String avatarImage;
    public String authTokenKey;
    public Bitmap bmpUser;

    public static UserInformation getInstance() {
        if(instance == null){
            instance = new UserInformation();
        }
        return instance;
    }

    public UserInformation(){
        super();

        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.zipCode = "";
        this.userAccountName = "";
        this.avatarImage = "";
        this.authTokenKey = "";
    }

    public void setInformation(String strAuthToken){
        this.authTokenKey = strAuthToken;
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put(Constants.kUDAuthToken, strAuthToken);
            ServerConnect.POST(Constants.kWSApiProfile, jsonParam.toString(), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObj) {
        try {
            JSONObject jsonTmp = jsonObj.getJSONObject(Constants.kWSApiResponseData).getJSONObject(Constants.USERINFO_SUBSCRIBER_KEY);

            this.email = jsonTmp.getString(Constants.USERINFO_EMAIL_KEY);
            this.firstName = jsonTmp.getString(Constants.USERINFO_FIRSTNAME_KEY);
            this.lastName = jsonTmp.getString(Constants.USERINFO_LASTNAME_KEY);
            this.zipCode = jsonTmp.getString(Constants.USERINFO_ZIPCODE_KEY);
            this.userAccountName = jsonTmp.getString(Constants.USERINFO_USERNAME_KEY);
            this.avatarImage = jsonTmp.getString(Constants.USERINFO_AVATAR_KEY);

            ImageLoader.getInstance().loadImage(this.avatarImage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    UserInformation.getInstance().bmpUser = bitmap;
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure() {

    }
}
