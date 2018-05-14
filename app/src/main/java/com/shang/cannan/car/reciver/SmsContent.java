package com.shang.cannan.car.reciver;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.lansent.cannan.util.Log;
import com.lansent.cannan.util.RxEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/8     13:22
 * Project          : Car
 * PackageName :  com.shang.cannan.car.reciver;
 */

public class SmsContent extends ContentObserver {
	private Cursor cursor = null;
	private Context context;

	public SmsContent(Handler handler, Context context) {
		super(handler);
		this.context = context;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Log.i("SMSTest", "Begin");

		//读取收件箱中指定号码的短信
//  cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read", "body"},
//    " address=? and read=?", new String[]{"10086", "0"}, "_id desc");//按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了

		cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read", "body"},
				null, null, "_id desc");
		if (cursor == null) {
			return;
		}

		Log.i("SMSTest", "cursor.isBeforeFirst(): " + cursor.isBeforeFirst() + " cursor.getCount(): " + cursor.getCount());
		if (cursor != null && cursor.getCount() > 0) {

			cursor.moveToFirst();
			int smsbodyColumn = cursor.getColumnIndex("body");
			String smsBody = cursor.getString(smsbodyColumn);
			Log.i("SMSTest", "smsBody = " + smsBody);
			String sms = getDynamicPassword(smsBody);
			RxEvent.getInstance().post(sms);
		}
	}


	private String getDynamicPassword(String str) {
		Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
		Matcher m = continuousNumberPattern.matcher(str);
		String dynamicPassword = "";
		while(m.find()){
			if(m.group().length() == 6) {
				System.out.print(m.group());
				dynamicPassword = m.group();
			}
		}
		return dynamicPassword;
	}
}
