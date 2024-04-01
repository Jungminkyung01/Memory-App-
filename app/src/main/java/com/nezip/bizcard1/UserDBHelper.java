package com.nezip.bizcard1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class UserDBHelper extends SQLiteOpenHelper 
{
	
	public UserDBHelper(Context context) 
	{
		super(context, "corpMember.db", null, 3);
	}

	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE CORPMEMBER ( USER_EMAIL TEXT, USER_NAME TEXT, COMPANY_NAME TEXT, DEPT TEXT, GRADE TEXT, OFFICE_ADDR TEXT, UPMOO TEXT, OFFICE_TEL TEXT, OFFICE_FAX TEXT, MOBILE TEXT, USER_USE_DT TEXT,WORK_EMAIL TEXT, TOP_PHONE TEXT, HOME_PAGE TEXT, KEYWORD TEXT, COMPANY_LOGO TEXT, MY_PHOTO TEXT, ID TEXT, INSERT_DATE TEXT, PAGE TEXT, POSITION TEXT, REG_ID TEXT );");
	}

	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS CORPMEMBER");
		onCreate(db);
	}
}