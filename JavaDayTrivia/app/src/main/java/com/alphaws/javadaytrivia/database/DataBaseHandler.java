package com.alphaws.javadaytrivia.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "JavaDayTrivia.db";
	private static final int DATABASE_VERSION = 1;
	
	//TABLES
	public static final String USER = "User";
	public static final String BEACON = "Beacon";
	
	//COLUMNS
	public static final String USER_FIRTSNAME = "firstName";
	public static final String USER_LASTSNAME = "lastName";
	public static final String USER_EMAIL = "email";
	public static final String USER_OCCUPATION = "occupation";

	public static final String BEACON_UUID = "uuid";
	public static final String BEACON_MAJOR = "major";
	public static final String BEACON_MINOR = "minor";
	public static final String BEACON_PLACE = "place";
	public static final String BEACON_SEEN = "seen";

	private static DataBaseHandler databaseHandler;
	
	public static DataBaseHandler getInstance(Context context){
		if(databaseHandler == null){
			databaseHandler = new DataBaseHandler(context);
		}
		return databaseHandler;
	}
	public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + USER + "( "
				+ USER_EMAIL  + " TEXT PRIMARY KEY, "
				+ USER_FIRTSNAME   + " TEXT, "
				+ USER_LASTSNAME + " TEXT, "
				+ USER_OCCUPATION   + " INTEGER);";
		
		db.execSQL(sql);
		
		sql = "CREATE TABLE " + BEACON + "( "
				+ BEACON_UUID   + " TEXT, "
				+ BEACON_MAJOR + " TEXT, "
				+ BEACON_MINOR + " TEXT, "
				+ BEACON_SEEN + " INTEGER, "
				+ BEACON_PLACE    + " INTEGER PRIMARY KEY);";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
