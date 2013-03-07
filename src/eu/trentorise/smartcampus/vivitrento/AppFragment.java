/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either   express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.vivitrento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.common.AppInspector;
import eu.trentorise.smartcampus.common.LauncherException;
import eu.trentorise.smartcampus.common.Status;
import eu.trentorise.smartcampus.protocolcarrier.ProtocolCarrier;
import eu.trentorise.smartcampus.protocolcarrier.common.Constants.Method;
import eu.trentorise.smartcampus.protocolcarrier.custom.MessageRequest;
import eu.trentorise.smartcampus.protocolcarrier.custom.MessageResponse;
import eu.trentorise.smartcampus.vivitrento.apps.ApkInstaller.ApkDownloaderTask;
import eu.trentorise.smartcampus.vivitrento.models.SmartApp;
import eu.trentorise.smartcampus.vivitrento.models.UpdateModel;
import eu.trentorise.smartcampus.vivitrento.settings.SettingsActivity;
import eu.trentorise.smartcampus.vivitrento.util.ConnectionUtil;


public class AppFragment extends SherlockFragment {

	private ConnectivityManager mConnectivityManager;
	private AppInspector mInspector;
	
	
	 // variable used for changing version downloaded
	 
	private static final String KEY_UPDATE_DEV = "update_dev";
	
	
	 // variable used for forcing refresh coming back from setting activity
	 
	private static final String KEY_UPDATE_REFRESH = "refresh";	


	private int heightActionBar = 0;
	private AppTask mAppTask;
	private ApkDownloaderTask mDownloaderTask;
	public static final String PREFS_NAME = "LauncherPreferences";
	private static final String UPDATE = "_updateModel";
	private String UPDATE_ADDRESS = null;
	private String UPDATE_ADDRESS_DEV = null;
	private String UPDATE_HOST = null;
	private static final String LAUNCHER = "SmartLAuncher";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_URL = "url";
	private int[] version;
	private boolean toUpdate = true;
	private ProgressDialog progress = null;
	private boolean to_be_updated=false;
	private AppItem launcher;
	 // force the update pressing the menu button
	private boolean forced = false;
	private SharedPreferences settings = null;
	
	@Override
	public void onCreate(Bundle args) {
		super.onCreate(args);
		// Getting connectivity manager
		mConnectivityManager = ConnectionUtil
				.getConnectivityManager(getActivity());
		// Getting inspector
		mInspector = new AppInspector(getActivity());
		// Asking for an option menu
		setHasOptionsMenu(true);
		UPDATE_ADDRESS = getResources().getString(R.string.update_address);
		UPDATE_HOST = getResources().getString(R.string.update_host);

		// if you have some problem with the stored data, uncomment these lines
		// and the data are erased
		
//		  SharedPreferences settings =
//		  getActivity().getSharedPreferences(PREFS_NAME, 0);
//		  SharedPreferences.Editor editor = settings.edit(); editor.clear();
//		  editor.commit();
		 

		settings = getActivity().getSharedPreferences(
				PREFS_NAME, 0);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle args) {
		View v = inflater.inflate(R.layout.frag_apps, null);

		return v;
	}

	@Override
	public void onViewCreated(View v, Bundle args) {
		super.onViewCreated(v, args);

	}


	@Override
	public void onSaveInstanceState(Bundle arg0) {
		super.onSaveInstanceState(arg0);
	}
	public void check_version(){
		// Starting new task
		startNewAppTask();
	}
	@Override
	public void onStart() {
		super.onStart();
		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(false);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(
				false);
		getSherlockActivity().getSupportActionBar().setDisplayShowTitleEnabled(
				true);
		getSherlockActivity().getSupportActionBar().setTitle(
				getString(R.string.launcher_name));

		if (getSherlockActivity().getSupportActionBar().getNavigationMode() != ActionBar.NAVIGATION_MODE_STANDARD) {
			getSherlockActivity().getSupportActionBar().setNavigationMode(
					ActionBar.NAVIGATION_MODE_STANDARD);
		}

	}


	@Override
	public void onStop() {
		super.onStop();
		// Stopping any active task
		stopAnyActiveAppTask();
	}

	private void startNewAppTask() {
		// Stopping task
		stopAnyActiveAppTask();
		// Starting new one
		mAppTask = new AppTask();
		mAppTask.execute();

	}

