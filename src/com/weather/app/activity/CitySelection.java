package com.weather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.weather.app.R;
import com.weather.app.database.WeatherDBUtil;
import com.weather.app.model.City;
import com.weather.app.model.Couty;
import com.weather.app.model.Province;
import com.weather.app.util.HttpCallbackListener;
import com.weather.app.util.HttpUtil;
import com.weather.app.util.UtilForDataBase;

public class CitySelection extends BaseActivity {
	private ListView listView;
	private int current_Level;
	private final int PROVINCE_MODE = 1;
	private final int CITY_MODE = 2;
	private final int COUTY_MODE = 3;
	private List<String> data = new ArrayList<String>();
	private WeatherDBUtil weatherDBUtil;
	private List<Province> provinces;
	private List<City> cities;
	private List<Couty> couties;
	private ArrayAdapter<String> adapter;
	private ProgressDialog progressDialog;
	private Province selected_Province;
	private City selected_City;
	private Couty selected_Couty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		listView = (ListView) this.findViewById(R.id.listView);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, data);
		listView.setAdapter(adapter);
		weatherDBUtil = WeatherDBUtil.getWeatherDBUtilInstance(this);
		queryProvinces();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (current_Level == PROVINCE_MODE && provinces != null
						&& provinces.size() > 0) {
					selected_Province = provinces.get(position);
					queryCities();
				} else if (current_Level == CITY_MODE && cities != null
						&& cities.size() > 0) {

					selected_City = cities.get(position);
					Log.d("TAG", "selected" + selected_City.getCity_name());
					queryCouties();
				} else if (current_Level == COUTY_MODE) {
					selected_Couty = couties.get(position);
					String coutyCode = selected_Couty.getCouty_code();
					String coutyName=selected_Couty.getCouty_name();
					Intent intent = new Intent(CitySelection.this,
							WeatherInfActivity.class);
					intent.putExtra("coutyCode", coutyCode);
					intent.putExtra("coutyName", coutyName);
					startActivity(intent);
				}
			}
		});
		back.setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (current_Level == CITY_MODE) {
			queryProvinces();
		} else if (current_Level == COUTY_MODE) {
			queryCities();
		}
	}

	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_area_selected);
	}

	@Override
	protected String getTitlebarTitle() {
		// TODO Auto-generated method stub
		return "城市列表";
	}

	public void queryProvinces() {
		current_Level = PROVINCE_MODE;
		provinces = weatherDBUtil.getProvinces();
		if (provinces.size() > 0) {
			data.clear();
			for (Province province : provinces) {
				String province_Name = province.getProvince_name();
				data.add(province_Name);
			}
			Log.d("TAG", "data:" + data.toString());
			adapter.notifyDataSetChanged();
		} else {
			loadDataFromServer(null, "province");
		}
	}

	public void queryCities() {
		current_Level = CITY_MODE;
		cities = weatherDBUtil.getCities(selected_Province.getId());
		if (cities.size() > 0) {
			data.clear();
			for (City city : cities) {
				String city_Name = city.getCity_name();
				data.add(city_Name);
			}

			adapter.notifyDataSetChanged();
		} else {
			loadDataFromServer(selected_Province.getProvince_code(), "city");
		}
	}

	public void queryCouties() {
		current_Level = COUTY_MODE;
		couties = weatherDBUtil.getCouties(selected_City.getId());
		if (couties.size() > 0) {

		} else {

		}
		if (couties.size() > 0) {
			data.clear();
			for (Couty couty : couties) {
				String couty_Name = couty.getCouty_name();
				data.add(couty_Name);
			}
			Log.d("TAG", "couty:" + data.toString());
			adapter.notifyDataSetChanged();
		} else {
			loadDataFromServer(selected_City.getCity_code(), "couty");
		}
	}

	private void loadDataFromServer(String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();

		HttpUtil.send(address, new HttpCallbackListener() {
			boolean result = false;

			@Override
			public void error(Exception e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void detailData(String response) {
				// TODO Auto-generated method stub
				dismissProgressDialog();
				if ("province".equals(type)) {
					result = UtilForDataBase.handleSaveProvincesForResponse(
							weatherDBUtil, response);

				} else if ("city".equals(type)) {
					result = UtilForDataBase.handleSaveCitiesForResponse(
							weatherDBUtil, response, selected_Province.getId());
				} else if ("couty".equals(type)) {
					result = UtilForDataBase.handleSaveCoutiesForResponse(
							weatherDBUtil, response, selected_City.getId());
					if (result) {
						List<Couty> couties = weatherDBUtil
								.getCouties(selected_City.getId());
						Log.d("TAG", "cityId:" + selected_City.getId() + ":"
								+ couties.size());
					}

				}
				if (result) {

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Log.d("TAG", "1111");
							dismissProgressDialog();
							if ("province".equals(type)) {

								queryProvinces();
							} else if ("city".equalsIgnoreCase(type)) {
								queryCities();
							} else if ("couty".equalsIgnoreCase(type)) {

								queryCouties();
							}
						}
					});
				}
			}
		});

	}

	public void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(CitySelection.this);
			progressDialog.setMessage("正在传输中，稍安勿躁");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	public void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

	}
}
