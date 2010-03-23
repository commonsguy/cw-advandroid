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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class TwitterSender extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String tweet=getIntent().getStringExtra(Intent.EXTRA_TEXT);
		
		if (TextUtils.isEmpty(tweet)) {
			tweet=getIntent().getStringExtra(Intent.EXTRA_SUBJECT);
		}
		
		if (TextUtils.isEmpty(tweet)) {
			Toast
				.makeText(this, "No message supplied!", Toast.LENGTH_LONG)
				.show();
		}
		else {
			if (tweet.length()>140) {
				tweet=TextUtils.substring(tweet, 0, 139);
			}
			
			Intent i=new Intent(this, SenderService.class);
			
			i.putExtra(Intent.EXTRA_TEXT, tweet);
			startService(i);
			
			Toast
				.makeText(this, "Your tweet is on its way!", Toast.LENGTH_LONG)
				.show();
		}
		
		finish();
	}
}
