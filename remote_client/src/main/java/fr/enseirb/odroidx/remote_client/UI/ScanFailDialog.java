package fr.enseirb.odroidx.remote_client.UI;

import fr.enseirb.odroidx.remote_client.MainActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ScanFailDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("STB not found");
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getActivity().finish();
			}
		});
		builder.setPositiveButton("Retry", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((MainActivity) getActivity()).lauchScan();
			}
		});
		return builder.create();
	}
}
