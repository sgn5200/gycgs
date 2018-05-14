package com.lansent.cannan.data;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;
import java.util.regex.Pattern;


/**
 * Description    :
 * CreateAuthor: Cannan
 * Create time   : 2017/10/19     10:32
 */

public class DiskCache {
	private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
	private static final int MAX_DISK_CACHE_SIZE = 20 * 1024 * 1024; // 20MB

	private Pattern compile;


	public DiskCache(Context context, File diskDir, long diskMaxSize) {
		final String REGEX = "@createTime\\{(\\d+)\\}expireMills\\{((-)?\\d+)\\}@";
		compile = Pattern.compile(REGEX);
	}

	public void put(String key, String value) {
		if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) return;

	}

	public DiskCache setCacheTime(long cacheTime) {
		return this;
	}

	private static File getDiskCacheDir(Context context, String dirName) {
		String cachePath;
		if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable())
				&& context.getExternalCacheDir() != null) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + dirName);
	}

	private static long calculateDiskCacheSize(File dir) {
		long size = 0;
		try {
			if (!dir.exists()) {
				dir.mkdirs();
			}
			StatFs statFs = new StatFs(dir.getAbsolutePath());
			long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
			size = available / 50;
		} catch (IllegalArgumentException ignored) {

		}
		return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
	}
}
