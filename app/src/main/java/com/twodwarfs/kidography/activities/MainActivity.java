package com.twodwarfs.kidography.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.audio.SoundManager;
import com.twodwarfs.kidography.fragments.BaseFragment;
import com.twodwarfs.kidography.fragments.MainFragment;

public class MainActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		MainFragment mainFragment = MainFragment.newInstance();
		transaction.add(R.id.main_container, mainFragment);
//		transaction.addToBackStack(mainFragment.getName());
		transaction.commit();
	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		int count = fm.getBackStackEntryCount();

		if(count > 0) {
			FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(count-1);
			String str = backEntry.getName();
			BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(str);

			if(fragment != null) {
				fragment.onBackPressed();
			}
		}
		else {
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		SoundManager.instance(this).startBgMusic();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		SoundManager.instance(this).stopBgMusic();
	}
}