package com.nezip.bizcard1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class PostDBHelper extends SQLiteOpenHelper 
{
	
	public PostDBHelper(Context context) 
	{
		super(context, "post.db", null, 3);
	}

	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE POST ( POST_NO TEXT, POST_ADDR TEXT, USE_ADDR TEXT );");
	}

	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS POST");
		onCreate(db);
	}
}