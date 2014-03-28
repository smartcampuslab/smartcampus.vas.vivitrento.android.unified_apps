package eu.trentorise.smartcampus.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.jp.helper.JPParamsHelper;
import eu.trentorise.smartcampus.notifications.NotificationsHelper;
import eu.trentorise.smartcampus.viaggiatrento.LauncherActivity;
import eu.trentorise.smartcampus.viaggiatrento.R;

public class ViviTrentoHelper {

	public static final String APP_TOKEN = "vivitrento";
	public static final String SYNC_DB_NAME = "vivitrento_notifications";
//	public static final String SYNC_SERVICE = "/communicator/sync";
	private final static String CORE_MOBILITY = "core.mobility";
	private static final int MAX_MSG = 50;
	
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
		try{
		NotificationsHelper.init(ctx, JPParamsHelper.getAppToken(), null, CORE_MOBILITY, MAX_MSG);
		} catch (Exception e) {
			Log.e(ctx.getClass().getName(), e.toString());
			Toast.makeText(ctx.getApplicationContext(),
					ctx.getString(R.string.app_failure_operation),
					Toast.LENGTH_SHORT).show();
		}
	}
	
	public static boolean isInstantiated() {
		return (helper != null);
	}

	public static SCAccessProvider getAccessProvider() {
		if (accessProvider == null) {
			accessProvider = SCAccessProvider.getInstance(mContext);
		}

		return accessProvider;
	}


}
