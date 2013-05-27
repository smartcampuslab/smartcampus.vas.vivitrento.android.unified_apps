package eu.trentorise.smartcampus.common;

import android.content.Context;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.jp.helper.JPHelper;
import eu.trentorise.smartcampus.jp.helper.JPParamsHelper;
import eu.trentorise.smartcampus.notifications.NotificationsHelper;

public class ViviTrentoHelper {

	public static String APP_TOKEN = "vivitrento";
	public static String SYNC_DB_NAME = "vivitrento_notifications";
	public static String SYNC_SERVICE = "/communicator/sync";
	public static String AUTHORITY = "eu.trentorise.smartcampus.notifications";

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
		
		NotificationsHelper.init(ctx, APP_TOKEN, SYNC_DB_NAME, SYNC_SERVICE, AUTHORITY);
	}

	public static SCAccessProvider getAccessProvider() {
		if (accessProvider == null) {
			accessProvider = new AMSCAccessProvider();
		}

		return accessProvider;
	}
}
