package com.nezip.bizcard1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.nezip.bizcard1.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

public class PostPopup extends Activity {

	PostDBHelper postdbhelper;
	// public static String postNo="";
	// public static String postAddr="";
	public static String tpy = "";

	@Override
	public void onBackPressed() {
		setResult(-1);
		finish();

	}

	ProgressDialog progressDialog;
	MyAdapter MyAdapter;

	ArrayList<PostData> postaddrs = new ArrayList<PostData>();
	int lastPosition = 0; // ListView 마지막 position

	public boolean appStart = false;
	public boolean loadingflag = true;
	public static boolean isLoading = false;

	Handler mHandler;

	ListView postList;
	EditText addr;

	changeString changeString = new changeString();
	boolean firstSettingEnd = false;

	// 하단 탭버튼
	Button submitBtn, closeBtn, clearBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent_rec = getIntent();
		this.tpy = Info.isNull(intent_rec.getStringExtra("TYPE")).toString();

		appStart = true;
		loadingflag = true;

		setContentView(R.layout.postpopup);

		// 화면 고정
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		postList = (ListView) findViewById(R.id.postList);
		// postList.startAnimation(new
		// AnimationUtils().loadAnimation(PostPopup.this, R.anim.push_left_in));

		addr = (EditText) findViewById(R.id.addr);

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					if (!firstSettingEnd) {

						MyAdapter = new MyAdapter(PostPopup.this,
								R.layout.postitem, postaddrs);
						postList.setAdapter(MyAdapter);
						postList.setFocusable(false);
						firstSettingEnd = true;

					}

