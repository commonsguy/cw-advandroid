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

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ItemsFragment extends PersistentListFragment {
  private OnItemListener listener=null;
  private FeedAdapter adapter=null;
  private boolean persistentSelection=false;
  
  public ItemsFragment() {
    this(false);
  }
  
  public ItemsFragment(boolean persistentSelection) {
    this.persistentSelection=persistentSelection;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setRetainInstance(true);
  }

  @Override
  public void onActivityCreated(Bundle state) {
    super.onActivityCreated(state);

    registerForContextMenu(getListView());
    restoreState(state);
    
    if (persistentSelection) {
      enablePersistentSelection();
    }
  }

  @Override
  public void onListItemClick(ListView l, View v, int position,
                              long id) {
    super.onListItemClick(l, v, position, id);
    
    if (listener!=null) {
      listener.onItemSelected(adapter.getItem(position));
    }
  }
  
  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
    new MenuInflater(getActivity())
          .inflate(R.menu.items_context, menu);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterView.AdapterContextMenuInfo info=
      (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

    switch (item.getItemId()) {
      case R.id.share:
        Intent send=new Intent(Intent.ACTION_SEND);
    
        send.putExtra(Intent.EXTRA_SUBJECT, R.string.share_subject);
        send.putExtra(Intent.EXTRA_TEXT,
                      adapter.getItem(info.position).getLink().toString());
        send.setType("text/plain");
        
        startActivity(Intent.createChooser(send,
                                           getText(R.string.share_title)));
        
        return(true);
    }
    
    return(super.onContextItemSelected(item));
  }
  
  public void loadUrl(String url) {
    new FeedTask().execute(url);
  }
  
  public void setOnItemListener(OnItemListener listener) {
    this.listener=listener;
  }
  
  private void setFeed(RSSFeed feed) {
    adapter=new FeedAdapter(feed);
    setListAdapter(adapter);
  }
    
  public interface OnItemListener {
    void onItemSelected(RSSItem item);
  }
  
  private void goBlooey(Throwable t) {
    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
    
    builder
      .setTitle(R.string.exception)
      .setMessage(t.toString())
      .setPositiveButton(R.string.close, null)
      .show();
  }

  private class FeedTask extends AsyncTask<String, Void, RSSFeed> {
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
        Log.e("ItemsFragment", "Exception parsing feed", e);
        goBlooey(e);
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
    public RSSItem getItem(int position) {
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
        
        row=inflater.inflate(R.layout.row, parent, false);
      }
      
      RSSItem item=(RSSItem)getItem(position);
      
      ((TextView)row).setText(item.getTitle());
      
      return(row);
    }
  }
}