package com.nezip.bizcard1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCardView extends Activity
{
	static int position = 0;

	public static String idCheck="NO";	
	
	TextView userName;
	//TextView userName01;
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
    //TextView topPhone;
    TextView keyword;
    //EditText companyLogo;
    //EditText myPhoto;

	TextView  editBtn;
    
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

		setContentView(R.layout.bizcardview_2110);

		userName = (TextView) findViewById(R.id.userName);
		//userName01 = (TextView) findViewById(R.id.userName01);
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
		//topPhone = (TextView) findViewById(R.id.topPhone);
		keyword = (TextView) findViewById(R.id.keyword);

		//companyLogo = (EditText) findViewById(R.id.companyLogo);
		//myPhoto = (EditText) findViewById(R.id.myPhoto);

		//하단 탭버튼

		userName.setText(Info.name);
		//userName01.setText(Info.userName01);
		companyName.setText(Info.companyName);
		companyName01.setText(Info.companyName01);
		dept.setText(Info.dept);
		grade.setText(Info.grade);
		officeAddr.setText(Info.officeAddr);
		officeAddr01.setText(Info.officeAddr01);
		upmoo.setText(Info.upmoo);
		officeTel.setText(Info.officeTel);
		officeFax.setText(Info.officeFax);
		mobile.setText(Info.mobile);
		mobile01.setText(Info.mobile01);
		workEmail.setText(Info.workEmail);
		//topPhone.setText(Info.topPhone);
		keyword.setText(Info.keyword);
		//companyLogo.setText(Info.companyLogo);
		//myPhoto.setText(Info.myPhoto);

		final Button officeTelBtn = (Button) findViewById(R.id.officeTelBtn);
		officeTelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!"".equals(officeTel.getText().toString())) {
					try {
						Info.performDial(MyCardView.this, officeTel.getText().toString());
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
						Info.performDial(MyCardView.this, mobile.getText().toString());
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
						Info.performDial(MyCardView.this, mobile01.getText().toString());
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
						Info.performDial(MyCardView.this, mobile01.getText().toString());

					} catch (OutOfMemoryError e) {
						// System.gc();
					} catch (Exception e) {
					}
				}
			}
		});

		editBtn = (TextView) findViewById(R.id.editBtn);
		editBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), BizCardForm.class);
					intent.putExtra("SEARCHTEXT", "");
					finish();
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});


		final TextView UserOffBtn = (TextView) findViewById(R.id.logoutBtn);
		UserOffBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					//ChatRoomListBtn.setBackgroundResource(R.drawable.han_main_bottom_icon_public_b);
					UserOffBtn.setTextColor(Color.RED);

					//로그아웃 삽입
					SharedPreferences isFirstRunPreferences = PreferenceManager.getDefaultSharedPreferences(MyCardView.this);
					SharedPreferences.Editor editor = isFirstRunPreferences.edit();
					editor.putBoolean("AUTO_LOGIN", false);
					editor.putString("AUTO_EMAIL", "");
					editor.putString("AUTO_PWD", "");
					editor.commit();

					Info.email = "";
					Info.name = "";
					Info.pwd = "";

					Intent intent = new Intent(MyCardView.this, SecessionPopup02.class);
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
					UserOffBtn.setTextColor(Color.BLACK);
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});


		ImageView myCardIV = (ImageView) findViewById(R.id.myCardIV);
		myCardIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), MyCardView.class);
					intent.putExtra("SEARCHTEXT", "");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});


		ImageView reCardIV = (ImageView) findViewById(R.id.reCardIV);
		reCardIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), BizCardList.class);
					intent.putExtra("SEARCHTEXT", "");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});


		ImageView mainIV = (ImageView) findViewById(R.id.mainIV);
		mainIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), BizCardFindList.class);
					intent.putExtra("SEARCHTEXT", "");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});



		ImageView chatIV = (ImageView) findViewById(R.id.chatIV);
		chatIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), ChatRoomList.class);
					intent.putExtra("SEARCHTEXT", "");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});



		ImageView systemIV = (ImageView) findViewById(R.id.systemIV);
		systemIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), Setting.class);
					intent.putExtra("SEARCHTEXT", "");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});


	}

}
