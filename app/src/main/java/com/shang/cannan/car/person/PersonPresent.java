package com.shang.cannan.car.person;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.lansent.cannan.api.ApiManager;
import com.lansent.cannan.api.Method;
import com.lansent.cannan.api.URLParam;
import com.lansent.cannan.util.Log;
import com.shang.cannan.car.util.UrlConstant;
import com.shang.cannan.car.vo.OwnerVo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/10     9:39
 * Project          : Car
 * PackageName :  com.shang.cannan.car.person;
 */

public class PersonPresent {
	private String TAG = getClass().getSimpleName();
	private final String token;
	private PersonView personView;
	private Context context;
	private List<Disposable> listDis = new ArrayList<>();

	public PersonPresent(PersonView personView, Context context, String token) {
		this.context = context;
		this.personView = personView;
		this.token = token;
	}

	/**
	 * 释放
	 */
	public void onRelease() {

	}


	public void query(OwnerVo vo, String code) {
		URLParam param = new URLParam(UrlConstant.URL_Query);
		param.setMethod(Method.POST);
		param.addParam("IdentCode", vo.getIdentCode());
		param.addParam("OwnerType", vo.getOwnerType() + "");
		param.addParam("CardType", vo.getCardType() + "");
		param.addParam("CardCode", vo.getCardCode());
		param.addParam("securityCode", code);

		personView.showDialog(true);
		ApiManager.getInstance().requestDefault(param, context, true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						personView.showDialog(false);
						String rs = responseBody.string();
						personView.query(true, rs);
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						personView.showDialog(false);
						personView.query(false, "未知错误" + throwable.getMessage());
					}
				});

	}

	public void loadCode() {
		URLParam param = new URLParam(UrlConstant.URL_CODE);
		param.setMethod(Method.GET);
		ApiManager.getInstance().requestDefault(param, context, true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						personView.loadCode(true, responseBody.bytes());
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						throwable.printStackTrace();
						personView.loadCode(false, null);
					}
				});
	}

	/**
	 * 灰度化处理
	 *
	 * @param bitmap3
	 * @return
	 */
	public Bitmap convertGray(Bitmap bitmap3) {
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
		Paint paint = new Paint();
		paint.setColorFilter(filter);
		Bitmap result = Bitmap.createBitmap(bitmap3.getWidth(), bitmap3.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(bitmap3, 0, 0, paint);
		return result;
	}

	private  Bitmap suffleImageRGB(Bitmap bmp) {
		Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int color = bmp.getPixel(x, y);
				Log.i(TAG,"color "+color);
				if(color!= -16777216)
				result.setPixel(x, y, color);
			}
		}
		return  result ;
	}

}
