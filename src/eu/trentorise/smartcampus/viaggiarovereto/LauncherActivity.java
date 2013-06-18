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
package eu.trentorise.smartcampus.viaggiarovereto;

import android.accounts.AccountManager;
import android.accounts.OperationCanceledException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import eu.trentorise.smartcampus.ac.Constants;
import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.android.common.GlobalConfig;
import eu.trentorise.smartcampus.common.ViviTrentoHelper;
import eu.trentorise.smartcampus.dt.DiscoverTrentoActivity;
import eu.trentorise.smartcampus.jp.BaseActivity;
import eu.trentorise.smartcampus.jp.HomeActivity;
import eu.trentorise.smartcampus.jp.MonitorJourneyActivity;
import eu.trentorise.smartcampus.jp.PlanJourneyActivity;
import eu.trentorise.smartcampus.jp.ProfileActivity;
import eu.trentorise.smartcampus.jp.SavedJourneyActivity;
import eu.trentorise.smartcampus.jp.SmartCheckActivity;
import eu.trentorise.smartcampus.jp.helper.JPHelper;
import eu.trentorise.smartcampus.jp.helper.JPParamsHelper;
import eu.trentorise.smartcampus.jp.notifications.BroadcastNotificationsActivity;
import eu.trentorise.smartcampus.jp.notifications.NotificationsFragmentActivityJP;
import eu.trentorise.smartcampus.viaggiarovereto.apps.ApkInstaller.ApkDownloaderTask;
import eu.trentorise.smartcampus.viaggiarovereto.util.ConnectionUtil;

//public class LauncherActivity extends SherlockFragmentActivity {
public class LauncherActivity extends BaseActivity {

