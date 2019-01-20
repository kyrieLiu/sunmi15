package ys.app.pad.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StatFs;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dennis
 * @create 2014-4-23
 */
public class BitmapUtil {
	/**
	 * 读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/***
	 * 根据资源文件获取Bitmap
	 * 
	 * @param context
	 * @param drawableId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int drawableId,
										int screenWidth, int screenHight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		options.inInputShareable = true;
		options.inPurgeable = true;
		InputStream stream = context.getResources().openRawResource(drawableId);
		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
		return getBitmap(bitmap, screenWidth, screenHight);
	}

	/***
	 * 等比例压缩图片
	 * 
	 * @param bitmap
	 * @param screenWidth
	 * @param screenHight
	 * @return
	 */
	public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
								   int screenHight) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Logger.i("图片宽度" + w + ",screenWidth=" + screenWidth);
		Matrix matrix = new Matrix();
		float scale = (float) screenWidth / w;
		float scale2 = (float) screenHight / h;

		// scale = scale < scale2 ? scale : scale2;

		// 保证图片不变形.
		matrix.postScale(scale, scale);
		// w,h是原图的属性.
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	/***
	 * 保存图片至SD卡
	 * 
	 * @param bm
	 * @param url
	 * @param quantity
	 */
	private static int FREE_SD_SPACE_NEEDED_TO_CACHE = 1;
	private static int MB = 1024 * 1024;
	public static void saveBmpToSd(Bitmap bm, String url, int quantity) {
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			return;
		}
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
			return;
		String dir = Environment.getExternalStorageDirectory().getPath()+"/dennis";
		String filename = url;
		// 目录不存在就创建
		File dirPath = new File(dir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		File file = new File(dir + "/" + filename);
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, quantity, outStream);
			outStream.flush();
			outStream.close();

		} catch (FileNotFoundException e) {
			Logger.i(e.toString());
		} catch (IOException e) {
			Logger.i(e.toString());
		}

	}

	/***
	 * 获取SD卡图片
	 * 
	 * @param url
	 * @param quantity
	 * @return
	 */
	public static Bitmap GetBitmap(String url, int quantity) {
		InputStream inputStream = null;
		String filename = "";
		Bitmap map = null;
		URL url_Image = null;
		String LOCALURL = "";
		if (url == null)
			return null;
		try {
			filename = url;
		} catch (Exception err) {
		}

		LOCALURL = URLEncoder.encode(filename);

		String dir = Environment.getExternalStorageDirectory().getPath()+"/dennis";
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) && Exist(Environment.getExternalStorageDirectory().getPath()+"/dennis/" + LOCALURL)) {
			map = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/dennis/"  + "/" + LOCALURL);
		} else {
			try {
				url_Image = new URL(url);
				inputStream = url_Image.openStream();
				map = BitmapFactory.decodeStream(inputStream);
				// url = URLEncoder.encode(url, "UTF-8");
				if (map != null) {
					saveBmpToSd(map, LOCALURL, quantity);
				}
				inputStream.close();
			} catch (Exception e) {
				Logger.i(e.toString());
				return null;
			}
		}
		return map;
	}

	/***
	 * 判断图片是存在
	 * 
	 * @param url
	 * @return
	 */
	public static boolean Exist(String url) {
		File file = new File(Environment.getExternalStorageDirectory().getPath()+"/dennis/"  + url);
		return file.exists();
	}

	/** * 计算sdcard上的剩余空间 * @return */
	private static int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;

		return (int) sdFreeMB;
	}

	/**
	 * 生成条码bitmap
	 * @param content
	 * @param format
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap generateBitmap(String content, int format, int width, int height) {
		if(content == null || content.equals(""))
			return null;
		BarcodeFormat barcodeFormat;
		switch (format){
			case 0:
				barcodeFormat = BarcodeFormat.UPC_A;
				break;
			case 1:
				barcodeFormat = BarcodeFormat.UPC_E;
				break;
			case 2:
				barcodeFormat = BarcodeFormat.EAN_13;
				break;
			case 3:
				barcodeFormat = BarcodeFormat.EAN_8;
				break;
			case 4:
				barcodeFormat = BarcodeFormat.CODE_39;
				break;
			case 5:
				barcodeFormat = BarcodeFormat.ITF;
				break;
			case 6:
				barcodeFormat = BarcodeFormat.CODABAR;
				break;
			case 7:
				barcodeFormat = BarcodeFormat.CODE_93;
				break;
			case 8:
				barcodeFormat = BarcodeFormat.CODE_128;
				break;
			case 9:
				barcodeFormat = BarcodeFormat.QR_CODE;
				break;
			default:
				barcodeFormat = BarcodeFormat.QR_CODE;
				height = width;
				break;
		}
		MultiFormatWriter qrCodeWriter = new MultiFormatWriter();
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "GBK");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		try {
			BitMatrix encode = qrCodeWriter.encode(content, barcodeFormat, width, height, hints);
			int[] pixels = new int[width * height];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (encode.get(j, i)) {
						pixels[i * width + j] = 0x00000000;
					} else {
						pixels[i * width + j] = 0xffffffff;
					}
				}
			}
			return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		return null;
	}



}
