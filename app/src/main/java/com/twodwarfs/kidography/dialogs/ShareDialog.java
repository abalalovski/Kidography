
package com.twodwarfs.kidography.dialogs;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twodwarfs.kidography.App;
import com.twodwarfs.kidography.R;
import com.twodwarfs.kidography.utils.Utils;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class ShareDialog extends BaseDialog {

	private Button mShareButton;
	private TextView mShareTextView;

	private Context mContext;
	private View mRoot;

	public ShareDialog(Context context) {
		super(context, R.style.BorderlessDialogStyle, R.layout.layout_share_shot);

		mShareButton = (Button) findViewById(R.id.button_do_share);
		mShareTextView = (TextView) findViewById(R.id.textView_share_title);
		mRoot = findViewById(R.id.share_root);
		mRoot.setDrawingCacheEnabled(true);

		setCancelable(true);
		mContext = context;

		mShareButton.setOnClickListener(mOnClickListener);

		initTypefaces();
		
		float width = mContext.getResources().getDimension(R.dimen.share_dialog_width);
		float height = mContext.getResources().getDimension(R.dimen.share_dialog_height);
		
		getWindow().setLayout((int)width, (int)height);
	}

	private void initTypefaces() {
		mShareButton.setTypeface(App.happyHellTypeface);
		mShareTextView.setTypeface(App.happyHellTypeface);
	}

	public void setText(String text) {
		mShareTextView.setText(text);
	}

	public Bitmap getDrawingCache() {
		return mRoot.getDrawingCache();
	}

	public void doShare() {
		String state = Environment.getExternalStorageState();
		File fileDir = mContext.getExternalFilesDir(null);
		File shareFile = new File(fileDir, "share.png");
		
		mShareButton.setVisibility(View.GONE);
		mRoot.invalidate();
		
		Bitmap bmp = getDrawingCache();

		try {
			FileOutputStream out = new FileOutputStream(
					shareFile.getAbsolutePath());
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();

			Utils.share(mContext,
					mContext.getString(R.string.share_title), 
					shareFile.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
			Utils.doToast(mContext, "There was a problem sharing the results!");
		}
		
		dismiss();

	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			doShare();
		}
	};
}
