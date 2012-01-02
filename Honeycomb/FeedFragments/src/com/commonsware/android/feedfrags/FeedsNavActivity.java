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

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class FeedsNavActivity extends AbstractFeedsActivity {
  ArrayAdapter<Feed> adapter=null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_nav);

    ActionBar bar=getActionBar();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      adapter=
          new ArrayAdapter<Feed>(bar.getThemedContext(), R.layout.row,
                                 Feed.getFeeds());
    }
    else {
      adapter=
          new ArrayAdapter<Feed>(this, R.layout.row, Feed.getFeeds());
    }

    bar.setListNavigationCallbacks(adapter, new NavListener());
    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE,
                          ActionBar.DISPLAY_SHOW_TITLE);
    bar.setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    new MenuInflater(this).inflate(R.menu.feeds_nav_options, menu);

    return(super.onCreateOptionsMenu(menu));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();

        return(true);
    }

    return(super.onOptionsItemSelected(item));
  }

  private void removeFragments(FragmentManager fragMgr,
                               FragmentTransaction xaction) {
    ItemsFragment items=
        (ItemsFragment)fragMgr.findFragmentById(R.id.second_pane);

    if (items != null) {
      xaction.remove(items);

      ContentFragment content=
          (ContentFragment)fragMgr.findFragmentById(R.id.third_pane);

      if (content != null && !content.isRemoving()) {
        xaction.remove(content);
        fragMgr.popBackStack();
      }
    }
  }

  public void addNewFeed(Feed feed) {
    adapter.add(feed);
  }

  private class NavListener implements ActionBar.OnNavigationListener {
    public boolean onNavigationItemSelected(int itemPosition,
                                            long itemId) {
      FragmentManager fragMgr=getSupportFragmentManager();
      FragmentTransaction xaction=fragMgr.beginTransaction();

      addItems(xaction, Feed.getFeeds().get(itemPosition));
      removeFragments(fragMgr, xaction);
      xaction.commit();

      return(true);
    }
  }
}