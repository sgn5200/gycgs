package com.shang.cannan.car.person;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/10     9:39
 * Project          : Car
 * PackageName :  com.shang.cannan.car.person;
 */

public interface  PersonView {

	/**
	 * 显示对话框管理
	 * @param show true显示
	 */
	 public void showDialog(boolean show);


	/**
	 * 撤销填报
	 * @param success
	 */
	 public void loadCode(boolean success,byte[] bytes);

	public void query(boolean success,String msg);


}
