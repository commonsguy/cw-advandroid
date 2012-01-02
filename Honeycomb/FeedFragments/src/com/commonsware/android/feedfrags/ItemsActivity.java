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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ItemsActivity extends FragmentActivity
    implements ItemsFragment.OnItemListener {
  public static final String EXTRA_FEED_KEY=
    "com.commonsware.android.feedfrags.EXTRA_FEED_KEY";
  ItemsFragment items=null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.items);
    
    items=(ItemsFragment)getSupportFragmentManager()
                          .findFragmentById(R.id.items);
    
    String key=getIntent().getStringExtra(EXTRA_FEED_KEY);
    
    if (key!=null) {
      Feed feed=Feed.getFeed(key);
      
      setTitle(String.format(getString(R.string.feed_title),
                             feed.toString()));
      items.loadUrl(feed.getUrl());
    }
  }
  
  @Override
  public void onResume() {
    super.onResume();
    
    items.setOnItemListener(this);
  }
  
  public void onItemSelected(RSSItem item) {
    startActivity(new Intent(Intent.ACTION_VIEW, item.getLink()));
  }
}