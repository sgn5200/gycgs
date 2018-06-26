package com.shang.cannan.car.person;

import android.app.AlertDialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lansent.cannan.base.AbsBaseActivity;
import com.shang.cannan.car.MyApp;
import com.shang.cannan.car.R;
import com.shang.cannan.car.adapter.PersonAdapter;
import com.shang.cannan.car.dao.OwnerDao;
import com.shang.cannan.car.util.MyListView;
import com.shang.cannan.car.util.SystemStatusManager;
import com.shang.cannan.car.vo.OwnerVo;

import java.util.List;

/**
 * @Desc : 所有客户
 * @Params :
 * @Author : Cannan
 * @Date : 2018/5/9
 */
public class PersonActivity extends AbsBaseActivity implements View.OnClickListener {

	private List<OwnerVo> list;
	private MyListView lvPerson;
	private PersonAdapter adapter;
	private OwnerVo longClick;
	private AlertDialog.Builder builder;
	private CheckBox checkBox;//预约显示

	@Override
	public int getLayout() {
		return R.layout.activity_person;
	}

	@Override
	public void initViews() {
		initWindow();
		View left = getView(R.id.topLeftIv);
		left.setVisibility(View.VISIBLE);
		left.setOnClickListener(this);
		TextView tv = getView(R.id.topTitleTv);

		lvPerson = getView(R.id.lvPerson);
		adapter = new PersonAdapter(this);
		lvPerson.setAdapter(adapter);
		list = OwnerDao.getInstance(MyApp.helper).getAll();

		checkBox = getView(R.id.topRightIv);
		adapter.setDaata(list);
		builder = new AlertDialog.Builder(this);


		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					list = OwnerDao.getInstance(MyApp.helper).getAll("1");
				}else{
					list = OwnerDao.getInstance(MyApp.helper).getAll("0");
				}
				adapter.setDaata(list);
			}
		});

		tv.setText("客户列表 "+list.size());
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		list = OwnerDao.getInstance(MyApp.helper).getAll();
		adapter.setDaata(list);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.topLeftIv:
				finish();
				break;
		}
	}
}
