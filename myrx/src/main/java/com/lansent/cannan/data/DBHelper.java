package com.lansent.cannan.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.lansent.cannan.util.Log;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Cancellable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Cannan on 2017/7/28 0028.
 * sqlite 数据库帮助类
 */

public class DBHelper extends SQLiteOpenHelper {
	// 当前数据库版本号
	private final static int VERSION = 1;

	private SQLiteDatabase database;

	private List<String> arrayTableSql;
	private String TAG = getClass().getSimpleName();

	/**
	 * 构造方法，
	 *
	 * @param context       用于创建数据库和打开数据库
	 * @param name          数据库名字
	 * @param arrayTableSql 数据库中的创建表的语句集合
	 */
	public DBHelper(Context context, String name, List<String> arrayTableSql) {
		this(context, name, null, VERSION);
		this.arrayTableSql = arrayTableSql;
	}

	public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 获取数据库对象，自行处理
	 *
	 * @return
	 */
	public SQLiteDatabase getDatabase() {
		return open();
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		//创建表
		if (arrayTableSql != null) {
			for (String crateTableSql : arrayTableSql) {
				sqLiteDatabase.execSQL(crateTableSql);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}


	/**
	 * 执行SQL语句
	 * @param sql      语句
	 * @param bindArgs sql语句中代替？
	 */
	public synchronized void execSQL(String sql, Object[] bindArgs) {
		SQLiteDatabase database = open();
		database.execSQL(sql, bindArgs);
		database.close();
	}


	/**
	 * 异步查询
	 * @param sql      查询语句
	 * @param bindArgs sql语句中代替？
	 * @return Cursor 返回原生游标
	 */
	public synchronized Flowable<Cursor> rawQuery(final String sql, final String[] bindArgs) {
		Flowable<Cursor> fb = Flowable.create(new FlowableOnSubscribe<Cursor>() {
			@Override
			public void subscribe(FlowableEmitter<Cursor> e) throws Exception {
				open();
				final Cursor cursor = database.rawQuery(sql, bindArgs);
				e.onNext(cursor);
				e.setCancellable(new Cancellable() {
					@Override
					public void cancel() throws Exception {
						Log.i(TAG, "cancel");
						cursor.close();
						close();
					}
				});
			}
		}, BackpressureStrategy.ERROR);

		fb.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
		return fb;
	}

	/**
	 * 异步增加
	 * @param table 表名
	 * @param contentValues 设定文件
	 * @return void 返回类型null
	 */
	public synchronized void insert(final String table, final ContentValues contentValues) {
		Flowable<Boolean> fb = Flowable.create(new FlowableOnSubscribe<Boolean>() {
			@Override
			public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
				open();
				database.insert(table, null, contentValues);
				e.setCancellable(new Cancellable() {
					@Override
					public void cancel() throws Exception {
						Log.i(TAG, "cancle");
						close();
					}
				});
			}
		}, BackpressureStrategy.ERROR);
		fb.subscribeOn(Schedulers.io()).subscribe();
	}

	/**
	 * 异步更改
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs   设定文件
	 * @return void 返回类型
	 */
	public synchronized void update(final String table, final ContentValues values, final String whereClause, final String[] whereArgs) {
		Flowable<Boolean> fb = Flowable.create(new FlowableOnSubscribe<Boolean>() {
			@Override
			public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
				open();
				database.beginTransaction();
				database.update(table, values, whereClause, whereArgs);
				database.setTransactionSuccessful();
				database.endTransaction();
				e.setCancellable(new Cancellable() {
					@Override
					public void cancel() throws Exception {
						Log.i(TAG, "cancle");
						close();
					}
				});
			}
		}, BackpressureStrategy.ERROR);
		fb.subscribeOn(Schedulers.io()).subscribe();
	}

	/**
	 * 异步删除
	 * @param table       表名
	 * @param whereClause 删除的条件
	 * @param whereArgs   条件中的？代替值
	 */
	public synchronized void delete(final String table, final String whereClause, final String[] whereArgs) {
		Flowable<Boolean> fb = Flowable.create(new FlowableOnSubscribe<Boolean>() {
			@Override
			public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
				open();
				database.beginTransaction();
				database.delete(table, whereClause, whereArgs);
				database.setTransactionSuccessful();
				database.endTransaction();
				e.setCancellable(new Cancellable() {
					@Override
					public void cancel() throws Exception {
						Log.i(TAG, "cancle");
						close();
					}
				});
			}
		}, BackpressureStrategy.ERROR);
		fb.subscribeOn(Schedulers.io()).subscribe();
	}

	/**
	 * 打开数据库
	 * 防止sqlite异常和死锁
	 *
	 * @return 数据库对象   {@Link SQLiteDatabase}
	 */
	private SQLiteDatabase open() {
		if (database == null || !database.isOpen()) {
			try {
				database = getWritableDatabase();   //同时具备读和写的功能，当磁盘满后调用会异常
			} catch (SQLiteException e) {
				e.printStackTrace();
				database = getReadableDatabase();   //具备只读功能
			}
		}
		return database;
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (null != database && database.isOpen()) {
			database.close();
			database = null;
		}
	}
}
