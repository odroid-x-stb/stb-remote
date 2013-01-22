package fr.enseirb.odroidx.remote_client.UI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ScanningDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setTitle("Scanning...");
		dialog.setIndeterminate(true);
		return dialog;
	}
}
