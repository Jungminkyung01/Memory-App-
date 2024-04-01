package com.nezip.bizcard1;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatRoomList extends Activity {

	ChatDBHelper chatdbhelper;

	LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(0, 0);

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MyCardView.class);
		finish();
		startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	ProgressDialog progressDialog;
	MyAdapter MyAdapter;

	ArrayList<ChatData> messages = new ArrayList<ChatData>();

	private ArrayList<listViewCanvas> lView = new ArrayList<listViewCanvas>();

	private class listViewCanvas {
		private String photo;
	}

	// 좌우 스크롤을 위한 변수
	float firstX;
	float firstY;
	float lastX;
	float lastY;
	boolean right = false;

	final int itemListCnt = 10;
	int list_look_num = 1;
	int listNumber = 0;
	int page = 1; // 페이지
	int lastItemIdx = 0; // 마지막으로 본 item index
	int lastPosition = 0; // ListView 마지막 position

	boolean loadingflag = true;
	boolean isLoading = false;

	Handler mHandler;

	ListView chatRoomList;
	ImageButton prev;
	ImageButton next;
	LinearLayout rect;
	LinearLayout pageNavi;
	TextView pageIndex;
	Intent intentRev;

	int position = 5;

	changeString changeString = new changeString();
	boolean firstSettingEnd = false;

	// 하단 탭버튼
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ("".equals(Info.email)) {
			if (!Info.login(getApplicationContext())) {
				Intent intent = new Intent(this, Login1th.class);
				finish();
				startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
				return;
			}
		}

		Info.replay = true;
		setContentView(R.layout.chatroomlist_2140);

		intentRev = getIntent();

		// 횡 스크롤뷰
		rect = (LinearLayout) findViewById(R.id.rightToleft);

		// 화면 고정
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		chatRoomList = (ListView) findViewById(R.id.chatRoomList);
		chatRoomList.startAnimation(new AnimationUtils().loadAnimation( ChatRoomList.this, R.anim.push_left_in));

		pageNavi = (LinearLayout) findViewById(R.id.pageNavi);
		pageIndex = (TextView) findViewById(R.id.pageIndex);

		progressDialog = new ProgressDialog(ChatRoomList.this);
		progressDialog.setMessage("로딩중입니다.\n잠시만 기다려주세요. ");
		progressDialog.setCancelable(false);
		progressDialog.show();

		new CountDownTimer(1000, 1) {
			@Override
			public void onTick(long millisUntilFinished) {
				onPause();
			}

			@Override
			public void onFinish() {
				if (progressDialog != null && progressDialog.isShowing())
					progressDialog.dismiss();
			}
		}.start();

		String cnt = getData(0);
		Log.d("오류", "'" + cnt +"'");

		try{
			Info.ChatRoomTotalPage = (int) (Double.parseDouble(cnt) + (cnt.indexOf('.') > 0 ? 1 : 0));
		} catch(Exception e){ 
			Info.ChatRoomTotalPage = 0;
		}
		if (Info.ChatRoomTotalPage < Info.ChatRoomPage) {
			Info.ChatRoomPage = 1;
		}
		pageIndex.setText(Info.ChatRoomPage + " / " + Info.ChatRoomTotalPage);
		if (Info.ChatRoomTotalPage > 1) {
			pageNavi.setVisibility(View.VISIBLE);
		} else {
			// pageNavi.setVisibility(View.GONE);
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
					if (gpage <= Info.ChatRoomTotalPage && gpage > 0) {
						Info.ChatRoomPage = gpage;
 
						if(Info.ChatRoomPage == 0)Info.ChatRoomPage = 1;	
						MyAsyncTask at = new MyAsyncTask();
						try {
							at.execute();
						} catch (Exception e) {

						}

						pageIndex.setText(Info.ChatRoomPage + " / "
								+ Info.ChatRoomTotalPage);
						if (Info.ChatRoomPage == 1) {
							prev.setBackgroundResource(R.drawable.han_photo_icon_prev_b);
						} else {
							prev.setBackgroundResource(R.drawable.han_photo_icon_prev_a);
						}
						if (Info.ChatRoomPage == Info.ChatRoomTotalPage) {
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

		if (Info.ChatRoomPage <= 1) {
			prev.setBackgroundResource(R.drawable.han_photo_icon_prev_b);
		} else {
			prev.setBackgroundResource(R.drawable.han_photo_icon_prev_a);
		}
		if (Info.ChatRoomPage >= Info.ChatRoomTotalPage) {
			next.setBackgroundResource(R.drawable.han_photo_icon_next_b);
		} else {
			next.setBackgroundResource(R.drawable.han_photo_icon_next_a);
		}

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					if (!firstSettingEnd) {

						MyAdapter = new MyAdapter(ChatRoomList.this,
								R.layout.chatroomlistitem_2140, messages);
						chatRoomList.setAdapter(MyAdapter);
						chatRoomList.setFocusable(false);
						firstSettingEnd = true;

					}

					if (messages.isEmpty() && Info.ChatRoomTotalPage == 0) {
						Toast.makeText(ChatRoomList.this, "자료가 없습니다.", 0)
								.show();

					} else if (Info.replay && MyAdapter.isEmpty()
							&& Info.ChatRoomPage <= Info.ChatRoomTotalPage) {
 
						if(Info.ChatRoomPage == 0)Info.ChatRoomPage = 1;	
						MyAsyncTask at = new MyAsyncTask();
						try {
							at.execute();
						} catch (Exception e) {

						}

						if (progressDialog != null
								&& progressDialog.isShowing())
							progressDialog.dismiss();
						Info.replay = false;
						return;

					} else {
						chatRoomList.setSelection(Info.ChatRoomPosition);
					}

					Info.replay = true;

				}

			}
		};

		chatRoomList.setOnScrollListener(new OnScrollListener() {

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

			}
		});


		isLoading = true;



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
					Intent intent = new Intent(getApplicationContext(), BizCardList.class);
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
	public class MyAdapter extends ArrayAdapter<ChatData> {
		Context maincon; 
		ArrayList<ChatData> arSrc;
		int layout;

		public MyAdapter(Context context, int alayout,
				ArrayList<ChatData> aarSrc) {
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

				final TextView userEmail = (TextView) convertView
						.findViewById(R.id.userEmail);
				final TextView userName = (TextView) convertView
						.findViewById(R.id.userName);
				final TextView message = (TextView) convertView
						.findViewById(R.id.message);
				final TextView createDate = (TextView) convertView.findViewById(R.id.createDate);

				final ImageView photo = (ImageView) convertView
						.findViewById(R.id.userPhoto);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("userEmail", changeString
						.dbTostr(arSrc.get(position).USER_EMAIL1)));
				final String result = Info
						.Submit("http://swcbizcard.nezip.co.kr/UserPhotoAction.asp",
								params);
				Bitmap userPhotoImage = Info.GetImageFromURL(result,
						ChatRoomList.this);
				if (userPhotoImage != null) {
					userPhotoImage = Bitmap.createScaledBitmap(
							userPhotoImage, 60, 60, false);
					photo.setImageBitmap(Info
							.getRoundedCornerBitmap(userPhotoImage, 10));
				}
				lView.get(position).photo = result;

				userEmail
						.setText(changeString.dbTostr(arSrc.get(position).USER_EMAIL1));
				userName.setText(changeString.dbTostr(arSrc.get(position).USER_NAME1));
				message.setText(changeString.dbTostr(arSrc.get(position).MESSAGE));
				createDate
						.setText(changeString.dbTostr(arSrc.get(position).CREATE_DATE));

				final LinearLayout chatRoomListItem = (LinearLayout) convertView
						.findViewById(R.id.chatRoomListItem);
				if ("NEW"
						.equals(changeString.dbTostr(arSrc.get(position).USER_NAME2))) {
					chatRoomListItem
							.setBackgroundResource(R.drawable.chatroomnoneborder);
				}
				chatRoomListItem.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							chatRoomListItem
									.setBackgroundResource(R.drawable.chatroomselectborder);

							Intent find_go = new Intent(ChatRoomList.this,
									ChatingForm.class);
							find_go.putExtra("EMAIL",
									changeString.dbTostr(arSrc
											.get(position).USER_EMAIL1));
							find_go.putExtra("ENAME",
									changeString.dbTostr(arSrc
											.get(position).USER_NAME1));
							find_go.putExtra("PHOTO",
									lView.get(position).photo);
							find_go.putExtra("RETURN", "NO");

							startActivity(find_go);
							isLoading = true;

							new CountDownTimer(1000, 1) {
								@Override
								public void onTick(long millisUntilFinished) {
									onPause();
								}

								@Override
								public void onFinish() {
									chatRoomListItem
											.setBackgroundResource(R.drawable.chatroomborder);
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
		public void add(ChatData object) {
			// TODO Auto-generated method stub
			super.add(object);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arSrc.size();
		}

		@Override
		public ChatData getItem(int arg0) {
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

			dbload();

			// 로딩 완료
			isLoading = false;

			// 작업이 완료 된 후 할일
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// 작업을 시작하기 전 할일
			isLoading = true;
			progressDialog = new ProgressDialog(ChatRoomList.this);
			progressDialog.setMessage("로딩중입니다.\n잠시만 기다려주세요. ");
			progressDialog.setCancelable(false);
			progressDialog.show();


			if (!firstSettingEnd) {
				messages.clear();
			} else {
				if(MyAdapter != null) MyAdapter.clear();
			}
			
			if(lView != null) {
				for(int i=0; i < lView.size(); i++) lView.get(i).photo = null;
				lView.clear();
			}

			lastPosition = 0;
 
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				// Log.i("doInBackground / loadXML : ",""+page);

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

	void dbload() {
		lastPosition = 0;

		try {

			String result = getData(Info.ChatRoomPage);
			if (!"".equals(result)) {

				String[] messageRow = result.split("</R>");

				for (int i = 0; i < messageRow.length; i++) {

					if (!"".equals(messageRow[i].trim())) {
						String[] messageCol = messageRow[i].split("</C>");
						if (messageCol.length > 5) {
							boolean find = false;
							for (int p = 0; p < messages.size(); p++) {
								if (messages.get(p).USER_EMAIL1
										.equals(messageCol[1])) {
									find = true;
								}
							}
							if (!find) {
								if (!firstSettingEnd) {
									messages.add(new ChatData(
											messages.size() - 1, Integer
													.toString(i),
											messageCol[1], "CHAT",
											messageCol[2], messageCol[5],
											messageCol[3], messageCol[4]));
								} else {
									MyAdapter.add(new ChatData(
											messages.size() - 1, Integer
													.toString(i),
											messageCol[1], "CHAT",
											messageCol[2], messageCol[5],
											messageCol[3], messageCol[4]));
								}

								listViewCanvas lv = new listViewCanvas();
								lv.photo = null;
								lView.add(lv);
							}
						}
					}
				}
			}

			if (firstSettingEnd) MyAdapter.notifyDataSetChanged();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			mHandler.sendEmptyMessage(1);

		}
	}

	@Override
	public void onResume() {

		NotificationManager mNotiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotiManager.cancelAll();

		if (isLoading) {
 
			if(Info.ChatRoomPage == 0)Info.ChatRoomPage = 1;	
			MyAsyncTask at = new MyAsyncTask();
			try {
				at.execute();
			} catch (Exception e) {

			}
		}

		super.onResume();

	}

	@Override
	public void onDestroy() {

		if (progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		super.onDestroy();

	}

	String getData(int page) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page", "" + page));
		params.add(new BasicNameValuePair("pageSize", "20"));
		params.add(new BasicNameValuePair("userEmail1", Info.email));

		String result = "";
		String result2 = "";
		String result3 = "0";
		Info.ChatRoomNewRows = 0;

		result = Info.Submit(
				"http://swcbizcard.nezip.co.kr/ChatMessageNoneAction.asp",
				params);
		result = result.trim();
		Log.d("로우", "SQL : '" + result + "'");
		if (!"".equals(result)) {
			
			result2 = result;
			result3 = "0";

			if (page == 0 && result.split("</C>").length > 1) {
				result2 = result.split("</C>")[0];
				result3 = result.split("</C>")[1];
				Info.ChatRoomNewRows = Integer.parseInt(result3);
			}
			
			
		}
		int tot = 0;

		SQLiteDatabase db = null;
		Cursor cursor = null;

		try {
			String sql = "";
			ChatDBHelper chatdbhelper = new ChatDBHelper(this);
			db = chatdbhelper.getWritableDatabase();
			// 디비 나만 쓸거야 시작
			cursor = null;

			sql = " SELECT DISTINCT A.ID, A.USER_EMAIL1, A.USER_NAME1, A.MESSAGE, A.CREATE_DATE FROM ";

			sql = sql
					+ " (SELECT USER_EMAIL1, USER_NAME1, MESSAGE, CREATE_DATE, ID FROM CHAT WHERE USER_EMAIL2 = '"
					+ Info.email + "'  ";
			sql = sql + "  UNION ALL ";
			sql = sql
					+ "  SELECT USER_EMAIL2, USER_NAME2, MESSAGE, CREATE_DATE, ID FROM CHAT WHERE USER_EMAIL1 = '"
					+ Info.email + "'  ) A, ";

			sql = sql + "  (SELECT USER_EMAIL1, MAX(ID) AS ID  ";
			sql = sql
					+ "     FROM (SELECT USER_EMAIL1, ID AS ID FROM CHAT WHERE USER_EMAIL2 = '"
					+ Info.email + "'  ";
			sql = sql + "            UNION ALL ";
			sql = sql
					+ "           SELECT USER_EMAIL2, ID AS ID FROM CHAT WHERE USER_EMAIL1 = '"
					+ Info.email + "'  ) ";
			sql = sql + "    GROUP BY USER_EMAIL1 ) B ";

			sql = sql + " WHERE A.USER_EMAIL1 = B.USER_EMAIL1 ";
			sql = sql + "   AND A.ID = B.ID ";

			sql = sql + " ORDER BY A.ID DESC";

			cursor = db.rawQuery(sql, null);

			
			tot = Integer.parseInt(result3) + cursor.getCount();

			if (page == 0) {
				if (cursor.getCount() > 0) {
					result2 = Integer.toString((tot / 20) + ((tot % 20) > 0 ? 1 : 0));
				}
			} else {
				if ((Info.ChatRoomNewRows / 20) == Info.ChatRoomPage) {
					for (int i = (Info.ChatRoomNewRows % 20); i < 20
							&& cursor.moveToNext(); i++) {
						String msg = cursor.getString(3);
						if (msg.length() > 6
								&& "PHOTO:".equals(msg.substring(0, 6))) {
							msg = "[PHOTO]";
						}
						result2 = result2 + cursor.getString(0) + "</C>"
								+ cursor.getString(1) + "</C>"
								+ cursor.getString(2) + "</C>" + msg + "</C>"
								+ cursor.getString(4) + "</C>OLD" + "</R>";
					}
				} else if ((Info.ChatRoomNewRows / 20) < Info.ChatRoomPage) {
					int rows = (Info.ChatRoomPage - (Info.ChatRoomNewRows / 20)) - 1;

					for (int i = (Info.ChatRoomNewRows % 20); i < (20 * rows)
							&& cursor.moveToNext(); i++) {
						;
					}

					int i = 1;
					while (cursor.moveToNext() && i <= 20) {
						String msg = cursor.getString(3);
						if (msg.length() > 6
								&& "PHOTO:".equals(msg.substring(0, 6))) {
							msg = "[PHOTO]";
						}
						result2 = result2 + cursor.getString(0) + "</C>"
								+ cursor.getString(1) + "</C>"
								+ cursor.getString(2) + "</C>" + msg + "</C>"
								+ cursor.getString(4) + "</C>OLD" + "</R>";
						i++;
					}
				}
			}

			cursor.close();
			cursor = null;

			db.close();
			db = null;

			chatdbhelper.close();
			chatdbhelper = null;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (db != null) {
				db.close();
				db = null;
			}

			if (cursor != null) {
				cursor.close();
				cursor = null;
			}

			if (chatdbhelper != null) {
				chatdbhelper.close();
				chatdbhelper = null;
			}
			
			return result2;
		}

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

			if (Info.ChatRoomPage < Info.ChatRoomTotalPage) {
				nextBtnClick();
			}

		} else {
			right = true;

			if (Info.ChatRoomPage > 1) {
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
		if (Info.ChatRoomPage > 1) {
			Info.ChatRoomPage--;
			Info.ChatRoomPosition = 0;
 
			MyAsyncTask at = new MyAsyncTask();
			try {
				at.execute();
			} catch (Exception e) {

			}

			pageIndex.setText(Info.ChatRoomPage + " / "
					+ Info.ChatRoomTotalPage);
			if (Info.ChatRoomPage == 1) {
				prev.setBackgroundResource(R.drawable.han_photo_icon_prev_b);
			} else {
				prev.setBackgroundResource(R.drawable.han_photo_icon_prev_a);
			}
			if (Info.ChatRoomPage == Info.ChatRoomTotalPage) {
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
		if (Info.ChatRoomPage < Info.ChatRoomTotalPage) {
			Info.ChatRoomPage++;
			Info.ChatRoomPosition = 0;
 
			MyAsyncTask at = new MyAsyncTask();
			try {
				at.execute();
			} catch (Exception e) {

			}

			pageIndex.setText(Info.ChatRoomPage + " / "
					+ Info.ChatRoomTotalPage);
			if (Info.ChatRoomPage == 1) {
				prev.setBackgroundResource(R.drawable.han_photo_icon_prev_b);
			} else {
				prev.setBackgroundResource(R.drawable.han_photo_icon_prev_a);
			}
			if (Info.ChatRoomPage == Info.ChatRoomTotalPage) {
				next.setBackgroundResource(R.drawable.han_photo_icon_next_b);
			} else {
				next.setBackgroundResource(R.drawable.han_photo_icon_next_a);
			}
		} else {
			next.setBackgroundResource(R.drawable.han_photo_icon_next_b);
		}
	}

}
