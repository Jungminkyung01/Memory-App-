package com.nezip.bizcard1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class BizCardGrid extends Activity {

	static int curPage = Info.BizCardListPage;

	// 좌우 스크롤을 위한 변수
	float firstX;
	float firstY;
	float lastX;
	float lastY;
	boolean right = false;
	LinearLayout rect;

	static Intent intentRev;

	static ImageButton prev;
	static ImageButton next;

	static UserDBHelper userdbhelper;

	LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(0, 0);

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MyCardView.class);
		finish();
		startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	ProgressDialog progressDialog;
	static MyAdapter MyAdapter;

	ArrayList<UserData> members = new ArrayList<UserData>();

	int lastPosition = 0; // ListView 마지막 position

	boolean loadingflag = true;
	static boolean isLoading = false;

	Handler mHandler;

	GridView userGrid;

	LinearLayout pageNavi;
	static TextView pageIndex;

	changeString changeString = new changeString();
	boolean firstSettingEnd = false;

	// 하단 탭버튼
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if("".equals(Info.email)){
	    	if(!Info.login(getApplicationContext())){
	    		Intent intent = new Intent(this, Login1th.class);
	            finish();
	            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
				return;
	    	}
	    }
		
		setContentView(R.layout.bizcardlist_2100_card);




		// 횡 스크롤뷰
		rect = (LinearLayout) findViewById(R.id.rightToleft);
		deleteDatabase("corpMember.db");
		Info.replay = true;
		intentRev = getIntent();

		// 화면 고정
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		userGrid = (GridView) findViewById(R.id.userGrid);
		userGrid.startAnimation(new AnimationUtils().loadAnimation(
				BizCardGrid.this, R.anim.push_left_in));

		pageNavi = (LinearLayout) findViewById(R.id.pageNavi);
		pageIndex = (TextView) findViewById(R.id.pageIndex);
		//TextView pageTitle = (TextView) findViewById(R.id.pageTitle);
		//pageTitle.setText("내가 등록한 거래처");
		
		

		String cnt = getData(0);
		Info.BizCardListTotalPage = (int) (Double.parseDouble(cnt) + (cnt.indexOf('.') > 0 ? 1
				: 0));
		if (Info.BizCardListTotalPage < Info.BizCardListPage) {
			Info.BizCardListPage = 1;
		}
		pageIndex.setText(Info.BizCardListPage + " / " + Info.BizCardListTotalPage);
		if (Info.BizCardListTotalPage > 1) {
			pageNavi.setVisibility(View.VISIBLE);
		}

		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();

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

		if (Info.BizCardListPage <= 1) {
			prev.setBackgroundResource(R.drawable.left);
		} else {
			prev.setBackgroundResource(R.drawable.left);
		}
		if (Info.BizCardListPage >= Info.BizCardListTotalPage) {
			next.setBackgroundResource(R.drawable.right);
		} else {
			next.setBackgroundResource(R.drawable.right);
		}

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					if (!firstSettingEnd) {

						MyAdapter = new MyAdapter(BizCardGrid.this,
								R.layout.bizcardlistitem_2100_card, members);
						userGrid.setAdapter(MyAdapter);
						userGrid.setFocusable(false);
						firstSettingEnd = true;

					}

					if (members.isEmpty() && Info.BizCardListTotalPage == 0) {
						Toast.makeText(BizCardGrid.this, "자료가 없습니다.", 0).show();

					} else if (Info.replay && MyAdapter.isEmpty()
							&& Info.BizCardListPage <= Info.BizCardListTotalPage) {
 
						if(Info.BizCardListPage == 0)Info.BizCardListPage = 1;	
						MyAsyncTask at = new MyAsyncTask();
						try {
							at.execute();
						} catch (Exception e) {

						}

						Info.replay = false;
						return;

					} else {
						userGrid.setSelection(Info.BizCardListPosition);
					}

					Info.replay = true;
					BizCardGrid.curPage = Info.BizCardListPage;

				}

			}
		};

		userGrid.setOnScrollListener(new OnScrollListener() {

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

		if(Info.BizCardListPage == 0)Info.BizCardListPage = 1;	
		MyAsyncTask at = new MyAsyncTask();
		try {
			at.execute();
		} catch (Exception e) {

		}





        final ImageView searchBtn = (ImageView) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = null;
                    intent = new Intent(getApplicationContext(), BizCardListSearchText.class);
                    intent.putExtra("SEARCHTEXT", Info.isNull(intentRev.getStringExtra("SEARCHTEXT")));
                    intent.putExtra("SEARCHTYPE", "GRID");
                    startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } catch (OutOfMemoryError e) {
                    // System.gc();
                } catch (Exception e) {
                }

            }
        });



		ImageView change_img = (ImageView) findViewById(R.id.change_img);
		change_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), BizCardList.class);
					intent.putExtra("SEARCHTEXT", "");
					intent.putExtra("SEARCHTYPE", "LIST");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});




		ImageView myCardIV = (ImageView) findViewById(R.id.myCardIV);
		myCardIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), MyCardView.class);
					intent.putExtra("SEARCHTEXT", "");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});


		ImageView reCardIV = (ImageView) findViewById(R.id.reCardIV);
		reCardIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), BizCardGrid.class);
					intent.putExtra("SEARCHTEXT", "");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});


		ImageView mainIV = (ImageView) findViewById(R.id.mainIV);
		mainIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), BizCardFindList.class);
					intent.putExtra("SEARCHTEXT", "");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});



		ImageView chatIV = (ImageView) findViewById(R.id.chatIV);
		chatIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), ChatRoomList.class);
					intent.putExtra("SEARCHTEXT", "");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});



		ImageView systemIV = (ImageView) findViewById(R.id.systemIV);
		systemIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(getApplicationContext(), Setting.class);
					intent.putExtra("SEARCHTEXT", "");
					startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();

				} catch (OutOfMemoryError e) {
					//System.gc();
				} catch (Exception e) {
				}
			}
		});





	}

	// 리스트 어댑터
	public class MyAdapter extends ArrayAdapter<UserData> {
		Context maincon; 
		ArrayList<UserData> arSrc;
		int layout;

		public MyAdapter(Context context, int alayout,
				ArrayList<UserData> aarSrc) {
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

				Bitmap cbm = null;
				Bitmap ubm = null;

				final TextView userName = (TextView) convertView.findViewById(R.id.userName);
				final TextView companyName = (TextView) convertView.findViewById(R.id.companyName);
				final TextView officeAddr = (TextView) convertView.findViewById(R.id.officeAddr);
				final TextView mobile = (TextView) convertView.findViewById(R.id.mobile);
				final TextView userUseDt = (TextView) convertView.findViewById(R.id.userUseDt);

				if ("".equals(arSrc.get(position).MOBILE)) {
					mobile.setTextColor(Color.parseColor("#ff654565"));
					mobile.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							try {
								Info.performDial(BizCardGrid.this,
										arSrc.get(position).MOBILE);

							} catch (OutOfMemoryError e) {
								// System.gc();
							} catch (Exception e) {
							}
						}
					});
				}

				userName.setText(arSrc.get(position).USER_NAME);
				companyName.setText(arSrc.get(position).COMPANY_NAME);
				officeAddr.setText(arSrc.get(position).OFFICE_ADDR);
				mobile.setText(arSrc.get(position).MOBILE);
				userUseDt.setText(arSrc.get(position).USER_USE_DT);


				final LinearLayout RowItem = (LinearLayout) convertView.findViewById(R.id.RowItem);
				RowItem.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							Info.BizCardListPosition = position;



							Intent intent = new Intent(getApplicationContext(), BizCardView.class);
							intent.putExtra("userEmail", arSrc.get(position).USER_EMAIL);
							intent.putExtra("userName", arSrc.get(position).USER_NAME);
							intent.putExtra("companyName", arSrc.get(position).COMPANY_NAME);
							intent.putExtra("dept", arSrc.get(position).DEPT);
							intent.putExtra("grade", arSrc.get(position).GRADE);
							intent.putExtra("upmoo", arSrc.get(position).UPMOO);
							intent.putExtra("officeAddr", arSrc.get(position).OFFICE_ADDR);
							intent.putExtra("officeTel", arSrc.get(position).OFFICE_TEL);
							intent.putExtra("officeFax", arSrc.get(position).OFFICE_FAX);
							intent.putExtra("mobile", arSrc.get(position).MOBILE);
							intent.putExtra("workEmail", arSrc.get(position).WORK_EMAIL);
							intent.putExtra("keyword", arSrc.get(position).KEYWORD);

							startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));



							new CountDownTimer(1000, 1) {
								@Override
								public void onTick(long millisUntilFinished) {
									onPause();
								}

								@Override
								public void onFinish() {
									RowItem.setBackgroundResource(R.drawable.selectborder);
									// userEmail.setTextColor(0xff000000);
									// userName.setTextColor(0xff787878);
								}
							}.start();
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
		public void add(UserData object) {
			// TODO Auto-generated method stub
			super.add(object);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arSrc.size();
		}

		@Override
		public UserData getItem(int arg0) {
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
			isLoading = false;

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

			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			} else {
				progressDialog = new ProgressDialog(BizCardGrid.this);
			}

			progressDialog.setMessage("로딩중입니다.\n잠시만 기다려주세요. ");
			progressDialog.setCancelable(false);
			progressDialog.show();

			if (!firstSettingEnd) {
				members.clear();
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
				loadXML(BizCardGrid.this);

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

	static String getData(int page) { 
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page", "" + page)); 
		params.add(new BasicNameValuePair("search", Info.isNull(intentRev.getStringExtra("SEARCHTEXT"))));  
		params.add(new BasicNameValuePair("userEmail", Info.email));

		String result = Info.Submit(
				"http://swcbizcard.nezip.co.kr/BizCardListAction.asp", params);

		return result.trim();
	}

	static void loadXML(Context context) throws Exception {
		if (Info.BizCardListTotalPage < Info.BizCardListPage) {
			Info.BizCardListPage = 1;
		}

		String sql = "";

		userdbhelper = new UserDBHelper(context);
		SQLiteDatabase db = userdbhelper.getWritableDatabase();

		sql = "DELETE FROM CORPMEMBER ";
		sql = sql
				+ " WHERE PAGE = '"
				+ ("0000000000" + Info.BizCardListPage)
						.substring(("0000000000" + Info.BizCardListPage).length() - 10)
				+ "'";

		db.beginTransaction();
		db.execSQL(sql);
		db.setTransactionSuccessful();
		db.endTransaction();

		boolean sqlRun = true;

		db.beginTransaction();

		String result = getData(Info.BizCardListPage);
		if (!"".equals(result.trim())) {

			String[] messageRow = result.split("</R>");

			for (int i = 0; i < messageRow.length; i++) {
				if (!"".equals(messageRow[i].trim())) {
					String[] messageCol = messageRow[i].split("</C>");
					if (messageCol.length > 17) {

						sql = "INSERT INTO CORPMEMBER ( USER_EMAIL, USER_NAME, COMPANY_NAME, DEPT, GRADE, OFFICE_ADDR, UPMOO, OFFICE_TEL, OFFICE_FAX, MOBILE, USER_USE_DT, WORK_EMAIL, TOP_PHONE, HOME_PAGE, KEYWORD, COMPANY_LOGO, MY_PHOTO, REG_ID, ID, PAGE, POSITION) VALUES ( '"
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
								+ messageCol[7].replaceAll("'", "''")
								+ "', '"
								+ messageCol[8].replaceAll("'", "''")
								+ "', '"
								+ messageCol[9].replaceAll("'", "''")
								+ "', '"
								+ messageCol[10].replaceAll("'", "''")
								+ "', '"
								+ messageCol[11].replaceAll("'", "''")
								+ "', '"
								+ messageCol[12].replaceAll("'", "''")
								+ "', '"
								+ messageCol[13].replaceAll("'", "''")
								+ "', '"
								+ messageCol[14].replaceAll("'", "''")
								+ "', '"
								+ messageCol[15].replaceAll("'", "''")
								+ "', '"
								+ messageCol[16].replaceAll("'", "''")
								+ "', '"
								+ messageCol[17].replaceAll("'", "''")
								+ "', '"
								+ messageCol[18].replaceAll("'", "''")
								+ "', '"
								+ ("0000000000" + Info.BizCardListPage)
										.substring(("0000000000" + Info.BizCardListPage)
												.length() - 10)
								+ "', '"
								+ ("0000000000" + i)
										.substring(("0000000000" + i).length() - 10)
								+ "');";
						try {
							db.execSQL(sql);
							// Log.d("SQL", sql);
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
		userdbhelper.close();

		if (!sqlRun) {
			context.deleteDatabase("corpMember.db");
		}

	}

	void dbload() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		lastPosition = 0;

		try {
			String sql = "";
			UserDBHelper corpdbhelper = new UserDBHelper(this);
			db = corpdbhelper.getWritableDatabase();
			// 디비 나만 쓸거야 시작
			cursor = null;
			sql = "SELECT USER_EMAIL, USER_NAME, COMPANY_NAME, DEPT, GRADE, OFFICE_ADDR, UPMOO, OFFICE_TEL, OFFICE_FAX, MOBILE, USER_USE_DT, WORK_EMAIL, TOP_PHONE, HOME_PAGE, KEYWORD, COMPANY_LOGO, MY_PHOTO, ID, REG_ID ";
			sql = sql + "  FROM CORPMEMBER ";
			sql = sql
					+ " WHERE PAGE = '"
					+ ("0000000000" + Info.BizCardListPage)
							.substring(("0000000000" + Info.BizCardListPage).length() - 10)
					+ "'";
			sql = sql + " ORDER BY PAGE, POSITION ";

			cursor = db.rawQuery(sql, null);
			// Log.i("SQL", sql);
			while (cursor.moveToNext()) {
				// Log.i("SQL", sql);
				if (!firstSettingEnd) {
					members.add(new UserData(cursor.getString(0), cursor
							.getString(1), cursor.getString(2), cursor
							.getString(3), cursor.getString(4), cursor
							.getString(5), cursor.getString(6), cursor
							.getString(7), cursor.getString(8), cursor
							.getString(9), cursor.getString(10), cursor
							.getString(11), cursor.getString(12), cursor
							.getString(13), cursor.getString(14), cursor
							.getString(15), cursor.getString(16), cursor
							.getString(17), cursor.getString(18)));
				} else {
					MyAdapter.add(new UserData(cursor.getString(0), cursor
							.getString(1), cursor.getString(2), cursor
							.getString(3), cursor.getString(4), cursor
							.getString(5), cursor.getString(6), cursor
							.getString(7), cursor.getString(8), cursor
							.getString(9), cursor.getString(10), cursor
							.getString(11), cursor.getString(12), cursor
							.getString(13), cursor.getString(14), cursor
							.getString(15), cursor.getString(16), cursor
							.getString(17), cursor.getString(18)));
				}

			}

			if (firstSettingEnd) MyAdapter.notifyDataSetChanged();

			cursor.close();
			cursor = null;

			db.close();
			db = null;

			corpdbhelper.close();
			corpdbhelper = null;

		} catch (Exception e) {
			e.printStackTrace();
			deleteDatabase("corpMember.db");

		} finally {
			if (db != null) {
				db.close();
				db = null;
			}

			if (cursor != null) {
				cursor.close();
				cursor = null;
			}

			if (userdbhelper != null) {
				userdbhelper.close();
				userdbhelper = null;
			}

			mHandler.sendEmptyMessage(1);

		}
	}

	@Override
	public void onResume() {

		if (BizCardGrid.curPage != Info.BizCardListPage) {
			if (Info.BizCardListPage < Info.BizCardListTotalPage) {
 
				if(Info.BizCardListPage == 0)Info.BizCardListPage = 1;	
				MyAsyncTask at = new MyAsyncTask();
				try {
					at.execute();
				} catch (Exception e) {

				}

				pageIndex.setText(Info.BizCardListPage + " / " + Info.BizCardListTotalPage);
				if (Info.BizCardListPage == 1) {
					prev.setBackgroundResource(R.drawable.left);
				} else {
					prev.setBackgroundResource(R.drawable.left);
				}
				if (Info.BizCardListPage == Info.BizCardListTotalPage) {
					next.setBackgroundResource(R.drawable.right);
				} else {
					next.setBackgroundResource(R.drawable.right);
				}
			} else {
				next.setBackgroundResource(R.drawable.right);
			}
		}
		userGrid.setSelection(Info.BizCardListPosition);
		super.onResume();

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
		try {
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
		} catch (OutOfMemoryError e) {
			// System.gc();
		} catch (Exception e) {
		}
		return super.dispatchTouchEvent(ev);
	}

	@SuppressLint("NewApi")
	void MoveNext(boolean flag) {
		if (flag) {
			right = false;

			if (Info.BizCardListPage < Info.BizCardListTotalPage) {
				nextBtnClick();
			}

		} else {
			right = true;

			if (Info.BizCardListPage > 1) {
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
		if (Info.BizCardListPage > 1) {
			Info.BizCardListPage--;
			Info.BizCardListPosition = 0;
 
			MyAsyncTask at = new MyAsyncTask();
			try {
				at.execute();
			} catch (Exception e) {

			}

			pageIndex.setText(Info.BizCardListPage + " / " + Info.BizCardListTotalPage);
			if (Info.BizCardListPage == 1) {
				prev.setBackgroundResource(R.drawable.left);
			} else {
				prev.setBackgroundResource(R.drawable.left);
			}
			if (Info.BizCardListPage == Info.BizCardListTotalPage) {
				next.setBackgroundResource(R.drawable.right);
			} else {
				next.setBackgroundResource(R.drawable.right);
			}
		} else {
			prev.setBackgroundResource(R.drawable.left);
		}
	}

	public void nextBtnClick() {
		if (isLoading)
			return;
		if (Info.BizCardListPage < Info.BizCardListTotalPage) {
			Info.BizCardListPage++;
			Info.BizCardListPosition = 0;
 
			MyAsyncTask at = new MyAsyncTask();
			try {
				at.execute();
			} catch (Exception e) {

			}

			pageIndex.setText(Info.BizCardListPage + " / " + Info.BizCardListTotalPage);
			if (Info.BizCardListPage == 1) {
				prev.setBackgroundResource(R.drawable.left);
			} else {
				prev.setBackgroundResource(R.drawable.left);
			}
			if (Info.BizCardListPage == Info.BizCardListTotalPage) {
				next.setBackgroundResource(R.drawable.right);
			} else {
				next.setBackgroundResource(R.drawable.right);
			}
		} else {
			next.setBackgroundResource(R.drawable.right);
		}
	}

}
