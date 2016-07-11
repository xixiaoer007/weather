package com.weather.app.util;

import android.text.TextUtils;
import android.util.Log;

import com.weather.app.database.WeatherDBUtil;
import com.weather.app.model.City;
import com.weather.app.model.Couty;
import com.weather.app.model.Province;

public class UtilForDataBase {
	public static boolean handleSaveProvincesForResponse(
			WeatherDBUtil weatherDBUtil, String response) {
		Log.d("TAG", "response:"+response);
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
		Log.d("TAG", "handleSaveCoutiesForResponse:"+response);
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
