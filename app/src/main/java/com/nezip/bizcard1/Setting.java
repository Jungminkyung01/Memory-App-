package com.nezip.bizcard1;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Setting extends Activity 
{

	SharedPreferences isFirstRunPreferences;
	

	@Override
	public void onBackPressed() 
	{
		Intent intent = new Intent(getApplicationContext(), MyCardView.class);
		finish();
		startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	//하단 탭버튼
	LinearLayout ch1_btn;
	CheckBox ch1, ch2, ch3, ch4, ch5, ch6 ;
	boolean checked;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    
	    if("".equals(Info.email)){
	    	if(!Info.login(getApplicationContext())){
	    		Intent intent = new Intent(this, Login1th.class);
	            finish();
	            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
				return;
	    	}
	    }

	    setContentView(R.layout.setting_2150);
	    //화면 고정
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
	    ch2 = (CheckBox) findViewById(R.id.ch2);
	    isFirstRunPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	    ch2.setChecked(isFirstRunPreferences.getBoolean("AUTO_LOGIN", false));
	    ch2.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				SharedPreferences.Editor editor = isFirstRunPreferences.edit();
				editor.putBoolean("AUTO_LOGIN", isChecked);
				
				editor.putString("AUTO_EMAIL", isChecked?Info.email:"");
				editor.putString("AUTO_PWD", isChecked?Info.pwd:"");
				
				editor.commit();
				
			}
		});
		
		ch3 = (CheckBox) findViewById(R.id.ch3);
	    isFirstRunPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	    ch3.setChecked(isFirstRunPreferences.getBoolean("ALARM_SOUND", true));
	    ch3.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				SharedPreferences.Editor editor = isFirstRunPreferences.edit();
				editor.putBoolean("ALARM_SOUND", ch3.isChecked());
				Info.sound = ch3.isChecked();
				editor.commit();
				
			}
		});
	    

	    ch5 = (CheckBox) findViewById(R.id.ch5);
	    ch5.setChecked(isFirstRunPreferences.getBoolean("MESSAGE_ACCEPT", true));
	    ch5.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				SharedPreferences.Editor editor = isFirstRunPreferences.edit();
				editor.putBoolean("MESSAGE_ACCEPT", ch5.isChecked());
				editor.commit();				
			}
		});


	    List<NameValuePair> param = new ArrayList<NameValuePair>();
	    param.add(new BasicNameValuePair("userEmail", Info.email));
		String result = Info.Submit("http://swcbizcard.nezip.co.kr/UserStateAction.asp", param);
		param.clear();
		
	    /*ch6 = (CheckBox) findViewById(R.id.ch6);
	    ch6.setChecked("YES".equals(result));
	    ch6.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				
				SharedPreferences.Editor editor = isFirstRunPreferences.edit();
				editor.putBoolean("USER_STATE", ch6.isChecked());
				editor.commit();
				
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("userEmail", Info.email ));
				param.add(new BasicNameValuePair("userState", ch6.isChecked()?"YES":"NO" ));
				String result = Info.Submit("http://swcbizcard.nezip.co.kr/UserStateChangeAction.asp", param);
			}
		});*/
	    
	    
	    
	    final TextView managerChatBtn = (TextView) findViewById(R.id.managerChatBtn);
	    managerChatBtn.setOnClickListener(new OnClickListener() 	 
	    {
			@Override
			public void onClick(View v) 
			{
				try {
					managerChatBtn.setTextColor(Color.RED);
					
					Intent intent = new Intent(Setting.this, ChatingForm.class);
					intent.putExtra("EMAIL", "corp");
					intent.putExtra("ENAME", "운영자");
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




		final TextView usereditBtn = (TextView) findViewById(R.id.usereditBtn);
		usereditBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					//ChatRoomListBtn.setBackgroundResource(R.drawable.han_main_bottom_icon_public_b);
					usereditBtn.setTextColor(Color.RED);
					Intent intent = new Intent(Setting.this, UserEdit.class);
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
					usereditBtn.setTextColor(Color.BLACK);
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});


		final TextView UserOffBtn = (TextView) findViewById(R.id.userOffBtn);
		UserOffBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					//ChatRoomListBtn.setBackgroundResource(R.drawable.han_main_bottom_icon_public_b);
					UserOffBtn.setTextColor(Color.RED);

					//회원탈퇴 삽입


					Intent intent = new Intent(Setting.this, SecessionPopup.class);
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
	
	
	@Override
	protected void onResume() {
		/////////////미투데이 트위터 로그인 버튼
		super.onResume();
	}
}
