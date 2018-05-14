/**
 *      
 */
package com.lansent.cannan.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

/**
 * 对bitmap执行处理的工具类
 * 
 */
public class BitmapUtil {

	/**
	 * @param bitmap
	 *            要压缩和保存的图片
	 *            ，单位是kb
	 * @return 返回图片的输出流
	 */
	public static ByteArrayOutputStream compressBitmap(Bitmap bitmap, int size) {

		int options = 100;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 质量压缩方法，100表示不压缩，把压缩后的数据存放到baos中
		bitmap.compress(CompressFormat.JPEG, options,
				byteArrayOutputStream);
		while ((byteArrayOutputStream.toByteArray().length / 1024) > size) {
			// 循环判断如果压缩后图片是否大于size,继续压缩
			byteArrayOutputStream.reset();// 重置baos即清空baos
			options /= 2;
			bitmap.compress(CompressFormat.JPEG, options,
					byteArrayOutputStream);// 这里压缩options%，把压缩后的数据存放到baos中
			if (options == 0) {
				break;
			}
		}

		return byteArrayOutputStream;
	}

	/**
	 * @param bitmap
	 *            要压缩和保存的图片
	 *            ，单位是kb
	 * @return 返回图片的输出流
	 */
	public static InputStream compressBitmapToInputStream(Bitmap bitmap) {

		int options = 100;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 质量压缩方法，100表示不压缩，把压缩后的数据存放到baos中
		bitmap.compress(CompressFormat.JPEG, options,
				byteArrayOutputStream);
		ByteArrayInputStream mArrayInputStream = new ByteArrayInputStream(
				byteArrayOutputStream.toByteArray());
		if (byteArrayOutputStream != null) {
			try {
				byteArrayOutputStream.flush();
				byteArrayOutputStream.close();
				byteArrayOutputStream = null;
				System.gc();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mArrayInputStream;
	}

	public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
		if (context == null || uri == null)
			return null;

		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/** Bitmap -- Byte */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, baos);
		byte[] mByte = baos.toByteArray();
		if (baos != null) {
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			baos = null;
		}
		return mByte;
	}

	/**
	 * Bitmap--Base64 string
	 **/
	public static String BitmapToBase64String(Bitmap mBitmap) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		mBitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
		String dataBase64 = Base64.encodeToString(
				byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
		return dataBase64;
	}

	/** 图片尺寸 */
	public static class ImageSize {
		public int width;
		public int height;
	}

	/**
	 *
	 * 获取ImageView的尺寸
	 *
	 * @param imageView
	 * @return
	 */
	public static ImageSize obtainImageViewSize(ImageView imageView) {
		ImageSize imageSize = new ImageSize();
		DisplayMetrics metrics = imageView.getContext().getResources()
				.getDisplayMetrics();
		LayoutParams params = imageView.getLayoutParams();
		//
		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getWidth();
		if (width <= 0)
			width = params.width;

		if (width <= 0)
			// width = imageView.getMaxWidth();// 需要API16
			width = getImageViewFieldValue(imageView, "mMaxWidth");

		if (width <= 0)
			width = metrics.widthPixels;

		//
		int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getHeight(); // Get actual image height
		if (height <= 0)
			height = params.height; // Get layout height parameter

		if (height <= 0)
			height = getImageViewFieldValue(imageView, "mMaxHeight");

		if (height <= 0)
			height = metrics.heightPixels;

		imageSize.width = width;
		imageSize.height = height;
		return imageSize;
	}

	/**
	 *
	 * 反射获得ImageView设置的最大宽度和高度
	 *
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return value;
	}

	/***
	 * 图片的缩放方法--缩放到指定比例
	 *
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	/**
	 *
	 * 等比例尺寸缩放图片
	 *
	 * @param bitmap
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		// 实现等比缩放
		boolean isLongBitmap = false;
		if (width <= newWidth && height <= newHeight) {
			// 不缩放
		} else {
			if (width / height >= 2) {
				if (height > 1200) {
					matrix.postScale(scaleHeight, scaleHeight);
				} else {
					isLongBitmap = true;
				}
			} else if (height / width >= 2) {
				if (width > 1200) {
					matrix.postScale(scaleWidth, scaleWidth);
				} else {
					isLongBitmap = true;
				}
			} else {
				if (width > height) {
					matrix.postScale(scaleWidth, scaleWidth);
				} else {
					matrix.postScale(scaleHeight, scaleHeight);
				}
			}
		}
		// 得到新的图片
		if (isLongBitmap) {
			return bitmap;
		} else {
			return compressImage(Bitmap.createBitmap(bitmap, 0, 0, width,
					height, matrix, false), 250);
		}
	}

	/**
	 * 读取本地图为drawable
	 **/
	public static Drawable compressFileToDrawable(String mFilePath) {
		Bitmap mBitmap = BitmapFactory.decodeFile(mFilePath);
		Drawable mDrawable = new BitmapDrawable(null, mBitmap);
		return mDrawable;
	}

	/**
	 *  保存图片
	 **/
	@SuppressLint("SdCardPath")
	public static boolean saveBitmap2file(Bitmap bmp, String filename) {
		CompressFormat format = CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		File file=new File(filename);
		if(file.exists()){
			file.delete();
		}

		try {
			stream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (bmp.compress(format, quality, stream)) {
			try {
				stream.flush();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 读取本地图片为bitmap
	 **/
	public static Bitmap compressFileToBitmap(String mFilePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		return BitmapFactory.decodeFile(mFilePath, options);
	}

	/**
	 * 旋转图片
	 * @param bitmap
	 * @param degree
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
		// 旋转图片
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bitmap;
	}

	// 压缩图片大小
	public static Bitmap compressImage(Bitmap image, int size) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于size
			if (options < 0) {
				break;
			} // kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 20;// 每次都减少20
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
		// options.inJustDecodeBounds = true;
		mBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, mBitmapOptions);// 把ByteArrayInputStream数据生成图片
		try {
			baos.close();
			isBm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * drawable缩放
	 * 
	 * @param drawable
	 * @param  w
	 * @param  h
	 **/
	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap
		Matrix matrix = new Matrix(); // 创建操作图片用的 Matrix 对象
		float scaleWidth = ((float) w / width); // 计算缩放比例
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true); // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
		return new BitmapDrawable(null, newbmp); // 把 bitmap 转换成 drawable 并返回
	}

	/**
	 * drawable to bitmap
	 **/
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth(); // 取 drawable 的长宽
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // 取 drawable 的颜色格式
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应
																	// bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // 把 drawable 内容画到画布中
		return bitmap;
	}

	/**
	 * 1、bitmap  to  uri
	 * <p/>
	 * Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
	 * <p/>
	 * 2、uri  to  bitmap
	 * <p/>
	 * Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
	 * 读取图片属性：旋转的角度
	 *
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}
