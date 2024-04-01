package com.nezip.bizcard1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BizCardView extends Activity
{
	static int position = 0;

	public static String idCheck="NO";	
	
	TextView userName;
	TextView companyName;
	TextView companyName01;
	TextView dept;
	TextView grade;
	TextView upmoo;
    TextView officeAddr;
	TextView officeAddr01;
    TextView officeTel;
    TextView officeFax;
    TextView mobile;
	TextView mobile01;
    TextView workEmail;
    TextView keyword;

	Intent intentRec = null;
    @Override
	public void onDestroy() {

    	
		super.onDestroy();

	}

	
	@Override
	public void onBackPressed() 
	{
		finish();
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ("".equals(Info.email)) {
			if (!Info.login(getApplicationContext())) {
				Intent intent = new Intent(this, Login1th.class);
				finish();
				startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
				return;
			}
		}
		intentRec = getIntent();
		setContentView(R.layout.bizcardview_2120);

		userName = (TextView) findViewById(R.id.userName);
		companyName = (TextView) findViewById(R.id.companyName);
		companyName01 = (TextView) findViewById(R.id.companyName01);
		dept = (TextView) findViewById(R.id.dept);
		grade = (TextView) findViewById(R.id.grade);
		upmoo = (TextView) findViewById(R.id.upmoo);
		officeAddr = (TextView) findViewById(R.id.officeAddr);
		officeAddr01 = (TextView) findViewById(R.id.officeAddr01);
		officeTel = (TextView) findViewById(R.id.officeTel);
		officeFax = (TextView) findViewById(R.id.officeFax);
		mobile = (TextView) findViewById(R.id.mobile);
		mobile01 = (TextView) findViewById(R.id.mobile01);
		workEmail = (TextView) findViewById(R.id.workEmail);
		keyword = (TextView) findViewById(R.id.keyword);

		userName.setText(intentRec.getStringExtra("userName"));
		companyName.setText(intentRec.getStringExtra("companyName"));
		companyName01.setText(intentRec.getStringExtra("companyName"));
		dept.setText(intentRec.getStringExtra("dept"));
		grade.setText(intentRec.getStringExtra("grade"));
		officeAddr.setText(intentRec.getStringExtra("officeAddr"));
		officeAddr01.setText(intentRec.getStringExtra("officeAddr"));
		upmoo.setText(intentRec.getStringExtra("upmoo"));
		officeTel.setText(intentRec.getStringExtra("officeTel"));
		officeFax.setText(intentRec.getStringExtra("officeFax"));
		mobile.setText(intentRec.getStringExtra("mobile"));
		mobile01.setText(intentRec.getStringExtra("mobile"));
		workEmail.setText(intentRec.getStringExtra("workEmail"));
		keyword.setText(intentRec.getStringExtra("keyword"));

		final Button officeTelBtn = (Button) findViewById(R.id.officeTelBtn);
		officeTelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!"".equals(officeTel.getText().toString())) {
					try {
						Info.performDial(BizCardView.this, officeTel.getText().toString());
					} catch (OutOfMemoryError e) {
						// System.gc();
					} catch (Exception e) {
					}
				}
			}
		});

		final TextView mobile = (TextView) findViewById(R.id.mobile);
		mobile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!"".equals(mobile.getText().toString())) {
					try {
						Info.performDial(BizCardView.this, mobile.getText().toString());
					} catch (OutOfMemoryError e) {
						// System.gc();
					} catch (Exception e) {
					}
				}
			}
		});

		final TextView mobile01 = (TextView) findViewById(R.id.mobile01);
		mobile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!"".equals(mobile01.getText().toString())) {
					try {
						Info.performDial(BizCardView.this, mobile01.getText().toString());
					} catch (OutOfMemoryError e) {
						// System.gc();
					} catch (Exception e) {
					}
				}
			}
		});

		final Button topPhoneBtn = (Button) findViewById(R.id.topPhoneBtn);
		topPhoneBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!"".equals(mobile01.getText().toString())) {
					try {
						Info.performDial(BizCardView.this, mobile01.getText().toString());

					} catch (OutOfMemoryError e) {
						// System.gc();
					} catch (Exception e) {
					}
				}
			}
		});

		final ImageView UserOffBtn = (ImageView) findViewById(R.id.backBtn);
		UserOffBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					finish();
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});



		final Button managerChatBtn = (Button) findViewById(R.id.ChatBtn);
		managerChatBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					managerChatBtn.setTextColor(Color.RED);

					Intent intent = new Intent(getApplicationContext(), ChatingForm.class);
					intent.putExtra("EMAIL", intentRec.getStringExtra("userEmail"));
					intent.putExtra("ENAME", userName.getText().toString());
					intent.putExtra("PHOTO", "");
					intent.putExtra("RETURN", "YES");

					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
					managerChatBtn.setTextColor(Color.BLACK);
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});


	}

}
