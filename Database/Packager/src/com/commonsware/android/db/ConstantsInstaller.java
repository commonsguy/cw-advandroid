/* Copyright (c) 2008 -- CommonsWare, LLC

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
	 
package com.commonsware.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


class ConstantsInstaller	extends DatabaseInstaller {
	public ConstantsInstaller(Context context, String name,
														SQLiteDatabase.CursorFactory factory,
														int version)	{
		super(context, name, factory, version);
	}
	
	void handleInstallError(Throwable t) {
		Log.e("Constants", "Exception installing database", t);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion,
													int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS constants");
		onCreate(db);
	}
}