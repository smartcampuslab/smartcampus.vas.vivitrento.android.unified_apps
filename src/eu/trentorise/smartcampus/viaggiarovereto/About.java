package eu.trentorise.smartcampus.viaggiarovereto;

import eu.trentorise.smartcampus.jp.BaseActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class About extends BaseActivity
{
	ImageButton close;
	Fragment fragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_about);
		getSupportActionBar().hide();
		close = (ImageButton) findViewById(R.id.close_credits);
		setBtClose();
	}
	
	
	private void setBtClose()
	{
		close.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				onBackPressed();
			}
		});
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
		overridePendingTransition(R.anim.alpha_in,
				R.anim.alpha_out);

	}
}
