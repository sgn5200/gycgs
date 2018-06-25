package com.shang.cannan.car.ocr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.lansent.cannan.base.AbsBaseActivity;
import com.lansent.cannan.util.Log;
import com.shang.cannan.car.R;
import com.shang.cannan.car.util.CustomProgress;

import java.io.File;
import java.util.Date;

public class OcrActivity extends AbsBaseActivity implements View.OnClickListener, SurfaceHolder.Callback, Camera.PictureCallback {
	public static final int REQUEST_CODE_GALLERY = 110;
	private File mFileTemp = null;
	private AlertDialog.Builder alertDialog;
	private boolean hasGotToken;
	private String ak = "sQ5LXek5kg4CzaUiLiRRZknD";
	private String sk = "mmoca6YfMYSOuyuzMENHMX9WoEBMXBkB";
	private SurfaceView surfaceView;
	private Camera camera;
	private Camera.AutoFocusCallback myAutoFocusCallback;
	private ImageView ivPhoto;
	private View viewDo, viewResult;
	private String path;
	private ClipboardManager mClipboardManager;
	private CustomProgress customPro;
	private Spinner spinner;

	@Override
	public int getLayout() {
		return R.layout.activity_ocr;
	}

	@Override
	public void initViews() {
		mFileTemp = new File(getFilesDir(), new Date().getTime() + ".jpg");
		View left = getView(R.id.topLeftIv);
		left.setVisibility(View.VISIBLE);
		left.setOnClickListener(this);
		TextView tvTitle = getView(R.id.topTitleTv);
		tvTitle.setText("智能识别");
		surfaceView = getView(R.id.surfView);
		viewDo = getView(R.id.viewDo);
		viewResult = getView(R.id.viewResult);
		final SurfaceHolder holder = surfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this);
		ivPhoto = getView(R.id.ivPhoto);
		spinner = getView(R.id.spinner);
		initListener(this, R.id.album_button, R.id.take_photo_button, R.id.light_button);
		initListener(this, R.id.confirm_button, R.id.cancel_button);
		mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		//自动聚焦变量回调
		myAutoFocusCallback = new Camera.AutoFocusCallback() {
			public void onAutoFocus(boolean success, Camera camera) {
				if (success)//success表示对焦成功
				{
					Log.i(TAG, "onAutoFocus succeed...");
					camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
					doAutoFocus();
				} else {
					Log.i(TAG, "onAutoFocus failed...");
				}
			}
		};
	}


	Camera.Parameters parameters;

	//设置聚焦
	private void doAutoFocus() {
		parameters = camera.getParameters();
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		camera.setParameters(parameters);
		camera.autoFocus(new Camera.AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if (success) {
					camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
					if (!Build.MODEL.equals("KORIDY H30")) {
						parameters = camera.getParameters();
						parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续自动对焦
						camera.setParameters(parameters);
					} else {
						parameters = camera.getParameters();
						parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
						camera.setParameters(parameters);
					}
				}
			}
		});
	}

	public void setCameraOrientation(int cameraId, Camera camera) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				break;
			case Surface.ROTATION_270:
				degrees = 270;
				break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.topLeftIv:
				finish();
				break;
			case R.id.album_button:
				camera.stopPreview();
				viewDo.setVisibility(View.GONE);
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
				break;
			case R.id.take_photo_button:
				camera.takePicture(null, null, null, this);
				break;
			case R.id.light_button:
				Camera.Parameters parameters = camera.getParameters();
				parameters.setFlashMode("off".equals(parameters.getFlashMode()) ? Camera.Parameters.FLASH_MODE_TORCH: "off" );
				camera.setParameters(parameters);
				break;
			case R.id.cancel_button:
				ivPhoto.setVisibility(View.GONE);
				surfaceView.setVisibility(View.VISIBLE);
				viewResult.setVisibility(View.GONE);
				viewDo.setVisibility(View.VISIBLE);
				camera.startPreview();
				break;
			case R.id.confirm_button:
				String msg = (String) spinner.getSelectedItem();
				customPro = CustomProgress.show(this, msg , false, null);
				recFile(spinner.getSelectedItemPosition());
				break;
		}
	}

	private void recFile(int position) {
		switch (position) {
			case 0:
				recGeneralBasic(this, path);
				break;
			case 1:
				recWebimage(this, path);
				break;
			case 2:
				recBankCard(this, path);
				break;
			case 3:
				recReceipt(this, path);
				break;
			case 4:
				recVatInvoice(this, path);
				break;
			case 5:
				recNumbers(this, path);
				break;
			case 6:
				recIDCard(true, path);
				break;
			case 7:
				recIDCard(false, path);
				break;
		}
	}

	/**
	 * 通用文字识别
	 *
	 * @param ctx
	 * @param filePath
	 */
	public void recGeneralBasic(Context ctx, String filePath) {
		GeneralBasicParams param = new GeneralBasicParams();
		param.setImageFile(new File(filePath));
		OCR.getInstance(ctx).recognizeGeneralBasic(param, new OnResultListener<GeneralResult>() {
			@Override
			public void onResult(GeneralResult result) {
				StringBuilder sb = new StringBuilder();
				for (WordSimple wordSimple : result.getWordList()) {
					WordSimple word = wordSimple;
					sb.append(word.getWords());
					sb.append("\n");
				}
				alertText("识别成功", sb.toString());
			}

			@Override
			public void onError(OCRError error) {
				alertText("识别出错", error.getMessage());
			}
		});
	}

	/**
	 * 网络图片识别
	 *
	 * @param ctx
	 * @param filePath
	 */
	public void recWebimage(Context ctx, String filePath) {
		GeneralBasicParams param = new GeneralBasicParams();
		param.setDetectDirection(true);
		param.setImageFile(new File(filePath));
		OCR.getInstance(ctx).recognizeWebimage(param, new OnResultListener<GeneralResult>() {
			@Override
			public void onResult(GeneralResult result) {
				StringBuilder sb = new StringBuilder();
				for (WordSimple wordSimple : result.getWordList()) {
					WordSimple word = wordSimple;
					sb.append(word.getWords());
					sb.append("\n");
				}
				alertText("识别成功", sb.toString());

			}

			@Override
			public void onError(OCRError error) {
				alertText("识别出错", error.getMessage());
			}
		});
	}


	/**
	 * 银行卡识别
	 *
	 * @param ctx
	 * @param filePath
	 */
	public void recBankCard(Context ctx, String filePath) {
		BankCardParams param = new BankCardParams();
		param.setImageFile(new File(filePath));
		OCR.getInstance(ctx).recognizeBankCard(param, new OnResultListener<BankCardResult>() {
			@Override
			public void onResult(BankCardResult result) {
				String res = String.format("卡号：%s\n类型：%s\n发卡行：%s",
						result.getBankCardNumber(),
						result.getBankCardType().name(),
						result.getBankName());
				alertText("识别成功",result.getBankCardNumber());
			}

			@Override
			public void onError(OCRError error) {
				alertText("识别出错", error.getMessage());
			}
		});
	}

	/**
	 * 通用票据
	 *
	 * @param ctx
	 * @param filePath
	 */
	public void recReceipt(Context ctx, String filePath) {
		OcrRequestParams param = new OcrRequestParams();
		param.setImageFile(new File(filePath));
		param.putParam("detect_direction", "true");
		OCR.getInstance(ctx).recognizeReceipt(param, new OnResultListener<OcrResponseResult>() {
			@Override
			public void onResult(OcrResponseResult result) {
				alertText("识别成功", result.getJsonRes());
			}

			@Override
			public void onError(OCRError error) {
				alertText("识别出错", error.getMessage());
			}
		});
	}

	/**
	 * 增值税识别
	 *
	 * @param ctx
	 * @param filePath
	 */
	public void recVatInvoice(Context ctx, String filePath) {
		OcrRequestParams param = new OcrRequestParams();
		param.setImageFile(new File(filePath));
		OCR.getInstance(ctx).recognizeVatInvoice(param, new OnResultListener<OcrResponseResult>() {
			@Override
			public void onResult(OcrResponseResult result) {
				alertText("识别成功", result.getJsonRes());
			}

			@Override
			public void onError(OCRError error) {
				alertText("识别出错", error.getMessage());
			}
		});
	}

	/**
	 * 数字识别
	 *
	 * @param ctx
	 * @param filePath
	 */
	public void recNumbers(Context ctx, String filePath) {
		OcrRequestParams param = new OcrRequestParams();
		param.setImageFile(new File(filePath));
		OCR.getInstance(ctx).recognizeNumbers(param, new OnResultListener<OcrResponseResult>() {
			@Override
			public void onResult(OcrResponseResult result) {
				alertText("识别成功", result.getJsonRes());
			}

			@Override
			public void onError(OCRError error) {
				alertText("识别出错", error.getMessage());
			}
		});
	}

	/**
	 * 身份证识别
	 *
	 * @param isFront  身份证正面
	 * @param filePath
	 */
	private void recIDCard(boolean isFront, String filePath) {
		IDCardParams param = new IDCardParams();
		param.setImageFile(new File(filePath));
		// 设置身份证正反面
		param.setIdCardSide(isFront ? "front" : "back");
		// 设置方向检测
		param.setDetectDirection(true);
		// 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
		param.setImageQuality(20);
		OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
			@Override
			public void onResult(IDCardResult result) {
				if (result != null && result.getName()!=null) {
					alertText("识别成功", result.getName().getWords()+"\n"+result.getIdNumber());
				}
			}

			@Override
			public void onError(OCRError error) {
				alertText("失败失败", error.getMessage());
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alertDialog = new AlertDialog.Builder(this);
		initAccessTokenWithAkSk(ak, sk);
//		initAccessToken();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (camera != null)
			camera.release();
	}

	/**
	 * 以license文件方式初始化
	 */
	private void initAccessToken() {
		OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
			@Override
			public void onResult(AccessToken accessToken) {
				String token = accessToken.getAccessToken();
				hasGotToken = true;
				alertText(" 获取token成功", token);

			}

			@Override
			public void onError(OCRError error) {
				error.printStackTrace();
				alertText("licence方式0token失败", error.getMessage());
			}
		}, getApplicationContext());
	}

	/**
	 * 用明文ak，sk初始化
	 */
	private void initAccessTokenWithAkSk(String ak, String sk) {
		OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
			@Override
			public void onResult(AccessToken result) {
				String token = result.getAccessToken();
				hasGotToken = true;
			}

			@Override
			public void onError(OCRError error) {
				error.printStackTrace();
				alertText("AK，SK方式获取token失败", error.getMessage());
			}
		}, getApplicationContext(), ak, sk);
	}

	private void alertText(final String title, final String message) {
		Log.i(TAG, message);
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (customPro != null) {
					customPro.dismiss();
				}
				alertDialog.setTitle(title)
						.setMessage(message)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (!hasGotToken) {
									finish();
								}
								ClipData mClipData = ClipData.newPlainText("ocr", message);
								mClipboardManager.setPrimaryClip(mClipData);
								dialog.dismiss();
							}
						})
						.show();
			}
		});
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 当Surface被创建的时候，该方法被调用，可以在这里实例化Camera对象
		//同时可以对Camera进行定制
		camera = Camera.open(); //获取Camera实例
		setCameraOrientation(0, camera);

		/**
		 * Camera对象中含有一个内部类Camera.Parameters.该类可以对Camera的特性进行定制
		 * 在Parameters中设置完成后，需要调用Camera.setParameters()方法，相应的设置才会生效
		 * 由于不同的设备，Camera的特性是不同的，所以在设置时，需要首先判断设备对应的特性，再加以设置
		 * 比如在调用setEffects之前最好先调用getSupportedColorEffects。如果设备不支持颜色特性，那么该方法将
		 * 返回一个null
		 */
		try {

			Camera.Parameters param = camera.getParameters();
			//如果是竖屏
			param.set("orientation", "portrait");
			//在2.2以上可以使用
			//camera.setDisplayOrientation(90);
			Display display = getWindowManager().getDefaultDisplay();
			int screenWidth = display.getWidth();
			int screenHeight = display.getHeight();
			holder.setFixedSize(screenWidth, screenHeight);
//			param.setPictureSize(screenWidth, screenHeight);
			camera.setPreviewDisplay(holder);
			camera.setParameters(param);
		} catch (Exception e) {
			// 如果出现异常，则释放Camera对象
			camera.release();
		}
		//启动预览功能
		camera.startPreview();
		camera.autoFocus(myAutoFocusCallback);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 当Surface被销毁的时候，该方法被调用
		//在这里需要释放Camera资源
		camera.stopPreview();
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		Bitmap newBit = rotateBitmap(bitmap);
		Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), newBit, null, null));
		startPhotoZoom(uri);
