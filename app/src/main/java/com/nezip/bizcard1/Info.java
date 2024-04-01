package com.nezip.bizcard1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Info {

	public static String ProjectID = "errand1-17c9d";
    // 개발자 콘솔에서 발급받은 API Key
	private static String API_KEY = "AIzaSyCCJYlSkoEkyIQ18dRWEmXabeJn5zOi6k0";
	// 기기가 활성화 상태일 때 보여줄 것인지.
	private static boolean DELAY_WHILE_IDLE = true;
	// 기기가 비활성화 상태일 때 GCM Storage에 보관되는 시간
	private static int TIME_TO_LIVE = 3;
	// 메세지 전송 실패시 재시도할 횟수
	private static int RETRY = 3;

	public static String id = "";
	public static String email = "";
	public static String fcmemail = "";
	public static String name = "";
	public static String phone = "";
	public static String pwd = "";
	public static String myPhoto = "";
	public static String companyName = "";
	public static String companyName01 = "";
	public static String userName = "";
	public static String userName01 = "";
	public static String dept = "";
	public static String grade = "";
	public static String upmoo = "";
	public static String officeAddr = "";
	public static String officeAddr01 = "";
	public static String officeTel = "";
	public static String officeFax = "";
	public static String mobile = "";
	public static String mobile01 = "";
	public static String workEmail = "";
	public static String topPhone = "";
	public static String homePage = "";
	public static String keyword = "";
	public static String companyLogo = "";
	public static String userUseDt = "";

	public static String regid = "";
	public static String adminRegid = "";

	public static boolean runChating = false;
	public static boolean chatMessageLoding = true;
	public static boolean reciveMessage=false;

	public static boolean sound = true;
	public static boolean soundRun = false;
	public static boolean replay = true;

	private final float DENSITY_240 = 1.0f;
	private final float DENSITY_160 = 0.65f;
	private final float DENSITY_150 = 1.6f;

	private final float SCREEN_STANDARD_WIDTH = 480.0f;
	private final float SCREEN_STANDARD_HEIGHT = 800.0f;

	public static float MAG_WIDTH;
	public static float MAG_HEIGHT;
	public static int ScreenWidth;
	public static int ScreenHeight;

	public static int ChatRoomPage = 1;
	public static int ChatRoomTotalPage = 0;
	public static int ChatRoomNewRows = 0;
	public static int ChatRoomPosition = 0;

	public static int BizCardListPage = 1;
	public static int BizCardListTotalPage = 0;
	public static int BizCardListPosition = 0;

	public static int BizCardFindPage = 1;
	public static int BizCardFindTotalPage = 0;
	public static int BizCardFindPosition = 0;

	public static int UserSelectPage = 1;
	public static int UserSelectTotalPage = 0;
	public static int UserSelectPosition = 0;
	
	public static String UserSelectEmail = "";
	public static String UserSelectName = "";

	public static String SelectPostNo = "";
	public static String SelectPostAddr = "";
	public static String SelectSearchText = "";
	public static String SelectSearchCido = "";
	public static String SelectSearchGu = "";

	public static int PointPage = 1;
	public static int PointPosition = 0;

	public static float gDeviceDensity;

	// static ProgressDialog progressDialog;

	public Info(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int dpiClassification = dm.densityDpi;

		if (dpiClassification == 160) {
			gDeviceDensity = DENSITY_160;
		} else if (dpiClassification == 150) {
			gDeviceDensity = DENSITY_150;
		} else {
			gDeviceDensity = DENSITY_240;
		}

		// PeopleInsideUtil.d("====DENSITY / gDEN : ", dpiClassification + " / "
		// + gDeviceDensity);

		ScreenWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();
		ScreenHeight = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		MAG_WIDTH = (float) ScreenWidth / SCREEN_STANDARD_WIDTH;
		MAG_HEIGHT = (float) ScreenHeight / SCREEN_STANDARD_HEIGHT;

		// Log.d("====W/H", ScreenWidth + "/" + ScreenHeight + " / " + MAG_WIDTH
		// + " / " + MAG_HEIGHT + " / " + gDeviceDensity);

	}

	public void setHeight(int h) {
		MAG_HEIGHT = (float) h / SCREEN_STANDARD_HEIGHT;
	}

	static public Bitmap scale(Bitmap bitmap) {

		return Bitmap.createScaledBitmap(bitmap,
				(int) (bitmap.getWidth() * (MAG_WIDTH * gDeviceDensity)),
				(int) (bitmap.getHeight() * (MAG_HEIGHT * gDeviceDensity)),
				true);

	}

	static public Bitmap scaleFull(Bitmap bitmap) {

		return Bitmap.createScaledBitmap(bitmap, (int) (ScreenWidth),
				(int) (ScreenHeight), false);

	}

	static public int iX(int w) {
		return (int) (w * MAG_WIDTH);
	}

	static public int iY(int h) {
		return (int) (h * MAG_HEIGHT);
	}


	static String isNull(String param){
		if(param == null || "".equals(param) || "null".equals(param)) {
			return "";
		} else {
			return param;
		}
	}

	private static String getURLQuery(List<NameValuePair> params){
		StringBuilder stringBuilder = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params)
		{
			if (first)
				first = false;
			else
				stringBuilder.append("&");

			try {
				stringBuilder.append(URLEncoder.encode(pair.getName(), "UTF-8"));
				stringBuilder.append("=");
				stringBuilder.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				//e.printStackTrace();
			}
		}

		return stringBuilder.toString();
	}




    static public String Submit_old(String URL, List<NameValuePair> paramArr) {
        try {

            paramArr.add(new BasicNameValuePair("packagePhone", Info.phone));
            paramArr.add(new BasicNameValuePair("packageUser", Info.email));
            paramArr.add(new BasicNameValuePair("packageUserName", Info.name));
            paramArr.add(new BasicNameValuePair("packageName", Intro.class.getPackage().getName()));

            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            StringBuilder responseStringBuilder = new StringBuilder();
            responseStringBuilder.append("");

            try {
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(getURLQuery(paramArr));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                connection.connect();


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    int c;
                    while ((c = bufferedReader.read()) != -1) {
                        responseStringBuilder.append( (char)c ) ;
                    }
                    bufferedReader.close();

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return responseStringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(getApplicationContext(),
            // "네트워크에 문제가 있는것 같습니다.\n다시 시도하여 주십시오.", 0).show();
            return "";
        }
    }



    static public String Submit(String URL, List<NameValuePair> paramArr) {
		try {

			StrictMode.enableDefaults();

			paramArr.add(new BasicNameValuePair("packagePhone", Info.phone));
			paramArr.add(new BasicNameValuePair("packageUser", Info.email));
			paramArr.add(new BasicNameValuePair("packageUserName", Info.name));
			paramArr.add(new BasicNameValuePair("packageName", MyCardView.class.getPackage().getName()));

            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            StringBuilder responseStringBuilder = new StringBuilder();
            responseStringBuilder.append("");

			try {
                connection.setRequestMethod("POST");
				connection.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
				connection.setRequestProperty("Context-Type", "application/x-www-form-urlencoded;cahrset=UTF-8");
                connection.setDoInput(true);
                connection.setDoOutput(true);
				connection.setChunkedStreamingMode(0);

				OutputStream os = connection.getOutputStream();
				os.write(getURLQuery(paramArr).getBytes("UTF-8") ); // 출력 스트림에 출력.
				os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
				os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.\
/*
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bufferedWriter.write(getURLQuery(paramArr) );
                bufferedWriter.flush();
                bufferedWriter.close();
*/
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

					int c;
					while ((c = bufferedReader.read()) != -1) {
						responseStringBuilder.append((char) c);
					}
					bufferedReader.close();
				}else{
                	return "";
                }
  

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
			} finally {
				connection.disconnect();
            }
            return responseStringBuilder.toString(); 

			
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	static public String Submit(String URL, List<NameValuePair> paramArr, String encoding) {
		try {

			paramArr.add(new BasicNameValuePair("packagePhone", Info.phone));
			paramArr.add(new BasicNameValuePair("packageUser", Info.email));
			paramArr.add(new BasicNameValuePair("packageUserName", Info.name));
			paramArr.add(new BasicNameValuePair("packageName", MyCardView.class.getPackage().getName()));

            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            StringBuilder responseStringBuilder = new StringBuilder();
            responseStringBuilder.append("");
            
			try { 
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, encoding));
                bufferedWriter.write(getURLQuery(paramArr));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                connection.connect();
 
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    int c;
                    while ((c = bufferedReader.read()) != -1) {
                    	responseStringBuilder.append( (char)c ) ;  
                    }
                    bufferedReader.close();

                }
  

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
			} finally {
				connection.disconnect(); 
            }
            return responseStringBuilder.toString(); 

		} catch (Exception e) {
			e.printStackTrace();
			// Toast.makeText(getApplicationContext(),
			// "네트워크에 문제가 있는것 같습니다.\n다시 시도하여 주십시오.", 0).show();
			return "";
		}
	}

	public static Bitmap shrinkmethod(String file, int width, int height) {
		BitmapFactory.Options bitopt = new BitmapFactory.Options();
		bitopt.inJustDecodeBounds = true;
		Bitmap bit = BitmapFactory.decodeFile(file, bitopt);

		int h = (int) Math.ceil(bitopt.outHeight / (float) height);
		int w = (int) Math.ceil(bitopt.outWidth / (float) width);

		if (h > 1 || w > 1) {
			if (h > w) {
				bitopt.inSampleSize = h;

			} else {
				bitopt.inSampleSize = w;
			}
		}
		bitopt.inJustDecodeBounds = false;
		bit = BitmapFactory.decodeFile(file, bitopt);

		return bit;

	}

	static public boolean login(Context context) {
 
		SharedPreferences isFirstRunPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);

		Info.sound = isFirstRunPreferences.getBoolean("ALARM_SOUND", true);

		if (isFirstRunPreferences.getBoolean("AUTO_LOGIN", false)) {
			Info.email = isFirstRunPreferences.getString("AUTO_EMAIL", "");
			Info.pwd = isFirstRunPreferences.getString("AUTO_PWD", "");
		}

		if (isFirstRunPreferences.getBoolean("AUTO_LOGIN", false)
				&& !"".equals(Info.email) && !"".equals(Info.pwd)) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", Info.email));
			params.add(new BasicNameValuePair("pwd", Info.pwd));

			String result = Submit(
					"http://swcbizcard.nezip.co.kr/LoginAction.asp", params);

			if (!"".equals(result.trim())) {
				String[] messageCol = result.split("</C>");
				if (messageCol.length > 6) {

					Info.email = messageCol[0];
					Info.name = messageCol[1];

					Info.userName = messageCol[2];
					Info.userName01 = messageCol[2];
					Info.companyName = messageCol[2];
					Info.companyName01 = messageCol[2];
					Info.dept = messageCol[3];
					Info.grade = messageCol[4];
					Info.officeAddr = messageCol[5];
					Info.officeAddr01 = messageCol[5];
					Info.upmoo = messageCol[6];
					Info.officeTel = messageCol[7];
					Info.officeFax = messageCol[8];
					Info.mobile = messageCol[9];
					Info.mobile01 = messageCol[9];
					Info.workEmail = messageCol[10];
					Info.topPhone = messageCol[11];
					Info.homePage = messageCol[12];
					Info.keyword = messageCol[13];
					
					Info.companyLogo = messageCol[14];
					Info.myPhoto = messageCol[15];
					
					Info.regid = messageCol[16];
					Info.id = messageCol[17];

					Info.pwd = messageCol[18];
				    
					String regId = FirebaseInstanceId.getInstance().getToken();
					if (!"".equals(regId) && ("".equals(Info.regid) || !regId.equals(Info.regid))) {
						Info.regid = regId;
						params.clear();
						params.add(new BasicNameValuePair("userEmail", Info.email));
						params.add(new BasicNameValuePair("RegID", regId));
						result = Info.Submit("http://swcbizcard.nezip.co.kr/UserRegIDUpdateAction.asp", params);
					}
					
					Log.d("Info.regid", "regId : "+Info.regid);

					SharedPreferences.Editor editor = isFirstRunPreferences.edit();
					editor.putString("AUTO_EMAIL", Info.email);
					editor.putString("AUTO_PWD", Info.pwd);

					editor.commit();

				}

				if (!"".equals(Info.email)) {
					return true;
				} else {
					Info.email = "";
					Info.pwd = "";
					return false;
				}
			} else {
				Info.email = "";
				Info.pwd = "";
				return false;
			}
		} else {
			Info.email = "";
			Info.pwd = "";
			return false;
		}

	}

	static String fileupload(File orgfile, String filename, Handler timeHandler) {

		int serverResponseCode = 0;

		String dir = Info.email.replace("@", "_").replace(".", "_");
		dir = dir.replace("@", "_").replace(".", "_");
		dir = dir.replace("@", "_").replace(".", "_");
		String file = "/" + Info.email.substring(0, 1) + "/" + dir + "/" + filename;
		String urlPath = "http://swcbizcard.nezip.co.kr:8080/photo" + file;


		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 10 * 1024 * 1024;
		File sourceFile = orgfile;

		if (!sourceFile.isFile()) {
			return "";
		}
		else
		{
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(sourceFile);
				URL url = new URL("http://swcbizcard.nezip.co.kr:8080/admin/FileSave.asp");

				// Open a HTTP  connection to  the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

				conn.setRequestProperty("uploaded_file", filename);

				dos = new DataOutputStream(conn.getOutputStream());
				try  {
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"filePath\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes("\\" + Info.email.substring(0, 1) + "\\" + dir + "\\");
					dos.writeBytes(lineEnd);
				}    catch(Exception e)   { }
				try  {
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"fileName\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(URLEncoder.encode(filename, "utf-8"));
					dos.writeBytes(lineEnd);
				}    catch(Exception e)   { }


				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + filename + "\"" + lineEnd);
				dos.writeBytes(lineEnd);

				// create a buffer of  maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String result = conn.getResponseMessage();
				String serverResponseMessage = conn.getResponseMessage();

				//close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();


				Log.i("파일업로드", "결과 : "+result );

				if(serverResponseCode == 200){
					return urlPath;
				}


			} catch (MalformedURLException ex) {
			} catch (Exception e) {	}

			return "";

		} // End else block



	}

	// 파일확장자 가져오기
	public static String getExtension(String fileStr) {
		return fileStr
				.substring(fileStr.lastIndexOf(".") + 1, fileStr.length());
	}

	// 파일확장자 체크
	public static boolean getExtensionCheck(String fileStr) {

		// 파일확장자 가져오기
		String ext = getExtension(fileStr);

		if (ext.equals("") || ext == null) {
			return false;
		} else {
			ext = ext.toUpperCase();

			if (ext.equals("NET") || ext.equals("COM") || ext.equals("BAT")
					|| ext.equals("EXE") || ext.equals("JSP")
					|| ext.equals("CGI") || ext.equals("ASP")
					|| ext.equals("PHP") || ext.equals("PHP3")
					|| ext.equals("JAVA") || ext.equals("CLASS")
					|| ext.equals("ASPX") || ext.equals("JS")
					|| ext.equals("XML") || ext.equals("SQL")) {

				return false;
			} else {
				return true;
			}
		}
	}

	public static Bitmap GetImageFromURL(String strImageURL, Context context) {
		Bitmap imgBitmap = null;
		InputStream instream = null;
		HttpGet httpRequest = null;

		if ("".equals(strImageURL.trim())) {
			return null;
		}
		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int displayWidth = display.getWidth();
		int displayHeight = display.getHeight();

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.RGB_565;

		float widthScale = options.outWidth / displayWidth;
		float heightScale = options.outHeight / displayHeight;
		float scale = widthScale > heightScale ? widthScale : heightScale;

		if (scale > 8) {
			options.inSampleSize = 8;
		} else if (scale >= 6) {
			options.inSampleSize = 6;
		} else if (scale >= 4) {
			options.inSampleSize = 4;
		} else if (scale >= 2) {
			options.inSampleSize = 2;
		} else {
			options.inSampleSize = 1;
		}

		// options.inSampleSize = options.inSampleSize / 2;
		options.inJustDecodeBounds = false;

		try {

			URL url = new URL(strImageURL.replaceAll(" ", "%20"));
			httpRequest = new HttpGet(url.toURI());
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				response = null;
				httpclient = null;
				httpRequest = null;
				url = null;

				imgBitmap = null;

			} else {
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
						entity);
				instream = bufHttpEntity.getContent();

				imgBitmap = BitmapFactory.decodeStream(new FlushedInputStream(
						instream), new Rect(0, 0, 200, 200), options);

				bufHttpEntity = null;
				entity = null;
				httpclient = null;
				response = null;
				instream = null;
				httpRequest = null;
				url = null;
			}

		} catch (IOException e) {
			e.printStackTrace();
			httpRequest.abort();
			if (imgBitmap != null) {
				imgBitmap.recycle();
				imgBitmap = null;
			}

		} catch (IllegalStateException e) {
			e.printStackTrace();
			httpRequest.abort();
			if (imgBitmap != null) {
				imgBitmap.recycle();
				imgBitmap = null;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			httpRequest.abort();
			if (imgBitmap != null) {
				imgBitmap.recycle();
				imgBitmap = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			httpRequest.abort();
			if (imgBitmap != null) {
				imgBitmap.recycle();
				imgBitmap = null;
			}
		} finally {
			System.gc();
			if (instream != null) {
				try {
					instream.close();
				} catch (IOException e) {
					// Log.w("error closing stream", e);
				}
			}
		}

		if (imgBitmap != null) {
			return imgBitmap;
		} else {
			return null;
		}

		// return imgBitmap;
	}

	static class FlushedInputStream extends FilterInputStream {

		public FlushedInputStream(InputStream inputStream) {

			super(inputStream);

		}

		@Override
		public long skip(long n) throws IOException {

			long totalBytesSkipped = 0L;

			while (totalBytesSkipped < n) {

				long bytesSkipped = in.skip(n - totalBytesSkipped);

				if (bytesSkipped == 0L) {

					int b = read();

					if (b < 0) {

						break; // we reached EOF

					} else {

						bytesSkipped = 1; // we read one byte

					}

				}

				totalBytesSkipped += bytesSkipped;

			}

			return totalBytesSkipped;

		}

	}

	public static Options getBitmapSize(Options options) {
		int targetWidth = 0;
		int targetHeight = 0;

		if (options.outWidth > options.outHeight) {
			targetWidth = (int) (600 * 1.3);
			targetHeight = 600;
		} else {
			targetWidth = 600;
			targetHeight = (int) (600 * 1.3);
		}

		Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math
				.abs(options.outWidth - targetWidth);
		if (options.outHeight * options.outWidth * 2 >= 16384) {
			double sampleSize = scaleByHeight ? options.outHeight
					/ targetHeight : options.outWidth / targetWidth;
			options.inSampleSize = (int) Math.pow(2d,
					Math.floor(Math.log(sampleSize) / Math.log(2d)));
		}
		options.inJustDecodeBounds = false;
		options.inTempStorage = new byte[16 * 1024];

		return options;
	}

	public static Bitmap imgRotate(Bitmap bmp, int pr) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();

		Matrix matrix = new Matrix();
		matrix.postRotate(pr);

		Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height,
				matrix, true);
		bmp.recycle();

		return resizedBitmap;

	}

	public static void performDial(Activity activity, String phoneNo) {
		if (!"".equals(phoneNo)) {
			try {
				activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri
						.parse("tel:" + phoneNo)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendMessage(String title, String msg, String registrationId) {
		try {
			sendPostToFCM(title, registrationId, msg);
		} catch (Exception e) {
			Log.e("FCM", "IOException " + e.getMessage());
		}
	}

 
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float rPx) {
		if (bitmap == null) {
			return bitmap;
		} else {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = rPx/2;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		}

	}

	public static Bitmap getCircleBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return bitmap;
		} else {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			int size = (bitmap.getWidth() / 2);
			canvas.drawCircle(size, size, size, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		}

	}

	void installApp(Context con, String fileName) {
		Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(
				Uri.parse("file://" + fileName),
				"application/vnd.android.package-archive");
		con.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	void removeApp(Context con, String packageName) {
		Intent intent = new Intent(Intent.ACTION_DELETE).setData(Uri
				.parse("package:" + packageName));
		con.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	boolean getApplicationInstalled(Context con, String pkgName) {
		ApplicationInfo appInfo = null;

		final PackageManager pm = con.getPackageManager();
		try {
			appInfo = pm.getApplicationInfo(pkgName,
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		if (appInfo != null)
			return true;
		else
			return false;
	}


	public static Cursor getPhoneList(Context con) 
	{
		ContactDBHelper phonedbhelper = new ContactDBHelper(con);
		SQLiteDatabase db = phonedbhelper.getWritableDatabase();
		db.beginTransaction();
		db.execSQL(" DELETE FROM PHONE ");
		db.setTransactionSuccessful();
		db.endTransaction();
		
		db.beginTransaction();
		
		Cursor c = ((Activity) con).managedQuery( Phone.CONTENT_URI, 
                new String[]{ Phone._ID, Phone.DISPLAY_NAME, Phone.DATA1 },
                null, 
                null, 
                Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC, " + Phone.DATA1 + " COLLATE LOCALIZED ASC " );
		
		
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {
			String name = c.getString(c.getColumnIndex(Phone.DISPLAY_NAME)).trim();
			String phone = c.getString(c.getColumnIndex(Phone.DATA1)).replaceAll("-", "").trim();
			
			String sql = "INSERT INTO PHONE ( DISPLAY_NAME, PHONE_NUMBER ) VALUES ( '"
					+ name
					+ "', '"
					+ phone
					+ "');";
			try {
				db.execSQL(sql);
				//Log.d("SQL", sql);
			} catch (SQLException e) {
				con.deleteDatabase("phone.db");
				break;
			}

			c.moveToNext();
		}
		c.close();

		// 디비 나만 쓸거야 끝
		db.setTransactionSuccessful();
		db.endTransaction();
		
		String sql = "SELECT DISTINCT DISPLAY_NAME, PHONE_NUMBER FROM PHONE ";
		sql = sql + " ORDER BY DISPLAY_NAME ASC, PHONE_NUMBER ASC ";

		Cursor r = db.rawQuery(sql, null);
		r.moveToFirst();
		
		db.close();
		phonedbhelper.close();
		
		return r;
	}


	public class ChatData {
		private String userName;
		private String message;
		private String title;
		private String userToken;

		public ChatData() { }

		public ChatData(String userName, String message) {
			this.userName = userName;
			this.message = message;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
	private static final String SERVER_KEY = "AAAAhSzYAuc:APA91bESyyBkERgwCbIk_h4xDHHXgXnceYFuqdj0LUfF3KXAyQ_pxJk5nxF37FB5qFzJZBYBG7ZPQdGTUEk7TZQMBmnYu7vcIQ_HquT5QorPtOKCGzA4PJPNuP3PfSHFuudV2WRHgzFl";

	private static void sendPostToFCM(final String title, final String toUserFcmToken, final String message) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// FMC 메시지 생성 start
					JSONObject root = new JSONObject();
					JSONObject notification = new JSONObject();
					notification.put("message", message);
					notification.put("ticker", title);
					root.put("notification", notification);
					root.put("to", toUserFcmToken);
					// FMC 메시지 생성 end

					URL Url = new URL(FCM_MESSAGE_URL);
					HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
					conn.setRequestProperty("Accept", "application/json");
					conn.setRequestProperty("Content-type", "application/json");
					OutputStream os = conn.getOutputStream();
					os.write(root.toString().getBytes("utf-8"));
					os.flush();
					conn.getResponseCode();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


}