	public static final String UPDATE = "update";
	private boolean token_present = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		JPHelper.init(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			ViviTrentoHelper.init(getApplicationContext());

			final AMSCAccessProvider accessprovider = new AMSCAccessProvider();
			initGlobalConstants();
			//
			if (accessprovider.readToken(this, null) == null) {
				token_present = false;
				// dialogbox for registration
				DialogInterface.OnClickListener updateDialogClickListener;

				updateDialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						try {
							SharedPreferences settings = LauncherActivity.this.getSharedPreferences(
									AppFragment.PREFS_NAME, 0);
							SharedPreferences.Editor editor = settings.edit();
							switch (which) {
							case DialogInterface.BUTTON_POSITIVE:

								// yes -> accessprovider.getAuthToken(this,
								// null);-> shared preferences "registred" true

								accessprovider.getAuthToken(LauncherActivity.this, null);
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								// no -> accessprovider.getAuthToken(this,
								// "anonymous"); -> shared preferences
								// "registred" true

								accessprovider.getAuthToken(LauncherActivity.this, "anonymous");

								break;
							}
							editor.commit();
							appFragmentCheckVersion();
							invalidateOptionsMenu();

						} catch (OperationCanceledException e) {
							Toast.makeText(LauncherActivity.this, getString(R.string.token_required), Toast.LENGTH_LONG)
									.show();
							finish();
						} catch (Exception e) {
							Toast.makeText(LauncherActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT)
									.show();
							finish();
						}
					}
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setCancelable(false);
				builder.setMessage(getString(R.string.auth_required))
						.setPositiveButton(android.R.string.yes, updateDialogClickListener)
						.setNegativeButton(R.string.not_now, updateDialogClickListener).show();
			} else {
				token_present = true;
			}

		}

		catch (Exception e) {
			Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
			finish();
		}

		// Getting saved instance
		if (savedInstanceState == null) {
			// Loading first fragment that works as home for application.
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment frag = new AppFragment();
			ft.add(R.id.fragment_container, frag).commit();

		}
	}

	public void goToFunctionality(View view) {
		Intent intent;
		int viewId = view.getId();

		if (viewId == R.id.btn_planjourney) {
			intent = new Intent(this, PlanJourneyActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return;
		}   else if (viewId == R.id.btn_monitorrecurrentjourney) {
			intent = new Intent(this, MonitorJourneyActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return;
		} else if (viewId == R.id.btn_broadcast) {
			intent = new Intent(this, BroadcastNotificationsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return;
		} else if (viewId == R.id.btn_myprofile) {
			intent = new Intent(this, ProfileActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return;
		} else if (viewId == R.id.btn_monitorsavedjourney) {
			intent = new Intent(this, SavedJourneyActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return;
		} else if (viewId == R.id.btn_smart) {
			intent = new Intent(this, SmartCheckActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return;
		} else if (viewId == R.id.btn_notifications) {
			intent = new Intent(this, NotificationsFragmentActivityJP.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return;
		} else {
			Toast toast = Toast.makeText(this, R.string.tmp, Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
	}

	private void initGlobalConstants() throws NameNotFoundException, NotFoundException {
		Constants.setAuthUrl(this, getResources().getString(R.string.smartcampus_auth_url));
		GlobalConfig.setAppUrl(this, getResources().getString(R.string.smartcampus_app_url));
		SharedPreferences settings = LauncherActivity.this.getSharedPreferences(AppFragment.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		settings.edit().putBoolean(AppFragment.FIRSTTIME, true).commit();

	}

	private void appFragmentCheckVersion() {
		AppFragment appfragment = (AppFragment) LauncherActivity.this.getSupportFragmentManager().findFragmentById(
				R.id.fragment_container);
		appfragment.check_version();
	}

	// getting the notification intent update the launcher
	@Override
	public void onNewIntent(Intent arg0) {
		super.onNewIntent(arg0);
		Bundle extras = arg0.getExtras();

		if (extras != null) {
			ApkDownloaderTask mDownloaderTask = new ApkDownloaderTask(this, extras.getString("url"));

			if (ConnectionUtil.isConnected(ConnectionUtil.getConnectivityManager(this))) {
				// Checking url
				if (!TextUtils.isEmpty(extras.getString("url"))) {
					if (mDownloaderTask != null && !mDownloaderTask.isCancelled()) {
						mDownloaderTask.cancel(true);
					}
					mDownloaderTask = new ApkDownloaderTask(this, extras.getString(AppFragment.PARAM_URL));
					mDownloaderTask.execute();
				} else {
					Log.d(AppFragment.class.getName(),
							"Empty url for download: " + extras.getString(AppFragment.PARAM_NAME));
					Toast.makeText(this, R.string.error_occurs, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, R.string.enable_connection, Toast.LENGTH_SHORT).show();
				Intent intent = ConnectionUtil.getWifiSettingsIntent();
				startActivity(intent);
			}
		}

	}

	public void launchDT(View v) {
		startActivity(new Intent(this, HomeActivity.class));
	}

	public void launchJP(View v) {
		startActivity(new Intent(this, DiscoverTrentoActivity.class));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (JPHelper.isInitialized()) {
			JPHelper.getLocationHelper().start();
		}
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment frag = new AppFragment();
		ft.add(R.id.fragment_container, frag).commit();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (JPHelper.isInitialized()) {
			JPHelper.getLocationHelper().stop();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String token = data.getExtras().getString(AccountManager.KEY_AUTHTOKEN);
			if (token == null) {
				Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
				// clean shared preferences
			} else {
				appFragmentCheckVersion();
				invalidateOptionsMenu();
			}

		} else if (resultCode == RESULT_CANCELED) {
			Toast.makeText(this, getString(R.string.token_required), Toast.LENGTH_LONG).show();
			// clean shared preferences
			finish();

		} else {
			Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
			// clean shared preferences
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.emptymenu, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle arg0) {
		super.onSaveInstanceState(arg0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

}
// public class LauncherActivity extends FeedbackFragmentActivity {
//
// public static final String UPDATE = "update";
// private boolean token_present = false;
// private boolean mHiddenNotification;
//
//
//
// private void setHiddenNotification() {
// try {
// ApplicationInfo ai =
// getPackageManager().getApplicationInfo(this.getPackageName(),
// PackageManager.GET_META_DATA);
// Bundle aBundle = ai.metaData;
// mHiddenNotification = aBundle.getBoolean("hidden-notification");
// } catch (NameNotFoundException e) {
// mHiddenNotification = false;
// Log.e(HomeActivity.class.getName(),
// "you should set the hidden-notification metadata in app manifest");
// }
// if (mHiddenNotification) {
// View notificationButton = findViewById(R.id.btn_notifications);
// if (notificationButton != null)
// notificationButton.setVisibility(View.GONE);
// }
// }
//
// @Override
// protected void onStart() {
// super.onStart();
//
// getSupportActionBar().setHomeButtonEnabled(false);
// getSupportActionBar().setDisplayHomeAsUpEnabled(false);
// getSupportActionBar().setDisplayShowTitleEnabled(true);
//
// if (getSupportActionBar().getNavigationMode() !=
// ActionBar.NAVIGATION_MODE_STANDARD) {
// getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
// }
// }
//
//
// @Override
// public boolean onOptionsItemSelected(MenuItem item) {
// // Handle item selection
// switch (item.getItemId()) {
// case android.R.id.home:
// onBackPressed();
// }
// return super.onOptionsItemSelected(item);
// }
// @Override
// protected void onResume() {
// super.onResume();
//
// JPHelper.init(this);
// JPHelper.getLocationHelper().start();
// }
//
// @Override
// protected void onPause() {
// super.onPause();
// JPHelper.getLocationHelper().stop();
// }
//
// @Override
// public boolean onCreateOptionsMenu(Menu menu) {
// menu.clear();
// MenuInflater inflater = getSupportMenuInflater();
// inflater.inflate(R.menu.emptymenu, menu);
// return true;
// }
//
// public void goToFunctionality(View view) {
// Intent intent;
// int viewId = view.getId();
//
// if (viewId == R.id.btn_planjourney) {
// intent = new Intent(this, PlanJourneyActivity.class);
// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
// startActivity(intent);
// return;
// } else if (viewId == R.id.btn_broadcast) {
// intent = new Intent(this, BroadcastNotificationsActivity.class);
// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
// startActivity(intent);
// return;
// } else if (viewId == R.id.btn_myprofile) {
// intent = new Intent(this, ProfileActivity.class);
// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
// startActivity(intent);
// return;
// } else if (viewId == R.id.btn_monitorsavedjourney) {
// intent = new Intent(this, SavedJourneyActivity.class);
// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
// startActivity(intent);
// return;
// } else if (viewId == R.id.btn_smart) {
// intent = new Intent(this, SmartCheckActivity.class);
// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
// startActivity(intent);
// return;
// } else if (viewId == R.id.btn_notifications) {
// intent = new Intent(this, NotificationsFragmentActivityJP.class);
// intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
// startActivity(intent);
// return;
// } else {
// Toast toast = Toast.makeText(getApplicationContext(), R.string.tmp,
// Toast.LENGTH_SHORT);
// toast.show();
// return;
// }
// }
//
// public int getMainlayout() {
// return Config.mainlayout;
// }
// @Override
// public void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// setContentView(R.layout.home);
// if (getSupportActionBar().getNavigationMode() !=
// ActionBar.NAVIGATION_MODE_STANDARD)
// getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
// FeedbackFragmentInflater.inflateHandleButtonInRelativeLayout(this,
// (RelativeLayout) findViewById(R.id.home_relative_layout_jp));
// setHiddenNotification();
// try {
// ViviTrentoHelper.init(getApplicationContext());
//
// final AMSCAccessProvider accessprovider = new AMSCAccessProvider();
// initGlobalConstants();
// //
// if (accessprovider.readToken(this, null) == null) {
// token_present = false;
// // dialogbox for registration
// DialogInterface.OnClickListener updateDialogClickListener;
//
// updateDialogClickListener = new DialogInterface.OnClickListener() {
// @Override
// public void onClick(DialogInterface dialog, int which) {
//
// try {
// SharedPreferences settings =
// LauncherActivity.this.getSharedPreferences(AppFragment.PREFS_NAME, 0);
// SharedPreferences.Editor editor = settings.edit();
// switch (which) {
// case DialogInterface.BUTTON_POSITIVE:
//
// // yes -> accessprovider.getAuthToken(this,
// // null);-> shared preferences "registred" true
//
// accessprovider.getAuthToken(LauncherActivity.this, null);
// break;
//
// case DialogInterface.BUTTON_NEGATIVE:
// // no -> accessprovider.getAuthToken(this,
// // "anonymous"); -> shared preferences
// // "registred" true
//
// accessprovider.getAuthToken(LauncherActivity.this, "anonymous");
//
// break;
// }
// editor.commit();
// appFragmentCheckVersion();
// invalidateOptionsMenu();
//
// } catch (OperationCanceledException e) {
// Toast.makeText(LauncherActivity.this, getString(R.string.token_required),
// Toast.LENGTH_LONG).show();
// finish();
// } catch (Exception e) {
// Toast.makeText(LauncherActivity.this, getString(R.string.auth_failed),
// Toast.LENGTH_SHORT).show();
// finish();
// }
// }
// };
// AlertDialog.Builder builder = new AlertDialog.Builder(this);
// builder.setCancelable(false);
// builder.setMessage(getString(R.string.auth_required))
// .setPositiveButton(android.R.string.yes, updateDialogClickListener)
// .setNegativeButton(R.string.not_now, updateDialogClickListener).show();
// } else {
// token_present = true;
// }
//
// }
//
// catch (Exception e) {
// Toast.makeText(this, getString(R.string.auth_failed),
// Toast.LENGTH_SHORT).show();
// finish();
// }
//
// // // Getting saved instance
// // if (savedInstanceState == null) {
// // // Loading first fragment that works as home for application.
// // FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// // Fragment frag = new AppFragment();
// // ft.add(R.id.fragment_container, frag).commit();
// //
// // }
// }
// //
// private void initGlobalConstants() throws NameNotFoundException,
// NotFoundException {
// Constants.setAuthUrl(this,
// getResources().getString(R.string.smartcampus_auth_url));
// GlobalConfig.setAppUrl(this,
// getResources().getString(R.string.smartcampus_app_url));
// }
//
// private void appFragmentCheckVersion() {
// // AppFragment appfragment = (AppFragment)
// LauncherActivity.this.getSupportFragmentManager().findFragmentById(
// // R.id.fragment_container);
// // appfragment.check_version();
// }
//
// // getting the notification intent update the launcher
// @Override
// public void onNewIntent(Intent arg0) {
// super.onNewIntent(arg0);
// Bundle extras = arg0.getExtras();
//
// if (extras != null) {
// ApkDownloaderTask mDownloaderTask = new ApkDownloaderTask(this,
// extras.getString("url"));
//
// if (ConnectionUtil.isConnected(ConnectionUtil.getConnectivityManager(this)))
// {
// // Checking url
// if (!TextUtils.isEmpty(extras.getString("url"))) {
// if (mDownloaderTask != null && !mDownloaderTask.isCancelled()) {
// mDownloaderTask.cancel(true);
// }
// mDownloaderTask = new ApkDownloaderTask(this,
// extras.getString(AppFragment.PARAM_URL));
// mDownloaderTask.execute();
// } else {
// Log.d(AppFragment.class.getName(), "Empty url for download: " +
// extras.getString(AppFragment.PARAM_NAME));
// Toast.makeText(this, R.string.error_occurs, Toast.LENGTH_SHORT).show();
// }
// } else {
// Toast.makeText(this, R.string.enable_connection, Toast.LENGTH_SHORT).show();
// Intent intent = ConnectionUtil.getWifiSettingsIntent();
// startActivity(intent);
// }
// }
//
// }
//
//
//
//
// @Override
// public void onConfigurationChanged(Configuration newConfig) {
// super.onConfigurationChanged(newConfig);
//
// }
//
// @Override
// protected void onActivityResult(int requestCode, int resultCode, Intent data)
// {
// if (resultCode == RESULT_OK) {
// String token = data.getExtras().getString(AccountManager.KEY_AUTHTOKEN);
// if (token == null) {
// Toast.makeText(this, getString(R.string.auth_failed),
// Toast.LENGTH_SHORT).show();
// // clean shared preferences
// } else {
// appFragmentCheckVersion();
// invalidateOptionsMenu();
// }
//
// } else if (resultCode == RESULT_CANCELED ) {
// Toast.makeText(this, getString(R.string.token_required),
// Toast.LENGTH_LONG).show();
// // clean shared preferences
// finish();
//
// }else {
// Toast.makeText(this, getString(R.string.auth_failed),
// Toast.LENGTH_LONG).show();
// // clean shared preferences
// finish();
// }
// super.onActivityResult(requestCode, resultCode, data);
// }
//
//
//
// @Override
// protected void onSaveInstanceState(Bundle arg0) {
// super.onSaveInstanceState(arg0);
// }
//
//
//
// @Override
// public String getAppToken() {
// // TODO Auto-generated method stub
// return null;
// }
//
// @Override
// public String getAuthToken() {
// // TODO Auto-generated method stub
// return null;
// }
//
// }
