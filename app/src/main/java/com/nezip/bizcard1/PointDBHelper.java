package com.nezip.bizcard1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class PointDBHelper extends SQLiteOpenHelper 
{
	
	public PointDBHelper(Context context) 
	{
		super(context, "points.db", null, 3);
	}

	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE POINTS ( ID TEXT, USER_EMAIL TEXT, USER_NAME TEXT, POINT TEXT, INPUT_NAME TEXT, INPUT_TIME TEXT, CHECK_OK TEXT, INSERT_DATE TEXT  );");
	}

	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS POINTS");
		onCreate(db);
	}
}