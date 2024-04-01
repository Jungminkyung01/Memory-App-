package com.nezip.bizcard1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.firebase.iid.FirebaseInstanceId;
import com.nezip.bizcard1.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UserJoin extends Activity 
{
	
	public static String No="";
	public static String Addr="";	
	public static String idCheck="NO";	
	
	EditText userName;
	EditText userEmail;
    
	EditText userPwd;
	EditText userPwd2;

	ImageView join2thBtn;
	
	@Override
	public void onBackPressed() 
	{
        finish();
		Intent intent = new Intent(UserJoin.this, Join1th.class);
		startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.userjoin_2301);
	    
	    Intent intent_rec = getIntent();
	    
	    userName = (EditText) findViewById(R.id.userName);
	    userName.setText(Info.name);
	    userEmail = (EditText) findViewById(R.id.userEmail);	    
	    userEmail.setText(Info.email);
	    
	    TextView pageTitle = (TextView) findViewById(R.id.pageTitle);	
	    if(!"".equals(Info.email)){

			ImageView join2thBtn = (ImageView) findViewById(R.id.join2thBtn);
	    	LinearLayout buttonPartition = (LinearLayout) findViewById(R.id.buttonPartition);	
	    	Button joinEndBtn = (Button) findViewById(R.id.joinEndBtn);
	    	    
                
	    	LinearLayout userEmailEditLayout = (LinearLayout) findViewById(R.id.userEmailEditLayout);	
	    	userEmailEditLayout.setVisibility(View.GONE);
	    } else {
	    	LinearLayout userEmailViewLayout = (LinearLayout) findViewById(R.id.userEmailViewLayout);	
	    	userEmailViewLayout.setVisibility(View.GONE);
	    }
	    userEmail.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub	
				UserJoin.idCheck = "";
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub	
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub				
			}
		});
	    
	    
	    userPwd = (EditText) findViewById(R.id.userPwd);	   
	    userPwd.setText(Info.pwd);
	    userPwd2 = (EditText) findViewById(R.id.userPwd2);	  
	    userPwd2.setText(Info.pwd);
		
		Button dupCheckBtn = (Button) findViewById(R.id.dupCheckBtn);
		dupCheckBtn.setOnClickListener(new OnClickListener() 
	    {
	    	
			@Override
			public void onClick(View v) 
			{
				try {
					if("".equals(userEmail.getText().toString()))
					{
						Toast.makeText(UserJoin.this, "아이디를 입력해주세요.", 0).show();
						return;
					}
					/*
					if(userEmail.getText().toString().indexOf("@")==-1)
					{
						Toast.makeText(UserJoin.this, "이메일 형식이 올바르지 않습니다.", 0).show();
						return;
					}
					*/
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("email", userEmail.getText().toString()));
				    String result = Info.Submit("http://swcbizcard.nezip.co.kr/IDCheckAction.asp", params);
					UserJoin.idCheck = result.trim();

					if("YES".equals(UserJoin.idCheck)){
						Toast.makeText(UserJoin.this, "이용가능한 아이디 입니다.", 0).show();
					} else {
						userEmail.setText("");
						Toast.makeText(UserJoin.this, "이용중인 아이디 입니다.\n다른 아이디를 입력 하십시오.", 0).show();
					}
				    
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
				
			}
		});
	    
	    

		//하단 탭버튼

		join2thBtn = (ImageView) findViewById(R.id.join2thBtn);
		join2thBtn.setOnClickListener(new OnClickListener()
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
	    
	  
  		final Button joinEndBtn = (Button) findViewById(R.id.joinEndBtn);
  	    joinEndBtn.setOnClickListener(new OnClickListener() 
  	    {
  			@Override
  			public void onClick(View v) 
  			{
  				try {
	  				if("".equals(userName.getText().toString()))
					{
						Toast.makeText(UserJoin.this, "성명을 입력해 주세요.", 0).show();
						return;
					}
	  				if(!"수정".equals(joinEndBtn.getText().toString().trim())){
						/*
		  				if(userEmail.getText().toString().indexOf("@")==-1)
						{
							Toast.makeText(UserJoin.this, "이메일 형식이 올바르지 않습니다.", 0).show();
							return;
						}
						*/
		  				if(!"YES".equals(UserJoin.idCheck))
						{
							Toast.makeText(UserJoin.this, "아이디 중복확인을 클릭하십시오.", 0).show();
							return;
						}
	  				}
	  				if("".equals(userPwd.getText().toString()))
					{
						Toast.makeText(UserJoin.this, "비밀번호를 입력해 주세요.", 0).show();
						return;
					}
	  				if("".equals(userPwd2.getText().toString()))
					{
						Toast.makeText(UserJoin.this, "비밀번호 확인을 입력해 주세요.", 0).show();
						return;
					}
	  				if(!userPwd.getText().toString().equals(userPwd2.getText().toString()))
					{
						Toast.makeText(UserJoin.this, "비밀번호가 일치하지 않습니다.\n다시 입력해주세요.", 0).show();
						return;
					}
	  				
	  				
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("userName", userName.getText().toString()));
				    params.add(new BasicNameValuePair("userEmail", userEmail.getText().toString()));
				    params.add(new BasicNameValuePair("password", userPwd.getText().toString()));
				    
				  	String regId = FirebaseInstanceId.getInstance().getToken();
					Info.regid = regId;
					params.add(new BasicNameValuePair("regId", Info.regid)); 
				


			    	String result = "";
			    	if("수정".equals(joinEndBtn.getText().toString().trim())){
				    	result = Info.Submit("http://swcbizcard.nezip.co.kr/UserUpdateAction.asp", params);
				    } else {
				    	result = Info.Submit("http://swcbizcard.nezip.co.kr/UserInsertAction.asp", params);
				    	
				    }
			    	
			    	//Toast.makeText(UserJoin.this, result, 0).show();
	  				
				    if("YES".equals(result.trim())){
				    	
				    	SharedPreferences isFirstRunPreferences = PreferenceManager
								.getDefaultSharedPreferences(UserJoin.this);
						SharedPreferences.Editor editor = isFirstRunPreferences.edit();
						editor.putBoolean("AUTO_LOGIN", true);
						editor.putString("AUTO_EMAIL", userEmail.getText().toString());
						editor.putString("AUTO_PWD", userPwd.getText().toString());
						editor.commit();
						
				    	Info.login(getApplicationContext());
						finish();
					} else {
						Toast.makeText(UserJoin.this, "전송실패 : 네트워크 상태가 좋지 않은것 같습니다.", 0).show();
					}
				    
  				} catch (OutOfMemoryError e) {
  	  				//System.gc();
  	  			} catch (Exception e) {
  	  		    }
	  		}
  				
  			
  		});

	}
	
}
