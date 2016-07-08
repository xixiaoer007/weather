package com.weather.app.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weather.app.model.City;
import com.weather.app.model.Couty;
import com.weather.app.model.Province;

public class WeatherDBUtil {
	private final String db_Name = "weather";
	private final int version = 1;
	private WeatherDBUtil weatherDBUtil;
	private SQLiteDatabase dataBase_read;
	private SQLiteDatabase dataBase_write;

	public WeatherDBUtil(Context context) {
		WeatherDataBase weatherDataBase = new WeatherDataBase(context, db_Name,
				null, version);
		dataBase_read = weatherDataBase.getReadableDatabase();
		dataBase_write = weatherDataBase.getWritableDatabase();
	}

	public WeatherDBUtil getWeatherDBUtilInstance(Context context) {
		if (weatherDBUtil == null) {
			weatherDBUtil = new WeatherDBUtil(context);
		}
		return weatherDBUtil;
	}

	public void saveProvince(Province province) {
		if (province != null) {
			String province_Name = province.getProvince_name();
			String province_Code = province.getProvince_code();
			ContentValues values = new ContentValues();
			values.put("province_name", province_Name);
			values.put("province_code", province_Code);
			dataBase_write.insert("province", null, values);
		}
	}

	public List<Province> getProvinces() {
		List<Province> provinces = new ArrayList<Province>();
		Cursor cursor = dataBase_read.query("province", null, null, null, null,
				null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String province_name = cursor.getString(cursor
						.getColumnIndex("province_name"));
				String province_code = cursor.getString(cursor
						.getColumnIndex("province_code"));
				Province province = new Province();
				province.setId(id);
				province.setProvince_name(province_name);
				province.setProvince_code(province_code);
				provinces.add(province);
			} while (cursor.moveToNext());
		}
		return provinces;
	}

	public void saveCity(City city) {
		if (city != null) {
			String city_Name = city.getCity_name();
			String city_Code = city.getCity_code();
			int province_Id = city.getProvince_id();
			ContentValues values = new ContentValues();
			values.put("city_name", city_Name);
			values.put("city_code", city_Code);
			values.put("province_id", province_Id);
			dataBase_write.insert("city", null, values);
		}
	}

	public List<City> getCities(int province_Id) {
		List<City> cities = new ArrayList<City>();
		Cursor cursor = dataBase_read.query("city", null, "province_id =?",
				new String[] { String.valueOf(province_Id) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String city_name = cursor.getString(cursor
						.getColumnIndex("city_name"));
				String city_Code = cursor.getString(cursor
						.getColumnIndex("city_code"));

				City city = new City();
				city.setCity_code(city_Code);
				city.setCity_name(city_name);
				city.setId(id);
				city.setProvince_id(province_Id);
				cities.add(city);
			} while (cursor.moveToNext());
		}
		return cities;
	}

	public void saveCouty(Couty couty) {
		if (couty != null) {
			String couty_Name = couty.getCouty_name();
			String couty_Code = couty.getCouty_code();
			int city_id = couty.getCity_id();
			ContentValues values = new ContentValues();
			values.put("couty_name", couty_Name);
			values.put("couty_code", couty_Code);
			values.put("city_id", city_id);
			dataBase_write.insert("couty", null, values);
		}
	}

	public List<Couty> getCouties(int city_id) {
		List<Couty> couties = new ArrayList<Couty>();
		Cursor cursor = dataBase_read.query("city", null, "city_id =?",
				new String[] { String.valueOf(city_id) }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String couty_Name = cursor.getString(cursor
						.getColumnIndex("couty_name"));
				String couty_Code = cursor.getString(cursor
						.getColumnIndex("couty_code"));

				Couty couty = new Couty();
				couty.setId(id);
				couty.setCity_id(city_id);
				couty.setCouty_name(couty_Name);
				couty.setCouty_code(couty_Code);
			} while (cursor.moveToNext());
		}
		return couties;
	}
}
