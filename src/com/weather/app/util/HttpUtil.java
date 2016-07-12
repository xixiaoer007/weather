package com.weather.app.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {
	public static void send(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			HttpURLConnection connection;

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(3000);
					connection.setReadTimeout(3000);
					connection.setRequestMethod("GET");
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					StringBuilder builder = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					Log.d("TAG", builder.toString());
					if (builder.length() > 0) {
						listener.detailData(builder.toString());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					listener.error(e);
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
