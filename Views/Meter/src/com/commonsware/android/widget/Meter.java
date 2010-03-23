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
	 
package com.commonsware.android.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.LinearLayout;

public class Meter extends LinearLayout {
	private int max=100;
	private int incrAmount=1;
	private int decrAmount=-1;
	private ProgressBar bar=null;
	private View.OnClickListener onIncr=null;
	private View.OnClickListener onDecr=null;
	
	public Meter(final Context ctxt, AttributeSet attrs) {
		super(ctxt, attrs);
		
		this.setOrientation(HORIZONTAL);
		
		TypedArray a=ctxt.obtainStyledAttributes(attrs,
																							R.styleable.Meter,
																							0, 0);
		
		max=a.getInt(R.styleable.Meter_max, 100);
		incrAmount=a.getInt(R.styleable.Meter_incr, 1);
		decrAmount=-1*a.getInt(R.styleable.Meter_decr, 1);
		
		a.recycle();
	}
	
	public void setOnIncrListener(View.OnClickListener onIncr) {
		this.onIncr=onIncr;
	}
	
	public void setOnDecrListener(View.OnClickListener onDecr) {
		this.onDecr=onDecr;
	}
	
	public void setProgress(int progress) {
		bar.setProgress(progress);
	}
	
	public void setMax(int max) {
		this.max=max;
		bar.setMax(max);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		((Activity)getContext()).getLayoutInflater().inflate(R.layout.meter, this);
		
		bar=(ProgressBar)findViewById(R.id.bar);
		bar.setMax(max);
		
		ImageButton btn=(ImageButton)findViewById(R.id.incr);
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				bar.incrementProgressBy(incrAmount);
				
				if (onIncr!=null) {
					onIncr.onClick(Meter.this);
				}
			}
		});
		
		btn=(ImageButton)findViewById(R.id.decr);
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				bar.incrementProgressBy(decrAmount);
				
				if (onDecr!=null) {
					onDecr.onClick(Meter.this);
				}
			}
		});
	}
}
