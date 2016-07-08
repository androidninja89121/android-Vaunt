package android.vaunt.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.MainActivity;
import android.vaunt.activities.OnboardingActivity;
import android.vaunt.utility.DBUtil;
import android.vaunt.utility.UserInformation;
import android.vaunt.utility.Utils;
import android.vaunt.web.ServerConnect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends BaseFragment implements OnClickListener, ServerConnect.APIResponseListener {

	// Holds activity delegate instance
	private OnboardingActivity delegate;

	// View members
	private View view;
    private EditText etEmail;
    private EditText etPassword;
    String strAuthToken;

	public static LoginFragment newInstance() {
		return new LoginFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_login, container,
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
        vi.findViewById(R.id.btn_login).setOnClickListener(this);
        vi.findViewById(R.id.btn_return).setOnClickListener(this);
        etEmail = (EditText)vi.findViewById(R.id.et_email);
        etPassword = (EditText)vi.findViewById(R.id.et_password);

	}

    private void onLoginClicked() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.length() == 0) {
            Utils.showMessage("Please enter valid email address.");
            return;
        }

        if (password.length() < 8 ) {
            Utils.showMessage("Password must be at least 8 characters long.");
            return;
        }

        String encryptPass = Utils.getEncryptedPassword(password);
        Utils.printLog(encryptPass);
        JSONObject json = new JSONObject();
        try {
            json.put(Constants.USERINFO_EMAIL_KEY, email);
            json.put(Constants.USERINFO_HASHEDPASSWORK_KEY, encryptPass);
            json.put(Constants.USERINFO_DEVICETOKEN_KEY, "");
            json.put(Constants.USERINFO_DEVICEMODEL_KEY, Build.MODEL);
            json.put(Constants.USERINFO_DEVICEUID_KEY, Settings.Secure.getString(delegate.getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utils.showProgDialog(delegate, null, "Logging in", null);
        ServerConnect.POST(Constants.kWSApiLogin, json.toString(), this);

    }

	@Override
	public void onClick(View v) {

        if (v.getId() == R.id.btn_return) {
            FirstFragment fragment = FirstFragment.newInstance();
            baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                    true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_LEFT);
        } else if (v.getId() == R.id.btn_login) {
//            onLoginClicked();
            SharedPreferences pref = this.delegate.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean(Constants.kUDLoggedIn, true);
            editor.commit();
            delegate.onLogin();
        }

	}

    @Override
    public void onSuccess(JSONObject json) {
        try {
            int nCode = json.getInt(Constants.kWSApiResponseCode);
            String strMsg = json.getString(Constants.kWSApiResponseMessage);

            if(nCode == Constants.SUCCESS_CODE) {
                JSONObject jsonObj = json.getJSONObject(Constants.kWSApiResponseData);

                if(strMsg.equals(Constants.MSG_WELCOME)){
                    strAuthToken = jsonObj.getString(Constants.kUDAuthToken);
                    UserInformation.getInstance().setInformation(strAuthToken);

                    try {
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put(Constants.kUDAuthToken, strAuthToken);
                        ServerConnect.POST(Constants.kWSApiPublisherAllList, jsonParam.toString(), this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Utils.hideProgDialog();
                    DBUtil.getInstance().processPublisherData(jsonObj);
                    delegate.onLogin();
                }

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
    }
}
