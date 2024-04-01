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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

public class Login1th extends Activity 
{

	
	SharedPreferences saveConfigPref, isFirstRunPreferences;
	CheckBox autoLoginChk;
	EditText emailTxt, pwdTxt;
	
	@Override
	public void onBackPressed() 
	{
        finish();
		//Intent intent = new Intent(getApplicationContext(), MyCardView.class);
		//startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login1th_2000);

	    
	  //처음실행인지 가져오기
	    autoLoginChk = (CheckBox) findViewById(R.id.autoLoginChk);
	    emailTxt = (EditText) findViewById(R.id.email);
		pwdTxt = (EditText) findViewById(R.id.pwd);

		try{

			isFirstRunPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			saveConfigPref = getSharedPreferences("Config", MODE_PRIVATE);

			if(isFirstRunPreferences.getBoolean("IS_FIRST_RUN", true)){
				Toast.makeText(Login1th.this, "비즈카드 애플리케이션을 설치해 주셔서 감사합니다.", 0).show();

				SharedPreferences.Editor editor = isFirstRunPreferences.edit();
				editor.putBoolean("IS_FIRST_RUN", false);
				editor.commit();

			}

		}catch(Exception e){

		}
        emailTxt.setText(Info.email);
        pwdTxt.setText(Info.pwd);
        
        
	    
	    //화면 고정
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		//Log.i("현위치", "로그인!");
	    
		
	    //하단 탭버튼
		final Button loginBtn = (Button) findViewById(R.id.loginBtn);
		final Button joinBtn = (Button) findViewById(R.id.joinBtn);
		final Button find1Btn = (Button) findViewById(R.id.find1Btn);
		final Button find2Btn = (Button) findViewById(R.id.find2Btn);


		loginBtn.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				try 
				{

					
					if("".equals(pwdTxt.getText().toString()))
					{
						Toast.makeText(Login1th.this, "비밀번호를 입력해주세요.", 0).show();
						return;
					}
					/*
					if(emailTxt.getText().toString().indexOf("@")==-1)
					{
						Toast.makeText(Login1th.this, "이메일 형식이 올바르지 않습니다.", 0).show();
						return;
					}
					*/
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("pwd", pwdTxt.getText().toString()));
				    params.add(new BasicNameValuePair("email", emailTxt.getText().toString()));
				    	
				    String result = "";
					result = Info.Submit("http://swcbizcard.nezip.co.kr/LoginAction.asp", params);
					Toast.makeText(Login1th.this, result , 0).show();
					if(!"".equals(result.trim())){						
						String[] messageCol = result.split("</C>");

						if(messageCol.length > 5){
							Info.email = messageCol[0];
							Info.name = messageCol[1];
							Info.userName01 = messageCol[1];
							
							Info.companyName = messageCol[2];
							Info.companyName01 = messageCol[2];
							Info.dept = messageCol[3];
							Info.grade = messageCol[4];
							Info.officeAddr = messageCol[5];
							Info.officeAddr01 = messageCol[5];
							Info.upmoo = messageCol[6];
							Info.officeTel = messageCol[7];
							Info.officeFax = messageCol[8];
							Info.mobile = messageCol[9];
							Info.mobile01 = messageCol[9];
							Info.workEmail = messageCol[10];
							Info.topPhone = messageCol[11];
							Info.homePage = messageCol[12];
							Info.keyword = messageCol[13];
							Info.companyLogo = messageCol[14];
							Info.myPhoto = messageCol[15];
							Info.regid = messageCol[16];
							Info.id = messageCol[17];

							Info.pwd = messageCol[18];
							Info.userUseDt = messageCol[19];

							String regId = FirebaseInstanceId.getInstance().getToken();
							if(!"".equals(regId) && ("".equals(Info.regid) || !regId.equals(Info.regid))){
								Info.regid = regId;
								params.clear();
								params.add(new BasicNameValuePair("userEmail", Info.email));
								params.add(new BasicNameValuePair("RegID", regId));
								result = Info.Submit("http://swcbizcard.nezip.co.kr/UserRegIDUpdateAction.asp", params);
							}
							
						}
					}

					if(!"".equals(Info.email)){
						Toast.makeText(Login1th.this, "'"+Info.name + "' 님 반갑습니다.", 0).show();
						
						loginBtn.setTextColor(Color.RED);
						Intent intent = new Intent(Login1th.this, MyCardView.class);
			            finish();
				        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
					
					} else {
						Toast.makeText(Login1th.this, result + "아이디와 비밀번호를 확인하세요.", 0).show();
					}

					try{
						SharedPreferences.Editor editor = isFirstRunPreferences.edit();
						editor.putBoolean("AUTO_LOGIN", autoLoginChk.isChecked());
						if(autoLoginChk.isChecked()){
							editor.putString("AUTO_EMAIL", Info.email);
							editor.putString("AUTO_PWD", Info.pwd);
						}

						editor.commit();

					}catch (Exception e){

					}
				    
				 
				    
				}
				catch (Exception e) 
				{
				     e.printStackTrace();
				     Toast.makeText(Login1th.this, "네트워크에 문제가 있는것 같습니다.\n다시 시도하여 주십시오.", 0).show();
				}
				
			}
		});
		
	    joinBtn.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				try {
					joinBtn.setTextColor(Color.RED);
					Intent intent = new Intent(Login1th.this, Join1th.class);
		            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
			}
		});
	    
	    find1Btn.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				try {
					find1Btn.setTextColor(Color.RED);
					Intent intent = new Intent(Login1th.this, Find1th.class);
		            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
			}
		});
	    
	    find2Btn.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				try {
					find2Btn.setTextColor(Color.RED);
					Intent intent = new Intent(Login1th.this, Find2th.class);
		            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
			}
		});
	}
}
