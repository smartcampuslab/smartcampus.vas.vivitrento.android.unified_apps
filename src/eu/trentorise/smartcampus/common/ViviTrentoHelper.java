package eu.trentorise.smartcampus.common;

import android.content.Context;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.pushservice.NotificationCenter;


public class ViviTrentoHelper {

	public static final String APP_TOKEN = "viaggiatrento";
	public static final String SYNC_DB_NAME = "viaggiatrento_notifications";
//	public static final String SYNC_SERVICE = "/communicator/sync";
	private final static String CORE_MOBILITY = "core.mobility";
	private static final int MAX_MSG = 50;
	
	private static Context mContext;
	private static ViviTrentoHelper helper;

	private static SCAccessProvider accessProvider;
	public static NotificationCenter notificationCenter;


	public ViviTrentoHelper(Context ctx) {
		this.mContext = ctx;
	}

	public static void init(Context ctx) {
		if (helper == null) {
			helper = new ViviTrentoHelper(ctx);
		}
		
		notificationCenter = new NotificationCenter(ctx);
		
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
