package com.twodwarfs.kidography.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.R;

public class BaseActivity extends FragmentActivity {

	protected App mApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApp = (App) getApplication();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	protected void initViews() {
	}

	protected void initTypefaces() {
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.activity_enter_anim, 
				R.anim.activity_exit_anim);
	}
}
