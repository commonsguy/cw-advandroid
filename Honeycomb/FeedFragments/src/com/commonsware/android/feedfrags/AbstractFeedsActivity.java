/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
*/

package com.commonsware.android.feedfrags;

import org.mcsoxford.rss.RSSItem;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

abstract public class AbstractFeedsActivity extends FragmentActivity
    implements ItemsFragment.OnItemListener {
  abstract void addNewFeed(Feed feed);
      
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    new MenuInflater(this).inflate(R.menu.feeds_options, menu);

    return(super.onCreateOptionsMenu(menu));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.add:
        new AddFeedDialogFragment()
          .show(getSupportFragmentManager(), "add_feed");
    
        return(true);
        
      case R.id.lights_out:
        lightsOut();
        
        return(true);
    }
    
    return(super.onOptionsItemSelected(item));
  }
  
  public void onItemSelected(RSSItem item) {
    FragmentManager fragMgr=getSupportFragmentManager();
    ContentFragment content=
      (ContentFragment)fragMgr.findFragmentById(R.id.third_pane);
    FragmentTransaction xaction=fragMgr.beginTransaction();
    
    if (content==null || content.isRemoving()) {
      content=new ContentFragment(item.getLink().toString());
      
      xaction
        .add(R.id.third_pane, content)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .addToBackStack(null)
        .commit();
    }
    else {
      content.loadUrl(item.getLink().toString());
    }
  }
  
  private void lightsOut() {
    final View view=findViewById(android.R.id.content);
    
    view.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
    getActionBar().hide();
    
    view.postDelayed(new Runnable() {
      public void run() {
        view.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
        getActionBar().show();
      }
    }, 5000);
  }

  protected void addItems(FragmentTransaction xaction, Feed feed) {
    ItemsFragment items=new ItemsFragment(true);
    
    items.setOnItemListener(this);
    items.loadUrl(feed.getUrl());
    
    xaction.add(R.id.second_pane, items, "items");
  }
}