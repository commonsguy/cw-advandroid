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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import java.util.LinkedHashMap;
import java.util.Map;

class JoinCursor extends CursorWrapper {
	private I_JoinHandler join=null;
	private JoinCache cache=new JoinCache(100);
	
	JoinCursor(Cursor main, I_JoinHandler join) {
		super(main);
		
		this.join=join;
	}
	
	public int getColumnCount() {
		return(super.getColumnCount()+join.getColumnNames().length);
	}
	
	public int getColumnIndex(String columnName) {
		for (int i=0;i<join.getColumnNames().length;i++) {
			if (columnName.equals(join.getColumnNames()[i])) {
				return(super.getColumnCount()+i);
			}
		}
		
		return(super.getColumnIndex(columnName));
	}
	
	public int getColumnIndexOrThrow(String columnName) {
		for (int i=0;i<join.getColumnNames().length;i++) {
			if (columnName.equals(join.getColumnNames()[i])) {
				return(super.getColumnCount()+i);
			}
		}
		
		return(super.getColumnIndexOrThrow(columnName));
	}
	
	public String getColumnName(int columnIndex) {
		if (columnIndex>=super.getColumnCount()) {
			return(join.getColumnNames()[columnIndex-super.getColumnCount()]);
		}
		
		return(super.getColumnName(columnIndex));
	}
	
	public byte[] getBlob(int columnIndex) {
		if (columnIndex>=super.getColumnCount()) {
			ContentValues extras=cache.get(join.getCacheKey(this));
			int offset=columnIndex-super.getColumnCount();
			
			return(extras.getAsByteArray(join.getColumnNames()[offset]));
		}
		
		return(super.getBlob(columnIndex));
	}
	
	public double getDouble(int columnIndex) {
		if (columnIndex>=super.getColumnCount()) {
			ContentValues extras=cache.get(join.getCacheKey(this));
			int offset=columnIndex-super.getColumnCount();
			
			return(extras.getAsDouble(join.getColumnNames()[offset]));
		}
		
		return(super.getDouble(columnIndex));
	}
	
	public float getFloat(int columnIndex) {
		if (columnIndex>=super.getColumnCount()) {
			ContentValues extras=cache.get(join.getCacheKey(this));
			int offset=columnIndex-super.getColumnCount();
			
			return(extras.getAsFloat(join.getColumnNames()[offset]));
		}
		
		return(super.getFloat(columnIndex));
	}
	
	public int getInt(int columnIndex) {
		if (columnIndex>=super.getColumnCount()) {
			ContentValues extras=cache.get(join.getCacheKey(this));
			int offset=columnIndex-super.getColumnCount();
			
			return(extras.getAsInteger(join.getColumnNames()[offset]));
		}
		
		return(super.getInt(columnIndex));
	}
	
	public long getLong(int columnIndex) {
		if (columnIndex>=super.getColumnCount()) {
			ContentValues extras=cache.get(join.getCacheKey(this));
			int offset=columnIndex-super.getColumnCount();
			
			return(extras.getAsLong(join.getColumnNames()[offset]));
		}
		
		return(super.getLong(columnIndex));
	}
	
	public short getShort(int columnIndex) {
		if (columnIndex>=super.getColumnCount()) {
			ContentValues extras=cache.get(join.getCacheKey(this));
			int offset=columnIndex-super.getColumnCount();
			
			return(extras.getAsShort(join.getColumnNames()[offset]));
		}
		
		return(super.getShort(columnIndex));
	}
	
	public String getString(int columnIndex) {
		if (columnIndex>=super.getColumnCount()) {
			ContentValues extras=cache.get(join.getCacheKey(this));
			int offset=columnIndex-super.getColumnCount();
			
			return(extras.getAsString(join.getColumnNames()[offset]));
		}
		
		return(super.getString(columnIndex));
	}
	
	public boolean isNull(int columnIndex) {
		if (columnIndex>=super.getColumnCount()) {
			ContentValues extras=cache.get(join.getCacheKey(this));
			int offset=columnIndex-super.getColumnCount();
			
			return(extras.get(join.getColumnNames()[offset])==null);
		}
		
		return(super.isNull(columnIndex));
	}
	
	public boolean requery() {
		cache.clear();
		
		return(super.requery());
	}
	
	class JoinCache extends LinkedHashMap<String, ContentValues> {
		private int capacity=100;
		
		JoinCache(int capacity) {
			super(capacity+1, 1.1f, true);
			this.capacity=capacity;
		}
		
		protected boolean removeEldestEntry(Entry<String, ContentValues> eldest) {
			return(size()>capacity);
		}
		
		ContentValues get(String key) {
			ContentValues result=super.get(key);
			
			if (result==null) {
				result=join.getJoin(JoinCursor.this);
				put(key, result);
			}
			
			return(result);
		}
	}
}
