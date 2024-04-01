package com.nezip.bizcard1;

import com.nezip.bizcard1.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class PhotoView extends Activity 
{
	MoveView userPhoto;
	Bitmap sbm;
	
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

	    setContentView(R.layout.photoview);
	    
	    Intent intent_rec = getIntent();
	
	    userPhoto = (MoveView) findViewById(R.id.moveView1);
	    sbm = Info.GetImageFromURL(Info.isNull(intent_rec.getStringExtra("PHOTO")).toString(), PhotoView.this);
	    userPhoto.setImageBitmap(sbm);
	    
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
		
		Button leftTurnBtn = (Button) findViewById(R.id.leftTurnBtn);
		leftTurnBtn.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				try {
					if(sbm != null){
						sbm = Info.imgRotate(sbm, -90);
			    	    userPhoto.setImageBitmap(sbm);	 
					} else {
						Toast.makeText(PhotoView.this, "사진을 먼저 추가해 주세요.", 0).show();
					}
		    	    
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
			}
		});
		
		Button rightTurnBtn = (Button) findViewById(R.id.rightTurnBtn);
		rightTurnBtn.setOnClickListener(new OnClickListener() 
	    {
			@Override
			public void onClick(View v) 
			{
				try {
					if(sbm != null){
						sbm = Info.imgRotate(sbm, 90);
			    	    userPhoto.setImageBitmap(sbm);
					} else {
						Toast.makeText(PhotoView.this, "사진을 먼저 추가해 주세요.", 0).show();
					}
				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
			    }
				
				
			}
		});
		
		
	}
	
	
	
	@Override
	public void onDestroy() {
		
		userPhoto.setImageBitmap(null);
		if(!sbm.isRecycled()) {
			sbm.recycle();
			sbm = null;
		}
		super.onDestroy();
		
	}
	
}
