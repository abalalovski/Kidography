
package com.twodwarfs.kidography.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.R;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class PauseDialog extends BaseDialog {

	private Button mResumeButton;

	public PauseDialog(Context context) {
		super(context, R.style.BorderlessDialogStyle, R.layout.pause_dialog_layout);

		mResumeButton = (Button) findViewById(R.id.button_resume);

		setCancelable(true);

		initTypefaces();
	}

	private void initTypefaces() {
		mResumeButton.setTypeface(App.happyHellTypeface);
	}

	public void setOnClickListener(View.OnClickListener onClickListener) {
		mResumeButton.setOnClickListener(onClickListener);
	}

}
