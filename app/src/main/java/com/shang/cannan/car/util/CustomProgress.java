package com.shang.cannan.car.util;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/10     9:50
 * Project          : Car
 * PackageName :  com.shang.cannan.car.util;
 */


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.shang.cannan.car.R;


public class CustomProgress extends Dialog {

	public CustomProgress(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 弹出自定义ProgressDialog
	 *
	 * @param context        上下文
	 * @param message        提示
	 * @param cancelable     是否按返回键取消
	 * @param cancelListener 按下返回键监听
	 * @return
	 */
	public static CustomProgress show(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
		CustomProgress dialog = new CustomProgress(context, R.style.Custom_Progress);
		dialog.setTitle("");
		dialog.setContentView(R.layout.m_dialog_show);
		TextView txt = (TextView) dialog.findViewById(R.id.message);
		txt.setText(message);
		// 按返回键是否取消
		dialog.setCancelable(cancelable);
		// 监听返回键处理
		dialog.setOnCancelListener(cancelListener);
		// 设置居中
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// 设置背景层透明度
		lp.dimAmount = 0.6f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		try {
			dialog.show();
		} catch (Exception e) {
			Log.e("---", "网络加载窗口泄露");
		}
		return dialog;
	}

}
