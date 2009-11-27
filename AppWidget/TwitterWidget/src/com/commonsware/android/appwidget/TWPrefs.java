/* Copyright (c) 2008-09 -- CommonsWare, LLC

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

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;
import android.widget.RemoteViews;

public class TWPrefs extends PreferenceActivity {
	private static String CONFIGURE_ACTION="android.appwidget.action.APPWIDGET_CONFIGURE";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK &&
				Integer.parseInt(Build.VERSION.SDK)<5) {
			onBackPressed();
		}
		
		return(super.onKeyDown(keyCode, event));
	}
	
	@Override
	public void onBackPressed() {
		if (CONFIGURE_ACTION.equals(getIntent().getAction())) {
			Intent intent=getIntent();
			Bundle extras=intent.getExtras();

			if (extras!=null) {
				int id=extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
															AppWidgetManager.INVALID_APPWIDGET_ID);
				AppWidgetManager mgr=AppWidgetManager.getInstance(this);
				RemoteViews views=new RemoteViews(getPackageName(),
																					R.layout.widget);

				mgr.updateAppWidget(id, views);

				Intent result=new Intent();

				result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
													id);
				setResult(RESULT_OK, result);
				sendBroadcast(new Intent(this,
																	TwitterWidget.class));
			}
		}
		
		super.onBackPressed();
	} 
}
