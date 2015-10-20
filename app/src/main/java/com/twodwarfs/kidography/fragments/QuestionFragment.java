
package com.twodwarfs.kidography.fragments;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.BuildConfig;
import com.twodwarfs.kidography.GameType;
import com.twodwarfs.kidography.GameType.QuestionsType;
import com.twodwarfs.kidography.GameType.Type;
import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.audio.SoundManager;
import com.twodwarfs.kidography.db.QuestionsHarvester;
import com.twodwarfs.kidography.db.scores.Score;
import com.twodwarfs.kidography.db.scores.ScoresKeeper;
import com.twodwarfs.kidography.dialogs.ExitDialog;
import com.twodwarfs.kidography.dialogs.GameOverDialog;
import com.twodwarfs.kidography.dialogs.PauseDialog;
import com.twodwarfs.kidography.dialogs.ShareDialog;
import com.twodwarfs.kidography.dialogs.TipsDialog;
import com.twodwarfs.kidography.helpers.Constants;
import com.twodwarfs.kidography.helpers.CustomTypefaceSpan;
import com.twodwarfs.kidography.helpers.Points;
import com.twodwarfs.kidography.helpers.ResultsCollector;
import com.twodwarfs.kidography.question.Question;
import com.twodwarfs.kidography.utils.SettingsManager;
import com.twodwarfs.kidography.utils.Utils;
import com.twodwarfs.kidography.views.TransparentPanel;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class QuestionFragment extends BaseFragment {

	protected static final int TICK_DELAY = 1000; // ms, 1sec
	protected static final int TIME_UP_UPDATE = 0x001;
	protected static final int PAUSE_UPDATE = 0x010;

	private ProgressBar mGameProgressBar;
	private Button mFirstAnswerButton, mSecondAnswerButton, mThirdAnswerButton;
	private ImageButton mPauseButton;
	private TextView mQuestionTextView;
	private ImageView mQuestionTitleImageView;
	private TextView mAnnounceTextView;
	private TextView mSubInfoAnnounceTextView;
	private LinearLayout mQuestionlayout;
	private Button mAnswerButton, mCorrectAnswerButton;
	private ImageView mFlagImageView;

	private Timer mMainTimer, mAnswerTimer;
	private Question mCurrentQuestion;

	protected QuestionsHarvester mHarvester;
	private boolean mGameActive = true;
	private boolean mQuestionAsked = true;
	private int mCounter;

	private boolean mAnswerCorrect;

	private Animation mOutAnimation;

	private ScoresKeeper mScoresKeeper;
	private ExitDialog mExitDialog;
	private GameOverDialog mGameOverDialog;
	private PauseDialog mPauseDialog;
	private ResultsCollector mResult = new ResultsCollector();

	private Vibrator mVibrator;
	private ViewGroup mBannerView;
	private TextView mScoreInfoTextView;
	private TextView mScoreTextView;
	private TransparentPanel mFadingView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.question_main_layout, null);
		initUi(v);

		return v;
	}

	@Override
	protected void initUi(View parent) {
		mGameProgressBar = (ProgressBar) parent.findViewById(R.id.progressBar_timing);

		mFirstAnswerButton = (Button) parent.findViewById(R.id.button_answer_one);
		mSecondAnswerButton = (Button) parent.findViewById(R.id.button_answer_two);
		mThirdAnswerButton = (Button) parent.findViewById(R.id.button_answer_three);
		mQuestionTextView = (TextView) parent.findViewById(R.id.textView_question);
		mQuestionTitleImageView = (ImageView) parent.findViewById(R.id.imageView_question_title);
		mAnnounceTextView = (TextView) parent.findViewById(R.id.textView_start_game);
		mSubInfoAnnounceTextView = (TextView) parent.findViewById(R.id.textView_subinfo);
		mQuestionlayout = (LinearLayout) parent.findViewById(R.id.layout_question);
		
		mPauseButton = (ImageButton) parent.findViewById(R.id.button_pause);

		mFlagImageView = (ImageView) parent.findViewById(R.id.imageView_flag);

		mScoreInfoTextView = (TextView) parent.findViewById(R.id.textView_score_info);
		mScoreTextView = (TextView) parent.findViewById(R.id.textView_score);
		mFadingView = (TransparentPanel) parent.findViewById(R.id.panelView_fading);

		mExitDialog = new ExitDialog(getActivity());
		mExitDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.button_yes) {
					SoundManager.instance(getActivity()).playSound(Constants.GAME_OVER_SOUND, 1.0f);
					getFragmentManager().popBackStackImmediate();
				}
				else if(v.getId() == R.id.button_no) {
					mGameActive = true;
					startGame();
				}

				mExitDialog.dismiss();
			}
		});

		mGameOverDialog = new GameOverDialog(getActivity());
		mGameOverDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.button_share) {

					final ShareDialog shareDialog = new ShareDialog(getActivity());
					String gameTypeText = "";
					if(GameType.instance().getQuestionsType() == QuestionsType.RIDDLES) {
						gameTypeText = "Riddles";
					}
					else if(GameType.instance().getQuestionsType() == QuestionsType.FLAGS) {
						gameTypeText = "Flags";
					}
					else if(GameType.instance().getQuestionsType() == QuestionsType.CAPITALS) {
						gameTypeText = "Capitals";
					}
					else if(GameType.instance().getQuestionsType() == QuestionsType.LANDMARKS) {
						gameTypeText = "Landmarks";
					}

					String shareText = String.format(getString(R.string.share_text), 
							mResult.correct, gameTypeText, mResult.points);
					shareDialog.setText(shareText);
					
					shareDialog.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							gotoMainMenu();
						}
					});

					shareDialog.show();
				}
				else if(v.getId() == R.id.button_main_menu) {
					gotoMainMenu();
				}
				
				mGameOverDialog.dismiss();
			}
		});
		
		mGameOverDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				gotoMainMenu();
			}
		}); 

		mPauseDialog = new PauseDialog(getActivity());
		mPauseDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				mGameActive = true;
				SoundManager.instance(getActivity()).startBgMusic();
				startGame();				
			}
		});

		mBannerView = (ViewGroup) parent.findViewById(R.id.view_banner);

		if(GameType.instance().getGameType() == Type.TWELVER) {
			mPauseButton.setVisibility(View.GONE);
		}

		initListeners();
		initTypefaces(parent);
	}
	
	private void gotoMainMenu() {
		getFragmentManager().popBackStack("game_type_fragment", 
				FragmentManager.POP_BACK_STACK_INCLUSIVE);							
	}

	@Override
	protected void initListeners() {
		mFirstAnswerButton.setOnClickListener(mOnClickListener );
		mSecondAnswerButton.setOnClickListener(mOnClickListener);
		mThirdAnswerButton.setOnClickListener(mOnClickListener);
		mPauseButton.setOnClickListener(mOnClickListener);
		
		initData();
	}

	@Override
	protected void initData() {

		mHarvester = new QuestionsHarvester(getActivity());
		initialQuestion();

		mQuestionlayout.startAnimation(AnimationUtils.loadAnimation(getActivity(), 
				R.anim.translate_left_to_right_in));

		mScoresKeeper = new ScoresKeeper(getActivity());
		mScoresKeeper.open();

		mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

		initTwelverTiming();
		mAnswerTimer = new Timer();

		if(SettingsManager.showTips(getActivity())) {
			final TipsDialog tipsDialog = new TipsDialog(getActivity());
			tipsDialog.show();

			tipsDialog.setOnOkButtonClickedListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SoundManager.instance(getActivity()).playSound(
							Constants.BUTTON_CLICK_SOUND, 1);
					tipsDialog.dismiss();
				}
			});

			tipsDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					SettingsManager.setShowTips(getActivity(), tipsDialog.showNextTime());
					startGameAnim();
				}
			});
		}
		else {
			startGameAnim();
		}
	}

	@Override
	protected void initTypefaces(View parent) {
		((TextView) parent.findViewById(R.id.textView_score)).setTypeface(App.happyHellTypeface);
		((TextView) parent.findViewById(R.id.textView_score_info)).setTypeface(App.happyHellTypeface);

		mQuestionTextView.setTypeface(App.equalSansTypeface);

		mAnnounceTextView.setTypeface(App.happyHellTypeface);
		mSubInfoAnnounceTextView.setTypeface(App.happyHellTypeface);

		mFirstAnswerButton.setTypeface(App.happyHellTypeface);
		mSecondAnswerButton.setTypeface(App.happyHellTypeface);
		mThirdAnswerButton.setTypeface(App.happyHellTypeface);
	}

	@Override
	protected String getName() {
		return "question_fragment";
	}

	@Override
	public void onBackPressed() {
		mGameActive = false;
		mExitDialog.show();
	}

	private void spanQuestion() {
		String questionText = mCurrentQuestion.getText();
		SpannableStringBuilder questionSpanBuilder = new SpannableStringBuilder(questionText);

		int qmarksIndex = questionText.indexOf("...GUESS...");

		if(qmarksIndex < 0) {
			mQuestionTextView.setText(questionText);
		}
		else {
			questionSpanBuilder.setSpan(new CustomTypefaceSpan("", App.equalSansTypeface), 0, qmarksIndex, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			questionSpanBuilder.setSpan(new CustomTypefaceSpan("", App.happyHellTypeface), qmarksIndex, questionText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			questionSpanBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.holo_blue)), qmarksIndex, questionText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			mQuestionTextView.setText(questionSpanBuilder);
		}
	}

	private void initialQuestion() {
		resolveNextQuestion();

		if(GameType.instance().getQuestionsType()!=QuestionsType.FLAGS) {
			mQuestionTextView.setVisibility(View.VISIBLE);
			mFlagImageView.setVisibility(View.GONE);
			spanQuestion();
		}
		else {
			String resName = String.format("flag_%s", mCurrentQuestion.getText().toLowerCase());
			int resId= Utils.getResourceId(getActivity(), resName, "drawable");

			mQuestionTextView.setVisibility(View.GONE);
			mFlagImageView.setVisibility(View.VISIBLE);
			mFlagImageView.setImageResource(resId);
		}

		setAnswers();

		findCorrectAnswerButton();
	}

	private void resolveNextQuestion() {
		if(GameType.instance().getQuestionsType() == QuestionsType.RIDDLES) {
			mCurrentQuestion = mHarvester.nextRiddle();
			mQuestionTitleImageView.setBackgroundResource(R.drawable.riddle_header);
		}
		else if(GameType.instance().getQuestionsType() == QuestionsType.CAPITALS) {
			mCurrentQuestion = mHarvester.nextCapital();
			mQuestionTitleImageView.setBackgroundResource(R.drawable.capital_header);
		}
		else if(GameType.instance().getQuestionsType() == QuestionsType.FLAGS) {
			mCurrentQuestion = mHarvester.nextFlag();
			mQuestionTitleImageView.setBackgroundResource(R.drawable.flag_header);
		}
		else if(GameType.instance().getQuestionsType() == QuestionsType.LANDMARKS) {
			mCurrentQuestion = mHarvester.nextLandmark();
			mQuestionTitleImageView.setBackgroundResource(R.drawable.landmark_header);
		}
	}

	private void nextQuestion() {
		Animation outAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_left_to_right_out);
		final Animation inAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_left_to_right_in);

		inAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mQuestionAsked = true;
			}
		});

		mQuestionlayout.startAnimation(outAnimation);

		resolveNextQuestion();

		outAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				showAnswersButtons();

				if(GameType.instance().getQuestionsType()!=QuestionsType.FLAGS) {
					mQuestionTextView.setVisibility(View.VISIBLE);
					mFlagImageView.setVisibility(View.GONE);
					spanQuestion();
				}
				else {
					String resName = String.format("flag_%s", 
							mCurrentQuestion.getText().toLowerCase(Locale.getDefault()
									).replace(" ", "_"));

					int resId= Utils.getResourceId(getActivity(), resName, "drawable");
					mQuestionTextView.setVisibility(View.GONE);
					mFlagImageView.setVisibility(View.VISIBLE);
					mFlagImageView.setImageResource(resId);
					Log.i(Constants.TAG, resName);
				}

				setAnswers();

				mCorrectAnswerButton.setBackgroundResource(R.drawable.general_button_selector);
				mQuestionlayout.startAnimation(inAnimation);

				findCorrectAnswerButton();
			}
		});
	}

	protected void setAnswers() {
		List<String> answers = mCurrentQuestion.getAnswers();
		Collections.shuffle(answers);

		Utils.doLog(mCurrentQuestion.getText());

		mFirstAnswerButton.setText(answers.get(0));
		mSecondAnswerButton.setText(answers.get(1));
		mThirdAnswerButton.setText(answers.get(2));		
	}

	private void findCorrectAnswerButton() {
		String correctAnswer = mCurrentQuestion.getCorrectAnswerString();
		if(mFirstAnswerButton.getText().toString().equals(correctAnswer)) {
			mCorrectAnswerButton = mFirstAnswerButton;
		}
		else if(mSecondAnswerButton.getText().toString().equals(correctAnswer)) {
			mCorrectAnswerButton = mSecondAnswerButton;
		}
		else if(mThirdAnswerButton.getText().toString().equals(correctAnswer)) {
			mCorrectAnswerButton = mThirdAnswerButton;
		}
	}

	private void showAnswersButtons() {
		Animation zoomInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.show_fadein_anim);

		mFirstAnswerButton.setVisibility(View.VISIBLE);
		mSecondAnswerButton.setVisibility(View.VISIBLE);
		mThirdAnswerButton.setVisibility(View.VISIBLE);

		mFirstAnswerButton.startAnimation(zoomInAnimation);
		mSecondAnswerButton.startAnimation(zoomInAnimation);
		mThirdAnswerButton.startAnimation(zoomInAnimation);
	}

	private void hideAnswersButtons() {
		Animation zoomOutAnimation = AnimationUtils.loadAnimation(getActivity(), 
				R.anim.show_fadeout_anim);

		mFirstAnswerButton.startAnimation(zoomOutAnimation);
		mSecondAnswerButton.startAnimation(zoomOutAnimation);
		mThirdAnswerButton.startAnimation(zoomOutAnimation);
	}

	@Override
	public void onPause() {
		super.onPause();

		//		doPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void onButtonClick(View v) {

		int id = v.getId();
		if(id==R.id.button_answer_one || id==R.id.button_answer_two
				|| id==R.id.button_answer_three) {

			if(!mQuestionAsked) return;

			mAnswerButton = (Button) v;
			String correctAnswer = mCurrentQuestion.getCorrectAnswerString();
			String proposedAnswer = mAnswerButton.getText().toString();
			mAnswerCorrect = correctAnswer.equals(proposedAnswer);

			int answerSnd = 0; //mAnswerCorrect; // ? Constants.CORRECT_ANSWER_SOUND : Constants.WRONG_ANSWER_SOUND;
			if(mAnswerCorrect) {
				answerSnd = Constants.CORRECT_ANSWER_SOUND;
			}
			else {
				answerSnd = Constants.WRONG_ANSWER_SOUND;
				mVibrator.vibrate(100);
			}

			SoundManager.instance(getActivity()).playSound(answerSnd, 1.5f);

			answerCorrectnessReaction();
			mQuestionAsked = false;
		}
		else if(id==R.id.button_pause) {
			doPause();
		}
	}

	private void doPause() {
		SoundManager.instance(getActivity()).stopBgMusic();
		SoundManager.instance(getActivity()).playSound(Constants.SNORE_SOUND, 1);
		mGameActive = false;
		mPauseDialog.show();
	}

	private void answerCorrectnessReaction() {
		mCounter = 0;

		for(int i=0; i < 10; i++) {
			long secs = 500 -(50*i);
			if(BuildConfig.DEBUG) Utils.doLog(String.valueOf(secs));			

			final Message m = new Message();
			mAnswerTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					if((mCounter % 2) == 0) {
						m.what = mAnswerCorrect ? 1 : 0;
						mAnswerIndicatorHandler.sendMessage(m);
					}
					else {
						m.what = 2;
						mAnswerIndicatorHandler.sendMessage(m);
					}

					if(mCounter++ >= 9) {
						Message msg = new Message();
						msg.what = 3;
						mAnswerIndicatorHandler.sendMessage(msg);
					}
				}
			}, secs);
		}
	}

	protected void evaluateAnswer() {

		final Animation inAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.activity_enter_anim);

		mOutAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.translate_disappear_to_left);

		if(mAnswerCorrect) {
			QuestionsType type = mCurrentQuestion.getType();
			int point = 0;

			if(type == QuestionsType.RIDDLES) {
				point = Points.RIDDLES;
			}
			else if(type == QuestionsType.CAPITALS) {
				point = Points.CAPITALS;
			}
			else if(type == QuestionsType.FLAGS) {
				point = Points.FLAGS;
			}
			else if(type == QuestionsType.LANDMARKS) {
				point = Points.LANDMARKS;
			}

			mScoreInfoTextView.setTextColor(getResources().getColor(R.color.game_info_labels_color));

			mResult.points += point;
			mResult.correct++;

			mScoreInfoTextView.setText("+" + String.valueOf(point));
		}
		else {
			mScoreInfoTextView.setText(getString(R.string.wrong));
			mResult.wrong++;
			if(mResult.wrong == 3) {
				mScoreInfoTextView.setText(getString(R.string.oh_come_on));
			}
			else {
				mScoreInfoTextView.setText(getString(R.string.wrong));
			}

			mScoreInfoTextView.setTextColor(Color.RED);
			mOutAnimation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.translate_base_up_anim);
		}

		if(GameType.instance().getGameType() == Type.TWELVER) {
			mGameProgressBar.incrementProgressBy(-1);
			if(mGameProgressBar.getProgress() == 0) {
				gameOver();
			}
		}

		hideAnswersButtons();
		nextQuestion();

		mScoreInfoTextView.setVisibility(View.VISIBLE);

		mScoreInfoTextView.startAnimation(inAnimation);
		inAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				mScoreInfoTextView.startAnimation(mOutAnimation);
			}
		});

		mOutAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				mScoreInfoTextView.setVisibility(View.GONE);
				mScoreTextView.setText(String.format("Score: %d", mResult.points));
			}
		});		
	}

	private void initTwelverTiming() {
		int gameTime = SettingsManager.getGameTime(getActivity());
		int max = gameTime * 10 + 30; 

		if(GameType.instance().getGameType() == Type.TWELVER) {
			max = 12;
		}

		mGameProgressBar.setMax(max);
		mGameProgressBar.setProgress(max);
	}

	private void startGame() {

		mMainTimer = new Timer();

		if(GameType.instance().getGameType() == Type.LIMITED) {
			mMainTimer.schedule(new GameTimerTask(), 0, TICK_DELAY);
		}

		showAnswersButtons();
		mGameActive = true;
	}

	private void startGameAnim() {
		final AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setDuration(2200);
		fadeOut.setStartOffset(600);
		fadeOut.setInterpolator(new LinearInterpolator());

		mFadingView.startAnimation(fadeOut);

		final Animation wobbleInAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.wobble_anim_in);
		final Animation countDownZoomInAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.countdown_anim_out);

		if(GameType.instance().getGameType() == Type.LIMITED) {
			mSubInfoAnnounceTextView.setText(getString(R.string.time_limit_game_type));
		}		
		else if(GameType.instance().getGameType() == Type.TWELVER) {
			mSubInfoAnnounceTextView.setText(getString(R.string.twelve_game_type));
		}

		mAnnounceTextView.startAnimation(wobbleInAnimation);

		wobbleInAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mAnnounceTextView.startAnimation(countDownZoomInAnimation);
				SoundManager.instance(getActivity()).playSound(
						Constants.START_GAME, 1);
			}
		});

		countDownZoomInAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mAnnounceTextView.setVisibility(View.INVISIBLE);
			}
		});

		fadeOut.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mFadingView.setVisibility(View.GONE);
				startGame();
			}
		});
	}

	private void gameOver() {
		mGameActive = false;

		mGameOverDialog.setResult(mResult);
		mGameOverDialog.show();

		SoundManager.instance(getActivity()).playSound(Constants.GAME_OVER_SOUND, 1);
		SoundManager.instance(getActivity()).stopBgMusic();

		AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setDuration(1200);
		fadeIn.setInterpolator(new LinearInterpolator());

		final ImageView gameOverDialogTitle = (ImageView) mGameOverDialog.findViewById(R.id.imageView_game_over_title);

		final Animation wobbleOutAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.wobble_anim_out);
		final Animation wobbleInAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.wobble_anim_in);

		gameOverDialogTitle.startAnimation(wobbleOutAnimation);

		wobbleOutAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				gameOverDialogTitle.startAnimation(wobbleInAnimation);
			}
		});

		wobbleInAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				gameOverDialogTitle.startAnimation(wobbleOutAnimation);
			}
		});

		if(mResult.points > 0) {
			Score score = new Score();
			score.playerName = SettingsManager.getPlayerName(getActivity());
			score.points = mResult.points;

			mScoresKeeper.insertScore(score);
		}
	}

	private class GameTimerTask extends TimerTask  {
		@Override
		public void run() {
			mGameProgressBar.incrementProgressBy(-1);
			if(BuildConfig.DEBUG) Utils.doLog(String.valueOf(mGameProgressBar.getProgress()));

			if(mGameProgressBar.getProgress() == 0) {
				cancel();

				Message updateUiMessage = new Message();
				updateUiMessage.what = TIME_UP_UPDATE;

				mUIUpdatesHandler.sendMessage(updateUiMessage);
			}
			else if(!mGameActive) {
				cancel();
				Message updateUiMessage = new Message();
				updateUiMessage.what = PAUSE_UPDATE;
				mUIUpdatesHandler.sendMessage(updateUiMessage);
			}
		}
	};

	private Handler mUIUpdatesHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch(msg.what) {
			case TIME_UP_UPDATE:
				gameOver();
				break;
			case PAUSE_UPDATE:
				break;
			}
		}
	};

	private Handler mAnswerIndicatorHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch(msg.what) {
			case 0:
				mAnswerButton.setBackgroundResource(R.drawable.red_button_normal);
				mCorrectAnswerButton.setBackgroundResource(R.drawable.green_button_normal);
				break;
			case 1:
				mAnswerButton.setBackgroundResource(R.drawable.green_button_normal);
				break;
			case 2:
				mAnswerButton.setBackgroundResource(R.drawable.general_button_selector);
				break;
			case 3: 
				evaluateAnswer();
				break;
			}
		}
	};

	public static QuestionFragment newInstance() {
		return new QuestionFragment();
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SoundManager.instance(getActivity()).playSound(
					Constants.BUTTON_CLICK_SOUND, 1.0f);
			
			onButtonClick(v);
		}
	};  

}
