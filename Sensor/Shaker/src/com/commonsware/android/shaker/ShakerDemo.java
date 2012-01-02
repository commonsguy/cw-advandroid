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

package com.commonsware.android.shaker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShakerDemo extends Activity
  implements Shaker.Callback {
  private Shaker shaker=null;
  private TextView transcript=null;
  private ScrollView scroll=null;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    transcript=(TextView)findViewById(R.id.transcript);
    scroll=(ScrollView)findViewById(R.id.scroll);
    
    shaker=new Shaker(this, 1.25d, 500, this);
  }
  
  @Override
  public void onDestroy() {
    super.onDestroy();
    
    shaker.close();
  }
  
  public void shakingStarted() {
    Log.d("ShakerDemo", "Shaking started!");
    transcript.setText(transcript.getText().toString()+"Shaking started\n");
    scroll.fullScroll(View.FOCUS_DOWN);
  }
  
  public void shakingStopped() {
    Log.d("ShakerDemo", "Shaking stopped!");
    transcript.setText(transcript.getText().toString()+"Shaking stopped\n");
    scroll.fullScroll(View.FOCUS_DOWN);
  }
}