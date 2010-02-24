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

package com.commonsware.android.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class SteeringDemo extends Activity {
	private SensorManager mgr=null;
	private float prevOrientation=0.0f;
	private TextView transcript=null;
	private ScrollView scroll=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		transcript=(TextView)findViewById(R.id.transcript);
		scroll=(ScrollView)findViewById(R.id.scroll);

		mgr=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mgr.registerListener(listener,
													mgr.getDefaultSensor(Sensor.TYPE_ORIENTATION),
													SensorManager.SENSOR_DELAY_UI);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mgr.unregisterListener(listener);
	}
	
	private void steerLeft(float position, float delta) {
		StringBuffer line=new StringBuffer("Steered left by ");
		
		line.append(String.valueOf(delta));
		line.append(" to ");
		line.append(String.valueOf(position));
		line.append("\n");
		transcript.setText(transcript.getText().toString()+line.toString());
		scroll.fullScroll(View.FOCUS_DOWN);
	}
	
	private void steerRight(float position, float delta) {
		StringBuffer line=new StringBuffer("Steered right by ");
		
		line.append(String.valueOf(delta));
		line.append(" to ");
		line.append(String.valueOf(position));
		line.append("\n");
		transcript.setText(transcript.getText().toString()+line.toString());
		scroll.fullScroll(View.FOCUS_DOWN);
	}
	
	private SensorEventListener listener=new SensorEventListener() {
		public void onSensorChanged(SensorEvent e) {
			if (e.sensor.getType()==Sensor.TYPE_ORIENTATION) {
				float orientation=e.values[1];
				
				if (prevOrientation!=orientation) {
					if (prevOrientation<orientation) {
						steerLeft(orientation,
											orientation-prevOrientation);
					}
					else {
						steerRight(orientation,
												prevOrientation-orientation);
					}
					
					prevOrientation=e.values[1];
				}
			}
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// unused
		}
	};
}
