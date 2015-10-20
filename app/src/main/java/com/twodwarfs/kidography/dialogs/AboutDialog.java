
package com.twodwarfs.kidography.dialogs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.R;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class AboutDialog extends BaseDialog {

	private Button mOkButton;
	private Context mContext;

	public AboutDialog(Context context) {
		super(context, R.style.BorderlessDialogStyle, R.layout.about_dialog_layout);

		mOkButton = (Button) findViewById(R.id.button_ok);

		setCancelable(true);
		mContext = context;

		initTypefaces();
	}

	private void initTypefaces() {
		mOkButton.setTypeface(App.happyHellTypeface);

		View root = findViewById(R.id.container_about_dialog);
		overrideFonts(root);
	}

	public void setOnClickListener(View.OnClickListener onClickListener) {
		mOkButton.setOnClickListener(onClickListener);
	}

	private void overrideFonts(final View v) {
		try {
			if (v instanceof ViewGroup) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0; i < vg.getChildCount(); i++) {
					View child = vg.getChildAt(i);
					overrideFonts(child);
				}
			} else if (v instanceof TextView ) {
				((TextView) v).setTypeface(App.robotoTypeface);
			}
		} catch (Exception e) {
		}
	}

}
