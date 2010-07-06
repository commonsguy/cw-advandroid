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

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushDemoReceiver extends PushEndpoint {
	public void onRegister(Context ctxt, Intent intent,
													String registrationId) {
		Log.w("PushDemoReceiver-onRegister", registrationId);
	}
	
	public void onUnregister(Context ctxt, Intent intent) {
		Log.w("PushDemoReceiver-onUnregister", "got here!");
	}
	
	public void onRegisterError(Context ctxt, Intent intent,
															String error) {
		Log.w("PushDemoReceiver-onRegisterError", error);
	}
	
	public void onMessage(Context ctxt, Intent intent) {
		Log.w("PushDemoReceiver", intent.getStringExtra("payload"));
	}
}
