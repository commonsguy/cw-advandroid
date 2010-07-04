/***
	Copyright (c) 2009-10 CommonsWare, LLC
	
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

package com.commonsware.android.advservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import bsh.Interpreter;

public class BshService extends IntentService {
	private static final String SCRIPT="com.commonsware.SCRIPT";
	private static final String BROADCAST_ACTION="com.commonsware.BROADCAST_ACTION";
	private static final String RECEIVER="com.commonsware.RECEIVER";
	private static final String PAYLOAD="com.commonsware.PAYLOAD";
	private static final String RESULT_CODE="com.commonsware.RESULT_CODE";
	private static final int SUCCESS=1337;
	private Interpreter i=new Interpreter();
	
	public BshService() {
		super("BshService");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		try {
			i.set("context", this);
		}
		catch (bsh.EvalError e) {
			Log.e("BshService", "Error executing script", e);
		}
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		String script=intent.getStringExtra(SCRIPT);
		
		if (script!=null) {
			try {
				success(intent, i.eval(script).toString());
			}
			catch (Throwable e) {
				Log.e("BshService", "Error executing script", e);
				
				try {
					failure(intent, e.getMessage());
				}
				catch (Throwable t) {
					Log.e("BshService",
								"Error returning exception to client",
								t);
				}
			}
		}
	}
	
	private void success(Intent intent, String result) {
		String broadcast=intent.getStringExtra(BROADCAST_ACTION);
		
		if (broadcast==null) {
			ResultReceiver receiver=(ResultReceiver)intent.getParcelableExtra(RECEIVER);
			
			if (receiver!=null) {
				Bundle b=new Bundle();
				
				b.putString(PAYLOAD, result);
				
				receiver.send(SUCCESS, b);
			}
		}
		else {
			Intent bcast=new Intent(broadcast);
			
			bcast.setPackage("com.commonsware.android.advservice.client");
			bcast.putExtra(PAYLOAD, result);
			bcast.putExtra(RESULT_CODE, SUCCESS);
			
			sendBroadcast(bcast);
		}
	}
	
	private void failure(Intent intent, String error) {
		String broadcast=intent.getStringExtra(BROADCAST_ACTION);
		
		if (broadcast==null) {
			ResultReceiver receiver=(ResultReceiver)intent.getParcelableExtra(RECEIVER);
			
			if (receiver!=null) {
				Bundle b=new Bundle();
				
				b.putString(PAYLOAD, error);
				
				receiver.send(-1, b);
			}
		}
		else {
			Intent bcast=new Intent(broadcast);
			
			bcast.setPackage("com.commonsware.android.advservice.client");
			bcast.putExtra(PAYLOAD, error);
			bcast.putExtra(RESULT_CODE, -1);
			
			sendBroadcast(bcast);
		}
	}
}
