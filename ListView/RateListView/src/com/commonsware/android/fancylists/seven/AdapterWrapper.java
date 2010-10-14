/***
	Copyright (c) 2008-2010 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package com.commonsware.android.fancylists.seven;

import android.database.DataSetObserver;
import android.widget.ListAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

public class AdapterWrapper implements ListAdapter {
	ListAdapter delegate=null;
	
	public AdapterWrapper(ListAdapter delegate) {
		this.delegate=delegate;
	}
	
	public int getCount() {
		return(delegate.getCount());
	}
	
	public Object getItem(int position) {
		return(delegate.getItem(position));
	}
	
	public long getItemId(int position) {
		return(delegate.getItemId(position));
	}
	
	public View getView(int position, View convertView,
											ViewGroup parent) {
		return(delegate.getView(position, convertView, parent));
	}
	
	public void registerDataSetObserver(DataSetObserver observer) {
		delegate.registerDataSetObserver(observer);
	}
	
	public boolean hasStableIds() {
		return(delegate.hasStableIds());
	}
	
	public boolean isEmpty() {
		return(delegate.isEmpty());
	}
	
	public int getViewTypeCount() {
		return(delegate.getViewTypeCount());
	}
	
	public int getItemViewType(int position) {
		return(delegate.getItemViewType(position));
	}
	
	public void unregisterDataSetObserver(DataSetObserver observer) {
		delegate.unregisterDataSetObserver(observer);
	}
	
	public boolean areAllItemsEnabled() {
		return(delegate.areAllItemsEnabled());
	}
	
	public boolean isEnabled(int position) {
		return(delegate.isEnabled(position));
	}
}
