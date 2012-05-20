package com.fh.voting.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class Utils {
	static public void alertDialog(Context context, String message, View focus) {
		AlertDialog ad = new AlertDialog.Builder(context).create();
		final View toFocus = focus;
		ad.setCancelable(false); // This blocks the 'BACK' button
		ad.setMessage(message);
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (toFocus != null) {
					toFocus.requestFocus();
				}
			}
		});
		ad.show();
	}

	static public void alertErrorDialog(Context context, String message) {
		AlertDialog ad = new AlertDialog.Builder(context).create();
		ad.setCancelable(false); // This blocks the 'BACK' button
		ad.setMessage(message);
		ad.setIcon(android.R.drawable.ic_dialog_alert);
		ad.setButton("Dismiss", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		ad.show();
	}
}
