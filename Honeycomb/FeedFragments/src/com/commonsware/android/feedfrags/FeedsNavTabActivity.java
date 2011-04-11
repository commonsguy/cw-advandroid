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

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import org.mcsoxford.rss.RSSItem;

public class FeedsNavTabActivity extends AbstractFeedsActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_nav);
		
		showTabs();

		ActionBar bar=getActionBar();
		
		for (final Feed feed : Feed.getFeeds()) {
			bar.addTab(bar
									.newTab()
									.setText(feed.toString())
									.setTabListener(new TabListener(feed)));
		}
		
		bar.setListNavigationCallbacks(new ArrayAdapter<Feed>(this,
																													R.layout.row,
																													Feed.getFeeds()),
																	 new NavListener());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.feeds_nav_options, menu);

		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		ActionBar bar=getActionBar();
		
		if (bar.getNavigationMode()!=ActionBar.NAVIGATION_MODE_TABS) {
			menu.findItem(R.id.tabs).setVisible(true);
			menu.findItem(R.id.list).setVisible(false);
		}
		else {
			menu.findItem(R.id.tabs).setVisible(false);
			menu.findItem(R.id.list).setVisible(true);
		}
		
		return(super.onPrepareOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.nav:
				startActivity(new Intent(this, FeedsActivity.class));
				finish();
		
				return(true);
				
			case R.id.tabs:
				showTabs();
		
				return(true);
				
			case R.id.list:
				showList();
		
				return(true);
				
			case R.id.lights_out:
				lightsOut();
				
				return(true);
		}
		
		return(super.onOptionsItemSelected(item));
	}
	
	private void lightsOut() {
		final View view=findViewById(R.id.second_pane);
		
		view.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		getActionBar().hide();
		
		view.postDelayed(new Runnable() {
			public void run() {
				view.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
				getActionBar().show();
			}
		}, 5000);
	}
	
	private void showTabs() {
		ActionBar bar=getActionBar();
		
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	  bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
	}
	
	private void showList() {
		ActionBar bar=getActionBar();
		
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE,
													ActionBar.DISPLAY_SHOW_TITLE);
	}
	
	private void addItems(FragmentTransaction xaction, Feed feed) {
		ItemsFragment items=new ItemsFragment();
		
		items.setOnItemListener(FeedsNavTabActivity.this);
		items.loadUrl(feed.getUrl());
		
		xaction.add(R.id.second_pane, items, "items");
	}
	
	private void removeFragments(FragmentManager fragMgr,
															 FragmentTransaction xaction) {
		ItemsFragment items=(ItemsFragment)fragMgr.findFragmentById(R.id.second_pane);
			
		if (items!=null) {
			if (!items.isRemoving()) {
				try {
					xaction.remove(items);
				}
				catch (IllegalStateException e) {
					// TODO: figure out how to get rid of me
				}
			}
			
			ContentFragment content=
				(ContentFragment)fragMgr.findFragmentById(R.id.third_pane);
				
			if (content!=null && !content.isRemoving()) {
				try {
					xaction.remove(content);
				}
				catch (IllegalStateException e) {
					// TODO: figure out how to get rid of me
				}
			}
		}
	}
	
	private class NavListener implements ActionBar.OnNavigationListener {
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			FragmentManager fragMgr=getSupportFragmentManager();
			FragmentTransaction xaction=fragMgr.beginTransaction();
			
			addItems(xaction, Feed.getFeeds().get(itemPosition));
			removeFragments(fragMgr, xaction);
			xaction.commit();
			
			return(true);
		}
	}
	
	private class TabListener implements ActionBar.TabListener {
		Feed feed=null;
		
		TabListener(Feed feed) {
			this.feed=feed;
		}
		
		public void onTabSelected(ActionBar.Tab tab,
															android.app.FragmentTransaction unused) {
			FragmentManager fragMgr=getSupportFragmentManager();
			FragmentTransaction xaction=fragMgr.beginTransaction();
			
			addItems(xaction, feed);
			xaction.commit();
		}

		public void onTabUnselected(ActionBar.Tab tab,
																android.app.FragmentTransaction unused) {
			FragmentManager fragMgr=getSupportFragmentManager();
			FragmentTransaction xaction=fragMgr.beginTransaction();
			
			removeFragments(fragMgr, xaction);
			xaction.commit();
		}

		public void onTabReselected(ActionBar.Tab tab,
																android.app.FragmentTransaction xaction) {
			// NO-OP
		}
	}
}
