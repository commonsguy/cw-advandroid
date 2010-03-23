/* Copyright (c) 2008-10 -- CommonsWare, LLC

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
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import java.io.*;


abstract class DatabaseInstaller extends SQLiteOpenHelper {
	abstract void handleInstallError(Throwable t);
	
	private Context ctxt=null;
	
	public DatabaseInstaller(Context context, String name,
														SQLiteDatabase.CursorFactory factory,
														int version)	{
		super(context, name, factory, version);
		
		this.ctxt=context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			InputStream stream=ctxt
													.getResources()
													.openRawResource(R.raw.packaged_db);
			InputStreamReader is=new InputStreamReader(stream);
			BufferedReader in=new BufferedReader(is);
			String str;
	
			while ((str = in.readLine()) != null) {
				if (!str.equals("BEGIN TRANSACTION;") && !str.equals("COMMIT;")) {
					db.execSQL(str);
				}
			}
			
			in.close();
		}
		catch (IOException e) {
			handleInstallError(e);
		}
	}
}