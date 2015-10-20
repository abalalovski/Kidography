
package com.twodwarfs.kidography.db.scores;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.twodwarfs.kidography.utils.SettingsManager;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class ScoresKeeper {
	
	private SQLiteDatabase mDatabase;
	private ScoresSQLiteHelper mCachingDbHelper;
	private Context mContext;

	public ScoresKeeper(Context context) {
		mCachingDbHelper = new ScoresSQLiteHelper(context);
		mContext = context;
	}

	public void open() throws SQLException {
		mDatabase = mCachingDbHelper.getWritableDatabase();
	}

	public void close() {
		mCachingDbHelper.close();
	}

	public void insertScore(Score score) {
		ContentValues values = new ContentValues();
		values.put(ScoresFields.PLAYER_NAME, score.playerName);
		values.put(ScoresFields.POINTS, score.points);
		
		String playerName = SettingsManager.getPlayerName(mContext);
		Cursor exCursor = mDatabase.query(ScoresFields.TABLE_NAME, 
				ScoresFields.ALL_FIELDS, ScoresFields.PLAYER_NAME + " = '" + playerName + "'",
				null, null, null, null);
		exCursor.moveToFirst();
		
		if(exCursor.getCount() > 0) {
			Score sc = cursorToScore(exCursor);
			if(score.points > sc.points) {
				mDatabase.update(ScoresFields.TABLE_NAME, values, 
						ScoresFields.PLAYER_NAME + " = '" + playerName + "'", null);
			}
		}
		else {
			mDatabase.insert(ScoresFields.TABLE_NAME, null, values);
		}
		
//		Cursor cursor = mDatabase.query(ScoresFields.TABLE_NAME,
//				ScoresFields.ALL_FIELDS, ScoresFields.ID + " = " + opId, null,
//				null, null, null);
//		cursor.moveToFirst();
//		
//		Score newScore = cursorToScore(cursor);
//		cursor.close();
//		
//		return newScore;
	}

	public ArrayList<Score> getAllScores() {
		ArrayList<Score> scores = new ArrayList<Score>();

		Cursor cursor = mDatabase.query(ScoresFields.TABLE_NAME,
				ScoresFields.ALL_FIELDS, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			scores.add(cursorToScore(cursor));
			cursor.moveToNext();
		}

		cursor.close();
		return scores;
	}
	
	public ArrayList<Score> getTopScores() {
		ArrayList<Score> scores = new ArrayList<Score>();

		Cursor cursor = mDatabase.query(ScoresFields.TABLE_NAME,
				ScoresFields.ALL_FIELDS, null, null, null, null, 
				ScoresFields.POINTS + " DESC LIMIT 5");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			scores.add(cursorToScore(cursor));
			cursor.moveToNext();
		}

		cursor.close();
		return scores;
	}

	public boolean deleteScore(Score score) {
		return mDatabase.delete(ScoresFields.TABLE_NAME, 
				ScoresFields.ID + " = " + score.id, null) > 0;
	}
	
	private Score cursorToScore(Cursor cursor) {
		Score score = new Score();

		score.id = cursor.getInt(cursor.getColumnIndex(ScoresFields.ID));
		score.playerName = cursor.getString(cursor.getColumnIndex(ScoresFields.PLAYER_NAME));
		score.points = cursor.getInt(cursor.getColumnIndex(ScoresFields.POINTS));

		return score;
	}
}
