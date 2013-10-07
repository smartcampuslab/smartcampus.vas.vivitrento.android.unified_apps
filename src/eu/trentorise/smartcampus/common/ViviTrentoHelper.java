package eu.trentorise.smartcampus.common;

import android.content.Context;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.notifications.NotificationsHelper;
import eu.trentorise.smartcampus.vivitrento.R;

public class ViviTrentoHelper {

	public static final String APP_TOKEN = "vivitrento";
	public static final String SYNC_DB_NAME = "vivitrento_notifications";
	public static final String SYNC_SERVICE = "/communicator/sync";

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
		NotificationsHelper.init(ctx, APP_TOKEN, SYNC_DB_NAME, SYNC_SERVICE, ctx.getString(R.string.notificationsprovider_authority));
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
}
