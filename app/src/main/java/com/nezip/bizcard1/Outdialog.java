package com.nezip.bizcard1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class Outdialog extends Activity {

	public static String email = "";
	public static String name="";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.secession_2153);

        final EditText searchname = (EditText) findViewById(R.id.searchname);
		
        
        final Intent intent_rec = getIntent();
		
	    searchname.setText(intent_rec.getStringExtra("SEARCHTEXT"));
	    
        Button okBtn=(Button)findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				try {
					String searchText = searchname.getText().toString().trim();
					Intent intent = new Intent(getApplicationContext(), BizCardList.class);		
					if(!"".equals(searchname.getText().toString().trim())){
						intent.putExtra("SEARCHTEXT", searchText);
					} else {
						intent.putExtra("SEARCHTEXT", "");
					}
					if(!searchText.equals(intent_rec.getStringExtra("SEARCHTEXT"))){
						Info.BizCardListPage=1;
					}
					
					finish();
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
					
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
