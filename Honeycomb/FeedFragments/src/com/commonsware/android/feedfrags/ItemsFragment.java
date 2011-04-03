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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;

public class ItemsFragment extends ListFragment {
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		if (state!=null) {
			int position=state.getInt(FeedsFragment.STATE_CHECKED, -1);
			
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
		state.putInt(FeedsFragment.STATE_CHECKED,
									getListView().getCheckedItemPosition());
	}
	
	public void enablePersistentSelection() {
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	private void setFeed(RSSFeed feed) {
		setListAdapter(new FeedAdapter(feed));
	}
	
	private /* static */ class FeedTask extends AsyncTask<String, Void, RSSFeed> {
		private RSSReader reader=new RSSReader();
		private Exception e=null;
		
		@Override
		public RSSFeed doInBackground(String... urls) {
			RSSFeed result=null;
			
			try {
				result=reader.load(urls[0]);
			}
			catch (Exception e) {
				this.e=e;
			}
			
			return(result);
		}
		
		@Override
		public void onPostExecute(RSSFeed feed) {
			if (e==null) {
				setFeed(feed);
			}
			else {
				Log.e("LunchList", "Exception parsing feed", e);
				// activity.goBlooey(e);
			}
		}
	}
	
	private class FeedAdapter extends BaseAdapter {
		RSSFeed feed=null;
		
		FeedAdapter(RSSFeed feed) {
			super();
			
			this.feed=feed;
		}
		
		@Override
		public int getCount() {
			return(feed.getItems().size());
		}
		
		@Override
		public Object getItem(int position) {
			return(feed.getItems().get(position));
		}
		
		@Override
		public long getItemId(int position) {
			return(position);
		}
		
		@Override
		public View getView(int position, View convertView,
												ViewGroup parent) {
			View row=convertView;
			
			if (row==null) {													
				LayoutInflater inflater=getActivity().getLayoutInflater();
				
				row=inflater.inflate(android.R.layout.simple_list_item_1,
														 parent, false);
			}
			
			RSSItem item=(RSSItem)getItem(position);
			
			((TextView)row).setText(item.getTitle());
			
			return(row);
		}
	}
}