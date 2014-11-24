package eu.trentorise.smartcampus.viaggiarovereto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import eu.trentorise.smartcampus.jp.BaseActivity;

public class About extends BaseActivity {
	ImageButton close;
	Fragment fragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_about);
		getSupportActionBar().hide();
		close = (ImageButton) findViewById(R.id.close_credits);
		setBtClose();
	}

	private void setBtClose() {
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
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
	
	public void onClickComuneRovereto(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("http://www.comune.rovereto.tn.it"));
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
	
}
