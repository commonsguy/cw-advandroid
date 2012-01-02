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

package com.commonsware.android.listview;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SelectorDemo extends ListActivity {
  private static ColorStateList allWhite=ColorStateList.valueOf(0xFFFFFFFF);
  private static String[] items={"lorem", "ipsum", "dolor",
                                  "sit", "amet", "consectetuer",
                                  "adipiscing", "elit", "morbi",
                                  "vel", "ligula", "vitae",
                                  "arcu", "aliquet", "mollis",
                                  "etiam", "vel", "erat",
                                  "placerat", "ante",
                                  "porttitor", "sodales",
                                  "pellentesque", "augue",
                                  "purus"};
  
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);
    setListAdapter(new SelectorAdapter(this));
    getListView().setOnItemSelectedListener(listener);
  }
  
  class SelectorAdapter extends ArrayAdapter {
    SelectorAdapter(Context ctxt) {
      super(ctxt, R.layout.row, items);
    }
    
    @Override
    public View getView(int position, View convertView,
                          ViewGroup parent) {
      SelectorWrapper wrapper=null;
      
      if (convertView==null) {
        convertView=getLayoutInflater().inflate(R.layout.row,
                                                parent, false);
        wrapper=new SelectorWrapper(convertView);
        wrapper.getLabel().setTextColor(allWhite);
        convertView.setTag(wrapper);
      }
      else {
        wrapper=(SelectorWrapper)convertView.getTag();
      }
      
      wrapper.getLabel().setText(items[position]);
      
      return(convertView);
    }
  }
  
  class SelectorWrapper {
    View row=null;
    TextView label=null;
    View bar=null;
    
    SelectorWrapper(View row) {
      this.row=row;
    }
    
    TextView getLabel() {
      if (label==null) {
        label=(TextView)row.findViewById(R.id.label);
      }
      
      return(label);
    }
    
    View getBar() {
      if (bar==null) {
        bar=row.findViewById(R.id.bar);
      }
      
      return(bar);
    }
  }
  
  AdapterView.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
    View lastRow=null;
    
    public void onItemSelected(AdapterView<?> parent,
                               View view, int position,
                               long id) {
      if (lastRow!=null) {
        SelectorWrapper wrapper=(SelectorWrapper)lastRow.getTag();

        wrapper.getBar().setVisibility(View.INVISIBLE);
      }
      
      SelectorWrapper wrapper=(SelectorWrapper)view.getTag();
      
      wrapper.getBar().setVisibility(View.VISIBLE);
      lastRow=view;
    }
    
    public void onNothingSelected(AdapterView<?> parent) {
      if (lastRow!=null) {
        SelectorWrapper wrapper=(SelectorWrapper)lastRow.getTag();

        wrapper.getBar().setVisibility(View.INVISIBLE);
        lastRow=null;
      }
    }
  };
}