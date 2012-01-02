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
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class PersistentListFragment extends ListFragment {
  static public final String STATE_CHECKED="com.commonsware.android.feedfrags.STATE_CHECKED";

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
  
  protected void restoreState(Bundle state) {
    if (state!=null) {
      int position=state.getInt(STATE_CHECKED, -1);
      
      if (position>-1) {
        getListView().setItemChecked(position, true);
      }
    }
  }
  
  public void enablePersistentSelection() {
    getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
  }
}