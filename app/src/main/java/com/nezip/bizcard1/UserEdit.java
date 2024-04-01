package com.nezip.bizcard1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List; 

public class UserEdit extends Activity
{
	
	public static String No="";
	public static String Addr="";	
	public static String idCheck="NO";	
	
	EditText userName;
    TextView userEmail;
    
	EditText userPwd;
	EditText userPwd2;

	ImageView join2thBtn;
	
	@Override
	public void onBackPressed() 
	{
        finish();
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.user_2151);
	    
	    Intent intent_rec = getIntent();
	    
	    userName = (EditText) findViewById(R.id.userName);
	    userName.setText(Info.name);
	    userEmail = (TextView) findViewById(R.id.userEmail);
	    userEmail.setText(Info.email);
	    
	    TextView pageTitle = (TextView) findViewById(R.id.pageTitle);	
	    if(!"".equals(Info.email)){

			ImageView join2thBtn = (ImageView) findViewById(R.id.join2thBtn);
	    	LinearLayout buttonPartition = (LinearLayout) findViewById(R.id.buttonPartition);	
	    	Button joinEndBtn = (Button) findViewById(R.id.joinEndBtn);
	    	    

	    }

	    userEmail.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub	
				UserEdit.idCheck = "";
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


		//하단 탭버튼
  		final Button joinEndBtn = (Button) findViewById(R.id.joinEndBtn);
  	    joinEndBtn.setOnClickListener(new OnClickListener() 
  	    {
  			@Override
  			public void onClick(View v) 
  			{
  				try {
	  				if("".equals(userName.getText().toString()))
					{
						Toast.makeText(UserEdit.this, "성명을 입력해 주세요.", 0).show();
						return;
					}

	  				if("".equals(userPwd.getText().toString()))
					{
						Toast.makeText(UserEdit.this, "비밀번호를 입력해 주세요.", 0).show();
						return;
					}
	  				if("".equals(userPwd2.getText().toString()))
					{
						Toast.makeText(UserEdit.this, "비밀번호 확인을 입력해 주세요.", 0).show();
						return;
					}
	  				if(!userPwd.getText().toString().equals(userPwd2.getText().toString()))
					{
						Toast.makeText(UserEdit.this, "비밀번호가 일치하지 않습니다.\n다시 입력해주세요.", 0).show();
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
			    	result = Info.Submit("http://swcbizcard.nezip.co.kr/UserUpdateAction.asp", params);
			    	
			    	//Toast.makeText(UserJoin.this, result, 0).show();
	  				
				    if("YES".equals(result.trim())){
				    	
				    	SharedPreferences isFirstRunPreferences = PreferenceManager
								.getDefaultSharedPreferences(UserEdit.this);
						SharedPreferences.Editor editor = isFirstRunPreferences.edit();
						editor.putBoolean("AUTO_LOGIN", true);
						editor.putString("AUTO_EMAIL", userEmail.getText().toString());
						editor.putString("AUTO_PWD", userPwd.getText().toString());
						editor.commit();
						
				    	Info.login(getApplicationContext());
						finish();
					} else {
						Toast.makeText(UserEdit.this, "전송실패 : 네트워크 상태가 좋지 않은것 같습니다.", 0).show();
					}
				    
  				} catch (OutOfMemoryError e) {
  	  				//System.gc();
  	  			} catch (Exception e) {
  	  		    }
	  		}
  				
  			
  		});

	}
	
}