	private void stopAnyActiveAppTask() {
		if (mAppTask != null && !mAppTask.isCancelled()) {
			mAppTask.cancel(true);
		}
	}


	private int[] readUpdateVersions(String[] packageNames,
			int[] defaultVersions) {


		int[] res = defaultVersions;
		UpdateModel update = null;
		long nextUpdate = -1;
		if (settings != null && settings.contains(UPDATE)) {
			nextUpdate = settings.getLong(UPDATE, -1);
		}
		

		if (nextUpdate < System.currentTimeMillis() || forced) {
			//if press the button check now and don't the next time
			if (!forced)
				toUpdate = true;

			// try to update
			
			String destination = new String(UPDATE_ADDRESS);
			if (settings.getBoolean(KEY_UPDATE_DEV,	false))
			{
				destination=UPDATE_ADDRESS_DEV;
			}
			MessageRequest req = new MessageRequest(UPDATE_HOST, destination);
			
			
			
			req.setMethod(Method.GET);
			ProtocolCarrier pc = new ProtocolCarrier(getActivity(), LAUNCHER);
			try {
				MessageResponse mres = pc.invokeSync(req, LAUNCHER,
						new AMSCAccessProvider().readToken(getActivity(),
								null));
				if (mres != null && mres.getBody() != null) {
					// Update from variable sec
					Calendar dateCal = Calendar.getInstance();
					dateCal.setTime(new Date());
					dateCal.add(Calendar.SECOND,
							getResources().getInteger(R.integer.check_interval));
					nextUpdate = dateCal.getTime().getTime();
					update = new UpdateModel(mres.getBody());
					settings.edit().putLong(UPDATE, nextUpdate).commit();
					for (int i = 0; i < packageNames.length; i++) {
						Integer version = update.getVersion(packageNames[i]);
						res[i] = version == null ? 0 : version;
						settings.edit()
								.putInt(packageNames[i] + "-version", version)
								.commit();
					}
					version = res;
				}
			} catch (Exception e) {
				Log.e(AppFragment.class.getName(),
						"Error reading update config: " + e.getMessage());
			}
		} else {
			toUpdate = false;
			for (int i = 0; i < packageNames.length; i++) {
				res[i] = settings.getInt(packageNames[i] + "-version", 0);
			}
			version = res;
		}

		return res;
	}

	// Task that retrieves applications info
	private class AppTask extends AsyncTask<Void, Void, List<AppItem>> {
		private DialogInterface.OnClickListener updateDialogClickListener;
		

		@Override
		protected void onPreExecute() {
			if (settings.getBoolean(KEY_UPDATE_REFRESH, false))
			{
			forced=true;
			SharedPreferences.Editor editor = settings.edit();
			editor.remove(KEY_UPDATE_REFRESH).commit();
			}
			if (((toUpdate) && (progress == null)) || forced)
				progress = ProgressDialog.show(getSherlockActivity(), "",
						"Checking applications version", true);

		};

		@Override
		protected List<AppItem> doInBackground(Void... params) {

			List<AppItem> items = new ArrayList<AppItem>();
			List<AppItem> notInstalledItems = new ArrayList<AppItem>();
			// Getting applications names, packages, ...
			String[] labels = getResources().getStringArray(R.array.app_labels);
			String[] packages = getResources().getStringArray(
					R.array.app_packages);
			String[] backgrounds = getResources().getStringArray(
					R.array.app_backgrounds);
			String url = getResources().getString(R.string.smartcampus_url_apk);
			int[] versions = getResources().getIntArray(R.array.app_version);
			String[] filenames = getResources().getStringArray(R.array.apk_filename);

			versions = readUpdateVersions(packages, versions);

			Drawable ic_update = getResources().getDrawable(
					R.drawable.ic_app_update);

			TypedArray icons = getResources().obtainTypedArray(
					R.array.app_icons);
			TypedArray grayIcons = getResources().obtainTypedArray(
					R.array.app_gray_icons);
			// They have to be the same length
			assert labels.length == packages.length
					&& labels.length == backgrounds.length
					&& labels.length == icons.length()
					&& labels.length == grayIcons.length();
			// Preparing all items
			for (int i = 0; i < labels.length; i++) {
				AppItem item = new AppItem();
				item.app = new SmartApp();

				item.app.fillApp(labels[i], packages[i], buildUrlDownloadApp(url,packages[i],versions[i],filenames[i]),
						icons.getDrawable(i), grayIcons.getDrawable(i),
						backgrounds[i], versions[i],filenames[i]);
				try {
					mInspector.isAppInstalled(item.app.appPackage);
					item.status = eu.trentorise.smartcampus.common.Status.OK;
					if (!mInspector.isAppUpdated(item.app.appPackage,
							versions[i]))
						item.status = eu.trentorise.smartcampus.common.Status.NOT_UPDATED;
				} catch (LauncherException e) {
					e.printStackTrace();
					// Getting status
					item.status = e.getStatus();
				}
				// Matching just retrieved status
				switch (item.status) {
				case OK:
					items.add(item);
					break;
				case NOT_UPDATED:
					// Installed but not updated
					items.add(item);
					// actually is the same of OK
					break;
				default:
					// Not installed list
					notInstalledItems.add(item);
					break;
				}
			}
			// Concatenation of not installed ones
			items.addAll(notInstalledItems);
			// Returning result
			return items;
		}

