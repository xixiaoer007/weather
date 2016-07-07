package com.example.weather.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weather.model.City;
import com.example.weather.model.Couty;
import com.example.weather.model.Province;

public class WeatherDBUtil {
	public static final String db_name = "weather";
	public static final int version = 1;
	private SQLiteDatabase database_write;
	private SQLiteDatabase database_read;
	private WeatherDataBase weatherDataBase;
	private WeatherDBUtil weatherDBUtil;

	public WeatherDBUtil(Context context) {
		weatherDataBase = new WeatherDataBase(context, db_name, null, version);
		database_write = weatherDataBase.getWritableDatabase();
		database_read = weatherDataBase.getReadableDatabase();
	}

	public WeatherDBUtil getDBUtilInstance(Context context) {
		if (weatherDBUtil == null) {
			weatherDBUtil = new WeatherDBUtil(context);
		}
		return weatherDBUtil;
	}

	public void saveProvince(Province province) {
		if (province != null) {
			int id = province.getId();
			String province_Name = province.getProvince_name();
			String province_Code = province.getProvince_code();
			ContentValues values = new ContentValues();
			values.put("province_name", province_Name);
			values.put("province_code", province_Code);
			database_read.insert("province", null, values);
		}
	}

	public List<Province> readProvince() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = database_read.query("province", null, null, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String province_Name = cursor.getString(cursor
						.getColumnIndex("province_name"));
				String province_Code = cursor.getString(cursor
						.getColumnIndex("province_code"));
				province.setId(id);
				province.setProvince_name(province_Name);
				province.setProvince_code(province_Code);
				list.add(province);
			} while (cursor.moveToNext());
		}

		return list;
	}

	public void saveCity(City city) {
		if (city != null) {
			int id = city.getId();
			String city_Name = city.getCity_name();
			String city_Code = city.getCity_code();
			int province_Code = city.getProvince_code();
			ContentValues values = new ContentValues();
			values.put("city_name", city_Name);
			values.put("city_code", city_Code);
			values.put("province_id", province_Code);
			database_read.insert("city", null, values);
		}
	}

	public List<City> readCity() {
		List<City> list = new ArrayList<City>();
		Cursor cursor = database_read.query("city", null, null, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String city_Name = cursor.getString(cursor
						.getColumnIndex("city_name"));
				String city_Code = cursor.getString(cursor
						.getColumnIndex("city_code"));
				int province_Id = cursor.getInt(cursor
						.getColumnIndex("province_id"));
				city.setId(id);
				city.setCity_name(city_Name);
				city.setCity_code(city_Code);
				city.setProvince_code(province_Id);
				list.add(city);
			} while (cursor.moveToNext());
		}

		return list;
	}

	public void saveCouty(Couty couty) {
		if (couty != null) {
			int id = couty.getId();
			String couty_Name = couty.getCouty_name();
			String couty_Code = couty.getCouty_code();
			int city_Code = couty.getCity_code();
			ContentValues values = new ContentValues();
			values.put("couty_name", couty_Name);
			values.put("couty_code", couty_Code);
			values.put("city_id", city_Code);
			database_read.insert("couty", null, values);
		}
	}

	public List<Couty> readCouty() {
		List<Couty> list = new ArrayList<Couty>();
		Cursor cursor = database_read.query("couty", null, null, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			do {
				Couty couty = new Couty();
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String couty_Name = cursor.getString(cursor
						.getColumnIndex("couty_name"));
				String couty_Code = cursor.getString(cursor
						.getColumnIndex("couty_code"));
				int city_id = cursor.getInt(cursor.getColumnIndex("city_id"));
				couty.setId(id);
				couty.setCity_code(city_id);
				couty.setCouty_code(couty_Code);
				couty.setCouty_name(couty_Name);

				list.add(couty);
			} while (cursor.moveToNext());
		}

		return list;
	}
}
