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

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FeedsFragment extends PersistentListFragment {
  private OnFeedListener listener=null;
  private ArrayAdapter<Feed> adapter=null;
  private Bundle state=null;
  
  @Override
  public void onActivityCreated(Bundle state) {
    super.onActivityCreated(state);
    
    this.state=state;
  }
  
  @Override
  public void onResume() {
    super.onResume();
    
    loadFeeds();
    restoreState(state);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position,
                              long id) {
    super.onListItemClick(l, v, position, id);
    
    if (listener!=null) {
      listener.onFeedSelected(adapter.getItem(position));
    }
  }
  
  public void addNewFeed(Feed feed) {
    adapter.add(feed);
  }

  private void loadFeeds() {
    adapter=new ArrayAdapter<Feed>(getActivity(), R.layout.row,
                                    Feed.getFeeds());
    setListAdapter(adapter);
  }
  
  public void setOnFeedListener(OnFeedListener listener) {
    this.listener=listener;
  }
  
  public interface OnFeedListener {
    void onFeedSelected(Feed feed);
  }
}