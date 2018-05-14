package com.lansent.cannan.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Cannan on 2017/7/26 0026.
 * <p>
 * 重写Android Log 日志打印，
 * <p>
 * 添加打印线程，类，方法，行号，标签
 */

public class Log {

	private Log() {
	}

	/**
	 * 日志开关
	 */
	public static boolean enable = true;

	/**
	 * 保存打印日志到文件
	 */
	public static boolean saveLog = false;

	public static Context context;

	/**
	 * 日志标签
	 */
	public static String MyTag = "【Cannan】";

	/**
	 * 把日志输出到文件放在子线程所定义的线程池
	 */
	private static ExecutorService fixedThreadPool;

	/**
	 * 保存日志文件最大长度
	 */
	private static final int LOG_FILE_MAX_SIZE = 1024 * 1024;           //内存中日志文件最大值，1M
	/**
	 * 保存日志目录
	 */
	private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Framework" + File.separator + "log";

	/**
	 * 天与毫秒的倍数
	 */
	public static final int DAY = 86400000;

	public static void i(String TAG) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		if (enable) {
			android.util.Log.i(TAG, rebuildMsg(stackTraceElement, ""));
		}
	}

	public static void i(String TAG, String msg) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		if (enable) {
			android.util.Log.i(TAG, rebuildMsg(stackTraceElement, msg));
		}
	}

	public static void e(String TAG, String msg) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		if (enable) {
			android.util.Log.e(TAG, rebuildMsg(stackTraceElement, msg));
		}
	}

	public static void d(String TAG, String msg) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		if (enable) {
			android.util.Log.d(TAG, rebuildMsg(stackTraceElement, msg));
		}
	}

	public static void w(String TAG, String msg) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		if (enable) {
			android.util.Log.w(TAG, rebuildMsg(stackTraceElement, msg));
		}
	}

	public static void v(String TAG, String msg) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
		if (enable) {
			android.util.Log.v(TAG, rebuildMsg(stackTraceElement, msg));
		}
	}

	/**
	 * 格式化msg形式
	 *
	 * @param msg
	 * @return
	 */
	private static String rebuildMsg(StackTraceElement stackTraceElement, String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append(MyTag);
		sb.append(stackTraceElement.getClassName().substring(stackTraceElement.getClassName().lastIndexOf(".") + 1));
		sb.append(".");
		sb.append(stackTraceElement.getMethodName());
		sb.append(" ");
		sb.append(stackTraceElement.getLineNumber());
		sb.append("\t");
		sb.append(msg);

		String m = sb.toString();

		if (saveLog && null!=context) {
			wirte2file(m);
		}
		return m;
	}


	private static String getPath() {
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		int day = aCalendar.get(Calendar.DAY_OF_MONTH);
		String p = path + File.separator + day + ".txt";
		return p;
	}

	private static SimpleDateFormat myLogSdf = new SimpleDateFormat("MM-dd HH:mm:ss");
	private static OutputStreamWriter writer = null;

	/**
	 * 初始化，创建文件目录
	 *
	 * @param fileName
	 * @return
	 */
	private static boolean initWriter(String fileName) {
		File fileDir = new File(path);
		boolean mk = false;
		if (!fileDir.exists()) {
			mk = fileDir.mkdirs();
		}
		File file;//log file in sdcard
		file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			if (System.currentTimeMillis() - file.lastModified() > DAY * 28L) {
				file.createNewFile();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file, true));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static int count = 1;

	/**
	 * 输出到文件，大于1Mb时创建子文件
	 *
	 * @param msg
	 */
	private static void wirte2file(final String msg) {
		if (fixedThreadPool == null) {
			fixedThreadPool = Executors.newFixedThreadPool(3);
		}

		fixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {

				boolean result = false;
				String fileName = getPath();
				if (writer == null) {
					result = initWriter(fileName);
					if (!result) {
						saveLog = false;
						return;
					}
				}
				File file = new File(path, fileName);
				if (file.length() > LOG_FILE_MAX_SIZE) {
					initWriter(fileName + "_" + count);
					count += 1;
				}
				try {
					if (result) {
						print2File(getHeadInfo());
					}
					Date time = new Date();
					print2File(myLogSdf.format(time) + " : " + msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void print2File(String msg) throws IOException {
		writer.write(msg);
		writer.write("\n");
		writer.flush();
	}


	/**
	 * 打印头信息
	 *
	 * @return
	 */
	private static String getHeadInfo() {
		if (!saveLog) return "";
		StringBuilder sb = new StringBuilder();
		sb.append("\n ********************************* Log Head **************************\n\t设备厂商:  ");
		sb.append(Build.MANUFACTURER);//  设备厂商
		sb.append("\n\t设备型号：");
		sb.append(Build.MODEL);
		sb.append("\n\t安卓SDK：");
		sb.append(Build.VERSION.SDK);
		sb.append("\n\t系统版本：");
		sb.append(Build.VERSION.RELEASE);

		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo version = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			sb.append("\n\t版本信息：");
			sb.append(version.versionName);
			sb.append(".");
			sb.append(version.versionCode);
			sb.append("\n\t打印时间： ");
			sb.append(new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA).format(new Date()));//添加时间
			sb.append("\n *******************************  Log Head *************************** \n");
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}


	private static void zipLog() {
		File zipfile = new File(path.substring(0,path.lastIndexOf(File.separator)), "Log.zip");
		Log.i("aaa",""+zipfile.getAbsolutePath());
		if (zipfile.exists()) {
			zipfile.delete();
		}
		try {
			boolean rs = zipfile.createNewFile();
			Log.i("create dir ", "---------" + rs);
		} catch (IOException e) {
			e.printStackTrace();
		}

		File fileParent = new File(path);

		File[] list = fileParent.listFiles();

		try {
			ZipUtils.zipFiles(Arrays.asList(list), zipfile, "日志压缩");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void UnZipLog() {
		File zipfile = new File(path.substring(0,path.lastIndexOf(File.separator)), "Log.zip");
		File unFile = new File(path);
		try {
			boolean zipRS = ZipUtils.unzipFile(zipfile, unFile.getParentFile());
			Log.i("TAG", "zip rs " + zipRS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 *  在子线程中把日志打包，然后可以通过邮件发送
	 * @param activity
	 */
	public static void reportBug(final Context activity) throws ActivityNotFoundException {
		fixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				zipLog();
				File zipfile = new File(path.substring(0,path.lastIndexOf(File.separator)), "Log.zip");
				Intent intent = new Intent(Intent.ACTION_SEND);
				String[] tos = {"2087109609@qq.com"};    //接收
				String[] ccs = {"sgn5200@sina.com"};    //抄送
				intent.putExtra(Intent.EXTRA_EMAIL, tos);
				intent.putExtra(Intent.EXTRA_CC, ccs);
				intent.putExtra(Intent.EXTRA_TEXT, getHeadInfo());
				intent.putExtra(Intent.EXTRA_SUBJECT, "发送日志");

				Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", zipfile);
				intent.putExtra(Intent.EXTRA_STREAM, photoURI);
				intent.setType("application/octet-stream");
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Intent.createChooser(intent, "选择发送方式");
				activity.startActivity(intent);
			}
		});

	}
}
