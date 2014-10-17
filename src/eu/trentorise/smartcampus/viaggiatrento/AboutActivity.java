package eu.trentorise.smartcampus.viaggiatrento;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.overridePendingTransition(android.R.anim.fade_in, R.anim.push_down);
	}

}
