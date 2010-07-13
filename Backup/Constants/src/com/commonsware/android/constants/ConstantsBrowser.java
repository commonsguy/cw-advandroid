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

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ConstantsBrowser extends ListActivity {
	public static final Object[] BACKUP_LOCK=new Object[0];
	private static final int ADD_ID = Menu.FIRST+1;
	private static final int DELETE_ID = Menu.FIRST+3;
	private DatabaseHelper db=null;
	private Cursor constantsCursor=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new BackupTask().execute();
	}
	
	private void initUI() {
		db=new DatabaseHelper(this);
		constantsCursor=db
											.getReadableDatabase()
											.rawQuery("SELECT _ID, title, value "+
																"FROM constants ORDER BY title",
																null);
	
		ListAdapter adapter=new SimpleCursorAdapter(this,
													R.layout.row, constantsCursor,
													new String[] {"title", "value"},
													new int[] {R.id.title, R.id.value});
		
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		constantsCursor.close();
		db.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, ADD_ID, Menu.NONE, "Add")
				.setIcon(R.drawable.add)
				.setAlphabeticShortcut('a');

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case ADD_ID:
				add();
				return(true);
		}

		return(super.onOptionsItemSelected(item));
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
																		ContextMenu.ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, DELETE_ID, Menu.NONE, "Delete")
				.setIcon(R.drawable.delete)
				.setAlphabeticShortcut('d');
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case DELETE_ID:
				AdapterView.AdapterContextMenuInfo info=
					(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

				delete(info.id);
				return(true);
		}

		return(super.onOptionsItemSelected(item));
	}
	
	private void add() {
		LayoutInflater inflater=LayoutInflater.from(this);
		View addView=inflater.inflate(R.layout.add_edit, null);
		final DialogWrapper wrapper=new DialogWrapper(addView);
		
		new AlertDialog.Builder(this)
			.setTitle(R.string.add_title)
			.setView(addView)
			.setPositiveButton(R.string.ok,
													new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
															int whichButton) {
					processAdd(wrapper);
				}
			})
			.setNegativeButton(R.string.cancel,
													new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
															int whichButton) {
					// ignore, just dismiss
				}
			})
			.show();
	}
	
	private void delete(final long rowId) {
		if (rowId>0) {
			new AlertDialog.Builder(this)
				.setTitle(R.string.delete_title)
				.setPositiveButton(R.string.ok,
														new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
																int whichButton) {
						processDelete(rowId);
					}
				})
				.setNegativeButton(R.string.cancel,
														new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
																int whichButton) {
					// ignore, just dismiss
					}
				})
				.show();
		}
	}
	
	private void processAdd(DialogWrapper wrapper) {
		ContentValues values=new ContentValues(2);
		
		values.put("title", wrapper.getTitle());
		values.put("value", wrapper.getValue());
		
		db.getWritableDatabase().insert("constants", "title", values);
		constantsCursor.requery();
	}
	
	private void processDelete(long rowId) {
		String[] args={String.valueOf(rowId)};
		
		db.getWritableDatabase().delete("constants", "_ID=?", args);
		constantsCursor.requery();
	}
	
	static void copyFile(File src, File dest) throws IOException {
		if (!dest.exists()) {
			dest.createNewFile();
		}
 
		FileChannel srcChannel=null;
		FileChannel destChannel=null;
		
		try {
			srcChannel=new FileInputStream(src).getChannel();
			destChannel=new FileOutputStream(dest).getChannel();
			
			destChannel.transferFrom(srcChannel, 0, srcChannel.size());
			dest.setLastModified(src.lastModified());
		}
		finally {
			if (srcChannel!=null) {
				srcChannel.close();
			}
	 
			if (destChannel!=null) {
				destChannel.close();
			}
		}
	}
	
	class DialogWrapper {
		EditText titleField=null;
		EditText valueField=null;
		View base=null;
		
		DialogWrapper(View base) {
			this.base=base;
			valueField=(EditText)base.findViewById(R.id.value);
		}
		
		String getTitle() {
			return(getTitleField().getText().toString());
		}
		
		float getValue() {
			return(new Float(getValueField().getText().toString())
																									.floatValue());
		}
		
		private EditText getTitleField() {
			if (titleField==null) {
				titleField=(EditText)base.findViewById(R.id.title);
			}
			
			return(titleField);
		}
		
		private EditText getValueField() {
			if (valueField==null) {
				valueField=(EditText)base.findViewById(R.id.value);
			}
			
			return(valueField);
		}
	}
	
	class BackupTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			synchronized(BACKUP_LOCK) {
				File currentDb=getDatabasePath(DatabaseHelper.DATABASE_NAME);
				File backupDb=new File(DatabaseHelper.getBackupPath(ConstantsBrowser.this));
				boolean restore=false;
				boolean backup=false;
				
				if (backupDb.exists()) {
					if (!currentDb.exists() ||
							backupDb.lastModified()>currentDb.lastModified()) {
						restore=true;	// backup newer or db missing, so do a restore
					}
					else if (backupDb.lastModified()<currentDb.lastModified()) {
						backup=true;	// data changed, so do a backup
					}
					else {
						// no data change, so no need for backup or restore
					}
				}
				else if (currentDb.exists()) {
					backup=true;	// backup does not exist, so do a backup
				}
				else {
					// no original or backup, so nothing to do
				}
				
				try {
					if (backup) {
Log.w("ConstantsBrowser", "Making backup");						
						copyFile(currentDb, backupDb);
						requestBackupToCloud();
					}
					else if (restore) {
Log.w("ConstantsBrowser", "Restoring backup");						
						copyFile(backupDb, currentDb);
					}
					else {
Log.w("ConstantsBrowser", "Nothing to do!");						
					}
				}
				catch (Throwable t) {
					Log.e("ConstantsBrowser", "Exception making database backup", t);
				}
			}
			
			return(null);
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			initUI();
		}
		
		private void requestBackupToCloud() {
			try {
				Class clsBackupMgr=Class.forName("android.app.backup.BackupManager");
				
				clsBackupMgr
					.getMethod("dataChanged", String.class)
					.invoke(clsBackupMgr, "com.commonsware.android.constants");
			}
			catch (Throwable t) {
				Log.e("ConstantsBrowser", "Exception requesting backup to cloud", t);
			}
		}
	}
}