package com.nezip.bizcard1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.nezip.bizcard1.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 

@SuppressLint("NewApi")
public class ChatingForm extends Activity {

	private ChatDBHelper chatdbhelper;
	static String email = "";
	private static String name = "";
	private static String regid = "";
	private static String photo = "";

	private Bitmap userPhotoImage = null;

	private static String filePath = "";
	private static String SAMPLEIMG = "photo.jpg";
	private BitmapFactory.Options options = new BitmapFactory.Options();

	private LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
			0, 0);

	private long lastID = (long) 0;
	// ProgressDialog progressDialog;
	private MyAdapter MyAdapter;

	private ArrayList<ChatData> messages = new ArrayList<ChatData>();

	int lastPosition = 0; // ListView 마지막 position

	private boolean appStart = false;
	private boolean loadingflag = true;
	private static boolean isLoading = false;

	private ListView chatList;
	private EditText message;

	private changeString changeString = new changeString();
	private boolean firstSettingEnd = false;

	private Handler timeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			if (Info.reciveMessage && !isLoading && loadingflag) {

				Info.reciveMessage=false;
				Info.runChating = true;
				MyAsyncTask at = new MyAsyncTask();
				try {
					at.execute();
				} catch (Exception e) {

				}

			}

			timeHandler.sendEmptyMessageDelayed(0, 1000);
			super.handleMessage(msg);
		}
	};

	private Handler replaceHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			MyAdapter.notifyDataSetChanged();
			super.handleMessage(msg);
		}
	};

	@Override
	public void onBackPressed() {

		if (!"YES".equals(getIntent().getStringExtra("RETURN"))) {
			Intent intent = new Intent(getApplicationContext(),
					ChatRoomList.class);
			finish();
			startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		} else {
			finish();
		}

	}

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

		setContentView(R.layout.chatlist_2141);

		Intent intent_rec = getIntent();
		ChatingForm.email = "" + intent_rec.getStringExtra("EMAIL");
		ChatingForm.name = "" + intent_rec.getStringExtra("ENAME");
		ChatingForm.photo = "" + intent_rec.getStringExtra("PHOTO");
		ChatingForm.regid = "";

		TextView chatTitle = (TextView) findViewById(R.id.chatTitle);
		chatTitle.setText("'" + ChatingForm.name + "' 님");

		if ("".equals(this.photo) || "NO".equals(this.photo)
				|| this.photo == null) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userEmail", ChatingForm.email));
			String result = Info.Submit(
					"http://swcbizcard.nezip.co.kr/UserPhotoAction.asp",
					params);
			Log.d("result", "result : " + result);

			if (!"".equals(result.trim())) {
				this.photo = result.trim();
				// Log.d("userInfo[13]", "13 : "+userInfo[13]);
			} else {
				this.photo = "NO";
			}
		}

		if (!"".equals(this.photo) && !"NO".equals(this.photo)) {
			userPhotoImage = Info.GetImageFromURL(ChatingForm.photo, ChatingForm.this);
			if (userPhotoImage != null) {
				userPhotoImage = Bitmap.createScaledBitmap(userPhotoImage, 60, 60, false);
			}

		} else {
			if (userPhotoImage != null && !userPhotoImage.isRecycled()) {
				userPhotoImage.recycle();
			}
			userPhotoImage = null;
		}

		// 화면 고정
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		appStart = true;
		loadingflag = true;

		Toast.makeText(ChatingForm.this,
				"'" + ChatingForm.name + "' 님의 대화방 입니다.", 0).show();

		// 하단 탭버튼
		ImageView chatCloseBtn = (ImageView) findViewById(R.id.chatCloseBtn);
		chatCloseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}
			}
		});

		ImageView chatClearBtn = (ImageView) findViewById(R.id.chatClearBtn);
		chatClearBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					
					MyAdapter.clear();
					MyAdapter.notifyDataSetChanged();
					
					chatdbhelper = new ChatDBHelper(ChatingForm.this);
					SQLiteDatabase db = chatdbhelper.getWritableDatabase();

					String filenm = "";
					String pathnm = "";
					String path = "";

					String sql = "";
					sql = "SELECT MESSAGE ";
					sql = sql + "  FROM CHAT  ";
					sql = sql + " WHERE MESSAGE LIKE 'PHOTO:%' ";
					sql = sql + "   AND ((USER_EMAIL1 = '" + Info.email
							+ "' AND USER_EMAIL2= '" + ChatingForm.email
							+ "') ";
					sql = sql + "       OR (USER_EMAIL2 = '" + Info.email
							+ "' AND USER_EMAIL1= '" + ChatingForm.email
							+ "')) ";
					sql = sql + " ORDER BY ID ASC  ";

					Cursor cursor = null;
					cursor = db.rawQuery(sql, null);

					while (cursor.moveToNext()) {

						if (cursor.getString(0).length() > 6) {
							path = cursor.getString(0).substring(6);

							if (path.lastIndexOf("/photo/") > 0) {

								filenm = path.substring(
										path.lastIndexOf("/photo/") + 7,
										path.length());
								pathnm = "http://swcbizcard.nezip.co.kr/ChatPhotoDeleteUpdateAction.asp";

								List<NameValuePair> params2 = new ArrayList<NameValuePair>();
								params2.add(new BasicNameValuePair(
										"userEmail1", Info.email));
								params2.add(new BasicNameValuePair(
										"userEmail2", ChatingForm.email));
								params2.add(new BasicNameValuePair("filepath", filenm));
								params2.add(new BasicNameValuePair("imagepath", path));
								Info.Submit(pathnm, params2);

							}
						}

					}
					if (cursor != null) {
						cursor.close();
						cursor = null;
					}

					List<NameValuePair> params = new ArrayList<NameValuePair>();
					String result = null;
					params.add(new BasicNameValuePair("userEmail1", Info.email));
					params.add(new BasicNameValuePair("userEmail2",
							ChatingForm.email));

					result = Info
							.Submit("http://swcbizcard.nezip.co.kr/ChatPhotoDeleteListAction.asp",
									params);

					if (!"".equals(result)) {
						String[] messageRow = result.split("</R>");
						for (int i = 0; i < messageRow.length; i++) {
							if (!"".equals(messageRow[i].trim())) {
								String[] messageCol = messageRow[i]
										.split("</C>");
								if (messageCol.length > 1) {
									path = messageCol[0].trim();

									if (path.lastIndexOf("/photo/") > 0) {
										filenm = path.substring(
												path.lastIndexOf("/photo/") + 7,
												path.length());
										pathnm = path.substring(0,
												path.lastIndexOf("/photo/"))
												+ "/admin/PhotoDelete.asp";

										List<NameValuePair> params3 = new ArrayList<NameValuePair>();
										params3.add(new BasicNameValuePair(
												"filepath", filenm));
										Info.Submit(pathnm, params3);
									}
								}
							}
						}
					}

					db.beginTransaction();

					sql = "DELETE FROM CHAT " + " WHERE ((USER_EMAIL1 = '"
							+ Info.email + "' AND USER_EMAIL2= '"
							+ ChatingForm.email + "') "
							+ " OR (USER_EMAIL2 = '" + Info.email
							+ "' AND USER_EMAIL1= '" + ChatingForm.email
							+ "'));  ";

					db.execSQL(sql);

					// 디비 나만 쓸거야 끝
					db.setTransactionSuccessful();
					db.endTransaction();
					db.close();
					chatdbhelper.close();
 
					Info.reciveMessage=true;
					timeHandler.sendEmptyMessage(0);

				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}

			}
		});

		Button submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					if ("".equals(ChatingForm.regid)) {
						params.clear();
						params.add(new BasicNameValuePair("userEmail",
								ChatingForm.email));
						ChatingForm.regid = Info
								.Submit("http://swcbizcard.nezip.co.kr/UserRegIDAction.asp",
										params);
					}
