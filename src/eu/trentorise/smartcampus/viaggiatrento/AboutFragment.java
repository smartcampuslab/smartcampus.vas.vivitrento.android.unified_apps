package eu.trentorise.smartcampus.viaggiatrento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;

import eu.trentorise.smartcampus.viaggiatrento.R;

public class AboutFragment extends SherlockFragment {

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,	Bundle args) {
		View v = inflater.inflate(R.layout.about, null);
		// Getting UI references
		return v;
	}
}
