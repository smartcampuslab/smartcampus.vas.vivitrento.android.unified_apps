package eu.trentorise.smartcampus.viaggiarovereto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import eu.trentorise.smartcampus.jp.notifications.JPPushNotificationBuilder;
import eu.trentorise.smartcampus.jp.notifications.NotificationsFragmentActivityJP;
import eu.trentorise.smartcampus.pushservice.NotificationCenter;

public class GCMBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "GCMIntentService";
	private static final int NOTIFICATION_ID = 1234;

	@Override
	public void onReceive(Context context, Intent intent) {
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);

		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			// It's an error.
			Log.d(TAG, "Error!");
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			// Deleted messages on the server.
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
			// It's a regular GCM message.
			Log.d(TAG, "Message Received");
			new NotificationCenter(context).publishNotification(context, intent, new JPPushNotificationBuilder(),
					NOTIFICATION_ID, NotificationsFragmentActivityJP.class);
		}
	}
}
