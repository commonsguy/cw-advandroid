/***
	Copyright (c) 2011 CommonsWare, LLC
	
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

package com.commonsware.android.feedfrags;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class FeedsFragment extends ListFragment {
	static private ArrayList<Feed> FEEDS=new ArrayList<Feed>();
	static public final String STATE_CHECKED="com.commonsware.android.feedfrags.STATE_CHECKED";

	static {
		FEEDS.add(new Feed("Slashdot", "http://rss.slashdot.org/Slashdot/slashdot"));
		FEEDS.add(new Feed("Slashdot", "http://rss.slashdot.org/Slashdot/slashdot"));
		FEEDS.add(new Feed("Slashdot", "http://rss.slashdot.org/Slashdot/slashdot"));
		FEEDS.add(new Feed("Slashdot", "http://rss.slashdot.org/Slashdot/slashdot"));
		FEEDS.add(new Feed("Slashdot", "http://rss.slashdot.org/Slashdot/slashdot"));
	}
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		setListAdapter(new ArrayAdapter<Feed>(getActivity(),
																					android.R.layout.simple_list_item_1,
																					FEEDS));
		
		if (state!=null) {
			int position=state.getInt(STATE_CHECKED, -1);
			
			if (position>-1) {
				getListView().setItemChecked(position, true);
			}
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position,
															long id) {
		l.setItemChecked(position, true);
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		state.putInt(STATE_CHECKED,
									getListView().getCheckedItemPosition());
	}
	
	public void enablePersistentSelection() {
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	private static class Feed {
		String name;
		String url;
		
		Feed(String name, String url) {
			this.name=name;
			this.url=url;
		}
		
		public String toString() {
			return(name);
		}
	}
}