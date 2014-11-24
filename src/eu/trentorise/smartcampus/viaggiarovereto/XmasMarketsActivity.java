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

import it.sayservice.platform.smartplanner.data.message.RType;
import it.sayservice.platform.smartplanner.data.message.TType;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;

import eu.trentorise.smartcampus.jp.BaseActivity;
import eu.trentorise.smartcampus.jp.PlanJourneyActivity;
import eu.trentorise.smartcampus.jp.custom.UserPrefsHolder;
import eu.trentorise.smartcampus.jp.helper.XmasMarketsHelper;

public class XmasMarketsActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmasmarkets);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.xmasmarkets_actionbar_title);

		TextView par1 = (TextView) findViewById(R.id.xmasmarkets_tv_par1);
		par1.setText(Html.fromHtml(getString(R.string.xmasmarkets_par1)));
		TextView goToXmasMarkets = (TextView) findViewById(R.id.xmasmarkets_tv_link1);
		goToXmasMarkets.setText(Html.fromHtml(getString(R.string.xmasmarkets_link1)));
		TextView goToQuerciaParking = (TextView) findViewById(R.id.xmasmarkets_tv_link2);
		goToQuerciaParking.setText(Html.fromHtml(getString(R.string.xmasmarkets_link2)));
		TextView par2 = (TextView) findViewById(R.id.xmasmarkets_tv_par2);
		par2.setText(Html.fromHtml(getString(R.string.xmasmarkets_par2)));

		goToXmasMarkets.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "link 1",
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(), PlanJourneyActivity.class);
				intent.putExtra(getString(R.string.navigate_arg_to),
						XmasMarketsHelper.getXmasMarketAddress(getApplicationContext()));
				intent.putExtra(getString(R.string.userprefsholder), new UserPrefsHolder(null, RType.fastest, TType.CAR));
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});

		goToQuerciaParking.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "link 2",
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(), PlanJourneyActivity.class);
				intent.putExtra(getString(R.string.navigate_arg_to),
						XmasMarketsHelper.getXmasMarketParkingAddress(getApplicationContext()));
				intent.putExtra(getString(R.string.userprefsholder), new UserPrefsHolder(null, RType.fastest, TType.TRANSIT));
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
}
