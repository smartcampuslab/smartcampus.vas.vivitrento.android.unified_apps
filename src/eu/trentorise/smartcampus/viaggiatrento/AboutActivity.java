package eu.trentorise.smartcampus.viaggiatrento;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class AboutActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
		setContentView(R.layout.about);
		getSupportActionBar().hide();

		ImageButton closeCreditsBtn = (ImageButton) findViewById(R.id.close_credits);
		closeCreditsBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			TextView creditsVersion = (TextView) findViewById(R.id.credits_version);
			creditsVersion.setText(info.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		this.overridePendingTransition(android.R.anim.fade_in, R.anim.push_down);
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
	}

	public void onClickFBK(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://www.fbk.eu"));
		startActivity(i);
	}

	public void onClickSC(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://www.smartcampuslab.it"));
		startActivity(i);
	}

	public void onClickComune(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://www.comune.trento.it"));
		startActivity(i);
	}

	public void onClickIEC(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://www.iescities.eu/"));
		startActivity(i);
	}

	public void onClickStreetlife(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://www.streetlife-project.eu/index.html"));
		startActivity(i);
	}

	public void onClickSwAbout(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://www.smartcampuslab.it/swabout"));
		startActivity(i);
	}

	public void onClickCCT(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://http://www.comunitrentini.it/"));
		startActivity(i);
	}

}
