package com.nezip.bizcard1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.nezip.bizcard1.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class BuyPointPopup extends Activity {

	int mYear=0, mMonth=0, mDay=0, mHour=0, mMinute=0;
	Calendar cal = new GregorianCalendar();
	TimePickerDialog tpd=null;
	TextView inputTime;

	DatePickerDialog.OnDateSetListener mDateSetListener = 
		new DatePickerDialog.OnDateSetListener() {
		
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			
			inputTime.setText(String.format("%04d-%02d-%02d %02d:%02d", mYear, 
					mMonth + 1, mDay, mHour, mMinute));
			
			if(tpd == null){
				tpd = new TimePickerDialog(BuyPointPopup.this, mTimeSetListener, mHour, mMinute, false);
				tpd.show();
			}
			
		}            
	};
		
	TimePickerDialog.OnTimeSetListener mTimeSetListener = 
		new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet (TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			
			inputTime.setText(String.format("%04d-%02d-%02d %02d:%02d", mYear, 
					mMonth + 1, mDay, mHour, mMinute));
			tpd=null;
		}            
	};
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        
        setContentView(R.layout.buypointpopup);
        
        
        mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH);
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMinute = cal.get(Calendar.MINUTE); 
        
        inputTime = (TextView) findViewById(R.id.inputTime);	   
        inputTime.setText(String.format("%04d-%02d-%02d %02d:%02d", mYear, mMonth + 1, mDay, mHour, mMinute));
	    
        Button timeSetBtn = (Button) findViewById(R.id.timeSetBtn);
        timeSetBtn.setOnClickListener(new OnClickListener() 
	    {
	    	
			@Override
			public void onClick(View v) 
			{
				try {
					new DatePickerDialog(BuyPointPopup.this, mDateSetListener, mYear, mMonth, mDay).show();
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
				
			}
		});

        final TextView buypoint = (TextView) findViewById(R.id.buypoint);
        final Intent intent_rec = getIntent();
        buypoint.setText(Info.isNull(intent_rec.getStringExtra("BUYPOINT")).trim());
        
        final EditText inputName = (EditText) findViewById(R.id.inputName);	           
        Button okBtn=(Button)findViewById(R.id.okBtn);
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String point = buypoint.getText().toString().trim();
					point = point.substring(point.lastIndexOf("/")+1);
					point = point.replaceAll(",","");
					point = point.replaceAll("원","");
					point = point.trim();
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();				
					params.add(new BasicNameValuePair("userEmail", Info.email));
					params.add(new BasicNameValuePair("point", point));
					params.add(new BasicNameValuePair("inputName", inputName.getText().toString().trim()));
					params.add(new BasicNameValuePair("inputTime", inputTime.getText().toString().trim()));
				    String result = Info.Submit("http://swcbizcard.nezip.co.kr/PointInsertAction.asp", params);
			
					if(!"".equals(result.trim())){						
						if("YES".equals(result.trim())){
							Toast.makeText(BuyPointPopup.this, "입금확인요청 성공.", 0).show();
							
							for(int i=0; i < 3; i++){
								if("".equals(Info.adminRegid)){
									params.clear();
									params.add(new BasicNameValuePair("userEmail", "corp"));	    
									Info.adminRegid = Info.Submit("http://swcbizcard.nezip.co.kr/UserRegIDAction.asp", params);
								} else {
									break;
								}
							}
							
							params.clear();
							params.add(new BasicNameValuePair("userEmail1", Info.email));
						    params.add(new BasicNameValuePair("userEmail2", "corp" ));
						    params.add(new BasicNameValuePair("userName1", Info.name));
						    params.add(new BasicNameValuePair("userName2", "운영자" ));
						    params.add(new BasicNameValuePair("message", "'"+Info.name+"' 님 입금확인요청"));					    
						    params.add(new BasicNameValuePair("userCls", "BUYPOINT"));					    
						    result = Info.Submit("http://swcbizcard.nezip.co.kr/ChatMessageInsertAction.asp", params);
						    
							Info.sendMessage("TEXT", "'"+Info.name+"' 님 입금확인요청".toString(), Info.adminRegid);
							finish();
							
						} else {
							Toast.makeText(BuyPointPopup.this, "입금확인요청 실패.", 0).show();
						}
					}
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
			}
		});
		
		Button cancelBtn=(Button)findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
