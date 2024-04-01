package com.nezip.bizcard1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ChatDBHelper extends SQLiteOpenHelper 
{
	
	public ChatDBHelper(Context context) 
	{
		super(context, "chat.db", null, 3); 
	}

	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE CHAT ( ID TEXT, USER_EMAIL1 TEXT, USER_EMAIL2 TEXT, USER_NAME1 TEXT, USER_NAME2 TEXT, MESSAGE TEXT, CREATE_DATE TEXT );");
	}

	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS CHAT");
		onCreate(db);
	}
}