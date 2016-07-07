package com.example.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDataBase extends SQLiteOpenHelper {
	private final String PROVINCE = "create table province(id integer primary key autoincrement,province_name text,province_code text)";
	private final String CITY = "create table city(id integer primary key autoincrement,city_name text,city_code text,province_id integer)";
	private final String COUTY = "create table couty(id integer primary key autoincrement,couty_name text,couty_code text,city_id integer)";

	public WeatherDataBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(PROVINCE);
		db.execSQL(CITY);
		db.execSQL(COUTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
