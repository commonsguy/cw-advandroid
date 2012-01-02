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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

public class AddFeedDialogFragment extends DialogFragment {
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final View form=getActivity()
                      .getLayoutInflater()
                      .inflate(R.layout.add_feed, null);
  
    return(new AlertDialog.Builder(getActivity())
            .setTitle(R.string.add_feed)
            .setView(form)
            .setPositiveButton(R.string.add,
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                  processAdd(form);
                }
              }
            )
            .setNegativeButton(R.string.cancel, null)
            .create());
  }
  
  private void processAdd(View form) {
    EditText name=(EditText)form.findViewById(R.id.name);
    EditText url=(EditText)form.findViewById(R.id.url);
    Feed feed=Feed.addFeed(name.getText().toString(),
                           url.getText().toString());
    
    ((AbstractFeedsActivity)getActivity()).addNewFeed(feed);
  }
}