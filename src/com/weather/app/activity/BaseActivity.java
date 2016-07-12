package com.weather.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.weather.app.R;

public abstract class BaseActivity extends Activity {
	public TextView titleView;
	public Button back;
	public Button refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView();
		
		super.onCreate(savedInstanceState);
		
		iniTitleView();
		
		
	}

	public void iniTitleView() {
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
		titleView = (TextView) findViewById(R.id.titlebar_title);

		titleView.setText(getTitlebarTitle());
		refresh=(Button) this.findViewById(R.id.titlebar_btn_right);
		back = (Button) this.findViewById(R.id.titlebar_btn_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
	}

	public abstract void setContentView();

	abstract protected String getTitlebarTitle();
}
