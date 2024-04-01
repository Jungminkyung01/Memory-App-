package com.nezip.bizcard1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class SecessionPopup02 extends Activity {

	public static String email = "";
	public static String name="";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.secession_2110);

        final TextView logoutBtn = (TextView) findViewById(R.id.logoutBtn);

		final  Button okBtn=(Button)findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				try {
					okBtn.setTextColor(Color.RED);


							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("email", Info.email));
							String result=Info.Submit("http://swcbizcard.nezip.co.kr/UserOffAction.asp", params);
							if("YES".equals(result.trim())){

                                SharedPreferences isFirstRunPreferences = PreferenceManager
                                        .getDefaultSharedPreferences(SecessionPopup02.this);
                                SharedPreferences.Editor editor = isFirstRunPreferences
                                        .edit();
                                editor.putBoolean("AUTO_LOGIN",
                                        false);
                                editor.commit();

								Info.email = "";
								Info.name = "";
								Info.userName01 = "";

								Info.companyName = "";
								Info.companyName01 = "";
								Info.dept = "";
								Info.grade = "";
								Info.officeAddr = "";
								Info.officeAddr01 = "";
								Info.upmoo = "";
								Info.officeTel = "";
								Info.officeFax = "";
								Info.mobile = "";
								Info.mobile01 = "";
								Info.workEmail = "";
								Info.topPhone = "";
								Info.homePage = "";
								Info.keyword = "";
								Info.companyLogo = "";
								Info.myPhoto = "";
								Info.regid = "";
								Info.id = "";

								Info.pwd = "";

                                Intent intent = new Intent(	getApplicationContext(), Login1th.class);
                                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();

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
