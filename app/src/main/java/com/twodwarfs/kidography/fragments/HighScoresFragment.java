
package com.twodwarfs.kidography.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.db.scores.Score;
import com.twodwarfs.kidography.db.scores.ScoresKeeper;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class HighScoresFragment extends BaseFragment {

	private TextView mFirstPlayerNameTextView;
	private TextView mSecondPlayerNameTextView;
	private TextView mThirdPlayerNameTextView;
	private TextView mFourthPlayerNameTextView;
	private TextView mFifthPlayerNameTextView;
	private List<TextView> allPlayersNames = new ArrayList<TextView>();

	private TextView mFirstPlayerPointsTextView;
	private TextView mSecondPlayerPointsTextView;
	private TextView mThirdPlayerPointsTextView;
	private TextView mFourthPlayerPointsTextView;
	private TextView mFifthPlayerPointsTextView;
	private List<TextView> allPlayersPoints = new ArrayList<TextView>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.highscores_layout, null);
		initUi(v);

		return v;
	}

	@Override
	protected void initUi(View parent) {
		LinearLayout firstRowLayout = (LinearLayout) parent.findViewById(R.id.highscore_first_item);
		LinearLayout secondRowLayout = (LinearLayout) parent.findViewById(R.id.highscore_second_item);
		LinearLayout thirdRowLayout = (LinearLayout) parent.findViewById(R.id.highscore_third_item);
		LinearLayout fourthRowLayout = (LinearLayout) parent.findViewById(R.id.highscore_fourth_item);
		LinearLayout fifthRowLayout = (LinearLayout) parent.findViewById(R.id.highscore_fifth_item);

		mFirstPlayerNameTextView = (TextView) firstRowLayout.findViewById(R.id.textView_highscore_name);
		mSecondPlayerNameTextView = (TextView) secondRowLayout.findViewById(R.id.textView_highscore_name);
		mThirdPlayerNameTextView = (TextView) thirdRowLayout.findViewById(R.id.textView_highscore_name);
		mFourthPlayerNameTextView = (TextView) fourthRowLayout.findViewById(R.id.textView_highscore_name);
		mFifthPlayerNameTextView = (TextView) fifthRowLayout.findViewById(R.id.textView_highscore_name);
		allPlayersNames.add(mFirstPlayerNameTextView);
		allPlayersNames.add(mSecondPlayerNameTextView);
		allPlayersNames.add(mThirdPlayerNameTextView);
		allPlayersNames.add(mFourthPlayerNameTextView);
		allPlayersNames.add(mFifthPlayerNameTextView);

		mFirstPlayerPointsTextView =  (TextView) firstRowLayout.findViewById(R.id.textView_highscore_scores);
		mSecondPlayerPointsTextView =  (TextView) secondRowLayout.findViewById(R.id.textView_highscore_scores);
		mThirdPlayerPointsTextView =  (TextView) thirdRowLayout.findViewById(R.id.textView_highscore_scores);
		mFourthPlayerPointsTextView =  (TextView) fourthRowLayout.findViewById(R.id.textView_highscore_scores);
		mFifthPlayerPointsTextView =  (TextView) fifthRowLayout.findViewById(R.id.textView_highscore_scores);
		allPlayersPoints.add(mFirstPlayerPointsTextView);
		allPlayersPoints.add(mSecondPlayerPointsTextView);
		allPlayersPoints.add(mThirdPlayerPointsTextView);
		allPlayersPoints.add(mFourthPlayerPointsTextView);
		allPlayersPoints.add(mFifthPlayerPointsTextView);

		initListeners();
		initTypefaces(parent);
	}

	@Override
	protected void initListeners() {
		initData();
	}

	@Override
	protected void initData() {
		initScores();
	}

	@Override
	protected void initTypefaces(View parent) {
		for(TextView view : allPlayersNames) {
			view.setTypeface(App.happyHellTypeface);
		}

		for(TextView view : allPlayersPoints) {
			view.setTypeface(App.happyHellTypeface);
		}
	}

	@Override
	protected String getName() {
		return "highscores_fragment";
	}

	private void initScores() {
		ScoresKeeper scoresKeeper = new ScoresKeeper(getActivity());
		scoresKeeper.open();

		ArrayList<Score> topScores = scoresKeeper.getTopScores();
		for(int i=0; i < topScores.size(); i++) {
			allPlayersNames.get(i).setVisibility(View.VISIBLE);
			allPlayersPoints.get(i).setVisibility(View.VISIBLE);

			allPlayersNames.get(i).setText(topScores.get(i).playerName);
			allPlayersPoints.get(i).setText(String.valueOf(topScores.get(i).points));
		}
	}

	public static HighScoresFragment newInstance() {
		return new HighScoresFragment();
	}
}
