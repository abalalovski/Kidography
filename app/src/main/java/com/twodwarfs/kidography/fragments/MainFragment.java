
package com.twodwarfs.kidography.fragments;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.audio.SoundManager;
import com.twodwarfs.kidography.dialogs.AboutDialog;
import com.twodwarfs.kidography.dialogs.SettingsDialog;
import com.twodwarfs.kidography.dialogs.ShareDialog;
import com.twodwarfs.kidography.helpers.Constants;
import com.twodwarfs.kidography.utils.Utils;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class MainFragment extends BaseFragment {

	private ImageView mEiffelImageView;
	private ImageView mLibertyStatueImageView;
	private ImageView mCompasImageView;

	private ImageView mLeftCloudImageView;
	private ImageView mRightCloudImageView;
	
	private Button mNewGameButton;
	private Button mSettingsButton;
	private ImageButton mHighScoresButton;
	private ImageButton mAboutButton;

	private float mDisplayWidth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_main_layout, null);
		initUi(v);
		
		return v;
	}

	@Override
	protected void initUi(View parent) {
		mLeftCloudImageView = (ImageView) parent.findViewById(R.id.imageView_left_cloud);
		mRightCloudImageView = (ImageView) parent.findViewById(R.id.imageView_right_cloud);

		mEiffelImageView = (ImageView) parent.findViewById(R.id.imageView_eifel);
		mLibertyStatueImageView = (ImageView) parent.findViewById(R.id.imageView_liberty);
		mCompasImageView = (ImageView) parent.findViewById(R.id.imageView_compas);
		
		mNewGameButton = (Button) parent.findViewById(R.id.button_new_game);
		mSettingsButton = (Button) parent.findViewById(R.id.button_settings);
		mHighScoresButton = (ImageButton) parent.findViewById(R.id.button_highscores);
		mAboutButton = (ImageButton) parent.findViewById(R.id.button_about);

		mLeftCloudImageView.setVisibility(View.VISIBLE);
		mRightCloudImageView.setVisibility(View.VISIBLE);

		mDisplayWidth = getResources().getDisplayMetrics().widthPixels;

		if(Utils.isLargeDisplay(getActivity()) && 
				Utils.isNewerThanGingerbread()) {   
			loadAnimations();
		}

		initListeners();
		initTypefaces(parent);
	}

	@Override
	protected void initListeners() {
		mNewGameButton.setOnClickListener(mOnClickListener);
		mSettingsButton.setOnClickListener(mOnClickListener);
		mHighScoresButton.setOnClickListener(mOnClickListener);
		mAboutButton.setOnClickListener(mOnClickListener);
	}

	@Override
	protected void initData() {
	}

	@Override
	public String getName() {
		return "main_fragment";
	}

	@SuppressLint("NewApi")
	private void loadAnimations() {
		mCompasImageView.setRotation(-10);

		ValueAnimator leftLandmarkAnimator = ValueAnimator.ofFloat(520, 0);
		leftLandmarkAnimator.setDuration(1500);
		leftLandmarkAnimator.setInterpolator(new OvershootInterpolator(1.5f));
		leftLandmarkAnimator.start();

		leftLandmarkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Float value = ((Float) (animation.getAnimatedValue())).floatValue();
				mEiffelImageView.setTranslationY(value);
			}
		});

		ValueAnimator rightLandmarkAnimator = ValueAnimator.ofFloat(520, 0);
		rightLandmarkAnimator.setDuration(1000);
		rightLandmarkAnimator.start();

		rightLandmarkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Float value = ((Float) (animation.getAnimatedValue())).floatValue();
				mLibertyStatueImageView.setTranslationY(value);
			}
		});

		ValueAnimator rotateCompasAnimator = ValueAnimator.ofFloat(-10, 10);
		rotateCompasAnimator.setDuration(6000);
		rotateCompasAnimator.setRepeatCount(Animation.INFINITE);
		rotateCompasAnimator.setRepeatMode(Animation.REVERSE);
		rotateCompasAnimator.setStartDelay(1500);
		rotateCompasAnimator.start();

		rotateCompasAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Float value = ((Float) (animation.getAnimatedValue())).floatValue();
				mCompasImageView.setRotation(value);
			}
		});

		ValueAnimator firstCloudAnimator = ValueAnimator.ofFloat(-220, mDisplayWidth*2);
		firstCloudAnimator.setDuration(40000);
		firstCloudAnimator.setInterpolator(new LinearInterpolator());
		firstCloudAnimator.setRepeatCount(Animation.INFINITE);
		firstCloudAnimator.setRepeatMode(Animation.RESTART);
		firstCloudAnimator.start();

		firstCloudAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Float value = ((Float) (animation.getAnimatedValue())).floatValue();
				mLeftCloudImageView.setTranslationX(value);
			}
		});

		ValueAnimator secondCloudAnimator = ValueAnimator.ofFloat(-220, mDisplayWidth*2);
		secondCloudAnimator.setDuration(20000);
		secondCloudAnimator.setInterpolator(new LinearInterpolator());
		secondCloudAnimator.setRepeatCount(Animation.INFINITE);
		secondCloudAnimator.setRepeatMode(Animation.RESTART);
		secondCloudAnimator.start();

		secondCloudAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Float value = ((Float) (animation.getAnimatedValue())).floatValue();
				mRightCloudImageView.setTranslationX(value);
			}
		});		
	}

	@Override
	protected void initTypefaces(View parent) {
		((Button) parent.findViewById(R.id.button_new_game)).setTypeface(App.happyHellTypeface);
		((Button) parent.findViewById(R.id.button_settings)).setTypeface(App.happyHellTypeface);
	}

	public static MainFragment newInstance() {
		return new MainFragment();
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SoundManager.instance(getActivity()).playSound(
					Constants.BUTTON_CLICK_SOUND, 1.0f);
			
			if(v.getId()==R.id.button_new_game) {
				showFragment(GameTypeFragment.newInstance());
			}
			else if(v.getId()==R.id.button_settings) {
				new SettingsDialog(getActivity()).show();
			}
			else if(v.getId()==R.id.button_about) {
				final AboutDialog aboutDialog = new AboutDialog(getActivity());
				aboutDialog.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						aboutDialog.dismiss();
					}
				});

				aboutDialog.show();
			}
			else if(v.getId()==R.id.button_highscores) {
				showFragment(HighScoresFragment.newInstance());
			}
		}
	};
}
