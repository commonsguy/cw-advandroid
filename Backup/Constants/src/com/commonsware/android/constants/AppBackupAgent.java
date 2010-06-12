/* Copyright (c) 2008-2010 -- CommonsWare, LLC

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
	 
package com.commonsware.android.constants;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.os.ParcelFileDescriptor;
import java.io.IOException;

public class AppBackupAgent extends BackupAgentHelper {
  static final String DB_BACKUP_KEY="db";

	@Override
  public void onCreate() {
		addHelper(DB_BACKUP_KEY,
								new FileBackupHelper(this,
																		 DatabaseHelper.getBackupPath(this)));
  }
	
	@Override
	public void onBackup(ParcelFileDescriptor oldState,
												BackupDataOutput data,
												ParcelFileDescriptor newState)
		throws IOException {
    synchronized(ConstantsBrowser.BACKUP_LOCK) {
android.util.Log.w("AppBackupAgent", "onBackup");				
      super.onBackup(oldState, data, newState);
    }
}

	@Override
	public void onRestore(BackupDataInput data,
												int appVersionCode,
												ParcelFileDescriptor newState)
		throws IOException {
    synchronized(ConstantsBrowser.BACKUP_LOCK) {
android.util.Log.w("AppBackupAgent", "onRestore");				
      super.onRestore(data, appVersionCode, newState);
    }
	}
}