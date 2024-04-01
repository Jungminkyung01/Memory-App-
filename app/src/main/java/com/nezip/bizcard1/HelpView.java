package com.nezip.bizcard1;

import com.nezip.bizcard1.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;


public class HelpView extends Activity 
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
	    setContentView(R.layout.helpview);
		
	    WebView photoView = (WebView) findViewById(R.id.photoView);
	    //photoView.getSettings().setJavaScriptEnabled(true);
	    photoView.setVerticalScrollBarEnabled(false);
	    photoView.setHorizontalScrollBarEnabled(false);
	    photoView.setClickable(false);
	    photoView.setFocusable(false);
		
	    
	    //Intent intent_rec = getIntent();
	    photoView.loadUrl("http://swcbizcard.nezip.co.kr/help.html");
	    
		
		Button closeBtn = (Button) findViewById(R.id.closeBtn);
		closeBtn.setOnClickListener(new OnClickListener() 
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
	
	
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		
	}
	
}
