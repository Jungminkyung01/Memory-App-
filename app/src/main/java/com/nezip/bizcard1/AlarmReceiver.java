package com.nezip.bizcard1;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {
    
	private final String TAG = "###" + this.getClass().getSimpleName() + "###";
	
	private Context context;
	private Intent i;
	public ComponentName compService;
	
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {

			//PendingIntent m_sender = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class), 0);
			//AlarmManager m_am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			//m_am.cancel(m_sender);
			//m_am.set(AlarmManager.RTC_WAKEUP, 1000, m_sender);
		 
		};
	};
	
	@Override
    public void onReceive(Context con, Intent intent) //10초마다 이리루 들어옵니다
    {
		Log.d(TAG, "##### onReceive #####");
		
		/*
		this.context = con;

		compService = new ComponentName(context, "com.nezip.bizcard1.AlarmService");
    	i = new Intent();

		i.setComponent(compService);
		context.startService(i);
		
        handler.sendEmptyMessageDelayed(0, 1000);        
		 */
    }
}
