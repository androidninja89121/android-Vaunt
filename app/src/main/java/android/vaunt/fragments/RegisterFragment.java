package android.vaunt.fragments;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.Nullable;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.activities.BaseActivity;
import android.vaunt.activities.OnboardingActivity;
import android.vaunt.utility.UserInformation;
import android.vaunt.utility.Utils;
import android.vaunt.web.ServerConnect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterFragment extends BaseFragment implements OnClickListener, ServerConnect.APIResponseListener {

	// Holds activity delegate instance
	private OnboardingActivity delegate;
    private JSONObject registrationParams;
	// View members
	private View view;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirm;
    private EditText etZipCode;

	public static RegisterFragment newInstance() {
		return new RegisterFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_register, container,
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
        vi.findViewById(R.id.btn_register).setOnClickListener(this);
        vi.findViewById(R.id.btn_return).setOnClickListener(this);
        etFirstName = (EditText)vi.findViewById(R.id.et_first_name);
        etLastName = (EditText)vi.findViewById(R.id.et_last_name);
        etEmail = (EditText)vi.findViewById(R.id.et_email);
        etPassword = (EditText)vi.findViewById(R.id.et_password);
        etConfirm = (EditText)vi.findViewById(R.id.et_confirm_password);
        etZipCode = (EditText)vi.findViewById(R.id.et_zipcode);
	}

	@Override
	public void onClick(View v) {

        if (v.getId() == R.id.btn_return) {
            delegate.onBackPressed();
        } else if (v.getId() == R.id.btn_register) {
            onRegisterClicked();
        }

	}

    private void onRegisterClicked() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirm = etConfirm.getText().toString();
        String zipCode = etZipCode.getText().toString();

        if (firstName.length() == 0) {
            Utils.showMessage("Please enter first name.");
            return;
        }

        if (lastName.length() == 0) {
            Utils.showMessage("Please enter last name.");
            return;
        }

        if (email.length() == 0) {
            Utils.showMessage("Please enter valid email address.");
            return;
        }

        if (password.length() < 8 || confirm.length() < 8) {
            Utils.showMessage("Password must be at least 8 characters long.");
            return;
        }

        if (!password.equals(confirm)) {
            Utils.showMessage("Passwords do not match.");
            return;
        }

        if (zipCode.length() == 0) {
            Utils.showMessage("Please enter zip code.");
            return;
        }

        registrationParams = new JSONObject();
        try {
            registrationParams.put(Constants.USERINFO_HASH_KEY, Constants.kWSApiSharedKey);
            registrationParams.put(Constants.USERINFO_FIRSTNAME_KEY, firstName);
            registrationParams.put(Constants.USERINFO_LASTNAME_KEY, lastName);
            registrationParams.put(Constants.USERINFO_EMAIL_KEY, email);
            registrationParams.put(Constants.USERINFO_PASSWORD_KEY, password);
            registrationParams.put(Constants.USERINFO_ZIPCODE_KEY, zipCode);
            registrationParams.put(Constants.USERINFO_DEVICETOKEN_KEY, "xxxx");
            registrationParams.put(Constants.USERINFO_DEVICEMODEL_KEY, Build.MODEL);
            registrationParams.put(Constants.USERINFO_DEVICEUID_KEY, Secure.getString(delegate.getContext().getContentResolver(),
                    Secure.ANDROID_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utils.showProgDialog(delegate, null, "Registering", null);
        ServerConnect.POST(Constants.kWSApiRegister, registrationParams.toString(), this);

    }

    @Override
    public void onSuccess(JSONObject json) {


        Utils.printLog(json.toString());
        Utils.hideProgDialog();

        try {
            int nCode = json.getInt(Constants.kWSApiResponseCode);
            String strMsg = json.getString(Constants.kWSApiResponseMessage);

            if(nCode == Constants.SUCCESS_CODE){
                JSONObject jsonObj = json.getJSONObject(Constants.kWSApiResponseData);
                UserInformation.getInstance().authTokenKey = jsonObj.getString(Constants.kUDAuthToken);
                UserInformation.getInstance().email = registrationParams.getString(Constants.USERINFO_EMAIL_KEY);

                SelectAvatarFragment fragment = SelectAvatarFragment.newInstance();
                baseActivity.showFragment(R.id.fl_fragment_container, fragment,
                        true, BaseActivity.CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
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
