
package com.twodwarfs.kidography.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.helpers.ResultsCollector;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class GameOverDialog extends BaseDialog {

	private Button mShareMenuButton;
	private Button mMainMenuButton;
	
	private TextView mCorrectAnswersTextView;
	private TextView mWrongAnswersTextView;
	private TextView mScoreTextView;
	
	private Context mContext;

	public GameOverDialog(Context context) {
		super(context, R.style.BorderlessDialogStyle, R.layout.game_over_dialog_layout);

		mContext = context;

		mShareMenuButton = (Button) findViewById(R.id.button_share);
		mMainMenuButton = (Button) findViewById(R.id.button_main_menu);

		mCorrectAnswersTextView = (TextView) findViewById(R.id.textView_correct);
		mWrongAnswersTextView = (TextView) findViewById(R.id.textView_wrong);
		mScoreTextView = (TextView) findViewById(R.id.textView_score);

		setCancelable(true);

		initTypefaces();
	}

	private void initTypefaces() {
		mShareMenuButton.setTypeface(App.happyHellTypeface);
		mMainMenuButton.setTypeface(App.happyHellTypeface);
		
		mCorrectAnswersTextView.setTypeface(App.happyHellTypeface);
		mWrongAnswersTextView.setTypeface(App.happyHellTypeface);
		mScoreTextView.setTypeface(App.happyHellTypeface);
	}

	public void setOnClickListener(View.OnClickListener onClickListener) {
		mShareMenuButton.setOnClickListener(onClickListener);
		mMainMenuButton.setOnClickListener(onClickListener);
	}
	
	public void setResult(ResultsCollector result) {
		mScoreTextView.setText(mContext.getString(R.string.score) + result.points);
		mCorrectAnswersTextView.setText(mContext.getString(R.string.correct) + result.correct);
		mWrongAnswersTextView.setText(mContext.getString(R.string.wrong) + ": " + result.wrong);
	}

}
