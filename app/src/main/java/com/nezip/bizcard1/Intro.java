package com.nezip.bizcard1;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import static com.nezip.bizcard1.Info.email;

import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ListView;



import java.util.ArrayList;

import java.util.Iterator;


public class Intro extends Activity 
{
	
	public static int update_flag=2;
	private ProgressDialog dialog;
	private int maxCol, nowCol;
	
	private Timer introTimer;
	private TimerTask timerTask;

	AnimationDrawable aniCatDrawable;

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		//화면 고정
        setContentView(R.layout.intro);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


		if(Info.email.indexOf('@') > 0) {
			 Info.fcmemail = Info.email.substring(0, Info.email.indexOf('@'));
		} else {
			 Info.fcmemail = Info.email;
		}
		Info.regid = FirebaseInstanceId.getInstance().getToken();


		ImageView imgV = (ImageView) findViewById(R.id.Logo);
        aniCatDrawable = (AnimationDrawable) imgV.getDrawable();
        if(aniCatDrawable!=null){
            aniCatDrawable.start();
        }else {
            Toast.makeText(this,"애니메이션 실패!!",Toast.LENGTH_SHORT).show();
        }

		Animation an = AnimationUtils.loadAnimation(this, R.anim.alpha);
		imgV.startAnimation(an);

		Info.login(getApplicationContext());
		Log.e("전화번호", "아이디" + email);
		if (!"".equals(email)) {
			UpdateTask ut = new UpdateTask();
			ut.execute();
		}else{
			Intent intent = null;
			intent = new Intent(Intro.this, Login1th.class);
            finish();
            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}

    }
    
    void nextPage() {

		Intent intent = null;
    	intent = new Intent(Intro.this, MyCardView.class);
        finish();
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }



	public class UpdateTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			//Log.d(TAG, "onPreExecute : " + isRunning);
		}

		@Override
		protected Boolean doInBackground(Void... arg) {
			//Log.i("doInBackground", "Doing [UpdataTask]");
			try {

				if(!"".equals(email)){

					String phones = "";
					String id = "";

					Log.e("전화번호 등록", "전화목록" + phones);
					Cursor c = managedQuery( ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							new String[]{ ContactsContract.CommonDataKinds.Phone.DATA1 },
							null,
							null,
							"REPLACE(REPLACE(" + ContactsContract.CommonDataKinds.Phone.DATA1 + ",' ', ''),'-', '') ASC " );
					c.moveToFirst();
					for (int i = 0; i < c.getCount(); i++) {
						String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1)).replaceAll("-", "").replaceAll(" ", "").trim();
						if(!id.equals(phone)){
							phones += phone + "</C>전화번호</R>" ;
							Log.e("전화번호 등록", "전화목록" + phone);
						}
						id = phone;
						c.moveToNext();


					}
					c.close();
					phones += "</END>" + "</C>끝</R>";
					Log.e("전화번호 등록", "전화목록" + phones);

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("userEmail", email));
					params.add(new BasicNameValuePair("phones", phones));


					String result = Info.Submit("http://swcbizcard.nezip.co.kr/PhoneListUpdateAction.asp", params);

					if("YES".equals(result.trim())){

					}


				}

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("전화번호", e.getMessage());
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			//Log.i("onPostExecute", "End [UpdataTask]");

			new CountDownTimer(1000, 1) {
				@Override
				public void onTick(long millisUntilFinished) {
					onPause();
				}

				@Override
				public void onFinish() {
					nextPage();
				}
			}.start();

		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

	}
}