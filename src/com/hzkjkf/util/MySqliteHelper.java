package com.hzkjkf.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MySqliteHelper extends SQLiteOpenHelper {

	Context context;
	SQLiteDatabase db;

	public MySqliteHelper(Context context) {
		this(context, SqlInfo.DATA_NAME, null, 7);
		this.context = context;
	}

	/**
	 * @param context
	 * @param name
	 *            数据库名
	 * @param factory
	 * @param version
	 *            数据库版本号
	 */
	public MySqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}

	/**
	 * 创建表格
	 * 
	 * @param db
	 *            数据仓库
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		// 使用DDL语言创建表格
		// Create table 表名 “(_id integer primary key autoincrement, name text )”
		String sql = "create table " + SqlInfo.TABLE_NAME + "(" + SqlInfo.ID
				+ " integer primary key autoincrement, " + SqlInfo.PHONE
				+ " text, " + SqlInfo.STATUS + " text, " + SqlInfo.TEXT
				+ " text, " + SqlInfo.TIME + " text, " + SqlInfo.USERNAME
				+ " text, " + SqlInfo.FROM + " text, " + SqlInfo.SELLERNAME
				+ " text, " + SqlInfo.ISREADED + " text, " + SqlInfo.TYPE
				+ " text)";
		db.execSQL(sql);
		// create table user '(_id integer primary key autoincrement, name text,
		// age text, edu text, email text )'
	}

	/**
	 * 升级数据库
	 * */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		this.db = db;
		String sql = "create table " + SqlInfo.TABLE_NAME + "(" + SqlInfo.ID
				+ " integer primary key autoincrement, " + SqlInfo.PHONE
				+ " text, " + SqlInfo.STATUS + " text, " + SqlInfo.TEXT
				+ " text, " + SqlInfo.TIME + " text, " + SqlInfo.USERNAME
				+ " text, " + SqlInfo.FROM + " text, " + SqlInfo.SELLERNAME
				+ " text, " + SqlInfo.ISREADED + " text, " + SqlInfo.TYPE
				+ " text)";
		db.execSQL(sql);
	}

	public Cursor getCursor() {
		this.db = this.getReadableDatabase();
		Cursor cursor = this.db.query(SqlInfo.TABLE_NAME, null, null, null,
				null, null, null);
		this.db.close();
		return cursor;
	}

	public Cursor getInfoCursor(String selection, String args[]) {
		this.db = this.getReadableDatabase();
		Cursor cursor = this.db.query(SqlInfo.TABLE_NAME, null, selection,
				args, null, null, SqlInfo.TIME + " asc");
		return cursor;
	}

	public void insertData(ContentValues values) {
		this.db = this.getWritableDatabase();
		this.db.insert(SqlInfo.TABLE_NAME, SqlInfo.ID, values);
		this.db.close();
	}

	/**
	 * @param whereClause
	 *            : SqlInfo.AGE+"=? and "+SqlInfo.EDU+"=?" ,and表示且，or表示或
	 * @param whereArgs
	 *            ：new String[]{"22", "aaa"}，对应的值
	 * **/
	public void deleteData(String whereClause, String[] whereArgs) {
		this.db = this.getWritableDatabase();
		// and 表示且 SqlInfo.AGE+"=? and "+SqlInfo.EDU+"=?"
		// or 表示或者
		this.db.delete(SqlInfo.TABLE_NAME, whereClause, whereArgs);
		this.db.close();
	}

	public void updateData(ContentValues values, String whereClause,
			String[] whereArgs) {
		this.db = this.getWritableDatabase();
		this.db.update(SqlInfo.TABLE_NAME, values, whereClause, whereArgs);
		this.db.close();
	}

	/**
	 * @param orderBy
	 *            :SqlInfo.AGE+" asc" ,asc表示升序，desc表示降序
	 * */
	public Cursor sort(String orderBy) {
		this.db = this.getReadableDatabase();
		// asc升序，desc降序
		Cursor cursor = this.db.query(SqlInfo.TABLE_NAME, null, null, null,
				null, null, orderBy);
		this.db.close();
		return cursor;
	}
	//
	// public Cursor getNameAndAge()
	// {
	// this.db = this.getReadableDatabase();
	// //String[] columns 筛选指定字段的值
	//
	// Cursor cursor = this.db.query(SqlInfo.TABLE_NAME, new
	// String[]{SqlInfo.NAME, SqlInfo.AGE}, SqlInfo.AGE+"<=?", new
	// String[]{"28"}, null, null, SqlInfo.AGE+" asc");
	// return cursor;
	// }
}
