package eu.trentorise.smartcampus.viaggiarovereto;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AboutFragment extends SherlockDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dlg = super.onCreateDialog(savedInstanceState);
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
            return dlg;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle args) {
            View v = inflater.inflate(R.layout.about, null);
            ((TextView)v.findViewById(R.id.tv_about_sc)).setMovementMethod(LinkMovementMethod.getInstance());
            ((TextView)v.findViewById(R.id.tv_about_cdc)).setMovementMethod(LinkMovementMethod.getInstance());
            return v;
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