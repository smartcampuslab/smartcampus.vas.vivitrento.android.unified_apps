package eu.trentorise.smartcampus.vivitrento.widget.shortcuts;

import eu.trentorise.smartcampus.dt.DiscoverTrentoActivity;
import eu.trentorise.smartcampus.jp.HomeActivity;
import eu.trentorise.smartcampus.vivitrento.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class SmartCampusShortCuts extends AppWidgetProvider {
	private static final String ACTION_CLICK = "ACTION_CLICK";

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Toast.makeText(context, "Set shortcuts", Toast.LENGTH_SHORT).show();
	}
	  @Override
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	      int[] appWidgetIds) {
			Toast.makeText(context, "Update shortcuts", Toast.LENGTH_SHORT).show();
	        final int N = appWidgetIds.length;
	        
	        // Perform this loop procedure for each App Widget that belongs to this provider
	        for (int i=0; i<N; i++) {
	            int appWidgetId = appWidgetIds[i];
	            // Create an Intent to launch ExampleActivity
	            Intent intent = new Intent(context, DiscoverTrentoActivity.class);
	            intent.putExtra("FRAGMENT", "TODAY");
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
	                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);

	            PendingIntent dtpendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	            
	            
	            intent = new Intent(context, HomeActivity.class);
	            intent.putExtra("FRAGMENT", "BUS");
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
	                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            
	            PendingIntent jppendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	            // Get the layout for the App Widget and attach an on-click listener
	            // to the button
				RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.shortcuts_widget);
	            views.setOnClickPendingIntent(R.id.button1, dtpendingIntent);
	            
	            views.setOnClickPendingIntent(R.id.button2, jppendingIntent);

	            // Tell the AppWidgetManager to perform an update on the current app widget
	            appWidgetManager.updateAppWidget(appWidgetId, views);
	            

	  }
}
}
