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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

public class PointList extends Activity {

	Spinner pointState;
	String[] arChapter = { "    ", "YES : 승인", "NO  : 대기", "DEL : 삭제" };
	ArrayAdapter<CharSequence> mAdapter;
	boolean mInitSelection = true;

	// 좌우 스크롤을 위한 변수
	float firstX;
	float firstY;
	float lastX;
	float lastY;
	boolean right = false;
	LinearLayout rect;

	String intent_val;
	Intent intentRev;

	ImageButton prev;
	ImageButton next;

	PointDBHelper pointdbhelper;

	LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(0, 0);

	@Override
	public void onBackPressed() {
		finish();
	}

	ProgressDialog progressDialog;
	MyAdapter MyAdapter;

	ArrayList<PointData> points = new ArrayList<PointData>();

	int lastPosition = 0; // ListView 마지막 position

	boolean loadingflag = true;
	boolean isLoading = false;

	Handler mHandler;

	ListView pointList;

	int position = 5;

	static int totalPage = 1; // 총 페이지

	LinearLayout pageNavi;
	TextView pageIndex;

	changeString changeString = new changeString();
	boolean firstSettingEnd = false;

	// 하단 탭버튼
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pointlist);

		// 횡 스크롤뷰
		rect = (LinearLayout) findViewById(R.id.rightToleft);

		intentRev = getIntent();

		// 화면 고정
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		pointState = (Spinner) findViewById(R.id.pointState);
		pointState.setPrompt("구매이용권 검색조건");

		mAdapter = new ArrayAdapter<CharSequence>(this,
				android.R.layout.simple_spinner_item, arChapter);
		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pointState.setAdapter(mAdapter);
		pointState.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				if (mInitSelection) {
					mInitSelection = false;
					pointState.setSelection(2);
				} else {

					Info.PointPage = 1;
					String cnt = getData(0);

					//Toast.makeText(PointList.this, cnt, 0).show();
					//return;

					//*

					try{
						PointList.totalPage = (int) (Double.parseDouble(cnt) + (cnt.indexOf('.') > 0 ? 1 : 0));
					}catch(Exception e){
						PointList.totalPage = 0;
					}


					pageIndex.setText(Info.PointPage + " / " + PointList.totalPage);
					if (PointList.totalPage > 1) {
						pageNavi.setVisibility(View.VISIBLE);
					}

					MyAsyncTask at = new MyAsyncTask();
					try {
						at.execute();
					} catch (Exception e) {

					}
					//*/

				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		pointList = (ListView) findViewById(R.id.pointList);
		pointList.startAnimation(new AnimationUtils().loadAnimation(
				PointList.this, R.anim.push_left_in));

		pageNavi = (LinearLayout) findViewById(R.id.pageNavi);
		pageIndex = (TextView) findViewById(R.id.pageIndex);

		prev = (ImageButton) findViewById(R.id.prev);
		next = (ImageButton) findViewById(R.id.next);

		prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					prevBtnClick();
				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}
			}
		});

		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					nextBtnClick();

				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}
			}
		});

		final EditText gotopage = (EditText) findViewById(R.id.gotopage);
		final Button gotoBtn = (Button) findViewById(R.id.gotoBtn);
		gotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (isLoading)
						return;
					int gpage = Integer.parseInt("0"
							+ gotopage.getText().toString());
					if (gpage <= PointList.totalPage && gpage > 0) {
						Info.PointPage = gpage;
 
						MyAsyncTask at = new MyAsyncTask();
						try {
							at.execute();
						} catch (Exception e) {

						}

						pageIndex.setText(Info.PointPage + " / " + PointList.totalPage);
						if (Info.PointPage == 1) {
							prev.setBackgroundResource(R.drawable.han_photo_icon_prev_b);
						} else {
							prev.setBackgroundResource(R.drawable.han_photo_icon_prev_a);
						}
						if (Info.PointPage == PointList.totalPage) {
							next.setBackgroundResource(R.drawable.han_photo_icon_next_b);
						} else {
							next.setBackgroundResource(R.drawable.han_photo_icon_next_a);
						}
					}
				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}
			}
		});

		final Button searchBtn = (Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(),
							PointSearchText.class);
					intent.putExtra("SEARCHTEXT", Info.isNull(intentRev.getStringExtra("SEARCHTEXT"))); 

					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}
			}
		});

		if (Info.PointPage <= 1) {
			prev.setBackgroundResource(R.drawable.han_photo_icon_prev_b);
		} else {
			prev.setBackgroundResource(R.drawable.han_photo_icon_prev_a);
		}
		if (Info.PointPage >= PointList.totalPage) {
			next.setBackgroundResource(R.drawable.han_photo_icon_next_b);
		} else {
			next.setBackgroundResource(R.drawable.han_photo_icon_next_a);
		}

		mHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 1) {

					if (!firstSettingEnd) {

						MyAdapter = new MyAdapter(PointList.this,
								R.layout.pointitem, points);
						pointList.setAdapter(MyAdapter);
						pointList.setFocusable(false);
						firstSettingEnd = true;

					}

					if (points.isEmpty()) {
						Toast.makeText(PointList.this, "자료가 없습니다.", 0).show();
					} else {
						pointList.setSelection(Info.PointPosition);
					}

				}

			}
		};

		pointList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

				if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
						&& !isLoading) {
					loadingflag = false;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				/*
				 * int totalCnt = firstVisibleItem + visibleItemCount;
				 * 
				 * if( !loadingflag && totalCnt > 0 && totalCnt ==
				 * (totalItemCount) && lastItemIdx <= totalCnt){
				 * 
				 * // 로딩 시작 loadingflag = true; //listNumber += itemListCnt;
				 * lastItemIdx = firstVisibleItem + visibleItemCount; page++;
				 * 
				 * //Log.i("onScroll : ",""+page);
				 * 
				 * 
				 * MyAsyncTask at = new MyAsyncTask(); at.execute();
				 * 
				 * }
				 */

			}
		});

	}

	// 리스트 어댑터
	public class MyAdapter extends ArrayAdapter<PointData> {
		Context maincon; 
		ArrayList<PointData> arSrc;
		int layout;

		public MyAdapter(Context context, int alayout,
				ArrayList<PointData> aarSrc) {
			super(context, alayout, aarSrc);
			maincon = context; 
			arSrc = aarSrc;
			layout = alayout;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) maincon.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(layout, parent, false);

			}
				if(lastPosition < position) lastPosition = position;
			try {
				final TextView userEmail = (TextView) convertView
						.findViewById(R.id.userEmail);
				final TextView userName = (TextView) convertView
						.findViewById(R.id.userName);
				final TextView point = (TextView) convertView
						.findViewById(R.id.point);
				final TextView insertDate = (TextView) convertView
						.findViewById(R.id.insertDate);
				final TextView inputName = (TextView) convertView
						.findViewById(R.id.inputName);
				final TextView inputTime = (TextView) convertView
						.findViewById(R.id.inputTime);
				final TextView checkOk = (TextView) convertView
						.findViewById(R.id.checkOk);

				userName.setText(changeString.dbTostr(arSrc.get(position).USER_NAME));
				userEmail
						.setText(changeString.dbTostr(arSrc.get(position).USER_EMAIL));
				point.setText(changeString.dbTostr(arSrc.get(position).POINT));
				inputName
						.setText(changeString.dbTostr(arSrc.get(position).INPUT_NAME));
				inputTime
						.setText(changeString.dbTostr(arSrc.get(position).INPUT_TIME));
				checkOk.setText(changeString.dbTostr(arSrc.get(position).CHECK_OK));
				insertDate
						.setText(changeString.dbTostr(arSrc.get(position).INSERT_DATE));

				final LinearLayout managerLayout = (LinearLayout) convertView
						.findViewById(R.id.managerLayout);
				if (!"corp".equals(Info.email)
						&& !"rclee".equals(Info.email)) {
					managerLayout.setVisibility(View.GONE);
				}

				final Button okBtn = (Button) convertView
						.findViewById(R.id.okBtn);
				okBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {

							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("id", arSrc
									.get(position).ID));
							params.add(new BasicNameValuePair("point",
									point.getText().toString()));
							params.add(new BasicNameValuePair("command",
									"YES"));

							String result = Info
									.Submit("http://swcbizcard.nezip.co.kr/PointUpdateAction.asp",
											params);
							if ("YES".equals(result.trim())) {
								Toast.makeText(PointList.this,
										"포인트 저장 완료.", 0).show();
								checkOk.setText("YES");
								arSrc.get(position).setCheckOk("YES");
								okBtn.setTextColor(Color.RED);
							} else {
								// Log.i("RESULT", result);
								Toast.makeText(PointList.this,
										"포인트 저장 실패.", 0).show();
							}
						} catch (OutOfMemoryError e) {
							// System.gc();
						} catch (Exception e) {
						}
					}
				});

				final Button waitBtn = (Button) convertView
						.findViewById(R.id.waitBtn);
				waitBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {

							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("id",
									changeString.dbTostr(arSrc
											.get(position).ID)));
							params.add(new BasicNameValuePair("point",
									point.getText().toString()));
							params.add(new BasicNameValuePair("command",
									"NO"));

							String result = Info
									.Submit("http://swcbizcard.nezip.co.kr/PointUpdateAction.asp",
											params);
							if ("YES".equals(result.trim())) {
								Toast.makeText(PointList.this,
										"포인트 저장 완료.", 0).show();
								checkOk.setText("NO");
								arSrc.get(position).setCheckOk("NO");
								waitBtn.setTextColor(Color.RED);
							} else {

								// Log.i("RESULT", result);
								Toast.makeText(PointList.this,
										"포인트 저장 실패.", 0).show();
							}
						} catch (OutOfMemoryError e) {
							// System.gc();
						} catch (Exception e) {
						}
					}
				});

				final Button delBtn = (Button) convertView
						.findViewById(R.id.delBtn);
				delBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							new AlertDialog.Builder(PointList.this)
									.setMessage("해당 구매포인트를 삭제 하시겠습니가?")
									.setNegativeButton(
											"취소",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {

												}
											})
									.setPositiveButton(
											"확인",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {

													List<NameValuePair> params = new ArrayList<NameValuePair>();
													params.add(new BasicNameValuePair(
															"id",
															changeString
																	.dbTostr(arSrc
																			.get(position).ID)));
													params.add(new BasicNameValuePair(
															"point",
															point.getText()
																	.toString()));

													if ("DEL".equals(arSrc
															.get(position).CHECK_OK)) {
														params.add(new BasicNameValuePair(
																"command",
																"OK"));
													} else {
														params.add(new BasicNameValuePair(
																"command",
																"DEL"));
													}

													String result = Info
															.Submit("http://swcbizcard.nezip.co.kr/PointUpdateAction.asp",
																	params);
													if ("YES".equals(result
															.trim())) {
														Toast.makeText(
																PointList.this,
																"구매포인트 삭제 완료.",
																0).show();
														checkOk.setText("DEL");
														arSrc.get(position)
																.setCheckOk(
																		"DEL");
														delBtn.setTextColor(Color.RED);
													} else {

														// Log.i("RESULT",
														// result);
														Toast.makeText(
																PointList.this,
																"구매포인트 삭제 실패.",
																0).show();
													}

												}
											})

									.show();
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
		public void add(PointData object) {
			// TODO Auto-generated method stub
			super.add(object);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arSrc.size();
		}

		@Override
		public PointData getItem(int arg0) {
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
		protected void onPostExecute(Void result) {

			// 로딩 완료
			// loadingflag = false;
			isLoading = false;

			// Log.i("onPostExecute / dbload: ",""+page);
			dbload();

			// 작업이 완료 된 후 할일
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {

			// 작업을 시작하기 전 할일
			isLoading = true;
			progressDialog = new ProgressDialog(PointList.this);
			progressDialog.setMessage("로딩중입니다.\n잠시만 기다려주세요. ");
			progressDialog.setCancelable(false);
			progressDialog.show();

			if (!firstSettingEnd) {
				points.clear();
			} else {
				if(MyAdapter != null) MyAdapter.clear();
			}

			lastPosition = 0;
 
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				// Log.i("doInBackground / loadXML : ",""+page);
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
		protected void onCancelled() {
			// 작업 취소
			loadingflag = true;
			isLoading = false;
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			super.onCancelled();
		}
	}

	String getData(int page) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page", "" + page));

		params.add(new BasicNameValuePair("search", Info.isNull(intentRev.getStringExtra("SEARCHTEXT"))));
		params.add(new BasicNameValuePair("userEmail", Info.email));
		params.add(new BasicNameValuePair("pointState", pointState.getSelectedItem().toString().substring(0, 3).trim()));

		String result = Info.Submit("http://swcbizcard.nezip.co.kr/PointListAction.asp", params);

		return result.trim();
	}

	void loadXML() throws Exception {
		if (PointList.totalPage < Info.PointPage) {
			Info.PointPage = 1;
		}

		String sql = "";

		pointdbhelper = new PointDBHelper(PointList.this);
		SQLiteDatabase db = pointdbhelper.getWritableDatabase();
		db.beginTransaction();

		sql = "DELETE FROM POINTS; ";

		db.execSQL(sql);
		db.setTransactionSuccessful();
		db.endTransaction();

		boolean sqlRun = true;

		db.beginTransaction();

		String result = getData(Info.PointPage);
		if (!"".equals(result.trim())) {

			String[] messageInputTime = result.split("</R>");

			for (int i = 0; i < messageInputTime.length; i++) {
				// Log.i("XML", messageInputTime[i]);
				if (!"".equals(messageInputTime[i].trim())) {
					String[] messageCol = messageInputTime[i].split("</C>");
					if (messageCol.length > 7) {

						sql = "INSERT INTO POINTS ( ID, USER_EMAIL, POINT, INPUT_NAME, INPUT_TIME, CHECK_OK, INSERT_DATE, USER_NAME) VALUES ( '"
								+ messageCol[0].replaceAll("'", "''")
								+ "', '"
								+ messageCol[1].replaceAll("'", "''")
								+ "', '"
								+ messageCol[2].replaceAll("'", "''")
								+ "', '"
								+ messageCol[3].replaceAll("'", "''")
								+ "', '"
								+ messageCol[4].replaceAll("'", "''")
								+ "', '"
								+ messageCol[5].replaceAll("'", "''")
								+ "', '"
								+ messageCol[6].replaceAll("'", "''")
								+ "', '"
								+ messageCol[7].replaceAll("'", "''") + "');";
						// Log.i("SQL", sql);
						try {
							db.execSQL(sql);
						} catch (SQLException e) {
							sqlRun = false;
						}

					}
				}

			}

		}

		// 디비 나만 쓸거야 끝
		db.setTransactionSuccessful();
		db.endTransaction();

		db.close();
		pointdbhelper.close();

		if (!sqlRun) {
			deleteDatabase("points.db");
		}

	}

	void dbload() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		lastPosition = 0;

		try {
			String sql = "";
			PointDBHelper pointdbhelper = new PointDBHelper(this);
			db = pointdbhelper.getWritableDatabase();
			// 디비 나만 쓸거야 시작
			cursor = null;
			sql = "SELECT ID, USER_EMAIL, USER_NAME, POINT, INPUT_NAME, INPUT_TIME, CHECK_OK, INSERT_DATE FROM POINTS ";

			cursor = db.rawQuery(sql, null);
			// Log.i("SQL", sql);
			while (cursor.moveToNext()) {

				if (!firstSettingEnd) {
					points.add(new PointData(cursor.getString(0), cursor
							.getString(1), cursor.getString(2), cursor
							.getString(3), cursor.getString(4), cursor
							.getString(5), cursor.getString(6), cursor
							.getString(7)));
				} else {
					MyAdapter.add(new PointData(cursor.getString(0), cursor
							.getString(1), cursor.getString(2), cursor
							.getString(3), cursor.getString(4), cursor
							.getString(5), cursor.getString(6), cursor
							.getString(7)));
				}

			}

			if (firstSettingEnd) MyAdapter.notifyDataSetChanged();

			cursor.close();
			cursor = null;

			db.close();
			db = null;

			pointdbhelper.close();
			pointdbhelper = null;
		} catch (Exception e) {
			e.printStackTrace();
			deleteDatabase("points.db");
		} finally {
			if (db != null) {
				db.close();
				db = null;
			}

			if (cursor != null) {
				cursor.close();
				cursor = null;
			}

			if (pointdbhelper != null) {
				pointdbhelper.close();
				pointdbhelper = null;
			}

			mHandler.sendEmptyMessage(1);

		}
	}

	@Override
	public void onDestroy() {

		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		super.onDestroy();

	}

	// 좌우 스크롤을 만들어 봅시당 ㄴ ㅑㅎ ㅏㅎ ㅏ
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			firstX = ev.getX();
			firstY = ev.getY();
			break;

		case MotionEvent.ACTION_UP:
			lastX = ev.getX();
			lastY = ev.getY();
			if ((firstX >= rect.getLeft()) && (firstX <= rect.getRight())
					&& (firstY - 20 >= rect.getTop())
					&& (firstY - 20 <= rect.getBottom())) {
				if (Math.abs(firstX - lastX) > ((rect.getRight() / 2) * 1)) {
					boolean plus = lastX - firstX < 0;

					MoveNext(plus);
 
				}

			}

			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@SuppressLint("NewApi")
	void MoveNext(boolean flag) {

		if (flag) {
			right = false;

			if (Info.PointPage < PointList.totalPage) {
				nextBtnClick();
			}

		} else {
			right = true;

			if (Info.PointPage > 1) {
				prevBtnClick();
			}
		}
	}

	@Override
	public void finish() {

		super.finish();
 
	}

	public void prevBtnClick() {
		if (isLoading)
			return;
		if (Info.PointPage > 1) {
			Info.PointPage--;
			Info.PointPosition = 0;
 
			MyAsyncTask at = new MyAsyncTask();
			try {
				at.execute();
			} catch (Exception e) {

			}

			pageIndex.setText(Info.PointPage + " / " + PointList.totalPage);
			if (Info.PointPage == 1) {
				prev.setBackgroundResource(R.drawable.han_photo_icon_prev_b);
			} else {
				prev.setBackgroundResource(R.drawable.han_photo_icon_prev_a);
			}
			if (Info.PointPage == PointList.totalPage) {
				next.setBackgroundResource(R.drawable.han_photo_icon_next_b);
			} else {
				next.setBackgroundResource(R.drawable.han_photo_icon_next_a);
			}
		} else {
			prev.setBackgroundResource(R.drawable.han_photo_icon_prev_b);
		}
	}

	public void nextBtnClick() {
		if (isLoading)
			return;
		if (Info.PointPage < PointList.totalPage) {
			Info.PointPage++;
			Info.PointPosition = 0;

 
			MyAsyncTask at = new MyAsyncTask();
			try {
				at.execute();
			} catch (Exception e) {

			}

			pageIndex.setText(Info.PointPage + " / " + PointList.totalPage);
			if (Info.PointPage == 1) {
				prev.setBackgroundResource(R.drawable.han_photo_icon_prev_b);
			} else {
				prev.setBackgroundResource(R.drawable.han_photo_icon_prev_a);
			}
			if (Info.PointPage == PointList.totalPage) {
				next.setBackgroundResource(R.drawable.han_photo_icon_next_b);
			} else {
				next.setBackgroundResource(R.drawable.han_photo_icon_next_a);
			}
		} else {
			next.setBackgroundResource(R.drawable.han_photo_icon_next_b);
		}
	}

}
