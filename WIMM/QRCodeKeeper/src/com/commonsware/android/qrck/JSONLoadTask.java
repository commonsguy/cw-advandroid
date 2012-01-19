/***
  Copyright (c) 2012 CommonsWare, LLC
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

package com.commonsware.android.qrck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONLoadTask extends AsyncTask<String, Void, JSONObject> {
  private Context ctxt=null;
  private Listener listener=null;
  private Exception ex=null;

  static JSONObject load(Context ctxt, String fn) throws JSONException,
                                                 IOException {
    FileInputStream is=ctxt.openFileInput(fn);
    InputStreamReader reader=new InputStreamReader(is);
    BufferedReader in=new BufferedReader(reader);
    StringBuilder buf=new StringBuilder();
    String str;

    while ((str=in.readLine()) != null) {
      buf.append(str);
    }

    in.close();

    return(new JSONObject(buf.toString()));
  }

  public JSONLoadTask(Context ctxt, Listener listener) {
    this.ctxt=ctxt;
    this.listener=listener;
  }

  /**
   * Runs on a worker thread, loading in our data.
   */
  @Override
  public JSONObject doInBackground(String... path) {
    JSONObject json=null;

    try {
      String fn=path[0];

      if (new File(ctxt.getFilesDir(), fn).exists()) {
        json=load(ctxt, path[0]);
      }
    }
    catch (Exception ex) {
      this.ex=ex;
    }

    return(json);
  }

  @Override
  protected void onPostExecute(JSONObject json) {
    if (listener != null) {
      if (json != null) {
        listener.handleResult(json);
      }

      if (ex != null) {
        listener.handleError(ex);
        SyncService.cleanup(ctxt);
      }
    }
  }

  public interface Listener {
    void handleResult(JSONObject json);

    void handleError(Exception ex);
  }
}