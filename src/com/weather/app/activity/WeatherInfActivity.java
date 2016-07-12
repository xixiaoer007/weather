package com.weather.app.activity;

import java.util.Arrays;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.weather.app.R;
import com.weather.app.util.HttpCallbackListener;
import com.weather.app.util.HttpUtil;
import com.weather.app.util.PinyinTool;

public class WeatherInfActivity extends BaseActivity {
	String coutyCode;
	String coutyName;
	private TextView publish_text;
	private LinearLayout weather_info_layout;
	private TextView current_date;
	private TextView weather_desp;
	private TextView temp1;
	private TextView temp2;
	private PinyinTool pinyinTool;
	String coutyName2pinyin = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		refresh.setVisibility(View.VISIBLE);
		coutyCode = getIntent().getStringExtra("coutyCode");
		coutyName = getIntent().getStringExtra("coutyName");
		if (coutyName != null) {
			titleView.setText(coutyName + "天气预报");
		}
		pinyinTool = new PinyinTool();
		publish_text = (TextView) this.findViewById(R.id.publish_text);
		weather_info_layout = (LinearLayout) this
				.findViewById(R.id.weather_info_layout);
		current_date = (TextView) this.findViewById(R.id.current_date);
		weather_desp = (TextView) this.findViewById(R.id.weather_desp);
		temp1 = (TextView) this.findViewById(R.id.temp1);
		temp2 = (TextView) this.findViewById(R.id.temp2);
		weather_info_layout.setVisibility(View.GONE);

		try {
			coutyName2pinyin = pinyinTool.toPinYin(coutyName);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!TextUtils.isEmpty(coutyName2pinyin)) {

			publish_text.setText("更新中...");
			// weather_info_layout.setVisibility(View.GONE);
			apiTest(coutyName2pinyin);
		}
		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				publish_text.setText("更新中...");
				weather_info_layout.setVisibility(View.GONE);
				apiTest(coutyName2pinyin);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		coutyCode = getIntent().getStringExtra("coutyCode");
	}

	@Override
	public void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_weather_inf);
	}

	@Override
	protected String getTitlebarTitle() {
		// TODO Auto-generated method stub
		return "天气预报";
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(this, CitySelection.class);
		startActivity(intent);
	}

	private void queryWeatherDetail(String coutyCode) {
		Log.d("TAG", "coutyCode:" + coutyCode);
		String address = "http://www.weather.com.cn/data/cityinfo/" + coutyCode
				+ ".html";
		HttpUtil.send(address, new HttpCallbackListener() {

			@Override
			public void error(Exception e) {
				// TODO Auto-generated method stub
				Log.d("TAG", "error");
			}

			@Override
			public void detailData(String response) {
				// TODO Auto-generated method stub
				String[] arrays = response.split("\\|");
				Log.d("TAG", "response" + Arrays.toString(arrays) + ":"
						+ arrays.length);
			}
		});
	}

	private void apiTest(String cityNameForPinyin) {

		Parameters para = new Parameters();
		PinyinTool tool = new PinyinTool();
		para.put("city", cityNameForPinyin);
		ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",
				ApiStoreSDK.GET, para, new ApiCallBack() {

					@Override
					public void onSuccess(int status, String responseString) {
						Log.d("WeatherInfActivity", "status" + status + "data:"
								+ responseString);
						if ("ok".equals(getStatus(responseString))) {
							publish_text.setText("更新完成");
							weather_info_layout.setVisibility(View.VISIBLE);
							try {
								detailDataFromHefeng(responseString);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							publish_text.setText("天气更新受阻了，不开心");
							weather_info_layout.setVisibility(View.GONE);
						}
					}

					@Override
					public void onComplete() {
						Log.d("WeatherInfActivity", "onComplete");

					}

					@Override
					public void onError(int status, String responseString,
							Exception e) {
						Log.d("WeatherInfActivity", "onError, status: "
								+ status);
						Log.i("WeatherInfActivity", "errMsg: "
								+ (e == null ? "" : e.getMessage()));

					}

				});

	}

	public String getStatus(String responseString) {
		String status = null;
		JSONObject object;
		try {
			object = new JSONObject(responseString);
			JSONArray array = object.getJSONArray("HeWeather data service 3.0");
			for (int i = 0; i < array.length(); i++) {
				Object o = array.opt(i);
				if (o instanceof JSONObject) {
					JSONObject branch = (JSONObject) o;
					status = branch.getString("status");
					if (status != null) {
						return status;
					}
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return status;

	}

	private void detailDataFromHefeng(String responseString)
			throws JSONException {
		// TODO Auto-generated method stub
		JSONObject object = new JSONObject(responseString);
		JSONArray array = object.getJSONArray("HeWeather data service 3.0");
		for (int i = 0; i < array.length(); i++) {
			Object o = array.opt(i);
			StringBuilder builder = new StringBuilder();

			if (o instanceof JSONObject) {
				JSONObject branch = (JSONObject) o;

				JSONObject basic = branch.getJSONObject("basic");
				if (basic != null) {
					JSONObject update = basic.getJSONObject("update");
					current_date.setText(update.getString("loc"));
				}
				JSONObject now = branch.getJSONObject("now");
				if (now != null) {
					JSONObject cond = now.getJSONObject("cond");
					weather_desp.setText(cond.getString("txt"));
				}

				JSONArray daily_forecast = branch
						.getJSONArray("daily_forecast");
				for (int j = 0; j < daily_forecast.length(); j++) {
					Object o1 = daily_forecast.opt(i);
					if (o1 instanceof JSONObject) {
						JSONObject branch1 = (JSONObject) o1;

						JSONObject tmp = branch1.getJSONObject("tmp");
						if (tmp != null) {
							temp2.setText(tmp.getString("max"));
							temp1.setText(tmp.getString("min"));
						}
					}
				}

			}
		}
	}
}
