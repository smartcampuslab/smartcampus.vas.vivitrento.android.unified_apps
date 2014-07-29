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

import java.io.IOException;

import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import eu.trentorise.smartcampus.ac.Constants;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.android.common.GlobalConfig;
import eu.trentorise.smartcampus.android.feedback.utils.FeedbackFragmentInflater;
import eu.trentorise.smartcampus.common.ViviTrentoHelper;
import eu.trentorise.smartcampus.jp.Config;
import eu.trentorise.smartcampus.jp.MonitorJourneyActivity;
import eu.trentorise.smartcampus.jp.PlanJourneyActivity;
import eu.trentorise.smartcampus.jp.ProfileActivity;
import eu.trentorise.smartcampus.jp.SavedJourneyActivity;
import eu.trentorise.smartcampus.jp.SmartCheckActivity;
import eu.trentorise.smartcampus.jp.TutorialManagerActivity;
import eu.trentorise.smartcampus.jp.helper.JPHelper;
import eu.trentorise.smartcampus.jp.notifications.BroadcastNotificationsActivity;
import eu.trentorise.smartcampus.jp.notifications.NotificationsFragmentActivityJP;
import eu.trentorise.smartcampus.viaggiarovereto.apps.ApkInstaller.ApkDownloaderTask;
import eu.trentorise.smartcampus.viaggiarovereto.util.ConnectionUtil;

public class LauncherActivity extends TutorialManagerActivity {

	public static final String UPDATE = "update";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		JPHelper.init(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		try {
			ViviTrentoHelper.init(getApplicationContext());
			initGlobalConstants();
			final AMSCAccessProvider accessprovider = new AMSCAccessProvider();
			ensureToken(accessprovider);
		}

		catch (Exception e) {
			Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
			finish();
		}

		// Feedback
		FeedbackFragmentInflater.inflateHandleButtonInRelativeLayout(this,
				(RelativeLayout) findViewById(R.id.home_relative_layout_jp));

	}

	/**
	 * @param accessprovider
	 * @throws OperationCanceledException
	 * @throws AuthenticatorException
	 * @throws IOException
	 */
	private void ensureToken(final AMSCAccessProvider accessprovider)
			throws OperationCanceledException, AuthenticatorException,
			IOException {
		//
		if (accessprovider.readToken(this, null) == null) {
			if (accessprovider.readUserData(this, null) == null) {
				showLoginDialog(accessprovider);
			} else {
//				accessprovider.getAuthToken(LauncherActivity.this, accessprovider.isUserAnonymous(this) ? "anonymous" : null);
			}
		} else {
			if (JPHelper.isFirstLaunch(this)) {
				showTourDialog();
				JPHelper.disableFirstLaunch(this);
			}
		}
	}

	/**
	 * @param accessprovider
	 */
	private void showLoginDialog(final AMSCAccessProvider accessprovider) {
		// dialogbox for registration
		DialogInterface.OnClickListener updateDialogClickListener;

		updateDialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				try {
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
		settings.edit().putBoolean(AppFragment.FIRSTTIME, true).commit();

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

	@Override
	protected void onResume() {
		super.onResume();
		if (JPHelper.isInitialized()) {
			JPHelper.getLocationHelper().start();
		}
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		Fragment frag = new AppFragment();
//		ft.add(R.id.fragment_container, frag).commit();
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
		if (requestCode == SCAccessProvider.SC_AUTH_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String token = data.getExtras().getString(AccountManager.KEY_AUTHTOKEN);
				if (token == null) {
					Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
					// clean shared preferences
				} else {
					invalidateOptionsMenu();
					if (JPHelper.isFirstLaunch(this)) {
						showTourDialog();
						JPHelper.disableFirstLaunch(this);
					}
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
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle arg0) {
		super.onSaveInstanceState(arg0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		} else if (item.getItemId() == R.id.upgrade_user_menu) {
			// promote user
			AMSCAccessProvider ac = new AMSCAccessProvider();
			ac.promote(this, null, ac.readToken(this, null));
		} else if (item.getItemId() == R.id.about) {
			 FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
             Fragment fragment = new AboutFragment();
             fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
             fragmentTransaction.replace(Config.mainlayout,fragment, "about");
             fragmentTransaction.addToBackStack(fragment.getTag());
             fragmentTransaction.commit();
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getSupportMenuInflater().inflate(R.menu.launchergripmenu, menu);
		menu.getItem(0).setVisible(false);
		SubMenu submenu = menu.getItem(1).getSubMenu();
		submenu.clear();
		submenu.setIcon(R.drawable.ic_action_overflow);

		submenu.add(Menu.CATEGORY_SYSTEM, R.id.menu_item_tutorial, Menu.NONE, R.string.menu_tutorial);
		submenu.add(Menu.CATEGORY_SYSTEM, R.id.about, Menu.NONE, R.string.about);// about
																					// page

		if (new AMSCAccessProvider().isUserAnonymous(this)) {
			submenu.add(Menu.CATEGORY_SYSTEM, R.id.upgrade_user_menu, Menu.NONE, R.string.upgrade_user_menu);
		}

		return super.onPrepareOptionsMenu(menu);
	}
} 