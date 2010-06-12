/* Copyright (c) 2008-2010 -- CommonsWare, LLC

	 Licensed under the Apache License, Version 2.0 (the "License");
	 you may not use this file except in compliance with the License.
	 You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

	 Unless required by applicable law or agreed to in writing, software
	 distributed under the License is distributed on an "AS IS" BASIS,
	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 See the License for the specific language governing permissions and
	 limitations under the License.
*/
	 
package com.commonsware.android.constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorManager;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME="db";
	public static final String TITLE="title";
	public static final String VALUE="value";
	
	public static String getBackupPath(Context ctxt) {
		return(ctxt.getDatabasePath(DATABASE_NAME).getAbsolutePath()+".backup");
	}
	
	public DatabaseHelper(Context ctxt) {
		super(ctxt, DATABASE_NAME, null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE constants (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, value REAL);");
		
		ContentValues cv=new ContentValues();
		
		cv.put(TITLE, "Gravity, Death Star I");
		cv.put(VALUE, SensorManager.GRAVITY_DEATH_STAR_I);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Earth");
		cv.put(VALUE, SensorManager.GRAVITY_EARTH);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Jupiter");
		cv.put(VALUE, SensorManager.GRAVITY_JUPITER);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Mars");
		cv.put(VALUE, SensorManager.GRAVITY_MARS);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Mercury");
		cv.put(VALUE, SensorManager.GRAVITY_MERCURY);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Moon");
		cv.put(VALUE, SensorManager.GRAVITY_MOON);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Neptune");
		cv.put(VALUE, SensorManager.GRAVITY_NEPTUNE);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Pluto");
		cv.put(VALUE, SensorManager.GRAVITY_PLUTO);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Saturn");
		cv.put(VALUE, SensorManager.GRAVITY_SATURN);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Sun");
		cv.put(VALUE, SensorManager.GRAVITY_SUN);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, The Island");
		cv.put(VALUE, SensorManager.GRAVITY_THE_ISLAND);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Uranus");
		cv.put(VALUE, SensorManager.GRAVITY_URANUS);
		db.insert("constants", TITLE, cv);
		
		cv.put(TITLE, "Gravity, Venus");
		cv.put(VALUE, SensorManager.GRAVITY_VENUS);
		db.insert("constants", TITLE, cv);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		android.util.Log.w("Constants", "Upgrading database, which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS constants");
		onCreate(db);
	}
}