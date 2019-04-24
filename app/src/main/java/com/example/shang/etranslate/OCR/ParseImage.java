/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2016-2-24	| duanbokan 	| 	create the file                       
 */

package com.example.shang.etranslate.OCR;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.googlecode.android.TessBaseAPI;

import java.io.IOException;

public class ParseImage
{
	private static final String TAG = "ParseImage";
	
	private static ParseImage instance;
	
	public static ParseImage getInstance()
	{
		if (instance == null)
		{
			instance = new ParseImage();
		}
		return instance;
	}
	

	public String parseImageToString(String imagePath) throws IOException
	{
		if (imagePath == null || imagePath.equals(""))
		{
			return TessErrorCode.IMAGE_PATH_IS_NULL;
		}
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
		
		int rotate = 0;
		
		ExifInterface exif = new ExifInterface(imagePath);
		
		int imageOrientation = exif
				.getAttributeInt(ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);
		
		Log.i(TAG, "Current image orientation is " + imageOrientation);
		
		switch (imageOrientation)
		{
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			default:
				break;
		}
		
		Log.i(TAG, "Current image need rotate: " + rotate);
		
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		
		Matrix mtx = new Matrix();
		mtx.preRotate(rotate);
		
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		
		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(TessConstantConfig.TESSBASE_PATH,TessConstantConfig.DEFAULT_LANGUAGE_CHI);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		baseApi.end();
		return recognizedText;
	}
}
