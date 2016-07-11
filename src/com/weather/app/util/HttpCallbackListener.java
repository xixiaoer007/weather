package com.weather.app.util;

public interface HttpCallbackListener {
	public abstract void detailData(String response);
	public abstract void error(Exception e);
}
