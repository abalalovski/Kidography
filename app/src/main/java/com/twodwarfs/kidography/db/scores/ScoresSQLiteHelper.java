package com.twodwarfs.kidography.db.scores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoresSQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "quizion_scores.db";
	private static final int DATABASE_VERSION = 1;

	public ScoresSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		for(String createString : ScoresDBCreationString.ALL_CREATES) {
			database.execSQL(createString);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ScoresFields.TABLE_NAME);
		onCreate(db);
	}
} 