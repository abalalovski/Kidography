package com.twodwarfs.kidography.audio;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.helpers.Constants;
import com.twodwarfs.kidography.utils.SettingsManager;

public class SoundManager {

	private static SoundManager mInstance = null;

	private SoundPool mSoundPool;
	private SparseIntArray mSoundsMap;
	private Context mContext;

	protected SoundManager(Context c) {
		mContext = c;
	}

	public static SoundManager instance(Context c) {
		if(mInstance == null) {
			mInstance = new SoundManager(c);
		}

		return mInstance;
	}

	public void initSoundPool() {
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		mSoundsMap = new SparseIntArray();

		mSoundsMap.put(Constants.BUTTON_CLICK_SOUND, mSoundPool.load(mContext, R.raw.click, 1));
		mSoundsMap.put(Constants.WRONG_ANSWER_SOUND, mSoundPool.load(mContext, R.raw.wrong, 1));
		mSoundsMap.put(Constants.CORRECT_ANSWER_SOUND, mSoundPool.load(mContext, R.raw.correct, 1));
		mSoundsMap.put(Constants.GAME_OVER_SOUND, mSoundPool.load(mContext, R.raw.gameover, 1));
		mSoundsMap.put(Constants.SNORE_SOUND,  mSoundPool.load(mContext, R.raw.snore, 1));
		mSoundsMap.put(Constants.START_GAME,  mSoundPool.load(mContext, R.raw.start, 1));
	}

	public void playSound(int sound, float fSpeed) {
		if(SettingsManager.areSoundsEnabled(mContext)) {
			AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax;  

			mSoundPool.play(mSoundsMap.get(sound), volume, volume, 1, 0, fSpeed);
		}
	}
	
	public void pause(int soundId) {
		mSoundPool.pause(soundId);
	}

	public void startBgMusic(boolean startAnyway) {
		if(SettingsManager.isBackgroundMusicEnabled(mContext) || startAnyway) {
			Intent bgMusicServiceIntent = new Intent(mContext, BackgroundMusicService.class);
			mContext.startService(bgMusicServiceIntent);
		}
	}
	
	public void startBgMusic() {
		startBgMusic(false);
	}

	public void stopBgMusic() {
		Intent bgMusicServiceIntent = new Intent(mContext, BackgroundMusicService.class);
		mContext.stopService(bgMusicServiceIntent);
	}
}
