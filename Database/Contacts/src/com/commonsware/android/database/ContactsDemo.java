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

package com.commonsware.android.database;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class ContactsDemo extends ListActivity
	implements AdapterView.OnItemSelectedListener {
	private static String[] options={"Contact Names",
																		"Contact Names & Numbers",
																		"Contact Names & Email Addresses"};
	private ListAdapter[] listAdapters=new ListAdapter[3];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		initListAdapters();
		
		Spinner spin=(Spinner)findViewById(R.id.spinner);
		spin.setOnItemSelectedListener(this);
		
		ArrayAdapter<String> aa=new ArrayAdapter<String>(this,
															android.R.layout.simple_spinner_item,
															options);
		
		aa.setDropDownViewResource(
						android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);
	}
	
	public void onItemSelected(AdapterView<?> parent,
																View v, int position, long id) {
		setListAdapter(listAdapters[position]);
	}
	
	public void onNothingSelected(AdapterView<?> parent) {
		// ignore
	}
	
	private void initListAdapters() {
		listAdapters[0]=buildNameAdapter();
		listAdapters[1]=buildPhonesAdapter();
		listAdapters[2]=buildEmailAdapter();
	}
	
	private ListAdapter buildNameAdapter() {
		String[] PROJECTION=new String[] { 	Contacts.People._ID,
																				Contacts.PeopleColumns.NAME
																			};
		Cursor c=managedQuery(Contacts.People.CONTENT_URI,
													PROJECTION, null, null,
													Contacts.People.DEFAULT_SORT_ORDER);
		
		return(new SimpleCursorAdapter(	this,
																		android.R.layout.simple_list_item_1,
																		c,
																		new String[] {
																			Contacts.PeopleColumns.NAME
																		},
																		new int[] {
																			android.R.id.text1
																		}));
	}
	
	private ListAdapter buildPhonesAdapter() {
		String[] PROJECTION=new String[] { 	Contacts.Phones._ID,
																				Contacts.Phones.NAME,
																				Contacts.Phones.NUMBER
																			};
		Cursor c=managedQuery(Contacts.Phones.CONTENT_URI,
													PROJECTION, null, null,
													Contacts.Phones.DEFAULT_SORT_ORDER);
		
		return(new SimpleCursorAdapter(	this,
																		android.R.layout.simple_list_item_2,
																		c,
																		new String[] {
																			Contacts.Phones.NAME,
																			Contacts.Phones.NUMBER
																		},
																		new int[] {
																			android.R.id.text1,
																			android.R.id.text2
																		}));
	}
	
	private ListAdapter buildEmailAdapter() {
		String[] PROJECTION=new String[] { 	Contacts.ContactMethods._ID,
																				Contacts.ContactMethods.DATA,
																				Contacts.PeopleColumns.NAME
																			};
		Cursor c=managedQuery(Contacts.ContactMethods.CONTENT_EMAIL_URI,
													PROJECTION, null, null,
													Contacts.ContactMethods.DEFAULT_SORT_ORDER);
		
		return(new SimpleCursorAdapter(	this,
																		android.R.layout.simple_list_item_2,
																		c,
																		new String[] {
																			Contacts.PeopleColumns.NAME,
																			Contacts.ContactMethods.DATA
																		},
																		new int[] {
																			android.R.id.text1,
																			android.R.id.text2
																		}));
	}
}
