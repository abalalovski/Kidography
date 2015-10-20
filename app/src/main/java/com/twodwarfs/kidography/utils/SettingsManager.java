package com.twodwarfs.kidography.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.twodwarfs.kidography.BuildConfig;
import com.twodwarfs.kidography.helpers.Constants;

public class SettingsManager {
	
	private static void setStringValue(Context context, String key, String value) {
		if(BuildConfig.DEBUG) Utils.doLog(key + ": " + String.valueOf(value));
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(key, value);
        prefEditor.commit();
	}
	
	private static String getStringValue(Context context, String key, String defValue) {
		SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defValue);
	}
	
	private static void setIntegerValue(Context context, String key, int value) {
		if(BuildConfig.DEBUG) Utils.doLog(key + ": " + String.valueOf(value));
		SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putInt(key, value);
        prefEditor.commit();
	}
	
	private static int getIntegerValue(Context context, String key, int defValue) {
		SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defValue);
	}
	
	private static void setBooleanValue(Context context, String key, boolean value) {
		if(BuildConfig.DEBUG) Utils.doLog(key + ": " + String.valueOf(value));
		SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putBoolean(key, value);
        prefEditor.commit();
	}
	
	private static boolean getBooleanValue(Context context, String key, boolean defValue) {
		SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defValue);
	}
	
	public static void setBackgroundMusicEnabled(Context context, boolean isEnabled) {
        setBooleanValue(context, Constants.PLAY_BG_MUSIC, isEnabled);
	}
	
	public static boolean isBackgroundMusicEnabled(Context context) {
        return getBooleanValue(context, Constants.PLAY_BG_MUSIC, true);
	}
	
	public static void setPlaySoundsEnabled(Context context, boolean areEnabled) {
		setBooleanValue(context, Constants.PLAY_SOUNDS, areEnabled);
	}
	
	public static boolean areSoundsEnabled(Context context) {
		return getBooleanValue(context, Constants.PLAY_SOUNDS, true);
	}
	
	public static void setDoAnimations(Context context, boolean doAnimations) {
		setBooleanValue(context, Constants.DO_ANIMATIONS, doAnimations);
	}
	
	public static boolean doAnimations(Context context) {
		return getBooleanValue(context, Constants.DO_ANIMATIONS, true);
	}
	
	public static void setGameTime(Context context, int timeInSeconds) {
		setIntegerValue(context, Constants.GAME_TIME, timeInSeconds);
	}
	
	public static int getGameTime(Context context) {
		return getIntegerValue(context, Constants.GAME_TIME, 0);
	}
	
	public static void setShowTips(Context context, boolean showTips) {
		setBooleanValue(context, Constants.SHOW_TIPS, showTips);
	}
	
	public static boolean showTips(Context context) {
		return getBooleanValue(context, Constants.SHOW_TIPS, true);
	}

	public static void setPlayerName(Context context, String name) {
		setStringValue(context, Constants.PLAYER_NAME, name);
	}
	
	public static String getPlayerName(Context context) {
		return getStringValue(context, Constants.PLAYER_NAME, "");
	}
}
