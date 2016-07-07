package com.example.weather.model;

public class Couty {
	private int id;
	private String couty_name;
	private String couty_code;
	private int city_code;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCouty_name() {
		return couty_name;
	}

	public void setCouty_name(String couty_name) {
		this.couty_name = couty_name;
	}

	public String getCouty_code() {
		return couty_code;
	}

	public void setCouty_code(String couty_code) {
		this.couty_code = couty_code;
	}

	public int getCity_code() {
		return city_code;
	}

	public void setCity_code(int city_code) {
		this.city_code = city_code;
	}

}
