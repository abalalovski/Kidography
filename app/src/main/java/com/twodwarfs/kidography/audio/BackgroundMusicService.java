package com.twodwarfs.kidography.audio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.twodwarfs.kidography.R;

public class BackgroundMusicService extends Service {

    private MediaPlayer mPlayer;
    
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        mPlayer = MediaPlayer.create(this, R.raw.music);
        mPlayer.setLooping(true);
        
		AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;  
        
        mPlayer.setVolume(volume, volume);
    }
    
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {
    	
    }
    
    public IBinder onUnBind(Intent arg0) {
        return null;
    }

    public void onStop() {

    }

    public void onPause() {
    }
    
    @Override
    public void onDestroy() {
        mPlayer.stop();
        mPlayer.release();
    }

    @Override
    public void onLowMemory() {
    }
}
