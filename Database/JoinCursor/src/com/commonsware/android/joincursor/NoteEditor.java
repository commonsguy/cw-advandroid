/* Copyright (c) 2008 -- CommonsWare, LLC

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
	 
package com.commonsware.android.joincursor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NoteEditor extends Activity {
	TextView note=null;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.edit);
		note=(TextView)findViewById(R.id.text);
		
		String prose=getIntent().getStringExtra(JoinDemo.NOTE);
		
		if (prose!=null) {
			note.setText(prose);
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent i=new Intent(getIntent());
		
		i.putExtra(JoinDemo.NOTE, note.getText().toString());
		
		setResult(0, i);
	}
}