		private String buildUrlDownloadApp(String url, String packages, int versions,
				String filenames) {
			return  new String(url+packages+"/"+versions+"/"+filenames);
		}

		@Override
		protected void onPostExecute(List<AppItem> result) {
			super.onPostExecute(result);
			// se anche il launcher
			if (progress != null) {
				try {
					progress.cancel();
					progress = null;
				} catch (Exception e) {
					Log.w(getClass().getName(),
							"Problem closing progress dialog: "
									+ e.getMessage());
				}
			}
			int i = 0;
			for (AppItem app : result) {
				if (app.app.name.compareTo("ViviTrento") == 0)
					break;
				i++;
			}
			launcher = result.get(i);
			/*listener to open the dialog for the update*/

			if (launcher.status == eu.trentorise.smartcampus.common.Status.NOT_UPDATED)// e
																						// non
																						// e'
																						// nella
																						// blacklist;l
			{
				/*update menu button on*/
				to_be_updated=true;
				AppFragment.this.getSherlockActivity().invalidateOptionsMenu();
				/*create notification if it is a new version*/
				if (newversion(launcher)){
					shownotificationupdate();
				}
			}

			result.remove(i);
			AppFragment.this.getSherlockActivity().invalidateOptionsMenu();

			if (forced)
				forced = false;
		}

		private boolean newversion(AppItem launcher) {
			/*check if the version is new respect to the last checked*/
			SharedPreferences settings = getActivity()
					.getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings
					.edit();
			if (!settings.contains(launcher.app.name
					+ "-last"))
				{
				editor.putInt(launcher.app.name
						+ "-last", launcher.app.version);
				editor.commit();
				return false;
				}
			if (settings.getInt(launcher.app.name
					+ "-last",0)<launcher.app.version) {
				editor.putInt(launcher.app.name
						+ "-last", launcher.app.version);
				editor.commit();
				return true;
			}
			else return false;
		}

		private void shownotificationupdate() {

			Context context = getSherlockActivity();          
			NotificationManager manager = (NotificationManager)getSherlockActivity().getSystemService(getSherlockActivity().NOTIFICATION_SERVICE);
			Notification notification = new Notification( R.drawable.launcher, getString(R.string.update_application_notification), System.currentTimeMillis());  
			Intent notificationIntent = new Intent( context,  LauncherActivity.class); 
			notificationIntent.putExtra(PARAM_NAME, launcher.app.name);
			notificationIntent.putExtra(PARAM_URL, launcher.app.url);
			PendingIntent pendingIntent = PendingIntent.getActivity( context , 0, notificationIntent, 0);               
			notification.flags =  Intent.FLAG_ACTIVITY_CLEAR_TOP | Notification.FLAG_SHOW_LIGHTS ;
			notification.contentView = new RemoteViews(getSherlockActivity().getPackageName(), R.layout.update_notification);
			notification.contentIntent = pendingIntent;
			notification.contentView.setTextViewText(R.id.notification_title, getString(R.string.update_application_notification));
			manager.notify(1, notification);

		}

		

	}



