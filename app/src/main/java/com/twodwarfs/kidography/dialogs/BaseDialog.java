
package com.twodwarfs.kidography.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;

import com.twodwarfs.kidography.utils.Utils;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class BaseDialog extends Dialog {

	public BaseDialog(Context context) {
		super(context);
	}
	
	public BaseDialog(Context context, int layout, int resLayoutId) {
		super(context, layout);
		
		setContentView(resLayoutId);
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float width = (float) (metrics.widthPixels - ((float)metrics.widthPixels * 0.2));
		
		if(!Utils.isLargeDisplay(context)) {
//			width = (int) (metrics.widthPixels); 
		}
		
		getWindow().setLayout((int) width, LayoutParams.WRAP_CONTENT);
		
	}
}
