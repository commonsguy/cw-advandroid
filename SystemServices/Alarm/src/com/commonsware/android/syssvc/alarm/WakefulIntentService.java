/***
	Copyright (c) 2009 CommonsWare, LLC
	
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

package com.commonsware.android.syssvc.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class WakefulIntentService extends IntentService {
	public static final String LOCK_NAME_STATIC="com.commonsware.android.syssvc.AppService.Static";
	public static final String LOCK_NAME_LOCAL="com.commonsware.android.syssvc.AppService.Local";
	private static PowerManager.WakeLock lockStatic=null;
	private PowerManager.WakeLock lockLocal=null;
	
	public static void acquireStaticLock(Context context) {
		getLock(context).acquire();
	}
	
	synchronized private static PowerManager.WakeLock getLock(Context context) {
		if (lockStatic==null) {
			PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
			
			lockStatic=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
														LOCK_NAME_STATIC);
			lockStatic.setReferenceCounted(true);
		}
		
		return(lockStatic);
	}
	
	public WakefulIntentService(String name) {
		super(name);
	}
	
	public void onCreate() {
		super.onCreate();
		
		PowerManager mgr=(PowerManager)getSystemService(Context.POWER_SERVICE);
		
		lockLocal=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
															LOCK_NAME_LOCAL);
		lockLocal.setReferenceCounted(true);
	}
	
	@Override
	public void onStart(Intent intent, final int startId) {
		lockLocal.acquire();
		
		super.onStart(intent, startId);
		
		getLock(this).release();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		lockLocal.release();
	}
}