	public void downloadApplication(String url, String name) {
		if (ConnectionUtil.isConnected(mConnectivityManager)) {
			// Checking url
			if (!TextUtils.isEmpty(url)) {
				if (mDownloaderTask != null
						&& !mDownloaderTask.isCancelled()) {
					mDownloaderTask.cancel(true);
				}
				mDownloaderTask = new ApkDownloaderTask(getActivity(), url);
				mDownloaderTask.execute();
			} else {
				Log.d(AppFragment.class.getName(),
						"Empty url for download: " + name);
				Toast.makeText(getActivity(), R.string.error_occurs,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getActivity(), R.string.enable_connection,
					Toast.LENGTH_SHORT).show();
			Intent intent = ConnectionUtil.getWifiSettingsIntent();
			startActivity(intent);
		}
	}
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getSherlockActivity().getSupportMenuInflater().inflate(R.menu.launchergripmenu,
				menu);
		
		MenuItem item = menu.getItem(0).setVisible(to_be_updated);
		SubMenu submenu = menu.getItem(1).getSubMenu();
		submenu.clear();
		submenu.setIcon(R.drawable.ic_action_overflow);

		submenu.add(Menu.CATEGORY_SYSTEM, R.id.check_updates, Menu.NONE,
				R.string.check_updates);// force the update check

		submenu.add(Menu.CATEGORY_SYSTEM, R.id.about, Menu.NONE, R.string.about);// about
																				// page
		SharedPreferences settings = getActivity()
				.getSharedPreferences(PREFS_NAME, 0);
		
		if (!settings.getBoolean(getString(R.string.registered_pref), false))
			{
			submenu.add(Menu.CATEGORY_SYSTEM, R.id.upgrade_user_menu, Menu.NONE, R.string.upgrade_user_menu);
			}
		
		}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.update_option_list:
			Fragment newFragment = new ManualUpdateFragment();
			Bundle args = new Bundle();
			args.putIntArray("versions", version);
			// Put any other arguments
			newFragment.setArguments(args);
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			transaction.addToBackStack(null);
			transaction.commit();
			return true;
		case R.id.check_updates:
			// force the updates
			forced = true;
			startNewAppTask();
			return true;
		case R.id.settings:
			startActivity(new Intent(getActivity(), SettingsActivity.class));
			return true;
		case R.id.about:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.smartcampus_url_credits)));
			startActivity(browserIntent);

			return true;
		case R.id.to_be_update:
			/*open the update dialog and download the application*/
			update_launcher(launcher.app.url,launcher.app.name);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}

	}

	private void update_launcher(final String app_url,final String app_name) {
		DialogInterface.OnClickListener updateDialogClickListener;

		updateDialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// If yes is pressed download the new app
					if (ConnectionUtil.isConnected(mConnectivityManager)) {
						// Checking url
						if (!TextUtils.isEmpty(app_url)) {
							if (mDownloaderTask != null
									&& !mDownloaderTask.isCancelled()) {
								mDownloaderTask.cancel(true);
							}
							mDownloaderTask = new ApkDownloaderTask(getActivity(), app_url);
							mDownloaderTask.execute();
						} else {
							Log.d(AppFragment.class.getName(),
									"Empty url for download: " + app_name);
							Toast.makeText(getActivity(), R.string.error_occurs,
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getActivity(), R.string.enable_connection,
								Toast.LENGTH_SHORT).show();
						Intent intent = ConnectionUtil.getWifiSettingsIntent();
						startActivity(intent);
					}
				

					break;

				case DialogInterface.BUTTON_NEGATIVE:

					break;
				}
			}
		};	
		SharedPreferences settings = getActivity()
				.getSharedPreferences(PREFS_NAME, 0);
		settings.toString();
	

			// update
			AlertDialog.Builder builder = new AlertDialog.Builder(
					getSherlockActivity());
			builder.setCancelable(false);
			builder.setMessage(
					getString(R.string.update_application_question))
					.setPositiveButton("Yes", updateDialogClickListener)
					.setNegativeButton("No", updateDialogClickListener)
					.show();


	}

	// Item wrapper of a smartApp
	class AppItem {
		SmartApp app;
		eu.trentorise.smartcampus.common.Status status = Status.NOT_FOUND;
	}

}

