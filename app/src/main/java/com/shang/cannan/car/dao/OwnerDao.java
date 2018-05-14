package com.shang.cannan.car.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lansent.cannan.data.DBHelper;
import com.lansent.cannan.util.Log;
import com.shang.cannan.car.vo.OwnerVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/9     15:24
 * Project          : Car
 * PackageName :  com.shang.cannan.car.dao;
 */

public class OwnerDao {
	private DBHelper helper;
	private static  OwnerDao instance;

	private OwnerDao(DBHelper helper){
		this.helper = helper;
	}

	public static OwnerDao getInstance(DBHelper helper){
		  if(null == instance){
		  	instance= new OwnerDao(helper);
		  }
		  return instance;
	}

	/**
	 * 查
	 * 	"CarServiceNo int," +
	 "NumberType int," +
	 "IdentCode char(50)," +
	 "OwnerName char(20),"+
	 "OwnerType int,"+
	 "CardType int,"+
	 "updateStatus int,"+
	 "okStatus int,"+
	 "createTime char(50),"
	 "CardCode char(50)"+
	 */
	public List<OwnerVo> getAll(){
		List<OwnerVo> list;
		String sql = "select * from "+SqlConstant.SQL_TABLE_OWNER +" ORDER BY createTime DESC";
		 Cursor cursor=helper.getDatabase().rawQuery(sql,null);
		 if(cursor==null){
		 	return null;
		 }
		list  = new ArrayList<>();
		 while (cursor.moveToNext()){
		 	OwnerVo vo= new OwnerVo();
		 	vo.setCarServiceNo(cursor.getInt(cursor.getColumnIndex("CarServiceNo")));
		 	vo.setNumberType(cursor.getInt(cursor.getColumnIndex("NumberType")));
		 	vo.setIdentCode(cursor.getString(cursor.getColumnIndex("IdentCode")));
		 	vo.setOwnerName(cursor.getString(cursor.getColumnIndex("OwnerName")));
		 	vo.setOwnerType(cursor.getInt(cursor.getColumnIndex("OwnerType")));
		 	vo.setCardType(cursor.getInt(cursor.getColumnIndex("CardType")));
		 	vo.setUpdateStatus(cursor.getInt(cursor.getColumnIndex("updateStatus")));
		 	vo.setOkStatus(cursor.getInt(cursor.getColumnIndex("okStatus")));
		 	vo.setCardCode(cursor.getString(cursor.getColumnIndex("CardCode")));
		 	vo.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
		 	list.add(vo);
		 }
		 cursor.close();
		 return list;
	}


	public List<OwnerVo> getAll(String status){
		List<OwnerVo> list;
		String sql = "select * from "+SqlConstant.SQL_TABLE_OWNER +"   where okStatus=? ORDER BY createTime DESC";
		Cursor cursor=helper.getDatabase().rawQuery(sql,new String[]{status});
		if(cursor==null){
			return null;
		}
		list  = new ArrayList<>();
		while (cursor.moveToNext()){
			OwnerVo vo= new OwnerVo();
			vo.setCarServiceNo(cursor.getInt(cursor.getColumnIndex("CarServiceNo")));
			vo.setNumberType(cursor.getInt(cursor.getColumnIndex("NumberType")));
			vo.setIdentCode(cursor.getString(cursor.getColumnIndex("IdentCode")));
			vo.setOwnerName(cursor.getString(cursor.getColumnIndex("OwnerName")));
			vo.setOwnerType(cursor.getInt(cursor.getColumnIndex("OwnerType")));
			vo.setCardType(cursor.getInt(cursor.getColumnIndex("CardType")));
			vo.setUpdateStatus(cursor.getInt(cursor.getColumnIndex("updateStatus")));
			vo.setOkStatus(cursor.getInt(cursor.getColumnIndex("okStatus")));
			vo.setCardCode(cursor.getString(cursor.getColumnIndex("CardCode")));
			vo.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
			list.add(vo);
		}
		cursor.close();
		return list;
	}



	public OwnerVo getOwner(String card){
		String sql = "select * from "+SqlConstant.SQL_TABLE_OWNER +"where CardCode=? ORDER BY createTime DESC";
		Cursor cursor=helper.getDatabase().rawQuery(sql,new String[]{card});
		if(cursor==null){
			return null;
		}
		OwnerVo vo= new OwnerVo();
		if (cursor.moveToNext()){
			vo.setCarServiceNo(cursor.getInt(cursor.getColumnIndex("CarServiceNo")));
			vo.setNumberType(cursor.getInt(cursor.getColumnIndex("NumberType")));
			vo.setIdentCode(cursor.getString(cursor.getColumnIndex("IdentCode")));
			vo.setOwnerName(cursor.getString(cursor.getColumnIndex("OwnerName")));
			vo.setOwnerType(cursor.getInt(cursor.getColumnIndex("OwnerType")));
			vo.setCardType(cursor.getInt(cursor.getColumnIndex("CardType")));
			vo.setUpdateStatus(cursor.getInt(cursor.getColumnIndex("updateStatus")));
			vo.setOkStatus(cursor.getInt(cursor.getColumnIndex("okStatus")));
			vo.setCardCode(cursor.getString(cursor.getColumnIndex("CardCode")));
			vo.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
		}
		cursor.close();
		return vo;
	}


	/**
	 * 增
	 */
	 public void insert(OwnerVo vo){
		 SQLiteDatabase db = helper.getDatabase();
		 ContentValues values = new ContentValues();
		values.put("CarServiceNo",vo.getCarServiceNo());
		values.put("NumberType",vo.getNumberType());
		values.put("IdentCode",vo.getIdentCode());
		values.put("OwnerName",vo.getOwnerName());
		values.put("OwnerType",vo.getOwnerType());
		values.put("CardType",vo.getCardType());
		values.put("updateStatus",vo.getUpdateStatus());
		values.put("okStatus",vo.getOkStatus());
		values.put("CardCode",vo.getCardCode());
		values.put("createTime",System.currentTimeMillis());
		 db.beginTransaction();
	 	 db.insert(SqlConstant.SQL_TABLE_OWNER,null,values);
	 	 db.setTransactionSuccessful();
	 	 db.endTransaction();
		 Log.i("cannan",vo.toString()+"  save ok");

	 }


	/**
	 * 改
	 */
	public void update(OwnerVo vo){
		SQLiteDatabase db = helper.getDatabase();
		ContentValues values = new ContentValues();
		values.put("CarServiceNo",vo.getCarServiceNo());
		values.put("NumberType",vo.getNumberType());
		values.put("IdentCode",vo.getIdentCode());
		values.put("OwnerName",vo.getOwnerName());
		values.put("OwnerType",vo.getOwnerType());
		values.put("CardType",vo.getCardType());
		values.put("updateStatus",vo.getUpdateStatus());
		values.put("okStatus",vo.getOkStatus());
		values.put("createTime",System.currentTimeMillis());
		db.beginTransaction();
		db.update(SqlConstant.SQL_TABLE_OWNER,values,"CardCode =? ",new String[]{vo.getCardCode()});
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * 删
	 */
	public void delete(OwnerVo vo){
		SQLiteDatabase db = helper.getDatabase();
		db.beginTransaction();
		db.delete(SqlConstant.SQL_TABLE_OWNER,"CardCode =? ",new String[]{vo.getCardCode()});
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
