package com.nezip.bizcard1;

import java.util.ArrayList;
import java.util.List;

import com.nezip.bizcard1.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class UserPoint extends Activity 
{
	
	Long startId = (long) 0;
	WebView popupView;
	EditText idNumber;
	
	Spinner  buyPoint;
    String[] arChapter = {
			"10,000P 이용권  / 10,000원",
			"20,000P 이용권  / 20,000원",
			"30,000P 이용권  / 30,000원",
			"40,000P 이용권  / 40,000원",
			"50,000P 이용권  / 50,000원",
			"60,000P 이용권  / 60,000원",
			"70,000P 이용권  / 70,000원",
			"80,000P 이용권  / 80,000원",
			"90,000P 이용권  / 90,000원",
			"100,000P 이용권  / 100,000원",
			"110,000P 이용권  / 110,000원",
			"120,000P 이용권  / 120,000원",
			"130,000P 이용권  / 130,000원",
			"140,000P 이용권  / 140,000원",
			"150,000P 이용권  / 150,000원",
			"160,000P 이용권  / 160,000원",
			"170,000P 이용권  / 170,000원",
			"180,000P 이용권  / 180,000원",
			"190,000P 이용권  / 190,000원",
			"200,000P 이용권  / 200,000원"
		};
    ArrayAdapter<CharSequence> mAdapter;
    boolean mInitSelection = true;
	

	@Override
	public void onBackPressed() 
	{
		finish();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    
	    if("".equals(Info.email)){
	    	if(!Info.login(getApplicationContext())){
	    		Intent intent = new Intent(this, Login1th.class);
	            finish();
	            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
				return;
	    	}
	    }

		setContentView(R.layout.corppoint);
	    
	    
	    buyPoint = (Spinner) findViewById(R.id.buyPoint);
	    buyPoint.setPrompt("구매할 이용권을  선택하세요.");
		
		mAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, arChapter);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buyPoint.setAdapter(mAdapter);
		
		
		
		

		buyPoint.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// 최초 전개시에도 Selected가 호출되는데 이때는 프레퍼런스에서 최후 장을 찾아 로드한다.
				// 이후부터는 사용자가 선택한 장을 로드한다.
				
				if (mInitSelection) {
					mInitSelection = false;
					buyPoint.setSelection(0);
				}
			}
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
	    
		//하단 탭버튼
		
		Button buyPointBtn = (Button) findViewById(R.id.buyPointBtn);
		buyPointBtn.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{

				try {
					Intent intent = new Intent(UserPoint.this, BuyPointPopup.class);
					intent.putExtra("BUYPOINT", buyPoint.getSelectedItem().toString());     
			        
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
				
			} 
		});
		
		Button buyPointHistBtn = (Button) findViewById(R.id.buyPointHistBtn);
		buyPointHistBtn.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{

				try {
					Intent intent = new Intent(UserPoint.this, PointList.class);
					intent.putExtra("SEARCHTEXT", Info.isNull(getIntent().getStringExtra("SEARCHTEXT")));
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
				
			} 
		});
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userEmail", Info.email));
	    String BuyPoint = Info.Submit("http://swcbizcard.nezip.co.kr/CorpBuyPointAction.asp", params);
	    
	    TextView totalBuyPoint = (TextView) findViewById(R.id.totalBuyPoint);
	    totalBuyPoint.setText(CustomTextWathcer.makeStringComma(BuyPoint)+"P");
	    
	    params.clear();
		params.add(new BasicNameValuePair("userEmail", Info.email));
		String UsePoint = Info.Submit("http://swcbizcard.nezip.co.kr/CorpUsePointAction.asp", params);
	    
		TextView totalUsePoint = (TextView) findViewById(R.id.totalUsePoint);
		totalUsePoint.setText(CustomTextWathcer.makeStringComma(UsePoint)+"P");		
		
		String BalPoint = Integer.toString(Integer.parseInt(BuyPoint) - Integer.parseInt(UsePoint));	    
		TextView totalBalPoint = (TextView) findViewById(R.id.totalBalPoint);
		totalBalPoint.setText(CustomTextWathcer.makeStringComma(BalPoint)+"P");   
		

	}
	
}
