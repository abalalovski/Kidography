package com.twodwarfs.kidography.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.twodwarfs.kidography.helpers.Constants;

public class QuestionsDataBaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/com.twodwarfs.kidography/databases/";

	private SQLiteDatabase mDataBase;
	private final Context mContext;

	private String mDbName;

	public QuestionsDataBaseHelper(Context context, String dbName) {
		super(context, dbName, null, 1);
		mContext = context;
		mDbName = dbName;
	}

	public void createDataBase() throws IOException{
		boolean dbExist = checkDataBase();
		if(!dbExist) {
			getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.i(Constants.TAG, e.getLocalizedMessage());
			}
		} 
	}

	private boolean checkDataBase(){
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + mDbName;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch(SQLiteException e){
			Log.i(Constants.TAG, e.getLocalizedMessage());
		}

		if(checkDB != null){
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException{
		InputStream inputStream = mContext.getAssets().open("db/" + mDbName);
		String outFileName = DB_PATH + mDbName;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		inputStream.close();
	}

	public void openDataBase() throws SQLException{
		String myPath = DB_PATH + mDbName;
		
		mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if(mDataBase != null)
			mDataBase.close();
		
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}