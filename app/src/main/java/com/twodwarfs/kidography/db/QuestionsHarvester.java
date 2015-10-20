package com.twodwarfs.kidography.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.twodwarfs.kidography.GameType.QuestionsType;
import com.twodwarfs.kidography.question.Capital;
import com.twodwarfs.kidography.question.Flag;
import com.twodwarfs.kidography.question.Landmark;
import com.twodwarfs.kidography.question.Question;
import com.twodwarfs.kidography.question.Riddle;
import com.twodwarfs.kidography.utils.Utils;

public class QuestionsHarvester {

	private static final String DATABASE_NAME = "questions.db";
	
	private Context mContext;
	private SQLiteDatabase mDb;
	
	private List<Riddle> mRiddles = new ArrayList<Riddle>();
	private List<Capital> mCapitals = new ArrayList<Capital>();	
	private List<Landmark> mLandmarks = new ArrayList<Landmark>();
	private List<Flag> mFlags = new ArrayList<Flag>();
	
	private int mLastRiddleIndex = 0;
	private int mLastCapitalIndex = 0;
	private int mLastLandmarkIndex = 0;
	private int mLastFlagIndex = 0;

	public QuestionsHarvester(Context context) {
		mContext = context;

		initDb(DATABASE_NAME);
		
		mRiddles = getAllRiddles();
		mCapitals = getAllCapitals();
		mLandmarks = getAllLandmarks();
		mFlags = getAllFlags();
	}
	
	public Riddle nextRiddle() {
		int index = mLastRiddleIndex++;
		if(index >= mRiddles.size()) {
			index = 0;
			mLastRiddleIndex = 0;
		}
		
		return mRiddles.get(index);
	}
	
	public Capital nextCapital() {
		int index = mLastCapitalIndex++;
		if(index >= mCapitals.size()) {
			index = 0;
			mLastCapitalIndex = 0;
		}
		
		return mCapitals.get(index);
	}
	
	public Landmark nextLandmark() {
		int index = mLastLandmarkIndex++;
		if(index >= mLandmarks.size()) {
			index = 0;
			mLastLandmarkIndex = 0;
		}
		
		return mLandmarks.get(index);
	}
	
	public Flag nextFlag() {
		int index = mLastFlagIndex++;
		if(index >= mFlags.size()) {
			index = 0;
			mLastFlagIndex = 0;
		}
		
		return mFlags.get(index);
	}
	
	private List<Riddle> getAllRiddles() {
		String[] fields = Question.Fields.ALL_FIELDS;
		String tableName = Question.Fields.RIDDLES_TABLE_NAME;
		Cursor cursor = mDb.query(tableName, fields, "", null, null, null, null);
		
		List<Riddle> riddles = new ArrayList<Riddle>();
		while(cursor.moveToNext()) {
			Riddle riddle = new Riddle();

			String[] answers = cursor.getString(cursor.getColumnIndex(Question.Fields.ANSWERS)).split(",");

			riddle.setText(cursor.getString(cursor.getColumnIndex(Question.Fields.TEXT)));
			riddle.setAnswers(answers);
			riddle.setCorrectAnswer(cursor.getInt(cursor.getColumnIndex(Question.Fields.CORRECT)));
			riddle.setQuestionsType(QuestionsType.RIDDLES);
					
			riddles.add(riddle);
		}

		Collections.shuffle(riddles);
		return riddles;
	}
	
	private List<Capital> getAllCapitals() {
		String[] fields = Question.Fields.ALL_FIELDS;
		String tableName = Question.Fields.CAPITALS_TABLE_NAME;
		Cursor cursor = mDb.query(tableName, fields, "", null, null, null, null);

		List<Capital> capitals = new ArrayList<Capital>();
		while(cursor.moveToNext()) {
			Capital capital = new Capital();

			String[] answers = cursor.getString(cursor.getColumnIndex(Question.Fields.ANSWERS)).split(",");
			
			capital.setText(cursor.getString(cursor.getColumnIndex(Question.Fields.TEXT)));
			capital.setAnswers(answers);
			capital.setCorrectAnswer(cursor.getInt(cursor.getColumnIndex(Question.Fields.CORRECT)));
			capital.setQuestionsType(QuestionsType.CAPITALS);
					
			capitals.add(capital);
		}

		Collections.shuffle(capitals);
		return capitals;
	}
	
	private List<Landmark> getAllLandmarks() {
		String[] fields = Question.Fields.ALL_FIELDS;
		String tableName = Question.Fields.LANDMARKS_TABLE_NAME;
		Cursor cursor = mDb.query(tableName, fields, "", null, null, null, null);

		List<Landmark> landmarks = new ArrayList<Landmark>();
		while(cursor.moveToNext()) {
			Landmark landmark = new Landmark();

			String[] answers = cursor.getString(cursor.getColumnIndex(Question.Fields.ANSWERS)).split(",");
			
			landmark.setText(cursor.getString(cursor.getColumnIndex(Question.Fields.TEXT)));
			landmark.setAnswers(answers);
			landmark.setCorrectAnswer(cursor.getInt(cursor.getColumnIndex(Question.Fields.CORRECT)));
			landmark.setQuestionsType(QuestionsType.LANDMARKS);
					
			landmarks.add(landmark);
		}

		Collections.shuffle(landmarks);
		return landmarks;
	}
	
	private List<Flag> getAllFlags() {
		String[] fields = Question.Fields.ALL_FIELDS;
		String tableName = Question.Fields.FLAGS_TABLE_NAME;
		Cursor cursor = mDb.query(tableName, fields, "", null, null, null, null);

		List<Flag> flags = new ArrayList<Flag>();
		while(cursor.moveToNext()) {
			Flag flag = new Flag();

			String[] answers = cursor.getString(cursor.getColumnIndex(Question.Fields.ANSWERS)).split(",");
			
			flag.setText(cursor.getString(cursor.getColumnIndex(Question.Fields.TEXT)));
			flag.setAnswers(answers);
			flag.setCorrectAnswer(cursor.getInt(cursor.getColumnIndex(Question.Fields.CORRECT)));
			flag.setQuestionsType(QuestionsType.LANDMARKS);
					
			flags.add(flag);
		}

		Collections.shuffle(flags);
		return flags;
	}

	private void initDb(String dbName) {
		QuestionsDataBaseHelper myDbHelper = new QuestionsDataBaseHelper(mContext, dbName);

		try {
			myDbHelper.createDataBase();
		} catch (IOException e) {
			Utils.doLog(e.getLocalizedMessage());
		}

		try {
			myDbHelper.openDataBase();
		} catch(SQLException sqle){
			throw sqle;
		}

		mDb = myDbHelper.getReadableDatabase();
	}
}
