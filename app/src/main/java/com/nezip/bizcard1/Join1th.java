package com.nezip.bizcard1;

import com.nezip.bizcard1.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class Join1th extends Activity 
{
	@Override
	public void onBackPressed() 
	{

		finish();
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.join1th);

	    //화면 고정
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		WebView roleview = (WebView) findViewById(R.id.roleView);
		roleview.getSettings().setJavaScriptEnabled(false);
		roleview.setVerticalScrollBarEnabled(false);
		roleview.setClickable(false);
		roleview.loadUrl("http://swcbizcard.nezip.co.kr/role.html");
		
		final CheckBox agree = (CheckBox) findViewById(R.id.agree);
		
	    //하단 탭버튼
	    
	    final Button corpUserBtn = (Button) findViewById(R.id.corpUserBtn);
	    corpUserBtn.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				try {
					if(agree.isChecked()){
						corpUserBtn.setTextColor(Color.RED);
						Intent intent = new Intent(Join1th.this, UserJoin.class);
			            finish();
			            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
					} else {
						Toast.makeText(Join1th.this, "이용약관 및 개인정보취급방침에 동의하셔야만 회원가입이 가능합니다.", 0).show();
					}
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
			}
		});
	    

		final Button login1thBtn = (Button) findViewById(R.id.login1thBtn);
		login1thBtn.setOnClickListener(new OnClickListener() 
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
		
		

		
	}

}