					if (postaddrs.isEmpty()) {
						Toast.makeText(PostPopup.this, "자료가 없습니다.", 0).show();
					}

				}
			}
		};

		// 하단 탭버튼
		Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					setResult(-1);
					finish();
				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}
			}
		});

		Button searchBtn = (Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {


					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(addr.getWindowToken(),
									0);
						}
					}, 500);

					loadingflag = true;
					if (!isLoading && loadingflag) {

						MyAsyncTask at = new MyAsyncTask();
						try {
							at.execute();
						} catch (Exception e) {

						}

					}
				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}

			}
		});

		postList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

				//

				if (firstVisibleItem == 0 && loadingflag) {
					// Toast.makeText(PostPopup.this,
					// "onScroll firstVisibleItem : " + firstVisibleItem,
					// 0).show();

					if (!isLoading && loadingflag) {

						// MyAsyncTask at = new MyAsyncTask();
						// at.execute();

						// Toast.makeText(PostPopup.this, "onScroll page : " +
						// firstVisibleItem, 0).show();

					}
				}

			}
		});
	}

	// 리스트 어댑터
	public class MyAdapter extends ArrayAdapter<PostData> {
		Context maincon; 
		ArrayList<PostData> arSrc;
		int layout;

		public MyAdapter(Context context, int alayout,
				ArrayList<PostData> aarSrc) {
			super(context, alayout, aarSrc);
			maincon = context; 
			arSrc = aarSrc;
			layout = alayout;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

				if(lastPosition < position) lastPosition = position;
			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) maincon.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(layout, parent, false);
			}
			try {
				final TextView txt1 = (TextView) convertView
						.findViewById(R.id.postNo);
				final TextView txt2 = (TextView) convertView
						.findViewById(R.id.postAddr);
				final TextView txt3 = (TextView) convertView
						.findViewById(R.id.useAddr);

				txt1.setText(changeString.dbTostr(arSrc.get(position).POST_NO));
				txt2.setText(changeString.dbTostr(arSrc.get(position).POST_ADDR));
				txt3.setText(changeString.dbTostr(arSrc.get(position).USE_ADDR));

				final Button postItem = (Button) convertView
						.findViewById(R.id.postBtn);
				postItem.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							Info.SelectPostNo = txt1.getText().toString();
							Info.SelectPostAddr = txt3.getText().toString();

							setResult(Activity.RESULT_OK);
							finish();
						} catch (OutOfMemoryError e) {
							// System.gc();
						} catch (Exception e) {
						}
					}
				});

			} catch (Exception e) {

			}

			return convertView;
		}

		@Override
		public void add(PostData object) {
			// TODO Auto-generated method stub
			super.add(object);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arSrc.size();
		}

		@Override
		public PostData getItem(int arg0) {
			// TODO Auto-generated method stub
			return arSrc.get(arg0);
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

	}

	class MyAsyncTask extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			// 작업을 시작하기 전 할일
			isLoading = true;
			loadingflag = false;
			progressDialog = new ProgressDialog(PostPopup.this);
			progressDialog.setMessage("로딩중입니다.\n잠시만 기다려주세요. ");
			progressDialog.setCancelable(false);
			progressDialog.show();

			if (!firstSettingEnd) {
				postaddrs.clear();
			} else {
				if(MyAdapter != null) MyAdapter.clear();
			}

			lastPosition = 0;


			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				loadXML();

			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// return false;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// return false;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// return false;
			} catch (Exception e) {
				e.printStackTrace();
				// return false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			dbload();

			super.onPostExecute(result);

			loadingflag = true;
			isLoading = false;

			// 작업이 완료 된 후 할일
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();

		}

		@Override
		protected void onCancelled() {
			// 작업 취소
			loadingflag = true;
			isLoading = false;
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			super.onCancelled();
		}

	}

	void loadXML() throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", addr.getText().toString()));
		String result = "";

		boolean sqlRun = true;

		String sql = "";

		postdbhelper = new PostDBHelper(PostPopup.this);
		SQLiteDatabase db = postdbhelper.getWritableDatabase();
		db.beginTransaction();

		sql = "DELETE FROM POST; ";

		db.execSQL(sql);

		// 디비 나만 쓸거야 끝
		db.setTransactionSuccessful();
		db.endTransaction();

		db.beginTransaction();

		result = Info.Submit("http://swcbizcard.nezip.co.kr/PostAction.asp", params);
		if (!"".equals(result.trim())) {

			String[] addrRow = result.split("</R>");
			for (int i = 0; i < addrRow.length; i++) {
				// Log.i("XML", addrRow[i]);

				if (!"".equals(addrRow[i].trim())) {
					String[] addrCol = addrRow[i].split("</C>");
					if (addrCol.length > 2) {

						sql = "INSERT INTO POST( POST_NO, POST_ADDR, USE_ADDR ) VALUES ( '"
								+ addrCol[0].replaceAll("'", "''")
								+ "', '"
								+ addrCol[1].replaceAll("'", "''")
								+ "', '"
								+ addrCol[2].replaceAll("'", "''") + "');";

						try {
							db.execSQL(sql);
						} catch (SQLException e) {
							sqlRun = false;
							break;
						}

					}
				}

			}
		}

		// 디비 나만 쓸거야 끝
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		postdbhelper.close();

		if (!sqlRun) {
			deleteDatabase("post.db");
		}

	}

	void dbload() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		lastPosition = 0;

		try {
			String sql = "";
			PostDBHelper postdbhelper = new PostDBHelper(this);
			db = postdbhelper.getWritableDatabase();
			// 디비 나만 쓸거야 시작
			cursor = null;

			sql = "SELECT POST_NO, POST_ADDR, USE_ADDR FROM POST ORDER BY POST_NO ASC ";

			cursor = db.rawQuery(sql, null);
			// Log.i("SQL", sql);
			while (cursor.moveToNext()) {

				if (!firstSettingEnd) {
					postaddrs.add(new PostData(cursor.getString(0), cursor
							.getString(1), cursor.getString(2)));
				} else {
					MyAdapter.add(new PostData(cursor.getString(0), cursor
							.getString(1), cursor.getString(2)));
				}

			}

			if (firstSettingEnd) MyAdapter.notifyDataSetChanged();

			// 디비 나만 쓸거야 끝

			cursor.close();
			cursor = null;

			db.close();
			db = null;

			postdbhelper.close();
			postdbhelper = null;
		} catch (Exception e) {
			e.printStackTrace();
			deleteDatabase("post.db");
		} finally {
			if (db != null) {
				db.close();
				db = null;
			}

			if (cursor != null) {
				cursor.close();
				cursor = null;
			}

			if (postdbhelper != null) {
				postdbhelper.close();
				postdbhelper = null;
			}

			mHandler.sendEmptyMessage(1);

		}
	}

	@Override
	public void onPause() {

		// Toast.makeText(PostPopup.this, "종료 : ", 0).show();
		appStart = false;
		loadingflag = false;

		super.onPause();

	}

	@Override
	public void onStop() {

		// Toast.makeText(PostPopup.this, "종료 : ", 0).show();
		appStart = false;
		loadingflag = false;

		super.onStop();

	}

	@Override
	public void onDestroy() {

		// Toast.makeText(PostPopup.this, "종료 : ", 0).show();
		appStart = false;
		loadingflag = false;

		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		super.onDestroy();

	}

	@Override
	public void onStart() {

		loadingflag = true;
		appStart = true;

		super.onStart();

	}

	@Override
	public void onResume() {

		loadingflag = true;
		appStart = true;

		super.onResume();

	}

	@Override
	public void onRestart() {

		loadingflag = true;
		appStart = true;

		super.onRestart();

	}

}
