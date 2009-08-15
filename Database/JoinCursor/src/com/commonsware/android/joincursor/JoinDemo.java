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
	 
package com.commonsware.android.joincursor;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;

public class JoinDemo extends ListActivity {
	public static String NOTE="_NOTE";
	private static String NOTE_ID="NOTE_ID";
	private static String[] PROJECTION=new String[] { CallLog.Calls._ID,
																										CallLog.Calls.NUMBER,
																										CallLog.Calls.DATE,
																										CallLog.Calls.DURATION
																									};
	private static SimpleDateFormat FORMAT=new SimpleDateFormat("MM/d h:mm a");
	private Cursor cursor=null;
	private int noteColumn=-1;
	private int idColumn=-1;
	private int noteIdColumn=-1;
	private SQLiteDatabase db=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Cursor c=managedQuery(android.provider.CallLog.Calls.CONTENT_URI,
													PROJECTION, null, null,
													CallLog.Calls.DATE+" DESC");
		
		cursor=new JoinCursor(c, join);
		noteColumn=cursor.getColumnIndex(NOTE);
		idColumn=cursor.getColumnIndex(CallLog.Calls._ID);
		noteIdColumn=cursor.getColumnIndex(NOTE_ID);
		setListAdapter(new CallPlusAdapter(cursor));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		cursor.requery();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (db!=null) {
			db.close();
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v,
																	int position, long id) {
		cursor.moveToPosition(position);
		
		String note=cursor.getString(noteColumn);
		
		if (note==null || note.length()==0) {
			Intent i=new Intent(this, NoteEditor.class);
			
			i.putExtra(NOTE, note);
			i.putExtra("call_id", cursor.getInt(idColumn));
			i.putExtra("note_id", cursor.getInt(noteIdColumn));
			startActivityForResult(i, 1);
		}
		else {
			Intent i=new Intent(this, NoteActivity.class);
			
			i.putExtra(NOTE, note);
			startActivity(i);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode,
																		int resultCode,
																		Intent data) {
		String note=data.getStringExtra(NOTE);
		
		if (note!=null) {
			int noteId=data.getIntExtra(NOTE_ID, -1);
			ContentValues cv=new ContentValues();
			
			cv.put("note", note);
			
			if (noteId==-1) {
				int callId=data.getIntExtra("call_id", -1);
				
				cv.put("call_id", callId);
				
				getDb().insertOrThrow("call_notes", "_id", cv);
			}
			else {
				String[] args={String.valueOf(noteId)};
				
				getDb().update("call_notes", cv, "_ID", args);
			}
		}
	}
	
	SQLiteDatabase getDb() {
		if (db==null) {
			db=(new NotesInstaller(JoinDemo.this)).getWritableDatabase();
		}
		
		return(db);
	}
	
	I_JoinHandler join=new I_JoinHandler() {
		String[] columns={NOTE_ID, NOTE};
		
		public String[] getColumnNames() {
				return(columns);
		}
		
		public String getCacheKey(Cursor c) {
				return(String.valueOf(c.getInt(c.getColumnIndex(CallLog.Calls._ID))));
		}
		
		public ContentValues getJoin(Cursor c) {
			String[] args={getCacheKey(c)};
			Cursor j=getDb().rawQuery("SELECT _ID, note FROM call_notes WHERE call_id=?", args);
			ContentValues result=new ContentValues();
			
			j.moveToFirst();
			
			if (j.isAfterLast()) {
				result.put(columns[0], -1);
				result.put(columns[1], (String)null);
			}
			else {
				result.put(columns[0], j.getInt(0));
				result.put(columns[1], j.getString(1));
			}
			
			j.close();
			
			return(result);
		}
	};
	
	class CallPlusAdapter extends CursorAdapter {
		CallPlusAdapter(Cursor c) {
			super(JoinDemo.this, c);
		}
		
		@Override
		public void bindView(View row, Context ctxt,
													Cursor c) {
			ViewWrapper wrapper=(ViewWrapper)row.getTag();
			
			wrapper.update(c);
		}
		
		@Override
		public View newView(Context ctxt, Cursor c,
													ViewGroup parent) {
			LayoutInflater inflater=getLayoutInflater();
			
			View row=inflater.inflate(R.layout.row, null);
			ViewWrapper wrapper=new ViewWrapper(row);
			
			row.setTag(wrapper);
			wrapper.update(c);
			
			return(row);
		}
	}

	class ViewWrapper {
		View base;
		TextView number=null;
		TextView duration=null;
		TextView time=null;
		ImageView icon=null;
		
		ViewWrapper(View base) {
			this.base=base;
		}
		
		TextView getNumber() {
			if (number==null) {
				number=(TextView)base.findViewById(R.id.number);
			}
			
			return(number);
		}
		
		TextView getDuration() {
			if (duration==null) {
				duration=(TextView)base.findViewById(R.id.duration);
			}
			
			return(duration);
		}
		
		TextView getTime() {
			if (time==null) {
				time=(TextView)base.findViewById(R.id.time);
			}
			
			return(time);
		}
		
		ImageView getIcon() {
			if (icon==null) {
				icon=(ImageView)base.findViewById(R.id.note);
			}
			
			return(icon);
		}
		
		void update(Cursor c) {
			getNumber().setText(c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)));
			getTime().setText(FORMAT.format(c.getInt(c.getColumnIndex(CallLog.Calls.DATE))));
			getDuration().setText(c.getString(c.getColumnIndex(CallLog.Calls.DURATION))+" seconds");
			
			String note=c.getString(c.getColumnIndex(NOTE));
			
			if (note!=null && note.length()>0) {
				getIcon().setVisibility(View.VISIBLE);
			}
			else {
				getIcon().setVisibility(View.GONE);
			}
		}
	}
}
