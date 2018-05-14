package com.shang.cannan.car.dao;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/9     15:25
 * Project          : Car
 * PackageName :  com.shang.cannan.car.dao;
 */

public class SqlConstant {

	/**
	 *
	 * 	private String  __RequestVerificationToken;  //令牌
	 private int CarServiceNo;                             //业务类型
	 private int NumberType;                              //车辆类型
	 private String IdentCode;                            //识别代号
	 private String OwnerName;                         //名字
	 private int OwnerType;                              //1个人 2 单位
	 private int CardType;                          // 证件号类型 身份证 1   机构2  其他3
	 private String CardCode;
	 */
	public static String SQL_TABLE_OWNER = "table_owner";
	public static String SQL_OWNER_CREATE = "create table if not exists table_owner( " +
			"CarServiceNo int," +
			"NumberType int," +
			"IdentCode char(50)," +
			"OwnerName char(20),"+
			"OwnerType int,"+
			"CardType int,"+
			"updateStatus int,"+
			"createTime char(30),"+
			"okStatus int,"+
			"CardCode char(50)  primary key"+
			" );";
}
