package com.twodwarfs.kidography.dialogs;

import java.util.Random;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.utils.SettingsManager;

public class TipsDialog extends BaseDialog {

	private Button mOkButton;
	private CheckBox mCheckBox;
	private TextView mTipTextView;
	
	public TipsDialog(Context context) {
		super(context, R.style.BorderlessDialogStyle, R.layout.tips_dialog_layout);
		
		mTipTextView = (TextView) findViewById(R.id.textView_tip);
		mOkButton = (Button) findViewById(R.id.button_ok);
		mCheckBox = (CheckBox) findViewById(R.id.checkBox_show_tips);
		mCheckBox.setChecked(!SettingsManager.showTips(context));
		
		String[] tips = context.getResources().getStringArray(R.array.tips);
		int tipIndex = new Random().nextInt(tips.length);
		mTipTextView.setText(tips[tipIndex]);	
		
		mOkButton.setTypeface(App.happyHellTypeface);
		mCheckBox.setTypeface(App.robotoTypeface);
		mTipTextView.setTypeface(App.robotoTypeface);
	}
	
	public void setOnOkButtonClickedListener(View.OnClickListener listener) {
		mOkButton.setOnClickListener(listener);
	}
	
	public boolean showNextTime() {
		return !mCheckBox.isChecked();
	}
}
