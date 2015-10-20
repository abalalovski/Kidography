
package com.twodwarfs.kidography.dialogs;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.helpers.Constants;
import com.twodwarfs.kidography.utils.SettingsManager;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class NameDialog extends BaseDialog {
	
	private EditText mNameEditText;
	private Button mOkButton;
	private Context mContext;
	
	public NameDialog(Context context) {
		super(context, R.style.BorderlessDialogStyle, R.layout.name_dialog_layout);
		
		mContext = context;
		
		initViews();
		initListeners();
		initTypefaces();
	}

	@Override
	public void show() {
		super.show();
		
		mNameEditText.setText(SettingsManager.getPlayerName(mContext));
	}
	
	private void initViews() {
		mNameEditText = (EditText) findViewById(R.id.editText_name);
		mOkButton = (Button) findViewById(R.id.button_ok);
	}
	
	private void initListeners() {
		mOkButton.setOnClickListener(mOnButtonClickListener);
	}

	private void initTypefaces() {
		((TextView) findViewById(R.id.textView_name_dialog_title)).setTypeface(App.happyHellTypeface);
		mOkButton.setTypeface(App.happyHellTypeface);
		mNameEditText.setTypeface(App.happyHellTypeface);
	}

	private android.view.View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.button_ok) {
				String name = TextUtils.isEmpty(mNameEditText.getText().toString()) ? 
						Constants.DEFAULT_PLAYER_NAME : mNameEditText.getText().toString();
				
				SettingsManager.setPlayerName(mContext, name);
			}

			dismiss();
		}
	};
}
