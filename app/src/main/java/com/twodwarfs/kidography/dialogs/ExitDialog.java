
package com.twodwarfs.kidography.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.R;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class ExitDialog extends BaseDialog {

	private Button mYesButton;
	private Button mNoButton;

	public ExitDialog(Context context) {
		super(context, R.style.BorderlessDialogStyle, R.layout.exit_dialog_layout);

		mYesButton = (Button) findViewById(R.id.button_yes);
		mNoButton = (Button) findViewById(R.id.button_no);

		setCancelable(false);

		initTypefaces();
	}

	private void initTypefaces() {
		mYesButton.setTypeface(App.happyHellTypeface);
		mNoButton.setTypeface(App.happyHellTypeface);
	}

	public void setOnClickListener(View.OnClickListener onClickListener) {
		mYesButton.setOnClickListener(onClickListener);
		mNoButton.setOnClickListener(onClickListener);
	}

}
