package com.nezip.bizcard1;

import java.util.ArrayList;
import java.util.List;

import com.nezip.bizcard1.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;


public class AlarmService extends Service {

	private final String TAG = "###" + this.getClass().getSimpleName() + "###";
	
	private UpdateTask ut;

	private boolean isRunning = false;
	private int notiTimer=0;
	static ArrayList<ChatData> chatDatas = new ArrayList<ChatData>();
	
	
	private Handler timeHandler;
	
	
	/**
	 * 데이터 업데이트 시 비동기 방식으로 처리하기 위한 클래스
	 */
	public class UpdateTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			//Log.i("onPreExecute", "Start [UpdataTask]");
			isRunning = true;
			//Log.d(TAG, "onPreExecute : " + isRunning);
		}

		@Override
		protected Boolean doInBackground(Void... arg) {
			//Log.i("doInBackground", "Doing [UpdataTask]");

			try {
				
				loadXML();

				
			} catch (Exception e) {
				e.printStackTrace();
				//Log.e(TAG, e.getMessage());
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			//Log.i("onPostExecute", "End [UpdataTask]");
			
			isRunning = false;						
			notifiHandler.sendEmptyMessage(0);
			
			stopSelf();
			
			
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub

			isRunning = false;
			
			super.onCancelled();
		}
		
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		Log.d(TAG, "===== onBind =====");
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "===== onCreate =====");
		
		ut = new UpdateTask();
		
		timeHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				if( !isRunning && !ut.isCancelled() ){
					
					try{
						
						ut.execute();
						
					} catch(Exception e){
						isRunning = false;
						//Log.d(TAG, e.getMessage());
					}

				} 
				timeHandler.sendEmptyMessageDelayed(0, 60000);	
				super.handleMessage(msg);
			}
		};
				
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "===== onDestroy =====");
		
		timeHandler.removeMessages(0);
		notifiHandler.removeMessages(0);
		
		isRunning = false;
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG, "===== onStart =====");

		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG, "===== onStartCommand =====");
		
		if("".equals(Info.email)){
			Info.login(getApplicationContext());
		}
		
		NotificationManager mNotiManager = (NotificationManager)getSystemService( NOTIFICATION_SERVICE );
		
		
		timeHandler.sendEmptyMessage(0);

		return super.onStartCommand(intent, flags, startId);
	}
	
	
	private Handler notifiHandler =  new Handler(){
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {

			Info.reciveMessage=true;

			notiTimer = msg.what;

			if(chatDatas.size() > 0 && chatDatas.size() > notiTimer){		
				
				Log.d("Info.runChating", ""+Info.runChating);
				Log.d("ChatingForm.email", ":"+ChatingForm.email);
				Log.d("chatDatas.get(notiTimer).getUserEmail1()", ""+chatDatas.get(notiTimer).getUserEmail1());
										
				if("CHAT".equals(chatDatas.get(notiTimer).getUserEmail2()) && (!Info.runChating || !ChatingForm.email.equals(chatDatas.get(notiTimer).getUserEmail1()))){
					
					Notification noti = new Notification(R.drawable.icon, null, System.currentTimeMillis());
					NotificationManager mNotiManager = (NotificationManager)getSystemService( NOTIFICATION_SERVICE );
					mNotiManager.cancelAll();
					
					Intent intent = new Intent(getApplicationContext(), ChatingForm.class);					
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("EMAIL", chatDatas.get(notiTimer).getUserEmail1());
					intent.putExtra("ENAME", chatDatas.get(notiTimer).getUserName1());
					intent.putExtra("PHOTO", "");
					intent.putExtra("RETURN", "NO");
					intent.putExtra("NOTIID", Integer.parseInt(chatDatas.get(notiTimer).getId()));
					
					if(Info.sound && !Info.soundRun) {
						noti.defaults |= Notification.DEFAULT_SOUND;	
						//Info.soundRun = true;
					}
					noti.flags |= Notification.FLAG_AUTO_CANCEL;
					
					PendingIntent content = PendingIntent.getActivity( getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT  );						
					RemoteViews napView = new RemoteViews(getPackageName(), R.layout.alarmview);
					noti.contentView = napView;
					noti.contentIntent = content;
		
					String message = chatDatas.get(notiTimer).getMessage().toString();
					if(message.length() > 6 && "PHOTO:".equals(message.substring(0, 6))){
						message = "[PHOTO]";
					}

					/*
					noti.setLatestEventInfo(getApplicationContext(), 
							chatDatas.get(notiTimer).getUserName1().toString(), 
							message, 
							content);
					*/

					//mNotiManager.cancel(Integer.parseInt(chatDatas.get(notiTimer).getId()));
					mNotiManager.notify(Integer.parseInt(chatDatas.get(notiTimer).getId()), noti);
				}
				
				notiTimer++;
				if(notiTimer >= chatDatas.size()){
					notiTimer = 0;
					chatDatas.clear();
					notifiHandler.removeMessages(0);
					 
				} else {
					//notifiHandler.sendEmptyMessageDelayed(notiTimer, 1000);					
				}

			}
			
		};
	};
	
	
	void loadXML() throws Exception
    {

		if(!"".equals(Info.email) && !"".equals(Info.name)){
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String result = "";
			SharedPreferences isFirstRunPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			if(isFirstRunPreferences.getBoolean("MESSAGE_ACCEPT", true)){			
					
				params.add(new BasicNameValuePair("userEmail1", Info.email));
				params.add(new BasicNameValuePair("page", "1"));
				params.add(new BasicNameValuePair("pageSize", "10000"));

				result = Info.Submit("http://swcbizcard.nezip.co.kr/ChatMessageLastAction.asp", params);
				Log.d("result", result);
				
				if(!"".equals(result)){		
					
					String[] messageRow = result.split("</R>");
					
					for(int i=0; i < messageRow.length; i++){
						
						if(!"".equals(messageRow[i].trim())){
							String[] messageCol = messageRow[i].split("</C>");
							if(messageCol.length > 4){					
								chatDatas.add(new ChatData(chatDatas.size()-1, messageCol[0], messageCol[1], "CHAT", messageCol[2], "", messageCol[3], messageCol[4]));
							}
						}
					}
				}
			}
		}
    }
	
}
