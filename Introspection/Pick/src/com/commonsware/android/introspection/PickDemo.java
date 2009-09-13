/***
	Copyright (c) 2008-2009 CommonsWare, LLC
	
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

package com.commonsware.android.introspection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class PickDemo extends Activity {
	static final int PICK_REQUEST=1337;
	private EditText type;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		type=(EditText)findViewById(R.id.type);
		
		Button btn=(Button)findViewById(R.id.pick);
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent i=new Intent(Intent.ACTION_PICK,
										Uri.parse(type.getText().toString()));

				startActivityForResult(i, PICK_REQUEST);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
																		Intent data) {
		if (requestCode==PICK_REQUEST) {
			if (resultCode==RESULT_OK) {
					startActivity(new Intent(Intent.ACTION_VIEW,
																		data.getData()));
			}
		}
	}
}