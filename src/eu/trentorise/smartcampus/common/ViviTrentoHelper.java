package eu.trentorise.smartcampus.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.notifications.NotificationsHelper;
import eu.trentorise.smartcampus.viaggiarovereto.R;

public class ViviTrentoHelper {

	public static final String APP_TOKEN = "vivitrento";
	public static final String SYNC_DB_NAME = "vivitrento_notifications";
	public static final String SYNC_SERVICE = "/communicator/sync";
	private static final String SHOWED_T_D = "showed terms dialog";
	public static final String T_D_PREFS = "t_d_prefs";

	private static Context mContext;
	private static ViviTrentoHelper helper;

	private static SCAccessProvider accessProvider;

	public ViviTrentoHelper(Context ctx) {
		this.mContext = ctx;
	}

	public static void init(Context ctx) {
		if (helper == null) {
			helper = new ViviTrentoHelper(ctx);
		}

		NotificationsHelper.init(ctx, APP_TOKEN, SYNC_DB_NAME, SYNC_SERVICE,
				ctx.getString(R.string.notificationsprovider_authority));
	}

	public static boolean isInstantiated() {
		return (helper != null);
	}

	public static SCAccessProvider getAccessProvider() {
		if (accessProvider == null) {
			accessProvider = new AMSCAccessProvider();
		}

		return accessProvider;
	}

	public static boolean showTermsDialog(SharedPreferences sp) {
		SharedPreferences sharedPreferences = sp;
		if (sharedPreferences.contains(SHOWED_T_D)) {
			return false;
		} else {
			return true;
		}
	}

	public static void setShowedTermsDialog(SharedPreferences sp) {
		SharedPreferences sharedPreferences = sp;
		if (!sharedPreferences.contains(SHOWED_T_D)) {
			Editor editor = sharedPreferences.edit();
			editor.putBoolean(SHOWED_T_D, true);
			editor.commit();
		}

	}
}
