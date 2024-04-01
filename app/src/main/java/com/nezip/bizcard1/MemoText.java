package com.nezip.bizcard1;

import java.util.ArrayList;
import java.util.List;

import com.nezip.bizcard1.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

 
public class MemoText extends Activity {

	public static String email = "";
	public static String name="";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.memotext);

        final EditText searchname = (EditText) findViewById(R.id.searchname);
		
        
        final Intent intent_rec = getIntent();
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userEmail", Info.email));
		if(!"".equals(intent_rec.getStringExtra("ID"))) {
			params.add(new BasicNameValuePair("id", intent_rec.getStringExtra("ID"))); 
		} else {
			params.add(new BasicNameValuePair("id", ""));  
		}
 
	    String result = Info.Submit("http://swcbizcard.nezip.co.kr/MemoLoadAction.asp", params);
	    searchname.setText(result);
	    
        Button okBtn=(Button)findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				try {
					String searchText = searchname.getText().toString().trim();					
					if(!"".equals(searchText)){
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("userEmail", Info.email));
						
						if(!"".equals(intent_rec.getStringExtra("ID"))) {
							params.add(new BasicNameValuePair("id", intent_rec.getStringExtra("ID"))); 
						} else {
							params.add(new BasicNameValuePair("id", ""));  
						}
 
						params.add(new BasicNameValuePair("memo", searchText));
					    String result = Info.Submit("http://swcbizcard.nezip.co.kr/MemoUpdateAction.asp", params);
					    if("YES".equals(result.trim())){
					    	
					    }
					}
				    
					finish();
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
