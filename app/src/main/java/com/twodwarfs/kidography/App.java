package com.twodwarfs.kidography;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Application;
import android.graphics.Typeface;
import android.os.Environment;

import com.twodwarfs.kidography.audio.SoundManager;
import com.twodwarfs.kidography.utils.Utils;


public class App extends Application {
	
//	public static Typeface buttonsTypeface;
	public static Typeface happyHellTypeface;
	public static Typeface robotoTypeface;

	public static Typeface equalSansTypeface;
//	public static Typeface answerButtonTypeface;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		SoundManager.instance(this).initSoundPool();
		initializeTypefaces();
		
		File scoresFile = new File("/data/data/com.twodwarfs.kidography/databases/", "quizion_scores.db");
		InputStream scoresInputStream = null;
		try {
			scoresInputStream = new FileInputStream(scoresFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(scoresFile.exists()) {
			try {
				File newFile = new File(Environment.getExternalStorageDirectory(),  "quizion_scores.db");
				newFile.createNewFile();
				Utils.saveStreamAsFile(newFile, scoresInputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initializeTypefaces() {
		happyHellTypeface = Typeface.createFromAsset(getAssets(), "fonts/HappyHell.ttf");
		robotoTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Condensed.ttf");
    	equalSansTypeface = Typeface.createFromAsset(getAssets(), "fonts/EqualSans_Demo.ttf");
	}
}
