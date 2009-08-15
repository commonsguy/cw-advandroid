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

package com.commonsware.android.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CompassDemo extends Activity {
	private SensorManager mgr=null;
	private TextView degrees=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		degrees=(TextView)findViewById(R.id.degrees);

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
	
	private SensorEventListener listener=new SensorEventListener() {
		public void onSensorChanged(SensorEvent e) {
			if (e.sensor.getType()==Sensor.TYPE_ORIENTATION) {
				degrees.setText(String.valueOf(e.values[0]));
			}
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// unused
		}
	};
}
