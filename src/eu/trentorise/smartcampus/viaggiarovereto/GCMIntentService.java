package eu.trentorise.smartcampus.viaggiarovereto;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import eu.trentorise.smartcampus.jp.notifications.NotificationsFragmentActivityJP;
import eu.trentorise.smartcampus.pushservice.NotificationCenter;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	private static final int NOTIFICATION_ID = 1234;

	public GCMIntentService() {
		super(TAG);
	}

	@Override
	protected void onError(Context ctx, String sError) {
		Log.d(TAG, "Error: " + sError);

	}

	@Override
	protected void onMessage(Context ctx, Intent intent) {

		Log.d(TAG, "Message Received");

		new NotificationCenter(ctx).publishNotification(intent,
				NOTIFICATION_ID, NotificationsFragmentActivityJP.class);

	}

	@Override
	protected void onRegistered(Context ctx, String regId) {
		// send regId to your server

	}

	@Override
	protected void onUnregistered(Context ctx, String regId) {
		// send notification to your server to remove that regId
	}

}
