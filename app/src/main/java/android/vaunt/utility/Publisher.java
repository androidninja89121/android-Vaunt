package android.vaunt.utility;

import android.vaunt.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 10/14/2015.
 */
public class Publisher {
    public String uid;
    public String email;
    public String firstName;
    public String lastName;
    public String userName;

    public String description;
    public String avatar;
    public String banner;

    public int streamCount;

    public Publisher(JSONObject jsonObject){
        super();
        try {
            this.uid = jsonObject.getString(Constants.USERINFO_UID);
            this.email = jsonObject.getString(Constants.USERINFO_EMAIL_KEY);
            this.firstName = jsonObject.getString(Constants.USERINFO_FIRSTNAME_KEY);
            this.lastName = jsonObject.getString(Constants.USERINFO_LASTNAME_KEY);
            this.userName = jsonObject.getString(Constants.USERINFO_USERNAME_KEY);
            this.description = jsonObject.getString(Constants.USERINFO_DESCRIPTION);
            if(this.description.equals("null")) this.description = "";
            this.avatar = jsonObject.getString(Constants.USERINFO_AVATAR_KEY);
            this.banner = jsonObject.getString(Constants.USERINFO_BANNER);
            this.streamCount = jsonObject.getInt(Constants.USERINFO_STREAM_COUNT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Publisher(){
        super();
        this.uid = "uid";
        this.email = "email";
        this.firstName = "first name";
        this.lastName = "last name";
        this.userName = "user name";
        this.description = "description";
        this.avatar = "avatar";
        this.banner = "banner";
        this.streamCount = 0;
    }

    public String fullName(){
        return this.firstName + this.lastName;
    }

    public boolean isOnline(){
        return this.streamCount > 0;
    }
}
