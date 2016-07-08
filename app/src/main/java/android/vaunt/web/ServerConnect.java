package android.vaunt.web;

import android.os.AsyncTask;
import android.util.Base64;
import android.vaunt.Constants;
import android.vaunt.utility.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 9/21/2015.
 */
public class ServerConnect {

    public interface APIResponseListener {
        public void onSuccess(JSONObject result);
        public void onFailure();
    }

    public static void GET(String url, String json, final APIResponseListener responseListener) {

    }
    public static void POST(final String url, final String json, final APIResponseListener responseListener) {
        new AsyncTask<Void, Void, JSONObject>(){

            @Override
            protected JSONObject doInBackground(Void... voids) {
                final HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
                HttpClient httpclient = new DefaultHttpClient(httpParams);
                HttpPost httppost = new HttpPost(Constants.kWSApiBaseUrl + "/" + url);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
                try {
                    httppost.setEntity(new StringEntity(json));
                    HttpResponse response = httpclient.execute(httppost);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null;) {
                        builder.append(line).append("\n");
                    }
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    JSONObject finalResult = new JSONObject(tokener);
                    Utils.printLog(finalResult.toString());
                    return finalResult;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public void onPostExecute (JSONObject result) {
                if (result != null) {
                    responseListener.onSuccess(result);
                }
                else
                    responseListener.onFailure();
            }
        }.execute();
    }

    public static void UploadFiles(final String url, final JSONObject datas, final JSONObject params, final APIResponseListener responseListener) {
        new AsyncTask<Void, Void, JSONObject>(){

            @Override
            protected JSONObject doInBackground(Void... voids) {
                final HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
                HttpClient httpclient = new DefaultHttpClient(httpParams);
                HttpPost httppost = new HttpPost(Constants.kWSApiBaseUrl + "/" + url);
//                httppost.setHeader("Accept", "application/json");
//                httppost.setHeader("Content-type", "application/json");
                try {
                    byte[] byte_arr = (byte[])datas.get("data");
                    ByteArrayBody bab = new ByteArrayBody(byte_arr, datas.getString("fileName"));

                    MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                    reqEntity.addPart("avatar", bab);
                    reqEntity.addPart(Constants.kUDAuthToken, new StringBody(params.getString(Constants.kUDAuthToken)));
                    reqEntity.addPart(Constants.USERINFO_USERNAME_KEY, new StringBody(params.getString(Constants.USERINFO_USERNAME_KEY)));

                    if(params.has(Constants.USERINFO_FIRSTNAME_KEY)){
                        reqEntity.addPart(Constants.USERINFO_FIRSTNAME_KEY, new StringBody(params.getString(Constants.USERINFO_FIRSTNAME_KEY)));
                        reqEntity.addPart(Constants.USERINFO_LASTNAME_KEY, new StringBody(params.getString(Constants.USERINFO_LASTNAME_KEY)));
                        reqEntity.addPart(Constants.USERINFO_EMAIL_KEY, new StringBody(params.getString(Constants.USERINFO_EMAIL_KEY)));
                        reqEntity.addPart(Constants.USERINFO_ZIPCODE_KEY, new StringBody(params.getString(Constants.USERINFO_ZIPCODE_KEY)));
                    }

                    httppost.setEntity(reqEntity);
                    HttpResponse response = httpclient.execute(httppost);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null;) {
                        builder.append(line).append("\n");
                    }
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    JSONObject finalResult = new JSONObject(tokener);
                    Utils.printLog(finalResult.toString());
                    return finalResult;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            public void onPostExecute (JSONObject result) {
                if (result != null) {
                    responseListener.onSuccess(result);
                }
                else
                    responseListener.onFailure();
            }
        }.execute();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        responseListener.onSuccess(null);
    }
}