//	    ivPhoto.setImageBitmap(newBit);
		ivPhoto.setVisibility(View.VISIBLE);
		surfaceView.setVisibility(View.GONE);
	}


	public void startPhotoZoom(Uri uri) {
		Log.i(TAG, "uri == " + uri.toString());
		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
		intent.setType("image/*");
		intent.setData(uri);
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		//该参数可以不设定用来规定裁剪区的宽高比
////		intent.putExtra("aspectX", 2);
////		intent.putExtra("aspectY", 1);
//		//该参数设定为你的imageView的大小
////		intent.putExtra("outputX", 600);
////		intent.putExtra("outputY", 300);
//		intent.putExtra("scale", true);
//		//是否返回bitmap对象
//		intent.putExtra("return-data", true);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileTemp);
////		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片的格式
//		intent.putExtra("noFaceDetection", false); // 头像识别
		startActivityForResult(intent, 3);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			camera.stopPreview();
			viewDo.setVisibility(View.GONE);
			surfaceView.setVisibility(View.GONE);
			ivPhoto.setVisibility(View.VISIBLE);
			viewResult.setVisibility(View.VISIBLE);
			switch (requestCode) {
				case REQUEST_CODE_GALLERY: //从相册返回照片Uri
					Uri uri = data.getData();
					startPhotoZoom(uri);
					break;
				case 3:
					if (data.getData() != null) {
						path = getRealPathFromURI(data.getData());
						Bitmap bitmapGa = BitmapFactory.decodeFile(path);                                  //path转bitmap
						ivPhoto.setImageBitmap(bitmapGa);
					} else {
						Log.i(TAG, "data.getdata is null");
					}
					break;
			}
		} else {
			camera.startPreview();
			surfaceView.setVisibility(View.VISIBLE);
			ivPhoto.setVisibility(View.GONE);
			viewDo.setVisibility(View.VISIBLE);
			viewResult.setVisibility(View.GONE);
		}
	}


	private String getRealPathFromURI(Uri contentURI) {
		String result;
		Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}

	int degrees = 0;

	/**
	 * 获取选择后的图片
	 */
	public Bitmap rotateBitmap(Bitmap b) {
		degrees += 90;
		Log.i("-------", "------------?" + degrees);
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				Log.e("--", "内存溢出" + ex.getMessage());
			}
		}
		return b;
	}

}
