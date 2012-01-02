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

package com.commonsware.android.ordered;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NoticeReceiver extends BroadcastReceiver {
  private static final int NOTIFY_ME_ID=1337;

  @Override
  public void onReceive(Context context, Intent intent) {
    NotificationManager mgr=
      (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification note=new Notification(R.drawable.stat_notify_chat,
                                        "Yoo-hoo! Wake up!",
                                        System.currentTimeMillis());
    PendingIntent i=PendingIntent.getActivity(context, 0,
                            new Intent(context, OrderedActivity.class),
                                              0);
    
    note.setLatestEventInfo(context, "You Care About This!",
                            "...but not enough to keep the activity running",
                            i);
    
    mgr.notify(NOTIFY_ME_ID, note);
  }
}