package android.vaunt;

import android.app.Application;
import android.vaunt.utility.TypeFaceUtil;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class VauntApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		TypeFaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/OpenSans-Regular.ttf");
	}

}
