package com.weather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.weather.app.database.WeatherDBUtil;
import com.weather.app.model.City;
import com.weather.app.model.Couty;
import com.weather.app.model.Province;

public class UtilForDataBase {
	public static boolean handleSaveProvincesForResponse(
			WeatherDBUtil weatherDBUtil, String response) {
		Log.d("TAG", "response:" + response);
		if (!TextUtils.isEmpty(response)) {
			String[] provinces_Content = response.split(",");
			if (provinces_Content != null && provinces_Content.length > 0) {
				for (String province_Content : provinces_Content) {
					String[] province_detail = province_Content.split("\\|");
					Province province = new Province();
					province.setProvince_code(province_detail[0]);
					province.setProvince_name(province_detail[1]);
					weatherDBUtil.saveProvince(province);

				}
			}

			return true;
		}
		return false;
	}

	public static void handleWeatherInfForResponse(Context context,
			String response) {

		if (!TextUtils.isEmpty(response)) {
			JSONObject content;
			try {
				content = new JSONObject(response);
				JSONObject weatherInf = content.getJSONObject("weatherinfo");
				String city = weatherInf.getString("city");
				String cityId = weatherInf.getString("cityid");
				String temp1 = weatherInf.getString("temp1");
				String temp2 = weatherInf.getString("temp2");
				String weatherdetail = weatherInf.getString("weather");
				String updatetime = weatherInf.getString("ptime");
				saveWeatherInf(context, city, cityId, temp1, temp2,
						weatherdetail, updatetime);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static void saveWeatherInf(Context context, String city_Name,
			String city_code, String max_temp1, String min_temp2,
			String weatherDetail, String updatetime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日",
				Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putString("cityName", city_Name);
		editor.putString("cityCode", city_code);
		editor.putString("max_temp1", max_temp1);
		editor.putString("min_temp2", min_temp2);
		editor.putString("weatherDetail", weatherDetail);
		editor.putString("updatetime", updatetime);
		editor.putString("currenttime", format.format(new Date()));
		editor.commit();
	}

	public static boolean handleSaveCitiesForResponse(
			WeatherDBUtil weatherDBUtil, String response, int province_Id) {
		if (!TextUtils.isEmpty(response)) {
			String[] cities_Content = response.split(",");
			if (cities_Content != null && cities_Content.length > 0) {
				for (String city_Content : cities_Content) {
					String[] city_detail = city_Content.split("\\|");
					City city = new City();
					city.setCity_code(city_detail[0]);
					city.setCity_name(city_detail[1]);
					city.setProvince_id(province_Id);
					weatherDBUtil.saveCity(city);
				}
			}
			return true;
		}
		return false;
	}

	public static boolean handleSaveCoutiesForResponse(
			WeatherDBUtil weatherDBUtil, String response, int city_Id) {
		Log.d("TAG", "handleSaveCoutiesForResponse:" + response);
		if (!TextUtils.isEmpty(response)) {
			String[] couties_Content = response.split(",");
			if (couties_Content != null && couties_Content.length > 0) {
				for (String couty_Content : couties_Content) {
					String[] couty_detail = couty_Content.split("\\|");
					Couty couty = new Couty();
					couty.setCouty_code(couty_detail[0]);
					couty.setCouty_name(couty_detail[1]);
					couty.setCity_id(city_Id);
					weatherDBUtil.saveCouty(couty);

				}
			}
			return true;
		}
		return false;
	}
}
