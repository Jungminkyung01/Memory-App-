package com.nezip.bizcard1;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSProvider {

	LocationManager mlocManager;	
	LocationListener mlocListener;	
	String provider;	
	Location location;	
	String bestProvider;	
	Criteria criteria;	
	 	
	public GPSProvider(LocationManager mlocManager){ // 생성자	
		this.mlocManager = mlocManager; // GPS값을 받아오기 위해서는 LocationManager 클래스의 오브젝트가 반드시 필요하다.	
		
		mlocListener = new LocationListener(){ // 위치와 관련된 디바이스의 다양한 Event에 따른 리스너를 정의해주어야 한다.	
			public void onLocationChanged(Location loc) {  // 사용자의 위치가 변할때마다 그를 감지해내는 메소드	
				loc.getLatitude(); 	
				loc.getLongitude();	
			}
	
			public void onProviderDisabled(String provider){}	
			public void onProviderEnabled(String provider) {}  	
			public void onStatusChanged(String provider, int status, Bundle extras){} 	
		}; 
	
		criteria = new Criteria(); // Criteria는 위치 정보를 가져올 때 고려되는 옵션 정도로 생각하면 된다.	
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);	
		criteria.setPowerRequirement(Criteria.POWER_LOW);	
		criteria.setAltitudeRequired(false);	
		criteria.setBearingRequired(false);	
		criteria.setSpeedRequired(false);	
		criteria.setCostAllowed(true);
	
		bestProvider = mlocManager.getBestProvider(criteria, true); //주어진 옵션 채팅에 맞는 가장 적합한 위치 정보 제공자를 설정한다.	
		location = mlocManager.getLastKnownLocation(bestProvider); //마지막으로 감지된 사용자의 위치를 찾아내는 메소드	
	} 
	
	public double getLatitude(){	
		bestProvider = mlocManager.getBestProvider(criteria, true); // 적합한 위치 정보 제공자를 찾아내고		
		mlocManager.requestLocationUpdates(bestProvider, 0, 0, mlocListener); // 정보 제공자를 통해 외치 업데이트를 한 다음		
		location = mlocManager.getLastKnownLocation(bestProvider);  // 최종 위치 정보를 파악해내고			   
		
		return location.getLatitude(); //Latitude 값을 리턴
	} 
	
	public double getLongitude(){ //Latitude와 원리는 같다.	
		bestProvider = mlocManager.getBestProvider(criteria, true);		
		mlocManager.requestLocationUpdates(bestProvider, 0, 0, mlocListener);		
		location = mlocManager.getLastKnownLocation(bestProvider);
		
		return location.getLongitude();	
	}
}

/*
< 외부에서 GPSProvider 클래스를 이용해서 위치 정보를 받아오는 법 >?

 

...

LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 

// 시스템에서 제공하는 위치 정보 서비스를 받아와서 그를 LocationManager로 캐스팅

GPSProvider gps = new GPSProvider(mlocManager); //오브젝트 생성

 

double longitude = gps.getLongitude(); //이건 아시죠?^^

double latitude = gps.getLatitude();

...

 

< 두 GPS location간의 거리 구하는 방법 >

...

float[] distance = new float[2]; // float 형태의 사이즈 2의 행렬 생성

float actual_distance; //실제 거리 값을 담을 변수

 

Location.distanceBetween(lat1, lon1, lat2, lon2, distance); 

//lat1와 lon1은 첫번째 사용자, lat2와 lon2는 두번째 사용자의 GPS 값.

//distanceBetween은 Location클래스 내에서 정의된 static 함수이기 때문에 Location 클래스를 통해 아무데서나 부를 수 있다.

//이 메소드가 호출되고 나면 distance 행렬의 첫번째 요소로 두 지점의 거리가 할당된다.

actual_distance = distance[0]; //간단한 사용을 위해 일반 변수로 넘겨주기.

...




*/



