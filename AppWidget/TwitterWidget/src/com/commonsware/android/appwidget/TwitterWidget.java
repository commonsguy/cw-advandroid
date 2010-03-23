/* Copyright (c) 2008-10 -- CommonsWare, LLC

	 Licensed under the Apache License, Version 2.0 (the "License");
	 you may not use this file except in compliance with the License.
	 You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

	 Unless required by applicable law or agreed to in writing, software
	 distributed under the License is distributed on an "AS IS" BASIS,
	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 See the License for the specific language governing permissions and
	 limitations under the License.
*/
	 
package com.commonsware.android.appwidget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import java.util.List;
import winterwell.jtwitter.Twitter;

public class TwitterWidget extends AppWidgetProvider {
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		if (intent.getAction()==null) {
			ctxt.startService(new Intent(ctxt, UpdateService.class));
		}
		else {
			super.onReceive(ctxt, intent);
		}
	}
	
	@Override
	public void onUpdate(Context ctxt,
												AppWidgetManager mgr,
												int[] appWidgetIds) {
		ctxt.startService(new Intent(ctxt, UpdateService.class));
	}

	public static class UpdateService extends IntentService {
		private SharedPreferences prefs=null;
		
		public UpdateService() {
			super("TwitterWidget$UpdateService");
		}
		
		@Override
		public void onCreate() {
			super.onCreate();
			
			prefs=PreferenceManager.getDefaultSharedPreferences(this);
		}
		
		@Override
		public void onHandleIntent(Intent intent) {
			ComponentName me=new ComponentName(this,
																				 TwitterWidget.class);
			AppWidgetManager mgr=AppWidgetManager.getInstance(this);
			
			mgr.updateAppWidget(me, buildUpdate(this));
		}

		private RemoteViews buildUpdate(Context context) {
			RemoteViews updateViews=new RemoteViews(context.getPackageName(),
																							R.layout.widget);
			String user=prefs.getString("user", null);
			String password=prefs.getString("password", null);
			
			if (user!=null && password!=null) {
				Twitter client=new Twitter(user, password);
				List<Twitter.Status> timeline=client.getFriendsTimeline();
				
				if (timeline.size()>0) {
					Twitter.Status s=timeline.get(0);
					
					updateViews.setTextViewText(R.id.friend,
																			s.user.screenName);
					updateViews.setTextViewText(R.id.status,
																			s.text);

					Intent i=new Intent(this, TwitterWidget.class);
					PendingIntent pi=PendingIntent.getBroadcast(context,
																											0	, i,
																											0);
						
					updateViews.setOnClickPendingIntent(R.id.refresh,
																							pi);

					i=new Intent(this, TWPrefs.class);
					pi=PendingIntent.getActivity(context, 0	, i, 0);
					updateViews.setOnClickPendingIntent(R.id.configure,
																							pi);
				}
			}
			
			return(updateViews);
		}
	}
}