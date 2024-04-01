package com.nezip.bizcard1;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BizCardForm extends Activity 
{
	static int position = 0;
	static ImageView userPhoto;
	static ImageView corpPhoto;
	static Bitmap bm = null;
	static Bitmap cbm = null;
	static Bitmap ubm = null;
	boolean logoImgUpload = false;
	boolean photoImgUpload = false;
	
	static String companyFilePath = "";
	static String myPhotoFilePath = "";
	
	static String SAMPLEIMG = "photo.jpg";
	BitmapFactory.Options options = new BitmapFactory.Options();
	

	public static String idCheck="NO";

	//EditText userName;
	TextView userName01;
	EditText companyName;
	TextView companyName01;
    EditText dept;
    EditText grade;
    EditText upmoo;
    EditText officeAddr;
	TextView officeAddr01;
    EditText officeTel;
    EditText officeFax;
    EditText mobile;
	TextView mobile01;
    EditText workEmail;
    //EditText topPhone;
    //EditText homePage;
    EditText keyword;
    //EditText companyLogo;
    //EditText myPhoto;

	TextView submitBtn; //cancelBtn;
	
	 @Override
	public void onBackPressed()
	{
		Intent intent = new Intent(getApplicationContext(),MyCardView.class);
		intent.putExtra("SEARCHTEXT", Info.isNull(getIntent().getStringExtra("SEARCHTEXT")));
		startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

		StrictMode.enableDefaults();
	    setContentView(R.layout.bizcardform_2111);
	    
	    Intent intent_rec = getIntent();

	    //userName=(EditText) findViewById(R.id.userName);
		userName01=(TextView) findViewById(R.id.userName01);
		companyName = (EditText) findViewById(R.id.companyName);
		companyName01 = (TextView) findViewById(R.id.companyName01);
	    dept = (EditText) findViewById(R.id.dept);
	    grade = (EditText) findViewById(R.id.grade);
	    upmoo = (EditText) findViewById(R.id.upmoo);
	    officeAddr = (EditText) findViewById(R.id.officeAddr);
		officeAddr01 = (TextView) findViewById(R.id.officeAddr01);
	    officeTel = (EditText) findViewById(R.id.officeTel);
	    officeFax = (EditText) findViewById(R.id.officeFax);
	    mobile = (EditText) findViewById(R.id.mobile);
		mobile01 = (TextView) findViewById(R.id.mobile01);
	    workEmail = (EditText) findViewById(R.id.workEmail);
	    //topPhone = (EditText) findViewById(R.id.topphone);
	    //homePage = (EditText) findViewById(R.id.homepage);
	    keyword = (EditText) findViewById(R.id.keyword);
	    //companyLogo = (EditText) findViewById(R.id.companyLogo);
	    //myPhoto = (EditText) findViewById(R.id.myPhoto);
	    
		//corpPhoto = (ImageView) findViewById(R.id.corpPhoto);
		//userPhoto = (ImageView) findViewById(R.id.userPhoto);
	    
	    //하단 탭버튼

		//userName.setText(Info.userName);
		userName01.setText(Info.userName01);
		companyName.setText(Info.companyName);
		companyName01.setText(Info.companyName01);
	    dept.setText(Info.dept);
	    grade.setText(Info.grade);
	    officeAddr.setText(Info.officeAddr);
		officeAddr01.setText(Info.officeAddr01);
	    upmoo.setText(Info.upmoo);
	    officeTel.setText(Info.officeTel);
	    officeFax.setText(Info.officeFax);
	    mobile.setText(Info.mobile);
		mobile01.setText(Info.mobile01);
	    workEmail.setText(Info.workEmail);
	   // topPhone.setText(Info.topPhone);
	    //homePage.setText(Info.homePage);
	    keyword.setText(Info.keyword);
	    //companyLogo.setText(Info.companyLogo);
	    //myPhoto.setText(Info.myPhoto);

		/*
	    if(!"".equals(companyLogo.getText().toString())) {
		    cbm = Info.GetImageFromURL(companyLogo.getText().toString(), this);
		    corpPhoto.setImageBitmap(Info.getRoundedCornerBitmap(cbm, 40));
	    }
	    
	    if(!"".equals(myPhoto.getText().toString())) {
		    ubm = Info.GetImageFromURL(myPhoto.getText().toString(), this);
		    userPhoto.setImageBitmap(Info.getRoundedCornerBitmap(ubm, 40));
	    }
	    
	    cancelBtn = (Button) findViewById(R.id.cancelBtn);
	    cancelBtn.setOnClickListener(new OnClickListener() 
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
	    
	    */

  		submitBtn = (TextView) findViewById(R.id.submitBtn);
  		submitBtn.setOnClickListener(new OnClickListener() 
  	    {
  			@Override
  			public void onClick(View v) 
  			{
  				try {
  					String companyLogoUrl = "";
  					String myPhotoUrl = "";
					List<NameValuePair> params = new ArrayList<NameValuePair>();

  					if("".equals(companyName.getText().toString())) {
						Toast.makeText(BizCardForm.this, "화사명을 입력해 주세요.", 0).show();
						return;
					}/* else if(!"수정".equals(submitBtn.getText().toString().trim())){
		  				if(workEmail.getText().toString().indexOf("@")==-1)
						{
							Toast.makeText(BizCardForm.this, "이메일 형식이 올바르지 않습니다.", 0).show();
							return;
						}
	  				}*/

					params.add(new BasicNameValuePair("userEmail", Info.email));
					//params.add(new BasicNameValuePair("userName", userName.getText().toString()));
					params.add(new BasicNameValuePair("userName01", userName01.getText().toString()));
					params.add(new BasicNameValuePair("companyName", companyName.getText().toString()));
					params.add(new BasicNameValuePair("companyName01", companyName01.getText().toString()));
					params.add(new BasicNameValuePair("dept", dept.getText().toString()));
					params.add(new BasicNameValuePair("grade", grade.getText().toString()));
					params.add(new BasicNameValuePair("officeAddr", officeAddr.getText().toString()));
					params.add(new BasicNameValuePair("officeAddr01", officeAddr01.getText().toString()));
					params.add(new BasicNameValuePair("upmoo", upmoo.getText().toString()));
					params.add(new BasicNameValuePair("officeTel", officeTel.getText().toString()));
					params.add(new BasicNameValuePair("officeFax", officeFax.getText().toString()));
					params.add(new BasicNameValuePair("mobile", mobile.getText().toString()));
					params.add(new BasicNameValuePair("mobile01", mobile01.getText().toString()));
					params.add(new BasicNameValuePair("workEmail", workEmail.getText().toString()));
					//params.add(new BasicNameValuePair("topphone", topPhone.getText().toString()));
					//params.add(new BasicNameValuePair("homepage", homePage.getText().toString()));
					params.add(new BasicNameValuePair("keyword", keyword.getText().toString()));
					
					//params.add(new BasicNameValuePair("companyLogo", companyLogoUrl));
					//params.add(new BasicNameValuePair("myPhoto", myPhotoUrl));


				    String result = "";
				    try {
						result = Info.Submit("http://swcbizcard.nezip.co.kr/BizCardUpdateAction.asp", params);
					}catch (Exception e){
					}

	  				
				    if("YES".equals(result.trim())){

						Info.userName01 = userName01.getText().toString();
				    	Info.companyName = companyName.getText().toString();
						Info.companyName01 = companyName01.getText().toString();
						Info.dept = dept.getText().toString();
						Info.grade = grade.getText().toString();
						Info.officeAddr = officeAddr.getText().toString();
						Info.officeAddr01 = officeAddr01.getText().toString();
						Info.upmoo = upmoo.getText().toString();
						Info.officeTel = officeTel.getText().toString();
						Info.officeFax = officeFax.getText().toString();
						Info.mobile = mobile.getText().toString();
						Info.mobile01 = mobile01.getText().toString();
						Info.workEmail = workEmail.getText().toString();
						//Info.topPhone = topPhone.getText().toString();
						//Info.homePage = homePage.getText().toString();
						Info.keyword = keyword.getText().toString();
						//Info.companyLogo = companyLogoUrl;
						Info.myPhoto = myPhotoUrl;
				    	
				    	onBackPressed();				
					} else {
						Toast.makeText(BizCardForm.this, "등록실패 : 네트워크 상태가 좋지 않은것 같습니다.", 0).show();
					}
  				} catch (OutOfMemoryError e) {
  	  				//System.gc();
  	  			} catch (Exception e) {
  	  		    }
	  		}
  				
  			
  		});




		Button postNoBtn = (Button) findViewById(R.id.postNoBtn);
		postNoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Info.SelectPostNo = "";
				Info.SelectPostAddr = "";
				try {
					Intent intent = new Intent(BizCardForm.this, PostPopup.class);
					intent.putExtra("TYPE", "");
					startActivityForResult(intent, 99);
				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}
			}
		});

		ImageView backBtn = (ImageView) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener()
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


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
			//우편번호
			if (!"".equals(Info.SelectPostAddr)) {
				officeAddr.setText(Info.SelectPostNo+" "+Info.SelectPostAddr);
			}
		}

	}


	private Handler replaceHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			super.handleMessage(msg);
		}
	};

}
