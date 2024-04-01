package com.nezip.bizcard1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ContactDBHelper extends SQLiteOpenHelper 
{
	
	public ContactDBHelper(Context context) 
	{
		super(context, "phone.db", null, 3);
	}

	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE PHONE ( DISPLAY_NAME TEXT, PHONE_NUMBER TEXT );");
	}

	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS PHONE");
		onCreate(db);
	}
}