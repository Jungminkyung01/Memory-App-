package com.nezip.bizcard1;

import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

public class ContactList extends Activity {

	private String[] nameList;
	private String[] phoneList;

	private ArrayList<String> lists = new ArrayList<String>();	
	private ArrayList<String> list;
	private ArrayAdapter<String> arrayAdapter;
	

	@Override
	public void onBackPressed() 
	{
		
		Intent intent = new Intent(getApplicationContext(), MyCardView.class);
		finish();
		startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactlist);

		list = new ArrayList<String>();

		new loadContactsData().execute("");
	}

	public void getContactsData() {

		Cursor c = Info.getPhoneList(this);
		
		nameList = new String[c.getCount()];
		phoneList = new String[c.getCount()];

		c.moveToFirst();		
		for (int i = 0; i < c.getCount(); i++) {
			phoneList[i] = c.getString(1).trim();
			nameList[i] = c.getString(0).trim() + " : " + phoneList[i];
			
			if (nameList[i] != null && nameList[i].length() > 0) {
				list.add(nameList[i]);
			}
			c.moveToNext();
		}
		c.close();
		
	}

	private class loadContactsData extends AsyncTask<String, Void, Void> {

		private String Content;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(ContactList.this);

		protected void onPreExecute() {
			Dialog.setMessage("주소록 정보를 로딩 중입니다.");
			Dialog.show();
		}

		protected Void doInBackground(String... urls) {

			getContactsData();
			return null;
		}

		protected void onPostExecute(Void unused) {
			try {
				Dialog.dismiss();
				final ListView listView = (ListView) findViewById(R.id.list);
				listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				if (listView != null) {
					
					arrayAdapter = new ArrayAdapter<String>(getBaseContext(),
							android.R.layout.simple_list_item_multiple_choice, list);

					if (arrayAdapter != null)
						listView.setAdapter(arrayAdapter);

					listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@SuppressWarnings("rawtypes")
						@Override
						public void onItemClick(AdapterView parent, View view, int position, long id) {
							int selectedCount = 0;
							lists.clear();
							SparseBooleanArray sparse = listView.getCheckedItemPositions();
							final int length = listView.getCount();
							for(int i = 0; i < length; i++){
								if(sparse.get(i)){
									//Log.d("선택", list.get(i));
									lists.add(list.get(i));
									selectedCount ++;
								}
							}
							//Log.d("선택", selectedCount + "개 선택됨");
						}
					});

				}

				EditText findText = (EditText) findViewById(R.id.input);
				findText.addTextChangedListener(new TextWatcher() {
					public void afterTextChanged(Editable s) {
						findName(s.toString());
					}

					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					public void onTextChanged(CharSequence s, int start, int before, int count) {
					}
				});

				if (Error != null) {
					Toast.makeText(ContactList.this, Error, Toast.LENGTH_LONG).show();
				} else {
					// Toast.makeText(parent, "Source: " + Content,
					// Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	final static char[] chosungWord = { 0x3131, 0x3132, 0x3134, 0x3137, 0x3138,
			0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148,
			0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };

	public String hangulToOnlyChosung(String s) {
		int chosungNum, tempNum;
		String resultString = "";

		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (ch != ' ') {
				if (ch >= 0xAC00 && ch <= 0xD7A3) {
					tempNum = ch - 0xAC00;
					chosungNum = tempNum / (21 * 28);

					resultString += chosungWord[chosungNum];
				} else {
					resultString += ch;
				}
			}
		}
		return resultString;
	}

	public void findName(String findString) {
		try {
			if (findString.length() > 0) {
				String onlyChosungString = hangulToOnlyChosung(findString);
				list.clear();
				for (int i = 0; i < nameList.length; i++) {
					if (nameList[i] != null) {
						String onlyChosungNameString = hangulToOnlyChosung(nameList[i]);
						if (onlyChosungNameString.matches(".*" + onlyChosungString + ".*")) {
							if (nameList[i] != null)
								list.add(nameList[i]);
						}
					}
				}
				arrayAdapter.notifyDataSetChanged();
			} else {
				list.clear();
				for (int i = 0; i < nameList.length; i++) {
					if (nameList[i] != null)
						list.add(nameList[i]);
				}
				arrayAdapter.notifyDataSetChanged();
			}

		} catch (Exception e) {
		}
	}
	
	@Override
	public void onDestroy() {

		super.onDestroy();

	}
}