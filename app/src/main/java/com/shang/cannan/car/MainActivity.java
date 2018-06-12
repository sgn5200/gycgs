package com.shang.cannan.car;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lansent.cannan.api.ApiManager;
import com.lansent.cannan.api.Method;
import com.lansent.cannan.api.URLParam;
import com.lansent.cannan.base.AbsBaseActivity;
import com.lansent.cannan.util.Log;
import com.lansent.cannan.util.SharePreUtils;
import com.lansent.cannan.util.SimpleUtil;
import com.shang.cannan.car.adapter.MayAdapter;
import com.shang.cannan.car.adapter.MySpAdapter;
import com.shang.cannan.car.input.InputActivity;
import com.shang.cannan.car.login.LoginActivity;
import com.shang.cannan.car.maybreak.MayBreakActivity;
import com.shang.cannan.car.person.PersonActivity;
import com.shang.cannan.car.query.BreakQueryActivity;
import com.shang.cannan.car.util.CustomProgress;
import com.shang.cannan.car.util.SystemStatusManager;
import com.shang.cannan.car.util.UrlConstant;
import com.shang.cannan.car.vo.MayInfoVo;
import com.shang.cannan.car.vo.SiteVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class MainActivity extends AbsBaseActivity implements View.OnClickListener {


	/**
	 * UI
	 */
	private ListView lvMay;
	private DrawerLayout drawerLayout;
	private NavigationView navigationView;
	private ImageView menu;
	private Spinner spOptin, spCar, spSite;

	private int indexOp, indexCar;
	String indexSite;
	private List<SiteVo> listSite;
	private MySpAdapter siteAdapter;
	private MayAdapter mayAdapter;
	private float x1;
	private float x2;

	@Override
	public int getLayout() {
		return R.layout.activity_main;
	}

	@Override
	public void initViews() {
		initWindow();
		lvMay = getView(R.id.lvMay);
		spOptin = getView(R.id.spOp);
		spCar = getView(R.id.spCar);
		spSite = getView(R.id.spSite);

		TextView tvTitle = getView(R.id.topTitleTv);
		tvTitle.setText("车辆预约");
		drawerLayout = getView(R.id.activity_na);
		navigationView = getView(R.id.nav);
		menu = getView(R.id.topRightIv);
		menu.setVisibility(View.VISIBLE);
		View headerView = navigationView.getHeaderView(0);//获取头布局
		TextView tvVersion = headerView.findViewById(R.id.tvVersion);
		String version = SimpleUtil.getAppVersion(this);
		tvVersion.setText(version);
		menu.setOnClickListener(this);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				//item.setChecked(true);
				String itemStr =   item.getTitle().toString();
				Bundle bundle = new Bundle();
				switch (itemStr){
					case "信息填报":
						lunchActivity(InputActivity.class,null,false);
						break;
					case "所有客户":
						lunchActivity(PersonActivity.class,null,false);
						break;
					case "切换账号":
						SharePreUtils.clearAllConfig(MainActivity.this);
						lunchActivity(LoginActivity.class,null,false);
						break;
					case "预约查询":
						bundle.putInt("type",1);
						bundle.putString("url",UrlConstant.URL_Query);
						lunchActivity(BreakQueryActivity.class,bundle,false);
						break;
					case "一  阳  指":
						MayInfoVo cur = mayAdapter.getItem(0);
						if(cur == null){
							break;
						}
						showToast("谨慎使用，");
						bundle.putBoolean("isAm",false);        //  tvSiteName, tvTimepart, tvDate
						bundle.putString("siteName",cur.getBespeakSiteName());
						bundle.putString("siteNo",cur.getBespeakSiteNo());

						String data = cur.getBespeakDateHtml();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
						try {
							Date newData = sdf.parse(data);
							long lastDay = newData.getTime();
							long newLast = lastDay+86400000;
							String newStrData = sdf.format(new Date(newLast));
							Log.i(TAG,newStrData+"--");
							bundle.putString("date",newStrData);
							lunchActivity(MayBreakActivity.class,bundle,false);
						} catch (ParseException e) {
							e.printStackTrace();
							break;
						}
						break;
				}
				drawerLayout.closeDrawer(navigationView);
				return true;
			}
		});

		initSpinner();


		mayAdapter = new MayAdapter(this);
		mayAdapter.setMayClickListener(new MayAdapter.MayBreakListener() {
			@Override
			public void mayBreak(MayInfoVo vo, boolean am) {
				showToast("可以预约"+(am?vo.getAmMayBespeakNumber():vo.getPmMayBespeakNumber()));
				Bundle bundle = new Bundle();
				bundle.putBoolean("isAm",am);        //  tvSiteName, tvTimepart, tvDate
				bundle.putString("siteName",vo.getBespeakSiteName());
				bundle.putString("siteNo",vo.getBespeakSiteNo());
				bundle.putString("date",vo.getBespeakDateHtml());
				lunchActivity(MayBreakActivity.class,bundle,false);
			}
		});
		lvMay.setAdapter(mayAdapter);
		lvMay.setDivider(new ColorDrawable(0xfb4d4d4d));
		lvMay.setDividerHeight(2);
		lvMay.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.i(TAG,scrollState+"--");
				if(scrollState==0){
					requestCurData() ;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});

		lvMay.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					//当手指按下的时候
					x1 = event.getX();
				}
				if(event.getAction() == MotionEvent.ACTION_UP) {
					//当手指离开的时候
					x2 = event.getX();
					 if(x1 - x2 > 150) {
						 drawerLayout.closeDrawer(navigationView);
						return true;
					} else if(x2 - x1 > 150) {
						 drawerLayout.openDrawer(navigationView);
						return true;
					}
				}
				return false;
			}
		});
	}


	private void initSpinner() {
//		String[] taskArry = getResources().getStringArray(R.array.CarServiceNo); //业务类型
//		 String[] carArry = getResources().getStringArray(R.array.NumberType);//车辆类型
		spOptin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				indexOp = position;
				getSiteList();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		spCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				indexCar = position;
				getSiteList();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		siteAdapter = new MySpAdapter(this);
		spSite.setAdapter(siteAdapter);
		spSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				indexSite = siteAdapter.getItem(position).getBespeakSiteNo();
				requestCurData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		spOptin.setSelection(1);
		spCar.setSelection(1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.topRightIv://点击菜单，跳出侧滑菜单
				if (drawerLayout.isDrawerOpen(navigationView)) {
					drawerLayout.closeDrawer(navigationView);
				} else {
					drawerLayout.openDrawer(navigationView);
				}
				break;
		}
	}

	private void requestCurData() {
		if (TextUtils.isEmpty(indexSite) || indexOp == 0 || indexCar == 0) {
			showToast("请选择服务站点后重试");
			return;
		}

		if(progress.isShowing()){
			return;
		}

		progress= CustomProgress.show(this,"刷新中",true,null);
		StringBuffer buffer = new StringBuffer(UrlConstant.URL_MaySiteList);
		buffer.append("?bespeakServiceNo=")
				.append(indexOp)
				.append("&numberType=")
				.append(indexCar)
				.append("&bespeakSiteNo=")
				.append(indexSite);
		Log.i(TAG,buffer.toString());
		URLParam param = new URLParam(buffer.toString());
		param.setMethod(Method.POST);
		ApiManager.getInstance().requestDefault(param,this,true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						 String rs = responseBody.string();
						 Gson gson = new Gson();
						 TypeToken<List<MayInfoVo>> token = new TypeToken<List<MayInfoVo>>(){};
						 List<MayInfoVo> list = gson.fromJson(rs,token.getType());
						mayAdapter.setData(list);
						if(progress!=null){
							progress.dismiss();
						}
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						showToast(throwable.getMessage());
						if(progress!=null){
							progress.dismiss();
						}
					}
				});
	}


	private void initWindow() {//初始化窗口属性，让状态栏和导航栏透明
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setTintColor(getResources().getColor(R.color.colorAccent));
		tintManager.setStatusBarTintEnabled(true);
	}


	/**
	 * carServiceNo=1&numberType=1
	 */

	CustomProgress progress;
	private void getSiteList() {
		if (indexOp == 0 || indexCar == 0) {
			return;
		}
		progress=CustomProgress.show(this,"刷新中...",false,null);
		URLParam param = new URLParam(UrlConstant.URL_GETSiteList);
		param.setMethod(Method.POST);
		param.addParam("carServiceNo", String.valueOf(indexOp));
		param.addParam("numberType", String.valueOf(indexCar));
		ApiManager.getInstance().requestDefault(param, this, true)
				.subscribe(new Consumer<ResponseBody>() {
					@Override
					public void accept(ResponseBody responseBody) throws Exception {
						String rs = responseBody.string();
						Gson gson = new Gson();
						TypeToken<List<SiteVo>> token = new TypeToken<List<SiteVo>>() {
						};
						listSite = gson.fromJson(rs, token.getType());
						siteAdapter.setData(listSite);
						progress.dismiss();
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						showToast(throwable.getMessage());
						progress.dismiss();
					}
				});
	}

	long curTime;
	@Override
	public void onBackPressed() {
		 if((System.currentTimeMillis())-curTime>2000){
		 	curTime = System.currentTimeMillis();
		 	showToast("再按一次退出");
		 	return;
		 }
		 super.onBackPressed();
	}
}
