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
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

import eu.trentorise.smartcampus.ac.Constants;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.android.common.GlobalConfig;
import eu.trentorise.smartcampus.jp.BaseActivity;
import eu.trentorise.smartcampus.jp.Config;
import eu.trentorise.smartcampus.jp.PlanNewJourneyFragment;
import eu.trentorise.smartcampus.jp.helper.JPHelper;

public class PlanJourneyActivity extends BaseActivity {
	

	@Override
	protected void onResume() {
		super.onResume();
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.title_plan_journey);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	private void ensureToken(final AMSCAccessProvider accessprovider) throws OperationCanceledException,
			AuthenticatorException, IOException {
		//
		if (accessprovider.readToken(this, null) == null) {
			if (accessprovider.readUserData(this, null) == null) {
				showLoginDialog(accessprovider);
			} else {
				accessprovider.getAuthToken(PlanJourneyActivity.this,
						accessprovider.isUserAnonymous(this) ? "anonymous" : null);
			}
		}
	}

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

						accessprovider.getAuthToken(PlanJourneyActivity.this, null);
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						// no -> accessprovider.getAuthToken(this,
						// "anonymous"); -> shared preferences
						// "registred" true

						accessprovider.getAuthToken(PlanJourneyActivity.this, "anonymous");

						break;
					}
					invalidateOptionsMenu();

				} catch (OperationCanceledException e) {
					Toast.makeText(PlanJourneyActivity.this, getString(R.string.token_required), Toast.LENGTH_LONG)
							.show();
					finish();
				} catch (Exception e) {
					Toast.makeText(PlanJourneyActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT)
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		JPHelper.init(getApplicationContext());

		super.onCreate(savedInstanceState);
		try {

			setContentView(R.layout.empty_layout_jp);

			// getSupportActionBar().setDisplayShowTitleEnabled(false);
			// getSupportActionBar().removeAllTabs();
			setContentView(R.layout.home);
			// ViviTrentoHelper.init(getApplicationContext());
			initGlobalConstants();

			final AMSCAccessProvider accessprovider = new AMSCAccessProvider();
			ensureToken(accessprovider);
		}

		catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
			finish();
		}
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = new PlanNewJourneyFragment();
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.replace(Config.mainlayout, fragment);
		fragmentTransaction.commit();

		// // New journey
		// ActionBar.Tab tab =
		// getSupportActionBar().newTab().setText(R.string.tab_myoneoffjourneys);
		// tab.setTabListener(new TabListener<PlanNewJourneyFragment>(this,
		// Config.PLAN_NEW_FRAGMENT_TAG,
		// PlanNewJourneyFragment.class, Config.mainlayout));
		// getSupportActionBar().addTab(tab);

		// // new recur journeys
		// tab =
		// getSupportActionBar().newTab().setText(R.string.tab_myjourneys);
		// tab.setTabListener(new TabListener<MyItinerariesFragment>(this,
		// Config.MY_JOURNEYS_FRAGMENT_TAG,
		// MyItinerariesFragment.class, Config.mainlayout));
		// getSupportActionBar().addTab(tab);

		// tab =
		// getSupportActionBar().newTab().setText(R.string.tab_myrecjourneys);
		// tab.setTabListener(new TabListener<PlanRecurJourneyFragment>(this,
		// Config.PLAN_NEW_RECUR_FRAGMENT_TAG,
		// PlanRecurJourneyFragment.class, Config.mainlayout));
		// getSupportActionBar().addTab(tab);
		//
		// if (getSupportActionBar().getNavigationMode() !=
		// ActionBar.NAVIGATION_MODE_TABS) {
		// getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// }
	}

	private void initGlobalConstants() throws NameNotFoundException, NotFoundException {
		Constants.setAuthUrl(this, getResources().getString(R.string.smartcampus_auth_url));
		GlobalConfig.setAppUrl(this, getResources().getString(R.string.smartcampus_app_url));
		SharedPreferences settings = PlanJourneyActivity.this.getSharedPreferences(AppFragment.PREFS_NAME, 0);
		settings.edit().putBoolean(AppFragment.FIRSTTIME, true).commit();

	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == SCAccessProvider.SC_AUTH_ACTIVITY_REQUEST_CODE) {
//			if (resultCode == RESULT_OK) {
//				String token = data.getExtras().getString(AccountManager.KEY_AUTHTOKEN);
//				if (token == null) {
//					Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
//					// clean shared preferences
//				} else {
//					//do the request
//					FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//					SherlockFragment fragment = new PlanNewJourneyFragment();
//					fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//					fragmentTransaction.replace(Config.mainlayout, fragment);
//					fragmentTransaction.commit();
//				}
//
//			} else if (resultCode == RESULT_CANCELED) {
//				Toast.makeText(this, getString(R.string.token_required), Toast.LENGTH_LONG).show();
//				// clean shared preferences
//				finish();
//
//			} else {
//				Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
//				// clean shared preferences
//				finish();
//			}
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
}
