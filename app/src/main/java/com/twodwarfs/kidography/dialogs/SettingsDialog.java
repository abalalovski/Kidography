package com.twodwarfs.kidography.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.BuildConfig;
import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.audio.SoundManager;
import com.twodwarfs.kidography.helpers.Constants;
import com.twodwarfs.kidography.utils.SettingsManager;
import com.twodwarfs.kidography.utils.Utils;

public class SettingsDialog extends BaseDialog {

	private SeekBar mTimingSeekBar;
	private TextView mSecondsTextView;
	
	private Switch mBgMusicSwitch;
	private Switch mSoundsSwitch;
	private Switch mAnimationsSwitch;
	private Switch mTipsSwitch;
	
	private Button mOkButton;
	private Button mCancelButton;
	
	private Context mContext;

	public SettingsDialog(Context context) {
		super(context, R.style.BorderlessDialogStyle, R.layout.settings_layout);

		mContext = context;

		initViews();
		initListeners();
		
		initTypefaces();		
	}

	private void initViews() {
		mTimingSeekBar = (SeekBar) findViewById(R.id.seekBar_timing);
		mSecondsTextView = (TextView) findViewById(R.id.textview_timing_seconds);
		
		mBgMusicSwitch = (Switch) findViewById(R.id.button_music_switch);
		mSoundsSwitch = (Switch) findViewById(R.id.button_sounds_switch);
		mAnimationsSwitch = (Switch) findViewById(R.id.button_anim_switch);
		mTipsSwitch = (Switch) findViewById(R.id.button_tips_switch);
		
		initFromSettings();
		
		mOkButton = (Button) findViewById(R.id.button_ok);
		mCancelButton = (Button) findViewById(R.id.button_cancel); 
	}
	
	private void initListeners() {
		mOkButton.setOnClickListener(mOnButtonClickListener);
		mCancelButton.setOnClickListener(mOnButtonClickListener);
		
		mTimingSeekBar.setOnSeekBarChangeListener(mTimingSeekbarListener);
		
		mBgMusicSwitch.setOnCheckedChangeListener(mSwitchButtonsListener);
		mSoundsSwitch.setOnCheckedChangeListener(mSwitchButtonsListener);
		mAnimationsSwitch.setOnCheckedChangeListener(mSwitchButtonsListener);
		mTipsSwitch.setOnCheckedChangeListener(mSwitchButtonsListener);
	}

	private void initTypefaces() {
//		((TextView) findViewById(R.id.textView_settings_title)).setTypeface(App.titleTypeface);
		mOkButton.setTypeface(App.happyHellTypeface);
		mCancelButton.setTypeface(App.happyHellTypeface);
		
		((TextView) findViewById(R.id.textview_bg_music)).setTypeface(App.happyHellTypeface);
		((TextView) findViewById(R.id.textview_sounds)).setTypeface(App.happyHellTypeface);
		((TextView) findViewById(R.id.textview_animations)).setTypeface(App.happyHellTypeface);
		((TextView) findViewById(R.id.textview_tips)).setTypeface(App.happyHellTypeface);
		((TextView) findViewById(R.id.textview_timing_seconds)).setTypeface(App.happyHellTypeface);
		((TextView) findViewById(R.id.textview_timing_label)).setTypeface(App.happyHellTypeface);
		
		((Switch)findViewById(R.id.button_music_switch)).setTypeface(App.happyHellTypeface);
		((Switch)findViewById(R.id.button_sounds_switch)).setTypeface(App.happyHellTypeface);
		((Switch)findViewById(R.id.button_anim_switch)).setTypeface(App.happyHellTypeface);
		((Switch)findViewById(R.id.button_tips_switch)).setTypeface(App.happyHellTypeface);
	}
	
	private void writeSettings() {
		SettingsManager.setBackgroundMusicEnabled(mContext, mBgMusicSwitch.isChecked());
		SettingsManager.setPlaySoundsEnabled(mContext, mSoundsSwitch.isChecked());
		SettingsManager.setDoAnimations(mContext, mAnimationsSwitch.isChecked());
		SettingsManager.setShowTips(mContext, mTipsSwitch.isChecked());
		
		SettingsManager.setGameTime(mContext, mTimingSeekBar.getProgress());
		
		if(BuildConfig.DEBUG) {
			Utils.doLog("write: Background Music -> " + String.valueOf(mBgMusicSwitch.isChecked()));
			Utils.doLog("write: Sounds -> " + String.valueOf(mSoundsSwitch.isChecked()));
			Utils.doLog("write: Animations -> " + String.valueOf(mAnimationsSwitch.isChecked()));
			Utils.doLog("write: Tips -> " + String.valueOf(mTipsSwitch.isChecked()));
			
			Utils.doLog("write: Time -> " + String.valueOf(mTimingSeekBar.getProgress()));
		}
	}
	
	private void initFromSettings() {
		mBgMusicSwitch.setChecked(SettingsManager.isBackgroundMusicEnabled(mContext));
		mSoundsSwitch.setChecked(SettingsManager.areSoundsEnabled(mContext));
		mAnimationsSwitch.setChecked(SettingsManager.doAnimations(mContext));
		mTipsSwitch.setChecked(SettingsManager.showTips(mContext));
		
		int gameTime = SettingsManager.getGameTime(mContext) * 10 + 30;
		mTimingSeekBar.setProgress(SettingsManager.getGameTime(mContext));
		mSecondsTextView.setText(String.format("%d seconds", gameTime));
	}

	private OnSeekBarChangeListener mTimingSeekbarListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			mSecondsTextView.setText(String.format("%d seconds", progress * 10 + 30));
		}
	};

	private android.view.View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			SoundManager.instance(mContext).playSound(Constants.BUTTON_CLICK_SOUND, 1);
			
			if(v.getId()==R.id.button_ok) {
				writeSettings();
			}
			
			dismiss();
		}
	};
	
	private OnCheckedChangeListener mSwitchButtonsListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(buttonView == mBgMusicSwitch) {
				if(isChecked) {
					SoundManager.instance(mContext).startBgMusic(true);
				}
				else {
					SoundManager.instance(mContext).stopBgMusic();
				}
			}
			else if(buttonView == mSoundsSwitch) {
			}
			else if(buttonView == mAnimationsSwitch) {
			}
		}
	};
}
