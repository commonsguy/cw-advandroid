/***
	Copyright (c) 2010 CommonsWare, LLC
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package com.commonsware.android.c2dm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

abstract public class PushEndpoint extends BroadcastReceiver {
	abstract public void onRegister(Context ctxt,
																	Intent intent,
																	String registrationId);
	abstract public void onUnregister(Context ctxt,
																		Intent intent);
	abstract public void onRegisterError(Context ctxt,
																			 Intent intent,
																			 String error);
	abstract public void onMessage(Context ctxt,
																	Intent intent);
	
	public static void register(Context ctxt, String account) {
		Intent i=new Intent("com.google.android.c2dm.intent.REGISTER");

		i.putExtra("app",
								PendingIntent.getBroadcast(ctxt, 0, new Intent(), 0));
		i.putExtra("sender", account);

		ctxt.startService(i);
	}
	
	public static void unregister(Context ctxt) {
		Intent i=new Intent("com.google.android.c2dm.intent.UNREGISTER");

		i.putExtra("app",
								PendingIntent.getBroadcast(ctxt, 0, new Intent(), 0));

		ctxt.startService(i);
	}
	
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
			String error=intent.getStringExtra("error");
			
			if (error!=null) {
				onRegisterError(ctxt, intent, error);
			}
			else if (intent.getStringExtra("unregistered")!=null) {
				onUnregister(ctxt, intent);
			}
			else {
				onRegister(ctxt, intent,
									 intent.getStringExtra("registration_id"));
			}
    }
		else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
      onMessage(ctxt, intent);
    }
	}
}