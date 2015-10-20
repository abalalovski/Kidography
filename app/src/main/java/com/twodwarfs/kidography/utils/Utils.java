package com.twodwarfs.kidography.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.twodwarfs.kidography.helpers.Constants;

public class Utils {

	public static void doLog(String logText) {
		Log.i(Constants.TAG, logText);
	}

	public static void doToast(Context context, String toastText) {
		Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
	}

	public static int getResourceId(Context context, String name, String type) {
		return context.getResources().getIdentifier(name, type, context.getApplicationInfo().packageName);
	}

	public static void saveStreamAsFile(File file, InputStream is, boolean isLarge) throws IOException {
		byte[] imageData = isLarge ? readLargeInputStreamAsBytes(is) : readInputStreamAsBytes(is);
		FileOutputStream out = new FileOutputStream(file);
		out.write(imageData, 0, imageData.length);
		out.flush();
		out.close();
		imageData = null;		
	}

	public static byte[] readInputStreamAsBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}

		return byteBuffer.toByteArray();
	}

	public static byte[] readLargeInputStreamAsBytes(InputStream inputStream) throws IOException {
		byte[] fileData = new byte[1024];
		int read = 0;
		while(read != fileData.length) {
			read += inputStream.read(fileData, read, fileData.length - read);
		}

		return fileData;
	}

	public static void saveStreamAsFile(File file, InputStream is) throws IOException {
		saveStreamAsFile(file, is, false);
	}

	public static boolean isNewerThanGingerbread() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		return (currentapiVersion > android.os.Build.VERSION_CODES.GINGERBREAD &&
				currentapiVersion > android.os.Build.VERSION_CODES.GINGERBREAD_MR1);
	}

	public static boolean isLargeDisplay(Context context) {
		int density = context.getResources().getDisplayMetrics().densityDpi;
		return (density != DisplayMetrics.DENSITY_LOW && 
				density != DisplayMetrics.DENSITY_MEDIUM &&
				density != DisplayMetrics.DENSITY_HIGH);
	}

	public static void share(Context context, String text, String imagePath) {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, text);
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
		shareIntent.setType("image/jpeg");
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		context.startActivity(Intent.createChooser(shareIntent, "Share"));
	}
}
