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
	 *            ���ݿ���
	 * @param factory
	 * @param version
	 *            ���ݿ�汾��
	 */
	public MySqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}

	/**
	 * �������
	 * 
	 * @param db
	 *            ���ݲֿ�
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		// ʹ��DDL���Դ������
		// Create table ���� ��(_id integer primary key autoincrement, name text )��
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
	 * �������ݿ�
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
	 *            : SqlInfo.AGE+"=? and "+SqlInfo.EDU+"=?" ,and��ʾ�ң�or��ʾ��
	 * @param whereArgs
	 *            ��new String[]{"22", "aaa"}����Ӧ��ֵ
	 * **/
	public void deleteData(String whereClause, String[] whereArgs) {
		this.db = this.getWritableDatabase();
		// and ��ʾ�� SqlInfo.AGE+"=? and "+SqlInfo.EDU+"=?"
		// or ��ʾ����
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
	 *            :SqlInfo.AGE+" asc" ,asc��ʾ����desc��ʾ����
	 * */
	public Cursor sort(String orderBy) {
		this.db = this.getReadableDatabase();
		// asc����desc����
		Cursor cursor = this.db.query(SqlInfo.TABLE_NAME, null, null, null,
				null, null, orderBy);
		this.db.close();
		return cursor;
	}
	//
	// public Cursor getNameAndAge()
	// {
	// this.db = this.getReadableDatabase();
	// //String[] columns ɸѡָ���ֶε�ֵ
	//
	// Cursor cursor = this.db.query(SqlInfo.TABLE_NAME, new
	// String[]{SqlInfo.NAME, SqlInfo.AGE}, SqlInfo.AGE+"<=?", new
	// String[]{"28"}, null, null, SqlInfo.AGE+" asc");
	// return cursor;
	// }
}