/*
					if ("".equals(ChatingForm.regid)) {
						Toast.makeText(ChatingForm.this,
								"'" + ChatingForm.name + "' 님은 오프라인 상태 입니다.", 0)
								.show();
						return;
					}
*/

					String result = "";

					SharedPreferences isFirstRunPreferences = PreferenceManager
							.getDefaultSharedPreferences(ChatingForm.this);
					if (isFirstRunPreferences
							.getBoolean("MESSAGE_ACCEPT", true)) {

						params.clear();
						params.add(new BasicNameValuePair("userEmail",
								Info.email));

						if ("corp".equals(ChatingForm.email)) {
							result = "10000";
						} else {
							result = Info
									.Submit("http://swcbizcard.nezip.co.kr/CorpUserPointAction.asp",
											params);
						}

						result = result.trim();
						if (Integer.parseInt(result) < 20) {
							Toast.makeText(ChatingForm.this,
									"포인트 부족 : 발송에 필요한 포인트가 부족합니다.", 0).show();

							new AlertDialog.Builder(ChatingForm.this)
									.setMessage(
											"메시지 발송에 필요한 포인트는 20P입니다.\n\n"
													+ "귀하의 가용 포인트는 "
													+ CustomTextWathcer
															.makeStringComma(result)
													+ "P 입니다.\n"
													+ "귀하의 포인트가 "
													+ (20 - Integer.parseInt(result))
													+ "P 부족합니다.\n"
													+ "채팅 이용권를 충전 하시겠습니까?")
									.setNegativeButton(
											"취소",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													return;
												}
											})
									.setPositiveButton(
											"충전하기",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													Intent intent = new Intent(
															ChatingForm.this,
															UserPoint.class);
													finish();
													startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));

												}
											})

									.show();

							return;
						}

						EditText message = (EditText) findViewById(R.id.message);
						if ("".equals(message.getText().toString().trim())) {
							Toast.makeText(ChatingForm.this, "공백은 전송하지 않습니다.",
									0).show();
							return;
						}
						if (Info.email.equals(ChatingForm.email)) {
							Toast.makeText(ChatingForm.this,
									"본인과의 대화는 전송하지 않습니다.", 0).show();
							return;
						}

						params.clear();
						params.add(new BasicNameValuePair("userEmail1",
								Info.email));
						params.add(new BasicNameValuePair("userEmail2",
								ChatingForm.email));
						params.add(new BasicNameValuePair("userName1",
								Info.name));
						params.add(new BasicNameValuePair("userName2",
								ChatingForm.name));
						params.add(new BasicNameValuePair("message", message
								.getText().toString()));
						result = Info
								.Submit("http://swcbizcard.nezip.co.kr/ChatMessageInsertAction.asp",
										params);
						if (!"".equals(result.trim())) {
							if ("YES".equals(result.trim())) {

								Info.runChating = true;
								Info.chatMessageLoding = true;

								Info.reciveMessage=true;
								timeHandler.sendEmptyMessage(0);

								Info.sendMessage("TEXT", message.getText().toString(),
										ChatingForm.regid);
								message.setText("");

							} else {
								Toast.makeText(ChatingForm.this,
										"전송실패 : 네트워크 상태가 좋지 않은것 같습니다.", 0)
										.show();
							}
						}
						loadingflag = true;
					} else {
						Toast.makeText(
								ChatingForm.this,
								"전송실패 : 채팅 메시지 수신거부 상태입니다.\n환경설정에서 채팅메시지 수신 상태로 변경하세요.",
								0).show();
					}
				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}
			}
		});

		Button photoAddBtn = (Button) findViewById(R.id.photoAddBtn);
		photoAddBtn.setOnClickListener(new OnClickListener() {

			int position = 0;

			@Override
			public void onClick(View v) {

				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					if ("".equals(ChatingForm.regid)) {
						params.clear();
						params.add(new BasicNameValuePair("userEmail",
								ChatingForm.email));
						ChatingForm.regid = Info
								.Submit("http://swcbizcard.nezip.co.kr/UserRegIDAction.asp",
										params);
					}
/*
					if ("".equals(ChatingForm.regid)) {
						Toast.makeText(ChatingForm.this,
								"'" + ChatingForm.name + "' 님은 오프라인 상태 입니다.", 0)
								.show();
						return;
					}
*/

					SharedPreferences isFirstRunPreferences = PreferenceManager
							.getDefaultSharedPreferences(ChatingForm.this);
					if (isFirstRunPreferences
							.getBoolean("MESSAGE_ACCEPT", true)) {
						params.clear();
						params.add(new BasicNameValuePair("userEmail",
								Info.email));
						String result = "";
						if ("corp".equals(ChatingForm.email)) {
							result = "10000";
						} else {
							result = Info
									.Submit("http://swcbizcard.nezip.co.kr/CorpUserPointAction.asp",
											params);
						}

						if (Integer.parseInt(result) < 50) {
							Toast.makeText(ChatingForm.this,
									"포인트 부족 : 발송에 필요한 포인트가 부족합니다.", 0).show();

							new AlertDialog.Builder(ChatingForm.this)
									.setMessage(
											"사진 메시지 발송에 필요한 포인트는 50P입니다.\n\n"
													+ "귀하의 가용 포인트는 "
													+ CustomTextWathcer
															.makeStringComma(result)
													+ "P 입니다.\n"
													+ "귀하의 포인트가 "
													+ (50 - Integer.parseInt(result))
													+ "P 부족합니다.\n"
													+ "채팅 이용권를 충전 하시겠습니까?")
									.setNegativeButton(
											"취소",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													return;
												}
											})
									.setPositiveButton(
											"충전하기",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													Intent intent = new Intent(
															ChatingForm.this,
															UserPoint.class);
													finish();
													startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));

												}
											})

									.show();

							return;
						}

						String[] arr = { "포토앨범", "사진찍기" };
						new AlertDialog.Builder(ChatingForm.this)
								.setTitle("첨부 파일")
								.setNegativeButton("취소", null)
								.setSingleChoiceItems(arr, 0,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												position = which;
											}

										})
								.setPositiveButton("확인",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												if (position == 0) {
													Intent i = new Intent(
															Intent.ACTION_PICK,
															android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
													startActivityForResult(i,
															position);
												} else if (position == 1) {

													Intent i = new Intent(
															MediaStore.ACTION_IMAGE_CAPTURE);
													File file = new File(
															getExternalFilesDir(Environment.DIRECTORY_DCIM),
															SAMPLEIMG);
													if (!file.exists()) {
														file.mkdirs();
														file.delete();
													} else {
														file.delete();
													}

													i.putExtra(
															MediaStore.EXTRA_OUTPUT,
															Uri.fromFile(file));
													startActivityForResult(i,
															position);

												}
											}
										}).show();
					} else {
						Toast.makeText(
								ChatingForm.this,
								"전송실패 : 채팅 메시지 수신거부 상태입니다.\n환경설정에서 채팅메시지 수신 상태로 변경하세요.",
								0).show();
					}
				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}
			}
		});

		chatList = (ListView) findViewById(R.id.chatList);
		chatList.startAnimation(new AnimationUtils().loadAnimation(
				ChatingForm.this, R.anim.push_left_in));
		chatList.setOnScrollListener(new OnScrollListener() {

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
					// Toast.makeText(Chat.this, "onScroll firstVisibleItem : "
					// + firstVisibleItem, 0).show();

					if (!isLoading && loadingflag) {

						// MyAsyncTask at = new MyAsyncTask();
						// at.execute();

						// Toast.makeText(Chat.this, "onScroll page : " +
						// firstVisibleItem, 0).show();

					}
				}

			}
		});

		message = (EditText) findViewById(R.id.message);

		LinearLayout messagePanal = (LinearLayout) findViewById(R.id.messagePanal);
		messagePanal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {

					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									message.getWindowToken(), 0);
						}
					}, 500);

				} catch (OutOfMemoryError e) {
					// System.gc();
				} catch (Exception e) {
				}

			}
		});

		Info.runChating = true;
		Info.chatMessageLoding = true;
		
		Info.reciveMessage=true;
		timeHandler.sendEmptyMessage(0);

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
			if (arSrc.get(position).ROW_VIEW == null || convertView == null) {

				LayoutInflater inflater = (LayoutInflater) maincon.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(layout, parent, false);
				
				try {
					final TextView txt1 = (TextView) convertView
							.findViewById(R.id.messageName);
					final TextView txt2 = (TextView) convertView
							.findViewById(R.id.leftMessage);
					final TextView txt3 = (TextView) convertView
							.findViewById(R.id.rightMessage);

					// 텍스트 셋팅
					if ((arSrc.get(position).USER_EMAIL2)
							.equals(Info.email)) {

						final LinearLayout layout = (LinearLayout) convertView
								.findViewById(R.id.rightMessageLayout);
						layout.setLayoutParams(lParams);
						txt1.setText((arSrc.get(position).USER_NAME1));
						final TextView txt5 = (TextView) convertView
								.findViewById(R.id.leftMessageDate);
						txt5.setText((
								arSrc.get(position).CREATE_DATE).substring(5));

						final ImageView userPhoto = (ImageView) convertView
								.findViewById(R.id.userPhoto);
						if (userPhotoImage != null) {
							userPhoto.setImageBitmap(Info
									.getRoundedCornerBitmap(userPhotoImage, 10));
						}

						final FrameLayout leftPhotoLayout = (FrameLayout) convertView
								.findViewById(R.id.leftPhotoLayout);

						if ((arSrc.get(position).MESSAGE)
								.length() > 7
								&& "PHOTO:".equals((
										arSrc.get(position).MESSAGE).substring(
										0, 6))) {
							txt2.setVisibility(View.GONE);

							final String photoUrl = (
									arSrc.get(position).MESSAGE).substring(6);
							final ImageView leftPhoto = (ImageView) convertView
									.findViewById(R.id.leftPhoto);
							Bitmap userPhotoImage = Info.GetImageFromURL(
									photoUrl, ChatingForm.this);
							if (userPhotoImage != null) {
								int w = userPhotoImage.getWidth();
								int h = userPhotoImage.getHeight();
								int r = Math.round(w / 300);
								userPhotoImage = Bitmap.createScaledBitmap(
										userPhotoImage,
										r == 0 ? w : Math.round(w / r),
										r == 0 ? h : Math.round(h / r), false);
								leftPhoto
										.setImageBitmap(Info
												.getRoundedCornerBitmap(userPhotoImage, 10));
							}

							leftPhoto.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									try {
										Intent find_go = new Intent(
												ChatingForm.this,
												PhotoView.class);
										find_go.putExtra("PHOTO", photoUrl);
										startActivity(find_go);
									} catch (OutOfMemoryError e) {
										// System.gc();
									} catch (Exception e) {
									}
								}
							});

						} else {
							leftPhotoLayout.setVisibility(View.GONE);
							if (!"PHOTO:".equals((arSrc
									.get(position).MESSAGE))) {
								txt2.setText((arSrc
										.get(position).MESSAGE));
							}
						}

					} else {
						final LinearLayout layout = (LinearLayout) convertView
								.findViewById(R.id.leftMessageLayout);
						layout.setLayoutParams(lParams);
						final TextView txt5 = (TextView) convertView
								.findViewById(R.id.rightMessageDate);
						txt5.setText((
								arSrc.get(position).CREATE_DATE).substring(5));

						final FrameLayout rightPhotoLayout = (FrameLayout) convertView
								.findViewById(R.id.rightPhotoLayout);
						if ((arSrc.get(position).MESSAGE)
								.length() > 6
								&& "PHOTO:".equals((
										arSrc.get(position).MESSAGE).substring(
										0, 6))) {
							txt3.setVisibility(View.GONE);

							final String photoUrl = (
									arSrc.get(position).MESSAGE).substring(6);
							final ImageView rightPhoto = (ImageView) convertView
									.findViewById(R.id.rightPhoto);
							Bitmap userPhotoImage = Info.GetImageFromURL(
									photoUrl, ChatingForm.this);
							if (userPhotoImage != null) {
								int w = userPhotoImage.getWidth();
								int h = userPhotoImage.getHeight();
								int r = Math.round(w / 300);
								userPhotoImage = Bitmap.createScaledBitmap(
										userPhotoImage,
										r == 0 ? w : Math.round(w / r),
										r == 0 ? h : Math.round(h / r), false);
								rightPhoto
										.setImageBitmap(Info
												.getRoundedCornerBitmap(userPhotoImage, 10));
							}

							rightPhoto
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											try {
												Intent find_go = new Intent(
														ChatingForm.this,
														PhotoView.class);
												find_go.putExtra("PHOTO",
														photoUrl);
												startActivity(find_go);
											} catch (OutOfMemoryError e) {
												// System.gc();
											} catch (Exception e) {
											}
										}
									});

						} else {
							rightPhotoLayout.setVisibility(View.GONE);
							if (!"PHOTO:".equals((arSrc
									.get(position).MESSAGE))) {
								txt3.setText((arSrc
										.get(position).MESSAGE));
							}

						}

					}

					arSrc.get(position).ROW_VIEW = convertView;
					
				} catch (Exception e) {
				}
				
			} else {
				convertView = arSrc.get(position).ROW_VIEW;
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

		@Override
		public void notifyDataSetChanged() {

			super.notifyDataSetChanged();
		}

	}

	class MyAsyncTask extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPreExecute() {
			// 작업을 시작하기 전 할일
			isLoading = true;
			loadingflag = false;

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				if (Info.chatMessageLoding) {
					loadXML();
				}

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
			// 작업이 완료 된 후 할일

			if (Info.chatMessageLoding) {
				dbload();
			}
			Info.chatMessageLoding = false;

			super.onPostExecute(result);

			loadingflag = true;
			isLoading = false;

		}

		@Override
		protected void onCancelled() {
			// 작업 취소
			// if (progressDialog != null && progressDialog.isShowing())
			// progressDialog.dismiss();
			super.onCancelled();
			loadingflag = true;
			isLoading = false;

			Info.chatMessageLoding = false;
		}

	}

	void loadXML() throws Exception {

		chatdbhelper = new ChatDBHelper(ChatingForm.this);
		SQLiteDatabase db = chatdbhelper.getWritableDatabase();
		db.beginTransaction();

		boolean sqlRun = true;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String result = "";

		SharedPreferences isFirstRunPreferences = PreferenceManager
				.getDefaultSharedPreferences(ChatingForm.this);
		if (isFirstRunPreferences.getBoolean("MESSAGE_ACCEPT", true)) {

			params.add(new BasicNameValuePair("userEmail1", Info.email));
			params.add(new BasicNameValuePair("userEmail2", ChatingForm.email));

			result = Info.Submit(
					"http://swcbizcard.nezip.co.kr/ChatMessageAction.asp",
					params);
			params.clear();

			// Log.i("result", result);
			if (!"".equals(result) && !"NODATA".equals(result.trim())) {

				String sql = "";

				String[] messageRow = result.split("</R>");
				// Log.i("loadXML", messageRow.length+" : " + result);
				for (int i = 0; i < messageRow.length; i++) {
					String[] messageCol = messageRow[i].split("</C>");
					if (messageCol.length > 6) {

						String id = ("00000000000000000" + messageCol[0])
								.substring(("00000000000000000" + messageCol[0])
										.length() - 17);
						sql = "INSERT INTO CHAT(ID, USER_EMAIL1, USER_EMAIL2, USER_NAME1, USER_NAME2, MESSAGE, CREATE_DATE) VALUES ( '"
								+ id.replaceAll("'", "''")
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
								+ messageCol[6].replaceAll("'", "''") + "');";

						try {
							db.execSQL(sql);
						} catch (SQLException e) {
							sqlRun = false;
						}

					}

				}
			}

			// 디비 나만 쓸거야 끝
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
			chatdbhelper.close();

			if (!sqlRun) {
				deleteDatabase("chat.db");
			}

		}
	}

	void dbload() {
		SQLiteDatabase db = null;
		Cursor cursor = null;
		lastPosition = 0;

		try {
			String sql = "";
			ChatDBHelper chatdbhelper = new ChatDBHelper(this);
			db = chatdbhelper.getWritableDatabase();

			cursor = null;

			SharedPreferences saveConfigPref = getSharedPreferences("Config",
					MODE_PRIVATE);
			if (firstSettingEnd) {
				lastID = Long.parseLong(saveConfigPref.getString(
						ChatingForm.email + "_MESSAGE_ID", "0"));
			} else {
				lastID = 0;
			}

			String id = ("00000000000000000" + Long.toString(lastID))
					.substring(("00000000000000000" + Long.toString(lastID))
							.length() - 17);

			sql = "SELECT ID, USER_EMAIL1, USER_EMAIL2, USER_NAME1, USER_NAME2, MESSAGE, CREATE_DATE FROM CHAT  ";
			sql = sql + " WHERE ID > '" + id + "' ";
			sql = sql + " AND ((USER_EMAIL1 = '" + Info.email
					+ "' AND USER_EMAIL2= '" + ChatingForm.email + "') ";
			sql = sql + "   OR (USER_EMAIL2 = '" + Info.email
					+ "' AND USER_EMAIL1= '" + ChatingForm.email + "')) ";
			sql = sql + " ORDER BY ID ASC  ";

			cursor = db.rawQuery(sql, null);
			// Log.i("SQL", id+"");
			while (cursor.moveToNext()) {

				if (!firstSettingEnd) {
					messages.add(new ChatData(-1, cursor.getString(0), cursor
							.getString(1), cursor.getString(2), cursor
							.getString(3), cursor.getString(4), cursor
							.getString(5), cursor.getString(6)));
				} else {
					MyAdapter.add(new ChatData(-1, cursor.getString(0), cursor
							.getString(1), cursor.getString(2), cursor
							.getString(3), cursor.getString(4), cursor
							.getString(5), cursor.getString(6)));
				}

				lastID = Long.parseLong(cursor.getString(0));
			}

			

			// Log.i("lastID", lastID+"");

			SharedPreferences.Editor editor = saveConfigPref.edit();
			editor.putString(ChatingForm.email + "_MESSAGE_ID",
					Long.toString(lastID));
			editor.commit();

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

			if (!firstSettingEnd) {

				MyAdapter = new MyAdapter(ChatingForm.this, R.layout.chatitem, messages);
				chatList.setAdapter(MyAdapter);
				chatList.setFocusable(false);
				firstSettingEnd = true;

			}
			
			if (firstSettingEnd){
				chatList.setSelection(messages.size() - 1);
				MyAdapter.notifyDataSetChanged();
			}

		}
	}

	@Override
	public void onPause() {

		Log.d("ChatingForm.this", "onPause");

		appStart = false;
		loadingflag = false;
		if (timeHandler != null) {
			timeHandler.removeMessages(0);
		}
		Info.runChating = false;

		super.onPause();

	}

	@Override
	public void onStop() {

		Log.d("ChatingForm.this", "onStop");

		appStart = false;
		loadingflag = false;
		if (timeHandler != null) {
			timeHandler.removeMessages(0);
		}

		Info.runChating = false;

		super.onStop();

	}

	@Override
	public void onDestroy() {

		Log.d("ChatingForm.this", "onDestroy");

		appStart = false;
		loadingflag = false;
		if (timeHandler != null) {
			timeHandler.removeMessages(0);
		}

		Info.runChating = false;
 
		super.onDestroy();

	}

	@Override
	public void onStart() {

		loadingflag = true;
		appStart = true;

		Info.runChating = true;
		Info.chatMessageLoding = true;

		super.onStart();

	}

	@Override
	public void onResume() {

		loadingflag = true;
		appStart = true;

		NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		if (getIntent().getExtras().getInt("NOTIID") != 0) {
			NM.cancel(getIntent().getExtras().getInt("NOTIID"));
		}

		try {
			for (int i = 0; i < AlarmService.chatDatas.size(); i++) {
				if (ChatingForm.email.equals(AlarmService.chatDatas.get(i)
						.getUserEmail1())
						&& "CHAT".equals(AlarmService.chatDatas.get(i)
								.getUserEmail2())) {
					NM.cancel(Integer.parseInt(AlarmService.chatDatas.get(i)
							.getId()));
					AlarmService.chatDatas.remove(i);
					i--;
				}
			}
		} catch (Exception e) {
			;
		}

		Info.runChating = true;
		Info.chatMessageLoding = true;

		Info.reciveMessage=true;
		timeHandler.sendEmptyMessage(0);

		// Log.i("Chat.photo", ":"+this.photo);

		super.onResume();

	}

	@Override
	public void onRestart() {

		loadingflag = true;
		appStart = true;

		Info.runChating = true;
		Info.chatMessageLoding = true;

		super.onRestart();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		filePath = "";
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

			try {

				Cursor c = getContentResolver().query(
						Uri.parse(data.getData().toString()), null, null, null,
						null);
				c.moveToNext();
				filePath = c.getString(c
						.getColumnIndex(MediaStore.MediaColumns.DATA));
				c.close();

			} catch (Exception e) {
				;
			}

		} else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			File file = new File(
					getExternalFilesDir(Environment.DIRECTORY_DCIM), SAMPLEIMG);
			filePath = file.getAbsolutePath();
			file = null;

		}

		if ((requestCode == 0 || requestCode == 1)
				&& resultCode == Activity.RESULT_OK) {

			File file = new File(
					getExternalFilesDir(Environment.DIRECTORY_DCIM), "crop_"
							+ SAMPLEIMG);
			if (!file.exists()) {
				file.mkdirs();
				file.delete();
			} else {
				file.delete();
			}

			Intent i = new Intent("com.android.camera.action.CROP");
			i.setDataAndType(Uri.fromFile(new File(filePath)), "image/*");
			// i.putExtra("outputX", 1000);
			// i.putExtra("outputY", 1400);
			i.putExtra("aspectX", 1);
			i.putExtra("aspectY", 1.2);
			i.putExtra("scale", true);
			// i.putExtra("return-data", true);
			i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(i, 2);

		} else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {

			filePath = "";
			File file = new File(
					getExternalFilesDir(Environment.DIRECTORY_DCIM), "crop_"
							+ SAMPLEIMG);
			filePath = file.getAbsolutePath();

			if (!"".equals(filePath)) {
				try {
					options.inTempStorage = new byte[16 * 1024];
					options.inSampleSize = 1;

					Bitmap sbm = null;
					sbm = BitmapFactory.decodeFile(filePath, options);
					if (sbm.getWidth() > 600) {
						float r2 = (float) (600) / (float) sbm.getWidth();
						sbm = Bitmap.createScaledBitmap(sbm,
								Math.round(sbm.getWidth() * r2),
								Math.round(sbm.getHeight() * r2), false);
					}

					File targetFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "small_photo.png");
					FileOutputStream out = new FileOutputStream(targetFile);
					sbm.compress(Bitmap.CompressFormat.PNG, 100, out);
					sbm.recycle();
					out.flush();
					out.close();

					String fileName = System.currentTimeMillis() + "." + Info.getExtension(targetFile.getName());
					String result = Info.fileupload(targetFile, fileName, replaceHandler);
					targetFile = null;
					if (!"".equals(result)) {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.clear();
						params.add(new BasicNameValuePair("userEmail1",
								Info.email));
						params.add(new BasicNameValuePair("userEmail2",
								ChatingForm.email));
						params.add(new BasicNameValuePair("userName1",
								Info.name));
						params.add(new BasicNameValuePair("userName2",
								ChatingForm.name));
						params.add(new BasicNameValuePair("message", "PHOTO:"
								+ result));

						result = Info
								.Submit("http://swcbizcard.nezip.co.kr/ChatMessageInsertAction.asp",
										params);
						if (!"".equals(result.trim())) {
							if ("YES".equals(result.trim())) {

								Info.runChating = true;
								Info.chatMessageLoding = true;

								Info.reciveMessage=true;
								timeHandler.sendEmptyMessage(0);

								if ("".equals(ChatingForm.regid)) {
									params.clear();
									params.add(new BasicNameValuePair(
											"userEmail", ChatingForm.email));
									ChatingForm.regid = Info
											.Submit("http://swcbizcard.nezip.co.kr/UserRegIDAction.asp",
													params);
								}

								if (!"".equals(ChatingForm.regid)) {
									Info.sendMessage("PHOTO", "", ChatingForm.regid);
								}

							} else {
								Toast.makeText(ChatingForm.this,
										"전송실패 : 네트워크 상태가 좋지 않은것 같습니다.", 0)
										.show();
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

}
