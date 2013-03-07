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

import android.accounts.AccountManager;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import eu.trentorise.smartcampus.dt.DiscoverTrentoActivity;
import eu.trentorise.smartcampus.jp.HomeActivity;
import eu.trentorise.smartcampus.vivitrento.apps.ApkInstaller.ApkDownloaderTask;
import eu.trentorise.smartcampus.vivitrento.util.ConnectionUtil;



public class LauncherActivity extends SherlockFragmentActivity {
	
	
	public static final String UPDATE = "update";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			initGlobalConstants();
			new AMSCAccessProvider().getAuthToken(this, null);
		} catch (OperationCanceledException e) {
			Toast.makeText(this, getString(R.string.token_required), Toast.LENGTH_LONG).show();
			finish();
		} catch (Exception e) {
			Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
			finish();
		}
		
    	// Getting saved instance
		if (savedInstanceState == null) {
			//			 Loading first fragment that works as home for application.
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment frag = new AppFragment();
			ft.add(R.id.fragment_container, frag).commit();
		}
	}

	private void initGlobalConstants() throws NameNotFoundException, NotFoundException {
		Constants.setAuthUrl(this, getResources().getString(R.string.smartcampus_auth_url));
		GlobalConfig.setAppUrl(this, getResources().getString(R.string.smartcampus_app_url));
	}
	
	//getting the notification intent update the launcher
	@Override
	public void onNewIntent(Intent arg0) {
		super.onNewIntent(arg0);
		Bundle extras = arg0.getExtras();
		ApkDownloaderTask mDownloaderTask=new ApkDownloaderTask(this, extras.getString("url"));

	    if(extras != null){
	        if (ConnectionUtil.isConnected( ConnectionUtil.getConnectivityManager(this))) {
				// Checking url
				if (!TextUtils.isEmpty(extras.getString("url"))) {
					if (mDownloaderTask != null
							&& !mDownloaderTask.isCancelled()) {
						mDownloaderTask.cancel(true);
					}
					mDownloaderTask = new ApkDownloaderTask(this, extras.getString(AppFragment.PARAM_URL));
					mDownloaderTask.execute();
				} else {
					Log.d(AppFragment.class.getName(),
							"Empty url for download: " + extras.getString(AppFragment.PARAM_NAME));
					Toast.makeText(this, R.string.error_occurs,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, R.string.enable_connection,
						Toast.LENGTH_SHORT).show();
				Intent intent = ConnectionUtil.getWifiSettingsIntent();
				startActivity(intent);
			}
	    }
		
	}
	
	public void launchDT(View v){
		startActivity(new Intent(this,HomeActivity.class));
	}
	public void launchJP(View v){
		startActivity(new Intent(this,DiscoverTrentoActivity.class));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment frag = new AppFragment();
		ft.add(R.id.fragment_container, frag).commit();

	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		  FragmentManager fragmentManager = getSupportFragmentManager();
		  try{
		  AppFragment appfragment =  (AppFragment) fragmentManager.findFragmentById(R.id.fragment_container);	
		  } catch (ClassCastException e){
			  
		  }
		  }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String token = data.getExtras().getString(AccountManager.KEY_AUTHTOKEN);
			if (token == null) {
				Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, getString(R.string.token_required), Toast.LENGTH_LONG).show();
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
