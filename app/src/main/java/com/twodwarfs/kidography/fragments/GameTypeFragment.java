
package com.twodwarfs.kidography.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.GameType;
import com.twodwarfs.kidography.GameType.QuestionsType;
import com.twodwarfs.kidography.GameType.Type;
import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.audio.SoundManager;
import com.twodwarfs.kidography.dialogs.NameDialog;
import com.twodwarfs.kidography.dialogs.SettingsDialog;
import com.twodwarfs.kidography.helpers.Constants;
import com.twodwarfs.kidography.utils.SettingsManager;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class GameTypeFragment extends BaseFragment {
	
	private ViewFlipper mTypesFlipper;
	private ImageButton mEditNameButton;
	private TextView mPlayerNameTextView;
	private TextView mGameTypeLabelTextView;
	
	private Button mTwelverButton;
	private Button mTimeLimitButton;
	
	private Button mFirstCategory;
	private Button mSecondCategory;
	private Button mThirdCategory;
	private Button mFourthCategory;
	
	private Button mSettingsButton;
	private boolean mFirstTime = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.game_type_layout, null);
		initUi(v);

		if(!mFirstTime ) {
		}
		
		mFirstTime = false;
		
		return v;
	}

	@Override
	protected void initUi(View parent) {
		mPlayerNameTextView = (TextView) parent.findViewById(R.id.textView_player_name);
		mGameTypeLabelTextView = (TextView) parent.findViewById(R.id.textView_game_type_label);
		mTypesFlipper = (ViewFlipper) parent.findViewById(R.id.game_types_flipper);
		mEditNameButton = (ImageButton) parent.findViewById(R.id.imageButton_edit_name);
		
		mTwelverButton = (Button) parent.findViewById(R.id.button_twelver);
		mTimeLimitButton = (Button) parent.findViewById(R.id.button_time_limit);
		
		mFirstCategory = (Button) parent.findViewById(R.id.button_first_cat);
		mSecondCategory = (Button) parent.findViewById(R.id.button_second_cat);
		mThirdCategory = (Button) parent.findViewById(R.id.button_third_cat);
		mFourthCategory = (Button) parent.findViewById(R.id.button_fourth_cat);
		
		mSettingsButton = (Button) parent.findViewById(R.id.button_settings);
		
		initListeners();
		initTypefaces(parent);
	}

	@Override
	protected void initListeners() {
		mEditNameButton.setOnClickListener(mOnClickListener);
		
		mTwelverButton.setOnClickListener(mOnClickListener);
		mTimeLimitButton.setOnClickListener(mOnClickListener);
		
		mFirstCategory.setOnClickListener(mOnClickListener);
		mSecondCategory.setOnClickListener(mOnClickListener);
		mThirdCategory.setOnClickListener(mOnClickListener);
		mFourthCategory.setOnClickListener(mOnClickListener);
		
		mSettingsButton.setOnClickListener(mOnClickListener);
		
		initData();
	}

	@Override
	protected void initData() {
		checkPlayersName();
	}

	@Override
	protected void initTypefaces(View parent) {
		mGameTypeLabelTextView.setTypeface(App.happyHellTypeface);
		mPlayerNameTextView.setTypeface(App.happyHellTypeface);

		((Button)parent.findViewById(R.id.button_twelver)).setTypeface(App.happyHellTypeface);
		((Button)parent.findViewById(R.id.button_time_limit)).setTypeface(App.happyHellTypeface);

		((Button)parent.findViewById(R.id.button_first_cat)).setTypeface(App.happyHellTypeface);
		((Button)parent.findViewById(R.id.button_second_cat)).setTypeface(App.happyHellTypeface);
		((Button)parent.findViewById(R.id.button_third_cat)).setTypeface(App.happyHellTypeface);
		((Button)parent.findViewById(R.id.button_fourth_cat)).setTypeface(App.happyHellTypeface);

		((Button)parent.findViewById(R.id.button_settings)).setTypeface(App.happyHellTypeface);
	}

	@Override
	protected String getName() {
		return "game_type_fragment";
	}
	
	@Override
	public void onBackPressed() {
		if(mTypesFlipper.getDisplayedChild() > 0) {
			mTypesFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), 
					R.anim.translate_right_to_left_in));
			mTypesFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), 
					R.anim.translate_right_to_left_out));
			mTypesFlipper.setDisplayedChild(0);
		}
		else {
			super.onBackPressed();
		}
	}
	
	private void startGame(int reqId) {

		if(reqId==R.id.button_twelver) {
			GameType.instance().setGameType(Type.TWELVER);
			mTypesFlipper.showNext();
		}
		else if(reqId==R.id.button_time_limit) {
			GameType.instance().setGameType(Type.LIMITED);
			mTypesFlipper.showNext();
		}
		else if(reqId==R.id.button_first_cat) {
			GameType.instance().setQuestionsType(QuestionsType.RIDDLES);
			showFragment(QuestionFragment.newInstance());
		}
		else if(reqId==R.id.button_second_cat) {
			GameType.instance().setQuestionsType(QuestionsType.CAPITALS);
			showFragment(QuestionFragment.newInstance());
		}
		else if(reqId==R.id.button_third_cat) {
			GameType.instance().setQuestionsType(QuestionsType.FLAGS);
			showFragment(QuestionFragment.newInstance());
		}
		else if(reqId==R.id.button_fourth_cat) {
			GameType.instance().setQuestionsType(QuestionsType.LANDMARKS);
			showFragment(QuestionFragment.newInstance());
		}
		else if(reqId==R.id.button_settings) {
			new SettingsDialog(getActivity()).show();
		}
	}
	
	private void checkPlayersName() {
		Dialog nameDialog = new NameDialog(getActivity());
		nameDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				
				if(TextUtils.isEmpty(SettingsManager.getPlayerName(getActivity()))) {
					SettingsManager.setPlayerName(getActivity(), Constants.DEFAULT_PLAYER_NAME);
				}
				
				mPlayerNameTextView.setText(
						SettingsManager.getPlayerName(getActivity()));

			}
		});

		if(SettingsManager.getPlayerName(getActivity()).equals("")) {
			nameDialog.show();
		}
		else {
			mPlayerNameTextView.setText(SettingsManager.getPlayerName(getActivity()));
		}
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SoundManager.instance(getActivity()).playSound(
					Constants.BUTTON_CLICK_SOUND, 1.0f);
			
			int id = v.getId();
			if(id == R.id.imageButton_edit_name) {
				NameDialog nameDialog = new NameDialog(getActivity());
				nameDialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						mPlayerNameTextView.setText(
								SettingsManager.getPlayerName(getActivity()));
					}
				});
				
				nameDialog.show();
			}
			else {
				mTypesFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), 
						R.anim.translate_left_to_right_in));
				mTypesFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), 
						R.anim.translate_left_to_right_out));
		
				startGame(v.getId());
			}
		}
	};
	
	public static GameTypeFragment newInstance() {
		return new GameTypeFragment();
	}
}