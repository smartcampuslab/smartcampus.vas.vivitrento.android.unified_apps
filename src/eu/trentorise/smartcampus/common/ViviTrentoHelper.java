package eu.trentorise.smartcampus.common;

import android.content.Context;
import eu.trentorise.smartcampus.ac.SCAccessProvider;

public class ViviTrentoHelper {

	public static final String APP_TOKEN = "viaggiatrento";
	public static final String SYNC_DB_NAME = "viaggiatrento_notifications";
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
//		try{
//		NotificationsHelper.init(ctx, APP_TOKEN, ctx.getString(R.string.notificationsprovider_authority), CORE_MOBILITY, MAX_MSG);
//		NotificationsHelper.start(true);
//		} catch (Exception e) {
//			Log.e(ctx.getClass().getName(), e.toString());
//			e.printStackTrace();
//			Toast.makeText(ctx.getApplicationContext(),
//					ctx.getString(R.string.app_failure_operation),
//					Toast.LENGTH_SHORT).show();
//		}
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
