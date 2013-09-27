package eu.trentorise.smartcampus.viaggiatrento;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AboutFragment extends SherlockDialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dlg = super.onCreateDialog(savedInstanceState);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dlg;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,	Bundle args) {
		return inflater.inflate(R.layout.about, null);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getSherlockActivity().getSupportActionBar().show();
	}
	@Override
	public void onResume() {
		super.onResume();
		getSherlockActivity().getSupportActionBar().hide();
	}
}
