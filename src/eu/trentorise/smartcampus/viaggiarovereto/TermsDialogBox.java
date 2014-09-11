package eu.trentorise.smartcampus.viaggiarovereto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import eu.trentorise.smartcampus.common.ViviTrentoHelper;

public class TermsDialogBox extends DialogFragment {

	static TermsDialogBox newInstance() {
		TermsDialogBox f = new TermsDialogBox();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.terms_dialog_box, container, false);
		getDialog().setTitle("Terms of service");
		WebView mWebView = (WebView) v.findViewById(R.id.textview_terms_of_service);    
		String text = "<html><body>"
				                   + "<p align=\"justify\">"                
				                   + getString(R.string.terms_of_service) 
				                   + "</p> "
				                   + "</body></html>";
				           
				           mWebView.loadData(text, "text/html", "utf-8");
		Button button_ok = (Button) v.findViewById(R.id.button_accept);
		button_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
				getActivity().finish();
				Intent i = new Intent(getActivity(), LauncherActivity.class);
				startActivity(i);
				ViviTrentoHelper.setShowedTermsDialog(getActivity().getSharedPreferences(ViviTrentoHelper.T_D_PREFS, Context.MODE_PRIVATE));
				
			}
		});

		// Watch for button clicks.
		Button button_cancel = (Button) v.findViewById(R.id.button_not_accept);
		button_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		return v;
	}
}
