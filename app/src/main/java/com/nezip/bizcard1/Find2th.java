package com.nezip.bizcard1;

import java.util.ArrayList;
import java.util.List;

import com.nezip.bizcard1.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Find2th extends Activity {
	@Override
	public void onBackPressed() {

		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find2th_2500);

		// 화면 고정
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		final Button findBtn = (Button) findViewById(R.id.findBtn);

		findBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					EditText userName = (EditText) findViewById(R.id.userName);
					EditText userEmail = (EditText) findViewById(R.id.userEmail);
					EditText userPhone = (EditText) findViewById(R.id.userPhone); //PHONE

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("userName", userName
							.getText().toString()));
					params.add(new BasicNameValuePair("userEmail", userEmail
							.getText().toString()));
					params.add(new BasicNameValuePair("userPhone", userPhone
							.getText().toString()));
					String result = Info.Submit(
							"http://swcbizcard.nezip.co.kr/UserPwdAction.asp",
							params);
					if ("".equals(result.trim())) {
						Toast.makeText(Find2th.this,
								"가입정보를 찾을 수 없습니다.\n입력값을 확인 하십시오.", 0).show();
					} else {
						Toast.makeText(Find2th.this,
								"귀하가 입력하신 암호는  '" + result + "' 입니다.", 0)
								.show();
						Info.pwd = result;
					}

					finish();
				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}
			}
		});


		ImageView backBtn = (ImageView) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try {
					onBackPressed();
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});

	}
}
