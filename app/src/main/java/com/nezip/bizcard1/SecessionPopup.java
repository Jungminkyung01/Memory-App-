package com.nezip.bizcard1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class SecessionPopup extends Activity {

	public static String email = "";
	public static String name="";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.secession_2153);

        final EditText userPwd = (EditText) findViewById(R.id.userPwd);
		final CheckBox ch1 = (CheckBox) findViewById(R.id.ch1);


        final  Button okBtn=(Button)findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				try {
					okBtn.setTextColor(Color.RED);

					if(Info.pwd.equals(userPwd.getText().toString())){
						if(ch1.isChecked()){

							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("email", Info.email));
							String result=Info.Submit("http://swcbizcard.nezip.co.kr/UserOffAction.asp", params);
							if("YES".equals(result.trim())){

								SharedPreferences isFirstRunPreferences = PreferenceManager.getDefaultSharedPreferences(SecessionPopup.this);
								SharedPreferences.Editor editor = isFirstRunPreferences.edit();
								editor.putBoolean("AUTO_LOGIN", false);

								editor.putString("AUTO_EMAIL", "");
								editor.putString("AUTO_PWD", "");

								editor.commit();

								Info.email = "";
								Info.name = "";

								Info.companyName = "";
								Info.dept = "";
								Info.grade = "";
								Info.officeAddr = "";
								Info.upmoo = "";
								Info.officeTel = "";
								Info.officeFax = "";
								Info.mobile = "";
								Info.workEmail = "";
								Info.topPhone = "";
								Info.homePage = "";
								Info.keyword = "";
								Info.companyLogo = "";
								Info.myPhoto = "";
								Info.regid = "";
								Info.id = "";

								Info.pwd = "";

								Intent intent = new Intent(getApplicationContext(), MyCardView.class);
								finish();
								startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

							}
						} else {
							Toast.makeText(SecessionPopup.this, "퇄퇴동의에 체크하여 주십시오.", Toast.LENGTH_LONG).show();
						}
					}else if("".equals(userPwd.getText().toString())) {
						Toast.makeText(SecessionPopup.this, "암호를 입력하세요.", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(SecessionPopup.this, "암호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
					}

					okBtn.setTextColor(Color.BLACK);

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
			}
		});
		
		Button cancelBtn=(Button)findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				try {
					onBackPressed();
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
			}
		});
		

    }
    
    @Override
	public void onBackPressed() 
	{
    	setResult(-1, null);
		finish();
	}
    

}
