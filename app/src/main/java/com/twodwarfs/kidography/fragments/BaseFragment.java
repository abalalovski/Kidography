
package com.twodwarfs.kidography.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twodwarfs.kidography.R;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public abstract class BaseFragment extends Fragment {
	
	protected abstract void initUi(View parent);
	protected abstract void initListeners();
	protected abstract void initData();
	protected abstract void initTypefaces(View parent);
	protected abstract String getName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	protected void showFragment(BaseFragment fragment, boolean addToBackstack) {
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.activity_enter_anim, R.anim.activity_exit_anim, 
				R.anim.activity_enter_anim, R.anim.activity_exit_anim);
		transaction.replace(R.id.main_container, fragment, fragment.getName());
		if(addToBackstack) transaction.addToBackStack(fragment.getName());
		transaction.commit();
	}
	
	protected void showFragment(BaseFragment fragment) {
		showFragment(fragment, true);
	}
	
	public void onBackPressed() {
		int count = getActivity().getSupportFragmentManager().
				getBackStackEntryCount();
		
		if(count > 0) {
			getActivity().getSupportFragmentManager().popBackStackImmediate();
		}
		else {
			getActivity().finish();
		}
	}

}
