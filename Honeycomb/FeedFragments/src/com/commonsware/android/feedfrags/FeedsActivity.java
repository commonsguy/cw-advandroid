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

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FeedsActivity extends AbstractFeedsActivity
    implements FeedsFragment.OnFeedListener {
  private boolean isThreePane=false;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    FeedsFragment feeds
      =(FeedsFragment)getSupportFragmentManager()
                            .findFragmentById(R.id.feeds);
                            
    feeds.setOnFeedListener(this);
    
    isThreePane=(null!=findViewById(R.id.second_pane));
    
    if (isThreePane) {
      feeds.enablePersistentSelection();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    
    FragmentManager fragMgr=getSupportFragmentManager();
    ItemsFragment items=(ItemsFragment)fragMgr.findFragmentById(R.id.second_pane);
    
    if (items!=null) {
      items.setOnItemListener(this);
    }
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
      new MenuInflater(this).inflate(R.menu.feeds_hc_options, menu);
    }

    return(super.onCreateOptionsMenu(menu));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.nav:
        startActivity(new Intent(this, FeedsNavActivity.class));
    
        return(true);

      case R.id.tabs:
        startActivity(new Intent(this, FeedsTabActivity.class));
    
        return(true);
    }
    
    return(super.onOptionsItemSelected(item));
  }
  
  public void addItemsFragment(Feed feed) {
    FragmentManager fragMgr=getSupportFragmentManager();
    ItemsFragment items=(ItemsFragment)fragMgr.findFragmentById(R.id.second_pane);
    FragmentTransaction xaction=fragMgr.beginTransaction();
    
    if (items==null) {
      items=new ItemsFragment(true);
      items.setOnItemListener(this);
      
      xaction
        .add(R.id.second_pane, items)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .addToBackStack(null)
        .commit();
    }
    else {
      ContentFragment content=
        (ContentFragment)fragMgr.findFragmentById(R.id.third_pane);
      
      if (content!=null) {
        xaction.remove(content).commit();
        fragMgr.popBackStack();
      }
    }
      
    items.loadUrl(feed.getUrl());
  }
  
  public void onFeedSelected(Feed feed) {
    if (isThreePane) {
      addItemsFragment(feed);
    }
    else {
      Intent i=new Intent(this, ItemsActivity.class);
      
      i.putExtra(ItemsActivity.EXTRA_FEED_KEY, feed.getKey());
      
      startActivity(i);
    }
  }
  
  public void addNewFeed(Feed feed) {
    FeedsFragment feeds
      =(FeedsFragment)getSupportFragmentManager()
                            .findFragmentById(R.id.feeds);
                            
    feeds.addNewFeed(feed);
  }
}