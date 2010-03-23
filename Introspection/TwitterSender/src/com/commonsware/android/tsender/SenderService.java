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
	 
package com.commonsware.android.tsender;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import winterwell.jtwitter.Twitter;

public class SenderService extends IntentService {
	public SenderService() {
		super("SenderService");
	}
		
	@Override
	public void onHandleIntent(Intent intent) {
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		Twitter client=new Twitter(prefs.getString("user", ""),
																prefs.getString("password", ""));

		client.updateStatus(intent.getStringExtra(Intent.EXTRA_TEXT));
	}
}
