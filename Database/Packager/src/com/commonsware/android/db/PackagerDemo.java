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

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PackagerDemo extends ListActivity {
	private static final String[] PROJECTION = new String[] {
																	"_id", "title",	"value"};
	private Cursor constantsCursor;
	private SQLiteDatabase db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db=(new ConstantsInstaller(this, "constants.db", null, 1))
																		.getReadableDatabase();
		
		constantsCursor=db.query("constants", PROJECTION, null,
															null, null, null, "title ASC");
	
		ListAdapter adapter=new SimpleCursorAdapter(this,
													R.layout.row, constantsCursor,
													new String[] {"title",	"value"},
													new int[] {R.id.title, R.id.value});
		
		setListAdapter(adapter);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		db.close();
	}
}
