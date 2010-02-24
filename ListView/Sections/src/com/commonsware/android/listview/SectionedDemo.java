/***
	Copyright (c) 2008-2010 CommonsWare, LLC
	
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

package com.commonsware.android.listview;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SectionedDemo extends ListActivity {
	private static String[] items={"lorem", "ipsum", "dolor",
																	"sit", "amet", "consectetuer",
																	"adipiscing", "elit", "morbi",
																	"vel", "ligula", "vitae",
																	"arcu", "aliquet", "mollis",
																	"etiam", "vel", "erat",
																	"placerat", "ante",
																	"porttitor", "sodales",
																	"pellentesque", "augue",
																	"purus"};
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		
		adapter.addSection("Original",
												new ArrayAdapter<String>(this,
													android.R.layout.simple_list_item_1,
													items));
		
		List<String> list=Arrays.asList(items);
		
		Collections.shuffle(list);

		adapter.addSection("Shuffled",
												new ArrayAdapter<String>(this,
													android.R.layout.simple_list_item_1,
													list));
		
		list=Arrays.asList(items);
		
		Collections.shuffle(list);

		adapter.addSection("Re-shuffled",
												new ArrayAdapter<String>(this,
													android.R.layout.simple_list_item_1,
													list));
		
		setListAdapter(adapter);
	}
	
	SectionedAdapter adapter=new SectionedAdapter() {
		protected View getHeaderView(String caption, int index,
																	View convertView,
																	ViewGroup parent) {
			TextView result=(TextView)convertView;
			
			if (convertView==null) {
				result=(TextView)getLayoutInflater()
																	.inflate(R.layout.header,
																					 null);
			}
			
			result.setText(caption);
			
			return(result);
		}
	};
}
