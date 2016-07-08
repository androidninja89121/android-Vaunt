package android.vaunt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.vaunt.activities.BaseActivity;
import android.vaunt.fragments.BaseFragment;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {

    // Web Service Apis
    public static final String
    kWSApiBaseUrl =  "http://52.21.152.22/api/v1",
    kWSApiSharedKey              = "80fa115c9eca9a8d56b5fa7b221ec09cbf085d22ad2f55e1996268b59bcf7234",
    kWSApiVerifyAuthToken        = "subscriber/verify",
    kWSApiRegister               = "subscriber/register",
    kWSApiLogin                  = "subscriber/login",
    kWSApiUpdate                 = "subscriber/profile/update",
    kWSApiProfile                = "subscriber/profile",
    kWSApiPublisherAllList       = "publisher/all_list",
    kWSApiStreamInfo             = "stream/info",
    kVODWatchList                = "VODWatchList",
    kLIVEWatchList               = "LiveWatchList",
    kS3AWSImageBaseUrl           = "http://d2mfxjpe4qh8ou.cloudfront.net/images",
    kS3CoreImage                 = "coverimages",
    kWSApiResponseSuccess        = "success",
    kWSApiResponseMessage        = "message",
    kWSApiResponseCode           = "code",
    kWSApiResponseData           = "data",
    kWSApiResponsePublishers     = "publishers";

    //webservice post param keywords
    public static final String
     USERINFO_HASH_KEY                        = "hashed_shared_key",
     USERINFO_FIRSTNAME_KEY                   = "first_name",
     USERINFO_LASTNAME_KEY                    = "last_name",
     USERINFO_EMAIL_KEY                       = "email",
     USERINFO_PASSWORD_KEY                    = "password",
     USERINFO_ZIPCODE_KEY                     = "zip_code",
     USERINFO_DEVICETOKEN_KEY                 = "device_token",
     USERINFO_DEVICEMODEL_KEY                 = "device_model",
     USERINFO_DEVICEUID_KEY                   = "device_uid",
     USERINFO_USERNAME_KEY                    = "username",
     USERINFO_AVATAR_KEY                      = "avatar",
     USERINFO_HASHEDPASSWORK_KEY              = "hashed_password",
     USERINFO_USER_KEY                        = "user",
     USERINFO_SUBSCRIBER_KEY                  = "subscriber",
     USERINFO_UID                            = "uid",
     USERINFO_DESCRIPTION                     = "description",
     USERINFO_STREAM_COUNT                    = "stream_count",
     USERINFO_BANNER                          = "banner";

    public static final String
    ITEM_CATEGORY                             = "category",
    ITEM_TITLE                                = "title",
    ITEM_COVER_IMAGE                          = "CoverImage",
    ITEM_BIG_IMAGE                            = "BigImage",
    ITEM_TEXT                                 = "text",
    ITEM_VIEWERS                              = "viewers",
    ITEM_FB_SCRIBERS                          = "fb_subscribers",
    ITEM_TW_SCRIBERS                          = "tw_subscribers",
    ITEM_ISONLINE                             = "isOnline",
    ITEM_ISLIVE                               = "isLive",
    ITEM_SUB_TITLE                            = "SubTitle",
    ITEM_MA                                   = "isMA";

    public static final String
    YES                                       = "YES",
    NO                                        = "NO";

    public static final String
    CAT_SPORTS                                = "Sports",
    CAT_MUSIC                                 = "Music",
    CAT_MODELS                                = "Models",
    CAT_LIFESTYLE                             = "LifeStyle",
    CAT_FITNESS                               = "Fitness",
    CAT_FASHION                               = "Fashion",
    CAT_COMEDY                                = "Comedy",
    CAT_BEAUTY                                = "Beauty",
    CAT_ORIGINAL_SERIES                       = "Original Series";

    public static final int
    FROM_HOME                                 = 0,
    FROM_BROWSE                               = 1,
    FROM_LIVE                                 = 2;

    public static final String
     MSG_WELCOME                              = "Welcome!",
     MGS_SUCCESS_PUBLISHER                    = "Publisher list get successfully";


    // NSUserDefaults Keys
    public static final String
    kUDAppLastStartTime          = "appLastStartTime",
    kUDEmail                     = "email",
    kUDMsg                       = "message",
    kUDAuthToken                 = "auth_token",
    kUDLoggedIn                  = "logged_in",
    kUDPushDeviceToken           = "pushDeviceToken";


    public static final int
    SUCCESS_CODE = 200,
    DRAKE_PROFILE_INDEX = 0,
    KENDAL_JENNAR_PROFILE_INDEX = 1,
    KEVIN_HART_PROFILE_INDEX = 0,
    CHRIS_BROWN_PROFILE_INDEX = 1,
    JEN_SELTER_PROFILE_INDEX = 2,
    NICKI_MINAJ_PROFILE_INDEX = 13,
    AMBER_ROSE_PROFILE_INDEX = 4;
}